package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public class NamedRow implements Row {

    private final Row row;
    private final String alias;

    public NamedRow(Row row, String alias){
        this.row = row;
        this.alias = alias;
    }

    @Override
    public Row copy() {
        return new NamedRow(row.copy(), alias);
    }

    @Override
    public Row next() {
        return new NamedRow(row.next(), alias);
    }

    @Override
    public boolean hasNext() {
        return row.hasNext();
    }

    @Override
    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getInteger(columnNumber);
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getInteger(columnName);
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getFloat(columnNumber);
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getFloat(columnName);
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getBoolean(columnNumber);
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getBoolean(columnName);
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getString(columnNumber);
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getString(columnName);
    }

    @Override
    public boolean isNull(int columnNumber) throws NoSuchColumnException, IOException {
        return row.isNull(columnNumber);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException {
        return row.isNull(columnName);
    }

    @Override
    public boolean hasColumn(String columnName) throws IOException {
        return row.hasColumn(columnName);
    }

    @Override
    public String getName(int columnNumber) throws NoSuchColumnException, IOException {
        return row.getName(columnNumber);
    }

    @Override
    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException, IOException {
        return row.getType(columnNumber);
    }

    @Override
    public Schema.Field.Type getType(String columnName) throws NoSuchColumnException, IOException {
        return row.getType(columnName);
    }

    @Override
    public String getTableName(int columnNumber) throws NoSuchColumnException, IOException {
        return alias;
    }

    @Override
    public String getTableName(String columnName) throws NoSuchColumnException, IOException {
        return alias;
    }

    @Override
    public int columns() {
        return row.columns();
    }

}
