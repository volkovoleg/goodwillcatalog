package ru.goodfil.catalog.mann.merge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Unique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
public abstract class AbstractMergeUnit<T extends Unique> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    private final Class<T> klass;

    private final String query;

    private final Source[] sources;

    protected AbstractMergeUnit(Class<T> klass, String query, Source... sources) {
        this.klass = klass;
        this.query = query;
        this.sources = sources;
    }

    public final void process() {
        List<Wrapper<T>> result = new ArrayList<Wrapper<T>>();

        for (Source source : sources) {
            List<T> fromThisSource = new ArrayList<T>();

            if (source == Source.BASE) {
                fromThisSource.addAll(baseTemplate.find(query));
            }
            if (source == Source.MANN) {
                fromThisSource.addAll(mannTemplate.find(query));
            }

            result.addAll(Wrapper.bySource(fromThisSource, source.id()));
            logger.debug("Loaded " + fromThisSource.size() + " of " + klass.getSimpleName() + " from " + source.id());
        }

        if (result.size() > 0) {
            Merge<T> merge = mergers.byClass(klass);
            merge.merge(result);
        }
    }

    @Autowired
    private Mergers mergers;
}
