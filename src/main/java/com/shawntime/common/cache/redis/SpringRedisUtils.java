package com.shawntime.common.cache.redis;

import com.shawntime.common.common.ApplicationContextUtil;
import com.shawntime.common.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class SpringRedisUtils {

    private static final StringRedisTemplate redisTemplate = (StringRedisTemplate) ApplicationContextUtil.getBean("redisTemplate");

    public static void set(final String key, Object value) {
        final String jsonValue = JsonUtils.serialize(value);
        redisTemplate.opsForValue().set(key, jsonValue);
    }

    public static <T> T get(final String key, Class<T> elementType) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        return JsonUtils.deSerialize(jsonValue, elementType);
    }

    public static boolean setNX(final String key, Object value) {
        final String jsonValue = JsonUtils.serialize(value);
        return redisTemplate.opsForValue().setIfAbsent(key, jsonValue);
    }

    public static <T> T getSet(final String key, Object value, Class<T> clazz) {
        final String jsonValue = JsonUtils.serialize(value);
        String oldValue = redisTemplate.opsForValue().getAndSet(key, jsonValue);
        return JsonUtils.deSerialize(oldValue, clazz);
    }

    public static void delete(final String key) {
        redisTemplate.delete(key);
    }

    public static long increment(final String key, long expireTime) {
        return redisTemplate.opsForValue().increment(key, expireTime);
    }
}
