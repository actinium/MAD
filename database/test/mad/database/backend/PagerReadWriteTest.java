package mad.database.backend;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class PagerReadWriteTest {

    public PagerReadWriteTest() {
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
     * Test of readInteger method, of class Pager.
     */
    @Test
    public void testWriteAndReadInteger() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        int before = 456789;
        pager.writeInteger(page + 128, before);
        int after = pager.readInteger(page + 128);
        assertEquals(before, after);
    }

    /**
     * Test of readFloat method, of class Pager.
     */
    @Test
    public void testWriteAndReadFloat() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        float before = 456.789F;
        pager.writeFloat(page + 1234, before);
        float after = pager.readFloat(page + 1234);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of readBoolean method, of class Pager.
     */
    @Test
    public void testWriteAndReadBoolean() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        Pager pager = new Pager(testFile);
        
        int page1 = pager.newPage();
        int page2 = pager.newPage();
        {    
            boolean before = true;
            pager.writeBoolean(page1 + 124, before);
            boolean after = pager.readBoolean(page1 + 124);
            assertEquals(before, after);
            pager.writeBoolean(page1 + 8848, false);
            pager.writeBoolean(page1 + 8849, true);
        }
        {
            boolean before = true;
            pager.writeBoolean(page2 + 8848, before);
            boolean after = pager.readBoolean(page2 + 8848);
            assertEquals(before, after);
        }
        {
            boolean after1 = pager.readBoolean(page1+8848);
            boolean after2 = pager.readBoolean(page1+8849);
            assertFalse(after1);
            assertTrue(after2);
        }

    }

}
