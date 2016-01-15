package ru.goodfil.catalog.adapters.core.impl;

import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NullableIntegerCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class NullableIntegerCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else {
            return new Integer(cellValue);
        }
    }

    public static NullableIntegerCellConverter get() {
        return new NullableIntegerCellConverter();
    }
}
