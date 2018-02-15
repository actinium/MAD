package mad.database.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import mad.database.sql.Tokenizer.Token;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class TokenizerTest {

    public TokenizerTest() {
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

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer() {
        String query = "select * from tablename;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer2() {
        String query = "select true;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Boolean, "true"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer3() {
        String query = "select tableName;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "tableName"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer4() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select (tableName);\n");
            tokenizer.tokenize("select \"TextID\";\n");
        } catch (Tokenizer.TokenizeException ex) {
            fail("" + ex.getIndex() + ": " + ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.ID, "tableName"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon),
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.StringID, "TextID"),
                new Token(Token.TokenType.Semicolon)));

        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer;

        while (result.hasNext() && expResult.hasNext()) {
            assertEquals(expResult.next(), result.next());
        }

        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer5() {
        String query = "select ( 'Text with a char: ''c'' and a string: \"str\". Hello World!' ); ";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.Text, "Text with a char: 'c' and a string: \"str\". Hello World!"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer6() {
        String query = "select 'Text: ''Hello World!'',  Char: ''c'''; ";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Text, "Text: 'Hello World!',  Char: 'c'"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer7() {
        Tokenizer tokenizer = new Tokenizer();

        try {
            tokenizer.tokenize("select (\"select\"); ");
        } catch (Tokenizer.TokenizeException ex) {
            fail("" + ex.getIndex() + ": " + ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.StringID, "select"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon)));

        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer;
        while (result.hasNext() && expResult.hasNext()) {
            assertEquals(expResult.next(), result.next());
        }
        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());

        try {
            tokenizer.tokenize("select (tableName);");
        } catch (Tokenizer.TokenizeException ex) {
            fail("" + ex.getIndex() + ": " + ex.getMessage());
        }
        ArrayList<Token> tokens2 = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.ID, "tableName"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon)));

        Iterator<Tokenizer.Token> expResult2 = tokens2.iterator();
        Iterator<Tokenizer.Token> result2 = tokenizer;
        while (result2.hasNext() && expResult2.hasNext()) {
            assertEquals(expResult2.next(), result2.next());
        }
        assertFalse(result2.hasNext());
        assertFalse(expResult2.hasNext());

    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer8() {
        String query = "select 12.34;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Float, "12.34"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer9() {
        String query = "select (1234);";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.Integer, "1234"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer10() {
        String query = "select col1,col2 from tablename where col1 = 12;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer11() {
        String query = "select * from table1,table2 where table1.col2 = table2.col1;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "table1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "table2"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "table1"),
                new Token(Token.TokenType.Dot),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.ID, "table2"),
                new Token(Token.TokenType.Dot),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer12() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 == 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer13() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 != 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.NotEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer14() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 <> 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.NotEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer15() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 < 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.LessThan),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer16() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 > 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.GreaterThan),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer17() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 <= 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.LessThanOrEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer18() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 >= 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.GreaterThanOrEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer19() {
        String query
                = "select col1,col2\n"
                + "   from tablename\n"
                + "   where col1 == 'text' || 'text';";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Text, "text"),
                new Token(Token.TokenType.Concat),
                new Token(Token.TokenType.Text, "text"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer20() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where value%2=0;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.Modulo),
                new Token(Token.TokenType.Integer, "2"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "0"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer21() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where value+2=20;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.Plus),
                new Token(Token.TokenType.Integer, "2"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "20"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer22() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where value-10=1;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.Minus),
                new Token(Token.TokenType.Integer, "10"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "1"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer23() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where 0.5*(value+3)<33.3;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.Float, "0.5"),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.Plus),
                new Token(Token.TokenType.Integer, "3"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.LessThan),
                new Token(Token.TokenType.Float, "33.3"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer24() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where 0.5*(value+0.1)>=-0.3;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.Float, "0.5"),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.Plus),
                new Token(Token.TokenType.Float, "0.1"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.GreaterThanOrEquals),
                new Token(Token.TokenType.Minus),
                new Token(Token.TokenType.Float, "0.3"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer25() {
        String query
                = "select *\n"
                + "   from tablename\n"
                + "   where 0 <= col1 and col1 < 100;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.Integer, "0"),
                new Token(Token.TokenType.LessThanOrEquals),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.And),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.LessThan),
                new Token(Token.TokenType.Integer, "100"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer26() {
        String query
                = "select *\n"
                + "   from tablename\n"
                + "   where col1 & 1 == 1;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.BitwiseAnd),
                new Token(Token.TokenType.Integer, "1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Integer, "1"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer27() {
        String query
                = "select *\n"
                + "   from tablename\n"
                + "   where col1==col2 or col1==col3;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.Or),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.ID, "col3"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer28() {
        String query
                = "select *\n"
                + "   from tablename\n"
                + "   where col1 | col2 == 1;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.BitwiseOr),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Integer, "1"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer29() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where value<<10=100;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.LeftShift),
                new Token(Token.TokenType.Integer, "10"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "100"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer30() {
        String query
                = "select *\n"
                + "   from numbers\n"
                + "   where value>>10=0;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.Star),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "numbers"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "value"),
                new Token(Token.TokenType.RightShift),
                new Token(Token.TokenType.Integer, "10"),
                new Token(Token.TokenType.Equals),
                new Token(Token.TokenType.Integer, "0"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer31() {
        String query = "select \"true\" from notnot;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.StringID, "true"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "notnot"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer32() {
        String query
                = "--Hello Comments\n"
                + "select col1,col2\n"
                + "   from tablename -- Usefullcomment about tablename\n"
                + "   where col1 == 12;";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer33() {
        String query
                = "select col1,col2\n"
                + "   -- hello col1 & col2\n"
                + "   from tablename\n"
                + "   where col1 == 12;-- end comment";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Select),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "col2"),
                new Token(Token.TokenType.From),
                new Token(Token.TokenType.ID, "tablename"),
                new Token(Token.TokenType.Where),
                new Token(Token.TokenType.ID, "col1"),
                new Token(Token.TokenType.DoubleEquals),
                new Token(Token.TokenType.Integer, "12"),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer34() {
        String query
                = "create table employee(\n"
                + "   id integer,\n"
                + "   name varchar(50),\n"
                + "   salary integer\n"
                + ");";

        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Create),
                new Token(Token.TokenType.Table),
                new Token(Token.TokenType.ID, "employee"),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.ID, "id"),
                new Token(Token.TokenType.ID, "integer"),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "name"),
                new Token(Token.TokenType.ID, "varchar"),
                new Token(Token.TokenType.LParen),
                new Token(Token.TokenType.Integer, "50"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Comma),
                new Token(Token.TokenType.ID, "salary"),
                new Token(Token.TokenType.ID, "integer"),
                new Token(Token.TokenType.RParen),
                new Token(Token.TokenType.Semicolon)));

        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer35() {
        String query = "drop\ntable\nfox;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Drop),
                new Token(Token.TokenType.Table),
                new Token(Token.TokenType.ID, "fox"),
                new Token(Token.TokenType.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer36() {
        String query = "drop\ntable";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Drop),
                new Token(Token.TokenType.Table)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer37() {
        String query = "drop\r\ntable";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Drop),
                new Token(Token.TokenType.Table)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer38() {
        String query = "drop\rtable\r";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.TokenType.Drop),
                new Token(Token.TokenType.Table)));
        testTokeniser(query, tokens);
    }

    private void testTokeniser(String query, List<Token> expTokens) {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize(query);
        } catch (Tokenizer.TokenizeException ex) {
            fail("" + ex.getIndex() + ": " + ex.getMessage());
        }
        Iterator<Tokenizer.Token> expResult = expTokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer;

        while (result.hasNext() && expResult.hasNext()) {
            assertEquals(expResult.next(), result.next());
        }

        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }
}
