
package mad.database.sql.ast.selection;

import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class SelectionLimitStatement implements SelectStatement{
    private final SelectStatement statement;
    private final Expression limit;
    private final Expression offset;

    public SelectionLimitStatement(SelectStatement statement, Expression limit, Expression offset){
        this.statement = statement;
        this.limit = limit;
        this.offset = offset;
    }

    public SelectionLimitStatement(SelectStatement statement, Expression limit){
        this(statement,limit,null);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SelectionLimitStatement{\n");
        sb.append("  left = ").append("{\n");
        String leftLines[] = statement.toString().split("\\r\\n|\\n|\\r");
        for (String line : leftLines) {
            sb.append("    ").append(line).append("\n");
        }
        sb.append("  }\n");
        sb.append("  limit = ").append(limit).append(";\n");
        if(offset != null){
            sb.append("  offset = ").append(offset).append(";\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
