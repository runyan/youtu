package com.maoxiong.youtu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yanrun
 *
 */
public class LogUtil {

    /**
     * 获取最原始被调用的堆栈信息
     * @return
     */
    private static StackTraceElement findCaller() {
        // 获取堆栈信息
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        if(null == callStack){
        	return null;
        }
        // 最原始被调用的堆栈信息
        StackTraceElement caller = null;
        // 日志类名称
        String logClassName = LogUtil.class.getName();
        // 循环遍历到日志类标识
        boolean isEachLogClass = false;
 
        String className;
        // 遍历堆栈信息，获取出最原始被调用的方法信息
        for (StackTraceElement strackTraceEle : callStack) {
        	className = strackTraceEle.getClassName();
            // 遍历到日志类
            if(logClassName.equals(className)) {
                isEachLogClass = true;
            }
            // 下一个非日志类的堆栈，就是最原始被调用的方法
            if(isEachLogClass) {
                if(!logClassName.equals(className)) {
                    isEachLogClass = false;
                    caller = strackTraceEle;
                    break;
                }
            }
        }
        return caller;
    }
 
    /**
     * 自动匹配请求类名，生成logger对象，此处 logger name 值为 [className].[methodName]() Line: [fileLine]
     * @return    
     */
    private static Logger logger() {
    	//最原始被调用的堆栈对象
        StackTraceElement caller = findCaller();
        return caller == null ? LoggerFactory.getLogger(LogUtil.class) : 
        	LoggerFactory.getLogger(caller.getClassName() + " " + caller.getLineNumber());
    }
    
    public static void trace(String msg) {
    	logger().trace(msg);
    }
    public static void trace(String format, Object... args) {
    	logger().trace(format, args);
    }
    public static void debug(String msg) {
    	logger().debug(msg);
    }
    public static void debug(String format, Object... args) {
    	logger().debug(format, args);
    }
    public static void info(String msg) {
    	logger().info(msg);
    }
    public static void info(String format, Object... args) {
    	logger().info(format, args);
    }
    public static void warn(String msg) {
    	logger().warn(msg);
    }
    public static void warn(String format, Object... args) {
    	logger().warn(format, args);
    }
    public static void error(String msg) {
    	logger().error(msg);
    }
    public static void error(String format, Object... args) {
    	logger().error(format, args);
    }
    public static boolean isDebugEnabled() {
    	return logger().isDebugEnabled();
    }
}
