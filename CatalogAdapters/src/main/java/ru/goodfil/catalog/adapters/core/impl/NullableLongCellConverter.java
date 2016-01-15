package ru.goodfil.catalog.adapters.core.impl;

import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NullableLongCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class NullableLongCellConverter implements TabFileCellConverter {
    @Override
    public Object convert(String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else {
            return new Long(cellValue);
        }
    }

    public static NullableLongCellConverter get() {
        return new NullableLongCellConverter();
    }
}
