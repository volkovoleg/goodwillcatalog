package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.domain.Recordation;
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
 * @version $Id: MotorAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class MotorAdapter extends TableAdapter<Motor> {
    @Inject
    public MotorAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Motor.class, sessionProvider, validationProvider);
    }

    public List<Motor> getBySeriaId(@NotNull Long seriaId) {
        return getByCriteria(Restrictions.eq("seriaId", seriaId),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    public List<Motor> getByIds(@NotNull Set<Long> ids) {
        return getByCriteria(Restrictions.in("id", ids),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    @Override
    public List<Motor> getAll() {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Motor>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Motor.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .list();
            }
        }.execute();
    }

    @Override
    public List<Motor> getAllSorted(@NotNull @NotEmpty final String field) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Motor>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Motor.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .addOrder(Order.asc(field))
                        .list();
            }
        }.execute();
    }

    public void softDelete(@NotNull final Long motorId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.save(Recordation.create(getById(motorId).toString()));
                session.createQuery("update Motor m set m.disabled = true where m.id = ?")
                        .setLong(0, motorId)
                        .executeUpdate();
                return null;
            }
        }.execute();
    }

    public List<Motor> getBySeriaIds(final @NotNull Set<Long> seriaIds) {
        return getByCriteria(Restrictions.in("seriaId", seriaIds),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }
}
