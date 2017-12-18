package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class UpdateStatement implements Statement {

    private final String tableName;
    private final List<String> columns;
    private final List<Expression> values;
    private final Expression condition;

    public UpdateStatement(String tableName, Expression condition) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
        this.condition = condition;
    }

    public UpdateStatement(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
        this.condition = null;
    }

    public UpdateStatement(UpdateStatement statement, Expression condition) {
        this.tableName = statement.tableName();
        this.columns = statement.columns();
        this.values = statement.values();
        this.condition = condition;
    }

    /**
     *
     * @param columnName
     * @param value
     * @return
     */
    public UpdateStatement addUpdate(String columnName, Expression value) {
        columns.add(columnName);
        values.add(value);
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
        sb.append("UpdateStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        if (condition != null) {
            sb.append("  where = ").append(condition).append(";\n");
        }
        sb.append("  updates = {\n");
        for (int i = 0; i < columns().size(); i++) {
            sb.append("    ");
            sb.append(columns().get(i)).append("=");
            sb.append(values().get(i)).append(";\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UpdateStatement)) {
            return false;
        }
        UpdateStatement updateObj = (UpdateStatement) obj;
        if (!updateObj.tableName().equals(tableName())) {
            return false;
        }
        if (!updateObj.condition().equals(condition())) {
            return false;
        }
        if (updateObj.columns().size() != columns().size()) {
            return false;
        }
        for (int i = 0; i < columns().size(); i++) {
            if (!columns().get(i).equals(updateObj.columns().get(i))) {
                return false;
            }
        }
        if (updateObj.values().size() != values().size()) {
            return false;
        }
        for (int i = 0; i < values().size(); i++) {
            if (!values().get(i).equals(updateObj.values().get(i))) {
                return false;
            }
        }
        return true;
    }
}
