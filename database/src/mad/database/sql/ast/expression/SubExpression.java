package mad.database.sql.ast.expression;

/**
 *
 */
public class SubExpression implements Expression {

    private final Expression expression;

    public SubExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getSubExpression(){
        return expression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(expression);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SubExpression)) {
            return false;
        }
        SubExpression subObj = (SubExpression) obj;
        return subObj.expression.equals(expression);
    }
}
