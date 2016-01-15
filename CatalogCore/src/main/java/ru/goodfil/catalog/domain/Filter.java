package ru.goodfil.catalog.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.domain.dict.FilterStatus;
import ru.goodfil.catalog.utils.SearchMask;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Filter.java 114 2013-01-02 08:50:04Z sazonovkirill $
 */
@Entity
public class Filter implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotBlank
    @Index(name = "filterTypeCodeIndex")
    private String filterTypeCode;

    @Index(name = "filterNI")
    @NotBlank
    private String name;

    @Index(name = "filterSNI")
    private String searchName;

    /**
     * Флаг показывает, что фильтр должен быть показан на сайте.
     */
    private Boolean onSite = false;

    /**
     * Флаг показывает, что фильтр применим ко всем легковым автомобилям.
     */
    @Index(name = "applyToAllVT1")
    private Boolean applyToAll_VT1 = false;

    /**
     * Флаг показывает, что фильтр применим ко всем грузовым автомобилям.
     */
    @Index(name = "applyToAllVT2")
    private Boolean applyToAll_VT2 = false;

    /**
     * Флаг показывает, что фильтр применим ко всей специальной технике.
     */
    @Index(name = "applyToAllVT3")
    private Boolean applyToAll_VT3 = false;

    /**
     * Флаг показывает, что фильтр применим ко всем катерам и мотоциклам.
     */
    @Index(name = "applyToAllVT4")
    private Boolean applyToAll_VT4 = false;

    private String ean;
    private String oe;
    private String image;

    private String aParam;
    private String bParam;
    private String cParam;
    private String dParam;
    private String eParam;
    private String fParam;
    private String gParam;
    private String hParam;
    private String pbParam;
    private String nrParam;
    private Integer mannStatus;
    private Integer status;
    public Integer getMannStatus() {
        return mannStatus;
    }

    public void setMannStatus(Integer mannStatus) {
        this.mannStatus = mannStatus;
    }

    @NotNull
    private Boolean disabled = false;

    @Index(name = "filterFormIdIndex")
    private Long filterFormId;

    public Integer getStatusId() {
        return status;
    }

    public void setStatusId(Integer statusId) {
        this.status = statusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilterTypeCode() {
        return filterTypeCode;
    }

    public void setFilterTypeCode(String filterTypeCode) {
        this.filterTypeCode = filterTypeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.searchName = SearchMask.mask(name);
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getOe() {
        return oe;
    }

    public void setOe(String oe) {
        this.oe = oe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getaParam() {
        return aParam;
    }

    public void setaParam(String aParam) {
        this.aParam = aParam;
    }

    public String getbParam() {
        return bParam;
    }

    public void setbParam(String bParam) {
        this.bParam = bParam;
    }

    public String getcParam() {
        return cParam;
    }

    public void setcParam(String cParam) {
        this.cParam = cParam;
    }

    public String getdParam() {
        return dParam;
    }

    public void setdParam(String dParam) {
        this.dParam = dParam;
    }

    public String geteParam() {
        return eParam;
    }

    public void seteParam(String eParam) {
        this.eParam = eParam;
    }

    public String getfParam() {
        return fParam;
    }

    public void setfParam(String fParam) {
        this.fParam = fParam;
    }

    public String getgParam() {
        return gParam;
    }

    public void setgParam(String gParam) {
        this.gParam = gParam;
    }

    public String gethParam() {
        return hParam;
    }

    public void sethParam(String hParam) {
        this.hParam = hParam;
    }

    public String getPbParam() {
        return pbParam;
    }

    public void setPbParam(String pbParam) {
        this.pbParam = pbParam;
    }

    public String getNrParam() {
        return nrParam;
    }

    public void setNrParam(String nrParam) {
        this.nrParam = nrParam;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getFilterFormId() {
        return filterFormId;
    }

    public void setFilterFormId(Long filterFormId) {
        this.filterFormId = filterFormId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getFilterStatusAsString(){
        return FilterStatus.asString(getStatusId());
    }

    public Boolean getOnSite() {
//        return onSite;
//        Safe implementation below:
        if (onSite == null) {
            onSite = false;
        }
        return onSite;
    }

    public void setOnSite(Boolean onSite) {
        if (onSite == null) {
            onSite = false;
        }
        this.onSite = onSite;
    }

    public Boolean getApplyToAll_VT1() {
        if (applyToAll_VT1 == null) {
            applyToAll_VT1 = false;
        }
        return applyToAll_VT1;
    }

    public void setApplyToAll_VT1(Boolean applyToAll_VT1) {
        if (applyToAll_VT1 == null) {
            applyToAll_VT1 = false;
        }
        this.applyToAll_VT1 = applyToAll_VT1;
    }

    public Boolean getApplyToAll_VT2() {
        if (applyToAll_VT2 == null) {
            applyToAll_VT2 = false;
        }
        return applyToAll_VT2;
    }

    public void setApplyToAll_VT2(Boolean applyToAll_VT2) {
        if (applyToAll_VT2 == null) {
            applyToAll_VT2 = false;
        }
        this.applyToAll_VT2 = applyToAll_VT2;
    }

    public Boolean getApplyToAll_VT3() {
        if (applyToAll_VT3 == null) {
            applyToAll_VT3 = false;
        }
        return applyToAll_VT3;
    }

    public void setApplyToAll_VT3(Boolean applyToAll_VT3) {
        if (applyToAll_VT3 == null) {
            applyToAll_VT3 = false;
        }
        this.applyToAll_VT3 = applyToAll_VT3;
    }

    public Boolean getApplyToAll_VT4() {
        if (applyToAll_VT4 == null) {
            applyToAll_VT4 = false;
        }
        return applyToAll_VT4;
    }

    public void setApplyToAll_VT4(Boolean applyToAll_VT4) {
        if (applyToAll_VT4 == null) {
            applyToAll_VT4 = false;
        }
        this.applyToAll_VT4 = applyToAll_VT4;
    }

    @Override
    public String toString() {
        return name;
    }
}
