package ru.goodfil.catalog.exceptions;

/**
 * Base exception class for Goodwill Catalog Project.
 * @author sazonovkirill@gmail.com
 * @version $Id: GoodwillCatalogException.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class GoodwillCatalogException extends RuntimeException {
    public GoodwillCatalogException() {
    }

    public GoodwillCatalogException(String message) {
        super(message);
    }

    public GoodwillCatalogException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoodwillCatalogException(Throwable cause) {
        super(cause);
    }
}
