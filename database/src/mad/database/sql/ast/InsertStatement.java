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
     * @return
     */
    public InsertStatement addColumn(String columnName) {
        columns.add(columnName);
        return this;
    }

    /**
     *
     * @param type
     * @param value
     * @return
     */
    public InsertStatement addValue(TokenType type, String value) {
        values.add(new Value(type, value));
        return this;
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
        if (columns.isEmpty()) {
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
            sb.append("    ").append(v.value()).append(": ").append(v.type());
            sb.append(";\n");
        }
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InsertStatement)) {
            return false;
        }
        InsertStatement objIS = (InsertStatement) obj;
        if (!objIS.tableName().equals(tableName())) {
            return false;
        }
        if (objIS.columns().size() != columns().size()) {
            return false;
        }
        for (int i = 0; i < columns().size(); i++) {
            if (!columns().get(i).equals(objIS.columns().get(i))) {
                return false;
            }
        }
        if (objIS.values().size() != values().size()) {
            return false;
        }
        for (int i = 0; i < values().size(); i++) {
            if (values().get(i).type() != objIS.values().get(i).type()) {
                return false;
            }
            if (!values().get(i).value().equals(objIS.values().get(i).value())) {
                return false;
            }
        }
        return true;
    }

}
