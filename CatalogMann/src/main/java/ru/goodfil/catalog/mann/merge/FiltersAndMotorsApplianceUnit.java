package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.domain.Manufactor;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersAndMotorsApplianceUnit extends AbstractMergeUnit<FiltersAndMotors> {
    protected FiltersAndMotorsApplianceUnit() {
        super(FiltersAndMotors.class,
              "from FiltersAndMotors",
              Source.BASE, Source.MANN);
    }
}
