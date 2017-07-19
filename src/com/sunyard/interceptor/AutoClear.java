package com.sunyard.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日终清算流程注解
 * @author mumu
 *
 */

@Target(ElementType.METHOD)    
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoClear {
	
}
