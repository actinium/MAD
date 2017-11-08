package mad.util;

import java.nio.ByteBuffer;
import static mad.database.Config.BYTEORDER;

/**
 *
 */
public class Bytes {

    public static byte[] fromInt(int number) {
        byte[] ret = new byte[4];
        ByteBuffer.allocate(4).order(BYTEORDER).putInt(number).get(ret);
        return ret;
    }

    public static int toInt(byte[] bytes) {
        return ByteBuffer.allocate(4).order(BYTEORDER).put(bytes).getInt();
    }
    
    public static byte[] fromFloat(float number) {
        byte[] ret = new byte[4];
        ByteBuffer.allocate(4).order(BYTEORDER).putFloat(number).get(ret);
        return ret;
    }

    public static float toFloat(byte[] bytes) {
        return ByteBuffer.allocate(4).order(BYTEORDER).put(bytes).getFloat();
    }
    
    public static byte[] fromBoolean(boolean bool) {
        byte[] ret = new byte[1];
        ByteBuffer.allocate(1).order(BYTEORDER).put((byte) (bool ? 1 : 0)).get(ret);
        return ret;
    }

    public static boolean toBoolean(byte[] bytes) {
        return ByteBuffer.allocate(1).order(BYTEORDER).put(bytes).get() != 0;
    }
    
    public static byte[] fromString(String string) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    public static boolean toString(byte[] bytes) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }
}
