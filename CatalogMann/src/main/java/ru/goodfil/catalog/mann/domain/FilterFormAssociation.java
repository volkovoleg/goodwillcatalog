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
public class FilterFormAssociation {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long goodwillId;

    @Column
    private Long grouppe;

    @Column
    private String pdartg;

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

    public Long getGrouppe() {
        return grouppe;
    }

    public void setGrouppe(Long grouppe) {
        this.grouppe = grouppe;
    }

    public String getPdartg() {
        return pdartg;
    }

    public void setPdartg(String pdartg) {
        this.pdartg = pdartg;
    }
}
