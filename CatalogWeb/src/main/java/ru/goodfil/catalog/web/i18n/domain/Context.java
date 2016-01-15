package ru.goodfil.catalog.web.i18n.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Context.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class Context {
    @XStreamAsAttribute
    private String name;

    @XStreamImplicit(itemFieldName = "item")
    private List<Item> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Context{" +
                "name='" + name + '\'' +
                ", items=" + items +
                '}';
    }
}
