package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public interface Row {

    Row copy();
    /**
     *
     * @return
     */
    Row next();

    /**
     *
     * @return
     */
    boolean hasNext();

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    String getString(String columnName) throws NoSuchColumnException, TypeMismatchException,
            IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    boolean isNull(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    boolean isNull(String columnName) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws IOException
     */
    boolean hasColumn(String columnName) throws IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    String getName(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    Schema.Field.Type getType(String columnName) throws NoSuchColumnException, IOException;


    /**
     *
     * @param columnNumber
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    String getTableName(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @return
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    String getTableName(String columnName) throws NoSuchColumnException, IOException;

    /**
     *
     * @return
     */
    int columns();

    /**
     *
     */
    static class NoSuchColumnException extends Exception {

        public NoSuchColumnException() {
            super();
        }

        public NoSuchColumnException(String message) {
            super(message);
        }
    }

    /**
     *
     */
    static class TypeMismatchException extends Exception {

        public TypeMismatchException(String message) {
            super(message);
        }
    }
}
