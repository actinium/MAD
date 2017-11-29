package mad.database.sql;

import java.util.Arrays;
import mad.database.backend.table.Schema;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.CreateTableStatement.ColumnDefinition;
import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.StatementList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ParserTest {

    public ParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testParser1() {
        String query
                = "CREATE TABLE employee(\n"
                + "  id    INTEGER,\n"
                + "  name  VARCHAR(42),\n"
                + "  email VARCHAR(100)\n"
                + ");";
        Statement statement = new CreateTableStatement("employee",
                Arrays.asList(
                        new ColumnDefinition("id", Schema.Field.Type.Integer),
                        new ColumnDefinition("name", Schema.Field.Type.Varchar, 42),
                        new ColumnDefinition("email", Schema.Field.Type.Varchar, 100)
                ));
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser2() {
        String query = "DROP TABLE test;\n";
        Statement statement = new DropTableStatement("test");
        testParser(query, new StatementList(statement));
    }

    private void testParser(String query, Statement statement) {
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(tokenizer);
        try {
            tokenizer.tokenize(query);
            parser.readTokens();
            StatementList sl = new StatementList();
            while (!parser.done()) {
                sl.add(parser.parse());
            }
            assertEquals(statement, sl);
        } catch (Tokenizer.TokenizeException ex) {
            fail("Error tokenizing input:" + ex.getMessage() + " (" + ex.getIndex() + ")");
        } catch (Parser.ParseError ex) {
            fail(ex.getMessage());
        }
    }
}
