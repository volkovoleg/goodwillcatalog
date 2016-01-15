package ru.goodfil.catalog.web.i18n;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: I18nContextListener.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class I18nContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(I18ManagedBean.BEAN_NAME, new I18ManagedBean());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // nothing
    }
}
