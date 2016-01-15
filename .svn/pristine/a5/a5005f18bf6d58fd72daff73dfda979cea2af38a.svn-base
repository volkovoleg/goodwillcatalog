package ru.goodfil.catalog.mann.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
public abstract class PackageWorkUnit extends LoadWorkUnit {
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
            
            for (Map<String, Object> row : l) {
                EntityAndAssociation eaa = processRow(row);

                if (eaa.entity != null) goodwillTemplate.save(eaa.entity);
                if (eaa.association != null) deltaTamplate.save(eaa.association);
            }
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
