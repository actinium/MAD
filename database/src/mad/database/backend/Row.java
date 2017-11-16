package mad.database.backend;

/**
 *
 */
public interface Row {
    
    public Row next();
    public boolean hasNext();
    
    int getInteger( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    int getInteger( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    float getFloat( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    float getFloat( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    boolean getBoolean( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    boolean getBoolean( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    String getString( int columnNumber ) throws NoSuchColumnException, TypeMismatchException;
    String getString( String columnName ) throws NoSuchColumnException, TypeMismatchException;
    
    public class NoSuchColumnException extends Exception{}
    public class TypeMismatchException extends Exception{

        TypeMismatchException(String message) {
            super(message);
        }
    }
}
