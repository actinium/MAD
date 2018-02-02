package mad.database.sql.ast.expression;

import java.util.List;

/**
 *
 */
public class InValuesExpression implements Expression {

    private final Expression value;
    private final List<Expression> values;

    public InValuesExpression(Expression value, List<Expression> values) {
        this.value = value;
        this.values = values;
    }

}
