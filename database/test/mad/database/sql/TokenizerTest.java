package mad.database.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import mad.database.sql.Tokenizer.Token;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID,"tablename"),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer2() {
        String query = "select true;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Boolean, "true"),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer3() {
        String query = "select tableName;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "tableName"),
                new Token(Token.Type.Semicolon)));
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
            fail("" + ex.getIndex() +": "+ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID, "tableName"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon),
                new Token(Token.Type.Select),
                new Token(Token.Type.StringID, "TextID"),
                new Token(Token.Type.Semicolon)));

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
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.Text, "Text with a char: 'c' and a string: \"str\". Hello World!"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer6() {
        String query = "select 'Text: ''Hello World!'',  Char: ''c'''; ";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Text, "Text: 'Hello World!',  Char: 'c'"),
                new Token(Token.Type.Semicolon)));
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
            fail("" + ex.getIndex() +": "+ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.StringID, "select"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));

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
            fail("" + ex.getIndex() +": "+ex.getMessage());
        }
        ArrayList<Token> tokens2 = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID, "tableName"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));

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
                new Token(Token.Type.Select),
                new Token(Token.Type.Float, "12.34"),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer9() {
        String query = "select (1234);";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.Integer, "1234"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer10() {
        String query = "select col1,col2 from tablename where col1 = 12;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Equals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));
        testTokeniser(query, tokens);
    }

    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer11() {
        String query = "select * from table1,table2 where table1.col2 = table2.col1;";
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "table1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "table2"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "table1"),
                new Token(Token.Type.Dot),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.Equals),
                new Token(Token.Type.ID, "table2"),
                new Token(Token.Type.Dot),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer12() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 == 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.DoubleEquals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer13() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 != 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.NotEquals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer14() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 <> 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.NotEquals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer15() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 < 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.LessThan),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer16() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 > 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.GreaterThan),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer17() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 <= 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.LessThanOrEquals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer18() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 >= 12;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.GreaterThanOrEquals),
                new Token(Token.Type.Integer, "12"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer19() {
        String query =
                "select col1,col2\n"+
                "   from tablename\n"+
                "   where col1 == 'text' || 'text';";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.Comma),
                new Token(Token.Type.ID, "col2"),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.DoubleEquals),
                new Token(Token.Type.Text, "text"),
                new Token(Token.Type.Concat),
                new Token(Token.Type.Text, "text"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer20() {
        String query =
                "select *\n"+
                "   from numbers\n"+
                "   where value%2=0;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "numbers"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "value"),
                new Token(Token.Type.Modulo),
                new Token(Token.Type.Integer,"2"),
                new Token(Token.Type.Equals),
                new Token(Token.Type.Integer,"0"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer21() {
        String query =
                "select *\n"+
                "   from numbers\n"+
                "   where value+2=20;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "numbers"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "value"),
                new Token(Token.Type.Plus),
                new Token(Token.Type.Integer,"2"),
                new Token(Token.Type.Equals),
                new Token(Token.Type.Integer,"20"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer22() {
        String query =
                "select *\n"+
                "   from numbers\n"+
                "   where value-10=1;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "numbers"),
                new Token(Token.Type.Where),
                new Token(Token.Type.ID, "value"),
                new Token(Token.Type.Minus),
                new Token(Token.Type.Integer,"10"),
                new Token(Token.Type.Equals),
                new Token(Token.Type.Integer,"1"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer23() {
        String query =
                "select *\n"+
                "   from numbers\n"+
                "   where 0.5*(value+3)<33.3;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "numbers"),
                new Token(Token.Type.Where),
                new Token(Token.Type.Float,"0.5"),
                new Token(Token.Type.Star),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID, "value"),
                new Token(Token.Type.Plus),
                new Token(Token.Type.Integer,"3"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.LessThan),
                new Token(Token.Type.Float,"33.3"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer24() {
        String query =
                "select *\n"+
                "   from numbers\n"+
                "   where 0.5*(value+0.1)>=-0.3;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "numbers"),
                new Token(Token.Type.Where),
                new Token(Token.Type.Float,"0.5"),
                new Token(Token.Type.Star),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID, "value"),
                new Token(Token.Type.Plus),
                new Token(Token.Type.Float,"0.1"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.GreaterThanOrEquals),
                new Token(Token.Type.Minus),
                new Token(Token.Type.Float,"0.3"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer25() {
        String query =
                "select *\n"+
                "   from tablename\n"+
                "   where 0 <= col1 and col1 < 100;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.Integer,"0"),
                new Token(Token.Type.LessThanOrEquals),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.And),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.LessThan),
                new Token(Token.Type.Integer,"100"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }
    
    /**
     * Test of class Tokenizer.
     */
    @Test
    public void testTokenizer26() {
        String query =
                "select *\n"+
                "   from tablename\n"+
                "   where 0 <= col1 & col1 < 100;";
        
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Star),
                new Token(Token.Type.From),
                new Token(Token.Type.ID, "tablename"),
                new Token(Token.Type.Where),
                new Token(Token.Type.Integer,"0"),
                new Token(Token.Type.LessThanOrEquals),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.And),
                new Token(Token.Type.ID, "col1"),
                new Token(Token.Type.LessThan),
                new Token(Token.Type.Integer,"100"),
                new Token(Token.Type.Semicolon)));

        testTokeniser(query, tokens);
    }

    private void testTokeniser(String query, List<Token> expTokens){
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize(query);
        } catch (Tokenizer.TokenizeException ex) {
            fail("" + ex.getIndex() +": "+ex.getMessage());
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
