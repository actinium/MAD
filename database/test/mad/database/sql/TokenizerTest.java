package mad.database.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import mad.database.sql.Tokenizer.Token;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer2() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select true;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Boolean, "true"),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer3() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select tableName;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID, "tableName"),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer4() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select (tableName);");
            tokenizer.tokenize("select \"TextID\";");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer5() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select ( 'Text with a char: ''c'' and a string: \"str\". Hello World!' ); ");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage() + " " + ex.getIndex());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.Text, "Text with a char: 'c' and a string: \"str\". Hello World!"),
                new Token(Token.Type.RParen),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer6() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select 'Text: ''Hello World!'',  Char: ''c'''; ");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Text, "Text: 'Hello World!',  Char: 'c'"),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer7() {
        Tokenizer tokenizer = new Tokenizer();

        try {
            tokenizer.tokenize("select (\"select\"); ");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
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
            fail(ex.getMessage());
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer8() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select 12.34;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Float, "12.34"),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer9() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select (1234);");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.Integer, "1234"),
                new Token(Token.Type.RParen),
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
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer10() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select col1,col2 from tablename where col1 = 12;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
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

        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer;

        while (result.hasNext() && expResult.hasNext()) {
            assertEquals(expResult.next(), result.next());
        }

        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }

    /**
     * Test of iterator method, of class Tokenizer.
     */
    @Test
    public void testTokenizer11() {
        Tokenizer tokenizer = new Tokenizer();
        try {
            tokenizer.tokenize("select * from table1,table2 where table1.col2 = table2.col1;");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
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

        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer;

        while (result.hasNext() && expResult.hasNext()) {
            assertEquals(expResult.next(), result.next());
        }

        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }

}
