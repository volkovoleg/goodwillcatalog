package ru.goodfil.catalog.validation.core;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ValidationProvider.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public interface ValidationProvider {
    public <T> void validate(T o);
}
