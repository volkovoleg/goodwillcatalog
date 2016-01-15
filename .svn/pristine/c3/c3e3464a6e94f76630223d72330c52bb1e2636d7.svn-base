package ru.goodfil.catalog.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Setting.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Entity
@Managed
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Setting implements Unique {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotBlank
    @Index(name = "settingNameIndex")
    private String name;

    @NotNull
    @NotBlank
    private String value;

    public static Setting create(@NotNull @NotBlank String name, @NotNull @NotBlank String value) {
        Setting setting = new Setting();
        setting.setName(name);
        setting.setValue(value);
        return setting;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull @NotBlank String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(@NotNull @NotBlank String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(@NotNull @NotBlank Long id) {
        this.id = id;
    }
}
