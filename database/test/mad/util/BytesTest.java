package mad.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class BytesTest {

    public BytesTest() {
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
     * Test of fromInt method, of class Bytes.
     */
    @Test
    public void testFromInt0() {
        int number = 0;
        byte[] expResult = {0, 0, 0, 0};
        byte[] result = Bytes.fromInt(number);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of fromInt method, of class Bytes.
     */
    @Test
    public void testFromInt1234() {
        int number = 1234;
        byte[] expResult = {(byte) 0xD2, 0x4, 0, 0};
        byte[] result = Bytes.fromInt(number);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of fromInt method, of class Bytes.
     */
    @Test
    public void testFromInt2000000000() {
        int number = 2000000000;
        byte[] expResult = {0, (byte) 0x94, (byte) 0x35, (byte) 0x77};
        byte[] result = Bytes.fromInt(number);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toInt method, of class Bytes.
     */
    @Test
    public void testToInt0() {
        byte[] bytes = {0, 0, 0, 0};
        int expResult = 0;
        int result = Bytes.toInt(bytes);
        assertEquals(expResult, result);
    }

    /**
     * Test of toInt method, of class Bytes.16843009
     */
    @Test
    public void testToInt20171108() {
        byte[] bytes = {(byte) 0x64, (byte) 0xC9, (byte) 0x33, (byte) 0x01};
        int expResult = 20171108;
        int result = Bytes.toInt(bytes);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toInt method, of class Bytes.
     */
    @Test
    public void testToInt16843009() {
        byte[] bytes = {(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        int expResult = 16843009;
        int result = Bytes.toInt(bytes);
        assertEquals(expResult, result);
    }

    /**
     * Test of fromInt and toInt methods with 0.
     */
    @Test
    public void testIntZero() {
        int numberBefore = 0;
        byte[] result = Bytes.fromInt(numberBefore);
        int numberAfter = Bytes.toInt(result);
        assertEquals(numberBefore, numberAfter);
    }

    /**
     * Test of fromInt and toInt methods with Integer.MAX_VALUE.
     */
    @Test
    public void testIntMax() {
        int numberBefore = Integer.MAX_VALUE;
        byte[] result = Bytes.fromInt(numberBefore);
        int numberAfter = Bytes.toInt(result);
        assertEquals(numberBefore, numberAfter);
    }

    /**
     * Test of fromInt and toInt methods with Integer.MIN_VALUE.
     */
    @Test
    public void testIntMin() {
        int numberBefore = Integer.MIN_VALUE;
        byte[] result = Bytes.fromInt(numberBefore);
        int numberAfter = Bytes.toInt(result);
        assertEquals(numberBefore, numberAfter);
    }

    /**
     * Test of fromFloat method, of class Bytes.
     */
    @Test
    public void testFromFloat() {
        float number = 0.0F;
        byte[] expResult = null;
        byte[] result = Bytes.fromFloat(number);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toFloat method, of class Bytes.
     */
    @Test
    public void testToFloat() {
        byte[] bytes = null;
        float expResult = 0.0F;
        float result = Bytes.toFloat(bytes);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromBoolean method, of class Bytes.
     */
    @Test
    public void testFromBoolean() {
        boolean bool = false;
        byte[] expResult = null;
        byte[] result = Bytes.fromBoolean(bool);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toBoolean method, of class Bytes.
     */
    @Test
    public void testToBoolean() {
        byte[] bytes = null;
        boolean expResult = false;
        boolean result = Bytes.toBoolean(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromString method, of class Bytes.
     */
    @Test
    public void testFromString() {
        String string = "";
        byte[] expResult = null;
        byte[] result = Bytes.fromString(string);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Bytes.
     */
    @Test
    public void testToString() {
        byte[] bytes = null;
        boolean expResult = false;
        boolean result = Bytes.toString(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
