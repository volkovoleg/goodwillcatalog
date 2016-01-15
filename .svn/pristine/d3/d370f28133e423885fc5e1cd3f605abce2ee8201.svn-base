package ru.goodfil.catalog.mann.services;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.ManufactorAssociation;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
public abstract class PackageWorkUnit extends BaseWorkUnit {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String batchSize = "100";

    public abstract void setBatchSize(String batchSize);
    protected abstract String getTableName();
    protected abstract String getQueryName();
    protected abstract EntityAndAssociation processRow(Map<String, Object> row);
    protected void preprocess() {}
    protected void postprocess() {}
    
    public final void process() {
        preprocess();
        
        logger.debug("Processing as package");
        logger.debug("Batch size: " + batchSize);

        long max = countAll(getTableName());
        logger.debug("Rows to process: " + max);

        Long batchSize = new Long(this.batchSize);
        for (long i = 0; i < max; i += batchSize) {
            long cnt = (i + batchSize < max) ? batchSize : max - i;
            logger.debug("Processing {}...{}", i , i + cnt);


            final String query = getQuery(getQueryName());
            List<Map<String, Object>> l = mannTemplate.queryForList(query, i, i + cnt);
            
//            List<Object> entities = new ArrayList<Object>(l.size());
//            List<Object> associations = new ArrayList<Object>(l.size());
            for (Map<String, Object> row : l) {
                EntityAndAssociation eaa = processRow(row);

                if (eaa.entity != null) goodwillTemplate.save(eaa.entity);
                if (eaa.association != null) deltaTamplate.save(eaa.association);
//                entities.add(eaa.entity);
//                associations.add(eaa.association);
            }
//            goodwillTemplate.saveOrUpdateAll(entities);
//            deltaTamplate.saveOrUpdateAll(associations);
//            entities = null;
//            associations = null;
        }

        postprocess();
    }
    
    public static class EntityAndAssociation {
        private final Object entity;
        private final Object association;

        public EntityAndAssociation(Object entity, Object association) {
            this.entity = entity;
            this.association = association;
        }
    }
}
