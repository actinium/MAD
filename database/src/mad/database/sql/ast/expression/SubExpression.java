package mad.database.sql.ast.expression;

/**
 *
 */
public class SubExpression implements Expression{
    private final Expression expression;

    public SubExpression(Expression expression){
        this.expression = expression;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(expression);
        sb.append(")");
        return sb.toString();
    }
}
