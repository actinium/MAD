package mad.database.sql.ast;

/**
 *
 */
public class DropTableStatement implements Statement {

    public final String tableName;

    /**
     *
     * @param tableName
     */
    public DropTableStatement(String tableName) {
        this.tableName = tableName;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DropTableStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("}");
        return sb.toString();
    }
}
