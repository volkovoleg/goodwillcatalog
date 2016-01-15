package ru.goodfil.catalog.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Each.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public class Each {
    private Each() {
        // nothing
    }

    public static <T> void count(Collection<T> items, int count, MyCallable<T> operation, MyCallable<Collection<T>> flush) {
        List<T> iter = new ArrayList<T>();

        long cnt = 0;
        for (T item : items) {
            cnt++;
            operation.call(item);
            iter.add(item);

            if (iter.size() == count) {
                System.out.println("Procecced: " + cnt + " of " + items.size());

                flush.call(iter);
                iter = new ArrayList<T>();
            }
        }

        flush.call(iter);
        iter = null;
    }
}
