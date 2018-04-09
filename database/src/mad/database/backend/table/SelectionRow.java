package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public class SelectionRow implements Row {

    Row row;
    RowPredicate filter;

    /**
     *
     * @param row
     * @param filter
     * @throws SelectionRow.EmptySelection
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    private SelectionRow(Row row, RowPredicate filter) throws EmptySelection, NoSuchColumnException,
            TypeMismatchException, IOException {
        this.filter = filter;
        this.row = row;
        while (!filter.test(this.row)) {
            if (!this.row.hasNext()) {
                throw new EmptySelection();
            }
            this.row = this.row.next();
        }
    }

    /**
     *
     * @param row
     * @param filter
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws java.io.IOException
     */
    public static Row getFirstMatchingRow(Row row, RowPredicate filter) throws
            NoSuchColumnException, TypeMismatchException, IOException {
        if (row == null) {
            return null;
        }
        try {
            return new SelectionRow(row, filter);
        } catch (EmptySelection es) {
            return null;
        }
    }

    @Override
    public Row next() {
        try {
            return getFirstMatchingRow(row.next(), filter);
        } catch (NoSuchColumnException | TypeMismatchException | IOException ex) {
            return null;
        }
    }

    @Override
    public boolean hasNext() {
        return next() != null;
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
        return row.getTableName(columnNumber);
    }

    @Override
    public String getTableName(String columnName) throws NoSuchColumnException, IOException {
        return row.getTableName(columnName);
    }

    @Override
    public int columns() {
        return row.columns();
    }

    /**
     *
     */
    private static class EmptySelection extends Exception {
    }
}
