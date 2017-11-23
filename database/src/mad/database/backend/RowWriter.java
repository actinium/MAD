package mad.database.backend;

import mad.database.backend.table.Schema;
import mad.database.backend.table.Row;
import java.io.IOException;
import mad.util.Bytes;
import mad.util.NullBitMap;

/**
 *
 */
public class RowWriter {

    private final Pager pager;
    private final Schema schema;

    public RowWriter(Pager pager, Schema schema) {
        this.pager = pager;
        this.schema = schema;
    }

    public void write(int filePosition, Row row) throws IOException, Row.TypeMismatchException {
        if (row.size() != schema.columns()) {
            throw new Row.TypeMismatchException("Row doesn't match schema!");
        }
        try {
            for (int i = 0; i < schema.columns(); i++) {

                if (!row.getName(i).equals(schema.get(i).name)
                        || row.getType(i) != schema.get(i).type) {
                    throw new Row.TypeMismatchException("Row doesn't match schema!");
                }

                if (schema.get(i).type == Schema.Field.Type.Varchar) {
                    byte[] byteStr = Bytes.fromString(row.getString(i));
                    int varcharLength = schema.get(i).length;
                    if (byteStr.length > varcharLength) {
                        throw new Row.TypeMismatchException("String is to long to fit in varchar.");
                    }
                }
            }
        } catch (Row.NoSuchColumnException ex) {
            throw new Row.TypeMismatchException("Row doesn't match schema!");
        }
        NullBitMap nullMap = new NullBitMap();
        for (int i = 0; i < schema.columns(); i++) {
            try {
                int offset = schema.get(i).offset;
                if (row.isNull(i)) {
                    nullMap.setNull(i);
                    continue;
                }
                switch (schema.get(i).type) {
                    case Integer:
                        pager.writeInteger(filePosition + offset, row.getInteger(i));
                        break;
                    case Float:
                        pager.writeFloat(filePosition + offset, row.getFloat(i));
                        break;
                    case Boolean:
                        pager.writeBoolean(filePosition + offset, row.getBoolean(i));
                        break;
                    case Varchar:
                        pager.writeString(filePosition + offset, row.getString(i), schema.get(i).length);
                        break;
                }
            } catch (Row.NoSuchColumnException ex) {
                throw new Row.TypeMismatchException("Row doesn't match schema!");
            }
        }
        pager.writeBytes(filePosition+8, Bytes.fromNullBitMap(nullMap), 8);
    }

    public int getPreviousRowPointer(int filePosition) throws IOException {
        return pager.readInteger(filePosition);
    }

    public int getNextRowPointer(int filePosition) throws IOException {
        return pager.readInteger(filePosition + 4);
    }

    public void setPreviousRowPointer(int filePosition, int rowPointer) throws IOException {
        pager.writeInteger(filePosition, rowPointer);
    }

    public void setNextRowPointer(int filePosition, int rowPointer) throws IOException {
        pager.writeInteger(filePosition + 4, rowPointer);
    }
}
