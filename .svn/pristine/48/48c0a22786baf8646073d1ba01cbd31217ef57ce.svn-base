package ru.goodfil.catalog.mann.merge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.domain.Motor;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersAndMotorsApplianceMerge extends AbstractMerge<FiltersAndMotors> {
    private static final Logger logger = LoggerFactory.getLogger(FiltersAndMotorsApplianceMerge.class);
    private static final Logger logger_skpped = LoggerFactory.getLogger(FiltersAndMotorsApplianceMerge.class.getName() + ".Skipped");
    
    protected FiltersAndMotorsApplianceMerge() {
        super(FiltersAndMotors.class);
    }

    private Long processedCount = 0L;
    private Long skipped_noFilter = 0L;
    private Long skipped_noMotor = 0L;
    private Long skipped_noMotorNoFilter = 0L;

    Long id = 0L;

    @Override
    public FiltersAndMotors join(List<Wrapper<FiltersAndMotors>> items) {
        Wrapper<FiltersAndMotors> wrapper = items.get(0);
        FiltersAndMotors fam = wrapper.item();

        Long filterId = context.getDestBySource(Filter.class, wrapper.source(), fam.getFilterId());
        Long motorId = context.getDestBySource(Motor.class, wrapper.source(), fam.getMotorId());

        if (filterId == null || motorId == null) {
            if (filterId == null && motorId != null) skipped_noFilter++;
            if (motorId == null && filterId != null) skipped_noMotor++;
            if (motorId == null && filterId == null) skipped_noMotorNoFilter++;

            logger_skpped.debug(wrapper.source() + " id:" + fam.getId() + ", motorId:" + fam.getMotorId() + ", filterId:" + fam.getFilterId());
            return null;
        }

        id++;
        FiltersAndMotors newFam = new FiltersAndMotors();
        newFam.setId(id);
        newFam.setFilterId(filterId);
        newFam.setMotorId(motorId);
        processedCount++;
        return newFam;
    }

    @Override
    public String getBusinessKey(Wrapper<FiltersAndMotors> item) {
        Long filterId = context.getDestBySource(Filter.class, item.source(), item.item().getFilterId());
        Long motorId = context.getDestBySource(Motor.class, item.source(), item.item().getMotorId());

        return String.valueOf(filterId) + "#" + String.valueOf(motorId);
    }

    @Override
    protected void mergeChildren(List<Wrapper<FiltersAndMotors>> itemsToJoin) {
        // Nothing
    }

    @Override
    protected void afterMerge() {
        logger.debug("processedCount: " + processedCount);
        logger.debug("skipped_noFilter: " + skipped_noFilter);
        logger.debug("skipped_noMotor: " + skipped_noMotor);
        logger.debug("skipped_noMotorNoFilter: " + skipped_noMotorNoFilter);
    }
}
