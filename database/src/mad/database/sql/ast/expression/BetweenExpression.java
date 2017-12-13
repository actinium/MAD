package mad.database.sql.ast.expression;

/**
 *
 */
public class BetweenExpression implements Expression {

    private final Expression expression;
    private final Expression lowerBound;
    private final Expression upperBound;

    public BetweenExpression(Expression expression, Expression lowerBound, Expression upperBound) {
        this.expression = expression;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(lowerBound).append("<");
        sb.append(expression);
        sb.append("<").append(upperBound);
        sb.append("}");
        return sb.toString();
    }
}
