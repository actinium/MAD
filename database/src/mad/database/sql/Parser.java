package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import mad.database.backend.table.Schema.Field;
import mad.database.sql.Tokenizer.Token;
import mad.database.sql.Tokenizer.Token.Type;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.StatementList;

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

    /**
     *
     * @return
     */
    Type symbol() {
        return tokens.get(symbolIndex).type;
    }

    /**
     *
     * @return
     */
    public Type lastSymbol() {
        return tokens.get(tokens.size() - 1).type;
    }

    /**
     *
     * @return
     */
    Token token() {
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
    void nextSymbol() {
        symbolIndex++;
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
     * @param message
     * @throws Parser.ParseError
     */
    ParseError error(String message) throws ParseError {
        return new ParseError(message);
    }

    /**
     *
     * @param s
     * @return
     */
    boolean accept(Token.Type s) {
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
     */
    boolean expect(Token.Type s) throws ParseError {
        if (accept(s)) {
            return true;
        }
        throw error("expect: unexpected symbol");
    }

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
    String identifier() throws ParseError {
        if (accept(Type.ID)) {
            return value();
        } else if (accept(Type.StringID)) {
            return value();
        } else {
            throw error("identifier: Not a valid identifier!");
        }
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    CreateTableStatement createTableStatement() throws ParseError {
        if (accept(Type.Create)) {
            if (accept(Type.Table)) {
                String tableName = identifier();
                expect(Type.LParen);
                List<CreateTableStatement.ColumnDefinition> td = tableDefinition();
                expect(Type.RParen);
                expect(Type.Semicolon);
                return new CreateTableStatement(tableName, td);
            }
        }
        return null;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    List<CreateTableStatement.ColumnDefinition> tableDefinition() throws ParseError {
        List<CreateTableStatement.ColumnDefinition> tableDefinition = new ArrayList<>();
        do {
            CreateTableStatement.ColumnDefinition cd = columnDefinition();
            tableDefinition.add(cd);
        } while (accept(Type.Comma));
        return tableDefinition;
    }

    CreateTableStatement.ColumnDefinition columnDefinition() throws ParseError {
        String name = identifier();
        expect(Type.ID);
        Field.Type type = null;
        int length = 0;
        switch (value()) {
            case "integer":
                type = Field.Type.Integer;
                break;
            case "float":
                type = Field.Type.Float;
                break;
            case "boolean":
                type = Field.Type.Boolean;
                break;
            case "varchar":
                type = Field.Type.Varchar;
                expect(Type.LParen);
                length = integer();
                expect(Type.RParen);
        }
        return new CreateTableStatement.ColumnDefinition(name, type, length);
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    DropTableStatement dropTableStatement() throws ParseError {
        if (accept(Type.Drop)) {
            if (accept(Type.Table)) {
                String tableName = identifier();
                expect(Type.Semicolon);
                return new DropTableStatement(tableName);
            }
        }
        return null;
    }

    int integer() throws ParseError {
        if (accept(Type.Integer)) {
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
