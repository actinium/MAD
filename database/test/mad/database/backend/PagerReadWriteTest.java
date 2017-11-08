package mad.database.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        try(FileOutputStream writer = new FileOutputStream(testFile)){
            byte[] initBytes = new byte[dbHeaderSize];
            writer.write(initBytes);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of readInteger method, of class Pager.
     */
    @Test
    public void testWriteAndReadInteger1() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        int before = 456789;
        pager.writeInteger(page + 128, before);
        int after = pager.readInteger(page + 128);
        assertEquals(before, after);
        pager.close();
    }
    
    /**
     * Test of readInteger method, of class Pager.
     */
    @Test
    public void testWriteAndReadInteger2() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        {
            int before = 456789;
            pager.writeInteger(page + 128, before);
            int after = pager.readInteger(page + 128);
            assertEquals(before, after);
        }
        {
            int before = -456;
            pager.writeInteger(page + 7000, before);
            int after = pager.readInteger(page + 7000);
            assertEquals(before, after);
        }
        {
            int before = 0;
            pager.writeInteger(page + 0, 123);
            pager.writeInteger(page + 0, before);
            int after = pager.readInteger(page + 0);
            assertEquals(before, after);
        }
        pager.close();
    }
    
    /**
     * Test of readInteger method, of class Pager.
     */
    @Test
    public void testWriteAndReadInteger3() throws Exception {
        int before1 = 4321;
        int before2 = 789;
        int before3 = 111;
        int page1 = 0;
        int page2 = 0;
        {
            Pager pager = new Pager(testFile);
            page1 = pager.newPage();
            page2 = pager.newPage();
            
            pager.writeInteger(page1 + 1004, before1);
            int after1 = pager.readInteger(page1 + 1004);
            assertEquals(before1, after1);
            
            pager.writeInteger(page2 + 10, before2);
            int after2 = pager.readInteger(page2 + 10);
            assertEquals(before2, after2);
            
            pager.writeInteger(page1 + 10, before3);
            int after3 = pager.readInteger(page1 + 10);
            assertEquals(before3, after3);
            
            pager.close();
        }
        {
            Pager pager = new Pager(testFile);
            
            int after3 = pager.readInteger(page1 + 10);
            assertEquals(before3, after3);
            
            int after1 = pager.readInteger(page1 + 1004);
            assertEquals(before1, after1);
            
            int after2 = pager.readInteger(page2 + 10);
            assertEquals(before2, after2);
            
            pager.close();
        }
    }

    /**
     * Test of readFloat method, of class Pager.
     */
    @Test
    public void testWriteAndReadFloat() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        float before = 456.789F;
        pager.writeFloat(page + 1234, before);
        float after = pager.readFloat(page + 1234);
        assertEquals(before, after, 0.01);
        pager.close();
    }

    /**
     * Test of readBoolean method, of class Pager.
     */
    @Test
    public void testWriteAndReadBoolean() throws Exception {
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
        pager.close();

    }

}
