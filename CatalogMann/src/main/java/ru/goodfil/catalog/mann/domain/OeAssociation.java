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
public class OeAssociation {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long goodwillId;

    @Column
    private Long mitkz;

    @Column
    private Long mpdsl;

    @Column
    private Long lfdnr;

    public Long getGoodwillId() {
        return goodwillId;
    }

    public void setGoodwillId(Long goodwillId) {
        this.goodwillId = goodwillId;
    }

    public Long getMitkz() {
        return mitkz;
    }

    public void setMitkz(Long mitkz) {
        this.mitkz = mitkz;
    }

    public Long getMpdsl() {
        return mpdsl;
    }

    public void setMpdsl(Long mpdsl) {
        this.mpdsl = mpdsl;
    }

    public Long getLfdnr() {
        return lfdnr;
    }

    public void setLfdnr(Long lfdnr) {
        this.lfdnr = lfdnr;
    }
}

