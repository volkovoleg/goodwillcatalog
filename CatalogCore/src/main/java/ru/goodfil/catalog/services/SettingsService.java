package ru.goodfil.catalog.services;

import ru.goodfil.catalog.domain.Setting;

import java.util.List;

/**
 * Сервис для работы с таблицей настроек.
 * @author sazonovkirill@gmail.com
 * @version $Id: SettingsService.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public interface SettingsService {
    /**
     * Фурнкция возвращает все пары ключ/значение.
     */
    public List<Setting> getAllSettings();

    /**
     * Возвращает значение по ключу.
     * @param key ключ.
     * @return значение.
     */
    public String get(String key);

    /**
     * Позволятет установить пару ключ/значение.
     * @param key ключ.
     * @param vlaue значение.
     */
    public void set(String key, String vlaue);
}
