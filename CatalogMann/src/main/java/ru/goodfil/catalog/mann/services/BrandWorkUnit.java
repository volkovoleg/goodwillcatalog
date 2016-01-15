package ru.goodfil.catalog.mann.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.mann.domain.BrandAssociation;
import ru.goodfil.catalog.mann.domain.MannTables;

import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 25.11.12
 */
@Component
public class BrandWorkUnit extends PackageWorkUnit {
    @Value("${brand.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.OES;
    }

    @Override
    protected long countAll(String table) {
        final String countQuery = getQuery("count_brands");
        return mannTemplate.queryForLong(countQuery);
    }

    @Override
    protected String getQueryName() {
        return "select_brand";
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Brand brand = new Brand();
        brand.setId(id(row.get("ID")));
        brand.setName(str(row.get("NAME")));

        BrandAssociation association = new BrandAssociation();
        association.setGoodwillId(id(row.get("ID")));
        association.setMitnr(id(row.get("MITNR")));

        return new EntityAndAssociation(brand, association);
    }
}
