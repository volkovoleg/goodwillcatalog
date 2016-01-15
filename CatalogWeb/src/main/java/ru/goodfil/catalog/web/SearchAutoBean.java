package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Clear;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.domain.Seria;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.domain.parts.FilterRelativeMotor;
import ru.goodfil.catalog.export.domain.fullexport.FullCatalogExportParams;
import ru.goodfil.catalog.export.domain.fullexport.FullExportDocumentModel;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.reporting.FullCatalogExporter;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.web.utils.*;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SearchAutoBean.java 178 2014-07-10 13:25:32Z chezxxx@gmail.com $
 */
@Managed
@ManagedBean
public class SearchAutoBean extends PageBean {
    private static final String FILTER_PAGE = "/filter";
    private static final String FILTER_BEAN = "FilterBean";

    @NotNull
    @Inject
    private CarsService carsService;

    @NotNull
    @Inject
    private FiltersService filtersService;

    @NotNull
    @Inject
    private ExportService exportService;

    private final JSFComboboxModel vechicleTypes = new JSFComboboxModel();
    private final JSFComboboxModel manufactors = new JSFComboboxModel();
    private final JSFComboboxModel series = new JSFComboboxModel();

  //  private final ExportToExcel exportToExcel = new ExportToExcel();

    private final List<Motor> motors = new ArrayList<Motor>();
    private Long selectedMotorId;

    private final List<FilterRelativeMotor> filters = new ArrayList<FilterRelativeMotor>();
    private Long selectedFilterId;

    private int page;

    public SearchAutoBean() {
        init();
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
        vechicleTypes.setItems(asSelectItems(filterVechicleTypesByGoodwillBrand(carsService.getVechicleTypes())), true);
        page = 1;
    }

    @Clear
    @Logged
    public void clear() {

    }

    private List<Manufactor> filterManufactorsByGoodwillBrand(List<Manufactor> source) {
        List<Manufactor> result = new ArrayList<Manufactor>();
        for (Manufactor manufactor : source) {
            if (manufactor.getOnSite()) {
                result.add(manufactor);
            }
        }
        return result;
    }

    private List<Seria> filterSeriesByGoodwillBrand(List<Seria> source) {
        List<Seria> result = new ArrayList<Seria>();
        for (Seria seria : source) {
            if (seria.getOnSite()) {
                result.add(seria);
            }
        }
        return result;
    }

    private List<Motor> filterMotorsByGoodwillBrand(List<Motor> source) {
        List<Motor> result = new ArrayList<Motor>();
        for (Motor motor : source) {
            if (motor.getOnSite()) {
                result.add(motor);
            }
        }
        return result;
    }

    private List<Filter> filterFiltersByGoodwillBrand(List<Filter> source) {
        List<Filter> result = new ArrayList<Filter>();
        for (Filter filter : source) {
            if (filter.getOnSite()) {
                result.add(filter);
            }
        }
        return result;
    }

    private List<VechicleType> filterVechicleTypesByGoodwillBrand(List<VechicleType> vechicleTypes) {
        List<VechicleType> result = new ArrayList<VechicleType>();
        for (VechicleType vechicleType : vechicleTypes) {
            if (vechicleType.getOnSite()) {
                result.add(vechicleType);
            }
        }
        return result;
    }

    @PageAction
    public void updateMarks() {
        manufactors.clear();
        series.clear();
        motors.clear();
        filters.clear();

        Long selectedVechicleTypeId = vechicleTypes.getSelectedValueAsLong();
        if (selectedVechicleTypeId != null) {
            List<javax.faces.model.SelectItem> manufactorsSelectItems = asSelectItems(filterManufactorsByGoodwillBrand(carsService.getManufactorsByVechicleTypeId(selectedVechicleTypeId)));
            manufactors.setItems(manufactorsSelectItems, true);
            series.clear();

            if (manufactorsSelectItems.size() == 1) {
                manufactors.setSelectedValue((Long) manufactorsSelectItems.get(0).getValue());
                updateSeries();
            }
        }
    }

