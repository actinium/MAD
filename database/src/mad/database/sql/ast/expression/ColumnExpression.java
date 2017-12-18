package mad.database.sql.ast.expression;

/**
 *
 */
public class ColumnExpression implements Expression {

    private final String tableName;
    private final String columnName;
    private final boolean stringConvertable;

    private ColumnExpression(String tableName, String columnName, boolean stringConvertable) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.stringConvertable = stringConvertable;
    }

    public ColumnExpression(String tableName, String columnName) {
        this(tableName, columnName, false);
    }

    public ColumnExpression(String columnName, boolean stringConvertable) {
        this("", columnName, stringConvertable);
    }

    public ColumnExpression(String columnName) {
        this("", columnName, false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{Column");
        if (stringConvertable) {
            sb.append("(text)");
        }
        sb.append(":");
        if (!tableName.isEmpty()) {
            sb.append(tableName).append('.');
        }
        sb.append(columnName);
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnExpression)) {
            return false;
        }
        ColumnExpression columnObj = (ColumnExpression) obj;
        return columnObj.tableName.equals(tableName) &&
                columnObj.columnName.equals(columnName) &&
                columnObj.stringConvertable == stringConvertable;
    }
}
