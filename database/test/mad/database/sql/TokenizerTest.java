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
        Tokenizer instance = new Tokenizer("select;");
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = instance.iterator();
        
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
        Tokenizer instance = new Tokenizer("select true;");
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.Boolean,"true"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = instance.iterator();
        
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
        Tokenizer instance = new Tokenizer("select tableName;");
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.ID,"tableName"),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = instance.iterator();
        
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
        Tokenizer instance = new Tokenizer("select (tableName);");
        ArrayList<Token> tokens= new ArrayList<>(Arrays.asList(
                new Token(Token.Type.Select),
                new Token(Token.Type.LParen),
                new Token(Token.Type.ID,"tableName"),
                new Token(Token.Type.RParen),
                new Token(Token.Type.Semicolon)));
        
        Iterator<Tokenizer.Token> expResult = tokens.iterator();
        Iterator<Tokenizer.Token> result = instance.iterator();
        
        while(result.hasNext() && expResult.hasNext()){
            assertEquals(expResult.next(), result.next());
        }
        
        assertFalse(result.hasNext());
        assertFalse(expResult.hasNext());
    }
}
