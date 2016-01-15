package ru.goodfil.catalog.ui.swing;

/**
 * Спобоб открытия формы: "для редактирования", "для просмотра" и т.п.
 * Используется для enable/disable/make readonly полей формы.
 * @author sazonovkirill@gmail.com
 * @version $Id: EditMode.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public enum EditMode {
    /**
     * Не важно (форма не зависит от способа открытия).
     */
    UNDEFINED,

    /**
     * Создание/добавление нового объекта.
     */
    CREATE,

    /**
     * Редактирование/изменение существующего объема.
     */
    EDIT,

    /**
     * Только просмотр.
     */
    VIEW
}
