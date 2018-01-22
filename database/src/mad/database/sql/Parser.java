package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.sql.Tokenizer.Token;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class Parser {

    private final Tokenizer tokenizer;
    private final CreateTableParser createTableParser;
    private final DeleteParser deleteParser;
    private final DropTableParser dropTableParser;
    private final TruncateTableParser truncateTableParser;
    private final InsertParser insertParser;
    private final UpdateParser updateParser;

    private final ExpressionParser expressionParser;

    private final List<Token> tokens = new ArrayList<>();
    private int symbolIndex;

    /**
     *
     * @param tokenizer
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.createTableParser = new CreateTableParser(this);
        this.deleteParser = new DeleteParser(this);
        this.dropTableParser = new DropTableParser(this);
        this.truncateTableParser = new TruncateTableParser(this);
        this.insertParser = new InsertParser(this);
        this.updateParser = new UpdateParser(this);
        this.expressionParser = new ExpressionParser(this);
    }

    /**
     *
     */
    public void readTokens() {
        while (tokenizer.hasNext()) {
            Token token = tokenizer.next();
            tokens.add(token);
        }
    }

    //----------------------------------------------------------------------------------------------
    // Parser helper functions
    //----------------------------------------------------------------------------------------------
    /**
     *
     * @return symbolIndex == tokens.size()
     */
    public boolean done() {
        return symbolIndex == tokens.size();
    }

    /**
     *
     * @return tokens.get(tokens.size() - 1).type
     */
    public TokenType lastSymbol() {
        return tokens.get(tokens.size() - 1).type;
    }

    /**
     *
     * @return tokens.get(symbolIndex).type
     */
    private TokenType symbol() {
        return tokens.get(symbolIndex).type;
    }

    /**
     *
     * @return tokens.get(symbolIndex - 1)
     */
    Token token() {
        return tokens.get(symbolIndex - 1);
    }

    /**
     *
     * @return tokens.get(symbolIndex)
     */
    Token currentToken() {
        return tokens.get(symbolIndex);
    }

    /**
     *
     * @return
     */
    String value() {
        return tokens.get(symbolIndex - 1).value;
    }

    /**
     *
     * @return
     */
    String currentValue() {
        String vStr = tokens.get(symbolIndex).value;
        if (vStr == null) {
            vStr = symbol().stringValue();
        }
        return vStr;
    }

    /**
     *
     */
    void nextSymbol() {
        symbolIndex++;
    }

    /**
     *
     * @return
     */
    int getIndex() {
        return symbolIndex;
    }

    /**
     *
     * @param i
     */
    void setIndex(int i) {
        symbolIndex = i;
    }

    /**
     *
     * @param message
     */
    ParseError error(String message) {
        return new ParseError(message);
    }

    /**
     *
     * @param s
     * @return
     */
    boolean accept(Token.TokenType s) {
        if (!done() && symbol() == s) {
            nextSymbol();
            return true;
        }
        return false;
    }

    /**
     *
     * @param s
     * @return
     * @throws Parser.ParseError
     */
    boolean expect(Token.TokenType s) throws ParseError {
        if (accept(s)) {
            return true;
        }
        throw error("Parser-expect: unexpected symbol '" + currentValue() + "'. Expected '"
                + s.stringValue() + "'.");
    }

    //----------------------------------------------------------------------------------------------
    // Parsing
    //----------------------------------------------------------------------------------------------
    /**
     *
     * @return
     * @throws Parser.ParseError
     */
    public Statement parse() throws ParseError {
        Statement statement;
        if ((statement = createTableParser.parse()) != null
                || (statement = deleteParser.parse()) != null
                || (statement = dropTableParser.parse()) != null
                || (statement = truncateTableParser.parse()) != null
                || (statement = insertParser.parse()) != null
                || (statement = updateParser.parse()) != null) {
            return statement;
        }
        throw error("Parser-parse: Input didn't match a SQL Statement!");
    }

    /**
     *
     * @return
     * @throws Parser.ParseError
     */
    public Expression parseExpression() throws ParseError {
        return expressionParser.parse();
    }

    /**
     *
     * @return
     * @throws Parser.ParseError
     */
    String identifier() throws ParseError {
        if (accept(TokenType.ID) || accept(TokenType.StringID)) {
            return value();
        } else {
            throw error("Parser-identifier: '" + currentValue() + "' is not a valid identifier!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    int integerValue() throws ParseError {
        if (accept(TokenType.Integer)) {
            int value = 0;
            try {
                value = Integer.parseInt(value());
            } catch (NumberFormatException ex) {
                throw error("Parser-integerValue: Not a valid Integer!");
            }
            return value;
        } else {
            throw error("Parser-integerValue: Not a valid Integer!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    float floatValue() throws ParseError {
        if (accept(TokenType.Float)) {
            float value = 0;
            try {
                value = Float.parseFloat(value());
            } catch (NumberFormatException ex) {
                throw error("Parser-flaotValue: Not a valid Float!");
            }
            return value;
        } else {
            throw error("Parser-floatValue: Not a valid Float!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    boolean booleanValue() throws ParseError {
        if (accept(TokenType.Boolean)) {
            boolean value;
            value = Boolean.parseBoolean(value());
            return value;
        } else {
            throw error("Parser-booleanValue: Not a valid Boolean!");
        }
    }

    //----------------------------------------------------------------------------------------------
    // Exception
    //----------------------------------------------------------------------------------------------
    /**
     *
     */
    public static class ParseError extends Exception {

        /**
         *
         * @param message
         */
        public ParseError(String message) {
            super(message);
        }
    }
}
