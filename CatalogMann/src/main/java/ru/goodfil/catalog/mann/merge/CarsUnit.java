package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Manufactor;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class CarsUnit extends AbstractMergeUnit<Manufactor> {
    protected CarsUnit() {
        super(Manufactor.class,
              "from Manufactor",
              Source.BASE, Source.MANN);
    }
}
