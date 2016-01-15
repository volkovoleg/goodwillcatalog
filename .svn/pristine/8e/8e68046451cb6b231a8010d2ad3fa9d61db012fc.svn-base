package ru.goodfil.catalog.services;

import ru.goodfil.catalog.domain.Dict;

import javax.validation.constraints.NotNull;

/**
 * Сервис для работы со справочниками.
 * @author sazonovkirill@gmail.com
 * @version $Id: DictionaryService.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public interface DictionaryService {
    /**
     * Добавление типа двигателя.
     */
    public void addVechicleType(String name);

    /**
     * Переименование типа двигателя.
     */
    public void editVechicleType(String name);

    /**
     * Удаление типа двигателя
     */
    public void removeVechicleType(Long id, Long anotherId);


    /**
     * Добавление типа фильтра.
     */
    public void addFilterType(String code, String name);

    /**
     * Редактирование типа фильтра.
     */
    public void editFilterType(String name);

    /**
     * Удаление типа фильтра.
     */
    public void removeFilterType(Long id, Long anotherId);


    /**
     * Добавление формы фильтра.
     */
    public void addFilterForm(String filterTypeCode, String name, boolean[] params);

    public void editFilterForm();

    /**
     * Удаление формы фильтра.
     */
    public void removeFilterForm(Long filterFormId, Long anotherFilterFormId);
}
