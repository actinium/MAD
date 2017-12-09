package mad.database.sql.ast.expression;

/**
 *
 */
public class ValueExpression implements Expression {

    private final Value value;

    public ValueExpression(Value.Type type, String value) {
        this.value = new Value(type, value);
    }

    public ValueExpression(Value value) {
        this.value = value;
    }

    public static ValueExpression nullValue() {
        return new ValueExpression(new Value());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(value.type()).append(",").append(value.value());
        sb.append("}");
        return sb.toString();
    }

    public static class Value {

        private final Type type;
        private final String value;

        public Value(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        private Value() {
            this.type = Type.Null;
            this.value = "null";
        }

        private Type type() {
            return type;
        }

        private String value() {
            return value;
        }

        public enum Type {

            Integer,
            Float,
            Boolean,
            Text,
            Null
        }
    }
}
