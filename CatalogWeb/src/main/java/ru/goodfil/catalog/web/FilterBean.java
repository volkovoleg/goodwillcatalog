package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Clear;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.ListAsMap;
import ru.goodfil.catalog.web.utils.FacesUtils;
import ru.goodfil.catalog.web.utils.JSFComboboxModel;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.faces.model.SelectItem;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterBean.java 161 2013-08-07 12:30:13Z chezxxx@gmail.com $
 */
@Managed
@ManagedBean
public class FilterBean extends PageBean {
    @Inject
    @NotNull
    private FiltersService filtersService;

    @Inject
    @NotNull
    private CarsService carsService;

    @Inject
    @NotNull
    private AnalogsService analogsService;

    @NotNull
    private Filter filter;

    @NotNull
    private FilterForm filterForm;

    @NotNull
    private final List<MotorModel> motorModels = new ArrayList<MotorModel>();

    @NotNull
    private final List<MotorModel> selectedMotorModels = new ArrayList<MotorModel>();

    @NotNull
    private final List<Oe> oes = new ArrayList<Oe>();

    @NotNull
    private final List<Oe> selectedOes = new ArrayList<Oe>();

    @NotNull
    private final JSFComboboxModel manufactors = new JSFComboboxModel();

    @NotNull
    private final JSFComboboxModel series = new JSFComboboxModel();

    @NotNull
    private final JSFComboboxModel brands = new JSFComboboxModel();

    private Object selectedTab;

    private final List<ManufactorModel> manufactorModels = new ArrayList<ManufactorModel>();

    private final List<SeriaModel> seriaModels = new ArrayList<SeriaModel>();

    public List<SelectItem> getGlobalVechicleTypes() {
        String locale = languageBean.getLocale();

        if (getGlobalVechicleTypeAssigmentEnabled()) {
            Map<Long, VechicleType> vechicleTypes = ListAsMap.get(carsService.getVechicleTypes());

            List<SelectItem> result = new ArrayList<SelectItem>();
            if (filter.getApplyToAll_VT1()) {
                VechicleType vt = vechicleTypes.get(VechicleType.CARS);
                if (vt != null) {
                    String label = i18ManagedBean.get(vt.toString(), locale);
                    result.add(new SelectItem(vt.getId(), label));
                }
            }
            if (filter.getApplyToAll_VT2()) {
                VechicleType vt = vechicleTypes.get(VechicleType.TRUCKS);
                if (vt != null) {
                    String label = i18ManagedBean.get(vt.toString(), locale);
                    result.add(new SelectItem(vt.getId(), label));
                }
            }
            if (filter.getApplyToAll_VT3()) {
                VechicleType vt = vechicleTypes.get(VechicleType.SPECIAL);
                if (vt != null) {
                    String label = i18ManagedBean.get(vt.toString(), locale);
                    result.add(new SelectItem(vt.getId(), label));
                }
            }
            if (filter.getApplyToAll_VT4()) {
                VechicleType vt = vechicleTypes.get(VechicleType.BIKES);
                if (vt != null) {
                    String label = i18ManagedBean.get(vt.toString(), locale);
                    result.add(new SelectItem(vt.getId(), label));
                }
            }

            return result;
        } else {
            return null;
        }
    }

    /**
     * Возвращает true, если текущий фильтр привязан к хотя бы одному типу транспортного средства через механизм глобальных привязок.
     */
    public boolean getGlobalVechicleTypeAssigmentEnabled() {
        //Assert.notNull(filter);
        if(filter==null){return false;}

        return filter.getApplyToAll_VT1() == Boolean.TRUE ||
                filter.getApplyToAll_VT2() == Boolean.TRUE ||
                filter.getApplyToAll_VT3() == Boolean.TRUE ||
                filter.getApplyToAll_VT4() == Boolean.TRUE;
    }

    /**
     * Возвращает true, если текущий фильтр не привязан ни к одному типу транспортного средства через механизм глобальных привязок.
     */
    public boolean getGlobalVechicleTypeAssigmentDisabled() {
        //Assert.notNull(filter);
        if(filter==null){return false;}
        return filter.getApplyToAll_VT1() == Boolean.FALSE &&
                filter.getApplyToAll_VT2() == Boolean.FALSE &&
                filter.getApplyToAll_VT3() == Boolean.FALSE &&
                filter.getApplyToAll_VT4() == Boolean.FALSE;
    }

