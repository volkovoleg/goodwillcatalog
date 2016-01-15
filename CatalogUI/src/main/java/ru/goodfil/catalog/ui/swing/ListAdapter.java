package ru.goodfil.catalog.ui.swing;

import com.jgoodies.binding.list.ArrayListModel;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.WrapDynaBean;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Unique;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ListAdapter.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class ListAdapter<T extends Unique> {
    private final ArrayListModel<T> items = new ArrayListModel<T>();
    private final Class<T> klass;
    private final JList jlist;

    public ListAdapter(@NotNull Class<T> klass, @NotNull JList jlist) {
        this.klass = klass;
        this.jlist = jlist;
        this.jlist.setModel(items);
    }

    public int getSelectedCount() {
        return jlist.getSelectedIndices().length;
    }

    public boolean isOneSelected() {
        return getSelectedCount() == 1;
    }

    public boolean isNoneSelected() {
        return getSelectedCount() == 0;
    }

    public boolean isMultipleSelected() {
        return getSelectedCount() > 0;
    }

    public int size() {
        return items.size();
    }

    public int getCount() {
        return items.size();
    }

    public T getSelectedItem() {
        if (isOneSelected()) {
            int index = jlist.getSelectedIndex();
            return items.get(index);
        } else {
            return null;
        }
    }

    public <T2> T2 getSelectedItemField(String field) {
        T item = getSelectedItem();
        if (item == null) return null;

        DynaBean db = new WrapDynaBean(item);
        return (T2) db.get(field);
    }

    public <T2> Set<T2> getSelectedItemFields(String field) {
        Set<T2> result = new HashSet<T2>();
        for (T item : getSelectedItems()) {
            DynaBean db = new WrapDynaBean(item);
            result.add((T2) db.get(field));
        }
        return result;
    }

    public Long getSelectedItemId() {
        T item = getSelectedItem();
        if (item == null) return null;

        return item.getId();
    }

    public Set<Long> getSelectedItemIds() {
        Set<Long> result = new HashSet<Long>();
        for (T item : getSelectedItems()) {
            result.add(item.getId());
        }
        return result;
    }

    public List<T> getSelectedItems() {
        List<T> result = new ArrayList<T>();
        for (int index : jlist.getSelectedIndices()) {
            result.add(items.get(index));
        }
        return result;
    }

    public void clear() {
        items.clear();
    }

    public void add(T item) {
        items.add(item);
    }

    public void addAll(Collection<T> items) {
        this.items.addAll(items);
    }
}
