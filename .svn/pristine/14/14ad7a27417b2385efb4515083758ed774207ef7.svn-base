package ru.goodfil.catalog.web.utils;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Log4jInitListener.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class Log4jInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Log4jInitListener is initializing log4j");
        ServletContext sc = servletContextEvent.getServletContext();

        String webAppPath = sc.getRealPath("/");
        String log4jProp = webAppPath + "WEB-INF/log4j.properties";
        File f = new File(log4jProp);
        if (f.exists()) {
            System.out.println("Initializing log4j with: " + log4jProp);
            PropertyConfigurator.configure(log4jProp);
        } else {
            System.err.println("*** " + log4jProp + " file not found, so initializing log4j with BasicConfigurator");
            BasicConfigurator.configure();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // Do nothing
    }
}
