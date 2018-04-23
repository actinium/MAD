package mad.database.sql.ast;

import java.util.List;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.selection.SelectStatement;

/**
 *
 */
public class Tables {

    public static abstract class Table {

        protected final String as;

        protected Table(String as) {
            this.as = as;
        }

        public String getAlias(){
            return as;
        }
    }

    public static class SingleTable extends Table {

        private final String tableName;

        public SingleTable(String tableName, String as) {
            super(as);
            this.tableName = tableName;
        }

        public SingleTable(String tableName) {
            this(tableName, null);
        }

        public String getTableName(){
            return tableName;
        }
    }

    public static class SubSelect extends Table {

        private final SelectStatement statement;

        public SubSelect(SelectStatement statement, String as) {
            super(as);
            this.statement = statement;
        }

        public SubSelect(SelectStatement statement) {
            this(statement, null);
        }
    }

    public static class SubTable extends Table {

        private final Table table;

        public SubTable(Table table, String as) {
            super(as);
            this.table = table;
        }

        public Table getTable(){
            return table;
        }
    }

    public static class JoinedTables extends Table {

        private final Table leftTable;
        private final Join joinOp;
        private final Expression joinCondition;
        private final List<String> joinUsing;
        private final Table rightTable;

        public JoinedTables(Table left, Table right, Join op, Expression condition) {
            super(null);
            this.leftTable = left;
            this.rightTable = right;
            this.joinOp = op;
            this.joinCondition = condition;
            this.joinUsing = null;
        }

        public JoinedTables(Table left, Table right, Join op, List<String> using) {
            super(null);
            this.leftTable = left;
            this.rightTable = right;
            this.joinOp = op;
            this.joinCondition = null;
            this.joinUsing = using;
        }

        public JoinedTables(Table left, Table right, Join op) {
            super(null);
            this.leftTable = left;
            this.rightTable = right;
            this.joinOp = op;
            this.joinCondition = null;
            this.joinUsing = null;
        }

        public Table getLeftTable(){
            return leftTable;
        }

        public Table getRightTable(){
            return rightTable;
        }

        public enum Join {

            NatrualJoin,
            LeftOuterJoin,
            RightOuterJoin,
            FullOuterJoin,
            InnerJoin,
            CrossJoin
        }
    }
}
