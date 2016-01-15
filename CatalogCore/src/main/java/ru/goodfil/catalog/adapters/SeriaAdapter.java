package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.domain.Recordation;
import ru.goodfil.catalog.domain.Seria;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.HibernateTemplateWithoutTransaction;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SeriaAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class SeriaAdapter extends TableAdapter<Seria> {
    @Inject
    public SeriaAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Seria.class, sessionProvider, validationProvider);
    }

    public List<Seria> getByManufatorId(@NotNull final Long manufactorId) {
        return getByCriteria(Restrictions.eq("manufactorId", manufactorId),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    @Override
    public List<Seria> getAll() {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Seria>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Seria.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .list();
            }
        }.execute();
    }

    @Override
    public List<Seria> getAllSorted(@NotNull @NotEmpty final String field) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Seria>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Seria.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .addOrder(Order.asc(field))
                        .list();
            }
        }.execute();
    }

    public void softDelete(@NotNull final Long seriaId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.save(Recordation.create(getById(seriaId).toString()));
                session.createQuery("update Seria s set s.disabled = true where s.id = ?")
                        .setLong(0, seriaId)
                        .executeUpdate();
                return null;
            }
        }.execute();
    }

    /**
     * Возвращает серии по указанным идентификаторам.
     * @param seriesIds идентификаторы серий
     * @return серии по указанным идентификаторам.
     */
    public List<Seria> getByIds(@NotNull @NotEmpty Set<Long> seriesIds) {
        return getByCriteria(Restrictions.in("id", seriesIds),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    /**
     * Возвращает серии по идентификаторам производителей.
     * @param manufactorIds идентификаторы производителей.
     * @return серии по идентификаторам производителей.
     */
    public List<Seria> getByManufatorIds(@NotNull @NotEmpty Set<Long> manufactorIds) {
        return getByCriteria(Restrictions.in("manufactorId", manufactorIds),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }
}
