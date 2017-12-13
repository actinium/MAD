package mad.database.sql.ast.expression;

/**
 *
 */
public class IsNullExpression implements Expression {

    private final Expression expression;

    public IsNullExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("Is Null:");
        sb.append(expression);
        sb.append("}");
        return sb.toString();
    }
}
