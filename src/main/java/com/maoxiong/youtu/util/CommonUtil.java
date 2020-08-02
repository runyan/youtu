package com.maoxiong.youtu.util;

/**
 * 
 * @author yanrun
 *
 */
public class CommonUtil {

	public static String singularOrPlural(int num, String singular, String plural) {
		return num <= 1 ? singular : plural;
	}
}
