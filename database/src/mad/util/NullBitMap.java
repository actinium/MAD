package mad.util;

import java.util.BitSet;

/**
 *
 */
public class NullBitMap {

    private final BitSet bitmap;

    public NullBitMap() {
        bitmap = new BitSet(64);
    }

    public NullBitMap(byte[] bytes) {
        bitmap = BitSet.valueOf(bytes);
    }
    
    public void setNull(int index){
        bitmap.set(index, true);
    }
    
    public void setNotNull(int index){
        bitmap.set(index, false);
    }
    
    public boolean isNull(int index){
        return bitmap.get(index);
    }
    
    public byte[] toBytes(){
        return bitmap.toByteArray();
    }
}
