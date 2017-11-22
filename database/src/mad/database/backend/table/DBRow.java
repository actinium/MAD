package mad.database.backend.table;

import java.io.IOException;
import mad.database.backend.Pager;
import mad.util.Bytes;

/**
 *
 */
public class DBRow implements Row {

    private final Pager pager;
    private final Schema tableSchema;
    private final int filePosition;
    private final String tableName;

    public DBRow(Pager pager, Schema tableSchema, String tableName, int filePosition) {
        this.pager = pager;
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        this.filePosition = filePosition;
    }

    @Override
    public Row next() {
        try {
            int nextRowPointer = pager.readInteger(filePosition + 4);
            if (nextRowPointer != 0) {
                return new DBRow(pager, tableSchema, tableName, nextRowPointer);
            }
        } catch (IOException ex) {
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        try {
            int nextRowPointer = pager.readInteger(filePosition + 4);
            if (nextRowPointer != 0) {
                return true;
            }
        } catch (IOException ex) {
        }
        return false;
    }

    @Override
    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Integer) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readInteger(position);
        }
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return getInteger(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Float) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readFloat(position);
        }
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return getFloat(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Boolean) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readBoolean(position);
        }
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return getBoolean(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Varchar) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readString(position, tableSchema.get(columnNumber).length);
        }
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return getString(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public String getName(int columnNumber) throws NoSuchColumnException, IOException {
        return tableSchema.get(columnNumber).name;
    }

    @Override
    public boolean isNull(int columnNumber) throws NoSuchColumnException, IOException {
        return Bytes.toNullBitMap(pager.readBytes(filePosition + 8, 8)).isNull(columnNumber);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException {
        return isNull(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException, IOException {
        return tableSchema.get(columnNumber).type;
    }

    @Override
    public int size() {
        return tableSchema.size();
    }
    
    @Override
    public String getTableName(){
        return tableName;
    }

}
