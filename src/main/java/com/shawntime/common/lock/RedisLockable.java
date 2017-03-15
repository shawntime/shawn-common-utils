package com.shawntime.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RUNTIME
 * 定义注解
 * 编译器将把注释记录在类文件中，在运行时 VM 将保留注释，因此可以反射性地读取。
 * @author shma1664
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLockable {

    String prefix() default "";

    String[] key() default "";

    long expiration() default 120;

    boolean isWaiting() default false; //锁是否等待，默认为不等待

    int retryCount() default -1; // 锁等待重试次数，-1未不限制

    int retryWaitTime() default 10; //重试等待时间，默认10毫秒，单位毫秒
}
