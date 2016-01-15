package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.Mapping;
import ru.goodfil.catalog.adapters.core.TabFileCellConverter;
import ru.goodfil.catalog.utils.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: MappingImpl.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class MappingImpl implements Mapping {
    private final Class entityClass;
    private Map<String, ColumnMapping> columnMappings = new HashMap<String, ColumnMapping>();

    public MappingImpl(Class entityClass) {
        Assert.notNull(entityClass);

        this.entityClass = entityClass;
    }

    @Override
    public void addColumnMapping(String columnName, String fieldName, Class fieldKlass) {
        Assert.notBlank(columnName, fieldName);
        Assert.notNull(fieldKlass);

        columnMappings.put(columnName, new ColumnMapping(columnName, fieldName, fieldKlass));
    }

    @Override
    public void addColumnMapping(String columnName, String fieldName, Class fieldKlass, TabFileCellConverter cellConverter) {
        Assert.notBlank(columnName, fieldName);
        Assert.notNull(cellConverter, fieldKlass);

        columnMappings.put(columnName, new ColumnMapping(columnName, fieldName, fieldKlass, cellConverter));
    }

    @Override
    public boolean isContainsColumn(String columnName) {
        return columnMappings.containsKey(columnName);
    }

    @Override
    public String getFieldName(String columnName) {
        return columnMappings.get(columnName).getFieldName();
    }

    @Override
    public TabFileCellConverter getConverter(String columnName) {
        return columnMappings.get(columnName).getCellConverter();
    }

    @Override
    public Class getFieldClass(String columnName) {
        return columnMappings.get(columnName).getKlass();
    }

    private static class ColumnMapping {
        private final String columnName;
        private final String fieldName;
        private final Class klass;
        private TabFileCellConverter cellConverter = new StringCellConverter();

        private ColumnMapping(String columnName, String fieldName, Class klass) {
            this.columnName = columnName;
            this.fieldName = fieldName;
            this.klass = klass;
        }

        private ColumnMapping(String columnName, String fieldName, Class klass, TabFileCellConverter cellConverter) {
            this.columnName = columnName;
            this.fieldName = fieldName;
            this.klass = klass;
            this.cellConverter = cellConverter;
        }

        public String getColumnName() {
            return columnName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public TabFileCellConverter getCellConverter() {
            return cellConverter;
        }

        public Class getKlass() {
            return klass;
        }
    }
}
