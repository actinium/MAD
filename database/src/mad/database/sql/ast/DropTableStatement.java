package mad.database.sql.ast;

/**
 *
 */
public class DropTableStatement implements Statement {

    private final String tableName;
    private final boolean ifExists;

    /**
     *
     * @param tableName
     * @param ifExists
     */
    public DropTableStatement(String tableName, boolean ifExists) {
        this.tableName = tableName;
        this.ifExists = ifExists;
    }

    /**
     *
     *@param tableName
     */
    public DropTableStatement(String tableName) {
        this(tableName,false);
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
    public boolean ifExists() {
        return ifExists;
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
        sb.append("  ifExists = ").append(Boolean.toString(ifExists)).append(";\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DropTableStatement)) {
            return false;
        }
        return tableName().equals(((DropTableStatement) obj).tableName()) &&
                ifExists()==((DropTableStatement) obj).ifExists();
    }
}
