package com.shawntime.common.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mashaohua
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisLockable {

    String prefix() default "";

    String[] key() default "";

    long expiration() default 60;

    boolean isWaiting() default false; //锁是否等待，默认为不等待

    int retryCount() default -1; // 锁等待重试次数，-1未不限制

    int retryWaitingTime() default 10; // 锁等待重试间隔时间，默认10毫秒
}
