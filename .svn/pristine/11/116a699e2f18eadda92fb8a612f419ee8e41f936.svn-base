package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.Recordation;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;
import ru.goodfil.catalog.domain.FilterForm;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterFormAdapter.java 150 2013-06-19 10:02:19Z chezxxx@gmail.com $
 */
@Managed
public class FilterFormAdapter extends TableAdapter<FilterForm> {
    @Inject
    public FilterFormAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(FilterForm.class, sessionProvider, validationProvider);
    }

    public List<FilterForm> getByFilterTypeCode(@NotNull final String filterTypeCode) {
        return getByCriteria(Restrictions.eq("filterTypeCode", filterTypeCode));
    }

    public void deleteFilterFormById(@NotNull final Long filterFormId) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FilterForm o where o.id = :filterFormId");
                q.setLong("filterFormId", filterFormId);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }
}
