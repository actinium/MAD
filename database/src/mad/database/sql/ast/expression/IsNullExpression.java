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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IsNullExpression)) {
            return false;
        }
        IsNullExpression isNullObj = (IsNullExpression) obj;
        return isNullObj.expression.equals(expression);
    }
}
