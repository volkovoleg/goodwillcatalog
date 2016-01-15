package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.domain.Seria;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class ManufactorsMerge extends AbstractMerge<Manufactor> {
    protected ManufactorsMerge() {
        super(Manufactor.class);
    }

    Long id = 0L;

    @Override
    public Manufactor join(List<Wrapper<Manufactor>> items) {
        Wrapper<Manufactor> manufactorWrapper = findFistFromMannOtherwiseFirst(items);
        Manufactor manufactor = manufactorWrapper.item();

        id++;
        Manufactor newManufactor = new Manufactor();
        newManufactor.setId(id);
        newManufactor.setDisabled(false);
        newManufactor.setOnSite(true);
        newManufactor.setName(manufactor.getName());
        newManufactor.setVechicleTypeId(manufactor.getVechicleTypeId());

        return newManufactor;
    }

    @Override
    public String getBusinessKey(Wrapper<Manufactor> item) {
        return item.item().getName();
    }

    @Override
    public void mergeChildren(List<Wrapper<Manufactor>> itemsToJoin) {
        mergeChildren(itemsToJoin, "from Seria s where s.manufactorId = ?", Seria.class);
    }
}
