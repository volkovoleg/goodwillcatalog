package ru.goodfil.catalog.domain;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import ru.goodfil.catalog.utils.Assert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Motor.java 154 2013-06-28 12:41:19Z chezxxx@gmail.com $
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Motor implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotNull
    @Index(name = "seriaIdIndex")
    private Long seriaId;

    private String name;
    private String engine;
    private String kw;
    private String hp;
    private Date dateF;
    private Date dateT;
    private Integer mannStatus;

    @NotNull
    private Boolean disabled;

    private Boolean onSite;

    /**
     * Возвращает true, если два мотора полностью идентичны (сравниваются только бизнес-поля).
     */
    public static boolean completelyEqual(Motor m1, Motor m2) {
        Assert.isTrue(!m1.getDisabled());
        Assert.isTrue(!m2.getDisabled());

        return StringUtils.equalsIgnoreCase(m1.getName(), m2.getName()) &&
                StringUtils.equalsIgnoreCase(m1.getEngine(), m2.getEngine()) &&
                StringUtils.equalsIgnoreCase(m1.getKw(), m2.getKw()) &&
                StringUtils.equalsIgnoreCase(m1.getHp(), m2.getHp()) &&
                datesEqual(m1.getDateF(), m2.getDateF()) &&
                datesEqual(m1.getDateT(), m2.getDateT());
    }

    public String getFullBusinessKey() {
        return String.valueOf(name).toLowerCase() + "#" +
                String.valueOf(engine).toLowerCase() + "#" +
                String.valueOf(kw).toLowerCase() + "#" +
                String.valueOf(hp).toLowerCase() + "#" +
                String.valueOf(dateF != null ? dateF.getTime() : "").toLowerCase() + "#" +
                String.valueOf(dateT != null ? dateT.getTime() : "").toLowerCase() + "#";
    }

    /**
     * Возвращает true, если два мотора идентичны без учета дат выпуска (сравниваются только бизнес-поля).
     */
    public static boolean equalExceptDate(Motor m1, Motor m2) {
        Assert.isTrue(!m1.getDisabled());
        Assert.isTrue(!m2.getDisabled());

        return StringUtils.equalsIgnoreCase(m1.getName(), m2.getName()) &&
                StringUtils.equalsIgnoreCase(m1.getEngine(), m2.getEngine()) &&
                StringUtils.equalsIgnoreCase(m1.getKw(), m2.getKw()) &&
                StringUtils.equalsIgnoreCase(m1.getHp(), m2.getHp());
    }

    public String getBusinessKeyExceptDates() {
        return String.valueOf(name).toLowerCase() + "#" +
                String.valueOf(engine).toLowerCase() + "#" +
                String.valueOf(kw).toLowerCase() + "#" +
                String.valueOf(hp).toLowerCase() + "#";
    }

    private static boolean datesEqual(Date d1, Date d2) {
        if (d1 == null && d2 == null) {
            return true;
        } else if (d1 != null && d2 != null) {
            return d1.equals(d2);
        } else {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeriaId() {
        return seriaId;
    }

    public void setSeriaId(Long seriaId) {
        this.seriaId = seriaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public Date getDateF() {
        return dateF;
    }

    public void setDateF(Date dateF) {
        this.dateF = dateF;
    }

    public Date getDateT() {
        return dateT;
    }

    public void setDateT(Date dateT) {
        this.dateT = dateT;
    }

    public Integer getMannStatus() {
        return mannStatus;
    }

    public void setMannStatus(Integer mannStatus) {
        this.mannStatus = mannStatus;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public static Motor create() {
        Motor motor = new Motor();
        motor.setDisabled(false);
        motor.setMannStatus(new Integer(0));
        motor.setOnSite(true);
        return motor;
    }
    
    public static Motor copy(Motor originalMotor){
        Assert.notNull(originalMotor);
        Motor copyMotor=Motor.create();
        copyMotor.setSeriaId(originalMotor.getSeriaId());
        copyMotor.setDateF(originalMotor.getDateF());
        copyMotor.setDateT(originalMotor.getDateT());
        copyMotor.setEngine(originalMotor.getEngine());
        copyMotor.setHp(originalMotor.getHp());
        copyMotor.setKw(originalMotor.getKw());
        copyMotor.setOnSite(originalMotor.getOnSite());
        copyMotor.setName(originalMotor.getName());
        copyMotor.setMannStatus(originalMotor.getMannStatus());
        return copyMotor;
    }

    public Boolean getOnSite() {
//        return onSite;
//          Safe implementation below:
        if (onSite == null) {
            onSite = false;
        }
        return onSite;
    }

    public void setOnSite(Boolean onSite) {
        if (onSite == null) {
            onSite = false;
        }
        this.onSite = onSite;
    }

    @Override
    public String toString() {
        //return String.format("%s %s", name, engine).trim();
        String result = name;
        if (engine != null) result += " " + engine;
        return result.trim();
    }
}
