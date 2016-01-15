package ru.goodfil.catalog.mann.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.mann.domain.FilterAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.ManufactorAssociation;

import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
public class FilterWorkUnit extends PackageWorkUnit {
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

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Filter filter = new Filter();
        filter.setId(id(row.get("ID")));
        filter.setApplyToAll_VT1(Boolean.FALSE);
        filter.setApplyToAll_VT2(Boolean.FALSE);
        filter.setApplyToAll_VT3(Boolean.FALSE);
        filter.setApplyToAll_VT4(Boolean.FALSE);
        filter.setFilterFormId(id(row.get("FILTERFPRMID")));
        filter.setFilterTypeCode(str(row.get("FILTERTYPECODE")));
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
}
