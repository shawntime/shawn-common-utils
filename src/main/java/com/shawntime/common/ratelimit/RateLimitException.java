package com.shawntime.common.ratelimit;


/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-25 14:09
 * Desc:
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }

}
