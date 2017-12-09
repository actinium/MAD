package mad.database.sql.ast.expression;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class FunctionExpression implements Expression {

    private final String functionName;
    private final List<Expression> arguments;

    public FunctionExpression(String functionName, List<Expression> arguments) {
        this.functionName = functionName;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{function:");
        sb.append(functionName).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            sb.append(arguments.get(i));
            if (i != arguments.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        sb.append("}");
        return sb.toString();
    }
}
