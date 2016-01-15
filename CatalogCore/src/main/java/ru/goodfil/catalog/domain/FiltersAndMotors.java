package ru.goodfil.catalog.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import ru.goodfil.catalog.annotations.Managed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersAndMotors.java 177 2014-07-08 11:56:58Z chezxxx@gmail.com $
 */
@Entity
@Managed
public class FiltersAndMotors implements Unique {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotNull
    @Index(name = "filterIdIndex")
    private Long filterId;

    @NotNull
    @Index(name = "motorIdIndex")
    private Long motorId;

    @NotNull
    @Index(name = "FILTERCOMMENT")
    private String filterComment;

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

    public Long getMotorId() {
        return motorId;
    }

    public void setMotorId(Long motorId) {
        this.motorId = motorId;
    }

    public String getFilterComment() {
        return filterComment;
    }

    public void setFilterComment(String filterComment) {
        this.filterComment = filterComment;
    }

    public static FiltersAndMotors create(@NotNull Long motorId, @NotNull Long filterId) {
        FiltersAndMotors filtersAndMotors = new FiltersAndMotors();
        filtersAndMotors.setFilterId(filterId);
        filtersAndMotors.setMotorId(motorId);
        filtersAndMotors.setFilterComment("");
        return filtersAndMotors;
    }

    @Override
    public String toString(){
        return "FiltersAndMotors " + "filterId: " + filterId + ", " + "motorId: "+ motorId+ ", " + "filterComment: " + filterComment;
    }
}
