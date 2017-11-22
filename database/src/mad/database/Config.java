package mad.database;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class Config {

    /**
     * Version number.
     * 
     * Printed when starting program and when '.version' command is called.
     */
    public static final String MADVERSION = "0.1";

    /**
     * Constants used when writing and reading to file.
     * 
     * - PAGESIZE is the size of a page used by pager.
     * - BYTEORDER makes sure we use the same byte-order regardless of the
     *   machines default byte-order.
     * - CHARSET, all strings are UTF-8.
     */
    public static final int PAGESIZE = 8192;
    public static final ByteOrder BYTEORDER = ByteOrder.LITTLE_ENDIAN;
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * The number of pages stored in pager's cache.
     * 
     * The memory required for the cache is PAGECACHESIZE*PAGESIZE bytes.
     */
    public static int PAGECACHESIZE = 100;

}
