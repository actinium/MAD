package mad.database.sql.ast.selection;

import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class Columns {

    public static abstract class Column {

        protected final String as;

        public Column(String name) {
            this.as = name;
        }
    }

    public static class StarColumn extends Column {

        private final String tableName;

        public StarColumn() {
            this(null);
        }

        public StarColumn(String tableName) {
            super(null);
            this.tableName = tableName;
        }

        @Override
        public String toString() {
            if (tableName != null) {
                return "(" + tableName + ".*)";
            }
            return "(*)";
        }
    }

    public static class ExprColumn extends Column {

        private final Expression expression;

        public ExprColumn(Expression expression, String as) {
            super(as);
            this.expression = expression;
        }

        public ExprColumn(Expression expression) {
            this(expression, null);
        }

        public String toString() {
            if (as == null) {
                return expression.toString();
            }
            return expression.toString() + " as " + as;
        }
    }
}
