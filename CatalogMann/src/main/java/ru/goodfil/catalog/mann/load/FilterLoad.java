package ru.goodfil.catalog.mann.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.mann.domain.FilterAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;

import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
@Lazy(true)
public class FilterLoad extends PackageWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(FilterLoad.class);
    
    @Value("${filter.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.FILTERS;
    }

    @Override
    protected String getQueryName() {
        return "select_filter";
    }

    private Map<String, Long> randomFilterFormForFilterType = new HashMap<String, Long>();
    private Set<String> filterTypesWithoutRandomFilterForm = new HashSet<String>();
    
    private Logger logger_RandomFilterForm = LoggerFactory.getLogger(getClass() + ".RandomFilterForm");
    private Logger logger_FilterTypesWithoutRandomFilterForm = LoggerFactory.getLogger(getClass() + ".FilterTypesWithoutRandomFilterForm");
    private Logger logger_RandomFilterFormForFilter = LoggerFactory.getLogger(getClass() + ".RandomFilterFormForFilter");
    
    private Long filtersWithoutLegacyFilterForm = 0L;
    private Long filtersWithoutAnyFilterForm = 0L;
    
    private Logger logger_FiltersWithoutLegacyFilterForm = LoggerFactory.getLogger(getClass() + ".FiltersWithoutLegacyFilterForm");
    private Logger logger_FiltersWithoutAnyFilterForm = LoggerFactory.getLogger(getClass() + ".FiltersWithoutAnyFilterForm");
    
    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Long filterId = id(row.get("ID"));
        Long filterFormId = id(row.get("FILTERFORMID"));
        String filterTypeCode = str(row.get("FILTERTYPECODE"));
        
        if (filterFormId == null || filterFormId == 0) {
            if (!randomFilterFormForFilterType.containsKey(filterTypeCode)) {
                List l = goodwillTemplate.find("select ff.id from FilterForm ff where ff.filterTypeCode = ?", filterTypeCode);
                if (l != null && l.size() > 0) {
                    Long randomFilerForm = new Long(l.get(0).toString());
                    randomFilterFormForFilterType.put(filterTypeCode, randomFilerForm);

                    logger_RandomFilterForm.debug("For filtertype " + filterTypeCode + " random filterform is " + randomFilerForm);
                }
            }
            
            Long randomFilterForm = randomFilterFormForFilterType.get(filterTypeCode);
            if (randomFilterForm != null) {
                filtersWithoutLegacyFilterForm++;
                logger_FiltersWithoutLegacyFilterForm.debug(filterId.toString());
                
                filterFormId = randomFilterForm;
                logger_RandomFilterFormForFilter.debug("For filter " + filterId + " assigned random filter form: " + randomFilterForm);
            } else {
                if (!filterTypesWithoutRandomFilterForm.contains(filterTypeCode)) {
                    logger_FilterTypesWithoutRandomFilterForm.debug("We dont have random filterform for filtertype " + filterTypeCode);
                    filterTypesWithoutRandomFilterForm.add(filterTypeCode);
                }
            }
        }
        
        if (filterFormId == null || filterFormId == 0) {
            filtersWithoutAnyFilterForm++;
            logger_FiltersWithoutAnyFilterForm.debug(filterId.toString());
            return new EntityAndAssociation(null, null);
        }
        
        Filter filter = new Filter();
        filter.setId(filterId);
        filter.setApplyToAll_VT1(Boolean.FALSE);
        filter.setApplyToAll_VT2(Boolean.FALSE);
        filter.setApplyToAll_VT3(Boolean.FALSE);
        filter.setApplyToAll_VT4(Boolean.FALSE);
        filter.setFilterFormId(filterFormId);
        filter.setFilterTypeCode(filterTypeCode);
        filter.setaParam(str(row.get("APARAM")));
        filter.setbParam(str(row.get("BPARAM")));
        filter.setcParam(str(row.get("CPARAM")));
        filter.setdParam(str(row.get("DPARAM")));
        filter.seteParam(str(row.get("EPARAM")));
        filter.setfParam(str(row.get("FPARAM")));
        filter.setgParam(str(row.get("GPARAM")));
        filter.sethParam(str(row.get("HPARAM")));
        filter.setPbParam(str(row.get("PBPARAM")));
        filter.setNrParam(str(row.get("NRPARAM")));
        filter.setImage(null);
        filter.setEan(str(row.get("EAN")));
        filter.setName(str(row.get("NAME")));
        filter.setOnSite(Boolean.TRUE);
        filter.setSearchName(str(row.get("SEARCHNAME")));

        FilterAssociation association = new FilterAssociation();
        association.setGoodwillId(filter.getId());
        association.setMannId(filter.getId());

        return new EntityAndAssociation(filter, association);
    }

    @Override
    protected void postprocess() {
        logger.debug("Filters without legacy filterform: " + filtersWithoutLegacyFilterForm);
        logger.debug("Filters without any filterform: " + filtersWithoutAnyFilterForm);
    }
}
