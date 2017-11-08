package mad.database.backend;

import static mad.database.Config.PAGESIZE;
import mad.util.Bytes;
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
        page0.putBytes(page0.getPageStartPosition() + 100, Bytes.fromInt(1234));
        int i0 = Bytes.toInt(page0.getBytes(page0.getPageStartPosition() + 100, 4));
        assertEquals(1234, i0);

        Page page42 = new Page(PAGESIZE * 42, new byte[PAGESIZE]);
        page42.putBytes(page42.getPageStartPosition() + 4242, Bytes.fromInt(1100110088));
        int i42 = Bytes.toInt(page42.getBytes(page42.getPageStartPosition() + 4242, 4));
        assertEquals(1100110088, i42);
    }

}
