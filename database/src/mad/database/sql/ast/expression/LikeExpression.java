package mad.database.sql.ast.expression;

/**
 *
 */
public class LikeExpression implements Expression {

    private final Expression text;
    private final Expression pattern;
    private final Expression escape;

    public LikeExpression(Expression text, Expression pattern, Expression escape) {
        this.text = text;
        this.pattern = pattern;
        this.escape = escape;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("like:");
        sb.append(text).append(",");
        sb.append(pattern);
        if (escape != null) {
            sb.append(",").append(escape);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LikeExpression)) {
            return false;
        }
        LikeExpression likeObj = (LikeExpression) obj;
        if (likeObj.escape != null && escape != null && !likeObj.escape.equals(escape)) {
            return false;
        }
        if(likeObj.escape == null && escape != null){
            return false;
        }
        if(likeObj.escape != null && escape == null){
            return false;
        }
        return likeObj.text.equals(text) && likeObj.pattern.equals(pattern);
    }
}
