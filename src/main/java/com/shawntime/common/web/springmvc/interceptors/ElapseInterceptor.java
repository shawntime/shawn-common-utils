package com.shawntime.common.web.springmvc.interceptors;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by zhouxiaoming on 2015/8/31.
 */
public class ElapseInterceptor {
    private Boolean isOpen;

    private static Logger elapseLog = Logger.getLogger("elapseLogger");

    private static Logger elapseTimeOutLog = Logger.getLogger("elapseTimeOutLogger");

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if (isOpen) {
            String className = pjp.getSignature().getDeclaringTypeName();
            String methodName = pjp.getSignature().getName();
            long startDate = System.currentTimeMillis();
            Object returnValue = pjp.proceed();
            long endDate = System.currentTimeMillis();
            String topic = className + "/" + methodName;
            writeToLog(topic, (endDate - startDate));
            return returnValue;
        } else {
            return pjp.proceed();
        }
    }

    private void writeToLog(String topic, long elapsedMilliseconds) {
        String elapsedMillisecondsStringFormat = String.valueOf(elapsedMilliseconds);
        elapseLog.warn(topic + "`" + elapsedMillisecondsStringFormat);

        if (elapsedMilliseconds > 2000) {
            elapseTimeOutLog.fatal(getErrorLogMessage(elapsedMillisecondsStringFormat, topic));
        } else if (elapsedMilliseconds > 1000) {
            elapseTimeOutLog.error(getErrorLogMessage(elapsedMillisecondsStringFormat, topic));
        } else if (elapsedMilliseconds > 500) {
            elapseTimeOutLog.warn(getErrorLogMessage(elapsedMillisecondsStringFormat, topic));
        }
    }

    private String getErrorLogMessage(String elapsedMilliseconds, String topic) {
        return "AOP方法拦截超时,耗时:" + elapsedMilliseconds + ",执行方法:" + topic;
    }
}