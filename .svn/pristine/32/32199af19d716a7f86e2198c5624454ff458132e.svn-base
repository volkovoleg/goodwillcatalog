package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: LongCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class LongCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        return new Long(cellValue);
    }

    public static LongCellConverter get() {
        return new LongCellConverter();
    }
}
