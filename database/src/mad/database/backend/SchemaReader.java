package mad.database.backend;

import java.io.IOException;
import java.util.ArrayList;
import mad.database.backend.Schema.Field;

/**
 *
 */
public class SchemaReader {

    private Pager pager;

    public SchemaReader(Pager pager) {
        this.pager = pager;
    }

    /**
     *
     * @param filePosition
     * @return
     * @throws IOException
     */
    public Schema read(int filePosition) throws IOException {
        ArrayList<Field> fields = new ArrayList<>();
        int fieldCount = pager.readInteger(filePosition);
        for (int i = 0; i < fieldCount; i++) {
            int fieldSchemaOffset = pager.readInteger(filePosition + i * 4+4);

            int fieldOffset = pager.readInteger(filePosition + fieldSchemaOffset);
            byte fieldTypeByte = pager.readByte(filePosition + fieldSchemaOffset + 4);
            Field.Type fieldType;
            int fieldLength;
            int fieldNameLength;
            String fieldName;

            if (fieldTypeByte == Field.Type.Varchar.toByte()) {
                fieldType = Field.Type.Varchar;
                fieldLength = pager.readInteger(filePosition + fieldSchemaOffset + 5);
                fieldNameLength = pager.readInteger(filePosition + fieldSchemaOffset + 9);
                fieldName = pager.readString(filePosition + fieldSchemaOffset + 13, fieldNameLength);
            } else {
                if (fieldTypeByte == Field.Type.Integer.toByte()) {
                    fieldType = Field.Type.Integer;
                    fieldLength = 4;
                } else if (fieldTypeByte == Field.Type.Float.toByte()) {
                    fieldType = Field.Type.Float;
                    fieldLength = 4;
                } else {
                    fieldType = Field.Type.Boolean;
                    fieldLength = 1;
                }
                fieldNameLength = pager.readInteger(filePosition + fieldSchemaOffset + 5);
                fieldName = pager.readString(filePosition + fieldSchemaOffset + 9, fieldNameLength);
            }

            fields.add(new Field(i, fieldName, fieldType, fieldOffset, fieldLength));
        }
        return new Schema(fields);
    }
}
