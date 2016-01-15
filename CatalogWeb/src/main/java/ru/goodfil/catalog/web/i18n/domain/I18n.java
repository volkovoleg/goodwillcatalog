package ru.goodfil.catalog.web.i18n.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: I18n.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@XStreamAlias(value = "i18n")
public class I18n {
    @XStreamImplicit(itemFieldName = "item")
    private List<Item> items;

    @XStreamImplicit(itemFieldName = "context")
    private List<Context> contexts;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public void setContexts(List<Context> contexts) {
        this.contexts = contexts;
    }

    @Override
    public String toString() {
        return "I18n{" +
                "items=" + items +
                ", contexts=" + contexts +
                '}';
    }
}
