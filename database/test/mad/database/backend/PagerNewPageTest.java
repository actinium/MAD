
package mad.database.backend;

import java.io.File;
import java.io.FileOutputStream;
import static mad.database.Config.PAGESIZE;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 */
public class PagerNewPageTest {

    public PagerNewPageTest() {
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
     * Test of newPage method, of class Pager.
     */
    @Test
    public void testNewPage() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        int dbHeaderSize = 12;
        try(FileOutputStream writer = new FileOutputStream(testFile)){
            byte[] initBytes = new byte[dbHeaderSize];
            writer.write(initBytes);
        }
        Pager pager = new Pager(testFile);
        assertEquals(12, pager.newPage());
        assertEquals(12+PAGESIZE, testFile.length());
        assertEquals(12+PAGESIZE, pager.newPage());
        assertEquals(12+2*PAGESIZE, testFile.length());
    }

    /**
     * Test of newPage & freePage method, of class Pager.
     */
    @Test
    @Ignore
    public void testFreeNewPage() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        int dbHeaderSize = 12;
        try(FileOutputStream writer = new FileOutputStream(testFile)){
            byte[] initBytes = new byte[dbHeaderSize];
            writer.write(initBytes);
        }
        Pager pager = new Pager(testFile);
        assertEquals(12, pager.newPage());
        assertEquals(12+PAGESIZE, testFile.length());
        pager.freePage(12);
        assertEquals(12, pager.newPage());
        assertEquals(12+PAGESIZE, testFile.length());
    }
}