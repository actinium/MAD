package mad.database.sql.ast.expression;

import java.util.ArrayList;
import java.util.List;

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

    public Expression left() {
        return leftExpression;
    }

    public Expression right() {
        return rightExpression;
    }

    public Operator operator() {
        return operator;
    }

    /**
     * Rebuilds the syntax-tree to correct for operator precedence.
     *
     * @return a BinaryExpression with subexpressions with correct operator precedence.
     */
    public BinaryExpression adjustedForPrecedence() {
        List<Object> flatExpression = flat();
        for(Operator op: Operator.values()){
            List<Object> nextFlatExpression = new ArrayList<>();
            for(int i = 1; i<flatExpression.size();i+=2){
                if(((Operator)flatExpression.get(i)) == op){
                    Object left = flatExpression.get(i-1);
                    if(!nextFlatExpression.isEmpty() &&
                            nextFlatExpression.get(nextFlatExpression.size()-1) instanceof
                            BinaryExpression){
                        left = nextFlatExpression.get(nextFlatExpression.size()-1);
                        nextFlatExpression.remove(nextFlatExpression.size()-1);
                    }
                    nextFlatExpression.add(new BinaryExpression(
                            (Expression)left,
                            (Operator)flatExpression.get(i),
                            (Expression)flatExpression.get(i+1))
                    );
                }else{
                    if(nextFlatExpression.isEmpty() ||
                            !(nextFlatExpression.get(nextFlatExpression.size()-1) instanceof
                            BinaryExpression)){
                        nextFlatExpression.add(flatExpression.get(i-1));
                    }
                    nextFlatExpression.add(flatExpression.get(i));
                }
            }
            if(nextFlatExpression.size()%2 == 0){
                nextFlatExpression.add(flatExpression.get(flatExpression.size()-1));
            }
            flatExpression = nextFlatExpression;
        }
        return (BinaryExpression)flatExpression.get(0);
    }

    /**
     * Get all BinaryExpression subexpressions as a flat list.
     * Used to build a syntax-tree with proper operator precedence.
     *
     * The list is on the form:
     * +----------+--------+----------+--------+-...-+--------+----------+
     * |Expression|Operator|Expression|Operator| ... |Operator|Expression|
     * +----------+--------+----------+--------+-...-+--------+----------+
     *
     * @return a flat representation of the expression.
     */
    private List<Object> flat(){
        List<Object> flatExpression = new ArrayList<>();
        if(leftExpression instanceof BinaryExpression){
            flatExpression.addAll(((BinaryExpression)leftExpression).flat());
        }else{
            flatExpression.add(leftExpression);
        }
        flatExpression.add(operator);
        if(rightExpression instanceof BinaryExpression){
            flatExpression.addAll(((BinaryExpression)rightExpression).flat());
        }else{
            flatExpression.add(rightExpression);
        }
        return flatExpression;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryExpression)) {
            return false;
        }
        BinaryExpression binaryObj = (BinaryExpression) obj;
        return binaryObj.operator.equals(operator)
                && binaryObj.leftExpression.equals(leftExpression)
                && binaryObj.rightExpression.equals(rightExpression);
    }

    public static enum Operator {

        Concat("||",1),
        Multiply("*", 2),
        Divide("/", 3),
        Modulo("%", 4),
        Plus("+", 5),
        Minus("-", 6),
        LeftShift("<<", 7),
        RightShift(">>", 8),
        BitwiseAnd("&", 9),
        BitwiseOr("|", 10),
        LessThan("<", 11),
        LessThanOrEquals("<=", 12),
        GreaterThan(">", 13),
        GreaterThanOrEquals(">=", 14),
        Equals("=", 15),
        NotEquals("<>", 16),
        In("in", 17),
        And("and", 18),
        Or("or", 19);

        private final String string;
        private final int precedence;

        private Operator(String string, int precedence) {
            this.string = string;
            this.precedence = precedence;
        }

        public String stringValue() {
            return string;
        }

        public int precedence() {
            return precedence;
        }
    }
}
