package com.shawntime.common.cache.redis;

import java.util.List;

import com.shawntime.common.common.ApplicationContextUtil;
import com.shawntime.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.StaticScriptSource;

public class SpringRedisUtils {

    private static final StringRedisTemplate redisTemplate = (StringRedisTemplate) ApplicationContextUtil.getBean("redisTemplate");

    private DefaultRedisScript<Long> script;

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

    public static boolean setNX(final String key, final String value, final long expired) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            String result = (String) connection.execute("set",
                    key.getBytes(), value.getBytes(),
                    "nx".getBytes(), "ex".getBytes(), String.valueOf(expired).getBytes());
            if (result == null) {
                return false;
            }
            return "OK".equals(result);
        });
    }

    public static <T> T getSet(final String key, Object value, Class<T> clazz) {
        final String jsonValue = JsonUtils.serialize(value);
        String oldValue = redisTemplate.opsForValue().getAndSet(key, jsonValue);
        if (StringUtils.isEmpty(oldValue)) {
            return null;
        }
        return JsonUtils.deSerialize(oldValue, clazz);
    }

    public static void delete(final String key) {
        redisTemplate.delete(key);
    }

    public static long increment(final String key, long expireTime) {
        return redisTemplate.opsForValue().increment(key, expireTime);
    }

    public static Object lua(final String script, List<String> keys, List<String> args) {
        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Object.class);
        redisScript.setScriptSource(new StaticScriptSource(script));
        return redisTemplate.execute(redisScript, keys, args);
    }
}
