package ru.goodfil.catalog.web.utils;

import ru.goodfil.catalog.annotations.Managed;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: JSFComboboxModel.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
public class JSFComboboxModel {
    private Object selectedValue;
    private List<SelectItem> items;

    public static String getNullLabel() {
        return "[не выбрано]";
    }

    public Long getSelectedValueAsLong() {
        if (this.selectedValue == null) {
            return null;
        } else {
            return new Long(this.selectedValue.toString());
        }
    }

    public Object getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(Object value) {
        this.selectedValue = value;
    }

    public List<SelectItem> getItems() {
        if (this.items == null) {
            return new ArrayList<SelectItem>();
        }
        return this.items;
    }

    public void setItems(@NotNull List<javax.faces.model.SelectItem> items) {
        this.items = convert(items);
        this.selectedValue = null;
    }

    public void setItems(@NotNull List<javax.faces.model.SelectItem> items, boolean emptyValue) {
        this.items = new ArrayList<SelectItem>();
        if (emptyValue) {
            this.items.add(new SelectItem(null, getNullLabel()));
        }
        this.items.addAll(convert(items));
        this.selectedValue = null;
    }
    
    private List<SelectItem> convert(List<javax.faces.model.SelectItem> items) {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (javax.faces.model.SelectItem item : items) {
            SelectItem s = new SelectItem();
            s.setLabel(item.getLabel());
            s.setDescription(item.getDescription());
            s.setValue(item.getValue());
            s.setDisabled(item.isDisabled());
            s.setEscape(item.isEscape());
            result.add(s);
        }
        return result;
    }

    public void clear() {
        this.items = null;
        this.selectedValue = null;
    }
}