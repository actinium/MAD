package mad.database.sql;

import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.Statement;

/**
 *
 */
public class DropTableParser {

    private final Parser parser;

    public DropTableParser(Parser parser) {
        this.parser = parser;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    public Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Drop)) {
            if (parser.accept(Tokenizer.Token.TokenType.Table)) {
                boolean ifExists = ifExists();
                String tableName = parser.parseIdentifier();
                parser.expect(Tokenizer.Token.TokenType.Semicolon);
                return new DropTableStatement(tableName,ifExists);
            }
        }
        return null;
    }

    private boolean ifExists() throws Parser.ParseError{
        if(parser.accept(Tokenizer.Token.TokenType.If)){
            parser.expect(Tokenizer.Token.TokenType.Exists);
            return true;
        }else{
            return false;
        }
    }
}
