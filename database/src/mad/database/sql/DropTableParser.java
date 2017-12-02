
package mad.database.sql;

import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.Statement;

/**
 *
 */
public class DropTableParser {
    private final Parser parser;
    
    public DropTableParser(Parser parser){
        this.parser = parser;
    }
    
    /**
     *
     * @return @throws Parser.ParseError
     */
    public Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Drop)) {
            if (parser.accept(Tokenizer.Token.TokenType.Table)) {
                String tableName = parser.identifier();
                parser.expect(Tokenizer.Token.TokenType.Semicolon);
                return new DropTableStatement(tableName);
            }
        }
        return null;
    }
}
