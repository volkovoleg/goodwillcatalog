package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Unique;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class Mergers {
    @Autowired
    private BrandsMerge brandsMerge;

    @Autowired
    private OesMerge oesMerge;

    @Autowired
    private ManufactorsMerge manufactorsMerge;

    @Autowired
    private SeriesMerge seriesMerge;

    @Autowired
    private MotorsMerge motorsMerge;

    @Autowired
    private FilterTypesMerge filterTypesMerge;

    @Autowired
    private FilterFormsMerge filterFormsMerge;

    @Autowired
    private FiltersMerge filtersMerge;

    @Autowired
    private FiltersAndMotorsApplianceMerge filtersAndMotorsApplianceMerge;

    @Autowired
    private FiltersAndOesApplianceMerge filtersAndOesApplianceMerge;

    @PostConstruct
    protected void init() {
        addMerge(brandsMerge);
        addMerge(oesMerge);

        addMerge(manufactorsMerge);
        addMerge(seriesMerge);
        addMerge(motorsMerge);

        addMerge(filterTypesMerge);
        addMerge(filterFormsMerge);
        addMerge(filtersMerge);

        addMerge(filtersAndMotorsApplianceMerge);
        addMerge(filtersAndOesApplianceMerge);
    }

    private final Map<Class, Merge> mergers = new HashMap<Class, Merge>();

    private void addMerge(AbstractMerge merge) {
        mergers.put(merge.getKlass(), merge);
    }

    public <T extends Unique> Merge<T> byClass(Class<T> klass) {
        return mergers.get(klass);
    }
}
