package mad.database.sql.ast.expression;

/**
 *
 */
public class UnaryExpression implements Expression {

    private final Operator operator;
    private final Expression expression;

    public UnaryExpression(Operator operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(operator.stringValue()).append(expression.toString()).append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnaryExpression)) {
            return false;
        }
        UnaryExpression unaryObj = (UnaryExpression) obj;
        return unaryObj.operator.equals(operator) && unaryObj.expression.equals(expression);
    }

    public enum Operator {

        Minus("-"),
        Plus("+"),
        Not("!"),
        BitwiseNot("~");

        private final String string;

        private Operator(String string) {
            this.string = string;
        }

        public String stringValue() {
            return string;
        }
    }
}
