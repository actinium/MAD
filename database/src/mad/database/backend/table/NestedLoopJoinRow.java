package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public class NestedLoopJoinRow implements Row {

    private final Row leftRow;
    private final Row rightRow;
    private final Row rightRowFirst;

    public NestedLoopJoinRow(Row left, Row right, Row rightRowFirst) {
        this.leftRow = left;
        this.rightRow = right;
        this.rightRowFirst = rightRowFirst;
    }

    @Override
    public Row next() {
        if (rightRow.hasNext()) {
            return new NestedLoopJoinRow(leftRow, rightRow.next(), rightRowFirst);
        } else if (leftRow.hasNext()) {
            return new NestedLoopJoinRow(leftRow.next(),rightRowFirst,rightRowFirst);
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return leftRow.hasNext() || rightRow.hasNext();
    }

    @Override
    public int getInteger(int columnNum) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnNum).getInteger(columnNumber(columnNum));
    }

    @Override
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnName).getInteger(columnName);
    }

    @Override
    public float getFloat(int columnNum) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnNum).getFloat(columnNumber(columnNum));
    }

    @Override
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnName).getFloat(columnName);
    }

    @Override
    public boolean getBoolean(int columnNum) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnNum).getBoolean(columnNumber(columnNum));
    }

    @Override
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnName).getBoolean(columnName);
    }

    @Override
    public String getString(int columnNum) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnNum).getString(columnNumber(columnNum));
    }

    @Override
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException {
        return columnRow(columnName).getString(columnName);
    }

    @Override
    public boolean isNull(int columnNum) throws NoSuchColumnException, IOException {
        return columnRow(columnNum).isNull(columnNumber(columnNum));
    }

    @Override
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException {
        return columnRow(columnName).isNull(columnName);
    }

    @Override
    public boolean hasColumn(String columnName) throws IOException {
        return leftRow.hasColumn(columnName) || rightRow.hasColumn(columnName);
    }

    @Override
    public String getName(int columnNum) throws NoSuchColumnException, IOException {
        return columnRow(columnNum).getName(columnNumber(columnNum));
    }

    @Override
    public Schema.Field.Type getType(int columnNum) throws NoSuchColumnException, IOException {
        return columnRow(columnNum).getType(columnNumber(columnNum));
    }

    @Override
    public Schema.Field.Type getType(String columnName) throws NoSuchColumnException, IOException {
        return columnRow(columnName).getType(columnName);
    }

    @Override
    public String getTableName(int columnNum) throws NoSuchColumnException, IOException {
        return columnRow(columnNum).getTableName(columnNumber(columnNum));
    }

    @Override
    public String getTableName(String columnName) throws NoSuchColumnException, IOException {
        return columnRow(columnName).getTableName(columnName);
    }

    @Override
    public int columns() {
        return leftRow.columns() + rightRow.columns();
    }

    /**
     *
     * @param columnNum
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     */
    private Row columnRow(int columnNum) throws NoSuchColumnException {
        if (columnNum < columns()) {
            if (columnNum < leftRow.columns()) {
                return leftRow;
            } else {
                columnNum -= leftRow.columns();
                return rightRow;
            }
        } else {
            throw new NoSuchColumnException();
        }
    }

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException
     */
    private Row columnRow(String columnName) throws NoSuchColumnException, IOException {
        boolean leftHasColumn = leftRow.hasColumn(columnName);
        boolean rightHasColumn = rightRow.hasColumn(columnName);
        if (leftHasColumn && rightHasColumn) {

        } else if (leftHasColumn) {
            return leftRow;
        } else if (rightHasColumn) {
            return rightRow;
        }
        throw new NoSuchColumnException();
    }

    /**
     *
     * @param columnNum
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     */
    private int columnNumber(int columnNum) throws NoSuchColumnException {
        if (columnNum < columns()) {
            if (columnNum < leftRow.columns()) {
                return columnNum;
            } else {
                columnNum -= leftRow.columns();
                return columnNum;
            }
        } else {
            throw new NoSuchColumnException();
        }
    }

}
