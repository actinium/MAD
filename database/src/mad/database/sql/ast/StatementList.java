package mad.database.sql.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class StatementList{

    List<Statement> statements;

    /**
     *
     */
    public StatementList() {
        statements = new ArrayList<>();
    }

    /**
     *
     * @param statement
     */
    public StatementList(Statement statement) {
        statements = new ArrayList<>();
        statements.add(statement);
    }

    /**
     *
     * @param s
     */
    public void add(Statement s) {
        statements.add(s);
    }

    public List<Statement> getList(){
        return Collections.unmodifiableList(statements);
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

    public boolean equals(Object obj) {
        if (!(obj instanceof StatementList)) {
            return false;
        }
        StatementList objSL = (StatementList) obj;
        if (objSL.statements.size() != statements.size()) {
            return false;
        }
        for (int i = 0; i < statements.size(); i++) {
            if (!objSL.statements.get(i).equals(statements.get(i))) {
                return false;
            }
        }
        return true;
    }
}
