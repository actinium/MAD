package mad.database.sql.ast.expression;

/**
 *
 */
public class ConcatExpression implements Expression {

    private final Expression leftExpression;
    private final Expression rightExpression;

    public ConcatExpression(Expression leftExpression, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(leftExpression);
        sb.append(" || ");
        sb.append(rightExpression);
        sb.append("}");
        return sb.toString();
    }
}
