package com.shawntime.common.common.spelkey;

import java.lang.reflect.Method;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Created by IDEA
 * User: mashaohua
 * Date: 2016-10-12 10:04
 * Desc:
 */
public final class MethodCacheKey {

    private final Method method;

    private final Class<?> targetClass;

    public MethodCacheKey(Method method, Class<?> targetClass) {
        Assert.notNull(method, "method must be set.");
        this.method = method;
        this.targetClass = targetClass;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodCacheKey)) {
            return false;
        }
        MethodCacheKey otherKey = (MethodCacheKey) other;
        return (this.method.equals(otherKey.method) && ObjectUtils.nullSafeEquals(this.targetClass,
                otherKey.targetClass));
    }

    @Override
    public int hashCode() {
        return this.method.hashCode() * 29 + (this.targetClass != null ? this.targetClass.hashCode() : 0);
    }

}
