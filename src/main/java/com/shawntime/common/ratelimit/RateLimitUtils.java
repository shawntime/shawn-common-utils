package com.shawntime.common.ratelimit;

import com.shawntime.common.cache.redis.SpringRedisUtils;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-25 10:13
 * Desc: 频率限制
 */
public final class RateLimitUtils {

    private RateLimitUtils() {
        //
    }

    /**
     * 访问频率限制
     * @param key redis锁前缀名
     * @param expireTime 过期时间
     * @param limit 访问上限
     * @return true:超过，false：没有超过
     */
    public static boolean isExceedRate(String key, long expireTime, int limit) {
        long num = SpringRedisUtils.increment(key, expireTime);
        return num >= limit;

    }
}
