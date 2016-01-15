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
 * @version $Id: Brand.java 164 2013-09-06 12:30:45Z chezxxx@gmail.com $
 */
@Entity
@Managed
public class Brand implements Unique, MannStatus, StandaloneBrandStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotBlank
    @Index(name = "brandNI")
    private String name;

    @Index(name = "brandSNI")
    private String searchName;

    private Integer mannStatus;

    private Integer standaloneStatus;

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
        this.searchName = SearchMask.mask(name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public static Brand create(@NotNull @NotBlank String name) {
        Brand brand = new Brand();
        brand.setName(name.toUpperCase());
        brand.setMannStatus(new Integer(0));
        brand.setStandaloneStatus(new Integer(0));
        return brand;
    }

    @Override
    public void setStandaloneStatus(Integer standaloneStatus) {
        this.standaloneStatus = standaloneStatus;
    }

    public Integer getStandaloneStatus() {
        return standaloneStatus;
    }
}
