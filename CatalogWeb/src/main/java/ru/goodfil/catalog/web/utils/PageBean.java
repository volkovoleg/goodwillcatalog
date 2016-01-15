package ru.goodfil.catalog.web.utils;

import ru.goodfil.catalog.domain.Unique;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.web.InjectorBean;
import ru.goodfil.catalog.web.LanguageBean;
import ru.goodfil.catalog.web.i18n.I18ManagedBean;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: PageBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class PageBean {
    protected I18ManagedBean i18ManagedBean;
    protected LanguageBean languageBean;


    public PageBean() {
        InjectorBean injectorBean = FacesUtils.getManagedBean("InjectorBean");
        injectorBean.getInjector().injectMembers(this);

        i18ManagedBean = FacesUtils.getManagedBean(I18ManagedBean.BEAN_NAME);
        languageBean = FacesUtils.getManagedBean("LanguageBean");
    }

    public static List<SelectItem> asSelectItems(@NotNull Collection<? extends Unique> items) {
        Assert.notNull(items);

        List<SelectItem> result = new ArrayList<SelectItem>();
        for (Unique item : items) {
            result.add(new SelectItem(item.getId(), item.toString().trim()));
        }
        return result;
    }

    public JSFComboboxModel i18n(JSFComboboxModel model) {
        Assert.notNull(i18ManagedBean);

        String locale = languageBean.getLocale();

        if (model != null) {
            List<ru.goodfil.catalog.web.utils.SelectItem> items = model.getItems();
            for (ru.goodfil.catalog.web.utils.SelectItem item : items) {
                String label = item.getOriginalLabel();
                label = i18ManagedBean.get(label, locale);
                item.setTranslatedLabel(label);
            }
        }

        return model;
    }

    public JSFComboboxModel i18n(JSFComboboxModel model, String context) {
        Assert.notNull(i18ManagedBean);

        String locale = languageBean.getLocale();

        if (model != null) {
            List<ru.goodfil.catalog.web.utils.SelectItem> items = model.getItems();
            for (ru.goodfil.catalog.web.utils.SelectItem item : items) {
                String label = item.getOriginalLabel();
                label = i18ManagedBean.get(context, label, locale);
                item.setTranslatedLabel(label);
            }
        }

        return model;
    }

    public String i18n(String label) {
        Assert.notBlank(label);

        Assert.notNull(languageBean);
        Assert.notNull(i18ManagedBean);

        String locale = languageBean.getLocale();
        return i18ManagedBean.get(label, locale);
    }

    public String i18n(String context, String label) {
        Assert.notBlank(context);
        Assert.notBlank(label);

        Assert.notNull(languageBean);
        Assert.notNull(i18ManagedBean);

        String locale = languageBean.getLocale();
        return i18ManagedBean.get(context, label, locale);
    }

    public boolean checkValueForSpam(String value) throws IOException {
        boolean isSpam = false;
        if(!value.equals("")){
            isSpam = true;
        }
        return isSpam;
    }
}
