package mad.database;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class Config {
    
    public static final String MADVERSION = "0.1";
    
    public static final int PAGESIZE = 8192;
    public static final ByteOrder BYTEORDER = ByteOrder.LITTLE_ENDIAN;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
}
