package ru.goodfil.catalog.ui.guice.impl;

import ru.goodfil.catalog.services.PropertiesService;

import java.io.IOException;
import java.util.Properties;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: PropertiesServiceImpl.java 106 2012-12-21 13:26:15Z chezxxx@gmail.com $
 */
public class PropertiesServiceImpl implements PropertiesService {
    private final Properties props;

    public PropertiesServiceImpl() {
        props = new Properties();
        try {
            props.load(getClass().getResourceAsStream("/ru/goodfil/catalog/ui/system.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
