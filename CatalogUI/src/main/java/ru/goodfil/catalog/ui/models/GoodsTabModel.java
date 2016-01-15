package ru.goodfil.catalog.ui.models;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.guice.CatalogModule;
import ru.goodfil.catalog.ui.swing.resolver.DefaultResolver;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;
import ru.goodfil.catalog.utils.Assert;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: GoodsTabModel.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class GoodsTabModel {
    private final CarsService carsService;
    private final FiltersService filtersService;

    private String filterSearch;

    private final Resolver filterTypeResolver;
    private final Resolver filterFormResolver;

    private final SelectionInList<Manufactor> manufactors;
    private final SelectionInList<Seria> series;

    private final ArrayListModel<Filter> filters = new ArrayListModel<Filter>();
    private final ArrayListModel<Motor> motors = new ArrayListModel<Motor>();
    private final ArrayListModel<Motor> allMotors = new ArrayListModel<Motor>();

    private final ArrayListModel<OeModel> oes = new ArrayListModel<OeModel>();
    private final ArrayListModel<OeModel> allOes = new ArrayListModel<OeModel>();

    private Long selectedFilterId;
    private Long selectedMotorId;
    private Long selectedAllMotorId;

    private Long selectedManufactorId;
    private Long selectedSeriaId;

    private Long selectedOeId;
    private Long selectedAllOeId;

    public GoodsTabModel() {
        Injector injector = Guice.createInjector(new CatalogModule());

        carsService = injector.getInstance(CarsService.class);
        filtersService = injector.getInstance(FiltersService.class);

        filterTypeResolver = createFilterTypeResolver();
        filterFormResolver = createFilterFormResolver();

        manufactors = new SelectionInList<Manufactor>(carsService.getAllManufactors());
        series = new SelectionInList<Seria>();
    }

    private void reReadManufactors() {
        manufactors.setList(carsService.getAllManufactors());
    }

    private void reReadSeries(Long manufactorId) {
        series.setList(carsService.getSeriesByManufactorId(manufactorId));
    }

    private void reReadAllMotors(Long seriaId) {
        allMotors.clear();
        allMotors.addAll(carsService.getMotorsBySeriaId(seriaId));
    }

    private void reReadMotors(Long filterId) {
        motors.clear();
        motors.addAll(filtersService.getMotorsByFilterId(filterId));
    }

    private void reReadOes(Long filterId) {
        List<Oe> oes = filtersService.getOesByFilter(filterId);
        Set<Long> brandsIds = new HashSet<Long>();
        for (Oe oe : oes) {
            brandsIds.add(oe.getBrandId());
        }
        List<Brand> brands = filtersService.getBrandsByIds(brandsIds);
        Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
        for (Brand brand : brands) {
            brandMap.put(brand.getId(), brand);
        }

        this.oes.clear();
        for (Oe oe : oes) {
            OeModel oeModel = new OeModel();
            oeModel.setOeId(oe.getId());
            oeModel.setOe(oe.getName());

            Brand brand = brandMap.get(oe.getBrandId());
            oeModel.setBrand(brand.getName());
            oeModel.setBrandId(brand.getId());

            this.oes.add(oeModel);
        }
    }

    private void reReadAllOes(String oeName) {
        List<Oe> oes = filtersService.getOesByName(oeName);
        Set<Long> brandsIds = new HashSet<Long>();
        for (Oe oe : oes) {
            brandsIds.add(oe.getBrandId());
        }
        List<Brand> brands = filtersService.getBrandsByIds(brandsIds);
        Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
        for (Brand brand : brands) {
            brandMap.put(brand.getId(), brand);
        }

        this.allOes.clear();
        for (Oe oe : oes) {
            OeModel oeModel = new OeModel();
            oeModel.setOeId(oe.getId());
            oeModel.setOe(oe.getName());

            Brand brand = brandMap.get(oe.getBrandId());
            oeModel.setBrand(brand.getName());
            oeModel.setBrandId(brand.getId());

            this.allOes.add(oeModel);
        }
    }

    private Resolver createFilterTypeResolver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "");
        map.put("null", "");
        List<FilterType> filterTypes = carsService.getFilterTypes();
        for (FilterType filterType : filterTypes) {
            map.put(filterType.getCode(), filterType.getName());
        }
        return new DefaultResolver(map);
    }

    private Resolver createFilterFormResolver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "");
        map.put("null", "");
        List<FilterForm> filterForms = carsService.getFilterForms();
        for (FilterForm filterForm : filterForms) {
            map.put(filterForm.getId().toString(), filterForm.getName());
        }
        return new DefaultResolver(map);
    }

    public void doAddFilter(Filter filter) {
        filtersService.saveFilter(filter);
        doSearchFilters();
    }

    public void doSearchFilters() {
        Assert.notBlank(filterSearch);

        filters.clear();
        filters.addAll(filtersService.getFiltersByName(filterSearch));
    }


    public void setFilterSearch(String filterSearch) {
        this.filterSearch = filterSearch;
    }

    public ArrayListModel<Filter> getFilters() {
        return filters;
    }

    public ArrayListModel<Motor> getAllMotors() {
        return allMotors;
    }

    public ArrayListModel<Motor> getMotors() {
        return motors;
    }

    public Resolver getFilterTypeResolver() {
        return filterTypeResolver;
    }

    public Resolver getFilterFormResolver() {
        return filterFormResolver;
    }

    public void doUpdateFilter(Filter filter) {
        filtersService.updateFilter(filter);
        doSearchFilters();
    }

    public void doDeleteFilter(Long filterId) {
        filtersService.deleteFilter(filterId);
        doSearchFilters();
    }

    public SelectionInList<Manufactor> getManufactors() {
        return manufactors;
    }

    public SelectionInList<Seria> getSeries() {
        return series;
    }

    public ArrayListModel<OeModel> getOes() {
        return oes;
    }

    public ArrayListModel<OeModel> getAllOes() {
        return allOes;
    }

    /**
     * При выборе производителя начитать список серий.
     *
     * @param manufactorId
     */
    public void onManufactorSelected(Long manufactorId) {
        reReadSeries(manufactorId);
    }

    public void onSeriaSelected(Long seriaId) {
        reReadAllMotors(seriaId);
    }


    public void onFilterSelected(Long filterId) {
        reReadMotors(filterId);
        reReadOes(filterId);
    }

    /**
     * Прикрепить мотор к фильтру.
     *
     * @param filterId идентификатор фильтра
     */
    public void doAttachMotorToFilter(Set<Long> motorIds, Long filterId) {
        carsService.doAttachMotorsToFilter(filterId, motorIds);
        reReadMotors(filterId);
    }

    /**
     * Открепить мотор от фильтра.
     *
     * @param filterId идентификатор фильтра
     */
    public void doDetachMotorFromFilter(Set<Long> motorIds, Long filterId) {
        carsService.doAttachMotorsToFilter(filterId, motorIds);
        reReadMotors(filterId);
    }

    public void doSearchOes(@NotNull @NotBlank String oeName) {
        reReadAllOes(oeName);
    }

    /**
     * Прикрепить ОЕ к фильтру.
     *
     * @param oeIds    идентификаторы ОЕ.
     * @param filterId идентификатор фильтра.
     */
    public void doAttachOesToFilter(@NotNull @NotEmpty Set<Long> oeIds, @NotNull Long filterId) {
        carsService.doAttachOesToFilter(filterId, oeIds);
        reReadOes(filterId);
    }

    /**
     * Открепить ОЕ от фильтра.
     *
     * @param oeIds    идентификаторы ОЕ.
     * @param filterId идентификтаор фильтра.
     */
    public void doDetachOesFromFitler(@NotNull @NotEmpty Set<Long> oeIds, @NotNull Long filterId) {
        carsService.doDetachOesFromFilter(filterId, oeIds);
        reReadOes(filterId);
    }

    public static class OeModel {
        private Long oeId;
        private Long brandId;
        private String oe;
        private String brand;

        public Long getOeId() {
            return oeId;
        }

        public void setOeId(Long oeId) {
            this.oeId = oeId;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getOe() {
            return oe;
        }

        public void setOe(String oe) {
            this.oe = oe;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

    public static class OeModelTableModel extends AbstractTableAdapter<OeModel> {
        public OeModelTableModel(ListModel listModel) {
            super(listModel, new String[]{
                    "ОЕ",
                    "Производитель"
            });
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            OeModel oe = getRow(rowIndex);
            if (columnIndex == 0) return oe.getOe();
            if (columnIndex == 1) return oe.getBrand();
            return null;
        }
    }
}