    @Init
    @Logged
    @ValidateAfter
    public void init(@NotNull Long filterId, String tabName) {
        this.filter = filtersService.getFilterById(filterId);
        Assert.notNull(filter);
        this.filterForm = carsService.getFilterFormById(this.filter.getFilterFormId());
        Assert.notNull(filterForm);

        manufactorModels.clear();
        seriaModels.clear();

        selectedMotorModels.clear();
        selectedOes.clear();

        //
        //  Загружаем применимость
        //

        //  Применимость нужно загружать только в том случае, если не указана глобальная привязка
        if (filter.getApplyToAll_VT1() != Boolean.TRUE &&
                filter.getApplyToAll_VT2() != Boolean.TRUE &&
                filter.getApplyToAll_VT3() != Boolean.TRUE &&
                filter.getApplyToAll_VT4() != Boolean.TRUE) {
            SeriaModelAdapter seriaModelAdapter = new SeriaModelAdapter();
            ManufactorModelAdapter manufactorModelAdapter = new ManufactorModelAdapter();

            {
                motorModels.clear();
                List<Motor> motors = filtersService.getMotorsByFilterId(filterId);
                Map<Long, Motor> motorsMap = new HashMap<Long, Motor>();
                Set<Long> seriesIds = new HashSet<Long>();
                for (Motor motor : motors) {
                    seriesIds.add(motor.getSeriaId());
                    motorsMap.put(motor.getId(), motor);
                }

                List<Seria> series = carsService.getSeriesByIds(seriesIds);
                Map<Long, Seria> seriesMap = new HashMap<Long, Seria>();
                Set<Long> manufactorsIds = new HashSet<Long>();
                for (Seria seria : series) {
                    manufactorsIds.add(seria.getManufactorId());
                    seriesMap.put(seria.getId(), seria);
                }

                List<Manufactor> manufactos = carsService.getManufactorsByIds(manufactorsIds);
                Map<Long, Manufactor> manufactorsMap = new HashMap<Long, Manufactor>();
                for (Manufactor manufactor : manufactos) {
                    manufactorsMap.put(manufactor.getId(), manufactor);
                }

                for (Motor motor : motors) {
                    Seria seria = seriesMap.get(motor.getSeriaId());
                    if(seria!=null){
                    //Assert.notNull(seria);

                    seriaModelAdapter.addSeria(seria);

                    Manufactor manufactor = manufactorsMap.get(seria.getManufactorId());
                    Assert.notNull(manufactor);

                    manufactorModelAdapter.addManufactor(manufactor);

                    MotorModel motorModel = new MotorModel();
                    motorModel.setMotorId(motor.getId());
                    motorModel.setSeriaId(seria.getId());
                    motorModel.setManufactorId(manufactor.getId());
                    motorModel.setMotor(motor.getName());
                    motorModel.setEngine(motor.getEngine());
                    motorModel.setSeria(seria.getName());
                    motorModel.setManufcator(manufactor.getName());
                    motorModel.setDateF(motor.getDateF());
                    motorModel.setDateT(motor.getDateT());
                        motorModel.setkW(motor.getKw());
                        motorModel.sethP(motor.getHp());
                    motorModels.add(motorModel);
                    }
                }
            }

            manufactorModels.addAll(manufactorModelAdapter.getList());
            seriaModels.addAll(seriaModelAdapter.getList());

            final List<SelectItem> manufactorsSelectItems = new ArrayList<SelectItem>();
            for (ManufactorModel manufactorModel : manufactorModels) {
                manufactorsSelectItems.add(new SelectItem(manufactorModels.indexOf(manufactorModel), manufactorModel.getName()));
            }

            final List<SelectItem> seriesSelectItems = new ArrayList<SelectItem>();
            for (SeriaModel seriaModel : seriaModels) {
                seriesSelectItems.add(new SelectItem(seriaModels.indexOf(seriaModel), seriaModel.getName()));
            }

            Collections.sort(seriesSelectItems, new BeanComparator("label"));
            series.setItems(seriesSelectItems, true);

            Collections.sort(manufactorsSelectItems, new BeanComparator("label"));
            manufactors.setItems(manufactorsSelectItems, true);

            if (manufactorsSelectItems.size() == 1) {
                manufactors.setSelectedValue(manufactorsSelectItems.get(0).getValue());
                updateSeries();
            }

            if (seriesSelectItems.size() == 1) {
                series.setSelectedValue(seriesSelectItems.get(0).getValue());
                updateMotors();
            }
        }


        //
        //  Загружаем аналоги
        //
        oes.clear();
        oes.addAll(filtersService.getOesByFilter(filterId));

        Set<Long> brandsIds = new HashSet<Long>();

        List<SelectItem> brandsSelectItems = new ArrayList<SelectItem>();
        for (Oe oe : oes) {
            brandsIds.add(oe.getBrandId());
        }

        List<Brand> brands = analogsService.getBrandsByIds(brandsIds);
        for (Brand brand : brands) {
            brandsSelectItems.add(new SelectItem(brand.getId(), brand.getName()));
        }

        Collections.sort(brandsSelectItems, new BeanComparator("label"));
        this.brands.setItems(brandsSelectItems);

        if (brandsSelectItems.size() >= 1) {
            this.brands.setSelectedValue(brandsSelectItems.get(0).getValue());
            updateOes();
        }

        //
        //  Открываем нужную вкладку
        //
        selectedTab = tabName;
    }

