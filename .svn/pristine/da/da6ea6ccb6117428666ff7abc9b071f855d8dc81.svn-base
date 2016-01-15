package ru.goodfil.catalog.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Version.java 97 2012-09-28 10:24:21Z sazonovkirill $
 */
public class Version {
    public static String VERSION;
    public static String BUILD_TIMESTAMP;
    
    static {
        Properties props = new Properties();
        try {
            props.load(Version.class.getClassLoader().getResourceAsStream(Version.VERSION_FILE_NAME));
        } catch (IOException e) {
            System.out.println("File version.properties not found");
        }

        VERSION = props.getProperty(Version.VERSION_KEY);
        BUILD_TIMESTAMP = props.getProperty(Version.BUILD_TIMESTAMP_KEY);
    }

    private static final String VERSION_FILE_NAME = "version.properties";
    private static final String VERSION_KEY = "version";
    private static final String BUILD_TIMESTAMP_KEY = "timestamp";
}
