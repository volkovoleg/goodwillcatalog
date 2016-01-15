package ru.goodfil.catalog.mann;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.mann.merge.*;

import javax.annotation.PostConstruct;

/**
 * User: sazonovkirill@gmail.com
 * Date: 01.12.12
 */
@Component
@Lazy(true)
public class MergeService {
    private static final Logger logger = LoggerFactory.getLogger(MergeService.class);
    
    private String baseDir;

    @Autowired
    @Qualifier("finalTemplate")
    protected HibernateTemplate finalTemplate;
    
    @Autowired
    private BrandsAndOesUnit brandsAndOes;

    @Autowired
    private CarsUnit carsUnit;

    @Autowired
    private FiltersUnit filtersUnit;

    @Autowired
    private FiltersAndMotorsApplianceUnit filtersAndMotorsApplianceUnit;

    @Autowired
    private FiltersAndOesApplianceUnit filtersAndOesApplianceUnit;

    @PostConstruct
    public void init() {
        this.baseDir = System.getProperty("base.dir");
    }

    public void run() {
        logger.debug("Loading 4 standart vechicle types");

        finalTemplate.save(VechicleType.create(1L, "Легковые автомобили", true));
        finalTemplate.save(VechicleType.create(2L, "Грузовые автомашины и пассажирские автобусы", true));
        finalTemplate.save(VechicleType.create(3L, "Специальная техника и двигатели", true));
        finalTemplate.save(VechicleType.create(4L, "Мотоциклы и катера", true));

        logger.debug("Vechicle types loaded");
        
        oes();
        filters();
        cars();
        appliance();
    }

    protected void cars() {
        carsUnit.process();
    }

    protected void oes() {
        brandsAndOes.process();
    }

    protected void filters() {
        filtersUnit.process();
    }

    protected void appliance() {
        filtersAndMotorsApplianceUnit.process();
        filtersAndOesApplianceUnit.process();
    }
}
