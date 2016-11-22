package com.shawntime.utils.cache.redis;

import com.shawntime.utils.common.ApplicationContextUtil;
import com.shawntime.utils.helper.JsonHelper;
import org.springframework.data.redis.core.StringRedisTemplate;

public class SpringRedisHelper {

    private static final StringRedisTemplate redisTemplate = (StringRedisTemplate) ApplicationContextUtil.getBean("redisTemplate");

    public static void set(final String key, Object value) {
        final String jsonValue = JsonHelper.serialize(value);
        redisTemplate.opsForValue().set(key, jsonValue);
    }

    public static <T> T get(final String key, Class<T> elementType) {
        String jsonValue = redisTemplate.opsForValue().get(key);
        return JsonHelper.deSerialize(jsonValue, elementType);
    }

    public static boolean setNX(final String key, Object value) {
        final String jsonValue = JsonHelper.serialize(value);
        return redisTemplate.opsForValue().setIfAbsent(key, jsonValue);
    }

    public static <T> T getSet(final String key, Object value, Class<T> clazz) {
        final String jsonValue = JsonHelper.serialize(value);
        String oldValue = redisTemplate.opsForValue().getAndSet(key, jsonValue);
        return JsonHelper.deSerialize(oldValue, clazz);
    }

    public static void delete(final String key) {
        redisTemplate.delete(key);
    }
}
