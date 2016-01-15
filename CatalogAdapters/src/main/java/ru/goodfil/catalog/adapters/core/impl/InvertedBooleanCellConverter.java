package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: InvertedBooleanCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class InvertedBooleanCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        return "0".equals(cellValue);
    }

    public static InvertedBooleanCellConverter get() {
        return new InvertedBooleanCellConverter();
    }
}
