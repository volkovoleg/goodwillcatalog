package ru.goodfil.catalog.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterForm.java 156 2013-07-03 09:22:29Z chezxxx@gmail.com $
 */
@Entity
public class FilterForm implements Unique, MannStatus {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(generator = "CustomGenerator")
    @GenericGenerator(name = "CustomGenerator", strategy = "ru.goodfil.catalog.utils.CustomIdentityGenerator")
    private Long id;

    @NotBlank
    @Index(name = "filterFormfilterTypeCodeI")
    private String filterTypeCode;

    @NotBlank
    private String name;

    @NotNull
    private Boolean aParam;

    @NotNull
    private Boolean bParam;

    @NotNull
    private Boolean cParam;

    @NotNull
    private Boolean dParam;

    @NotNull
    private Boolean eParam;

    @NotNull
    private Boolean fParam;

    @NotNull
    private Boolean gParam;

    @NotNull
    private Boolean hParam;

    @NotNull
    private Boolean bpParam;

    @NotNull
    private Boolean nrParam;

    private Integer mannStatus;

    private String image;

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
    }

    public Boolean getaParam() {
        return aParam;
    }

    public void setaParam(Boolean aParam) {
        this.aParam = aParam;
    }

    public Boolean getbParam() {
        return bParam;
    }

    public void setbParam(Boolean bParam) {
        this.bParam = bParam;
    }

    public Boolean getcParam() {
        return cParam;
    }

    public void setcParam(Boolean cParam) {
        this.cParam = cParam;
    }

    public Boolean getdParam() {
        return dParam;
    }

    public void setdParam(Boolean dParam) {
        this.dParam = dParam;
    }

    public Boolean geteParam() {
        return eParam;
    }

    public void seteParam(Boolean eParam) {
        this.eParam = eParam;
    }

    public Boolean getfParam() {
        return fParam;
    }

    public void setfParam(Boolean fParam) {
        this.fParam = fParam;
    }

    public Boolean getgParam() {
        return gParam;
    }

    public void setgParam(Boolean gParam) {
        this.gParam = gParam;
    }

    public Boolean gethParam() {
        return hParam;
    }

    public void sethParam(Boolean hParam) {
        this.hParam = hParam;
    }

    public Boolean getBpParam() {
        return bpParam;
    }

    public void setBpParam(Boolean bpParam) {
        this.bpParam = bpParam;
    }

    public Boolean getNrParam() {
        return nrParam;
    }

    public void setNrParam(Boolean nrParam) {
        this.nrParam = nrParam;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static FilterForm create(final @NotNull @NotBlank String filterTypeCode) {
        FilterForm filterForm = new FilterForm();
        filterForm.setFilterTypeCode(filterTypeCode);
        filterForm.setaParam(false);
        filterForm.setbParam(false);
        filterForm.setcParam(false);
        filterForm.setdParam(false);
        filterForm.seteParam(false);
        filterForm.setfParam(false);
        filterForm.setgParam(false);
        filterForm.sethParam(false);
        filterForm.setBpParam(false);
        filterForm.setNrParam(false);
        filterForm.setMannStatus(new Integer(0));
        filterForm.setImage("");
        return filterForm;
    }

    public static FilterForm create(final @NotNull @NotBlank String filterTypeCode,
                             final boolean a,
                             final boolean b,
                             final boolean c,
                             final boolean d,
                             final boolean e,
                             final boolean f,
                             final boolean g,
                             final boolean h,
                             final boolean pb,
                             final boolean nr) {
        FilterForm filterForm = new FilterForm();
        filterForm.setFilterTypeCode(filterTypeCode);
        filterForm.setaParam(a);
        filterForm.setbParam(b);
        filterForm.setcParam(c);
        filterForm.setdParam(d);
        filterForm.seteParam(e);
        filterForm.setfParam(f);
        filterForm.setgParam(g);
        filterForm.sethParam(h);
        filterForm.setBpParam(pb);
        filterForm.setNrParam(nr);
        filterForm.setMannStatus(new Integer(0));
        return filterForm;
    }
}
