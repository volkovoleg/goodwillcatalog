package ru.goodfil.catalog.domain.dict;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.domain.Unique;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: VechicleType.java 104 2012-12-01 07:14:21Z sazonovkirill $
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VechicleType implements Unique {

    /**
     * Легковые автомобили.
     */
    public static final Long CARS = 1L;

    /**
     * Грузовые автомобили.
     */
    public static final Long TRUCKS = 2L;

    /**
     * Специальная техника.
     */
    public static final Long SPECIAL = 3L;

    /**
     * Катера и мотоциклы.
     */
    public static final Long BIKES = 4L;


    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotBlank
    private String name;

    private Boolean onSite;

    public static VechicleType create(Long id, String name, boolean onSite) {
        VechicleType vechicleType = new VechicleType();
        vechicleType.setId(id);
        vechicleType.setName(name);
        vechicleType.setOnSite(onSite);
        return vechicleType;
    }

    public static VechicleType create(Long id, String name) {
        VechicleType vechicleType = new VechicleType();
        vechicleType.setId(id);
        vechicleType.setName(name);
        return vechicleType;
    }

    public static VechicleType create(String name) {
        VechicleType vechicleType = new VechicleType();
        vechicleType.setName(name);
        return vechicleType;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return name;
    }
}

