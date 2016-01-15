package ru.goodfil.catalog.ui.models;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.guice.CatalogModule;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterWindowModel.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class FilterWindowModel {
    private CarsService carsService;
    private FiltersService filtersService;

    private final List<FilterForm> filterForms = new ArrayList<FilterForm>();
    private final List<FilterType> filterTypes = new ArrayList<FilterType>();

    private ValueHolder filterFormsSelection = new ValueHolder();
    private ValueHolder filterTypesSelection = new ValueHolder();

    private String name;
    private String ean;
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

    public FilterWindowModel() {
        Injector injector = Guice.createInjector(new CatalogModule());

        carsService = injector.getInstance(CarsService.class);
        filtersService = injector.getInstance(FiltersService.class);

        reReadFilterForms();
        reReadFilterTypes();
    }

    private void reReadFilterForms() {
        filterForms.clear();
        filterForms.addAll(carsService.getFilterForms());
    }

    private void reReadFilterTypes() {
        filterTypes.clear();
        filterTypes.addAll(carsService.getFilterTypes());
    }

    public List<FilterType> getFilterTypes() {
        return filterTypes;
    }

    public List<FilterForm> getFilterForms() {
        return filterForms;
    }

    public ValueModel getFilterTypesSelection() {
        return filterTypesSelection;
    }

    public ValueModel getFilterFormsSelection() {
        return filterFormsSelection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
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

    private ExtendedPropertyChangeSupport changeSupport = new ExtendedPropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }
}
