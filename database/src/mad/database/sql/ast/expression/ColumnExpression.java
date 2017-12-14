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
        sb.append("{Column:");
        if (!tableName.isEmpty()) {
            sb.append(tableName).append('.');
        }
        sb.append(columnName).append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnExpression)) {
            return false;
        }
        ColumnExpression columnObj = (ColumnExpression) obj;
        return columnObj.tableName.equals(tableName) && columnObj.columnName.equals(columnName);
    }
}
