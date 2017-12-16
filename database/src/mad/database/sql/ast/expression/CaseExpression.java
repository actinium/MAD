package mad.database.sql.ast.expression;

import java.util.List;

/**
 *
 */
public class CaseExpression implements Expression {

    private final Expression expression;
    private final List<Case> cases;
    private final Expression defaultCase;

    public CaseExpression(Expression expression, List<Case> cases, Expression defaultCase) {
        this.expression = expression;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CaseExpression)) {
            return false;
        }
        CaseExpression caseObj = (CaseExpression) obj;
        if ((expression == null && caseObj.expression != null)
                || (expression == null && caseObj.expression != null)) {
            return false;
        }
        if ((expression != null && caseObj.expression != null)
                && !expression.equals(caseObj.expression)) {
            return false;
        }
        if ((defaultCase == null && caseObj.defaultCase != null)
                || (defaultCase == null && caseObj.defaultCase != null)) {
            return false;
        }
        if ((defaultCase != null && caseObj.defaultCase != null)
                && !defaultCase.equals(caseObj.defaultCase)) {
            return false;
        }
        if (cases.size() != caseObj.cases.size()) {
            return false;
        }
        for (int i = 0; i < cases.size(); i++) {
            if (!cases.get(i).equals(caseObj.cases.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static class Case {

        private final Expression condition;
        private final Expression expression;

        public Case(Expression condition, Expression expression) {
            this.condition = condition;
            this.expression = expression;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Case)) {
                return false;
            }
            Case caseObj = (Case) obj;
            return condition.equals(caseObj.condition) && expression.equals(caseObj.expression);
        }
    }
}
