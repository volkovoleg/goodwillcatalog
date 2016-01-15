package ru.goodfil.catalog.ui.swing;

import ru.goodfil.catalog.utils.SelectItem;

import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: UIDictionary.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public interface UIDictionary {
    List<SelectItem> getSelectItems();

    String getLabel(String key);
}
