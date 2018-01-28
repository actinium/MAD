package mad.database.sql.ast.selection;

import java.util.List;
import mad.database.sql.ast.Tables.Table;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.selection.Columns.Column;

/**
 *
 */
public class SimpleSelectStatement implements SelectStatement {

    private boolean distinct;
    private List<Column> results;
    private Table tables;
    private Expression whereCondition;
    private List<Expression> groupBy;
    private Expression havingCondition;

    public SimpleSelectStatement(boolean distinct, List<Column> results,
            Table tables, Expression whereCondition, List<Expression> groupBy,
            Expression havingCondition) {
        this.distinct = distinct;
        this.results = results;
        this.tables = tables;
        this.whereCondition = whereCondition;
        this.groupBy = groupBy;
        this.havingCondition = havingCondition;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean getDistinct() {
        return distinct;
    }

    public void setResultColumns(List<Column> results) {
        this.results = results;
    }

    public void setTables(Table tables) {
        this.tables = tables;
    }

    public void setWhere(Expression where) {
        this.whereCondition = where;
    }

    public void setGroupBy(List<Expression> groupBy) {
        this.groupBy = groupBy;
    }

    public void setHaving(Expression having) {
        this.havingCondition = having;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SimpleSelectStatement{\n");
        sb.append("  distinct = ").append(distinct).append(";\n");
        sb.append("  results = {\n");
        for (Column col : results) {
            sb.append("    ").append(col).append(";\n");
        }
        sb.append("  }\n");
        if (tables != null) {
            sb.append("  tables = {\n");
            // Todo
            sb.append("  }\n");
        }
        if (whereCondition != null) {
            sb.append("  where = ").append(whereCondition).append(";\n");
        }
        if (groupBy != null) {
            sb.append("  group by = ");
            for (Expression expr : groupBy) {
                sb.append(expr).append(",");
            }
            sb.deleteCharAt(sb.length() - 1).append(";\n");
            if (havingCondition != null) {
                sb.append("  having = ").append(havingCondition).append(";\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
