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
    
    public int getInteger(int filePosition){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public void putInteger(int filePosition, int number){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public float getFloat(int filePosition){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public void putFloat(int filePosition, float number){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public boolean getBoolean(int filePosition){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public void putBoolean(int filePosition, boolean bool){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public String getString(int fileposition,int length){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
    public void putString(int fileposition, String string){
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
    
}
