package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Oe;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class BrandsMerge extends AbstractMerge<Brand> {
    protected BrandsMerge() {
        super(Brand.class);
    }
    
    Long id = 0L;

    @Override
    public Brand join(List<Wrapper<Brand>> items) {
        Wrapper<Brand> firstBrandWrapper = items.get(0);
        Brand firstBrand = firstBrandWrapper.item();

        id++;
        Brand newBrand = new Brand();
        newBrand.setId(id);
        newBrand.setName(firstBrand.getName());
        newBrand.setSearchName(firstBrand.getSearchName());

        return newBrand;
    }

    @Override
    public String getBusinessKey(Wrapper<Brand> item) {
        return item.item().getSearchName();
    }

    @Override
    public void mergeChildren(List<Wrapper<Brand>> itemsToJoin) {
        mergeChildren(itemsToJoin, "from Oe oe where oe.brandId = ?", Oe.class);
    }
}
