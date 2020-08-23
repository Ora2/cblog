package co.happymeal.cblog.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 * @author lyd
 */
@Aspect
@Component
public class LogAdvice {

    public static final Logger LOG = LoggerFactory.getLogger(LogAdvice.class);

    @Pointcut("(execution(public * co.happymeal.cblog.controller..*.*(..))) || " +
            "(execution(public * co.happymeal.cblog.service.*.*(..)))")
    private void invocation(){}

    @Before("invocation()")
    public void before(@NotNull JoinPoint joinPoint) {
        try {
            Field log = joinPoint.getTarget().getClass().getDeclaredField("LOG");
            log.setAccessible(true);
            Logger herLog = (Logger) log.get(joinPoint.getTarget());
            herLog.info(joinPoint.getStaticPart().getSignature().getName() + "(..) <- " + Arrays.toString(joinPoint.getArgs()));
        } catch (Exception e) {
            LOG.warn(joinPoint.getStaticPart().getSignature().getDeclaringTypeName() + " has no logger...");
        }
    }

}
