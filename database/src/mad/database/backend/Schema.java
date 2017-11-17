package mad.database.backend;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static mad.database.Config.PAGESIZE;
import mad.database.backend.Schema.Field;

/**
 *
 */
public class Schema implements Iterable<Field> {

    private final List<Field> fields;
    private final int bytes;
    private final int size;

    /**
     *
     * @param fields
     */
    public Schema(List<Field> fields) {
        this.fields = Collections.unmodifiableList(fields);
        this.bytes = calcBytes(fields);
        this.size = this.fields.size();
    }

    /**
     *
     * @param fields
     */
    public Schema(Field... fields) {
        this.fields = Collections.unmodifiableList(Arrays.asList(fields));
        this.bytes = calcBytes(this.fields);
        this.size = this.fields.size();
    }

    private int calcBytes(List<Field> fields) {
        int bSize = 16;
        for (Field f : fields) {
            if (f.type == Field.Type.Boolean) {
                bSize += 1;
            } else if (f.type == Field.Type.Integer) {
                bSize += 4;
            } else if (f.type == Field.Type.Float) {
                bSize += 4;
            } else if (f.type == Field.Type.Varchar) {
                bSize += f.length;
            }
        }
        return bSize;
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

    /**
     *
     * @return
     */
    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    /**
     *
     * @return the number of columns in a row.
     */
    public int size() {
        return size;
    }

    /**
     *
     * @return the number of bytes needed to represent one row.
     */
    public int bytes() {
        return bytes;
    }

    /**
     *
     * @return the maximum number of rows that can be stored in one page.
     */
    public int rowsPerPage() {
        return PAGESIZE / bytes();
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
