package mad.database.backend;

import java.util.ArrayList;

/**
 *
 */
public class ArrayRow implements Row {

    ArrayList<Cell> row = new ArrayList<>();

    public void addIntegerColumn(String columnName,int value){
        row.add(new IntegerCell(columnName, value));
    }
    
    public void addFloatColumn(String columnName,float value){
        row.add(new FloatCell(columnName, value));
    }
    
    public void addBooleanColumn(String columnName,boolean value){
        row.add(new BooleanCell(columnName, value));
    }
    
    public void addStringColumn(String columnName,String value){
        row.add(new StringCell(columnName, value));
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
        if(columnNumber < 0 || columnNumber>=row.size()){
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getInteger();
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for(Cell c : row){
            if(c.getColumnName().equals(columnName)){
                return c.getInteger();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if(columnNumber < 0 || columnNumber>=row.size()){
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getFloat();
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for(Cell c : row){
            if(c.getColumnName().equals(columnName)){
                return c.getFloat();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if(columnNumber < 0 || columnNumber>=row.size()){
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getBoolean();
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for(Cell c : row){
            if(c.getColumnName().equals(columnName)){
                return c.getBoolean();
            }
        }
        throw new NoSuchColumnException();
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException {
        if(columnNumber < 0 || columnNumber>=row.size()){
            throw new NoSuchColumnException();
        }
        return row.get(columnNumber).getString();
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException {
        for(Cell c : row){
            if(c.getColumnName().equals(columnName)){
                return c.getString();
            }
        }
        throw new NoSuchColumnException();
    }

    private static abstract class Cell {

        protected final String columnName;

        public Cell(String columnName) {
            this.columnName = columnName;
        }
        
        public String getColumnName(){
            return columnName;
        }

        public abstract int getInteger() throws TypeMismatchException;

        public abstract float getFloat() throws TypeMismatchException;

        public abstract boolean getBoolean() throws TypeMismatchException;

        public abstract String getString() throws TypeMismatchException;

    }

    private static class IntegerCell extends Cell {

        private final int value;

        public IntegerCell(String columnName, int value) {
            super(columnName);
            this.value = value;
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

    }

    private static class FloatCell extends Cell {

        private final float value;

        public FloatCell(String columnName, float value) {
            super(columnName);
            this.value = value;
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

    }

    private static class BooleanCell extends Cell {

        private final boolean value;

        public BooleanCell(String columnName, boolean value) {
            super(columnName);
            this.value = value;
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

    }

    private static class StringCell extends Cell {

        private final String value;

        public StringCell(String columnName, String value) {
            super(columnName);
            this.value = value;
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

    }

}
