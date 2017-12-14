package mad.database.sql.ast.expression;

/**
 *
 */
public class RegexpExpression implements Expression {

    private final Expression text;
    private final Expression pattern;

    public RegexpExpression(Expression text, Expression pattern) {
        this.text = text;
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("regexp:");
        sb.append(text).append(",");
        sb.append(pattern);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LikeExpression)) {
            return false;
        }
        RegexpExpression regexpObj = (RegexpExpression) obj;
        return regexpObj.text.equals(text) && regexpObj.pattern.equals(pattern);
    }
}
