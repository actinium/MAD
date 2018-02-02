package mad.database.sql.ast.expression;

import mad.database.sql.ast.selection.SelectStatement;

/**
 *
 */
public class InSelectExpression implements Expression {

    private final Expression value;
    private final SelectStatement statement;

    public InSelectExpression(Expression value,SelectStatement statement) {
        this.value = value;
        this.statement = statement;
    }
}
