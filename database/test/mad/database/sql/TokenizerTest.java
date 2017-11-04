package mad.database.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import mad.database.sql.Tokenizer.Token;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andreas Cederholm
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
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
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
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Boolean,"true"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
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
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID,"tableName"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
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
            tokenizer.tokenize("select \"TextText\";");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID,"tableName"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon),
                new Token(Token.Type.Select),
                new Token(Token.Type.Text,"TextText"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
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
            tokenizer.tokenize("select ( \"Text with a char: 'c' and a string: \"\"str\"\". Hello World!\" ); ");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.Text,"Text with a char: 'c' and a string: \"str\". Hello World!"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
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
            tokenizer.tokenize("select 'Text: \"Hello World!\". Char: ''c'''; ");
        } catch (Tokenizer.TokenizeException ex) {
            fail(ex.getMessage());
        }
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Text,"Text: \"Hello World!\". Char: 'c'"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = tokenizer.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
            assertEquals(expResult.next(), result.next());
        }
        
        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }
}
