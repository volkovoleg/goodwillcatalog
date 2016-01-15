package ru.goodfil.catalog.ui.swing;

import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.utils.SelectItem;

import javax.swing.*;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ComboAdapter.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class ComboAdapter {
    private final ValueModel value;
    private final JComboBox comboBox;
    private final UIDictionary dictionary;
    private final ComboBoxAdapter<SelectItem> adapter;

    public ComboAdapter(@NotNull JComboBox comboBox, @NotNull UIDictionary dictionary) {
        this.comboBox = comboBox;
        this.dictionary = dictionary;

        this.value = new ValueHolder();
        this.adapter = new ComboBoxAdapter<SelectItem>(dictionary.getSelectItems(), value);
        this.comboBox.setModel(this.adapter);
    }

    public void setValue(String code) {
        this.comboBox.setSelectedItem(new SelectItem(code));
    }

    public String getValue() {
        SelectItem selectItem = (SelectItem) this.comboBox.getSelectedItem();
        if (selectItem == null) {
            return null;
        } else {
            return selectItem.getId();
        }
    }

    public Long getLongValue() {
        String value = getValue();
        return value != null ? new Long(value) : null;
    }
}
