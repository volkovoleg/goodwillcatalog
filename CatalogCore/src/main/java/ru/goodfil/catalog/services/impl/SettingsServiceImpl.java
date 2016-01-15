package ru.goodfil.catalog.services.impl;

import com.google.inject.Inject;
import ru.goodfil.catalog.adapters.SettingAdapter;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Setting;
import ru.goodfil.catalog.services.SettingsService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Сервис для работы с таблицей настроек.
 * @author sazonovkirill@gmail.com
 * @version $Id: SettingsServiceImpl.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class SettingsServiceImpl implements SettingsService {
    @Inject
    @NotNull
    private SettingAdapter settingAdapter;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Setting> getAllSettings() {
        return settingAdapter.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(String key) {
        Setting setting = settingAdapter.getByName(key);
        if (setting == null) {
            return null;
        } else {
            return setting.getValue();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String key, String vlaue) {
        Setting setting = settingAdapter.getByName(key);
        if (setting == null) {
            settingAdapter.save(Setting.create(key, vlaue));
        } else {
            setting.setValue(vlaue);
            settingAdapter.merge(setting);
        }
    }

    public SettingAdapter getSettingAdapter() {
        return settingAdapter;
    }

    public void setSettingAdapter(SettingAdapter settingAdapter) {
        this.settingAdapter = settingAdapter;
    }
}
