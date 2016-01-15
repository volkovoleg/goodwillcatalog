package ru.goodfil.catalog.utils;


import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.goodfil.catalog.exceptions.DatabaseAccessException;

import java.net.URL;

/**
 * Constructs SessionFactory in Singleton from config file.
 * @author sazonovkirill@gmail.com
 * @version $Id: SessionFactoryHolder.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class SessionFactoryHolder {
    private static final Logger logger = LoggerFactory.getLogger(SessionFactoryHolder.class);

    private static SessionFactory sessionFactory;
    private static org.hibernate.cfg.Configuration hibernateConfiguration;
    private static URL configFile;

    public static SessionFactory getSessionFactory() {
        Assert.notNull(configFile);

        if (sessionFactory == null) {
            SessionFactoryHolder.sessionFactory = _getSessionFactory(configFile);
            SessionFactoryHolder.configFile = configFile;
        } else {
            logger.error("There was an attempt to construct SessionFactory twice!!!!!");
        }

        return sessionFactory;
    }

    private static SessionFactory _getSessionFactory(URL configFile) {
        Assert.notNull(configFile);

        try {
            hibernateConfiguration = new org.hibernate.cfg.Configuration();
            hibernateConfiguration.configure(configFile);
            return hibernateConfiguration.buildSessionFactory();
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    public static URL getConfigFile() {
        return configFile;
    }

    public static void setConfigFile(URL configFile) {
        SessionFactoryHolder.configFile = configFile;
    }

    public static String getSessionFactoryAsString() {
        return String.valueOf(sessionFactory);
    }

    public static String getDatabasePathWithPrefix() {
        String userDirectory = FileUtils.getUserDirectoryPath();
        String connectionUrl = hibernateConfiguration.getProperty("hibernate.connection.url");
        String result = connectionUrl.replace("jdbc:h2:file:", "").replace("~", userDirectory) + ".h2.db";
        return result;
    }

    public static String getDatabasePathWithoutPrefix() {
        String userDirectory = FileUtils.getUserDirectoryPath();
        String connectionUrl = hibernateConfiguration.getProperty("hibernate.connection.url");
        String result = connectionUrl.replace("jdbc:h2:file:", "").replace("~", userDirectory);
        return result;
    }
}
