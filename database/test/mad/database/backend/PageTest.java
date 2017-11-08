package mad.database.backend;

import java.nio.ByteBuffer;
import static mad.database.Config.BYTEORDER;
import static mad.database.Config.PAGESIZE;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class PageTest {

    public PageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPageStartPosition method, of class Page.
     */
    @Test
    public void testPageStart() {
        Page page0 = new Page(0, new byte[PAGESIZE]);
        assertEquals(page0.getPageStartPosition(), 0);

        Page page42 = new Page(PAGESIZE * 42, new byte[PAGESIZE]);
        assertEquals(PAGESIZE * 42, page42.getPageStartPosition());
    }

    /**
     * Test of getPageStartPosition method, of class Page.
     */
    @Test
    public void testPageWriteAndRead() {
        Page page0 = new Page(0, new byte[PAGESIZE]);
        ByteBuffer bb0w = ByteBuffer.allocate(4).order(BYTEORDER);
        bb0w.putInt(1234);
        page0.putBytes(page0.getPageStartPosition() + 100, bb0w.array());
        ByteBuffer bb0r = ByteBuffer.wrap(page0.getBytes(page0.getPageStartPosition() + 100, 4)).order(BYTEORDER);
        int i0 = bb0r.getInt();
        assertEquals(1234, i0);

        Page page42 = new Page(PAGESIZE * 42, new byte[PAGESIZE]);
        ByteBuffer bb42w = ByteBuffer.allocate(4).order(BYTEORDER);
        bb42w.putInt(1100110088);
        page42.putBytes(page42.getPageStartPosition() + 4242, bb42w.array());
        ByteBuffer bb42r = ByteBuffer.wrap(page42.getBytes(page42.getPageStartPosition() + 4242, 4)).order(BYTEORDER);
        int i42 = bb42r.getInt();
        assertEquals(1100110088, i42);
    }

}
