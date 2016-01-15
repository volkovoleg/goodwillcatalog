package ru.goodfil.catalog.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.utils.SearchMask;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Oe.java 149 2013-06-14 09:32:04Z chezxxx@gmail.com $
 */
@Entity
@Managed
public class Oe implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;
    private Integer mannStatus;

    public Integer getMannStatus() {
        return mannStatus;
    }

    public void setMannStatus(Integer mannStatus) {
        this.mannStatus = mannStatus;
    }

    @NotNull
    @Index(name = "brandIdIndex")
    private Long brandId;

    @NotBlank
    @Index(name = "oeNI")
    private String name;

    @Index(name = "oeSNI")
    private String searchName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.searchName = SearchMask.mask(name);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Oe create(@NotNull Long brandId, @NotNull @NotBlank String name) {
        Oe oe = new Oe();
        oe.setBrandId(brandId);
        oe.setName(name.toUpperCase());
        oe.setMannStatus(new Integer(0));
        return oe;
    }
    
    public static Oe copy (Oe originalOe){
        Oe newOe=Oe.create(originalOe.getBrandId(),originalOe.getName());
        newOe.setMannStatus(originalOe.getMannStatus());
        newOe.setSearchName(originalOe.getSearchName());
        return newOe;
    }
}
