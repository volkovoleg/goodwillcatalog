package ru.goodfil.catalog.adapters.core.impl;

import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.core.TabFileCellConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DateCellConverter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class DateCellConverter implements TabFileCellConverter {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy h:mm:ss");

    @Override
    public Object convert(String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return null;
        } else {
            try {
                return sdf.parse(cellValue);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DateCellConverter get() {
        return new DateCellConverter();
    }
}
