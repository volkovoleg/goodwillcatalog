package ru.goodfil.catalog.utils;

import org.hibernate.SessionFactory;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SessionFactoryReference.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class SessionFactoryReference {
    private final SessionFactory sessionFactory;

    public SessionFactoryReference(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory get() {
        return sessionFactory;
    }
}
