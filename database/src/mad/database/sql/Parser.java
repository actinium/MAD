package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.sql.Tokenizer.Token;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.InsertStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.TruncateTableStatement;

/**
 *
 */
public class Parser {

    private final Tokenizer tokenizer;
    private final CreateTableParser createTableParser;
    private final DropTableParser dropTableParser;
    private final List<Token> tokens = new ArrayList<>();
    private int symbolIndex;

    /**
     *
     * @param tokenizer
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.createTableParser = new CreateTableParser(this);
        this.dropTableParser = new DropTableParser(this);
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
     * @return
     */
    public TokenType lastSymbol() {
        return tokens.get(tokens.size() - 1).type;
    }

    /**
     *
     * @return
     */
    public boolean done() {
        return symbolIndex == tokens.size();
    }

    /**
     *
     * @return
     */
    private TokenType symbol() {
        return tokens.get(symbolIndex).type;
    }

    /**
     *
     * @return
     */
    private Token token() {
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
     */
    private void nextSymbol() {
        symbolIndex++;
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
        if (symbol() == s) {
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
        throw error("expect: unexpected symbol");
    }

    //----------------------------------------------------------------------------------------------
    // Parsing
    //----------------------------------------------------------------------------------------------
    /**
     *
     * @return @throws Parser.ParseError
     */
    public Statement parse() throws ParseError {
        Statement statement;
        if ((statement = createTableParser.parse()) != null
                || (statement = dropTableParser.parse()) != null
                || (statement = truncateTableStatement()) != null
                || (statement = insertStatement()) != null) {
            return statement;
        }
        throw error("parse: Input didn't match a SQL Statement!");
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    String identifier() throws ParseError {
        if (accept(TokenType.ID) || accept(TokenType.StringID)) {
            return value();
        } else {
            throw error("identifier: Not a valid identifier!");
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
                throw error("integerValue: Not a valid Integer!");
            }
            return value;
        } else {
            throw error("integerValue: Not a valid Integer!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private float floatValue() throws ParseError {
        if (accept(TokenType.Float)) {
            float value = 0;
            try {
                value = Float.parseFloat(value());
            } catch (NumberFormatException ex) {
                throw error("flaotValue: Not a valid Float!");
            }
            return value;
        } else {
            throw error("floatValue: Not a valid Float!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private boolean booleanValue() throws ParseError {
        if (accept(TokenType.Boolean)) {
            boolean value;
            value = Boolean.parseBoolean(value());
            return value;
        } else {
            throw error("booleanValue: Not a valid Boolean!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private String textValue() throws ParseError {
        if (accept(TokenType.Text) || accept(TokenType.StringID)) {
            return value();
        } else {
            throw error("textValue: Not a valid Text Value!");
        }
    }

    //----------------------------------------------------------------------------------------------
    // Statements
    //----------------------------------------------------------------------------------------------

    /**
     *
     * @return @throws Parser.ParseError
     */
    private Statement truncateTableStatement() throws ParseError {
        if (accept(TokenType.Truncate)) {
            if (accept(TokenType.Table)) {
                String tableName = identifier();
                expect(TokenType.Semicolon);
                return new TruncateTableStatement(tableName);
            }
        }
        return null;
    }

    private Statement insertStatement() throws ParseError {
        if (accept(TokenType.Insert)) {
            expect(TokenType.Into);
            String tableName = identifier();
            InsertStatement iStatement = new InsertStatement(tableName);

            columnList(iStatement);
            expect(TokenType.Values);
            valueList(iStatement);

            expect(TokenType.Semicolon);
            return iStatement;
        }
        return null;
    }

    /**
     *
     * @param istatement
     * @throws Parser.ParseError
     */
    private void columnList(InsertStatement istatement) throws ParseError {
        if (accept(TokenType.LParen)) {
            do {
                String columnName = identifier();
                istatement.addColumn(columnName);
            } while (accept(TokenType.Comma));
            expect(TokenType.RParen);
        }
    }

    /**
     *
     * @param istatement
     * @throws Parser.ParseError
     */
    private void valueList(InsertStatement istatement) throws ParseError {
        expect(TokenType.LParen);
        if (accept(TokenType.RParen)) {
            return;
        }
        do {
            switch (token().type) {
                case Null:
                    nextSymbol();
                    istatement.addValue(TokenType.Null, "NULL");
                    break;
                case Integer:
                    integerValue();
                    istatement.addValue(TokenType.Integer, value());
                    break;
                case Float:
                    floatValue();
                    istatement.addValue(TokenType.Float, value());
                    break;
                case Boolean:
                    booleanValue();
                    istatement.addValue(TokenType.Boolean, value());
                    break;
                case StringID:
                case Text:
                    textValue();
                    istatement.addValue(TokenType.Text, value());
                    break;
                default:
                    throw error("valueList: Token(" + token().type + ") did not match any valid type.");
            }
        } while (accept(TokenType.Comma));
        expect(TokenType.RParen);
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
