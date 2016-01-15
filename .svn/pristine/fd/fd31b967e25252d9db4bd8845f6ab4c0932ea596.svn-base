package ru.goodfil.catalog.adapters.core.impl;

import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

import java.math.BigDecimal;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NullableBigDecimalCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class NullableBigDecimalCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else {
            cellValue = cellValue.replace(',', '.');
            try {
                return new BigDecimal(cellValue);
            } catch (NumberFormatException e) {
                System.out.println("Value: " + cellValue);
                e.printStackTrace();
            }
            return null;
        }
    }

    public static NullableBigDecimalCellConverter get() {
        return new NullableBigDecimalCellConverter();
    }
}
