package ru.goodfil.catalog.annotations.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Init.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {
}
