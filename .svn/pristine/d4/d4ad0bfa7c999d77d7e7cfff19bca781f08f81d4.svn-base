package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.dict.FilterType;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersUnit extends AbstractMergeUnit<FilterType> {
    protected FiltersUnit() {
        super(FilterType.class,
              "from FilterType",
              Source.BASE, Source.MANN);
    }
}
