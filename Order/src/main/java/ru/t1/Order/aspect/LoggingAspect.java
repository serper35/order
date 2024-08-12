package ru.t1.Order.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class LoggingAspect {
    private static final Logger log = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("execution(* ru.t1.Order.service.impl.*.*(..))")
    private void allServiceMethods() {
    }

    @Before("allServiceMethods()")
    public void beforeMethodAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Calling method {} with args {}", methodName, args);
    }

    @AfterReturning(pointcut = "allServiceMethods()", returning = "result")
    public void afterReturningMethodAdvice(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Returning from method {} with result {}", methodName, result);
    }

    @AfterThrowing(pointcut = "allServiceMethods()", throwing = "exception")
    public void afterThrowingMethodAdvice(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("Exception thrown in method {}: {}", methodName, exception.getMessage());
    }
}
