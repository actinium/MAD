package mad.database.sql;

import mad.database.sql.ast.InsertStatement;
import mad.database.sql.ast.Statement;

/**
 *
 */
public class InsertParser {
    private final Parser parser;

    public InsertParser(Parser parser) {
        this.parser = parser;
    }
    
    public Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Insert)) {
            parser.expect(Tokenizer.Token.TokenType.Into);
            String tableName = parser.identifier();
            InsertStatement iStatement = new InsertStatement(tableName);

            columnList(iStatement);
            parser.expect(Tokenizer.Token.TokenType.Values);
            valueList(iStatement);

            parser.expect(Tokenizer.Token.TokenType.Semicolon);
            return iStatement;
        }
        return null;
    }

    /**
     *
     * @param istatement
     * @throws Parser.ParseError
     */
    private void columnList(InsertStatement istatement) throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.LParen)) {
            do {
                String columnName = parser.identifier();
                istatement.addColumn(columnName);
            } while (parser.accept(Tokenizer.Token.TokenType.Comma));
            parser.expect(Tokenizer.Token.TokenType.RParen);
        }
    }

    /**
     *
     * @param istatement
     * @throws Parser.ParseError
     */
    private void valueList(InsertStatement istatement) throws Parser.ParseError {
        parser.expect(Tokenizer.Token.TokenType.LParen);
        if (parser.accept(Tokenizer.Token.TokenType.RParen)) {
            return;
        }
        do {
            switch (parser.token().type) {
                case Null:
                    parser.nextSymbol();
                    istatement.addValue(Tokenizer.Token.TokenType.Null, "NULL");
                    break;
                case Integer:
                    parser.integerValue();
                    istatement.addValue(Tokenizer.Token.TokenType.Integer, parser.value());
                    break;
                case Float:
                    parser.floatValue();
                    istatement.addValue(Tokenizer.Token.TokenType.Float, parser.value());
                    break;
                case Boolean:
                    parser.booleanValue();
                    istatement.addValue(Tokenizer.Token.TokenType.Boolean, parser.value());
                    break;
                case StringID:
                case Text:
                    parser.textValue();
                    istatement.addValue(Tokenizer.Token.TokenType.Text, parser.value());
                    break;
                default:
                    throw parser.error("valueList: Token(" + parser.token().type + ") did not match any valid type.");
            }
        } while (parser.accept(Tokenizer.Token.TokenType.Comma));
        parser.expect(Tokenizer.Token.TokenType.RParen);
    }
}
