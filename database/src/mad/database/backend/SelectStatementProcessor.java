package mad.database.backend;

import java.io.IOException;
import mad.database.backend.table.NamedRow;
import mad.database.backend.table.NestedLoopJoinRow;
import mad.database.backend.table.Row;
import mad.database.sql.ast.Tables;
import mad.database.sql.ast.Tables.Table;
import mad.database.sql.ast.selection.SelectStatement;
import mad.database.sql.ast.selection.SimpleSelectStatement;

/**
 *
 */
public class SelectStatementProcessor {

    private final DB db;

    public SelectStatementProcessor(DB db) {
        this.db = db;
    }

    /**
     *
     * @param statement
     * @return
     * @throws java.io.IOException
     */
    public Row executeQuery(SelectStatement statement) throws IOException {
        if (statement instanceof SimpleSelectStatement) {
            SimpleSelectStatement simpleSelect = (SimpleSelectStatement) statement;
            Row row = getRow(simpleSelect.getTables());
            return row;
        }
        return null;
    }

    private Row getRow(Table table) throws IOException {
        if (table instanceof Tables.SingleTable) {
            Tables.SingleTable st = (Tables.SingleTable) table;
            String tableName = st.getTableName();

            if (db.hasTable(tableName)) {
                Row row = db.getFirstRow(db.getTablePointer(tableName));
                String alias = st.getAlias();
                if( alias != null){
                    row = new NamedRow(row, alias);
                }
                return row;
            }
        } else if (table instanceof Tables.JoinedTables) {
            Tables.JoinedTables jts = (Tables.JoinedTables) table;
            Row left = getRow(jts.getLeftTable());
            Row right = getRow(jts.getRightTable());
            if (left != null && right != null) {
                return new NestedLoopJoinRow(left, right, right.copy());
            }
        }
        return null;
    }
}
