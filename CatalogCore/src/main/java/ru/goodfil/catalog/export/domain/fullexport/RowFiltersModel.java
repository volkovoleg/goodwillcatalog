package ru.goodfil.catalog.export.domain.fullexport;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: RowFiltersModel.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class RowFiltersModel {
    private String filterTypeCode;
    private final Set<String> filters = new HashSet<String>();

    public String getFilterTypeId() {
        return filterTypeCode;
    }

    public void setFilterTypeId(String filterTypeId) {
        this.filterTypeCode = filterTypeId;
    }

    public Set<String> getFilters() {
        return filters;
    }
}
