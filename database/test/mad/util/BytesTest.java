
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
    public void testFromInt() {
        System.out.println("fromInt");
        int number = 0;
        byte[] expResult = {0,0,0,0};
        byte[] result = Bytes.fromInt(number);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toInt method, of class Bytes.
     */
    @Test
    public void testToInt() {
        System.out.println("toInt");
        byte[] bytes = null;
        int expResult = 0;
        int result = Bytes.toInt(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromFloat method, of class Bytes.
     */
    @Test
    public void testFromFloat() {
        System.out.println("fromFloat");
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
        System.out.println("toFloat");
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
        System.out.println("fromBoolean");
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
        System.out.println("toBoolean");
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
        System.out.println("fromString");
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
        System.out.println("toString");
        byte[] bytes = null;
        boolean expResult = false;
        boolean result = Bytes.toString(bytes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}