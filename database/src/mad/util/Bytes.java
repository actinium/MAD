package mad.util;

import java.nio.ByteBuffer;
import static mad.database.Config.BYTEORDER;
import static mad.database.Config.CHARSET;

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
        return string.getBytes(CHARSET);
    }
    
    public static byte[] fromString(String string, int length) {
        byte[] bytes = new byte[length];
        byte[] strBytes = string.getBytes(CHARSET);
        System.arraycopy(strBytes,0,bytes, 0, Math.min(length, strBytes.length));
        return bytes;
    }

    public static String toString(byte[] bytes) {
        int end = bytes.length;
        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] == 0){
                end = i;
                break;
            }
        }
        return new String(bytes, 0, end, CHARSET);
    }
}
