package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mad.database.sql.Tokenizer.Token.TokenType;

/**
 *
 */
public class InsertStatement implements Statement {

    private final String tableName;
    private final List<String> columns;
    private final List<Value> values;

    /**
     *
     * @param tableName
     */
    public InsertStatement(String tableName) {
        this.tableName = tableName;
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public String tableName() {
        return tableName;
    }

    /**
     *
     * @param columnName
     */
    public void addColumn(String columnName) {
        columns.add(columnName);
    }

    public void addValue(TokenType type, String value) {
        values.add(new Value(type, value));
    }

    /**
     *
     * @return
     */
    public List<String> columns() {
        return Collections.unmodifiableList(columns);
    }

    /**
     *
     * @return
     */
    public List<Value> values() {
        return Collections.unmodifiableList(values);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InsertStatement{\n");
        sb.append("  tablename = ").append(tableName).append(";\n");
        sb.append("  columns(").append(columns.size()).append(") = ");
        if (columns.size() == 0) {
            sb.append("ALL;\n");
        } else {
            sb.append("{\n");
            for (String c : columns) {
                sb.append("    ").append(c);
                sb.append(";\n");
            }
            sb.append("  }\n");
        }
        sb.append("  values(").append(values.size()).append(") = {\n");
        for (Value v : values) {
            sb.append("    ").append(v.value()).append(": ").append(v.type);
            sb.append(";\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     *
     */
    public static class Value {

        private final TokenType type;
        private final String value;

        /**
         *
         * @param type
         * @param value
         */
        public Value(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        /**
         *
         * @return
         */
        public TokenType type() {
            return type;
        }

        /**
         *
         * @return
         */
        public String value() {
            return value;
        }
    }
}
