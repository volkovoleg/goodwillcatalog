package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class SeriesMerge extends AbstractMerge<Seria> {
    @Autowired
    private MotorsMerge motorsMerge;

    protected SeriesMerge() {
        super(Seria.class);
    }
    
    Long id = 0L;

    @Override
    public Seria join(List<Wrapper<Seria>> items) {
        Wrapper<Seria> seriaWrapper = findFistFromMannOtherwiseFirst(items);
        Seria seria = seriaWrapper.item();

        id++;
        Seria newSeria = new Seria();
        newSeria.setId(id);
        newSeria.setDisabled(false);
        newSeria.setOnSite(true);
        newSeria.setName(seria.getName());

        Long manufactorId = getDestBySource(Manufactor.class, seriaWrapper.source(), seria.getManufactorId());
        newSeria.setManufactorId(manufactorId);

        return newSeria;
    }

    @Override
    public String getBusinessKey(Wrapper<Seria> item) {
        return item.item().getName();
    }

    @Override
    public void mergeChildren(List<Wrapper<Seria>> itemsToJoin) {
        mergeChildren(itemsToJoin, "from Motor m where m.seriaId = ?", Motor.class);
    }
}
