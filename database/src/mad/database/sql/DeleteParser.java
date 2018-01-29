package mad.database.sql;

import mad.database.sql.ast.DeleteStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class DeleteParser {

    private final Parser parser;

    public DeleteParser(Parser parser) {
        this.parser = parser;
    }

    public Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Delete)) {
            parser.expect(Tokenizer.Token.TokenType.From);
            String tableName = parser.parseIdentifier();
            Expression condition = null;
            if (parser.accept(Tokenizer.Token.TokenType.Where)) {
                condition = parser.parseExpression();
            }
            parser.expect(Tokenizer.Token.TokenType.Semicolon);
            return new DeleteStatement(tableName, condition);
        }
        return null;
    }

}
