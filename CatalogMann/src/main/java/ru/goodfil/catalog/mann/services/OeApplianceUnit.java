package ru.goodfil.catalog.mann.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.FiltersAndMotors;
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
public class OeApplianceUnit extends PackageWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(OeApplianceUnit.class);
    
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
    private Long missedCount = 0L;
    
    @Override
    protected void preprocess() {
        List<Oe> oes = goodwillTemplate.find("from Oe");
        for (Oe oe : oes) {
            oesMap.put(oe.getSearchName(), oe.getId());
        }

        logger.debug("oes map size: " + oesMap.size());

        this.id = 0L;
        this.missedCount = 0L;
    }       
    
    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        String oe = str(row.get("OE"));
        Long oeId = oesMap.get(oe);
        
        if (oeId == null) {
            logger.warn("Cannot find oe: " + oe);
            missedCount++;
            return new EntityAndAssociation(null, null);
        } else {
            FiltersAndOes fao = new FiltersAndOes();
            fao.setId(this.id);
            fao.setFilterId(id(row.get("FILTERID")));
            fao.setOeId(oeId);

            this.id++;
            return new EntityAndAssociation(fao, null);
        }
    }

    @Override
    protected void postprocess() {
        logger.debug("Missed " + missedCount + " oes");
    }
}
