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
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SectionRateLimitable {

    enum TimeUnit {

        DAYS("yyyyMMdd", 24 * 60 * 60),
        HOURS("yyyyMMddHH", 60 * 60),
        MINUTES("yyyyMMddHHmm", 60),
        SECOND("yyyyMMddHHmmss", 1);

        private String format;
        private long expire;

        TimeUnit(String format, long expire) {
            this.format = format;
            this.expire = expire;
        }

        public String getFormat() {
            return format;
        }

        public long getExpire() {
            return expire;
        }
    }

    String prefix() default "";

    String key() default "";

    TimeUnit timeUnit() default TimeUnit.DAYS;

    int limit() default 5;
}
