package mad.database.sql;

import mad.database.sql.Tokenizer.Token.TokenType;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.UpdateStatement;
import mad.database.sql.ast.expression.Expression;

/**
 *
 */
public class UpdateParser {

    private final Parser parser;

    public UpdateParser(Parser parser) {
        this.parser = parser;
    }

    public Statement parse() throws Parser.ParseError {
        if(parser.accept(TokenType.Update)){
            String tableName = parser.identifier();
            parser.expect(TokenType.Set);
            UpdateStatement statement = new UpdateStatement(tableName);
            assignment(statement);
            if(parser.accept(TokenType.Where)){
                Expression condition = parser.parseExpression();
                statement = new UpdateStatement(statement, condition);
            }
            parser.expect(TokenType.Semicolon);
            return statement;
        }
        return null;
    }

    private void assignment(UpdateStatement statement) throws Parser.ParseError{
        do{
            String columnName = parser.identifier();
            parser.expect(TokenType.Equals);
            Expression value = parser.parseExpression();
            statement.addUpdate(columnName, value);
        }while(parser.accept(TokenType.Comma));
    }
}
