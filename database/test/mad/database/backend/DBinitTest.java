package mad.database.backend;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class DBinitTest {

    public DBinitTest() {
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
     * Test of open method, of class DB.
     */
    @Test
    public void testInit() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        assertEquals(0, testFile.length());
        DB db = DB.open(testFile.getAbsolutePath());
        assertEquals(12,testFile.length());
        db.close();
    }

}