    @Clear
    @Logged
    public void clear() {
        this.filter = null;
        this.filterForm = null;
        this.motorModels.clear();
    }

    @Logged
    @PageAction
    public void goBack() {
        FacesUtils.redirect("/main");
    }

    public String getCheckSelectedFilter() {
        if (filter == null) {
            throw new IllegalStateException("Filter is not selected, but filter page is requested");
        }
        return "";
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public FilterForm getFilterForm() {
        return filterForm;
    }

    public void setFilterForm(FilterForm filterForm) {
        this.filterForm = filterForm;
    }

    public void setSelectedTab(Object selectedTab) {
        this.selectedTab = selectedTab;
    }

    public Object getSelectedTab() {
        return selectedTab;
    }

    public FiltersService getFiltersService() {
        return filtersService;
    }

    public void setFiltersService(FiltersService filtersService) {
        this.filtersService = filtersService;
    }

    public CarsService getCarsService() {
        return carsService;
    }

    public void setCarsService(CarsService carsService) {
        this.carsService = carsService;
    }

    public JSFComboboxModel getManufactors() {
        return i18n(manufactors);
    }

    public JSFComboboxModel getSeries() {
        return i18n(series);
    }

    public List<Oe> getSelectedOes() {
        return selectedOes;
    }

    public JSFComboboxModel getBrands() {
        return brands;
    }

    @PageAction
    @Logged
    public void updateSeries() {
        series.clear();
        selectedMotorModels.clear();

        Long selectedId = manufactors.getSelectedValueAsLong();
        if (selectedId != null) {
            ManufactorModel manufactorModel = manufactorModels.get(selectedId.intValue());
            final List<SelectItem> seriesSelectItems = new ArrayList<SelectItem>();
            for (SeriaModel seria : seriaModels) {
                if (seria.containsManufactorsWithIds(manufactorModel.getManufactorIds())) {
                    seriesSelectItems.add(new SelectItem(seriaModels.indexOf(seria), seria.getName()));
                }
            }

            series.setItems(seriesSelectItems, true);

            if (seriesSelectItems.size() == 1) {
                series.setSelectedValue(seriesSelectItems.get(0).getValue());
            }

            updateMotors();
        }
    }

    @PageAction
    @Logged
    public void updateMotors() {
        selectedMotorModels.clear();
        Long selectedId = series.getSelectedValueAsLong();
        if (selectedId != null) {
            SeriaModel selectedSeriaModel = seriaModels.get(selectedId.intValue());
            Set<Long> localSeriesIds = selectedSeriaModel.getSeriaIds();
            Seria selectedSeria = null;
            for (Long seriaId : localSeriesIds) {
                Seria localSeria = carsService.getSeriaById(seriaId);
                if (localSeria.getName().equals(selectedSeriaModel.getName())) {
                    selectedSeria = localSeria;
                }
            }
            if (selectedSeria != null) {
            for (MotorModel motorModel : motorModels) {
                if (motorModel.getSeriaId() == selectedSeria.getId()) {
                    selectedMotorModels.add(motorModel);
                }
            }
            }
        }
    }

    @PageAction
    @Logged
    public void updateOes() {
        selectedOes.clear();
        for (Oe oe : oes) {
            if (oe.getBrandId().equals(brands.getSelectedValueAsLong())) {
                selectedOes.add(oe);
            }
        }
    }

    public List<MotorModel> getSelectedMotorModels() {
        return selectedMotorModels;
    }

    public AnalogsService getAnalogsService() {
        return analogsService;
    }

    public void setAnalogsService(AnalogsService analogsService) {
        this.analogsService = analogsService;
    }

    /**
     * Возвращает путь к изображению текущего фильтра.
     * Если имя изображения указано без расшрения - тогда подразумевается jpg.
     */
    public String getFilterImagePath() {
        Filter filter = getFilter();
        if (filter != null) {
            String image = filter.getImage();
            if (image.endsWith(".jpg") ||
                    image.endsWith(".gif") ||
                    image.endsWith(".png")) {
                return image.trim();
            } else {
                return image + ".jpg";
            }
        }
        return null;
    }

    @Managed
    public static class MotorModel {
        @NotNull
        private Long motorId;

        @NotNull
        private Long seriaId;

        @NotNull
        private Long manufactorId;

        @NotNull
//        @NotBlank
        private String motor;

        @NotNull
//        @NotBlank
        private String seria;

        @NotNull
//        @NotBlank
        private String manufcator;

        private String engine;
        private Date dateF;
        private Date dateT;

        private String kW;
        private String hP;
        
        public Long getMotorId() {
            return motorId;
        }

        public void setMotorId(@NotNull Long motorId) {
            this.motorId = motorId;
        }

        public Long getSeriaId() {
            return seriaId;
        }

        public void setSeriaId(@NotNull Long seriaId) {
            this.seriaId = seriaId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }

        public void setManufactorId(@NotNull Long manufactorId) {
            this.manufactorId = manufactorId;
        }

        public String getMotor() {
            return motor;
        }

        public void setMotor(@NotNull /*@NotBlank*/ String motor) {
            this.motor = motor;
        }

        public String getSeria() {
            return seria;
        }

        public void setSeria(@NotNull /*@NotBlank*/ String seria) {
            this.seria = seria;
        }

        public String getManufcator() {
            return manufcator;
        }

        public void setManufcator(@NotNull /*@NotBlank*/ String manufcator) {
            this.manufcator = manufcator;
        }

        public String getkW() {
            return kW;
        }

        public void setkW(String kW) {
            this.kW = kW;
        }

        public String gethP() {
            return hP;
        }

        public void sethP(String hP) {
            this.hP = hP;
        }

        public Date getDateF() {
            return dateF;
        }

        public void setDateF(Date dateF) {
            this.dateF = dateF;
        }

        public Date getDateT() {
            return dateT;
        }

        public void setDateT(Date dateT) {
            this.dateT = dateT;
        }

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }
    }

