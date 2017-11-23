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
    public WritableRow next();
    
    /**
     * 
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setInteger(int columnNumber, int value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setInteger(String columnName, int value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setFloat(int columnNumber, float value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setFloat(String columnName, float value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setBoolean(int columnNumber, boolean value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnName
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setBoolean(String columnName, boolean value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnNumber
     * @param value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setString(int columnNumber, String value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnName
     * @param Value
     * @throws Row.NoSuchColumnException
     * @throws Row.TypeMismatchException
     * @throws IOException 
     */
    public void setString(String columnName, String Value) throws
            NoSuchColumnException, TypeMismatchException, IOException;

    /**
     * 
     * @param columnNumber
     * @throws Row.NoSuchColumnException
     * @throws IOException 
     */
    public void setNull(int columnNumber) throws NoSuchColumnException,
            IOException;

    /**
     * 
     * @param columnName
     * @throws Row.NoSuchColumnException
     * @throws IOException 
     */
    public void setNull(String columnName) throws NoSuchColumnException,
            IOException;

    /**
     * 
     * @param columnNumber
     * @throws Row.NoSuchColumnException
     * @throws IOException 
     */
    public void setNotNull(int columnNumber) throws NoSuchColumnException,
            IOException;

    /**
     * 
     * @param columnName
     * @throws Row.NoSuchColumnException
     * @throws IOException 
     */
    public void setNotNull(String columnName) throws NoSuchColumnException,
            IOException;

}
