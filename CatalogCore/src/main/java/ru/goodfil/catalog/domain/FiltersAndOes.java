package ru.goodfil.catalog.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersAndOes.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Entity
public class FiltersAndOes implements Unique {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotNull
    @Index(name = "filterOefilterIdIndex")
    private Long filterId;

    @NotNull
    @Index(name = "filterOeOeIdIndex")
    private Long oeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFilterId() {
        return filterId;
    }

    public void setFilterId(Long filterId) {
        this.filterId = filterId;
    }

    public Long getOeId() {
        return oeId;
    }

    public void setOeId(Long oeId) {
        this.oeId = oeId;
    }

    public static FiltersAndOes create(Long filterId, Long oeId) {
        FiltersAndOes filtersAndOes = new FiltersAndOes();
        filtersAndOes.setFilterId(filterId);
        filtersAndOes.setOeId(oeId);
        return filtersAndOes;
    }

    @Override
    public String toString(){
        return "FiltersAndOes " + "filterId: " + filterId + ", " + "oeId: "+ oeId;
    }
}
