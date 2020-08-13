package com.maoxiong.youtu.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author yanrun
 *
 */
public class ExceptionUtil {

	public static String getExceptionStackTrace(Throwable anexcepObj) {
		try (StringWriter sw = new StringWriter();
				PrintWriter printWriter = new PrintWriter(sw);) {
			anexcepObj.printStackTrace(printWriter);
			printWriter.flush();
			sw.flush();
			return sw.toString();
		} catch (IOException e) {
			LogUtil.error(e.getMessage());
			return null;
		} 
		
	}

}
