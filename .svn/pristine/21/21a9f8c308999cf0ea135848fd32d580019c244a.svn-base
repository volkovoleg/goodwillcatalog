package ru.goodfil.catalog.mann.merge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.domain.Oe;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersAndOesApplianceMerge extends AbstractMerge<FiltersAndOes> {
    private static final Logger logger = LoggerFactory.getLogger(FiltersAndOesApplianceMerge.class);
    private static final Logger logger_skpped = LoggerFactory.getLogger(FiltersAndOesApplianceMerge.class.getName() + ".Skipped");

    protected FiltersAndOesApplianceMerge() {
        super(FiltersAndOes.class);
    }

    private Long processedCount = 0L;
    private Long skipped_noFilter = 0L;
    private Long skipped_noOe = 0L;
    private Long skipped_noOeNoFilter = 0L;
    
    Long id = 0L;

    @Override
    public FiltersAndOes join(List<Wrapper<FiltersAndOes>> items) {
        Wrapper<FiltersAndOes> wrapper = items.get(0);
        FiltersAndOes fam = wrapper.item();

        Long filterId = context.getDestBySource(Filter.class, wrapper.source(), fam.getFilterId());
        Long oeId = context.getDestBySource(Oe.class, wrapper.source(), fam.getOeId());

        if (filterId == null || oeId == null) {
            if (filterId == null && oeId != null) skipped_noFilter++;
            if (oeId == null && filterId != null) skipped_noOe++;
            if (oeId == null && filterId == null) skipped_noOeNoFilter++;

            logger_skpped.debug(wrapper.source() + " id:" + fam.getId() + ", oeId:" + fam.getOeId() + ", filterId:" + fam.getFilterId());
            return null;
        }

        id++;
        FiltersAndOes newFam = new FiltersAndOes();
        newFam.setId(id);
        newFam.setFilterId(filterId);
        newFam.setOeId(oeId);
        processedCount++;
        return newFam;
    }

    @Override
    public String getBusinessKey(Wrapper<FiltersAndOes> item) {
        Long filterId = context.getDestBySource(Filter.class, item.source(), item.item().getFilterId());
        Long oeId = context.getDestBySource(Filter.class, item.source(), item.item().getOeId());

        return String.valueOf(filterId) + "#" + String.valueOf(oeId);
    }

    @Override
    protected void mergeChildren(List<Wrapper<FiltersAndOes>> itemsToJoin) {
        // Nothing
    }

    @Override
    protected void afterMerge() {
        logger.debug("processedCount: " + processedCount);
        logger.debug("skipped_noFilter: " + skipped_noFilter);
        logger.debug("skipped_noOe: " + skipped_noOe);
        logger.debug("skipped_noOeNoFilter: " + skipped_noOeNoFilter);
    }
}
