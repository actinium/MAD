package mad.database.sql;

import java.util.Arrays;
import mad.database.sql.ast.expression.BetweenExpression;
import mad.database.sql.ast.expression.BinaryExpression;
import mad.database.sql.ast.expression.CaseExpression;
import mad.database.sql.ast.expression.ColumnExpression;
import mad.database.sql.ast.expression.Expression;
import mad.database.sql.ast.expression.LikeExpression;
import mad.database.sql.ast.expression.NotExpression;
import mad.database.sql.ast.expression.SubExpression;
import mad.database.sql.ast.expression.UnaryExpression;
import mad.database.sql.ast.expression.ValueExpression;
import mad.database.sql.ast.expression.ValueExpression.Value;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class ExpressionParserTest {

    public ExpressionParserTest() {
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
    public void testExpressionParser1() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "col1*col2+col3";
        Expression expected = new BinaryExpression(
                new BinaryExpression(
                        new ColumnExpression("col1"),
                        BinaryExpression.Operator.Multiply,
                        new ColumnExpression("col2")),
                BinaryExpression.Operator.Plus,
                new ColumnExpression("col3")
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser2() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "45 + -12";
        Expression expected = new BinaryExpression(
                new ValueExpression(Value.Type.Integer, "45"),
                BinaryExpression.Operator.Plus,
                new UnaryExpression(
                        UnaryExpression.Operator.Minus,
                        new ValueExpression(Value.Type.Integer, "12")
                )
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser3() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "col1 = \"table\".col2";
        Expression expected = new BinaryExpression(
                new ColumnExpression("col1"),
                BinaryExpression.Operator.Equals,
                new ColumnExpression("table", "col2")
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser4() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "age not between 0 and 18";
        Expression expected = new NotExpression(
                new BetweenExpression(
                        new ColumnExpression("age"),
                        new ValueExpression(Value.Type.Integer, "0"),
                        new ValueExpression(Value.Type.Integer, "18")
                )
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser5() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "'test' like 't%'";
        Expression expected = new LikeExpression(
                new ValueExpression(Value.Type.Text, "test"),
                new ValueExpression(Value.Type.Text, "t%"),
                null
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser6() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "(col1+col2)*(col3+col4)";
        Expression expected = new BinaryExpression(
                new SubExpression(new BinaryExpression(
                                new ColumnExpression("col1"),
                                BinaryExpression.Operator.Plus,
                                new ColumnExpression("col2"))),
                BinaryExpression.Operator.Multiply,
                new SubExpression(new BinaryExpression(
                                new ColumnExpression("col3"),
                                BinaryExpression.Operator.Plus,
                                new ColumnExpression("col4")))
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser7() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "name <> 'Tom' and name != 'Fiona'";
        Expression expected = new BinaryExpression(
                new BinaryExpression(
                        new ColumnExpression("name"),
                        BinaryExpression.Operator.NotEquals,
                        new ValueExpression(Value.Type.Text, "Tom")
                ),
                BinaryExpression.Operator.And,
                new BinaryExpression(
                        new ColumnExpression("name"),
                        BinaryExpression.Operator.NotEquals,
                        new ValueExpression(Value.Type.Text, "Fiona")
                )
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser8() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "col1 * col2 - col3 <= 100";
        Expression expected = new BinaryExpression(
                new BinaryExpression(
                        new BinaryExpression(
                                new ColumnExpression("col1"),
                                BinaryExpression.Operator.Multiply,
                                new ColumnExpression("col2")
                        ),
                        BinaryExpression.Operator.Minus,
                        new ColumnExpression("col3")
                ),
                BinaryExpression.Operator.LessThanOrEquals,
                new ValueExpression(Value.Type.Integer, "100")
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser9() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query = "5-2-1";
        Expression expected = new BinaryExpression(
                new BinaryExpression(
                        new ValueExpression(Value.Type.Integer, "5"),
                        BinaryExpression.Operator.Minus,
                        new ValueExpression(Value.Type.Integer, "2")
                ),
                BinaryExpression.Operator.Minus,
                new ValueExpression(Value.Type.Integer, "1")
        );
        testExpression(query, expected);
    }

    @Test
    public void testExpressionParser10() throws Tokenizer.TokenizeException, Parser.ParseError {
        String query
                = "case col1"
                + "  when 0 then '00'"
                + "  when 1 then '01'"
                + "  when 2 then '10'"
                + "  when 3 then '11'"
                + "end";
        Expression expected = new CaseExpression(
                new ColumnExpression("col1"),
                Arrays.asList(
                        new CaseExpression.Case(
                                new ValueExpression(Value.Type.Integer, "0"),
                                new ValueExpression(Value.Type.Text, "00")
                        ),
                        new CaseExpression.Case(
                                new ValueExpression(Value.Type.Integer, "1"),
                                new ValueExpression(Value.Type.Text, "01")
                        ),
                        new CaseExpression.Case(
                                new ValueExpression(Value.Type.Integer, "2"),
                                new ValueExpression(Value.Type.Text, "10")
                        ),
                        new CaseExpression.Case(
                                new ValueExpression(Value.Type.Integer, "3"),
                                new ValueExpression(Value.Type.Text, "11")
                        )
                ),
                null
        );
        testExpression(query, expected);
    }

    private void testExpression(String expString, Expression expression) throws
            Tokenizer.TokenizeException, Parser.ParseError {
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokenize(expString);
        Parser parser = new Parser(tokenizer);
        parser.readTokens();
        ExpressionParser expParser = new ExpressionParser(parser);
        Expression exp = expParser.parse();
        assertEquals(expression, exp);
    }

}
