package mad.database.backend.expression;

import mad.database.sql.ast.expression.BetweenExpression;
import mad.database.sql.ast.expression.BinaryExpression;
import mad.database.sql.ast.expression.CaseExpression;
import mad.database.sql.ast.expression.ColumnExpression;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.expression.FunctionExpression;
import mad.database.sql.ast.expression.InSelectExpression;
import mad.database.sql.ast.expression.InValuesExpression;
import mad.database.sql.ast.expression.IsNullExpression;
import mad.database.sql.ast.expression.LikeExpression;
import mad.database.sql.ast.expression.NotExpression;
import mad.database.sql.ast.expression.RegexpExpression;
import mad.database.sql.ast.expression.SubExpression;
import mad.database.sql.ast.expression.UnaryExpression;
import mad.database.sql.ast.expression.ValueExpression;

/**
 *
 */
public class StaticExpressionProcessor {

    /**
     *
     * @param expression
     * @return
     * @throws mad.database.backend.expression.StaticExpressionProcessor.NonStaticVariableException
     */
    public static ValueExpression evaluate(Expression expression) throws NonStaticVariableException {
        if (expression instanceof ValueExpression) {
            return (ValueExpression) expression;
        } else if (expression instanceof BetweenExpression) {
            return evaluateBetween((BetweenExpression) expression);
        } else if (expression instanceof BinaryExpression) {
            return evaluateBinary((BinaryExpression) expression);
        } else if (expression instanceof CaseExpression) {
            return evaluateCase((CaseExpression) expression);
        } else if (expression instanceof ColumnExpression) {
            throw new NonStaticVariableException("Error: Non static variable referenced in static expression!");
        } else if (expression instanceof FunctionExpression) {
            FunctionExpression fExpression = (FunctionExpression) expression;

        } else if (expression instanceof InSelectExpression) {
            InSelectExpression iExp = (InSelectExpression) expression;

        } else if (expression instanceof InValuesExpression) {
            InValuesExpression iExp = (InValuesExpression) expression;

        } else if (expression instanceof IsNullExpression) {
            IsNullExpression iExp = (IsNullExpression) expression;

        } else if (expression instanceof LikeExpression) {
            LikeExpression lExp = (LikeExpression) expression;

        } else if (expression instanceof NotExpression) {
            NotExpression nExp = (NotExpression) expression;

        } else if (expression instanceof RegexpExpression) {
            RegexpExpression rExp = (RegexpExpression) expression;

        } else if (expression instanceof SubExpression) {
            SubExpression sExp = (SubExpression) expression;
            return evaluate(sExp.getSubExpression());
        } else if (expression instanceof UnaryExpression) {
            UnaryExpression uExp = (UnaryExpression) expression;

        }

        return null;
    }

    private static ValueExpression evaluateBetween(BetweenExpression bExp){
        return null;
    }

    private static ValueExpression evaluateBinary(BinaryExpression bExp) throws NonStaticVariableException{
        ValueExpression vLeft = evaluate(bExp.left());
        ValueExpression vRight = evaluate(bExp.right());
        return null;
    }

    private static ValueExpression evaluateCase(CaseExpression bExp){
        return null;
    }

    public static class NonStaticVariableException extends Exception{
        /**
         *
         * @param message
         */
        public NonStaticVariableException(String message) {
            super(message);
        }
    }
}
