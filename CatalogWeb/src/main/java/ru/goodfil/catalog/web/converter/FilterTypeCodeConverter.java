package ru.goodfil.catalog.web.converter;

import com.google.inject.Inject;
import org.apache.commons.lang.NotImplementedException;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateBefore;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterTypeCodeConverter.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
public class FilterTypeCodeConverter extends PageBean implements Converter {
    @NotNull
    @Inject
    private CarsService carsService;

    private List<FilterType> filterTypes;

    public List<FilterType> getFilterTypes() {
        if (filterTypes == null) {
            filterTypes = carsService.getFilterTypes();
        }
        return filterTypes;
    }

    @ValidateBefore
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        throw new NotImplementedException();
    }

    @ValidateBefore
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (o == null) {
            return "";
        } else {
            if (o instanceof String) {

                for (FilterType filterType : getFilterTypes()) {
                    if (filterType.getCode().equals(o)) {
                        String label = filterType.getName();
                        return i18n(label);
                    }
                }
                return "";

            } else {
                return "???";
            }
        }
    }
}
