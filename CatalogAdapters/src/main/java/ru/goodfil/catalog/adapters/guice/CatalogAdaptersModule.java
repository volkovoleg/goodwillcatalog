package ru.goodfil.catalog.adapters.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import ru.goodfil.catalog.adapters.core.Mapping;
import ru.goodfil.catalog.adapters.core.TabFile;
import ru.goodfil.catalog.adapters.core.impl.MappingImpl;
import ru.goodfil.catalog.adapters.core.impl.TabFileImpl;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.export.service.impl.ExportServiceImpl;
import ru.goodfil.catalog.services.*;
import ru.goodfil.catalog.services.impl.*;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.utils.impl.DefaultSessionProvider;
import ru.goodfil.catalog.validation.core.HibernateValidationProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CatalogAdaptersModule.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class CatalogAdaptersModule extends AbstractModule {
    @Override
    protected void configure() {
        String configFile = getClass().getClassLoader().getResource("/ru/goodfil/catalog/adapters/hibernate.cfg.xml").getPath();

        bind(ValidationProvider.class).to(HibernateValidationProvider.class).in(Singleton.class);
        bind(SessionProvider.class).to(DefaultSessionProvider.class).in(Singleton.class);
        bind(CarsService.class).to(CarsServiceImpl.class).in(Singleton.class);
        bind(FiltersService.class).to(FiltersServiceImpl.class).in(Singleton.class);
        bind(AnalogsService.class).to(AnalogServiceImpl.class).in(Singleton.class);
        bind(SettingsService.class).to(SettingsServiceImpl.class).in(Singleton.class);
        bind(SearchAutoIndexService.class).to(SearchAutoIndexServiceImpl.class).in(Singleton.class);
        bind(ExportService.class).to(ExportServiceImpl.class).in(Singleton.class);
        bind(String.class).annotatedWith(Names.named("configFile")).toInstance(configFile);

        bind(Mapping.class).to(MappingImpl.class);
        bind(TabFile.class).to(TabFileImpl.class);
    }
}
