package mad.database.sql.ast.selection;

/**
 * Union, Intersect, Except
 */
public class CompundSelectStatement implements SelectStatement {

    private final SelectStatement left;
    private final Operator operator;
    private final SelectStatement right;

    public CompundSelectStatement(SelectStatement left, Operator op, SelectStatement right) {
        this.left = left;
        this.operator = op;
        this.right = right;
    }

    public enum Operator {

        Union,
        UnionAll,
        Intersect,
        IntersectAll,
        Except,
        ExceptAll
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CompundSelectStatement{\n");
        sb.append("  operator = ").append(operator).append(";\n");
        sb.append("  left = ").append("{\n");
        String leftLines[] = left.toString().split("\\r\\n|\\n|\\r");
        for (String line : leftLines) {
            sb.append("    ").append(line).append("\n");
        }
        sb.append("  }\n");
        sb.append("  right = ").append("{\n");
        String rightLines[] = right.toString().split("\\r\\n|\\n|\\r");
        for (String line : rightLines) {
            sb.append("    ").append(line).append("\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }
}
