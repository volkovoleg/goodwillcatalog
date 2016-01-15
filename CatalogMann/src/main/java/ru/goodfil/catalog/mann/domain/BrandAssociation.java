package ru.goodfil.catalog.mann.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: sazonovkirill@gmail.com
 * Date: 25.11.12
 */
@Entity
public class BrandAssociation {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column
    private Long goodwillId;

    @Column
    private Long mitnr;

    public Long getGoodwillId() {
        return goodwillId;
    }

    public void setGoodwillId(Long goodwillId) {
        this.goodwillId = goodwillId;
    }

    public Long getMitnr() {
        return mitnr;
    }

    public void setMitnr(Long mitnr) {
        this.mitnr = mitnr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
