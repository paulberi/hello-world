package se.metria.markkoll.util.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspects {
    @Pointcut("execution(* *(..))")
    public void allMethods() {}

    @Pointcut("within(@se.metria.markkoll.util.logging.TraceAllMethods *)")
    public void inBeanWithTraceMethodsAnnotation() {}

    @Pointcut("@annotation(se.metria.markkoll.util.logging.TraceMethod)")
    public void tracableMethod() {}

    @Pointcut("@annotation(logTime)")
    public void logTimeAnnotation(LogTime logTime) {}

    @Around("allMethods() && tracableMethod() && !inBeanWithTraceMethodsAnnotation()")
    public Object log(ProceedingJoinPoint jp) throws Throwable {
        return logTrace(jp);
    }

    @Around("allMethods() && inBeanWithTraceMethodsAnnotation()")
    public Object logClassPublic(ProceedingJoinPoint jp) throws Throwable {
        return logTrace(jp);
    }

    @Around("allMethods() && logTimeAnnotation(logTime)")
    public Object logTime(ProceedingJoinPoint jp, LogTime logTime) throws Throwable {
        var start = System.nanoTime();
        var result = jp.proceed();
        var end = System.nanoTime();

        var time = (end-start) / 1_000_000_000f;

        log.info(logTime.message(), time);

        return result;
    }

    private Object logTrace(ProceedingJoinPoint jp) throws Throwable {
        var args = jp.getArgs();
        var argsList = args == null ?
                new ArrayList() :
                Arrays.stream(args)
                        .map(o -> o == null ? null : o.toString())
                        .collect(Collectors.toList());

        log.trace("{}({})", jp.getSignature().getName(), argsList);

        var result = jp.proceed();

        if (result != null){
            log.trace("{}() return: {}", jp.getSignature().getName(), result.toString());
        }

        return result;
    }
}
