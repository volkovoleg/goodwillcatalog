package ru.goodfil.catalog.mann.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.mann.domain.MannTables;
import ru.goodfil.catalog.mann.domain.MotorAssociation;

import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
@Lazy(true)
public class MotorLoad extends PackageWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(MotorLoad.class);

    @Value("${motor.batch_size}")
    @Override
    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected String getTableName() {
        return MannTables.MOTORS;
    }

    @Override
    protected String getQueryName() {
        return "select_motor";
    }

    @Override
    protected EntityAndAssociation processRow(Map<String, Object> row) {
        Motor motor = new Motor();
        motor.setId(id(row.get("ID")));
        motor.setSeriaId(id(row.get("SERIAID")));
        motor.setDateF(date(row.get("DATEF")));
        motor.setDateT(date(row.get("DATET")));
        motor.setEngine(str(row.get("ENGINE")));
        motor.setKw(str(row.get("KW")));
        motor.setHp(str(row.get("HP")));
        motor.setName(str(row.get("NAME")));
        motor.setDisabled(false);
        motor.setOnSite(true);

        MotorAssociation association = new MotorAssociation();
        association.setGoodwillId(motor.getId());
        association.setFzasl(id(row.get("FZASL")));
        association.setMdrsl(id(row.get("MDRSL")));
        association.setModsl(id(row.get("MODSL")));
        association.setMrksl(id(row.get("MRKSL")));

        return new EntityAndAssociation(motor, association);
    }
}

