package ru.goodfil.catalog.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.services.*;
import ru.goodfil.catalog.ui.guice.CatalogModule;
import ru.goodfil.catalog.utils.SessionProvider;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Services.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class Services {
    private static Injector injector = Guice.createInjector(new CatalogModule());

    private Services() {
    }

    public static AnalogsService getAnalogsService() {
        return injector.getInstance(AnalogsService.class);
    }

    public static CarsService getCarsService() {
        return injector.getInstance(CarsService.class);
    }

    public static FiltersService getFiltersService() {
        return injector.getInstance(FiltersService.class);
    }

    public static SessionProvider getSessionProvider() {
        return injector.getInstance(SessionProvider.class);
    }

    public static SettingsService getSettingService() {
        return injector.getInstance(SettingsService.class);
    }

    public static SearchAutoIndexService getSearchAutoIndexService() {
        return injector.getInstance(SearchAutoIndexService.class);
    }

    public static ExportService getExportService() {
        return injector.getInstance(ExportService.class);
    }

    public static PropertiesService getPropertiesService() {
        return injector.getInstance(PropertiesService.class);
    }
}
