package ru.goodfil.catalog.adapters.core.impl;

import org.apache.commons.beanutils.PropertyUtils;
import ru.goodfil.catalog.adapters.core.*;
import ru.goodfil.catalog.utils.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFileTransformationImpl.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class TabFileTransformationImpl<T> implements TabFileTransformation<T> {
    private final Class<T> klass;

    public TabFileTransformationImpl(Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public List<T> transform(TabFile tabFile, Mapping mapping) {
        Assert.notNull(tabFile, mapping);
        return transform(tabFile, mapping, null);
    }

    @Override
    public List<T> transform(TabFile tabFile, Mapping mapping, TransformationFilter<T> filter) {
        Assert.notNull(tabFile, mapping);

        List<T> result = new ArrayList<T>();

        for (int i = 0; i < tabFile.getRowsCount(); i++) {
            T object = null;

            try {
                object = klass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] row = tabFile.getData()[i];
            if (filter != null) {
                row = filter.transformRow(tabFile, row);
            }
            if (row == null) {
                continue;
            }

            for (int j = 0; j < tabFile.getColumnsCount(); j++) {
                String columnName = tabFile.getColumns()[j];
                if (!mapping.isContainsColumn(columnName)) {
                    continue;
                }

                String fieldName = mapping.getFieldName(columnName);
                TabFileCellConverter converter = mapping.getConverter(columnName);
                Class fieldClass = mapping.getFieldClass(columnName);
                String cellValue = row[j];
                Object objectValue = null;

                try {
                    objectValue = converter.convert(cellValue);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

                try {
                    PropertyUtils.setSimpleProperty(object, fieldName, objectValue);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if (filter != null) {
                object = filter.transformObject(object);
            }

            if (object != null) {
                result.add(object);
            }
        }

        return result;
    }
}
