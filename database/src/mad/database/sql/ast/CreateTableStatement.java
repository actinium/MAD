package mad.database.sql.ast;

import java.util.Collections;
import java.util.List;
import mad.database.backend.table.Schema.Field.Type;

/**
 *
 */
public class CreateTableStatement implements Statement {

    public final String tableName;
    public final List<ColumnDefinition> tableDefinition;

    public CreateTableStatement(String tableName, List<ColumnDefinition> tableDefinition) {
        this.tableName = tableName;
        this.tableDefinition = Collections.unmodifiableList(tableDefinition);
    }

    public static class ColumnDefinition {

        public final String name;
        public final Type type;
        public final int length;

        public ColumnDefinition(String name, Type type) {
            this.name = name;
            this.type = type;
            this.length = 0;
        }

        public ColumnDefinition(String name, Type type, int length) {
            this.name = name;
            this.type = type;
            this.length = length;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateTableStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("  tableDefinition = {\n");
        for (int i = 0; i < tableDefinition.size(); i++) {
            ColumnDefinition cd = tableDefinition.get(i);
            sb.append("    ").append(cd.name).append(" ").append(cd.type);
            if (cd.type == Type.Varchar) {
                sb.append("(").append(cd.length).append(")");
            }
            sb.append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
