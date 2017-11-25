package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StatementList implements Statement {

    List<Statement> statements;

    /**
     *
     */
    public StatementList() {
        statements = new ArrayList<>();
    }

    /**
     *
     * @param s
     */
    public void add(Statement s) {
        statements.add(s);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatementList{\n");
        for (Statement s : statements) {
            String lines[] = s.toString().split("\\r\\n|\\n|\\r");
            for (String line : lines) {
                sb.append("  ").append(line).append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
