package com.maserhe.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * 描述:
 *
 * @author Maserhe
 * @create 2021-05-02 20:09
 */
@Aspect
@Component
public class ServiceAspect {

    final static Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

    /**
     * Aop 通知
     */
    @Around("execution(* com.maserhe.*.service.Impl..*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("============开始执行{}.{}===============", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        joinPoint.proceed();

        long end = System.currentTimeMillis();
        long takeTime = start - end;
        if (takeTime > 3000) {
            logger.warn("当前执行消耗时间{}", takeTime);
        } else {
            logger.info("当前执行消耗时间{}", takeTime);
        }
        return null;
    }



}
