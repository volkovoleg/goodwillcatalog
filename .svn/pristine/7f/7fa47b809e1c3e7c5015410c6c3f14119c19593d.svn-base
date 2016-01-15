package ru.goodfil.catalog.ui.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Самодельный простенький буфер обмена.
 * @author sazonovkirill@gmail.com
 * @version $Id: Clipboard.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class Clipboard {
    private static Clipboard instance = new Clipboard();

    public static Clipboard getInstance() {
        return instance;
    }

    private final List items = new ArrayList();

    public boolean hasSomething() {
        return items.size() > 0;
    }

    public void put(Collection collection) {
        System.out.println("Clipboard (" + collection.size() + "): ");
        for (Object o : collection) {
            System.out.println(String.valueOf(o));
        }

        items.clear();
        items.addAll(collection);
    }

    public Collection get() {
        return items;
    }
}
