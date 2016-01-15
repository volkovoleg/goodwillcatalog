package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: BooleanCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class BooleanCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        return "1".equals(cellValue);
    }

    public static BooleanCellConverter get() {
        return new BooleanCellConverter();
    }
}
