package com.shawntime.common.utils;

import java.util.Collections;
import java.util.UUID;

import com.shawntime.common.cache.redis.SpringRedisUtils;

/**
 * Created by shma on 2018/8/29.
 */
public final class RedisLockUtil {

    private static final int DEFAULT_EXPIRE = 60;

    private static final String SCRIPT =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
            + "then\n"
            + "    return redis.call(\"del\",KEYS[1])\n"
            + "else\n"
            + "    return 0\n"
            + "end";

    private RedisLockUtil() {
        super();
    }

    /**
     *
     * @param key 锁的key
     * @return 返回value为null，则锁失败，不为null则锁成功
     */
    public static String lock(String key) {
        return lock(key, DEFAULT_EXPIRE);
    }

    public static boolean lock(String key, String value) {
        return lock(key, value, DEFAULT_EXPIRE);
    }

    public static String lock(String key, long expire) {
        String value = UUID.randomUUID().toString();
        boolean nx = SpringRedisUtils.setNX(key, value, expire);
        return nx ? value : null;
    }

    public static boolean lock(String key, String value, long expire) {
        return SpringRedisUtils.setNX(key, value, expire);
    }

    public static void unLock(String key, String value) {
        SpringRedisUtils.lua(SCRIPT, Collections.singletonList(key), Collections.singletonList(value));
    }
}
