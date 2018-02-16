package mad.database.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mad.database.backend.expression.StaticExpressionProcessor;
import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.Row;
import mad.database.backend.table.Schema;
import mad.database.backend.table.Schema.Field;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.InsertStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.Tables;
import mad.database.sql.ast.expression.ValueExpression;
import mad.database.sql.ast.expression.ValueExpression.Value;
import mad.database.sql.ast.selection.SelectStatement;
import mad.database.sql.ast.selection.SimpleSelectStatement;

/**
 *
 */
public class StatementProcessor {

    private final DB db;

    public StatementProcessor(DB db) {
        this.db = db;
    }

    /**
     *
     * @param statement
     * @throws java.io.IOException
     * @throws mad.database.backend.StatementProcessor.CreateTableException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     */
    public void executeUpdate(Statement statement) throws IOException, CreateTableException,
            Row.TypeMismatchException, StaticExpressionProcessor.NonStaticVariableException,
            NoSuchTableException {
        if (statement instanceof CreateTableStatement) {
            createTable((CreateTableStatement) statement);
        }
        if (statement instanceof InsertStatement) {
            insertRow((InsertStatement) statement);
        }
    }

    /**
     *
     * @param statement
     * @return
     */
    public Row executeQuery(SelectStatement statement) throws IOException {
        if (statement instanceof SimpleSelectStatement) {
            SimpleSelectStatement simpleSelect = (SimpleSelectStatement) statement;
            if (simpleSelect.getTables() instanceof Tables.SingleTable) {
                Tables.SingleTable table = (Tables.SingleTable) simpleSelect.getTables();
                String tableName = table.getTableName();
                if (db.hasTable(tableName)) {
                    return db.getFirstRow(db.getTablePointer(tableName));
                }
            }
        }
        return null;
    }

    /**
     *
     * @param cts
     * @throws IOException
     * @throws mad.database.backend.StatementProcessor.CreateTableException
     */
    private void createTable(CreateTableStatement cts) throws IOException, CreateTableException {
        String name = cts.tableName();
        for (String tableName : db.getTableNames()) {
            if (name.equals(tableName)) {
                throw new CreateTableException("Error: Table allready exists!");
            }
        }
        List<Field> fields = new ArrayList<>();
        for (CreateTableStatement.ColumnDefinition cd : cts.tableDefinition()) {
            fields.add(new Field(cd.name, cd.type, cd.length));
        }
        db.createTable(name, new Schema(fields));
    }

    /**
     *
     */
    public class CreateTableException extends Exception {

        /**
         *
         * @param message
         */
        public CreateTableException(String message) {
            super(message);
        }
    }

    /**
     *
     * @param statement
     * @throws IOException
     * @throws mad.database.backend.table.Row.TypeMismatchException
     * @throws mad.database.backend.expression.StaticExpressionProcessor.NonStaticVariableException
     * @throws mad.database.backend.StatementProcessor.NoSuchTableException
     */
    private void insertRow(InsertStatement statement) throws IOException, Row.TypeMismatchException, StaticExpressionProcessor.NonStaticVariableException, NoSuchTableException {
        String tableName = statement.tableName();
        if (!db.hasTable(tableName)) {
            throw new NoSuchTableException("Error: No table with that name.");
        }
        Schema schema = db.getSchema(tableName);
        ArrayRow row = new ArrayRow();
        for (int i = 0; i < schema.columns(); i++) {
            Field field = schema.get(i);
            Value value;
            if (statement.columns().isEmpty()) {
                if (statement.values().size() != schema.columns()) {
                    throw new Row.TypeMismatchException("Error: Number of values does not match number of columns.");
                }
                ValueExpression vexp = StaticExpressionProcessor.evaluate(statement.values().get(i));
                value = vexp.getValue();
            } else {
                ValueExpression vexp = StaticExpressionProcessor.evaluate(statement.getValue(field.name));
                value = vexp.getValue();
            }
            switch (field.type) {
                case Boolean:
                    if (value.type() == Value.Type.Boolean) {
                        row.addBooleanColumn(field.name, Boolean.parseBoolean(value.value()));
                    } else if (value.type() == Value.Type.Null) {
                        row.addNullBooleanColumn(field.name);
                    } else {
                        throw new Row.TypeMismatchException("Error: Value type did not match column type.");
                    }
                    break;
                case Float:
                    if (value.type() == Value.Type.Float || value.type() == Value.Type.Integer) {
                        row.addFloatColumn(field.name, Float.parseFloat(value.value()));
                    } else if (value.type() == Value.Type.Null) {
                        row.addNullFloatColumn(field.name);
                    } else {
                        throw new Row.TypeMismatchException("Error: Value type did not match column type.");
                    }
                    break;
                case Integer:
                    if (value.type() == Value.Type.Integer) {
                        row.addIntegerColumn(field.name, Integer.parseInt(value.value()));
                    } else if (value.type() == Value.Type.Null) {
                        row.addNullIntegerColumn(field.name);
                    } else {
                        throw new Row.TypeMismatchException("Error: Value type did not match column type.");
                    }
                    break;
                case Varchar:
                    if (value.type() == Value.Type.Text) {
                        row.addStringColumn(field.name, value.value());
                    } else if (value.type() == Value.Type.Null) {
                        row.addNullStringColumn(field.name);
                    } else {
                        throw new Row.TypeMismatchException("Error: Value type did not match column type.");
                    }
                    break;
            }

        }
        db.insertRow(tableName, row);
    }

    /**
     *
     */
    public static class NoSuchTableException extends Exception {

        public NoSuchTableException(String message) {
            super(message);
        }
    }
}
