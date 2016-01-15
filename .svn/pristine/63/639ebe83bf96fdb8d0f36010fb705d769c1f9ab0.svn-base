package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Manufactor;
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
 * @version $Id: ManufactorAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class ManufactorAdapter extends TableAdapter<Manufactor> {
    @Inject
    public ManufactorAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Manufactor.class, sessionProvider, validationProvider);
    }

    public List<Manufactor> getByVechicleTypeId(@NotNull Long vechicleTypeId) {
        return getByCriteria(Restrictions.eq("vechicleTypeId", vechicleTypeId),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    @Override
    public List<Manufactor> getAll() {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Manufactor>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Manufactor.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .list();
            }
        }.execute();
    }

    @Override
    public List<Manufactor> getAllSorted(@NotNull @NotEmpty final String field) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Manufactor>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Manufactor.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .addOrder(Order.asc(field))
                        .list();
            }
        }.execute();
    }

    public void softDelete(@NotNull final Long manufactorId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.save(Recordation.create(getById(manufactorId).toString()));
                session.createQuery("update Manufactor m set m.disabled = true where m.id = ?")
                        .setLong(0, manufactorId)
                        .executeUpdate();
                return null;
            }
        }.execute();
    }

    /**
     * Возвращает производителей по идентификаторам.
     * @param manufactorsIds идентификаторы производителей.
     * @return производители по идентификаторам.
     */
    public List<Manufactor> getByIds(@NotNull @NotEmpty final Set<Long> manufactorsIds) {
        return getByCriteria(Restrictions.in("id", manufactorsIds),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }
}
