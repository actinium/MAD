package mad.util;

import java.nio.ByteBuffer;
import static mad.database.Config.BYTEORDER;

/**
 *
 */
public class Bytes {

    public static byte[] fromInt(int number) {
        byte[] ret = new byte[4];
        ByteBuffer bb = ByteBuffer.allocate(4).order(BYTEORDER);
        bb.putInt(number).position(0).mark();
        bb.get(ret);
        return ret;
    }

    public static int toInt(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(4).order(BYTEORDER);
        bb.put(bytes).position(0).mark();
        return bb.getInt();
    }
    
    public static byte[] fromFloat(float number) {
        byte[] ret = new byte[4];
        ByteBuffer bb = ByteBuffer.allocate(4).order(BYTEORDER);
        bb.putFloat(number).position(0).mark();
        bb.get(ret);
        return ret;
    }

    public static float toFloat(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(4).order(BYTEORDER);
        bb.put(bytes).position(0).mark();
        return bb.getFloat();
    }
    
    public static byte[] fromBoolean(boolean bool) {
        byte[] ret = new byte[1];
        ByteBuffer bb = ByteBuffer.allocate(4).order(BYTEORDER);
        bb.put((byte) (bool ? 1 : 0)).position(0).mark();
        bb.get(ret);
        return ret;
    }

    public static boolean toBoolean(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.allocate(1).order(BYTEORDER);
        bb.put(bytes).position(0).mark();
        return bb.get() != 0;
    }
    
    public static byte[] fromString(String string) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    public static boolean toString(byte[] bytes) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
}
