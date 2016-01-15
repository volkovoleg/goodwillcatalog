package ru.goodfil.catalog.aspects;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: LoggingAspect.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
@Aspect
public class LoggingAspect {
    private static final boolean LOGGING_ENABLED = false;

    @Pointcut("within(ru.goodfil.catalog..*)")
    public void inCatalog() {
    }

    @Before("call(@ru.goodfil.catalog.annotations.Logged * *.*(..)) && inCatalog()")
    public void logEntering(JoinPoint jp) throws Throwable {
        if (!LOGGING_ENABLED) return;

        Object target = jp.getTarget();
        if (target != null) {
            Method method = ((MethodSignature) jp.getSignature()).getMethod();
            Object[] args = jp.getArgs();

            String methodName = method.getName();
            String className = target.getClass().getSimpleName();

            Logger logger = Logger.getLogger(target.getClass().getName());
            logger.finest("Enterging " + className + "#" + methodName + "(" + StringUtils.join(args, ",") + ")");
            System.out.println("Enterging " + className + "#" + methodName + "(" + StringUtils.join(args, ",") + ")");
        }
    }
}
