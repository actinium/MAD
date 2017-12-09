package mad.database.sql.ast.expression;

/**
 *
 */
public class ColumnExpression implements Expression {

    private final String tableName;
    private final String columnName;

    public ColumnExpression(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnExpression(String columnName) {
        this("", columnName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{column:");
        if (!tableName.isEmpty()) {
            sb.append(tableName).append('.');
        }
        sb.append(columnName).append("}");
        return sb.toString();
    }
}
