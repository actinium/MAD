
package mad.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 *
 */
public class Pipe {
    
    private final PipedInputStream pipedIn;
    private final PipedOutputStream pipedOut;
    
    public InputStream in;
    public OutputStream out;
    
    public Pipe() throws IOException{
        this.pipedIn = new PipedInputStream();
        this.pipedOut = new PipedOutputStream(pipedIn);
        
        in = pipedIn;
        out = pipedOut;
    }
    
}
