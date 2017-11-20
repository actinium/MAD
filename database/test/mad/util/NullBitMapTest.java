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
public class NullBitMapTest {

    public NullBitMapTest() {
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
     *
     */
    @Test
    public void testNull() {
        NullBitMap instance = new NullBitMap();
        for (int i = 0; i < 64; i++) {
            assertEquals(false, instance.isNull(i));
        }
    }
    
    /**
     *
     */
    @Test
    public void testNull2() {
        NullBitMap instance = new NullBitMap();
        instance.setNull(5);
        instance.setNull(8);
        instance.setNull(12);
        instance.setNull(13);
        instance.setNull(14);
        instance.setNotNull(8);
        instance.setNotNull(13);
        for (int i = 0; i < 64; i++) {
            if(i==5 || i==12 || i==14){
                assertTrue( instance.isNull(i));
            }else{
                assertFalse( instance.isNull(i));
            }
        }
    }
    
    /**
     *
     */
    @Test
    public void testNull3() {
        NullBitMap instance = new NullBitMap();
        instance.setNull(5);
        instance.setNull(8);
        instance.setNull(12);
        instance.setNull(13);
        instance.setNull(14);
        instance.setNotNull(8);
        instance.setNotNull(13);
        instance = Bytes.toNullBitMap(Bytes.fromNullBitMap(instance));
        for (int i = 0; i < 64; i++) {
            if(i==5 || i==12 || i==14){
                assertTrue( instance.isNull(i));
            }else{
                assertFalse( instance.isNull(i));
            }
        }
    }
    
    /**
     *
     */
    @Test
    public void testNull4() {
        NullBitMap instance = new NullBitMap();
        instance.setNull(0);
        instance.setNull(31);
        instance.setNull(63);
        for (int i = 0; i < 64; i++) {
            if(i==0 || i==31 || i==63){
                assertTrue( instance.isNull(i));
            }else{
                assertFalse( instance.isNull(i));
            }
        }
    }
    
    /**
     *
     */
    @Test
    public void testNull5() {
        NullBitMap instance = new NullBitMap();
        instance.setNull(0);
        instance.setNull(31);
        instance.setNull(63);
        instance = Bytes.toNullBitMap(Bytes.fromNullBitMap(instance));
        for (int i = 0; i < 64; i++) {
            if(i==0 || i==31 || i==63){
                assertTrue( instance.isNull(i));
            }else{
                assertFalse( instance.isNull(i));
            }
        }
    }

    /**
     *
     */
    @Test
    public void testToBytes() {
        NullBitMap instance = new NullBitMap();
        byte[] bytes = Bytes.fromNullBitMap(instance);
        assertEquals(8, bytes.length);
    }
    
    /**
     *
     */
    @Test
    public void testToBytes2() {
        NullBitMap instance = new NullBitMap();
        instance.setNull(5);
        instance.setNull(15);
        instance.setNull(28);
        instance.setNull(29);
        instance.setNull(55);
        byte[] bytes = Bytes.fromNullBitMap(instance);
        byte[] expBytes = {0b100000,(byte)0b10000000,0b0,0b110000,0b0,0b0,(byte)0b10000000,0b0};
        assertEquals(expBytes.length, bytes.length);
        assertArrayEquals(expBytes, bytes);
    }

    /**
     * 
     */
    @Test
    public void testFromBytes() {
        byte[] bytes = {0,0,0,0,0,0,0,0};
        NullBitMap nullMap = Bytes.toNullBitMap(bytes);
        for (int i = 0; i < 64; i++) {
            assertFalse(nullMap.isNull(i));
        }
    }
    
    /**
     * 
     */
    @Test
    public void testFromBytes2() {
        byte[] bytes = {0b100000,(byte)0b10000000,0b0,0b110000,0b0,0b0,(byte)0b10000000,0b0};
        NullBitMap nullMap = Bytes.toNullBitMap(bytes);
        for (int i = 0; i < 64; i++) {
            if(i== 5 || i==15||i==28||i==29||i==55){
                assertTrue(nullMap.isNull(i));
            }else{
                assertFalse(nullMap.isNull(i));
            }
        }
    }
}
