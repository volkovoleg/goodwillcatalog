package ru.goodfil.catalog.mann.services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 */
@Component
public class MannEtlService {
    @Autowired
    private VechicleTypeWorkUnit vechicleType;

    @Autowired
    private ManufactorWorkUnit manufactor;

    @Autowired
    private SeriaWorkUnit seria;
    
    @Autowired
    private MotorWorkUnit motor;
    
    @Autowired
    private BrandWorkUnit brand;

    @Autowired
    private OeWorkUnit oe;

    @Autowired
    private FilterTypeWorkUnit filterType;

    @Autowired
    private FilterFormWorkUnit filterForm;

    @Autowired
    private FilterWorkUnit filter;

    @Autowired
    private FilterApplianceUnit filterAppliance;

    @Autowired
    private OeApplianceUnit oeAppliance;
    
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
}
