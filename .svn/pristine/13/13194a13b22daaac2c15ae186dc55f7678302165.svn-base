package ru.goodfil.catalog.exceptions;

import ru.goodfil.catalog.utils.SessionFactoryHolder;

/**
 * Exceptions occurs, when an application cannot get access to the database.
 * @author sazonovkirill@gmail.com
 * @version $Id: DatabaseAccessException.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class DatabaseAccessException extends GoodwillCatalogException {
    private String configFile;
    private String sessionFactory;

    public DatabaseAccessException(Throwable t) {
        super(t);

        try {
            this.configFile = String.valueOf(SessionFactoryHolder.getConfigFile());
        } catch (Exception e) {
        }

        try {
            this.sessionFactory = String.valueOf(SessionFactoryHolder.getSessionFactoryAsString());
        } catch (Exception e) {
        }
    }

    public String getConfigFile() {
        return configFile;
    }

    public String getSessionFactory() {
        return sessionFactory;
    }
}
