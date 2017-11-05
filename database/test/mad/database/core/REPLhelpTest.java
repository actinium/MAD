package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import mad.util.Pipe;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class REPLhelpTest {
    
    public REPLhelpTest() {
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
     * Test of main method, of class REPL.
     */
    @Test
    public void testMain(){
        Pipe inPipe = initPipe();
        Pipe outPipe = initPipe();
        
        PrintWriter writer = new PrintWriter(inPipe.out);
        BufferedReader reader = new BufferedReader(new InputStreamReader(outPipe.in));
        
        REPL repl = new REPL(inPipe.in, outPipe.out);
        Thread replThread = new Thread(repl);
        replThread.start();
        
        writer.println(".help");
        writer.println(".exit");
        writer.flush();
        
        join(replThread);
        
        String result = read(reader);
        String expResult = 
                "dbname>" +
                ".exit    Exit this program.\n" +
                ".help    Show available commands.\n" +
                ".version Show version number.\n" +
                "dbname>";
        
        assertEquals(result,expResult);
        
        
    }
    
    private Pipe initPipe(){
        try{
            return new Pipe();
        }catch(IOException e){
            fail("Could not create pipe!");
        }
        return null;
    }
    
    private String read(BufferedReader br){
        StringBuilder sb = new StringBuilder();
        int i;
        try {
            while((i = br.read()) != -1 ){
                sb.append((char)i);
            }
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        return sb.toString();
    }
    
    private void join(Thread thread){
        try {
            thread.join();
        } catch (InterruptedException ex) {
            fail(ex.getMessage());
        }
    }
    
}
