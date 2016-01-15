package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.domain.FiltersAndOes;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersAndOesApplianceUnit extends AbstractMergeUnit<FiltersAndOes> {
    protected FiltersAndOesApplianceUnit() {
        super(FiltersAndOes.class,
              "from FiltersAndOes",
              Source.BASE, Source.MANN);
    }
}
