package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

import java.math.BigDecimal;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: BigDecimalCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class BigDecimalCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        return new BigDecimal(cellValue);
    }

    public static BigDecimalCellConverter get() {
        return new BigDecimalCellConverter();
    }
}
