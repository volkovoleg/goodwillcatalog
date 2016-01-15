package ru.goodfil.catalog.utils;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: HibernateTemplate.java 176 2014-07-08 10:00:08Z chezxxx@gmail.com $
 */
public abstract class HibernateTemplate {
    private final Session session;

    protected HibernateTemplate(@NotNull Session session) {
        Assert.notNull(session);

        this.session = session;
    }

    protected abstract Object run(Session session);

    public Object execute() {
        Object result = null;
        Transaction tx = session.beginTransaction();
        try {
            result = run(session);
         tx.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
           if (!tx.wasCommitted()) {
                tx.rollback();
            }
        }

        return result;
    }
}
