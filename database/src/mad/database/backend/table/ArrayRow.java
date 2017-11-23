package mad.database.backend.table;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class ArrayRow implements Row {

    ArrayList<Cell> row = new ArrayList<>();

    public ArrayRow addIntegerColumn(String columnName, int value) {
        row.add(new IntegerCell(columnName, value));
        return this;
    }

    public ArrayRow addFloatColumn(String columnName, float value) {
        row.add(new FloatCell(columnName, value));
        return this;
    }

    public ArrayRow addBooleanColumn(String columnName, boolean value) {
        row.add(new BooleanCell(columnName, value));
        return this;
    }

    public ArrayRow addStringColumn(String columnName, String value) {
        row.add(new StringCell(columnName, value));
        return this;
    }
    
    public ArrayRow addNullIntegerColumn(String columnName) {
        row.add(new IntegerCell(columnName));
        return this;
    }

    public ArrayRow addNullFloatColumn(String columnName) {
        row.add(new FloatCell(columnName));
        return this;
    }

    public ArrayRow addNullBooleanColumn(String columnName) {
        row.add(new BooleanCell(columnName));
        return this;
    }

    public ArrayRow addNullStringColumn(String columnName) {
        row.add(new StringCell(columnName));
        return this;
    }

    @Override
    public Row next() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getInteger();
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for (Cell c : row) {
            if (c.getColumnName().equals(columnName)) {
                return c.getInteger();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getFloat();
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for (Cell c : row) {
            if (c.getColumnName().equals(columnName)) {
                return c.getFloat();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getBoolean();
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for (Cell c : row) {
            if (c.getColumnName().equals(columnName)) {
                return c.getBoolean();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getString();
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for (Cell c : row) {
            if (c.getColumnName().equals(columnName)) {
                return c.getString();
            }
        }
        throw new NoSuchColumnException();
    }

    /**
     * 
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException 
     */
    @Override
    public boolean isNull(int columnNumber) throws NoSuchColumnException {
        return row.get(columnNumber).isNull();
    }

    /**
     * 
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException 
     */
    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException {
        for (Cell c : row) {
            if (c.getColumnName().equals(columnName)) {
                return c.isNull();
            }
        }
        throw new NoSuchColumnException();
    }
    
    /**
     * 
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException 
     */
    @Override
    public String getName(int columnNumber) throws NoSuchColumnException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).columnName;
    }

    /**
     * 
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException 
     */
    @Override
    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException {
        if (columnNumber < 0 || columnNumber >= row.size()) {
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getType();
    }

    /**
     * 
     * @return the number of columns in this row.
     */
    @Override
    public int columns() {
        return row.size();
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String getTableName(){
        return "";
    }

    private static abstract class Cell {

        protected final String columnName;
        protected final boolean isNull;

        public Cell(String columnName, boolean isNull) {
            this.columnName = columnName;
            this.isNull = isNull;
        }

        public String getColumnName() {
            return columnName;
        }

        public boolean isNull() {
            return isNull;
        }

        public abstract int getInteger() throws TypeMismatchException;

        public abstract float getFloat() throws TypeMismatchException;

        public abstract boolean getBoolean() throws TypeMismatchException;

        public abstract String getString() throws TypeMismatchException;

        public abstract Schema.Field.Type getType();

    }

    private static class IntegerCell extends Cell {

        private final int value;

        public IntegerCell(String columnName, int value) {
            super(columnName, false);
            this.value = value;
        }

        public IntegerCell(String columnName) {
            super(columnName, true);
            this.value = 0;
        }

        @Override
        public int getInteger() throws TypeMismatchException {
            return value;
        }

        @Override
        public float getFloat() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public boolean getBoolean() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public String getString() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public Schema.Field.Type getType() {
            return Schema.Field.Type.Integer;
        }

    }

    private static class FloatCell extends Cell {

        private final float value;

        public FloatCell(String columnName, float value) {
            super(columnName, false);
            this.value = value;
        }

        public FloatCell(String columnName) {
            super(columnName, true);
            this.value = 0;
        }

        @Override
        public int getInteger() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public float getFloat() throws TypeMismatchException {
            return value;
        }

        @Override
        public boolean getBoolean() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public String getString() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public Schema.Field.Type getType() {
            return Schema.Field.Type.Float;
        }

    }

    private static class BooleanCell extends Cell {

        private final boolean value;

        public BooleanCell(String columnName, boolean value) {
            super(columnName, false);
            this.value = value;
        }

        public BooleanCell(String columnName) {
            super(columnName, true);
            this.value = false;
        }

        @Override
        public int getInteger() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public float getFloat() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public boolean getBoolean() throws TypeMismatchException {
            return value;
        }

        public String getString() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public Schema.Field.Type getType() {
            return Schema.Field.Type.Boolean;
        }

    }

    private static class StringCell extends Cell {

        private final String value;

        public StringCell(String columnName, String value) {
            super(columnName, false);
            this.value = value;
        }

        public StringCell(String columnName) {
            super(columnName, true);
            this.value = null;
        }

        @Override
        public int getInteger() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public float getFloat() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public boolean getBoolean() throws TypeMismatchException {
            throw new TypeMismatchException("Requested type did not match actual type.");
        }

        @Override
        public String getString() throws TypeMismatchException {
            return value;
        }

        @Override
        public Schema.Field.Type getType() {
            return Schema.Field.Type.Varchar;
        }

    }

}
