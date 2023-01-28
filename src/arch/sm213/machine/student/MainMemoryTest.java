package arch.sm213.machine.student;

import machine.AbstractMainMemory;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainMemoryTest {

    MainMemory mainFour = new MainMemory(4);
    MainMemory mainEight = new MainMemory(8);

    // tests isAccessAligned method, here the address is aligned to the length of the object (size of address is evenly
    // divisible by object size), test is performed for memory of byte capacity 4 and 8
    @Test
    public void testIsAlignedAccessAligned() {
        assertTrue(mainFour.isAccessAligned(0, 1));
        assertTrue(mainEight.isAccessAligned(0, 1));
        assertTrue(mainFour.isAccessAligned(1, 1));
        assertTrue(mainFour.isAccessAligned(4, 2));
        assertTrue(mainEight.isAccessAligned(16, 4));
        assertTrue(mainFour.isAccessAligned(100, 20));
    }

    // tests isAccessAligned method, here the address is not aligned to the length of the object (size of address is not
    // evenly divisible by object size), test is performed for memory of byte capacity 4 and 8
    @Test
    public void testIsAlignedAccessUnaligned() {
        assertFalse(mainFour.isAccessAligned(1, 2));
        assertFalse(mainFour.isAccessAligned(4, 3));
        assertFalse(mainEight.isAccessAligned(32, 20));
        assertFalse(mainFour.isAccessAligned(1000, 184));
    }

    // tests that Set and Get methods, here the InvalidAddressException is thrown when the address given is < 0,
    // or some part of 'address' to 'address+length-1' is invalid
    @Test
    public void testSetandGetThrowException() throws AbstractMainMemory.InvalidAddressException {
        byte[] bytesFour = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytesFour[i] = (byte) 0x14;
        }
        byte[] bytesEight = new byte[8];
        for (int i = 0; i < 8; i++) {
            bytesEight[i] = (byte) 0xfa;
        }
        try {
            mainFour.set(1, bytesFour); // tries to access an invalid address midway through
            mainFour.get(1, 1);
            fail("did not throw InvalidAddressException");
        } catch (AbstractMainMemory.InvalidAddressException e) {
        }
        try {
            mainFour.set(5, bytesFour); // initial address is invalid
            mainFour.get(5, 1);
            fail("did not throw InvalidAddressException");
        } catch (AbstractMainMemory.InvalidAddressException e) {
        }
        try {
            mainFour.set(-1, bytesFour); // initial address is < 0
            mainFour.get(-1, 1);
            fail("did not throw InvalidAddressException");
        } catch (AbstractMainMemory.InvalidAddressException e) {
        }
    }

    // tests Set and Get methods for MainMemory, tests InvalidAddressException to ensure that it is not thrown when
    // input values are valid
    @Test
    public void testSetandGet() throws AbstractMainMemory.InvalidAddressException {
        byte[] bytes = new byte[4];
        byte a = (byte) 0x87;
        byte b = (byte) 0x65;
        byte c = (byte) 0x43;
        byte d = (byte) 0x21;
        bytes[0] = a;
        bytes[1] = b;
        bytes[2] = c;
        bytes[3] = d;

        try {
            mainFour.set(0, bytes);

            byte[] testFour1 = mainFour.get(0, 4);
            byte[] testFour2 = mainFour.get(1, 2);

            assertEquals(testFour1[0], a);
            assertEquals(testFour1[1], b);
            assertEquals(testFour1[2], c);
            assertEquals(testFour1[3], d);

            assertEquals(testFour2[0], b);
            assertEquals(testFour2[1], c);

        } catch (AbstractMainMemory.InvalidAddressException e) {
            fail("threw InvalidAddressException");
        }
    }

    // tests bytesToInteger method in 4 cases; one case where all bytes are set to 0, one case where
    // the bytes represent a small positive integer, one where the bytes represent a large positive integer, and one
    // where the bytes represent a negative integer with two's complement
    @Test
    public void testBytesToInteger() {
        byte z = 0;
        byte a = (byte) 0x12;
        byte b = (byte) 0x34;
        byte c = (byte) 0x56;
        byte d = (byte) 0x78;
        byte e = (byte) 0xfa;

        assertEquals(mainFour.bytesToInteger(z, z, z, z), 0);
        assertEquals(mainFour.bytesToInteger(z, z, z, e), 250);
        assertEquals(mainFour.bytesToInteger(a, b, c, d), 305419896);
        assertEquals(mainFour.bytesToInteger(e, a, b, c), -99470250);
    }

    // tests integerToBytes method in 4 cases; one case where integer is 0, one case where
    // the integer is a small positive value, one where the integer is a large positive value,
    // and one where the integer is a negative value
    @Test
    public void testIntegerToBytes() {
        byte[] zeroTest = mainFour.integerToBytes(0);
        byte[] smallTest = mainFour.integerToBytes(17);
        byte[] largeTest = mainFour.integerToBytes(287392512);
        byte[] negTest = mainFour.integerToBytes(-1000);
        byte a = (byte) 0x00;
        byte b = (byte) 0x11;
        byte c = (byte) 0x43;
        byte d = (byte) 0x21;
        byte e = (byte) 0xfc;
        byte f = (byte) 0x18;
        byte g = (byte) 0xff;

        assertEquals(zeroTest[0],a);
        assertEquals(zeroTest[1],a);
        assertEquals(zeroTest[2],a);
        assertEquals(zeroTest[3],a);

        assertEquals(smallTest[0], a);
        assertEquals(smallTest[1], a);
        assertEquals(smallTest[2], a);
        assertEquals(smallTest[3], b);

        assertEquals(largeTest[0], b);
        assertEquals(largeTest[1], d);
        assertEquals(largeTest[2], c);
        assertEquals(largeTest[3], a);

        assertEquals(negTest[0], g);
        assertEquals(negTest[1], g);
        assertEquals(negTest[2], e);
        assertEquals(negTest[3], f);
    }

}
