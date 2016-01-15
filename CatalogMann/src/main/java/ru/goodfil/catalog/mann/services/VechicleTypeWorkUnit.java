package ru.goodfil.catalog.mann.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.dict.VechicleType;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
public class VechicleTypeWorkUnit extends BaseWorkUnit {
    private static final Logger logger = LoggerFactory.getLogger(VechicleTypeWorkUnit.class);

    public void load() {
        logger.debug("Loading 4 standart vechicle types");

        goodwillTemplate.save(VechicleType.create(1L, "Легковые автомобили", true));
        goodwillTemplate.save(VechicleType.create(2L, "Грузовые автомашины и пассажирские автобусы", true));
        goodwillTemplate.save(VechicleType.create(3L, "Специальная техника и двигатели", true));
        goodwillTemplate.save(VechicleType.create(4L, "Мотоциклы и катера", true));

        logger.debug("Vechicle types loaded");
    }
}
