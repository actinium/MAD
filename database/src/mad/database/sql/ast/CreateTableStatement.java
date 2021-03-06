package mad.database.sql.ast;

import java.util.Collections;
import java.util.List;
import mad.database.backend.table.Schema.Field.Type;

/**
 *
 */
public class CreateTableStatement implements Statement {

    private final String tableName;
    private final List<ColumnDefinition> tableDefinition;

    /**
     *
     * @param tableName
     * @param tableDefinition
     */
    public CreateTableStatement(String tableName, List<ColumnDefinition> tableDefinition) {
        this.tableName = tableName;
        this.tableDefinition = tableDefinition;
    }

    /**
     *
     * @return
     */
    public String tableName() {
        return tableName;
    }

    public List<ColumnDefinition> tableDefinition() {
        return Collections.unmodifiableList(tableDefinition);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateTableStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("  tableDefinition = {\n");
        for (ColumnDefinition cd : tableDefinition) {
            sb.append("    ").append(cd.name).append(": ").append(cd.type);
            if (cd.type == Type.Varchar) {
                sb.append("(").append(cd.length).append(")");
            }
            sb.append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateTableStatement)) {
            return false;
        }
        CreateTableStatement objS = (CreateTableStatement) obj;
        if (!tableName.equals(objS.tableName)) {
            return false;
        }
        if (tableDefinition.size() != objS.tableDefinition.size()) {
            return false;
        }
        for (int i = 0; i < tableDefinition.size(); i++) {
            if (!tableDefinition.get(i).name.equals(objS.tableDefinition.get(i).name)) {
                return false;
            }
            if (tableDefinition.get(i).type != objS.tableDefinition.get(i).type) {
                return false;
            }
            if (tableDefinition.get(i).length != objS.tableDefinition.get(i).length) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     */
    public static class ColumnDefinition {

        public final String name;
        public final Type type;
        public final int length;

        /**
         *
         * @param name
         * @param type
         */
        public ColumnDefinition(String name, Type type) {
            this.name = name;
            this.type = type;
            this.length = 0;
        }

        /**
         *
         * @param name
         * @param type
         * @param length
         */
        public ColumnDefinition(String name, Type type, int length) {
            this.name = name;
            this.type = type;
            this.length = length;
        }
    }
}
