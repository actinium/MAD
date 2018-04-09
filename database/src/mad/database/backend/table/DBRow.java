package mad.database.backend.table;

import java.io.IOException;
import mad.database.backend.Pager;
import mad.util.Bytes;
import mad.util.NullBitMap;

/**
 *
 */
public class DBRow implements Row, WritableRow, DeleteableRow {

    private final Pager pager;
    private final Schema tableSchema;
    private final int filePosition;
    private final String tableName;

    /**
     *
     * @param pager
     * @param tableSchema
     * @param tableName
     * @param filePosition
     */
    public DBRow(Pager pager, Schema tableSchema, String tableName, int filePosition) {
        this.pager = pager;
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        this.filePosition = filePosition;
    }

    /**
     *
     * @return
     */
    @Override
    public WritableRow next() {
        try {
            int nextRowPointer = pager.readInteger(filePosition + 4);
            if (nextRowPointer != 0) {
                return new DBRow(pager, tableSchema, tableName, nextRowPointer);
            }
        } catch (IOException ex) {
        }
        return null;
    }

    /**
     *
     * @return
     */
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
    public WritableRow previous() {
        try {
            int nextRowPointer = pager.readInteger(filePosition);
            if (nextRowPointer != 0) {
                return new DBRow(pager, tableSchema, tableName, nextRowPointer);
            }
        } catch (IOException ex) {
        }
        return null;
    }

    @Override
    public boolean hasPrevious() {
        try {
            int nextRowPointer = pager.readInteger(filePosition);
            if (nextRowPointer != 0) {
                return true;
            }
        } catch (IOException ex) {
        }
        return false;
    }

    @Override
    public int getInteger(int columnNumber) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Integer) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readInteger(position);
        }
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        return getInteger(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Float) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readFloat(position);
        }
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        return getFloat(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Boolean) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readBoolean(position);
        }
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        return getBoolean(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Varchar) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            return pager.readString(position, tableSchema.get(columnNumber).length);
        }
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException,
            TypeMismatchException, IOException {
        return getString(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public boolean isNull(int columnNumber) throws NoSuchColumnException,
            IOException {
        return Bytes.toNullBitMap(pager.readBytes(filePosition + 8, 8))
                .isNull(columnNumber);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException,
            IOException {
        return isNull(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public void setInteger(int columnNumber, int value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Integer) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            pager.writeInteger(position, value);
        }
    }

    @Override
    public void setInteger(String columnName, int value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        setInteger(tableSchema.get(columnName).columnNumber, value);
    }

    @Override
    public void setFloat(int columnNumber, float value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Float) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            pager.writeFloat(position, value);
        }
    }

    @Override
    public void setFloat(String columnName, float value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        setFloat(tableSchema.get(columnName).columnNumber, value);
    }

    @Override
    public void setBoolean(int columnNumber, boolean value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Integer) {
            throw new TypeMismatchException("");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            pager.writeBoolean(position, value);
        }
    }

    @Override
    public void setBoolean(String columnName, boolean value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        setBoolean(tableSchema.get(columnName).columnNumber, value);
    }

    @Override
    public void setString(int columnNumber, String value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        if (tableSchema.get(columnNumber).type != Schema.Field.Type.Varchar) {
            throw new TypeMismatchException("");
        } else if (Bytes.fromString(value).length
                > tableSchema.get(columnNumber).length) {
            throw new TypeMismatchException("String does not fit in varchar!");
        } else {
            int position = filePosition + tableSchema.get(columnNumber).offset;
            int length = tableSchema.get(columnNumber).length;
            pager.writeString(position, value, length);
        }
    }

    @Override
    public void setString(String columnName, String value) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        setString(tableSchema.get(columnName).columnNumber, value);
    }

    @Override
    public void setNull(int columnNumber) throws NoSuchColumnException,
            IOException {
        byte[] nullMapBytes = pager.readBytes(filePosition + 8, 8);
        NullBitMap nullMap = Bytes.toNullBitMap(nullMapBytes);
        nullMap.setNull(columnNumber);
        nullMapBytes = Bytes.fromNullBitMap(nullMap);
        pager.writeBytes(filePosition + 8, nullMapBytes, 8);
    }

    @Override
    public void setNull(String columnName) throws NoSuchColumnException,
            IOException {
        setNull(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public void setNotNull(int columnNumber) throws NoSuchColumnException,
            IOException {
        byte[] nullMapBytes = pager.readBytes(filePosition + 8, 8);
        NullBitMap nullMap = Bytes.toNullBitMap(nullMapBytes);
        nullMap.setNotNull(columnNumber);
        nullMapBytes = Bytes.fromNullBitMap(nullMap);
        pager.writeBytes(filePosition + 8, nullMapBytes, 8);
    }

    @Override
    public void setNotNull(String columnName) throws NoSuchColumnException,
            IOException {
        setNotNull(tableSchema.get(columnName).columnNumber);
    }

    @Override
    public String getName(int columnNumber) throws NoSuchColumnException,
            IOException {
        return tableSchema.get(columnNumber).name;
    }

    @Override
    public Schema.Field.Type getType(int columnNumber) throws
            NoSuchColumnException, IOException {
        return tableSchema.get(columnNumber).type;
    }

    @Override
    public Schema.Field.Type getType(String columnName) throws NoSuchColumnException, IOException {
        return tableSchema.get(columnName).type;
    }

    @Override
    public String getTableName(int columnNumber){
        return tableName;
    }

    @Override
    public String getTableName(String columnName){
        return tableName;
    }

    @Override
    public int columns() {
        return tableSchema.columns();
    }

}
