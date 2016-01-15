package ru.goodfil.catalog.ui.models;

import com.google.inject.Inject;
import com.jgoodies.binding.list.ArrayListModel;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Manufactor;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.domain.Seria;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.swing.resolver.DefaultResolver;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CarsTabModel.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class CarsTabModel {
    private final CarsService carsService;
    private final FiltersService filtersService;

    private final ArrayListModel<VechicleType> vechicleTypes = new ArrayListModel<VechicleType>();
    private final ArrayListModel<Manufactor> manufactors = new ArrayListModel<Manufactor>();
    private final ArrayListModel<Seria> series = new ArrayListModel<Seria>();
    private final ArrayListModel<Motor> motors = new ArrayListModel<Motor>();
    private final ArrayListModel<Filter> filters = new ArrayListModel<Filter>();
    private final ArrayListModel<Filter> allFilters = new ArrayListModel<Filter>();
    private final ArrayListModel<FilterType> filterTypes = new ArrayListModel<FilterType>();

    private Long selectedVechicleTypeId;
    private Long selectedManufactorId;
    private Long selectedSeriaId;
    private Long selectedMotorId;
    private final Set<Long> selectedFilterIds = new HashSet<Long>();
    private final Set<Long> seletedAllFilterIds = new HashSet<Long>();

    private Resolver filterTypeResolver;

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

    private void reReadVechicleTypes() {
        vechicleTypes.clear();
        vechicleTypes.addAll(carsService.getVechicleTypes());
    }

    private void reReadFilterTypes() {
        filterTypes.clear();
        filterTypes.addAll(carsService.getFilterTypes());
    }

    private void reReadManufactors(Long vechicleTypeId) {
        manufactors.clear();
        manufactors.addAll(carsService.getManufactorsByVechicleTypeId(vechicleTypeId));
    }

    @Inject
    public CarsTabModel(CarsService carsService, FiltersService filtersService) {
        this.carsService = carsService;
        this.filtersService = filtersService;

        filterTypeResolver = createFilterTypeResolver();

        reReadVechicleTypes();
        reReadFilterTypes();
    }

    public void onVechicleTypeSelected() {
        reReadManufactors(getSelectedVechicleTypeId());
    }

    public void onManufactorSelected() {
        series.clear();
        series.addAll(carsService.getSeriesByManufactorId(getSelectedManufactorId()));
    }

    public void onSeriaSelected() {
        motors.clear();
        motors.addAll(carsService.getMotorsBySeriaId(getSelectedSeriaId()));
    }

    public void onMotorSelected() {
        filters.clear();
        filters.addAll(filtersService.getFiltersByMotorId(getSelectedMotorId()));
    }

    public void onFilterSearchAction(@NotNull @NotBlank String filterCode) {
        allFilters.clear();
        allFilters.addAll(filtersService.getFiltersByName(filterCode));
    }

//    public Object[][] getMotorsDataVector() {
//        Object[][] result = new Object[motors.size()][];
//        for (int i = 0; i < motors.size(); i++) {
//            Motor motor = motors.get(i);
//            result[i] = new Object[6];
//
//            result[i][0] = motor.getName();
//            result[i][1] = motor.getEngine();
//            result[i][2] = motor.getKw();
//            result[i][3] = motor.getHp();
//            result[i][4] = motor.getDateF();
//            result[i][5] = motor.getDateT();
//        }
//        return result;
//    }

    private String getFilterTypeNameByCode(String code) {
        for (FilterType filterType : filterTypes) {
            if (filterType.getCode().equals(code)) {
                return filterType.getName();
            }
        }
        return null;
    }

    public Object[][] getFiltersDataVector() {
        Object[][] result = new Object[filters.size()][];
        for (int i = 0; i < filters.size(); i++) {
            Filter filter = filters.get(i);

            result[i] = new Object[2];
            result[i][0] = filter.getName();
            result[i][1] = getFilterTypeNameByCode(filter.getFilterTypeCode());
        }
        return result;
    }

    public ArrayListModel<Filter> getFilters() {
        return filters;
    }

    public Object[][] getAllFiltersDataVector() {
        Object[][] result = new Object[allFilters.size()][];
        for (int i = 0; i < allFilters.size(); i++) {
            Filter filter = allFilters.get(i);

            result[i] = new Object[2];
            result[i][0] = filter.getName();
            result[i][1] = getFilterTypeNameByCode(filter.getFilterTypeCode());
        }
        return result;
    }

    public ArrayListModel<Filter> getAllFilters() {
        return allFilters;
    }

    public ArrayListModel<Motor> getMotors() {
        return motors;
    }

    public ArrayListModel<VechicleType> getVechicleTypes() {
        return vechicleTypes;
    }

    public ArrayListModel<Manufactor> getManufactors() {
        return manufactors;
    }

    public ArrayListModel<Seria> getSeries() {
        return series;
    }

    /**
     * Добавить тип транспортного средства.
     */
    public void doAddVechicleType(@NotNull @NotBlank final String vechicleTypeName) {
        VechicleType vechicleType = new VechicleType();
        vechicleType.setName(vechicleTypeName);

        carsService.addVechicleType(vechicleType);

        reReadVechicleTypes();

        this.selectedManufactorId = null;
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Удалить тип транспортного средства.
     */
    public void doRemoveVechicleType(@NotNull Long selectedVechicleTypeId) {
        carsService.removeVechicleType(selectedVechicleTypeId);

        reReadVechicleTypes();

        this.selectedManufactorId = null;
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Обновить тип транспортного средства.
     */
    public void doUpdateVechicleType(@NotNull @Valid VechicleType vechicleType) {
        carsService.updateVechicleType(vechicleType);

        reReadVechicleTypes();

        this.series.clear();
        this.motors.clear();
        this.filters.clear();
        this.selectedManufactorId = null;
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Добавить производителя.
     */
    public void doAddManufactor(@NotNull Long vechicleTypeId, @NotNull @NotBlank String name) {
        Manufactor manufactor = Manufactor.create(vechicleTypeId, name);

        carsService.addManufactor(manufactor);

        this.series.clear();
        this.motors.clear();
        this.filters.clear();
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Обновить производителя.
     */
    public void doUpdateManufactor(@NotNull @Valid Manufactor manufactor) {
        carsService.updateManufactor(manufactor);

        this.series.clear();
        this.motors.clear();
        this.filters.clear();
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Удалить производителя.
     */
    public void doRemoveManufactor(@NotNull Long selectedManufatorId) {
        carsService.removeManufactor(selectedManufatorId);

        this.series.clear();
        this.motors.clear();
        this.filters.clear();
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Добавить серию.
     */
    public void doAddSeria(@NotNull Long manufactorId, @NotNull @NotBlank String name) {
        Seria seria = Seria.createSeria(manufactorId, name);

        carsService.addSeria(seria);

        this.motors.clear();
        this.filters.clear();
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Обновить серию.
     */
    public void doUpdateSeria(@NotNull @Valid Seria seria) {
        carsService.updateSeria(seria);

        this.motors.clear();
        this.filters.clear();
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Удалить серию.
     */
    public void doRemoveSeria(@NotNull Long selectedSeriaId) {
        carsService.removeSeria(selectedSeriaId);

        this.motors.clear();
        this.filters.clear();
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();
    }

    /**
     * Добавление мотора.
     */
    public void doAddMotor(@NotNull @Valid Motor motor) {
        carsService.addMotor(motor);

        this.filters.clear();
        this.selectedFilterIds.clear();
    }

    /**
     * Обновление мотора.
     */
    public void doUpdateMotor(@NotNull @Valid Motor motor) {
        carsService.updateMotor(motor);

        this.filters.clear();
        this.selectedFilterIds.clear();
    }

    /**
     * Удаление мотора.
     */
    public void doRemoveMotor(@NotNull Long selectedMotorId) {
        carsService.removeMotor(selectedMotorId);

        this.filters.clear();
        this.selectedFilterIds.clear();
    }

    /**
     * Прикрепить фильтры.
     */
    public void doAttachFiltersToMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds) {
        carsService.doAttachFiltersToMotor(motorId, filterIds);
    }

    /**
     * Открепить фильтры.
     */
    public void doDetachFiltersFromMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds) {
        carsService.doDetachFiltersFromMotor(motorId, filterIds);
    }

    public Long getSelectedVechicleTypeId() {
        return selectedVechicleTypeId;
    }

    public void setSelectedVechicleTypeId(Long selectedVechicleTypeId) {
        this.selectedVechicleTypeId = selectedVechicleTypeId;
        this.selectedManufactorId = null;
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();

        this.manufactors.clear();
        this.series.clear();
        this.motors.clear();
        this.filters.clear();

        onVechicleTypeSelected();
    }

    public Long getSelectedManufactorId() {
        return selectedManufactorId;
    }

    public void setSelectedManufactorId(Long selectedManufactorId) {
        this.selectedManufactorId = selectedManufactorId;
        this.selectedSeriaId = null;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();

        this.series.clear();
        this.motors.clear();
        this.filters.clear();

        onManufactorSelected();
    }

    public Long getSelectedSeriaId() {
        return selectedSeriaId;
    }

    public void setSelectedSeriaId(Long selectedSeriaId) {
        this.selectedSeriaId = selectedSeriaId;
        this.selectedMotorId = null;
        this.selectedFilterIds.clear();

        this.motors.clear();
        this.filters.clear();

        onSeriaSelected();
    }

    public Long getSelectedMotorId() {
        return selectedMotorId;
    }

    public void setSelectedMotorId(Long selectedMotorId) {
        this.selectedMotorId = selectedMotorId;
        this.selectedFilterIds.clear();

        this.filters.clear();

        onMotorSelected();
    }

    public Resolver getFilterTypeResolver() {
        return filterTypeResolver;
    }
}
