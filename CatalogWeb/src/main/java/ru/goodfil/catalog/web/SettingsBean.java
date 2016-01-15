package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.domain.Setting;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.SettingsService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Бин для получения настроек из сущности Setting.
 * @see ru.goodfil.catalog.domain.Setting
 * @author sazonovkirill@gmail.com
 * @version $Id: SettingsBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
@ManagedBean
public class SettingsBean extends PageBean {
    private Map<String, String> settings;

    @NotNull
    @Inject
    private SettingsService settingsService;

    public Map<String, String> getSettings() {
        if (settings == null) {
            settings = new UpdatableMap(settingsService);

            List<Setting> theSettings = settingsService.getAllSettings();
            for (Setting setting : theSettings) {
                settings.put(setting.getName(), setting.getValue());
            }
        }
        return settings;
    }

    @Managed
    public static class UpdatableMap extends HashMap<String, String> {
        @NotNull
        private final SettingsService settingsService;

        public UpdatableMap(@NotNull SettingsService settingsService) {
            Assert.notNull(settingsService);

            this.settingsService = settingsService;
        }

        @Override
        public String get(Object key) {
            return settingsService.get(key.toString());
        }
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
