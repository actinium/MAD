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
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{not:");
        sb.append(expression);
        sb.append("}");
        return sb.toString();
    }
}
