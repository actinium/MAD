package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public interface Row {

    /**
     *
     * @return
     */
    public Row next();

    /**
     *
     * @return
     */
    public boolean hasNext();

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws IOException
     */
    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException
     */
    public boolean isNull(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException
     */
    public boolean isNull(String columnName) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException
     */
    public String getName(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws mad.database.backend.table.Row.NoSuchColumnException
     * @throws IOException
     */
    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @return
     */
    public int size();

    /**
     *
     * @return @throws IOException
     */
    public String getTableName() throws IOException;

    /**
     *
     */
    public static class NoSuchColumnException extends Exception {
    }

    /**
     *
     */
    public static class TypeMismatchException extends Exception {

        public TypeMismatchException(String message) {
            super(message);
        }
    }
}
