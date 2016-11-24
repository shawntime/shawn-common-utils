package com.shawntime.common.web.annotation;

import java.lang.annotation.*;

/**
 * 表单入参验证注解
 *
 * @author YinZhaohua
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoValidate {

    /**
     * 必填验证
     */
    boolean required() default false;

    /**
     * 最大值验证
     */
    int maxLength() default -1;

    /**
     * 手机号验证
     */
    boolean phone() default false;

    /**
     * 提示前缀
     */
    String prefixMsg() default "字段";

}
