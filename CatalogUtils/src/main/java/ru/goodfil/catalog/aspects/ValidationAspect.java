package ru.goodfil.catalog.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodValidator;

import javax.validation.*;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ValidationAspect.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
@Aspect
public class ValidationAspect {
    @Pointcut("within(@ru.goodfil.catalog.annotations.Managed ru.goodfil.catalog..*)")
    public void inCatalog() {
    }

    @Before("call(public * *(..)) && inCatalog()")
    public void validateArguments(JoinPoint jp) throws Throwable {
        Object target = jp.getTarget();
        if (target != null) {
            Method method = ((MethodSignature) jp.getSignature()).getMethod();
            Object[] args = jp.getArgs();

            try {
                Set<MethodConstraintViolation<Object>> problems = getMethodValidator().validateAllParameters(target, method, args);

                for (MethodConstraintViolation<Object> problem : problems) {
                    String parameterName = problem.getParameterName() + " " + problem.getMethod() + " " + problem.getKind();
                    throw new IllegalArgumentException(parameterName);
                }
            } catch (UnexpectedTypeException e) {
                //  TODO: fix nothing
                // nothing
            }
        }
    }

    @Before("execution(@ru.goodfil.catalog.annotations.ValidateBefore * *.*(..)) && inCatalog()")
    public void before(JoinPoint jp) {
        Object target = jp.getTarget();
        validate(target);
    }

    @After("execution(@ru.goodfil.catalog.annotations.ValidateAfter * *.*(..)) && inCatalog()")
    public void after(JoinPoint jp) {
        Object target = jp.getTarget();
        validate(target);
    }

    private void validate(Object o) {
        Set<ConstraintViolation<Object>> problems = getClassValidator().validate(o);

        if (problems.size() > 0) {
            Set<ConstraintViolation<?>> result = new HashSet<ConstraintViolation<?>>();
            for (ConstraintViolation<Object> problem : problems) {
                result.add(problem);
            }
            throw new ConstraintViolationException(result);
        }
    }

    private static final Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator();

    private static final MethodValidator methodValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory()
            .getValidator()
            .unwrap(MethodValidator.class);

    private Validator getClassValidator() {
        return validator;
    }

    private MethodValidator getMethodValidator() {
        return methodValidator;
    }
}
