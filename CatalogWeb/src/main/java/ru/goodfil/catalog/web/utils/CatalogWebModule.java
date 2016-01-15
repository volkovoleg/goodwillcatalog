package ru.goodfil.catalog.web.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.export.service.impl.ExportServiceImpl;
import ru.goodfil.catalog.services.*;
import ru.goodfil.catalog.services.impl.*;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.utils.impl.DefaultSessionProvider;
import ru.goodfil.catalog.validation.core.HibernateValidationProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.faces.context.FacesContext;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CatalogWebModule.java 169 2013-10-04 14:01:47Z chezxxx@gmail.com $
 */
public class CatalogWebModule extends AbstractModule {
    @Override
    protected void configure() {
        URL configFile = null;
        try {
            configFile = FacesContext.getCurrentInstance().getExternalContext().getResource("/WEB-INF/hibernate.cfg.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        bind(ValidationProvider.class).to(HibernateValidationProvider.class).in(Singleton.class);
        bind(SessionProvider.class).to(DefaultSessionProvider.class).in(Singleton.class);
        bind(CarsService.class).to(CarsServiceImpl.class).in(Singleton.class);
        bind(FiltersService.class).to(FiltersServiceImpl.class).in(Singleton.class);
        bind(AnalogsService.class).to(AnalogServiceImpl.class).in(Singleton.class);
        bind(SettingsService.class).to(SettingsServiceImpl.class).in(Singleton.class);
        bind(SearchAutoIndexService.class).to(SearchAutoIndexServiceImpl.class).in(Singleton.class);
        bind(ExportService.class).to(ExportServiceImpl.class).in(Singleton.class);
        bind(URL.class).annotatedWith(Names.named("configFile")).toInstance(configFile);

    }
}
