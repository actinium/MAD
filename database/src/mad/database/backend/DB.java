package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import mad.util.Bytes;

/**
 *
 */
public class DB implements AutoCloseable {

    private Pager pager;
    private SchemaWriter schemaWriter;
    private SchemaReader schemaReader;

    private DB(String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
        if (file.exists() && (!file.canRead() || !file.canWrite())) {
            throw new IOException("Can not access file: " + filename + "!");
        }
        if (!file.exists() || file.length() == 0) {
            initDBFile(file);
        }
        pager = new Pager(file);
        schemaWriter = new SchemaWriter(pager);
        schemaReader = new SchemaReader(pager);
    }

    /**
     *
     * @param filename
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static DB open(String filename) throws FileNotFoundException, IOException {
        return new DB(filename);
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        pager.close();
    }

    /**
     *
     * @param name
     * @param schema
     * @throws IOException
     */
    public void createTable(String name, Schema schema) throws IOException {
        int firstTablePointer = pager.readInteger(0);
        int lastTablePointer = pager.readInteger(4);
        int newTablePointer = pager.newPage();

        byte[] nameAsBytes = Bytes.fromString(name);
        int tableHeaderLength = 5 * 4 + nameAsBytes.length;
        byte[] table = new byte[tableHeaderLength];
        ByteBuffer tableBuffer = Bytes.getByteBuffer(table);

        int schemaPointer = newTablePointer + tableHeaderLength;
        tableBuffer.putInt(4, schemaPointer);
        schemaWriter.write(schemaPointer, schema);

        tableBuffer.putInt(16, nameAsBytes.length);
        tableBuffer.position(20).mark();
        tableBuffer.put(nameAsBytes, 0, nameAsBytes.length);

        pager.writeBytes(newTablePointer, table, table.length);
        if (firstTablePointer == 0) {
            pager.writeInteger(0, newTablePointer);
        } else {
            pager.writeInteger(lastTablePointer + 0, newTablePointer);
        }
        pager.writeInteger(4, newTablePointer);
    }

    /**
     *
     * @return @throws IOException
     */
    public List<String> getTableNames() throws IOException {
        List<String> tables = new ArrayList<>();
        for (int tablePointer = pager.readInteger(0); tablePointer != 0;
                tablePointer = pager.readInteger(tablePointer)) {
            int nameLength = pager.readInteger(tablePointer + 16);
            String name = pager.readString(tablePointer + 20, nameLength);
            tables.add(name);
        }
        return tables;
    }

    /**
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public int getTablePointer(String tableName) throws IOException {
        for (int tablePointer = pager.readInteger(0); tablePointer != 0;
                tablePointer = pager.readInteger(tablePointer)) {
            int nameLength = pager.readInteger(tablePointer + 16);
            String name = pager.readString(tablePointer + 20, nameLength);
            if (name.equals(tableName)) {
                return tablePointer;
            }
        }
        return 0;
    }

    /**
     *
     * @param tableName
     * @return
     * @throws IOException
     */
    public Schema getSchema(String tableName) throws IOException {
        for (int tablePointer = pager.readInteger(0); tablePointer != 0;
                tablePointer = pager.readInteger(tablePointer)) {
            int nameLength = pager.readInteger(tablePointer + 16);
            String name = pager.readString(tablePointer + 20, nameLength);
            if (name.equals(tableName)) {
                int scheamPointer = pager.readInteger(tablePointer + 4);
                return schemaReader.read(scheamPointer);
            }
        }
        return null;
    }

    // - Alter Table
    // - Truncate Table
    // - Drop Table
    //
    /**
     *
     * @param tableFilePosition
     * @param row
     * @throws IOException
     * @throws mad.database.backend.Row.TypeMismatchException
     */
    public void insertRow(int tableFilePosition, Row row) throws IOException, Row.TypeMismatchException {
        int schemaPointer = pager.readInteger(tableFilePosition + 4);
        Schema tableSchema = schemaReader.read(schemaPointer);
        RowWriter writer = new RowWriter(pager, tableSchema);
        int lastRowPointer = pager.readInteger(tableFilePosition + 12);

        if (lastRowPointer != 0) {
            int pagePointer = pager.positionToPageStart(lastRowPointer);
            int numOfRowsInPage = (lastRowPointer - pagePointer) / tableSchema.bytes();
            if (numOfRowsInPage == tableSchema.rowsPerPage()) {
                pagePointer = pager.newPage();
                writer.setNextRowPointer(lastRowPointer, pagePointer);
                writer.write(pagePointer, row);
                pager.writeInteger(tableFilePosition + 12, pagePointer);
            } else {
                writer.setNextRowPointer(lastRowPointer, lastRowPointer + tableSchema.bytes());
                writer.write(lastRowPointer + tableSchema.bytes(), row);
                pager.writeInteger(tableFilePosition + 12, lastRowPointer + tableSchema.bytes());
            }
        } else {
            int pagePointer = pager.newPage();
            writer.write(pagePointer, row);
            pager.writeInteger(tableFilePosition + 8, pagePointer);
            pager.writeInteger(tableFilePosition + 12, pagePointer);
        }

    }

    /**
     *
     * @param tableName
     * @param row
     * @throws IOException
     * @throws mad.database.backend.Row.TypeMismatchException
     */
    public void insertRow(String tableName, Row row) throws IOException, Row.TypeMismatchException {
        insertRow(getTablePointer(tableName), row);
    }

    /**
     *
     * @param tableFilePosition
     * @return
     * @throws IOException
     */
    public Row getFirstRow(int tableFilePosition) throws IOException {
        int rowPointer = pager.readInteger(tableFilePosition + 8);
        if (rowPointer == 0) {
            return null;
        }
        int schemaPointer = pager.readInteger(tableFilePosition + 4);
        Schema schema = schemaReader.read(schemaPointer);
        return new DBRow(pager, schema, rowPointer);
    }

    // - Update Row/Rows
    // - Delete Row/Rows
    // 
    // - Select
    /**
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void initDBFile(File file) throws FileNotFoundException, IOException {
        int dbHeaderSize = 12;
        try (FileOutputStream writer = new FileOutputStream(file)) {
            byte[] initBytes = new byte[dbHeaderSize];
            writer.write(initBytes);
        }
    }

}
