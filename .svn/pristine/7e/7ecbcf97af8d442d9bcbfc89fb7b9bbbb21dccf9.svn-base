package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterTypeAdapter.java 150 2013-06-19 10:02:19Z chezxxx@gmail.com $
 */
public class FilterTypeAdapter extends TableAdapter<FilterType> {
    @Inject
    public FilterTypeAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(FilterType.class, sessionProvider, validationProvider);
    }

    public FilterType getByCode(String filterTypeCode) {
        List<FilterType> filterTypes = getByCriteria(Restrictions.eq("code", filterTypeCode));
        Assert.isTrue(filterTypes.size() < 2);

        if (filterTypes.size() == 0) {
            return null;
        } else {
            return filterTypes.get(0);
        }
    }

    public void deleteFilterTypeById(@NotNull final Long filterTypeId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FilterType o where o.id = :filterTypeId");
                q.setLong("filterTypeId", filterTypeId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }
}
