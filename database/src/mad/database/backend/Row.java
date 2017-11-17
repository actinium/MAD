package mad.database.backend;

import java.io.IOException;

/**
 *
 */
public interface Row {

    public Row next();

    public boolean hasNext();

    public int getInteger(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    public int getInteger(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    public float getFloat(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    public float getFloat(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    public boolean getBoolean(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    public boolean getBoolean(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    public String getString(int columnNumber) throws NoSuchColumnException, TypeMismatchException, IOException;

    public String getString(String columnName) throws NoSuchColumnException, TypeMismatchException, IOException;

    public String getName(int columnNumber) throws NoSuchColumnException, IOException;

    public boolean isNull(int columnNumber) throws NoSuchColumnException, IOException;

    public Schema.Field.Type getType(int columnNumber) throws NoSuchColumnException;

    public int size();

    public class NoSuchColumnException extends Exception {
    }

    public class TypeMismatchException extends Exception {

        TypeMismatchException(String message) {
            super(message);
        }
    }
}
