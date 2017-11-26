package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.backend.table.Schema.Field;
import mad.database.sql.Tokenizer.Token;
import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.CreateTableStatement.ColumnDefinition;
import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.Statement;

/**
 *
 */
public class Parser {

    Tokenizer tokenizer;
    List<Token> tokens = new ArrayList<>();
    int symbolIndex;

    /**
     *
     * @param tokenizer
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
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
    private String value() {
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
    private ParseError error(String message) {
        return new ParseError(message);
    }

    /**
     *
     * @param s
     * @return
     */
    private boolean accept(Token.TokenType s) {
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
    private boolean expect(Token.TokenType s) throws ParseError {
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
        if ((statement = createTableStatement()) != null) {
            return statement;
        } else if ((statement = dropTableStatement()) != null) {
            return statement;
        }
        throw error("parse: Input didn't match a SQL Statement!");
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private String identifier() throws ParseError {
        if (accept(TokenType.ID)) {
            return value();
        } else if (accept(TokenType.StringID)) {
            return value();
        } else {
            throw error("identifier: Not a valid identifier!");
        }
    }

    /**
     *
     * @return @throws mad.database.sql.Parser.ParseError
     */
    private int integer() throws ParseError {
        if (accept(TokenType.Integer)) {
            int value = 0;
            try {
                value = Integer.parseInt(value());
            } catch (NumberFormatException ex) {
                throw error("integer: Not a valid Integer!");
            }
            return value;
        } else {
            throw error("integer: Not a valid Integer!");
        }
    }

    //----------------------------------------------------------------------------------------------
    // Statements
    //----------------------------------------------------------------------------------------------
    /**
     *
     * @return @throws Parser.ParseError
     */
    private CreateTableStatement createTableStatement() throws ParseError {
        if (accept(TokenType.Create)) {
            if (accept(TokenType.Table)) {
                String tableName = identifier();
                expect(TokenType.LParen);
                List<ColumnDefinition> td = tableDefinition();
                expect(TokenType.RParen);
                expect(TokenType.Semicolon);
                return new CreateTableStatement(tableName, td);
            }
        }
        return null;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private List<ColumnDefinition> tableDefinition() throws ParseError {
        List<ColumnDefinition> tableDefinition = new ArrayList<>();
        do {
            ColumnDefinition cd = columnDefinition();
            tableDefinition.add(cd);
        } while (accept(TokenType.Comma));
        return tableDefinition;
    }

    /**
     *
     * @return @throws mad.database.sql.Parser.ParseError
     */
    private ColumnDefinition columnDefinition() throws ParseError {
        String name = identifier();
        expect(TokenType.ID);
        Field.Type type = null;
        int length = 0;
        switch (value()) {
            case "int":
            case "integer":
                type = Field.Type.Integer;
                break;
            case "float":
                type = Field.Type.Float;
                break;
            case "bool":
            case "boolean":
                type = Field.Type.Boolean;
                break;
            case "varchar":
                type = Field.Type.Varchar;
                expect(TokenType.LParen);
                length = integer();
                expect(TokenType.RParen);
                break;
            default:
                throw error("columnDefinition: '" + value() + "' is not a valid type!");
        }
        return new ColumnDefinition(name, type, length);
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    DropTableStatement dropTableStatement() throws ParseError {
        if (accept(TokenType.Drop)) {
            if (accept(TokenType.Table)) {
                String tableName = identifier();
                expect(TokenType.Semicolon);
                return new DropTableStatement(tableName);
            }
        }
        return null;
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
