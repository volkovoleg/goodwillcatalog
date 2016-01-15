package ru.goodfil.catalog.ui.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.export.service.impl.ExportServiceImpl;
import ru.goodfil.catalog.services.*;
import ru.goodfil.catalog.services.impl.*;
import ru.goodfil.catalog.ui.guice.impl.PropertiesServiceImpl;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.utils.impl.DefaultSessionProvider;
import ru.goodfil.catalog.validation.core.HibernateValidationProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import java.net.URL;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CatalogModule.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class CatalogModule extends AbstractModule {
    @Override
    protected void configure() {
        URL configFile = getClass().getResource("/ru/goodfil/catalog/ui/hibernate.cfg.xml");

        bind(ValidationProvider.class).to(HibernateValidationProvider.class).in(Singleton.class);
        bind(SessionProvider.class).to(DefaultSessionProvider.class).in(Singleton.class);
        bind(CarsService.class).to(CarsServiceImpl.class).in(Singleton.class);
        bind(FiltersService.class).to(FiltersServiceImpl.class).in(Singleton.class);
        bind(AnalogsService.class).to(AnalogServiceImpl.class).in(Singleton.class);
        bind(SettingsService.class).to(SettingsServiceImpl.class).in(Singleton.class);
        bind(SearchAutoIndexService.class).to(SearchAutoIndexServiceImpl.class).in(Singleton.class);
        bind(ExportService.class).to(ExportServiceImpl.class).in(Singleton.class);

        bind(PropertiesService.class).to(PropertiesServiceImpl.class).in(Singleton.class);

        bind(URL.class).annotatedWith(Names.named("configFile")).toInstance(configFile);
    }
}
