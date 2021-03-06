package mad.database.sql;

import java.util.Arrays;
import mad.database.backend.table.Schema;
import mad.database.sql.ast.CreateTableStatement;
import mad.database.sql.ast.CreateTableStatement.ColumnDefinition;
import mad.database.sql.ast.DropTableStatement;
import mad.database.sql.ast.InsertStatement;
import mad.database.sql.ast.Statement;
import mad.database.sql.ast.StatementList;
import mad.database.sql.ast.TruncateTableStatement;
import mad.database.sql.ast.expression.ColumnExpression;
import mad.database.sql.ast.expression.ValueExpression;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        String query
                = "create table employee(\n"
                + "  id    int,\n"
                + "  name  varchar(42),\n"
                + "  email varchar(100)\n"
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
    public void testParser3() {
        String query = "DROP TABLE test;\n";
        Statement statement = new DropTableStatement("test");
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser4() {
        String query = "drop table test;\n";
        Statement statement = new DropTableStatement("test");
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser5() {
        String query = "INSERT INTO department(id,name) VALUES(12,'Department of Magic');\n";
        InsertStatement iStatement = new InsertStatement("department");
        iStatement.addColumn("id").addColumn("name");
        iStatement.addValue(new ValueExpression(ValueExpression.Value.Type.Integer, "12"));
        iStatement.addValue(new ValueExpression(ValueExpression.Value.Type.Text, "Department of Magic"));
        testParser(query, new StatementList(iStatement));
    }

    @Test
    public void testParser6() {
        String query = "insert into department values(12,'Department of Magic');\n";
        InsertStatement iStatement = new InsertStatement("department");
        iStatement.addValue(new ValueExpression(ValueExpression.Value.Type.Integer, "12"));
        iStatement.addValue(new ValueExpression(ValueExpression.Value.Type.Text, "Department of Magic"));
        testParser(query, new StatementList(iStatement));
    }

    @Test
    public void testParser7() {
        String query = "TRUNCATE TABLE test;\n";
        Statement statement = new TruncateTableStatement("test");
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser8() {
        String query = "truncate table test;\n";
        Statement statement = new TruncateTableStatement("test");
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser9() {
        String query = "insert into people(name,age) values(\"Tom\",34);\n";
        InsertStatement statement = new InsertStatement("people");
        statement.addColumn("name");
        statement.addValue(new ColumnExpression("Tom", true));
        statement.addColumn("age");
        statement.addValue(new ValueExpression(ValueExpression.Value.Type.Integer, "34"));
        testParser(query, new StatementList(statement));
    }

    @Test
    public void testParser10() {
        String query = "insert into people(name,age) values('Tom',34);\n";
        InsertStatement statement = new InsertStatement("people");
        statement.addColumn("name");
        statement.addValue(new ValueExpression(ValueExpression.Value.Type.Text, "Tom"));
        statement.addColumn("age");
        statement.addValue(new ValueExpression(ValueExpression.Value.Type.Integer, "34"));
        testParser(query, new StatementList(statement));
    }

    private void testParser(String query, StatementList statements) {
        Tokenizer tokenizer = new Tokenizer();
        Parser parser = new Parser(tokenizer);
        try {
            tokenizer.tokenize(query);
            parser.readTokens();
            StatementList sl = new StatementList();
            while (!parser.done()) {
                sl.add(parser.parse());
            }
            assertEquals(statements, sl);
        } catch (Tokenizer.TokenizeException ex) {
            fail("Error tokenizing input:" + ex.getMessage() + " (" + ex.getIndex() + ")");
        } catch (Parser.ParseError ex) {
            fail(ex.getMessage());
        }
    }
}
