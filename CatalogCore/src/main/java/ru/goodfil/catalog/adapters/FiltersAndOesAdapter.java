package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.HibernateTemplateWithoutTransaction;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersAndOesAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class FiltersAndOesAdapter extends TableAdapter<FiltersAndOes> {
    @Inject
    public FiltersAndOesAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(FiltersAndOes.class, sessionProvider, validationProvider);
    }

    public List<FiltersAndOes> getByFilterId(@NotNull final Long filterId) {
        return getByCriteria(Restrictions.eq("filterId", filterId));
    }

    public List<FiltersAndOes> getByOeId(@NotNull final Long oeId) {
        return getByCriteria(Restrictions.eq("oeId", oeId));
    }

    /**
     * Удалить все связи указанного ое с фильтрами.
     *
     * @param oeId идентификатор ое.
     */
    public void deleteByOeId(@NotNull final Long oeId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FiltersAndOes o where o.oeId = :oeId");
                q.setLong("oeId", oeId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }

    /**
     * Удалить все связи указанных в коллекии ое с фильтрами.
     *
     * @param ids идентификатор ое.
     */
    public void deleteByOeIds(@NotNull final Set<Long> ids) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                Query q = session.createQuery("delete from Oe o where o.id in (:ids)");
                q.setParameterList("ids", ids);
                q.executeUpdate();
                return null;
            }
        }.execute();

    }

    /**
     * Удалить все связи указанного фильтра с ое.
     *
     * @param filterId идентификатор фильтра.
     */
    public void deleteByFilterId(@NotNull final Long filterId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FiltersAndOes o where o.filterId = :filterId");
                q.setLong("filterId", filterId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }

    /**
     * Возвращает количество привязок фильтров к ое с указанным идентиифкатором фильтра и идентификатором ое.
     * Вообще говоря, таких привязок должно быть либо 0 л ибо 1, но кто ж его знает.
     *
     * @param filterId идентификатор фильтра.
     * @param oeId     идентификатор ое.
     * @return количество привязок фильтров к ое с указанным идентиифкатором фильтра и идентификатором ое.
     */
    public int getCountByFilterIdAndOeId(@NotNull final Long filterId, @NotNull final Long oeId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        Object result =
                new HibernateTemplateWithoutTransaction(session) {
                    @Override
                    protected Object run(Session session) {
                        org.hibernate.Query q = session.createQuery("select count(*) from FiltersAndOes o where o.filterId = :filterId and o.oeId = :oeId");
                        q.setLong("filterId", filterId);
                        q.setLong("oeId", oeId);
                        return q.uniqueResult();
                    }
                }.execute();

        return new Integer(result.toString());
    }

    /**
     * Возвращает количество привязок к указанному фильтру, у которых идентификатор ое входит в указанный кортеж.
     *
     * @param filterId идентификатор фильтра.
     * @param oesIds   кортеж идентификаторов ое.
     * @return количество привязок к указанному фильтру, у которых идентификатор ое входит в указанный кортеж.
     */
    public int getCountByFilterIdAndOesIds(@NotNull final Long filterId, @NotNull final Set<Long> oesIds) {
        if (oesIds.size() == 0) {
            return 0;
        }

        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        Object result =
                new HibernateTemplateWithoutTransaction(session) {
                    @Override
                    protected Object run(Session session) {
                        org.hibernate.Query q = session.createQuery("select count(*) from FiltersAndOes o where o.filterId = :filterId and o.oeId in (:oesIds)");
                        q.setLong("filterId", filterId);
                        q.setParameterList("oesIds", oesIds);
                        return q.uniqueResult();
                    }
                }.execute();

        return new Integer(result.toString());
    }

    public List<FiltersAndOes> getByOeIds(final Set<Long> oesIds) {
        if (oesIds.size() == 0) {
            return new ArrayList<FiltersAndOes>();
        }

        Session session = sessionProvider.getSession();
        Assert.notNull(session);
        Object result =
                new HibernateTemplateWithoutTransaction(session) {
                    @Override
                    protected Object run(Session session) {
                        org.hibernate.Query q = session.createQuery("select o from FiltersAndOes o where o.oeId in (:oesIds)");
                        q.setParameterList("oesIds", oesIds);
                        return q.list();
                    }
                }.execute();

        return (List<FiltersAndOes>) result;
    }
}
