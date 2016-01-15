package ru.goodfil.catalog.gc;

import org.hibernate.Query;
import org.hibernate.Session;
import ru.goodfil.catalog.utils.HibernateTemplate;
import ru.goodfil.catalog.utils.SessionProvider;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Gc.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class Gc {
    private final SessionProvider sessionProvider;

    public Gc(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public static class IntHolder {
        private int value;

        public IntHolder(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void increment(int value) {
            this.value += value;
        }
    }

    public int collect() {

        final IntHolder result = new IntHolder(0);

        final Session session = sessionProvider.getSession();

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from Manufactor m where m.vechicleTypeId not in (select vt.id from VechicleType vt)");
                result.increment(q.executeUpdate());
                return null;
            }
        }.execute();

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from Seria s where s.manufactorId not in (select m.id from Manufactor m)");
                result.increment(q.executeUpdate());
                return null;
            }
        }.execute();

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from Motor m where m.seriaId not in (select s.id from Seria s)");
                result.increment(q.executeUpdate());
                return null;
            }
        }.execute();

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                org.hibernate.Query q = session.createQuery("delete from FiltersAndMotors a where a.filterId not in (select f.id from Filter f)");
                result.increment(q.executeUpdate());
                return null;
            }
        }.execute();

        new HibernateTemplate(session) {
            @Override
            protected Object run(Session session) {
                Query q = session.createSQLQuery("" +
                        "DELETE FROM FiltersAndMotors fam2 WHERE fam2.id IN (" +
                        "SELECT fam.id " +
                        "FROM " +
                        "   FiltersAndMotors fam " +
                        "   LEFT OUTER JOIN Motor m ON fam.motorId = m.id " +
                        "WHERE m.id is null)");
                result.increment(q.executeUpdate());
                return null;
            }
        }.execute();

        return result.getValue();
    }
}
