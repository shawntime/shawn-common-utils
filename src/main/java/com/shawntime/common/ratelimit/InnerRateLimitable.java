package com.shawntime.common.ratelimit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RUNTIME
 * 定义注解
 * 编译器将把注释记录在类文件中，在运行时 VM 将保留注释，因此可以反射性地读取。
 * @author shma1664
 * 时间内方式注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InnerRateLimitable {

    String prefix() default "";

    String key() default "";

    int limit() default 5;

    long expiration() default 120;
}
