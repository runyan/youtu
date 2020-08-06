package com.maoxiong.youtu.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author yanrun
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YoutuConfig {
	
	String congfigFilePath() default "";
	
	String qq() default "";
	
	String appId() default "";
	
	String secretId() default "";
	
	String secretKey() default "";
	
	String fileSavePath() default "";

}
