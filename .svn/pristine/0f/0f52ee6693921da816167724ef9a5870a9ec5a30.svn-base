package ru.goodfil.catalog.mann.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.mann.domain.MannTables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
@Lazy(true)
public class OeApplianceLoad extends PackageWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(OeApplianceLoad.class);
    
    @Value("${oe_appliance.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.FILTER_APPLIANCE;
    }

    @Override
    protected String getQueryName() {
        return "select_oe_appliance";
    }

    private final Map<String, Long> oesMap = new HashMap<String, Long>();
    
    private Long id = 0L;
    private Long missedOesCount = 0L;
    private Long oesWithoutFilters = 0L;
    
    @Override
    protected void preprocess() {
        List<Oe> oes = goodwillTemplate.find("from Oe");
        for (Oe oe : oes) {
            oesMap.put(oe.getSearchName(), oe.getId());
        }

        logger.debug("oes map size: " + oesMap.size());

        this.id = 0L;
        this.missedOesCount = 0L;
        this.oesWithoutFilters = 0L;
    }       
    
    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        String oe = str(row.get("OE"));
        Long oeId = oesMap.get(oe);
        
        Long filterId = id(row.get("FILTERID"));
        if (filterId == null || filterId == 0) {
            // Skipping oe applicance without filter
            oesWithoutFilters++;
            return new EntityAndAssociation(null, null);
        }

        if (oeId == null || oeId == 0) {
            // Skipping oe applicance wihtout oe
            logger.warn("Cannot find oe: " + oe);
            missedOesCount++;
            return new EntityAndAssociation(null, null);
        }

        FiltersAndOes fao = new FiltersAndOes();
        fao.setId(this.id);
        fao.setFilterId(filterId);
        fao.setOeId(oeId);

        this.id++;
        return new EntityAndAssociation(fao, null);
    }

    @Override
    protected void postprocess() {
        logger.debug("Missed " + missedOesCount + " oes");
        logger.debug("OE applicance without filters: " + oesWithoutFilters);
    }
}
