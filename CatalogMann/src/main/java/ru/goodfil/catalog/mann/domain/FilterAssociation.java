package ru.goodfil.catalog.mann.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: sazonovkirill@gmail.com
 * Date: 27.11.12
 */
@Entity
public class FilterAssociation {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long goodwillId;

    @Column
    private Long mannId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodwillId() {
        return goodwillId;
    }

    public void setGoodwillId(Long goodwillId) {
        this.goodwillId = goodwillId;
    }

    public Long getMannId() {
        return mannId;
    }

    public void setMannId(Long mannId) {
        this.mannId = mannId;
    }
}
