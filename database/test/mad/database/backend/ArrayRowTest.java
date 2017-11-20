package mad.database.backend;

import mad.database.backend.table.ArrayRow;
import mad.database.backend.table.Schema;
import mad.database.backend.table.Row;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class ArrayRowTest {

    Row intStringRow;
    Row stringBoolFloatIntRow;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ArrayRow intStringArrayRow = new ArrayRow();
        intStringArrayRow.addIntegerColumn("id", 4433);
        intStringArrayRow.addStringColumn("name", "Charlie");
        intStringRow = intStringArrayRow;
        ArrayRow stringBoolFloatIntArrayRow = new ArrayRow();
        stringBoolFloatIntArrayRow.addStringColumn("name", "Alice");
        stringBoolFloatIntArrayRow.addBooleanColumn("smart", true);
        stringBoolFloatIntArrayRow.addFloatColumn("height", 1.75F);
        stringBoolFloatIntArrayRow.addIntegerColumn("age", 30);
        stringBoolFloatIntRow = stringBoolFloatIntArrayRow;

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class ArrayRow.
     */
    @Test
    public void testHasNext() {
        Row row = intStringRow;
        boolean expResult = false;
        boolean result = row.hasNext();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetValues1() throws Exception {
        Row row = intStringRow;
        assertEquals("id", row.getName(0));
        assertEquals("name", row.getName(1));
        assertEquals(Schema.Field.Type.Integer, row.getType(0));
        assertEquals(Schema.Field.Type.Varchar, row.getType(1));
        assertEquals(4433, row.getInteger(0));
        assertEquals("Charlie", row.getString(1));
    }

    /**
     *
     */
    @Test
    public void testGetValues2() throws Exception {
        Row row = intStringRow;
        assertEquals(4433, row.getInteger("id"));
        assertEquals("Charlie", row.getString("name"));
    }

    /**
     *
     */
    @Test
    public void testGetValues3() throws Exception {
        Row row = stringBoolFloatIntRow;
        assertEquals("name", row.getName(0));
        assertEquals("smart", row.getName(1));
        assertEquals("height", row.getName(2));
        assertEquals("age", row.getName(3));
        assertEquals(Schema.Field.Type.Varchar, row.getType(0));
        assertEquals(Schema.Field.Type.Boolean, row.getType(1));
        assertEquals(Schema.Field.Type.Float, row.getType(2));
        assertEquals(Schema.Field.Type.Integer, row.getType(3));
        assertEquals("Alice", row.getString("name"));
        assertEquals(true, row.getBoolean("smart"));
        assertEquals(1.75F, row.getFloat("height"), 0.01);
        assertEquals(30, row.getInteger("age"));
    }

    /**
     *
     */
    @Test
    public void testGetValues4() throws Exception {
        Row row = stringBoolFloatIntRow;
        assertEquals("Alice", row.getString(0));
        assertEquals(true, row.getBoolean(1));
        assertEquals(1.75F, row.getFloat(2), 0.01);
        assertEquals(30, row.getInteger(3));
    }

    /**
     *
     */
    @Test(expected = Row.NoSuchColumnException.class)
    public void testGetValues5() throws Exception {
        Row row = intStringRow;
        row.getBoolean(2);
    }

    /**
     *
     */
    @Test(expected = Row.NoSuchColumnException.class)
    public void testGetValues6() throws Exception {
        Row row = intStringRow;
        row.getBoolean(Integer.MAX_VALUE);
    }

    /**
     *
     */
    @Test(expected = Row.NoSuchColumnException.class)
    public void testGetValues7() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getBoolean(4);
    }

    /**
     *
     */
    @Test(expected = Row.NoSuchColumnException.class)
    public void testGetValues8() throws Exception {
        Row row = intStringRow;
        row.getBoolean("phone");
    }

    /**
     *
     */
    @Test(expected = Row.NoSuchColumnException.class)
    public void testGetValues9() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getBoolean("address");
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues10() throws Exception {
        Row row = intStringRow;
        row.getBoolean(0);
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues11() throws Exception {
        Row row = intStringRow;
        row.getFloat(0);
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues12() throws Exception {
        Row row = intStringRow;
        row.getString(0);
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues13() throws Exception {
        Row row = intStringRow;
        row.getInteger(1);
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues14() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getInteger("name");
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues15() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getFloat("age");
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues16() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getBoolean("height");
    }

    /**
     *
     */
    @Test(expected = Row.TypeMismatchException.class)
    public void testGetValues17() throws Exception {
        Row row = stringBoolFloatIntRow;
        row.getString("smart");
    }

}
