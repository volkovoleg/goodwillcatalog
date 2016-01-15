package ru.goodfil.catalog.mann.merge;

import org.hibernate.type.ListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class BrandsAndOesUnit extends AbstractMergeUnit<Brand> {
    protected BrandsAndOesUnit() {
        super(Brand.class,
              "from Brand",
               Source.BASE, Source.MANN);
    }
}
