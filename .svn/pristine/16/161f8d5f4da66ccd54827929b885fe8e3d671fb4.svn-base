package ru.goodfil.catalog.mann.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.mann.domain.FilterAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;

import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
public class FilterApplianceUnit extends PackageWorkUnit {
    @Value("${filter_appliance.batch_size}")
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
        return "select_filter_appliance";
    }

    private Long id = 0L;
    
    @Override
    protected void preprocess() {
        this.id = 0L;
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        FiltersAndMotors fam = new FiltersAndMotors();
        fam.setId(id);
        fam.setFilterId(id(row.get("FILTERID")));
        fam.setMotorId(id(row.get("MOTORID")));

        id++;
        return new EntityAndAssociation(fam, null);
    }
}
