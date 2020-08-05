package com.maoxiong.youtu.util;

/**
 * 
 * @author yanrun
 *
 */
public class RandomUtil {

	public RandomUtil() {
		throw new RuntimeException("no constructor for you");
	}

	public static String genRandomNum(int length) {
		long timeInNano = System.nanoTime();
		String nanoStr = String.valueOf(timeInNano);
		int nanoLength = nanoStr.length();
		length = nanoLength < 10 ? nanoLength : length;
		return nanoStr.substring(0, length);
	}
}
