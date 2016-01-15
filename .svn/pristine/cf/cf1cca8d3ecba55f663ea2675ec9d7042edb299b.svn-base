package ru.goodfil.catalog.web.converter;

import org.apache.commons.lang.NotImplementedException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ShortDateTimeConverter.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class ShortDateTimeConverter implements Converter {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        throw new NotImplementedException();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return "";
        } else {
            if (o instanceof Date) {
                return sdf.format(o);
            } else {
                return "???";
            }
        }
    }
}
