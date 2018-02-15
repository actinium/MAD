package mad.database.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import mad.database.backend.table.Schema.Field;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.selection.SelectStatement;

/**
 *
 */
public class StatementProcessor {

    private DB db;

    public StatementProcessor(DB db) {
        this.db = db;
    }

    /**
     *
     * @param statement
     */
    public void executeUpdate(Statement statement) throws IOException, CreateTableError {
        if(statement instanceof CreateTableStatement){
            createTable((CreateTableStatement)statement);
        }
    }

    /**
     *
     * @param statement
     * @return
     */
    public Row executeQuery(SelectStatement statement) {
        return null;
    }

    /**
     *
     * @param cts
     */
    private void createTable(CreateTableStatement cts) throws IOException, CreateTableError {
        String name = cts.tableName();
        for (String tableName : db.getTableNames()) {
            if (name.equals(tableName)) {
                throw new CreateTableError("Error: Table allready excists!");
            }
        }
        List<Field> fields = new ArrayList<>();
        for (CreateTableStatement.ColumnDefinition cd : cts.tableDefinition()) {
            fields.add(new Field(cd.name, cd.type, cd.length));
        }
        db.createTable(name, new Schema(fields));
    }

    public class CreateTableError extends Exception {

        /**
         *
         * @param message
         */
        public CreateTableError(String message) {
            super(message);
        }
    }
}
