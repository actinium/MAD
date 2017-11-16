package mad.database.backend;

/**
 *
 */
public interface Row {
    
    public Row next();
    public boolean hasNext();
    
    public int getInteger( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    public int getInteger( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    public float getFloat( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    public float getFloat( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    public boolean getBoolean( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    public boolean getBoolean( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    public String getString( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    public String getString( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    public String getName( int columnNumber) throws NoSuchColumnException;
    public Schema.Field.Type getType( int columnNumber) throws NoSuchColumnException;
    
    public class NoSuchColumnException extends Exception{}
    public class TypeMismatchException extends Exception{

        TypeMismatchException(String message) {
            super(message);
        }
    }
}
