package mad.database.sql;

import mad.database.sql.ast.Statement;
import mad.database.sql.ast.TruncateTableStatement;

/**
 *
 */
public class TruncateTableParser {

    private final Parser parser;

    public TruncateTableParser(Parser parser) {
        this.parser = parser;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    public Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Truncate)) {
            if (parser.accept(Tokenizer.Token.TokenType.Table)) {
                String tableName = parser.identifier();
                parser.expect(Tokenizer.Token.TokenType.Semicolon);
                return new TruncateTableStatement(tableName);
            }
        }
        return null;
    }
}
