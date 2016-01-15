package ru.goodfil.catalog.mann.domain;

import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author sazonovkirill@gmail.com
 */
@Entity
public class ManufactorAssociation {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long goodwillId;

    /**
     * Тип ТС
     */
    @Column
    private Long fzasl;

    /**
     * Производитель
     */
    @Column
    private Long mrksl;

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

    public Long getFzasl() {
        return fzasl;
    }

    public void setFzasl(Long fzasl) {
        this.fzasl = fzasl;
    }

    public Long getrMksl() {
        return mrksl;
    }

    public void setMrksl(Long mrksl) {
        this.mrksl = mrksl;
    }
}
