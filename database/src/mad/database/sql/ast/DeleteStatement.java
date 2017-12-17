package mad.database.sql.ast;

import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class DeleteStatement implements Statement {

    private final String tableName;
    private final Expression condition;

    public DeleteStatement(String tableName, Expression condition) {
        this.tableName = tableName;
        this.condition = condition;
    }

    public String tableName() {
        return tableName;
    }

    public Expression condition() {
        return condition;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeleteStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        if (condition != null) {
            sb.append("  condition = ").append(condition).append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeleteStatement)) {
            return false;
        }
        // check condition == null
        return tableName().equals(((DeleteStatement) obj).tableName())
                && condition().equals(((DeleteStatement) obj).condition());
    }
}
