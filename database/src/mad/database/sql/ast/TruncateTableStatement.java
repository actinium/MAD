package mad.database.sql.ast;

/**
 *
 */
public class TruncateTableStatement implements Statement {

    private final String tableName;

    /**
     *
     * @param tableName
     */
    public TruncateTableStatement(String tableName) {
        this.tableName = tableName;
    }

    /**
     *
     * @return
     */
    public String tableName() {
        return tableName;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TruncateTableStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TruncateTableStatement)) {
            return false;
        }
        return tableName.equals(((TruncateTableStatement) obj).tableName());
    }
}
