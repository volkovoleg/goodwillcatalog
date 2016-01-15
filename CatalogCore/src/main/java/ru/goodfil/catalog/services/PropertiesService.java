package ru.goodfil.catalog.services;

/**
 *
 * @author sazonovkirill@gmail.com
 * @version $Id: PropertiesService.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public interface PropertiesService {
    public static final String CATALOG_WEB_EXCHANGE_PATH = "catalog.web.exchange.path";
    public static final String IMAGES_DIRECTORY_PATH = "images.directory.path";

    public String getProperty(String key);
}
