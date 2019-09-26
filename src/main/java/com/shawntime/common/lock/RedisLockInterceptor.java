package com.shawntime.common.lock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Joiner;
import com.shawntime.common.utils.RedisLockUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author mashaohua
 */
@Aspect
@Component
public class RedisLockInterceptor {

    private static final LocalVariableTableParameterNameDiscoverer DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Pointcut("@annotation(com.shawntime.common.lock.RedisLockable)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = AopUtils.getMostSpecificMethod(methodSignature.getMethod(), point.getTarget().getClass());
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();

        RedisLockable redisLock = targetMethod.getAnnotation(RedisLockable.class);
        long expire = redisLock.expiration();
        String redisKey = getLockKey(redisLock, targetMethod, targetName, methodName, arguments);
        String uuid;
        if (redisLock.isWaiting()) {
            uuid = waitingLock(redisKey, expire, redisLock.retryCount(), redisLock.retryWaitingTime());
        } else {
            uuid = noWaitingLock(redisKey, expire);
        }
        if (StringUtils.isNotEmpty(uuid)) {
            try {
                return point.proceed();
            } finally {
                RedisLockUtil.unLock(redisKey, uuid);
            }
        } else {
            throw new RedisLockException(redisKey);
        }
    }

    private String getLockKey(RedisLockable redisLock, Method targetMethod,
                              String targetName, String methodName, Object[] arguments) {
        String[] keys = redisLock.key();
        String prefix = redisLock.prefix();
        StringBuilder sb = new StringBuilder("lock.");
        if (StringUtils.isEmpty(prefix)) {
            sb.append(targetName).append(".").append(methodName);
        } else {
            sb.append(prefix);
        }
        if (keys != null) {
            String keyStr = Joiner.on("+ '.' +").skipNulls().join(keys);
            EvaluationContext context = new StandardEvaluationContext(targetMethod);
            String[] parameterNames = DISCOVERER.getParameterNames(targetMethod);
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], arguments[i]);
            }
            Object key = PARSER.parseExpression(keyStr).getValue(context);
            sb.append("#").append(key);
        }
        return sb.toString();
    }

    private String noWaitingLock(String key, long expire) {
        return RedisLockUtil.lock(key, expire);
    }

    private String waitingLock(String key, long expire, int retryCount, int retryWaitingTime)
            throws InterruptedException {
        int count = 0;
        while (retryCount == -1 || count <= retryCount) {
            String uuid = noWaitingLock(key, expire);
            if (!StringUtils.isEmpty(uuid)) {
                return uuid;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(retryWaitingTime);
            } catch (InterruptedException e) {
                throw e;
            }
            count++;
        }
        return null;
    }
}
