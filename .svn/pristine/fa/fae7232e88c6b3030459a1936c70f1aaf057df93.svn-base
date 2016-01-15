package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.domain.Seria;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class MotorsMerge extends AbstractMerge<Motor> {
    protected MotorsMerge() {
        super(Motor.class);
    }

    Long id = 0L;

    @Override
    public Motor join(List<Wrapper<Motor>> items) {
        for (Wrapper<Motor> w : items) {
            if (w.item().getId() == 1000202100006L) {
                int l = 5;
            }
        }

        Wrapper<Motor> motorWrapper = findFistFromMannOtherwiseFirst(items);
        Motor motor = motorWrapper.item();

        id++;
        Motor newMotor = new Motor();
        newMotor.setId(id);
        newMotor.setDisabled(false);
        newMotor.setOnSite(true);
        newMotor.setName(motor.getName());
        newMotor.setDateF(motor.getDateF());
        newMotor.setDateT(motor.getDateT());
        newMotor.setEngine(motor.getEngine());
        newMotor.setKw(motor.getKw());
        newMotor.setHp(motor.getHp());

        Long seriaId = getDestBySource(Seria.class, motorWrapper.source(), motor.getSeriaId());
        newMotor.setSeriaId(seriaId);

        return newMotor;
    }

    @Override
    public String getBusinessKey(Wrapper<Motor> item) {
        return item.item().getName();
    }

    @Override
    public void mergeChildren(List<Wrapper<Motor>> itemsToJoin) {
        // Nothing
    }
}
