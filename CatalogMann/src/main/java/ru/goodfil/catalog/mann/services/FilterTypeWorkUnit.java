package ru.goodfil.catalog.mann.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.mann.domain.BrandAssociation;
import ru.goodfil.catalog.mann.domain.FilterTypeAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;

import java.util.List;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 25.11.12
 */
@Component
public class FilterTypeWorkUnit extends BaseWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(FilterTypeWorkUnit.class);
    
    public final void process() {
        logger.debug("Processing..");
        final String query = getQuery("select_filtertype");
        List<Map<String, Object>> l = mannTemplate.queryForList(query);

        long id = 0;
        for (Map<String, Object> row : l) {
            FilterType filterType = new FilterType();
            filterType.setId(id);
            filterType.setCode(str(row.get("CODE")));
            filterType.setName(str(row.get("NAME")));

            FilterTypeAssociation association = new FilterTypeAssociation();
            association.setGoodwillId(id);
            association.setPdart(str(row.get("CODE")));

            id++;
            goodwillTemplate.save(filterType);
            deltaTamplate.save(association);
        }
    }
}
