package mad.database.sql;

import java.util.ArrayList;
import java.util.List;
import mad.database.backend.table.Schema;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.Statement;

/**
 *
 */
public class CreateTableParser {

    private final Parser parser;

    CreateTableParser(Parser parser) {
        this.parser = parser;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    Statement parse() throws Parser.ParseError {
        if (parser.accept(Tokenizer.Token.TokenType.Create)) {
            if (parser.accept(Tokenizer.Token.TokenType.Table)) {
                String tableName = parser.parseIdentifier();
                parser.expect(Tokenizer.Token.TokenType.LParen);
                List<CreateTableStatement.ColumnDefinition> td = tableDefinition();
                parser.expect(Tokenizer.Token.TokenType.RParen);
                parser.expect(Tokenizer.Token.TokenType.Semicolon);
                return new CreateTableStatement(tableName, td);
            }
        }
        return null;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private List<CreateTableStatement.ColumnDefinition> tableDefinition() throws Parser.ParseError {
        List<CreateTableStatement.ColumnDefinition> tableDefinition = new ArrayList<>();
        do {
            CreateTableStatement.ColumnDefinition cd = columnDefinition();
            tableDefinition.add(cd);
        } while (parser.accept(Tokenizer.Token.TokenType.Comma));
        return tableDefinition;
    }

    /**
     *
     * @return @throws Parser.ParseError
     */
    private CreateTableStatement.ColumnDefinition columnDefinition() throws Parser.ParseError {
        String name = parser.parseIdentifier();
        parser.expect(Tokenizer.Token.TokenType.ID);
        Schema.Field.Type type = null;
        int length = 0;
        switch (parser.value().toLowerCase()) {
            case "int":
            case "integer":
                type = Schema.Field.Type.Integer;
                break;
            case "float":
                type = Schema.Field.Type.Float;
                break;
            case "bool":
            case "boolean":
                type = Schema.Field.Type.Boolean;
                break;
            case "varchar":
                type = Schema.Field.Type.Varchar;
                parser.expect(Tokenizer.Token.TokenType.LParen);
                length = parser.parseInteger();
                parser.expect(Tokenizer.Token.TokenType.RParen);
                break;
            default:
                throw parser.error("columnDefinition: '" + parser.value() + "' is not a valid type!");
        }
        return new CreateTableStatement.ColumnDefinition(name, type, length);
    }
}
