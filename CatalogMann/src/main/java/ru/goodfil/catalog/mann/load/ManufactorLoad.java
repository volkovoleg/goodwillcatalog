package ru.goodfil.catalog.mann.load;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.ManufactorAssociation;

import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
@Lazy(true)
public class ManufactorLoad extends PackageWorkUnit {
    @Value("${manufactor.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.MANUFACTORS;
    }

    @Override
    protected String getQueryName() {
        return "select_manufactor";
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Manufactor manufactor = new Manufactor();
        manufactor.setId(id(row.get("ID")));
        manufactor.setDisabled(false);
        manufactor.setName(str(row.get("NAME")));
        manufactor.setOnSite(true);
        manufactor.setVechicleTypeId(id(row.get("VECHICLETYPEID")));

        // In this case MANN id is equal to Goodwill id
        ManufactorAssociation association = new ManufactorAssociation();
        association.setGoodwillId(manufactor.getId());
        association.setFzasl(id(row.get("FZASL")));
        association.setMrksl(id(row.get("MRKSL")));

        return new EntityAndAssociation(manufactor, association);
    }
}
