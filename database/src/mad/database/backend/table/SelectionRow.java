package mad.database.backend.table;

import java.io.IOException;
import java.util.function.Predicate;

/**
 *
 */
public class SelectionRow implements Row {

    Row row;
    Predicate<Row> filter;

    /**
     *
     * @param row
     * @param filter
     * @throws EmptySelection
     */
    private SelectionRow(Row row, Predicate filter) throws EmptySelection {
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
     */
    public static Row getFirstMatchingRow(Row row, Predicate<Row> filter) {
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
        return getFirstMatchingRow(row.next(), filter);
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
    public int columns() {
        return row.columns();
    }

    @Override
    public String getTableName() throws IOException {
        return row.getTableName();
    }

    /**
     *
     */
    private static class EmptySelection extends Exception {
    }
}
