import java.util.HashMap;
import java.util.Map;

public class BrailleDictionary {
	private static Map<Character, String> brailleDict;
//	private static Map<Integer, String> integerDict;
//	private boolean intFlag;
	
	public BrailleDictionary() {
		brailleDict = new HashMap<Character, String>();
//		integerDict = new HashMap<Integer, String>();
//		intFlag = false;
		brailleDict.put(' ', "000000");
		brailleDict.put('a', "000001");
		brailleDict.put('b', "000011");
		brailleDict.put('c', "001001");
		brailleDict.put('d', "011001");
		brailleDict.put('e', "010001");
		brailleDict.put('f', "001011");
		brailleDict.put('g', "011011");
		brailleDict.put('h', "010011");
		brailleDict.put('i', "001010");
		brailleDict.put('j', "011010");
		brailleDict.put('k', "000101");
		brailleDict.put('l', "000111");
		brailleDict.put('m', "001101");
		brailleDict.put('n', "011101");
		brailleDict.put('o', "010101");
		brailleDict.put('p', "001111");
		brailleDict.put('q', "011111");
		brailleDict.put('r', "010111");
		brailleDict.put('s', "001110");
		brailleDict.put('t', "011110");
		brailleDict.put('u', "100101");
		brailleDict.put('v', "100111");
		brailleDict.put('w', "111010");
		brailleDict.put('x', "101101");
		brailleDict.put('y', "111101");
		brailleDict.put('z', "110101");
		brailleDict.put('#', "110101");
		
		//ADD SPECIAL CHARS
		
		
//		integerDict.put(Integer.valueOf(1), "000001");
//		integerDict.put(Integer.valueOf(2), "000011");
//		integerDict.put(Integer.valueOf(3), "001001");
//		integerDict.put(Integer.valueOf(4), "011001");
//		integerDict.put(Integer.valueOf(5), "010001");
//		integerDict.put(Integer.valueOf(6), "001011");
//		integerDict.put(Integer.valueOf(7), "011011");
//		integerDict.put(Integer.valueOf(8), "010011");
//		integerDict.put(Integer.valueOf(9), "001010");
//		integerDict.put(Integer.valueOf(0), "011010");

	}
	
	public String getBraille(Character c) {
		
		return brailleDict.get(c);
	}
	
	public boolean contains(Character c) {
		return brailleDict.containsKey(c);
		
	}
	
	
	
	
	
	
	
	
	
	
}
