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
     * Test of toInt method, of class Bytes.
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
    public void testFromFloat0() {
        float number = 0.0F;
        byte[] expResult = {0, 0, 0, 0};
        byte[] result = Bytes.fromFloat(number);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toFloat method, of class Bytes.
     */
    @Test
    public void testToFloat0() {
        byte[] bytes = {0, 0, 0, 0};
        float expResult = 0.0F;
        float result = Bytes.toFloat(bytes);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of Float methods with Pi.
     */
    @Test
    public void testFloatPI() {
        float before = 3.14159265359F;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.MAX_VALUE.
     */
    @Test
    public void testFloatMax() {
        float before = Float.MAX_VALUE;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.MIN_VALUE.
     */
    @Test
    public void testFloatMin() {
        float before = Float.MIN_VALUE;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.MIN_NORMAL.
     */
    @Test
    public void testFloatNormal() {
        float before = Float.MIN_NORMAL;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.POSITIVE_INFINITY.
     */
    @Test
    public void testFloatInfinity() {
        float before = Float.POSITIVE_INFINITY;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.NEGATIVE_INFINITY.
     */
    @Test
    public void testFloatNegativeInfinity() {
        float before = Float.NEGATIVE_INFINITY;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of Float methods with Float.NaN.
     */
    @Test
    public void testFloatNaN() {
        float before = Float.NaN;
        byte[] bytes = Bytes.fromFloat(before);
        float after = Bytes.toFloat(bytes);
        assertEquals(before, after, 0.01);
    }

    /**
     * Test of fromBoolean method, of class Bytes.
     */
    @Test
    public void testFromBooleanTrue() {
        boolean bool = true;
        byte[] expResult = {1};
        byte[] result = Bytes.fromBoolean(bool);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of fromBoolean method, of class Bytes.
     */
    @Test
    public void testFromBooleanFalse() {
        boolean bool = false;
        byte[] expResult = {0};
        byte[] result = Bytes.fromBoolean(bool);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toBoolean method, of class Bytes.
     */
    @Test
    public void testToBooleanTrue() {
        byte[] bytes = {1};
        boolean expResult = true;
        boolean result = Bytes.toBoolean(bytes);
        assertEquals(expResult, result);
    }

    /**
     * Test of toBoolean method, of class Bytes.
     */
    @Test
    public void testToBooleanFalse() {
        byte[] bytes = {0};
        boolean expResult = false;
        boolean result = Bytes.toBoolean(bytes);
        assertEquals(expResult, result);
    }

    /**
     * Test of Boolean methods, of class Bytes.
     */
    @Test
    public void testBooleanTrue() {
        boolean before = true;
        byte[] bytes = Bytes.fromBoolean(before);
        boolean after = Bytes.toBoolean(bytes);
        assertEquals(before, after);
    }

    /**
     * Test of Boolean methods, of class Bytes.
     */
    @Test
    public void testBooleanFalse() {
        boolean before = false;
        byte[] bytes = Bytes.fromBoolean(before);
        boolean after = Bytes.toBoolean(bytes);
        assertEquals(before, after);
    }

    /**
     * Test of fromString method, of class Bytes.
     */
    @Test
    public void testFromString() {
        String string = "Hello World";
        byte[] expResult = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] result = Bytes.fromString(string);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Bytes.
     */
    @Test
    public void testToString() {
        byte[] bytes = {
            (byte) 0xC3, (byte) 0xA5, // å
            (byte) 0xC3, (byte) 0xA4, // ä
            (byte) 0xC3, (byte) 0xB6, // ö
            (byte) 0xC3, (byte) 0x85, // Å
            (byte) 0xC3, (byte) 0x84, // Ä
            (byte) 0xC3, (byte) 0x96}; //Ö
        String expResult = "åäöÅÄÖ";
        String result = Bytes.toString(bytes);
        assertEquals(expResult, result);
    }

    /**
     * Test of String methods, of class Bytes.
     */
    @Test
    public void testString() {
        String before
                = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ\n"
                + "abcdefghijklmnopqrstuvwxyzåäö\n"
                + "1234567890";
        byte[] bytes = Bytes.fromString(before);
        String after = Bytes.toString(bytes);
        assertEquals(before, after);
    }

    /**
     * Test of String methods, of class Bytes.
     */
    @Test
    public void testString2() {
        String before
                = "!@#$£%^&*()_+|~\\`\n"
                + "\t{}[]:;\"'<>,./?\n"
                + "ÉÈËÊéèëêÁÀÄÃÂáàäãâÓÒÖÕÖóòöõô\n"
                + "çßñ";
        byte[] bytes = Bytes.fromString(before);
        String after = Bytes.toString(bytes);
        assertEquals(before, after);
    }
    
    /**
     * Test of fromString method, of class Bytes.
     */
    @Test
    public void testSizedFromString() {
        String string = "Hello World";
        byte[] expResult = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd',0,0,0,0,0,0,0,0,0};
        byte[] result = Bytes.fromString(string, 20);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of fromString method, of class Bytes.
     */
    @Test
    public void testSizedFromString1() {
        String string = "Hello World";
        byte[] expResult = {'H', 'e', 'l', 'l', 'o'};
        byte[] result = Bytes.fromString(string, 5);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of fromString method, of class Bytes.
     */
    @Test
    public void testSizedString() {
        String string = "Hello World";
        byte[] expResult = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd',0,0,0,0,0,0,0,0,0};
        byte[] result = Bytes.fromString(string, 20);
        assertArrayEquals(expResult, result);
        String after = Bytes.toString(result);
        assertEquals(string, after);
    }

}
