package com.maoxiong.youtu.util;

import java.util.Base64;

/**
 * 
 * @author yanrun
 *
 */
public class SignUtil {
	
	private SignUtil() {
		throw new RuntimeException("no constructor for you");
	}
	
	public static String getSign(String userQQ, String appID, String secretID, String secretKey) {  
		long tnowTimes = System.currentTimeMillis() / 1000;  
        long enowTimes = tnowTimes + 2592000;  
        String rRandomNum = RandomUtil.genRandomNum(10);  
        String param = "u=" + userQQ + "&a=" + appID + "&k=" + secretID + "&e="  
                + enowTimes + "&t=" + tnowTimes + "&r=" + rRandomNum + "&f=";  
        byte[] paramBytes = param.getBytes();
        int paramBytesLength = paramBytes.length;
        byte[] hmacSign = HmacSha1.getSignature(param, secretKey);  
        int hmacSignLength = hmacSign.length;
        byte[] all = new byte[hmacSignLength + paramBytesLength]; 
        System.arraycopy(hmacSign, 0, all, 0, hmacSignLength);  
        System.arraycopy(paramBytes, 0, all, hmacSignLength, paramBytesLength);  
        return Base64.getEncoder().encodeToString(all);
    }
	
}
