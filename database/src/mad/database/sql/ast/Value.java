package mad.database.sql.ast;

import mad.database.sql.Tokenizer;

/**
 *
 */
public class Value {

    private final Tokenizer.Token.TokenType type;
    private final String value;

    /**
     *
     * @param type
     * @param value
     */
    public Value(Tokenizer.Token.TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public Tokenizer.Token.TokenType type() {
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
