package ru.goodfil.catalog.mann.load;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * User: sazonovkirill@gmail.com
 * Date: 16.12.12
 */
@Component
@Lazy(true)
public class InfoWorkUnit extends LoadWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(InfoWorkUnit.class);
    
    private final String[] queries = new String[] {
            "select count(*) from SERIA s where s.MANUFACTORID not in (select m.ID from MANUFACTOR m)",
            "select count(*) from MOTOR m where m.SERIAID not in (select s.ID from SERIA s)",
            "select count(*) from FILTERFORM ff where ff.FILTERTYPECODE not in (select ft.CODE from FILTERTYPE ft)",
            "select count(*) from FILTER f where f.FILTERFORMID not in (select ff.ID from FILTERFORM ff)",
            "select count(*) from FILTER f where f.FILTERTYPECODE not in (select ft.CODE from FILTERTYPE ft)",
            "select count(*) from FILTERSANDMOTORS fam where fam.FILTERID not in (select f.ID from FILTER f)",
            "select count(*) from FILTERSANDMOTORS fam where fam.MOTORID not in (select m.ID from MOTOR m)",
            "select count(*) from FILTERSANDOES foe where foe.FILTERID not in (select f.ID from FILTER f)",
            "select count(*) from FILTERSANDOES foe where foe.OEID not in (select OE.ID from OE oe)",
            "select count(*) from OE oe where oe.BRANDID not in (select b.ID from BRAND b)"
    };

    public void show() {
        for (final String query : queries) {
            Object o = goodwillTemplate.executeWithNativeSession(new HibernateCallback<Object>() {
                @Override
                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    return session.createSQLQuery(query).uniqueResult();
                }
            });
            
            Long l = new Long(o.toString());
            logger.debug(query + " : " + l);
            System.out.println(query + " : " + l);
        }
    }
}
