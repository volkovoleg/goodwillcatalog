package ru.goodfil.catalog.utils;

import ru.goodfil.catalog.domain.Unique;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс позволяет для объектов типа Unique из поллекции получить map,
 * ключом в котором будет Unique#id
 * @see ru.goodfil.catalog.domain.Unique
 * @author sazonovkirill@gmail.com
 * @version $Id: ListAsMap.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public class ListAsMap {
    public static <T extends Unique> Map<Long, T> get(Collection<T> items) {
        Assert.notNull(items);

        Map<Long, T> result = new HashMap<Long, T>();
        for (Unique item : items) {
            result.put(item.getId(), (T) item);
        }
        return result;
    }
}
