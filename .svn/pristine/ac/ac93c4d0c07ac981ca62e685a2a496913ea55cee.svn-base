package ru.goodfil.catalog.utils.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.hibernate.Session;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.SessionFactoryHolder;
import ru.goodfil.catalog.utils.SessionFactoryReference;
import ru.goodfil.catalog.utils.SessionProvider;

import java.net.URL;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DefaultSessionProvider.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class DefaultSessionProvider implements SessionProvider {
    private Session session;
    private SessionFactoryReference sessionFactory;

    @Inject
    @Named(value = "configFile")
    private URL configFile;

    @Override
    public Session getSession() {
        if (sessionFactory == null) {
            init();
        }

        return session;
    }

    @Override
    public void close() {
        try {
            if (session != null) {
                session.close();
            }

            if (sessionFactory != null) {
                sessionFactory.get().close();
            }
        } catch (Exception e) {
            // nothing
        }
    }

    @Override
    public void init() {
        Assert.notNull(configFile);

        SessionFactoryHolder.setConfigFile(configFile);
        sessionFactory = new SessionFactoryReference(SessionFactoryHolder.getSessionFactory());
        session = sessionFactory.get().openSession();
    }
}
