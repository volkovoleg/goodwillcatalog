package ru.goodfil.catalog.export.domain.fullexport;

import java.util.HashSet;
import java.util.Set;

/**
 * Настройки выгрузки в Excel
 * @author sazonovkirill@gmail.com
 * @version $Id: FullCatalogExportParams.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class FullCatalogExportParams {
    /**
     * Типы изделий, которые необходимо выгрузить.
     */
    private final Set<Long> filterTypes = new HashSet<Long>();

    /**
     * Выгружать только продукцию GoodWill
     */
    private boolean goodwillOnly = true;

    /**
     * Пропускать строки, в которыъ отсутствуют фильтры.
     */
    private boolean ignoreEmptyRows = true;

    /**
     * Типы изделий, которые необходимо выгрузить.
     */
    public Set<Long> getFilterTypes() {
        return filterTypes;
    }

    /**
     * Выгружать только продукцию GoodWill
     */
    public boolean isGoodwillOnly() {
        return goodwillOnly;
    }

    /**
     * Выгружать только продукцию GoodWill
     */
    public void setGoodwillOnly(boolean goodwillOnly) {
        this.goodwillOnly = goodwillOnly;
    }

    /**
     * Пропускать строки, в которыъ отсутствуют фильтры.
     */
    public boolean isIgnoreEmptyRows() {
        return ignoreEmptyRows;
    }

    /**
     * Пропускать строки, в которыъ отсутствуют фильтры.
     */
    public void setIgnoreEmptyRows(boolean ignoreEmptyRows) {
        this.ignoreEmptyRows = ignoreEmptyRows;
    }
}
