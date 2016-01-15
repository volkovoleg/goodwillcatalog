package ru.goodfil.catalog.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.web.utils.CatalogWebModule;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: InjectorBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
@ManagedBean
public class InjectorBean {
    private Injector injector = Guice.createInjector(new CatalogWebModule());

    public synchronized Injector getInjector() {
        return injector;
    }
}
