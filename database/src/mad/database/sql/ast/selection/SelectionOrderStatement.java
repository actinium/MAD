package mad.database.sql.ast.selection;

import java.util.List;

/**
 *
 */
public class SelectionOrderStatement implements SelectStatement {

    private final SelectStatement statement;
    private final List<Order> orderBy;

    public SelectionOrderStatement(SelectStatement statement, List<Order> orderBy) {
        this.statement = statement;
        this.orderBy = orderBy;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("SelectionOrderStatement{\n");
        sb.append("  left = ").append("{\n");
        String leftLines[] = statement.toString().split("\\r\\n|\\n|\\r");
        for (String line : leftLines) {
            sb.append("    ").append(line).append("\n");
        }
        sb.append("  }\n");
        sb.append("  order by = {\n");
        for(Order order: orderBy){
            sb.append("    ").append(order).append(";\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

}
