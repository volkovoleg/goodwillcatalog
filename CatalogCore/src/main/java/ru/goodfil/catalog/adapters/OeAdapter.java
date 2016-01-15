package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.domain.Recordation;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.SearchMask;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: OeAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class OeAdapter extends TableAdapter<Oe> {
    @Inject
    public OeAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Oe.class, sessionProvider, validationProvider);
    }

    public List<Oe> getByBrandId(@NotNull final Long brandId) {
        return getByCriteria(Restrictions.eq("brandId", brandId), Order.asc("name"));
    }

    public List<Oe> searchByName(@NotNull @NotEmpty final String name) {
        return getByCriteria(Restrictions.like("searchName", SearchMask.mask(name), MatchMode.START),
                Order.asc("name"));
    }

    public List<Oe> getByIds(@NotNull final Set<Long> ids) {
        return getByCriteria(Restrictions.in("id", ids), Order.asc("name"));
    }

    public List<Oe> searchByBrandIdAndName(@NotNull final Long brandId, @NotNull @NotBlank final String name) {
        return getByCriteria(Restrictions.eq("brandId", brandId),
                Restrictions.like("searchName", SearchMask.mask(name), MatchMode.START),
                Order.asc("name"));
    }

    public List<Oe> getByBrandIdAndName(@NotNull final Long brandId, @NotNull @NotBlank final String name) {
        return getByCriteria(Restrictions.eq("brandId", brandId),
                Restrictions.eq("name", name),
                Order.asc("name"));
    }

    public List<Oe> getByName(@NotNull @NotBlank final String name) {
        return getByCriteria(Restrictions.eq("name", name), Order.asc("name"));
    }

    public void deleteByIds(@NotNull final Set<Long> ids) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                for(long i : ids){
                    System.out.println("id=" + getById(i).getId()+" "+"name="+ getById(i).getName());
                }

                Query q = session.createQuery("delete from Oe o where o.id in (:ids)");
                q.setParameterList("ids", ids);
                q.executeUpdate();
                return null;
            }
        }.execute();

    }
}
