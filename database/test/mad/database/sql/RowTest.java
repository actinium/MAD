package mad.database.sql;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class RowTest {
    
    public RowTest() {
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
     * Test of constructor, of class Row.
     */
    @Test
    public void testConstructor_definitionDecidesSize() {
        DataType[] def1 = { DataType.INTEGER, DataType.INTEGER };
        String[] val1 = { "1" };
        DataType[] def2 = { DataType.INTEGER };
        String[] val2 = { "1", "2" };
        
        Row instance1 = new Row(def1, val1);
        Row instance2 = new Row(def2, val2);
        
        assertEquals(2, instance1.getColumnCount());
        assertEquals(1, instance2.getColumnCount());
    }
    
    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_String1() {
        int index = 0;
        String newValue = "updated";
        Row instance = new Row(new DataType[] { DataType.VARCHAR }, new String[] { "initial" });
        
        boolean result = instance.updateColumn(index, newValue);
        
        assertEquals(true, result);
        assertEquals("updated", instance.getValueAsString(index));
    }
    
    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_String2() {
        String newValue1 = "updated";
        int newValue2 = 1;
        Row instance = new Row(new DataType[] { DataType.VARCHAR }, new String[] { "initial" });
        
        boolean result = instance.updateColumn(1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(-1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(0, newValue2);
        assertEquals(false, result);
        
        assertEquals("initial", instance.getValueAsString(0));
    }

    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_int1() {
        int index = 0;
        int newValue = 2;
        Row instance = new Row(new DataType[] { DataType.INTEGER }, new String[] { "1" });
        
        boolean result = instance.updateColumn(index, newValue);
        
        assertEquals(true, result);
        assertEquals(2, instance.getValueAsInteger(index));
    }
    
    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_int2() {
        int newValue1 = 2;
        String newValue2 = "2";
        Row instance = new Row(new DataType[] { DataType.INTEGER }, new String[] { "1" });
        
        boolean result = instance.updateColumn(1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(-1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(0, newValue2);
        assertEquals(false, result);
        
        assertEquals(1, instance.getValueAsInteger(0));
    }

    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_boolean1() {
        int index = 0;
        boolean newValue = false;
        Row instance = new Row(new DataType[] { DataType.BOOLEAN }, new String[] { "1" });
        
        boolean result = instance.updateColumn(index, newValue);
        
        assertEquals(true, result);
        assertEquals(false, instance.getValueAsBoolean(index));
    }
    
    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_boolean2() {
        boolean newValue1 = false;
        int newValue2 = 0;
        Row instance = new Row(new DataType[] { DataType.BOOLEAN }, new String[] { "1" });
        
        boolean result = instance.updateColumn(1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(-1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(0, newValue2);
        assertEquals(false, result);
        
        assertEquals(true, instance.getValueAsBoolean(0));
    }

    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_double1() {
        int index = 0;
        double newValue = 2.5;
        Row instance = new Row(new DataType[] { DataType.DOUBLE }, new String[] { "1.2" });
        
        boolean result = instance.updateColumn(index, newValue);
        
        assertEquals(true, result);
        assertEquals(2.5, instance.getValueAsDouble(index), .000001);
    }
    
    /**
     * Test of updateColumn method, of class Row.
     */
    @Test
    public void testUpdateColumn_int_double2() {
        double newValue1 = 2.5;
        int newValue2 = 2;
        Row instance = new Row(new DataType[] { DataType.DOUBLE }, new String[] { "1.2" });
        
        boolean result = instance.updateColumn(1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(-1, newValue1);
        assertEquals(false, result);
        
        result = instance.updateColumn(0, newValue2);
        assertEquals(false, result);
        
        assertEquals(1.2, instance.getValueAsDouble(0), .000001);
    }

    /**
     * Test of getColumnDataType method, of class Row.
     */
    @Test
    public void testGetColumnDataType() {
        Row instance = new Row(new DataType[] { DataType.BOOLEAN, DataType.DOUBLE, DataType.INTEGER, DataType.VARCHAR });
        DataType result1 = instance.getColumnDataType(-1);
        DataType result2 = instance.getColumnDataType(0);
        DataType result3 = instance.getColumnDataType(1);
        DataType result4 = instance.getColumnDataType(2);
        DataType result5 = instance.getColumnDataType(3);
        DataType result6 = instance.getColumnDataType(4);
        
        assertEquals(DataType.INVALID_TYPE, result1);
        assertEquals(DataType.BOOLEAN, result2);
        assertEquals(DataType.DOUBLE, result3);
        assertEquals(DataType.INTEGER, result4);
        assertEquals(DataType.VARCHAR, result5);
        assertEquals(DataType.INVALID_TYPE, result6);
    }
}
