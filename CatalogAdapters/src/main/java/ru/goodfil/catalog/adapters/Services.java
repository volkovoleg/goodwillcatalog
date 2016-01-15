package ru.goodfil.catalog.adapters;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.goodfil.catalog.adapters.guice.CatalogAdaptersModule;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.utils.SessionProvider;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Services.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class Services {
    private static Injector injector = Guice.createInjector(new CatalogAdaptersModule());

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
}
