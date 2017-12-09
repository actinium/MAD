package mad.database.sql.ast.expression;

/**
 *
 */
public class BinaryExpression implements Expression {

    private final Expression leftExpression;
    private final Operator operator;
    private final Expression rightExpression;

    public BinaryExpression(Expression leftExpression, Operator operator, Expression rightExpression) {
        this.leftExpression = leftExpression;
        this.operator = operator;
        this.rightExpression = rightExpression;
    }

    public Expression left(){
        return leftExpression;
    }

    public Expression right(){
        return rightExpression;
    }

    public Operator operator(){
        return operator;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(leftExpression);
        sb.append(operator.stringValue());
        sb.append(rightExpression);
        sb.append("}");
        return sb.toString();
    }

    public enum Operator {

        Multiply("*"),
        Divide("/"),
        Modulo("%"),
        Plus("+"),
        Minus("-"),
        LeftShift("<<"),
        RightShift(">>"),
        BitwiseAnd("&"),
        BitwiseOr("|"),
        LessThan("<"),
        LessThanOrEquals("<="),
        GreaterThan(">"),
        GreaterThanOrEquals(">="),
        Equals("="),
        NotEquals("<>"),
        In("in"),
        And("and"),
        Or("or");

        private final String string;

        private Operator(String string) {
            this.string = string;
        }

        public String stringValue() {
            return string;
        }
    }
}
