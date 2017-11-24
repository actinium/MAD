package mad.database.backend.table;

import java.io.IOException;

/**
 *
 */
public interface WritableRow extends Row {

    /**
     *
     * @return
     */
    @Override
    WritableRow next();

    /**
     *
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setInteger(int columnNumber, int value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setInteger(String columnName, int value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setFloat(int columnNumber, float value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setFloat(String columnName, float value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setBoolean(int columnNumber, boolean value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setBoolean(String columnName, boolean value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setString(int columnNumber, String value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnName
     * @param Value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException
     */
    void setString(String columnName, String Value) throws NoSuchColumnException,
            TypeMismatchException, IOException;

    /**
     *
     * @param columnNumber
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    void setNull(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    void setNull(String columnName) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnNumber
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    void setNotNull(int columnNumber) throws NoSuchColumnException, IOException;

    /**
     *
     * @param columnName
     * @throws Row.NoSuchColumnException
     * @throws IOException
     */
    void setNotNull(String columnName) throws NoSuchColumnException, IOException;

}
