package ru.goodfil.catalog.web.i18n;

import com.thoughtworks.xstream.XStream;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.web.i18n.domain.Context;
import ru.goodfil.catalog.web.i18n.domain.I18n;
import ru.goodfil.catalog.web.i18n.domain.Item;
import ru.goodfil.catalog.web.i18n.domain.Translation;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: I18ManagedBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class I18ManagedBean {
    public static final String BEAN_NAME = "i18Bean";

    private I18n i18n;

    public I18ManagedBean() {
        init();
    }

    public void init() {
        XStream xStream = new XStream();
        xStream.processAnnotations(new Class[]{
                I18n.class,
                Context.class,
                Item.class,
                Translation.class
        });
        i18n = (I18n) xStream.fromXML(this.getClass().getResourceAsStream("core.xml"));
    }

    public String get(String key, String locale) {
        Assert.notBlank(key);
        Assert.notBlank(locale);

        if (i18n.getItems() != null) {
            for (Item item : i18n.getItems()) {
                if (item.getKey() != null && item.getKey().equals(key)) {
                    if (item.getTranslations() != null) {
                        for (Translation translation : item.getTranslations()) {
                            if (translation.getLocale() != null && translation.getLocale().equals(locale)) {
                                return translation.getValue();
                            }
                        }
                    }
                }
            }
        }
        return key;
    }

    public String get(String contextName, String key, String locale) {
        Assert.notBlank(key);
        Assert.notBlank(locale);
        Assert.notBlank(contextName);

        if (i18n.getContexts() != null) {
            for (Context context : i18n.getContexts()) {
                if (context.getName() != null && context.getName().equals(contextName)) {
                    if (context.getItems() != null) {
                        for (Item item : context.getItems()) {
                            if (item.getKey() != null && item.getKey().equals(key)) {
                                if (item.getTranslations() != null) {
                                    for (Translation translation : item.getTranslations()) {
                                        if (translation.getLocale() != null && translation.getLocale().equals(locale)) {
                                            return translation.getValue();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return key;
    }
}
