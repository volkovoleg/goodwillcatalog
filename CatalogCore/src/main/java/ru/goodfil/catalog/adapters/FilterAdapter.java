package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Recordation;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.utils.*;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterAdapter.java 179 2014-07-18 12:31:54Z chezxxx@gmail.com $
 */
@Managed
public class FilterAdapter extends TableAdapter<Filter> {
    @Inject
    public FilterAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Filter.class, sessionProvider, validationProvider);
    }

    public List<Filter> getByIds(@NotNull final Set<Long> ids) {
        return getByCriteria(Restrictions.in("id", ids),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    public List<Filter> getByName(@NotNull @NotBlank final String name) {
        String maskName = SearchMask.mask(name);
        return getByCriteria(Restrictions.like("searchName", maskName, MatchMode.START),
                Restrictions.ne("disabled", Boolean.TRUE),
                Order.asc("name"));
    }

    @Override
    public List<Filter> getAll() {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Filter>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Filter.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .list();
            }
        }.execute();
    }

    @Override
    public List<Filter> getAllSorted(@NotNull @NotEmpty final String field) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Filter>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Filter.class)
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .addOrder(Order.asc(field))
                        .list();
            }
        }.execute();
    }

    public void softDelete(@NotNull final Long filterId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                session.save(Recordation.create(getById(filterId).toString()));
                session.createQuery("update Filter f set f.disabled = true where f.id = ?")
                        .setLong(0, filterId)
                        .executeUpdate();
                return null;
            }
        }.execute();
    }

    public List<Filter> getFiltersByFilterFormId(@NotNull final Long filterFormId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        return (List<Filter>) new HibernateTemplateWithoutTransaction(session) {
            @Override
            protected Object run(Session session) {
                return session.createCriteria(Filter.class)
                        .add(Restrictions.eq("filterFormId", filterFormId))
                        .add(Restrictions.ne("disabled", Boolean.TRUE))
                        .list();
            }
        }.execute();
    }

    public List<Filter> getFiltersByGlobalVechicleTypeId(@NotNull final Long selectedVechicleType) {
        if (selectedVechicleType.equals(VechicleType.CARS)) {
            return getByCriteria(Restrictions.eq("applyToAll_VT1", Boolean.TRUE),
                    Restrictions.ne("disabled", Boolean.TRUE),
                    Order.asc("name"));
        }

        if (selectedVechicleType.equals(VechicleType.TRUCKS)) {
            return getByCriteria(Restrictions.eq("applyToAll_VT2", Boolean.TRUE),
                    Restrictions.ne("disabled", Boolean.TRUE),
                    Order.asc("name"));
        }

        if (selectedVechicleType.equals(VechicleType.SPECIAL)) {
            return getByCriteria(Restrictions.eq("applyToAll_VT3", Boolean.TRUE),
                    Restrictions.ne("disabled", Boolean.TRUE),
                    Order.asc("name"));
        }

        if (selectedVechicleType.equals(VechicleType.BIKES)) {
            return getByCriteria(Restrictions.eq("applyToAll_VT4", Boolean.TRUE),
                    Restrictions.ne("disabled", Boolean.TRUE),
                    Order.asc("name"));
        }

        return new ArrayList<Filter>();
    }

    public List<String> getAllGParams(){
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        Object result =
                new HibernateTemplateWithoutTransaction(session) {
                    @Override
                    protected Object run(Session session) {
                        org.hibernate.Query q = session.createQuery("select distinct gParam  from Filter f where gParam!=:empty order by gParam")
                        .setParameter("empty", "''");
                        return q.list();
                    }
                }.execute();

        return (List<String>) result;
    }
}
