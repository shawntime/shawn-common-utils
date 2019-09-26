package com.shawntime.common.ratelimit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.shawntime.common.common.spelkey.KeySpELAdviceSupport;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

/**
 * Created by IDEA
 * User: shawntime
 * Date: 2016-11-25 14:11:20
 * Desc: 访问上限拦截
 */
@Aspect
@Component
public class RateLimitInterceptor extends KeySpELAdviceSupport {

    @Pointcut("@annotation(com.shawntime.common.ratelimit.*)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = AopUtils.getMostSpecificMethod(methodSignature.getMethod(), point.getTarget().getClass());
        Object target = point.getTarget();
        Object[] arguments = point.getArgs();

        String key = "";
        long expire = 0;
        int limit = 5;
        Annotation[] annotations = targetMethod.getAnnotations();
        for(Annotation annotation : annotations) {
            if(annotation.annotationType() == InnerRateLimitable.class) {
                InnerRateLimitable limitable = targetMethod.getAnnotation(InnerRateLimitable.class);
                key = getInnerKey(limitable, targetMethod, target, arguments);
                expire = limitable.expiration();
                limit = limitable.limit();
            } else if(annotation.annotationType() == SectionRateLimitable.class) {
                SectionRateLimitable limitable = targetMethod.getAnnotation(SectionRateLimitable.class);
                key = getSectionKey(limitable, targetMethod, target, arguments);
                expire = limitable.timeUnit().getExpire();
                limit = limitable.limit();
            } else {
                return point.proceed();
            }
        }

        boolean isLimit = RateLimitUtils.isExceedRate(key, expire, limit);
        if(isLimit) {
            throw new RateLimitException("访问上限");
        }

        return point.proceed();
    }

    private String getSectionKey(SectionRateLimitable rateLimitable,
                            Method targetMethod, Object target, Object[] arguments) {
        String key = rateLimitable.key();
        String prefix = rateLimitable.prefix();
        StringBuilder sb = getSpElKey(key, prefix, targetMethod, target, arguments);
        //时间段
        sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(rateLimitable.timeUnit().getFormat())));
        return sb.toString();
    }

    private String getInnerKey(InnerRateLimitable innerRateLimitable,
                                 Method targetMethod, Object target, Object[] arguments) {

        String key = innerRateLimitable.key();
        String prefix = innerRateLimitable.prefix();
        StringBuilder sb = getSpElKey(key, prefix, targetMethod, target, arguments);
        return sb.toString();
    }

    private StringBuilder getSpElKey(String keyStr, String prefix, Method targetMethod, Object target, Object[] arguments) {
        StringBuilder sb = new StringBuilder("limit.");
        sb.append(prefix);
        if(StringUtils.isNotEmpty(keyStr)) {
            SpELOperationContext context = getOperationContext(targetMethod, arguments, target, target.getClass());
            Object key = generateKey(keyStr, context);
            sb.append("#").append(key);
        }
        return sb;
    }



}
