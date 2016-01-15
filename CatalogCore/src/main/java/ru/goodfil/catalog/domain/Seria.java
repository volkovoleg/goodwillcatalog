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
 * @version $Id: Seria.java 154 2013-06-28 12:41:19Z chezxxx@gmail.com $
 */
@Managed
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Seria implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotNull
    @Index(name = "manufactorIdIndex")
    private Long manufactorId;

    @NotBlank
    private String name;

    @NotNull
    private Boolean disabled;

    private Boolean onSite;
    private Integer mannStatus;

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

    public Long getManufactorId() {
        return manufactorId;
    }

    public void setManufactorId(Long manufactorId) {
        this.manufactorId = manufactorId;
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

    public static Seria createSeria(@NotNull Long manufactorId, @NotNull @NotBlank String name) {
        Seria seria = new Seria();
        seria.setManufactorId(manufactorId);
        seria.setName(name);
        seria.setDisabled(false);
        seria.setMannStatus(new Integer(0));
        seria.setOnSite(true);
        return seria;
    }
    
    public static Seria copy(Seria originalSeria){
        Seria copySeria=createSeria(originalSeria.getManufactorId(),originalSeria.getName());
        copySeria.setOnSite(originalSeria.getOnSite());
        copySeria.setMannStatus(originalSeria.getMannStatus());
        return copySeria;
    }
}
