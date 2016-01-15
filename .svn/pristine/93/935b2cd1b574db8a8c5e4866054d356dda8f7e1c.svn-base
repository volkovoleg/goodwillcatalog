package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.HibernateTemplateWithoutTransaction;
import ru.goodfil.catalog.utils.SessionProvider;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NativeSQLAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class NativeSQLAdapter {
    @Inject
    protected SessionProvider sessionProvider;

    public int execute(@NotNull @NotBlank final String query) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        int result = (Integer)
                new HibernateTemplate(session) {
                    @Override
                    protected Object run(Session session) {
                        SQLQuery sqlQuery = session.createSQLQuery(query);
                        return sqlQuery.executeUpdate();
                    }
                }.execute();

        return result;
    }

    public List<Map<String, Object>> select(@NotNull @NotBlank final String query) {
        Session session = sessionProvider.getSession();
        Assert.notNull(session);

        List<Map<String, Object>> result = (List<Map<String, Object>>)
                new HibernateTemplateWithoutTransaction(session) {
                    @Override
                    protected Object run(Session session) {
                        SQLQuery sqlQuery = session.createSQLQuery(query);
                        sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                        return sqlQuery.list();
                    }
                }.execute();

        return result;
    }
}
