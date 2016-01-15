package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Oe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class OesMerge extends AbstractMerge<Oe> {
    protected OesMerge() {
        super(Oe.class);
    }

    Long id = 0L;

    @Override
    public Oe join(List<Wrapper<Oe>> items) {
        Wrapper<Oe> firstOeWrapper = items.get(0);
        Oe firstOe = firstOeWrapper.item();

        id++;
        Oe newOe = new Oe();
        newOe.setId(id);
        newOe.setName(firstOe.getName());
        newOe.setSearchName(firstOe.getSearchName());

        Long brandId = getDestBySource(Brand.class, firstOeWrapper.source(), firstOe.getBrandId());
        newOe.setBrandId(brandId);

        return newOe;
    }

    @Override
    public String getBusinessKey(Wrapper<Oe> item) {
        return item.item().getName();
    }

    @Override
    public void mergeChildren(List<Wrapper<Oe>> itemsToJoin) {
        // Nothing
    }
}
