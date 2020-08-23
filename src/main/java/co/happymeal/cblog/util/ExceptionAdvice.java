package co.happymeal.cblog.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lyd
 */
//@Aspect
//@Component
public class ExceptionAdvice {

    @Pointcut("execution(public * co.happymeal.cblog.controller..*.*(..))")
    private void invocation(){}

    @AfterThrowing(value = "invocation()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, @NotNull Throwable e) throws IOException {
        System.out.println("Catched");
//        if ("errorMessage".equals(e.getMessage())) {
//            throw new IOException("IOException");
//        }
    }
}
