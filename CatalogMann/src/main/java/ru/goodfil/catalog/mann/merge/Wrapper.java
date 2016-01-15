package ru.goodfil.catalog.mann.merge;

import ru.goodfil.catalog.utils.Assert;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
public class Wrapper<T> {
    private T item;
    private String source;

    public static <T> Wrapper<T> bySource(T item, String source) {
        Assert.notNull(item);
        Assert.notBlank(source);

        Wrapper<T> result = new Wrapper<T>();
        result.item = item;
        result.source = source;
        return result;
    }

    public static <T> List<Wrapper<T>> bySource(List<T> items, String source) {
        Assert.notNull(items);
        Assert.notNull(source);

        List<Wrapper<T>> result = new ArrayList<Wrapper<T>>();
        for (T item : items) {
            result.add(bySource(item, source));
        }
        return result;
    }

    public T item() {
        return item;
    }

    public String source() {
        return source;
    }
}
