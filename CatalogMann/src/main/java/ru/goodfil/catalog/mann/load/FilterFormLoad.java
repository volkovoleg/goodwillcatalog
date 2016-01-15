package ru.goodfil.catalog.mann.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.mann.domain.FilterFormAssociation;

import java.util.List;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 25.11.12
 */
@Component
@Lazy(true)
public class FilterFormLoad extends LoadWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(FilterFormLoad.class);
    
    public final void process() {
        logger.debug("Processing..");
        final String query = getQuery("select_filterform");
        List<Map<String, Object>> l = mannTemplate.queryForList(query);

        long id = 0;
        for (Map<String, Object> row : l) {
            FilterForm filterForm = new FilterForm();
            filterForm.setId(id(row.get("ID")));
            filterForm.setFilterTypeCode(str(row.get("FILTERTYPECODE")));
            filterForm.setName(str(row.get("NAME")));
            filterForm.setaParam(bool(row.get("APARAM")));
            filterForm.setbParam(bool(row.get("BPARAM")));
            filterForm.setcParam(bool(row.get("CPARAM")));
            filterForm.setdParam(bool(row.get("DPARAM")));
            filterForm.seteParam(bool(row.get("EPARAM")));
            filterForm.setfParam(bool(row.get("FPARAM")));
            filterForm.setgParam(bool(row.get("GPARAM")));
            filterForm.sethParam(bool(row.get("HPARAM")));
            filterForm.setBpParam(bool(row.get("BPPARAM")));
            filterForm.setNrParam(bool(row.get("NRPARAM")));

            FilterFormAssociation association = new FilterFormAssociation();
            association.setGoodwillId(id);
            association.setGrouppe(id(row.get("ID")));
            association.setPdartg(str(row.get("FILTERTYPECODE")));

            id++;
            goodwillTemplate.save(filterForm);
            deltaTamplate.save(association);
        }
    }
}
