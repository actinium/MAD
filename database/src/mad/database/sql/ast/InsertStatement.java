package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.expression.ValueExpression;

/**
 *
 */
public class InsertStatement implements Statement {

    private final String tableName;
    private final List<String> columns;
    private final List<Expression> values;

    /**
     *
     * @param tableName
     */
    public InsertStatement(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    /**
     *
     * @param columnName
     * @return
     */
    public InsertStatement addColumn(String columnName) {
        columns.add(columnName);
        return this;
    }

    /**
     *
     * @param expression
     * @return
     */
    public InsertStatement addValue(Expression expression) {
        values.add(expression);
        return this;
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
    public List<String> columns() {
        return Collections.unmodifiableList(columns);
    }

    /**
     *
     * @return
     */
    public List<Expression> values() {
        return Collections.unmodifiableList(values);
    }

    /**
     *
     * @param columnName
     * @return
     */
    public Expression getValue(String columnName) {
        int i = columns.indexOf(columnName);
        if (i == -1) {
            return ValueExpression.nullValue();
        } else {
            return values.get(i);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("  columns(").append(columns.size()).append(") = ");
        if (columns.isEmpty()) {
            sb.append("ALL;\n");
        } else {
            sb.append("{\n");
            for (String c : columns) {
                sb.append("    ").append(c);
                sb.append(";\n");
            }
            sb.append("  }\n");
        }
        sb.append("  values(").append(values.size()).append(") = {\n");
        for (Expression exp : values) {
            sb.append("    ").append(exp).append(";\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertStatement)) {
            return false;
        }
        InsertStatement insertObj = (InsertStatement) obj;
        if (!insertObj.tableName().equals(tableName())) {
            return false;
        }
        if (insertObj.columns().size() != columns().size()) {
            return false;
        }
        for (int i = 0; i < columns().size(); i++) {
            if (!columns().get(i).equals(insertObj.columns().get(i))) {
                return false;
            }
        }
        if (insertObj.values().size() != values().size()) {
            return false;
        }
        for (int i = 0; i < values().size(); i++) {
            if (!values().get(i).equals(insertObj.values().get(i))) {
                return false;
            }
        }
        return true;
    }

}
