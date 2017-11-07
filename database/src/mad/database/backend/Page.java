package mad.database.backend;

import java.nio.ByteBuffer;

/**
 *
 */
public class Page {
    
    private ByteBuffer data;
    private final int position;
    
    public Page(int position,byte[] data){
        this.position = position;
        this.data = ByteBuffer.wrap(data);
    }
    
    void read(){
        
    }
}
