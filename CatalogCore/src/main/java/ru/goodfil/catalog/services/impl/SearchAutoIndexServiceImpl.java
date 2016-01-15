package ru.goodfil.catalog.services.impl;

import org.hibernate.Session;
import ru.goodfil.catalog.services.SearchAutoIndexService;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.SessionProvider;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SearchAutoIndexServiceImpl.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class SearchAutoIndexServiceImpl implements SearchAutoIndexService {
    private SessionProvider sessionProvider;

    private static void executeQuery(final Session session, final String query) {
        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createSQLQuery(query);
                q.executeUpdate();
                return null;
            }
        }.execute();
    }

    @Override
    public void build() {
        final Session session = sessionProvider.getSession();

        final String MOTORS_QUERY = "" +
                "update Motor m set m.onSite = true where m.id in (\n" +
                "select m.Id from \n" +
                "Motor m\n" +
                "left outer join FiltersAndMotors fam on fam.motorId = m.id\n" +
                "left outer join Filter f on f.id = fam.filterId\n" +
                "where f.onSite = true)";
        executeQuery(session, MOTORS_QUERY);

        final String SERIA_QUERY = "" +
                "update Seria s set s.onSite = true where s.id in (\n" +
                "\tselect distinct(m.seriaId) from Motor m where m.onSite = true)";
        executeQuery(session, SERIA_QUERY);

        final String MANUFACTOR_QUERY = "" +
                "update Manufactor m set m.onSite = true where m.id in (\n" +
                "\tselect distinct(s.manufactorId) from Seria s where s.onSite = true)";
        executeQuery(session, MANUFACTOR_QUERY);

        final String VECHICLE_TYPE_QUERY = "" +
                "update VechicleType vt set vt.onSite = true where vt.id in (\n" +
                "\tselect distinct(m.vechicleTypeId) from Manufactor m where m.onSite = true)";
        executeQuery(session, VECHICLE_TYPE_QUERY);
    }

    public SessionProvider getSessionProvider() {
        return sessionProvider;
    }

    public void setSessionProvider(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }
}
