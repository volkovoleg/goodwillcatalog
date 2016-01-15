package ru.goodfil.catalog.web.i18n.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Item.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class Item {
    @XStreamAsAttribute
    private String key;

    @XStreamImplicit(itemFieldName = "translation")
    private List<Translation> translations;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    @Override
    public String toString() {
        return "Item{" +
                "key='" + key + '\'' +
                ", translations=" + translations +
                '}';
    }
}
