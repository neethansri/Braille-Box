package test;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.RaspberryPi2;

class RaspberryPi2Test {
	private RaspberryPi2 testPi;
	@BeforeEach
	void setUp() throws Exception {
		testPi = new RaspberryPi2(1002);
	}
	
	@Test
	void testBrailleDict() {
		assertEquals("100101", testPi.convertCharToBraille('u'), "U character should return 100101");
		assertEquals("110101", testPi.convertCharToBraille('#'), "# character should return 110101");
		assertEquals("000000", testPi.convertCharToBraille(' '), "Blank character should return 100101");
		assertEquals("000000", testPi.convertCharToBraille('*'), "Invalid  character should return 100101");

	}
	
	@Test
	void testProperPacket() {
		assertTrue(testPi.isProperPacket("hello"), "A proper message should return true");
		assertFalse(testPi.isProperPacket("hello1234"), "An improper message should return false");
		assertFalse(testPi.isProperPacket(""), "Empty message should return false");
		assertFalse(testPi.isProperPacket(null), "Null should return false");
		
	}
	
	@Test
	void testCharArray() {
		
		testPi.isProperPacket("hi1234");
		assertNull(testPi.getCharArray(), "Invalid message should not update charArray, charArray should return null");
		
		testPi.isProperPacket("hello");
		char[] testArray = new char[] {'h', 'e', 'l', 'l', 'o'};
		assertArrayEquals(testArray, testPi.getCharArray(), "hello should return the character array: ['h', 'e', 'l', 'l', 'o']");
		
		testPi.isProperPacket("hi1234");
		assertArrayEquals(testArray, testPi.getCharArray(), "Invalid message should not update charArray, charArray should return ['h', 'e', 'l', 'l', 'o']");
		
	}
	
	@Test
	void testLastChar() {
		testPi.isProperPacket("hello");
		assertFalse(testPi.isLastChar(), "testPi is not on last character, should return false");
		
		RaspberryPi2 testPi2 = new RaspberryPi2(1002, 5);
		testPi2.isProperPacket("hello");
		assertTrue(testPi2.isLastChar(), "testPi2 is on the last character, should return true");
	}

}
