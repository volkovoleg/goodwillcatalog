package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: IntegerCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class IntegerCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        return new Integer(cellValue);
    }

    public static IntegerCellConverter get() {
        return new IntegerCellConverter();
    }
}
