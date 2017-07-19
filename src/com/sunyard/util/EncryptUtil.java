package com.sunyard.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * 简单封装Spring Security StandardPasswordEncoder
 * @author mumu
 *
 */
public class EncryptUtil {
    //从配置文件中获得
    private static final String SITE_WIDE_SECRET = "sunyard"; //密钥与spring-security.xml配置密钥相同
    private static final PasswordEncoder encoder = new StandardPasswordEncoder(SITE_WIDE_SECRET);
 
    public static String encrypt(String rawPassword) {
         return encoder.encode(rawPassword);
    }
 
    public static boolean match(String rawPassword, String password) {
         return encoder.matches(rawPassword, password);
    }
      
    public static void main(String[] args) {
    	System.out.println(EncryptUtil.encrypt("admin"));
    	System.out.println(EncryptUtil.encrypt("admin"));
        System.out.println(EncryptUtil.encrypt("admin"));
    }

 }

