package mad.database.sql.ast.selection;

import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class Order {

    private final boolean ascending;
    private final Expression expression;

    public Order(Expression expression) {
        this.expression = expression;
        this.ascending = true;
    }

    public Order(Expression expression, boolean ascending) {
        this.expression = expression;
        this.ascending = ascending;
    }

    @Override
    public String toString() {
        String order;
        if (ascending) {
            order = "ascending";
        } else {
            order = "descending";
        }
        return expression.toString() + " " + order;
    }
}
