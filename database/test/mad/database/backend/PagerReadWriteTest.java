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
     * Test of reading and writing integers.
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
     * Test of reading and writing integers.
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
     * Test of reading and writing integers.
     */
    @Test
    public void testWriteAndReadInteger3() throws Exception {
        int before1 = 4321;
        int before2 = 789;
        int before3 = 111;
        int page1;
        int page2;
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
     * Test of reading and writing floats.
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
     * Test of reading and writing booleans.
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
            pager.writeBoolean(page1 + 848, false);
            pager.writeBoolean(page1 + 849, true);
        }
        {
            boolean before = true;
            pager.writeBoolean(page2 + 848, before);
            boolean after = pager.readBoolean(page2 + 848);
            assertEquals(before, after);
        }
        {
            boolean after1 = pager.readBoolean(page1+848);
            boolean after2 = pager.readBoolean(page1+849);
            assertFalse(after1);
            assertTrue(after2);
        }
        pager.close();

    }
    
    /**
     * Test of reading and writing strings.
     */
    @Test
    public void testWriteAndReadString() throws Exception {
        Pager pager = new Pager(testFile);
        
        int page1 = pager.newPage();
        int page2 = pager.newPage();
        {    
            String before = "The green dragon sleeps at night.";
            pager.writeString(page1 + 124, before,33);
            String after = pager.readString(page1 + 124,33);
            assertEquals(before, after);
            pager.writeString(page1 + 848, "Hello World",20);
            pager.writeString(page1 + 848, "Goodbye!", 20);
            String after2 = pager.readString(page1 + 848,20);
            assertEquals("Goodbye!", after2);
        }
        {
            String before = "A knight in shining armor.";
            pager.writeString(page2 + 848, before,30);
            String after = pager.readString(page2 + 848,30);
            assertEquals(before, after);
        }
        {
            String after = pager.readString(page1+848,20);
            assertEquals("Goodbye!",after);
        }
        pager.close();

    }
    
    /**
     * Test of reading and writing strings.
     */
    @Test
    public void testWriteAndReadString2() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        String before1 = "1234567890";
        String before2 = "XXXXXXXXXX";
        String before3 = "1234567890";
        
        pager.writeString(page + 100, before1,10);
        pager.writeString(page + 110, before2,10);
        pager.writeString(page + 120, before3,10);
        
        String after1 = pager.readString(page + 100,10);
        String after2 = pager.readString(page + 110,10);
        String after3 = pager.readString(page + 120,10);
        
        assertEquals(before2, after2);
        assertEquals(before1, after1);
        assertEquals(before3, after3);
        
        pager.close();
    }
    
    /**
     * Test of reading and writing strings.
     */
    @Test
    public void testWriteAndReadString3() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        String before1 = "123";
        String before2 = "XXXXXX";
        String before3 = "12345";
        
        pager.writeString(page + 100, before1,10);
        pager.writeString(page + 110, before2,10);
        pager.writeString(page + 120, before3,10);
        
        String after1 = pager.readString(page + 100,10);
        String after2 = pager.readString(page + 110,10);
        String after3 = pager.readString(page + 120,10);
        
        assertEquals(before1, after1);
        assertEquals(before2, after2);
        assertEquals(before3, after3);
        
        pager.close();
    }
    
    /**
     * Test of reading and writing strings.
     */
    @Test
    public void testWriteAndReadString4() throws Exception {
        Pager pager = new Pager(testFile);
        int page = pager.newPage();
        String before1 = "1234567890ABCDEF";
        String before2 = "XXXXXXXXXX------";
        String before3 = "1234567890ABCDEF";
        
        pager.writeString(page + 100, before1,10);
        pager.writeString(page + 110, before2,10);
        pager.writeString(page + 120, before3,10);
        
        String after3 = pager.readString(page + 120,10);
        String after1 = pager.readString(page + 100,10);
        String after2 = pager.readString(page + 110,10);
        
        assertEquals(before1.substring(0, 10), after1);
        assertEquals(before2.substring(0, 10), after2);
        assertEquals(before3.substring(0, 10), after3);
        
        pager.close();
    }

}