    @PageAction
    public void exportByVechicleTypes() {
        Long selectedVechicleTypeId = vechicleTypes.getSelectedValueAsLong();
        if (selectedVechicleTypeId != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            System.out.println("Began at " + sdf.format(Calendar.getInstance().getTime()));

            Map<String, String> columns = new LinkedHashMap<String, String>();
            for (FilterType filterType : carsService.getFilterTypes()) {
                columns.put(filterType.getCode(), filterType.getName());
            }

            try {
                File f = File.createTempFile("goodwill", "catalog");
                FullExportDocumentModel fullExportDocumentModel = exportService.getByVechicleTypeId(selectedVechicleTypeId, new FullCatalogExportParams());
                FullCatalogExporter.export(fullExportDocumentModel, columns, f);
                System.out.println("Finished at " + sdf.format(Calendar.getInstance().getTime()));

                exportFile(f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    private void exportFile(File tempFile) throws IOException {
        String uid = UUID.randomUUID().toString();
        HttpServletRequest request = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();

        String link = FilePublisher.retrieveFile(request, tempFile.getAbsolutePath(), uid + ".xls");

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.sendRedirect(link);
    }

    @PageAction
    public void updateSeries() {
        series.clear();
        motors.clear();
        filters.clear();

        Long selectedManufactorId = manufactors.getSelectedValueAsLong();
        if (selectedManufactorId != null) {

            List<javax.faces.model.SelectItem> seriesSelectItems = asSelectItems(filterSeriesByGoodwillBrand(carsService.getSeriesByManufactorId(selectedManufactorId)));
            series.setItems(seriesSelectItems, true);

            if (seriesSelectItems.size() == 1) {
                series.setSelectedValue((Long) seriesSelectItems.get(0).getValue());
                updateMotors();
            }
        }
    }

    @PageAction
    public void updateMotors() {
        motors.clear();
        filters.clear();
        setPage(1);
        Long selectedSeriaId = series.getSelectedValueAsLong();
        if (selectedSeriaId != null) {
            List<Motor> nonEmptyMotors = new ArrayList<Motor>();
            nonEmptyMotors.addAll(getNotEmptyMotors(filterMotorsByGoodwillBrand(carsService.getMotorsBySeriaId(selectedSeriaId))));
            if (nonEmptyMotors.size() > 0) {
                motors.addAll(nonEmptyMotors);
            }
        }
    }

    @Logged
    public void updateFilters() {
        filters.clear();
        if (selectedMotorId != null) {
            Set<Long> filterIds = new HashSet<Long>();

            List<Filter> list = filterFiltersByGoodwillBrand(filtersService.getFiltersByGlobalVechicleTypeId(vechicleTypes.getSelectedValueAsLong()));
            for (Filter filter : list) {
                if (!filterIds.contains(filter.getId())) {
                    filterIds.add(filter.getId());
                    filters.add(carsService.getRelationToFilter(filter.getId(),selectedMotorId));
                }
            }

            list = filterFiltersByGoodwillBrand(filtersService.getFiltersByMotorId(selectedMotorId));
            for (Filter filter : list) {
                if (!filterIds.contains(filter.getId())) {
                    filterIds.add(filter.getId());
                    filters.add(carsService.getRelationToFilter(filter.getId(),selectedMotorId));
                }
            }
        }
        setPage(page);
    }

    public List<Manufactor> getNotEmptyManufactor(List<Manufactor> manufactorList){
        List<Manufactor> nonEmptyManufactors = new ArrayList<Manufactor>();
        List<Manufactor> manufactors = new ArrayList<Manufactor>();
        manufactors.addAll(manufactorList);
        List<Seria> nonEmptySeries = new ArrayList<Seria>();
        for(Manufactor manufactor:manufactors){
           nonEmptySeries.addAll(getNotEmptySeries(filterSeriesByGoodwillBrand(carsService.getSeriesByManufactorId(manufactor.getId()))));
            if(nonEmptySeries.size()>0){
               nonEmptyManufactors.add(manufactor);
            }
            nonEmptySeries.clear();
        }
        return nonEmptyManufactors;
    }

    public List<Seria> getNotEmptySeries(List<Seria> seriaList){
        List<Seria> nonEmptySeries = new ArrayList<Seria>();
        List<Seria> series = new ArrayList<Seria>();
        series.addAll(seriaList);
        List<Motor> nonEmptyMotors = new ArrayList<Motor>();
        for (Seria seria : series) {
            nonEmptyMotors.addAll(getNotEmptyMotors(filterMotorsByGoodwillBrand(carsService.getMotorsBySeriaId(seria.getId()))));
            if (nonEmptyMotors.size() > 0) {
                nonEmptySeries.add(seria);
            }
            nonEmptyMotors.clear();
        }
        return nonEmptySeries;
    }

    public List<Motor> getNotEmptyMotors(List<Motor> motorList){
        List<Motor> nonEmptyMotors = new ArrayList<Motor>();
        List<Motor> motors = new ArrayList<Motor>();
        motors.addAll(motorList);
        List<Filter> filterList = new ArrayList<Filter>();
        for (Motor motor : motors) {
            filterList.addAll(filterFiltersByGoodwillBrand(filtersService.getFiltersByMotorId(motor.getId())));
            if (filterList.size() > 0) {
                nonEmptyMotors.add(motor);
            }
            filterList.clear();
        }
        return nonEmptyMotors;
    }

    @PageAction
    @Logged
    public void showFoto() {
        Assert.notNull(selectedFilterId);
        EventHelper.init(FILTER_BEAN, selectedFilterId, "info");
        FacesUtils.redirect(FILTER_PAGE);
    }

    @PageAction
    @Logged
    public void showApplication() {
        Assert.notNull(selectedFilterId);
        EventHelper.init(FILTER_BEAN, selectedFilterId, "appliance");
        FacesUtils.redirect(FILTER_PAGE);

    }

    @PageAction
    @Logged
    public void showAnalogs() {
        Assert.notNull(selectedFilterId);
        EventHelper.init(FILTER_BEAN, selectedFilterId, "oes");
        FacesUtils.redirect(FILTER_PAGE);
    }

    /**
     * Возвращает true, если приложение запущено в режиме отчуждаемой копии.
     * Т.к. только в этом режиме доступны выгрузки в Excel.
     */
    public boolean getIsStandaloneVersion() {
        String catalogStandalone = System.getProperty("catalog.standalone");
        return "1".equals(catalogStandalone);
    }

    public JSFComboboxModel getVechicleTypes() {
        return i18n(vechicleTypes);
    }

    public JSFComboboxModel getManufactors() {
        return i18n(manufactors);
    }

    public JSFComboboxModel getSeries() {
        return i18n(series);
    }

    public List<Motor> getMotors() {
        return motors;
    }

    public List<FilterRelativeMotor> getFilters() {
        return filters;
    }

    public CarsService getCarsService() {
        return carsService;
    }

    public void setCarsService(CarsService carsService) {
        this.carsService = carsService;
    }

    public Long getSelectedMotorId() {
        return selectedMotorId;
    }

    public void setSelectedMotorId(Long selectedMotorId) {
        this.selectedMotorId = selectedMotorId;
    }

    public Long getSelectedFilterId() {
        return selectedFilterId;
    }

    public void setSelectedFilterId(Long seletedFilterId) {
        this.selectedFilterId = seletedFilterId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
    public ExportToExcel getExportToExcel() {
        return exportToExcel;
    }

/*
    public class ExportToExcel {
        private final FullCatalogExportParams params = new FullCatalogExportParams();
        private final List<FilterType> filterTypes;

        public ExportToExcel() {
            filterTypes = carsService.getFilterTypes();
            final Set<Long> filterTypesIds = new HashSet<Long>();
            for (FilterType filterType : filterTypes) {
                filterTypesIds.add(filterType.getId());
            }

            params.setGoodwillOnly(true);
            params.setIgnoreEmptyRows(true);
            params.getFilterTypes().addAll(filterTypesIds);
        }


        public void exportVechicleType() throws IOException {
            System.gc();
            FullExportDocumentModel fullExportDocument = exportService.getByVechicleTypeId(vechicleTypes.getSelectedValueAsLong(), params);
            File f = exportDocumentModel(fullExportDocument);

            FacesUtils.download(new FileInputStream(f));
            System.gc();
        }

        public void exportManufactor() throws IOException {
            System.gc();
            FullExportDocumentModel fullExportDocument = exportService.getByManufactorId(manufactors.getSelectedValueAsLong(), params);
            File f = exportDocumentModel(fullExportDocument);

            FacesUtils.download(new FileInputStream(f));
            System.gc();
        }

        public void exportSeria() throws IOException {
            System.gc();
            FullExportDocumentModel fullExportDocument = exportService.getBySeriaId(series.getSelectedValueAsLong(), params);
            File f = exportDocumentModel(fullExportDocument);

            FacesUtils.download(new FileInputStream(f));
            System.gc();
        }

        private File exportDocumentModel(FullExportDocumentModel fullExportDocumentModel) throws IOException {
            Map<String, String> columns = new HashMap<String, String>();
            for (FilterType filterType : filterTypes) {
                columns.put(filterType.getCode(), filterType.getName());
            }

            File f = File.createTempFile("goodwill", "catalog");
            FullCatalogExporter.export(fullExportDocumentModel, columns, f);
            return f;
        }
    }
      */
}
