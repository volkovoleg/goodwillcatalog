package ru.goodfil.catalog.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NumberValidator.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class NumberValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        try {
            new Integer(o.toString());
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage("Значение должно быть числом"));
        }
    }
}
