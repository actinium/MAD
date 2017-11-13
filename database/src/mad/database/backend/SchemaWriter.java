package mad.database.backend;

import java.io.IOException;
import mad.database.backend.Schema.Field;
import mad.util.Bytes;

/**
 *
 */
public class SchemaWriter {

    private Pager pager;

    public SchemaWriter(Pager pager) {
        this.pager = pager;
    }

    public void write(int filePosition, Schema schema) throws IOException {
        int fieldCount = schema.size();
        pager.writeInteger(filePosition, fieldCount);
        int currentOffset = fieldCount * 4+4;
        int columnOffset = 16;
        for (int i = 0; i < fieldCount; i++) {
            pager.writeInteger(filePosition + i * 4 + 4, currentOffset);
            int fieldPosition = filePosition + currentOffset;
            Field field = schema.get(i);
            pager.writeInteger(fieldPosition, columnOffset);
            pager.writeByte(fieldPosition + 4, field.type.toByte());
            byte[] nameAsBytes = Bytes.fromString(field.name);
            if (field.type == Field.Type.Varchar) {
                pager.writeInteger(fieldPosition + 5, field.length);
                pager.writeInteger(fieldPosition + 9, nameAsBytes.length);
                pager.writeBytes(fieldPosition + 13, nameAsBytes, nameAsBytes.length);
                currentOffset += 13 + nameAsBytes.length;
            } else {
                pager.writeInteger(fieldPosition + 5, nameAsBytes.length);
                pager.writeBytes(fieldPosition + 9, nameAsBytes, nameAsBytes.length);
                currentOffset += 9 + nameAsBytes.length;
            }
            if (field.type == Field.Type.Varchar) {
                columnOffset += field.length;
            } else if (field.type == Field.Type.Boolean) {
                columnOffset += 1;
            } else {
                columnOffset += 4;
            }
        }
    }
}
