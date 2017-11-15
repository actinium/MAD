package mad.database.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
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
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable", new Schema(
                    new Schema.Field("afield1", Schema.Field.Type.Integer),
                    new Schema.Field("afield2", Schema.Field.Type.Integer)));
            db.createTable("SecondTable", new Schema(
                    new Schema.Field("sfield1", Schema.Field.Type.Integer),
                    new Schema.Field("sfield2", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable", "SecondTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            db.close();
        }
    }

    /**
     * Test of createTable method, of class DB.
     */
    @Test
    public void testCreateTable2() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable", new Schema(
                    new Schema.Field("afield1", Schema.Field.Type.Integer),
                    new Schema.Field("afield2", Schema.Field.Type.Integer),
                    new Schema.Field("afield3", Schema.Field.Type.Integer)));
            db.createTable("SecondTable", new Schema(
                    new Schema.Field("sfield1", Schema.Field.Type.Integer),
                    new Schema.Field("sfield2", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("ThirdTable", new Schema(
                    new Schema.Field("field1", Schema.Field.Type.Integer),
                    new Schema.Field("field2", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("EndTable", new Schema(
                    new Schema.Field("field1", Schema.Field.Type.Integer),
                    new Schema.Field("field2", Schema.Field.Type.Integer)));
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable", "SecondTable", "ThirdTable", "EndTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            db.close();
        }
    }

    /**
     * Test of createTable method, of class DB.
     */
    @Test
    public void testCreateTable3() throws Exception {
        File testFile = File.createTempFile("madtest-", Long.toString(System.nanoTime()));
        testFile.deleteOnExit();
        {
            DB db = DB.open(testFile.getAbsolutePath());
            db.createTable("AwesomeTable", new Schema(
                    new Schema.Field("field1", Schema.Field.Type.Integer),
                    new Schema.Field("field2", Schema.Field.Type.Integer)));
            db.close();
        }
        {
            DB db = DB.open(testFile.getAbsolutePath());
            List<String> tableNames = db.getTableNames();
            ArrayList<String> expTableNames = new ArrayList<>(Arrays.asList(
                    "AwesomeTable"));
            assertEquals(expTableNames.size(), tableNames.size());
            for (int i = 0; i < expTableNames.size() && i < tableNames.size(); i++) {
                assertEquals(expTableNames.get(i), tableNames.get(i));
            }
            Schema schema = db.getSchema("AwesomeTable");
            assertNotNull(schema);
            assertEquals(2, schema.size());
            assertEquals("field1", schema.get(0).name);
            assertEquals("field2", schema.get(1).name);
            assertEquals(Schema.Field.Type.Integer, schema.get(0).type);
            assertEquals(Schema.Field.Type.Integer, schema.get(1).type);
            db.close();
        }
    }

}
