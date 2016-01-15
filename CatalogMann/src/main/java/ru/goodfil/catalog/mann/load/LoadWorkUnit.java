package ru.goodfil.catalog.mann.load;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 */
public abstract class LoadWorkUnit {
    @Autowired
    @Qualifier("goodwillTemplate")
    protected HibernateTemplate goodwillTemplate;

    @Autowired
    @Qualifier("deltaTemplate")
    protected HibernateTemplate deltaTamplate;

    @Autowired
    @Qualifier("mannTemplate")
    protected JdbcTemplate mannTemplate;

    protected long countAll(String table) {
        return mannTemplate.queryForLong("select count(*) from " + table);
    }

    /**
     * Returns a query from a sql file (from classpath)
     * @param queryName query (file) name
     * @return query
     */
    protected String getQuery(String queryName) {
        final String _package = "ru/goodwill/catalog/mann/queries/";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(_package + queryName + ".sql");
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected Date date(Object o) {
        if (o == null) return null;

        try {
            return sdf.parse(o.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected Long id(Object o) {
        if (o == null) return null;
        else return new Long(o.toString());
    }
    
    protected String str(Object o) {
        if (o == null) return null;
        else return o.toString();
    }
    
    protected Boolean bool(Object o) {
        if (o == null) return null;              
        if (o.toString().equals("1")) return Boolean.TRUE;
        else return Boolean.FALSE;
    }
}
