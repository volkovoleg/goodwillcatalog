package ru.goodfil.catalog.ui.swing;

import ru.goodfil.catalog.domain.Dict;
import ru.goodfil.catalog.utils.SelectItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DefaultStringDictionary.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public abstract class DefaultStringDictionary<T extends Dict> implements UIDictionary {
    private final List<SelectItem> items = new ArrayList<SelectItem>();

    public DefaultStringDictionary(Collection<T> items) {
        setItems(items);
    }

    public DefaultStringDictionary() {
    }

    public void setItems(Collection<T> items) {
        this.items.clear();
        for (T item : items) {
            String id = item.getCode();
            String label = getLabel(item);

            SelectItem selectItem = new SelectItem(id, label);
            this.items.add(selectItem);
        }
    }

    protected abstract String getLabel(T item);

    @Override
    public List<SelectItem> getSelectItems() {
        return items;
    }

    @Override
    public String getLabel(String key) {
        for (SelectItem item : items) {
            if (item.getId().equals(key)) {
                return item.getLabel();
            }
        }
        return null;
    }
}