    @Managed
    public static class SeriaModelAdapter {
        private final Map<String, List<Seria>> series = new HashMap<String, List<Seria>>();

        public void addSeria(@NotNull final Seria seria) {
            String name = seria.getName();
            if (!series.containsKey(name)) {
                series.put(name, new ArrayList<Seria>());
            }
            series.get(name).add(seria);
        }

        public List<SeriaModel> getList() {
            List<SeriaModel> result = new ArrayList<SeriaModel>();
            for (String name : series.keySet()) {
                SeriaModel seriaModel = new SeriaModel(name);

                List<Seria> subitems = series.get(name);
                for (Seria subitem : subitems) {
                    seriaModel.addSubitem(subitem);
                }
                result.add(seriaModel);
            }

            Collections.sort(result, new Comparator<SeriaModel>() {
                @Override
                public int compare(SeriaModel o1, SeriaModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            return Collections.unmodifiableList(result);
        }
    }

    @Managed
    public static class ManufactorModelAdapter {
        private final Map<String, List<Manufactor>> manufactors = new HashMap<String, List<Manufactor>>();

        public void addManufactor(@NotNull final Manufactor manufactor) {
            String name = manufactor.getName();
            if (!manufactors.containsKey(name)) {
                manufactors.put(name, new ArrayList<Manufactor>());
            }
            manufactors.get(name).add(manufactor);
        }

        public List<ManufactorModel> getList() {
            List<ManufactorModel> result = new ArrayList<ManufactorModel>();
            for (String name : manufactors.keySet()) {
                ManufactorModel manufactorModel = new ManufactorModel(name);

                List<Manufactor> subitems = manufactors.get(name);
                for (Manufactor subitem : subitems) {
                    manufactorModel.addSubitem(subitem);
                }
                result.add(manufactorModel);
            }

            Collections.sort(result, new Comparator<ManufactorModel>() {
                @Override
                public int compare(ManufactorModel o1, ManufactorModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            return Collections.unmodifiableList(result);
        }
    }

    @Managed
    public static class ManufactorModel {
        private final String name;
        private final Set<VechicleTypeAssigment> subitems = new HashSet<VechicleTypeAssigment>();

        public ManufactorModel(@NotNull @NotBlank final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSubitem(@NotNull final Manufactor manufactor) {
            subitems.add(new VechicleTypeAssigment(manufactor.getVechicleTypeId(), manufactor.getId()));
        }

        public Set<VechicleTypeAssigment> getSubitems() {
            return Collections.unmodifiableSet(subitems);
        }

        public Set<Long> getManufactorIds() {
            Set<Long> result = new HashSet<Long>();
            for (VechicleTypeAssigment subitem : subitems) {
                result.add(subitem.getManufactorId());
            }
            return result;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Managed
    public static class SeriaModel {
        private final String name;
        private final Set<ManufactorAssigment> subitems = new HashSet<ManufactorAssigment>();

        public SeriaModel(@NotNull @NotBlank final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSubitem(@NotNull final Seria seria) {
            subitems.add(new ManufactorAssigment(seria.getId(), seria.getManufactorId()));
        }

        public Set<ManufactorAssigment> getSubitems() {
            return Collections.unmodifiableSet(subitems);
        }

        public boolean containsManufacorWithId(@NotNull final Long manufactorId) {
            for (ManufactorAssigment manufactorAssigment : subitems) {
                if (manufactorAssigment.getManufactorId().equals(manufactorId)) {
                    return true;
                }
            }
            return false;
        }

        public boolean containsManufactorsWithIds(@NotNull final Set<Long> manufactorIds) {
            for (ManufactorAssigment manufactorAssigment : subitems) {
                for (Long manufactorId : manufactorIds) {
                    if (manufactorAssigment.getManufactorId().equals(manufactorId)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public Set<Long> getSeriaIds() {
            Set<Long> result = new HashSet<Long>();
            for (ManufactorAssigment subitem : subitems) {
                result.add(subitem.getSeriaId());
            }
            return result;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Managed
    public static class VechicleTypeAssigment {
        private final Long vechicleTypeId;
        private final Long manufactorId;

        public VechicleTypeAssigment(@NotNull final Long vechicleTypeId, @NotNull final Long manufactorId) {
            this.vechicleTypeId = vechicleTypeId;
            this.manufactorId = manufactorId;
        }

        public Long getVechicleTypeId() {
            return vechicleTypeId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }
    }

    @Managed
    public static class ManufactorAssigment {
        private final Long manufactorId;
        private final Long seriaId;

        public ManufactorAssigment(@NotNull final Long seriaId, @NotNull final Long manufactorId) {
            this.seriaId = seriaId;
            this.manufactorId = manufactorId;
        }

        public Long getSeriaId() {
            return seriaId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }
    }
}
