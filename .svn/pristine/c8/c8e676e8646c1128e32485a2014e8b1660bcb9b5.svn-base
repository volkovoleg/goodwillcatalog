package ru.goodfil.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Manufactor.java 154 2013-06-28 12:41:19Z chezxxx@gmail.com $
 */
@Entity
@Managed
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Manufactor implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean disabled;

    private Boolean onSite;

    private Integer mannStatus;



    @NotNull
    @Index(name = "vechicleTypeIdIndex")
    private Long vechicleTypeId;

    public static Manufactor create(@NotNull Long vechicleTypeId, @NotNull @NotBlank String name) {
        Manufactor manufactor = new Manufactor();
        manufactor.setVechicleTypeId(vechicleTypeId);
        manufactor.setName(name);
        manufactor.setDisabled(false);
        manufactor.setMannStatus(new Integer(0));
        manufactor.setOnSite(true);
        return manufactor;
    }

    public static Manufactor copy(Manufactor originalManufactor){
     Manufactor copyManufactor=Manufactor.create(originalManufactor.getVechicleTypeId(),originalManufactor.getName());
        copyManufactor.setOnSite(originalManufactor.getOnSite());
        copyManufactor.setMannStatus(originalManufactor.getMannStatus());
        return copyManufactor;
    }

    public Integer getMannStatus() {
        return mannStatus;
    }

    public void setMannStatus(Integer mannStatus) {
        this.mannStatus = mannStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getVechicleTypeId() {
        return vechicleTypeId;
    }

    public void setVechicleTypeId(Long vechicleTypeId) {
        this.vechicleTypeId = vechicleTypeId;
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
