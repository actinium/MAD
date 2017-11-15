
package mad.database.backend;

import mad.database.backend.Schema.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class SchemaTest {

    public SchemaTest() {
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
     * Test of get method, of class Schema.
     */
    @Test
    public void testGet_int() {
        Schema schema = new Schema(
                new Field("id", Field.Type.Integer),
                new Field("name", Field.Type.Varchar,100),
                new Field("email", Field.Type.Varchar, 100));
        assertEquals(3, schema.size());
        
        assertEquals("id", schema.get(0).name);
        assertEquals(Field.Type.Integer, schema.get(0).type);
        
        assertEquals("name", schema.get(1).name);
        assertEquals(Field.Type.Varchar, schema.get(1).type);
        assertEquals(100, schema.get(1).length);
        
        assertEquals("email", schema.get(2).name);
        assertEquals(Field.Type.Varchar, schema.get(2).type);
        assertEquals(100, schema.get(2).length);
    }

    /**
     * Test of get method, of class Schema.
     */
    @Test
    public void testGet_String() {
        Schema schema = new Schema(
                new Field("id", Field.Type.Integer),
                new Field("name", Field.Type.Varchar,100),
                new Field("email", Field.Type.Varchar, 100));
        assertEquals(3, schema.size());
        
        assertEquals("id", schema.get("id").name);
        assertEquals(Field.Type.Integer, schema.get("id").type);
        
        assertEquals("name", schema.get("name").name);
        assertEquals(Field.Type.Varchar, schema.get("name").type);
        assertEquals(100, schema.get("name").length);
        
        assertEquals("email", schema.get("email").name);
        assertEquals(Field.Type.Varchar, schema.get("email").type);
        assertEquals(100, schema.get("email").length);
    }

    /**
     * Test of size method, of class Schema.
     */
    @Test
    public void testSize() {
        Schema schema = new Schema(
                new Field("id", Field.Type.Integer),
                new Field("name", Field.Type.Varchar,100),
                new Field("email", Field.Type.Varchar, 100));
        assertEquals(3, schema.size());
        assertEquals(220,schema.bytes());
        assertEquals(37, schema.rowsPerPage());
    }

}