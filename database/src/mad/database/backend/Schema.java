package mad.database.backend;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import mad.database.backend.Schema.Field;

/**
 *
 */
public class Schema implements Iterable<Field> {

    private final List<Field> fields;

    /**
     *
     * @param fields
     */
    public Schema(List<Field> fields) {
        this.fields = Collections.unmodifiableList(fields);
    }

    /**
     *
     * @param fields
     */
    public Schema(Field... fields) {
        this.fields = Collections.unmodifiableList(Arrays.asList(fields));
    }

    /**
     *
     * @param colNum
     * @return
     */
    public Field get(int colNum) {
        return fields.get(colNum);
    }

    /**
     *
     * @param colName
     * @return
     */
    public Field get(String colName) {
        int index = 0;
        while (index < fields.size()) {
            if (fields.get(index).name.equals(colName)) {
                return fields.get(index);
            }
            index++;
        }
        return null;
    }

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public int size() {
        return fields.size();
    }

    public static class Field {

        public enum Type {

            Integer(1),
            Float(2),
            Boolean(3),
            Varchar(4);

            private final byte b;

            private Type(int b) {
                this.b = (byte) b;
            }

            public byte toByte() {
                return b;
            }

        }

        public final int columnNumber;
        public final String name;
        public final Type type;
        public final int offset;
        public final int length;

        public Field(int colNum, String name, Type type, int offset, int length) {
            this.columnNumber = colNum;
            this.name = name;
            this.type = type;
            this.offset = offset;
            this.length = length;
        }
        
        public Field(String name, Type type, int length) {
            this.columnNumber = 0;
            this.name = name;
            this.type = type;
            this.offset = 0;
            this.length = length;
        }
        
        public Field(String name, Type type) {
            this.columnNumber = 0;
            this.name = name;
            this.type = type;
            this.offset = 0;
            this.length = 0;
        }
    }
}
