package ru.goodfil.catalog.mann.merge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.domain.Unique;

import java.util.*;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
public abstract class AbstractMerge<T extends Unique> implements Merge<T> {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Class<T> klass;

    public Class<T> getKlass() {
        return klass;
    }

    protected AbstractMerge(Class<T> klass) {
        this.klass = klass;
    }

    @Autowired
    @Qualifier("baseTemplate")
    protected HibernateTemplate baseTemplate;

    @Autowired
    @Qualifier("mannTemplate")
    protected HibernateTemplate mannTemplate;

    @Autowired
    @Qualifier("finalTemplate")
    protected HibernateTemplate finalTemplate;

    @Autowired
    protected MergeContext context;

    @Autowired
    protected Mergers mergers;

    protected void afterMerge() {};

    @Override
    public List<T> merge(List<Wrapper<T>> items) {
        Map<String/*businessKey*/, List<Wrapper<T>>> keyMap = new HashMap<String, List<Wrapper<T>>>();
        logger.trace("Merge for " + klass.getSimpleName());

        final List<T> result = new ArrayList<T>();

        // Groupping
        for (Wrapper<T> item : items) {
            String businessKey = getBusinessKey(item);

            if (!keyMap.containsKey(businessKey)) {
                keyMap.put(businessKey, new ArrayList<Wrapper<T>>());
            }

            keyMap.get(businessKey).add(item);
        }

        // Joining
        int counter = 0;
        for (Map.Entry<String, List<Wrapper<T>>> item : keyMap.entrySet()) {
            counter++;
            if (counter % 100 == 0)
                logger.debug("Processing " + klass + " " + counter + " of " + keyMap.size());

            String businessKey = item.getKey();
            List<Wrapper<T>> itemsToJoin = item.getValue();

            T newItem = join(itemsToJoin);
            if (newItem != null) {
                finalTemplate.save(newItem);
                for (Wrapper<T> itemToJoin : itemsToJoin) {
                    addToContext(itemToJoin.source(), itemToJoin.item().getId(), newItem.getId());
                }

                mergeChildren(itemsToJoin);
                result.add(newItem);
            }
        }

        afterMerge();
        return result;
    }

    protected abstract void mergeChildren(List<Wrapper<T>> itemsToJoin);

    protected <T2 extends Unique> void mergeChildren(List<Wrapper<T>> itemsToJoin, String query, Class<T2> klass) {
        List<Wrapper<T2>> children = new ArrayList<Wrapper<T2>>();

        for (Wrapper<T> wrapper : itemsToJoin) {
            if (wrapper.source().equals(Source.MANN.id())) {
                List<T2> l = mannTemplate.find(query, wrapper.item().getId());
                children.addAll(Wrapper.bySource(l, Source.MANN.id()));
            }
            if (wrapper.source().equals(Source.BASE.id())) {
                List<T2> l = baseTemplate.find(query, wrapper.item().getId());
                children.addAll(Wrapper.bySource(l, Source.BASE.id()));
            }
        }

        mergers.byClass(klass).merge(children);
    }

    public abstract T join(List<Wrapper<T>> items);

    public abstract String getBusinessKey(Wrapper<T> item);

    protected void addToContext(String source, String sourceId, String destId) {
        context.add(klass, source, sourceId, destId);
    }

    protected void addToContext(String source, Long sourceId, Long destId) {
        context.add(klass, source, sourceId, destId);
    }

    protected Long getDestBySource(Class klass, String source, Long sourceId) {
        return context.getDestBySource(klass, source, sourceId);
    }

    protected String getDestBySource(Class klass, String source, String sourceId) {
        return context.getDestBySource(klass, source, sourceId);
    }

    protected Wrapper<T> findFistFromMannOtherwiseFirst(List<Wrapper<T>> items) {
        for (Wrapper<T> item : items) {
            if (item.source().equals(Source.MANN.id())) {
                return item;
            }
        }

        return items.get(0);
    }
}
