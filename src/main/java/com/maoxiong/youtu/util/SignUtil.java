package com.maoxiong.youtu.util;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author yanrun
 *
 */
public class SignUtil {
	
	public static String getSign(String userQQ, String appID, String secretID, String secretKey) {  
		long tnowTimes = System.currentTimeMillis() / 1000;  
        long enowTimes = tnowTimes + 2592000;  
        String rRandomNum = HmacSha1.genRandomNum(10);  
        String param = "u=" + userQQ + "&a=" + appID + "&k=" + secretID + "&e="  
                + enowTimes + "&t=" + tnowTimes + "&r=" + rRandomNum + "&f=";  
        byte[] paramBytes = param.getBytes();
        int paramBytesLength = paramBytes.length;
        byte[] hmacSign = HmacSha1.getSignature(param, secretKey);  
        int hmacSignLength = hmacSign.length;
        byte[] all = new byte[hmacSignLength + paramBytesLength]; 
        System.arraycopy(hmacSign, 0, all, 0, hmacSignLength);  
        System.arraycopy(paramBytes, 0, all, hmacSignLength, paramBytesLength);  
        return Base64.encodeBase64String(all);
    }  

}
