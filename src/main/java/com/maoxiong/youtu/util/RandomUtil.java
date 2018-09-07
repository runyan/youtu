package com.maoxiong.youtu.util;

import java.util.Random;

public class RandomUtil {

	public RandomUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static String genRandomNum(int length) {
		int i;
		int count = 0;
		StringBuffer pwd = new StringBuffer();
		Random r = new Random();
		while (count < length) {
			i = r.nextInt(10);
			if(i >= 0) {
				pwd.append(String.valueOf(i));
				count++;
			}
		}
		return pwd.toString();
	}   
}
