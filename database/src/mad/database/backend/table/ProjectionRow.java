package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public class ProjectionRow implements Row {

    Row row;
    int[] columnMapping;

    private ProjectionRow(Row row, int[] columnMapping) {
        this.row = row;
        this.columnMapping = columnMapping;
    }

    public static ProjectionRow getFirstProjectionRow(Row row, int[] columnMapping) throws NoSuchColumnException{
        for (int i = 0; i < columnMapping.length; i++) {
            if (columnMapping[i] < 0 || columnMapping[i] > row.columns()) {
                throw new NoSuchColumnException("Projection references non-existing column!");
            }
        }
        return new ProjectionRow(row, columnMapping);
    }

    @Override
    public Row next() {
        Row nextRow = row.next();
        if(nextRow == null){
            return null;
        }
        return new ProjectionRow(row.next(), columnMapping);
    }

    @Override
    public boolean hasNext() {
        return row.hasNext();
    }

    @Override
    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getInteger(columnMapping[columnNumber]);
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getInteger(columnName);
    }

    @Override
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getFloat(columnMapping[columnNumber]);
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getFloat(columnName);
    }

    @Override
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getBoolean(columnMapping[columnNumber]);
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getBoolean(columnName);
    }

    @Override
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getString(columnMapping[columnNumber]);
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return row.getString(columnName);
    }

    @Override
    public boolean isNull(int columnNumber) throws NoSuchColumnException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.isNull(columnMapping[columnNumber]);
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException {
        return row.isNull(columnName);
    }

    @Override
    public String getName(int columnNumber) throws NoSuchColumnException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getName(columnMapping[columnNumber]);
    }

    @Override
    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException, IOException {
        if (columnNumber > columnMapping.length) {
            throw new NoSuchColumnException();
        }
        return row.getType(columnMapping[columnNumber]);
    }

    @Override
    public int columns() {
        return columnMapping.length;
    }

    @Override
    public String getTableName() throws IOException {
        return row.getTableName();
    }

}
