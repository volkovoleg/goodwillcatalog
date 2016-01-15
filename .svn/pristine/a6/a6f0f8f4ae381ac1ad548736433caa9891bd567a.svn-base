package ru.goodfil.catalog.validation.core;

import javax.validation.*;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: HibernateValidationProvider.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public class HibernateValidationProvider implements ValidationProvider {
    private final ValidatorFactory factory;
    private final Validator validator;

    public HibernateValidationProvider() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public <T> void validate(T object) {
        Set<ConstraintViolation<T>> problems = validator.validate(object);

        if (problems.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> problem : problems) {
                sb.append(problem.getPropertyPath() + " " + problem.getMessage() + "\n");
            }

            throw new ValidationException(sb.toString());
        }
    }
}
