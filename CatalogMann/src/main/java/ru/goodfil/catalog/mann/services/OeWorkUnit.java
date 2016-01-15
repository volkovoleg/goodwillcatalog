package ru.goodfil.catalog.mann.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.mann.domain.BrandAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.OeAssociation;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 25.11.12
 */
@Component
public class OeWorkUnit extends PackageWorkUnit {
    @Value("${oe.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.OES;
    }

    @Override
    protected String getQueryName() {
        return "select_oe";
    }

    private long lastId = 0;

    @PostConstruct
    public void init() {
        lastId = 0;
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Oe oe = new Oe();
        oe.setBrandId(id(row.get("BRANDID")));
        oe.setName(str(row.get("NAME")));
        oe.setSearchName(str(row.get("SEARCHNAME")));
        oe.setId(lastId);

        OeAssociation association = new OeAssociation();
        association.setGoodwillId(lastId);
        association.setLfdnr(id(row.get("LFDNR")));
        association.setMitkz(id(row.get("MITKZ")));
        association.setMpdsl(id(row.get("MPDSL")));
        
        lastId++;
        return new EntityAndAssociation(oe, association);
    }
}
