package ru.goodfil.catalog.mann.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.Seria;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.ManufactorAssociation;
import ru.goodfil.catalog.mann.domain.SeriaAssociation;

import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
public class SeriaWorkUnit extends PackageWorkUnit {
    @Value("${seria.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.SERIES;
    }

    @Override
    protected String getQueryName() {
        return "select_seria";
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Seria seria = new Seria();
        seria.setId(id(row.get("ID")));
        seria.setDisabled(false);
        seria.setName(str(row.get("NAME")));
        seria.setManufactorId(id(row.get("MANUFACTORID")));
        seria.setOnSite(true);

        // In this case MANN id is equal to Goodwill id
        SeriaAssociation association = new SeriaAssociation();
        association.setGoodwillId(seria.getId());
        association.setFzasl(id(row.get("FZASL")));
        association.setMrksl(id(row.get("FZASL")));
        association.setMdrsl(id(row.get("MDRSL")));

        return new EntityAndAssociation(seria, association);
    }
}
