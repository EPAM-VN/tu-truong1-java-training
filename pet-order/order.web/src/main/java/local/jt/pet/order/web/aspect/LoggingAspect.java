package local.jt.pet.order.web.aspect;

import local.jt.pet.order.web.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("execution(public * local.jt.pet.order.web.services.CustomerService.create(..))")
    private void logInfo() {}

    @Around("logInfo()")
    private Object logAround(ProceedingJoinPoint joinPoint)  throws Throwable {
        log.info("Before {}.{}", joinPoint.getClass().getName(), joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();

        log.info("After  {}.{}", joinPoint.getClass().getName(), joinPoint.getSignature().getName());

        return result;
    }

}
