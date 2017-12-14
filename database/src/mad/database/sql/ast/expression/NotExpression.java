package mad.database.sql.ast.expression;

/**
 *
 */
public class NotExpression implements Expression {

    private final Expression expression;

    public NotExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{Not:");
        sb.append(expression);
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NotExpression)) {
            return false;
        }
        NotExpression notObj = (NotExpression) obj;
        return notObj.expression.equals(expression);
    }
}
