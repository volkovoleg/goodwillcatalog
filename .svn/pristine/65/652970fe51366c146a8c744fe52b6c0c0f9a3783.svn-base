package ru.goodfil.catalog.mann;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.mann.load.*;

import javax.annotation.PostConstruct;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
@Lazy(true)
public class LoadService {
    @Autowired
    private VechicleTypeLoad vechicleType;

    @Autowired
    private ManufactorLoad manufactor;

    @Autowired
    private SeriaLoad seria;
    
    @Autowired
    private MotorLoad motor;
    
    @Autowired
    private BrandLoad brand;

    @Autowired
    private OeLoad oe;

    @Autowired
    private FilterTypeLoad filterType;

    @Autowired
    private FilterFormLoad filterForm;

    @Autowired
    private FilterLoad filter;

    @Autowired
    private FilterApplianceLoad filterAppliance;

    @Autowired
    private OeApplianceLoad oeAppliance;

    @Autowired
    private InfoWorkUnit info;
    
    private String baseDir;
    
    @PostConstruct
    public void init() {
        this.baseDir = System.getProperty("base.dir");        
    }

    public void run() {
        cars();
        oes();
        filters();
        appliance();

        info();
    }
    
    protected void cars() {
        vechicleType.load();
        manufactor.process();
        seria.process();
        motor.process();        
    }
    
    protected void oes() {
        brand.process();
        oe.process();        
    }

    protected void filters() {
        filterType.process();
        filterForm.process();
        filter.process();
    }

    protected void appliance() {
        filterAppliance.process();
        oeAppliance.process();
    }

    protected void info() {
        info.show();
    }
}
