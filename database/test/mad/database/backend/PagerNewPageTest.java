package mad.database.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
public class PagerNewPageTest {

    private File testFile;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        int dbHeaderSize = 12;
        try (FileOutputStream writer = new FileOutputStream(testFile)) {
            byte[] initBytes = new byte[dbHeaderSize];
            writer.write(initBytes);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of newPage method, of class Pager.
     */
    @Test
    public void testNewPage() throws Exception {
        Pager pager = new Pager(testFile);
        assertEquals(12, pager.newPage());
        assertEquals(12 + PAGESIZE, testFile.length());
        assertEquals(12 + PAGESIZE, pager.newPage());
        assertEquals(12 + 2 * PAGESIZE, testFile.length());
        pager.close();
    }

    /**
     * Test of newPage & freePage method, of class Pager.
     */
    @Test
    public void testFreeNewPage() throws Exception {
        Pager pager = new Pager(testFile);
        assertEquals(12, pager.newPage());
        assertEquals(12 + PAGESIZE, testFile.length());
        pager.freePage(12);
        assertEquals(12, pager.newPage());
        assertEquals(12 + PAGESIZE, testFile.length());
        pager.close();
    }

    /**
     * Test of newPage & freePage method, of class Pager.
     */
    @Test
    public void testFreeNewPage1() throws Exception {
        Pager pager = new Pager(testFile);

        // Allocate 3 pages
        assertEquals(12, pager.newPage());
        assertEquals(12 + PAGESIZE, testFile.length());
        assertEquals(12 + PAGESIZE, pager.newPage());
        assertEquals(12 + 2 * PAGESIZE, testFile.length());
        assertEquals(12 + 2 * PAGESIZE, pager.newPage());
        assertEquals(12 + 3 * PAGESIZE, testFile.length());

        // Free 2 pages
        pager.freePage(12 + 2 * PAGESIZE);
        pager.freePage(12);

        // Get 2 new pages. Shouldn't increase file size.
        int p1 = pager.newPage();
        int p2 = pager.newPage();
        assertTrue((p1 == 12) ^ (p2 == 12));
        assertTrue((p1 == 12 + 2 * PAGESIZE) ^ (p2 == 12 + 2 * PAGESIZE));
        assertEquals(12 + 3 * PAGESIZE, testFile.length());
        assertEquals(12 + 3 * PAGESIZE, testFile.length());

        // Free-list is empty again so we allocate a new page.
        assertEquals(12 + 3 * PAGESIZE, pager.newPage());
        assertEquals(12 + 4 * PAGESIZE, testFile.length());

        pager.close();
    }
}
