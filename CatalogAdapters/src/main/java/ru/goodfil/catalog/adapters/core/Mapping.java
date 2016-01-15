package ru.goodfil.catalog.adapters.core;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Mapping.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public interface Mapping {
    void addColumnMapping(String columnName, String fieldName, Class fieldKlass);

    void addColumnMapping(String columnName, String fieldName, Class fieldKlass, TabFileCellConverter cellConverter);

    boolean isContainsColumn(String columnName);

    String getFieldName(String columnName);

    TabFileCellConverter getConverter(String columnName);

    Class getFieldClass(String columnName);
}
