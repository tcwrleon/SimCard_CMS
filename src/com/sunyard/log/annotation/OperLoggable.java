package com.sunyard.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 * @author mumu
 *
 */

@Target(ElementType.METHOD)    
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLoggable {
	public String module() default "";
	public String description() default "";
}
