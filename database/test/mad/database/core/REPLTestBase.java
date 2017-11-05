package mad.database.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import mad.util.Pipe;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 */
public class REPLTestBase {

    PrintWriter writer;
    BufferedReader reader;
    Thread replThread;

    @Before
    public void setUp() {
        Pipe inPipe = initPipe();
        Pipe outPipe = initPipe();
        writer = new PrintWriter(inPipe.out, true);
        reader = new BufferedReader(new InputStreamReader(outPipe.in));
        REPL repl = new REPL(inPipe.in, outPipe.out);
        replThread = new Thread(repl);
        replThread.start();
    }

    @After
    public void tearDown() {
        join(replThread);
    }

    private Pipe initPipe() {
        try {
            return new Pipe();
        } catch (IOException e) {
            fail("Could not create pipe!");
        }
        return null;
    }

    protected String read(BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        int i;
        try {
            while ((i = br.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        return sb.toString();
    }

    private void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException ex) {
            fail(ex.getMessage());
        }
    }

}
