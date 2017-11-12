package mad.database.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class DBCreateTableTest {

    public DBCreateTableTest() {
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
     * Test of createTable method, of class DB.
     */
    @Test
    public void testCreateTable() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable");
            db.createTable("SecondTable");
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable", "SecondTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i),tableNames.get(i));
            }
        }
    }
    
    /**
     * Test of createTable method, of class DB.
     */
    @Test
    public void testCreateTable2() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable");
            db.createTable("SecondTable");
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("ThirdTable");
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("EndTable");
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable", "SecondTable","ThirdTable","EndTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i),tableNames.get(i));
            }
        }
    }

}
