package ru.goodfil.catalog.services.impl;

import com.google.inject.Inject;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.adapters.*;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateBefore;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.domain.parts.FilterRelativeMotor;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.JoinOptions;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CarsServiceImpl.java 178 2014-07-10 13:25:32Z chezxxx@gmail.com $
 */
@Managed
public class CarsServiceImpl implements CarsService {
    @NotNull
    @Inject
    private VechicleTypeAdapter vechicleTypeAdapter;

    @NotNull
    @Inject
    private ManufactorAdapter manufactorAdapter;

    @NotNull
    @Inject
    private SeriaAdapter seriaAdapter;

    @NotNull
    @Inject
    private MotorAdapter motorAdapter;

    @NotNull
    @Inject
    private FilterTypeAdapter filterTypeAdapter;

    @NotNull
    @Inject
    private FiltersAndMotorsAdapter filtersAndMotorsAdapter;

    @NotNull
    @Inject
    private FilterFormAdapter filterFormAdapter;

    @NotNull
    @Inject
    private FiltersAndOesAdapter filtersAndOesAdapter;

    @NotNull
    @Inject
    private FilterAdapter filterAdapter;

    @Logged
    @ValidateBefore
    @Override
    public List<VechicleType> getVechicleTypes() {
        return vechicleTypeAdapter.getAllSorted("id");
    }

    @Logged
    @ValidateBefore
    @Override
    public List<FilterType> getFilterTypes() {
        return filterTypeAdapter.getAll();
    }

    @Override
    public FilterType getFilterTypeById(final Long filterTypeId) {
        return filterTypeAdapter.getById(filterTypeId);
    }

    @Override
    public FilterType getFilterTypeByCode(final String filterTypeCode) {
        return filterTypeAdapter.getByCode(filterTypeCode);
    }

    @Logged
    @ValidateBefore
    @Override
    public List<Manufactor> getManufactorsByVechicleTypeId(final Long vechicleTypeId) {
        return manufactorAdapter.getByVechicleTypeId(vechicleTypeId);
    }

    @Override
    public List<Manufactor> getAllManufactors() {
        return manufactorAdapter.getAll();
    }

    @Logged
    @ValidateBefore
    @Override
    public List<Seria> getSeriesByManufactorId(final Long manufactorId) {
        return seriaAdapter.getByManufatorId(manufactorId);
    }

    @Logged
    @ValidateBefore
    @Override
    public List<Motor> getMotorsBySeriaId(final Long seriaId) {
        return motorAdapter.getBySeriaId(seriaId);
    }

    @Logged
    @ValidateBefore
    @Override
    public List<FilterForm> getFilterFormsByFilterTypeCode(final String filterTypeCode) {
        return filterFormAdapter.getByFilterTypeCode(filterTypeCode);
    }

    @Override
    public List<FilterForm> getFilterForms() {
        return filterFormAdapter.getAll();
    }

    @Override
    public FilterForm getFilterFormById(final Long filterFormId) {
        return filterFormAdapter.getById(filterFormId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addVechicleType(final VechicleType vechicleType) {
        vechicleTypeAdapter.save(vechicleType);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeVechicleType(final Long venchicleTypeId) {
        vechicleTypeAdapter.delete(venchicleTypeId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addManufactor(final Manufactor manufactor) {
        manufactorAdapter.save(manufactor);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeManufactor(final Long manufactorId) {
        final List<Seria> seriesToDelete = new ArrayList<Seria>();
        final List<Motor> motorsToDelete = new ArrayList<Motor>();
        final List<FiltersAndMotors> filtersAndMotorsToDelete = new ArrayList<FiltersAndMotors>();
        seriesToDelete.addAll(seriaAdapter.getByManufatorId(manufactorId));
        for (Seria seria : seriesToDelete) {
            if (seria != null) {
                motorsToDelete.addAll(motorAdapter.getBySeriaId(seria.getId()));
            }
        }
        for (Motor motor : motorsToDelete) {
            if (motor != null) {
                filtersAndMotorsToDelete.addAll(filtersAndMotorsAdapter.getByMotorId(motor.getId()));
            }
        }
        for (FiltersAndMotors andMotors : filtersAndMotorsToDelete) {
            if (andMotors != null) {
                filtersAndMotorsAdapter.delete(andMotors.getId());
            }
        }
        for (Motor motor : motorsToDelete) {
            motorAdapter.softDelete(motor.getId());
        }
        for (Seria seria : seriesToDelete) {
            seriaAdapter.softDelete(seria.getId());
        }
        manufactorAdapter.softDelete(manufactorId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addSeria(final Seria seria) {
        seriaAdapter.save(seria);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeSeria(final Long seriaId) {
        final List<Motor> motorsToDelete = new ArrayList<Motor>();
        final List<FiltersAndMotors> filtersAndMotorsToDelete = new ArrayList<FiltersAndMotors>();
        motorsToDelete.addAll(motorAdapter.getBySeriaId(seriaId));
        for (Motor motor : motorsToDelete) {
            if (motor != null) {
                filtersAndMotorsToDelete.addAll(filtersAndMotorsAdapter.getByMotorId(motor.getId()));
            }
        }
        for (FiltersAndMotors andMotors : filtersAndMotorsToDelete) {
            if (andMotors != null) {
                filtersAndMotorsAdapter.delete(andMotors.getId());
            }
        }
        for (Motor motor : motorsToDelete) {
            motorAdapter.softDelete(motor.getId());
        }
        seriaAdapter.softDelete(seriaId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addMotor(final Motor motor) {
        motorAdapter.save(motor);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeMotor(final Long motorId) {
        motorAdapter.softDelete(motorId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void updateVechicleType(VechicleType vechicleType) {
        vechicleTypeAdapter.merge(vechicleType);
    }

    @Logged
    @ValidateBefore
    @Override
    public void updateManufactor(Manufactor manufactor) {
        manufactorAdapter.merge(manufactor);
    }

    @Logged
    @ValidateBefore
    @Override
    public void updateSeria(Seria seria) {
        seriaAdapter.merge(seria);
    }

    @Logged
    @ValidateBefore
    @Override
    public void updateMotor(Motor motor) {
        motorAdapter.merge(motor);
    }

    @Logged
    @ValidateBefore
    @Override
    public void doAttachFiltersToMotor(Long motorId, Set<Long> filterIds) {
        final List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByMotorId(motorId);

        final List<FiltersAndMotors> newFiltersAndMotorsList = new ArrayList<FiltersAndMotors>();
        for (Long filterId : filterIds) {
            boolean alreadyAttached = false;
            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
                if (filtersAndMotors.getFilterId().equals(filterId)) {
                    alreadyAttached = true;
                }
            }
            if (alreadyAttached) continue;

            FiltersAndMotors filtersAndMotors = FiltersAndMotors.create(motorId, filterId);
            newFiltersAndMotorsList.add(filtersAndMotors);
        }

        if (newFiltersAndMotorsList.size() > 0) {
            filtersAndMotorsAdapter.save(newFiltersAndMotorsList);
        }
    }

    @Logged
    @ValidateBefore
    @Override
    public void doDetachFiltersFromMotor(Long motorId, Set<Long> filterIds) {
        List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByMotorId(motorId);
        List<FiltersAndMotors> filtersAndMotorsToDeleteList = new ArrayList<FiltersAndMotors>();
        for (Long filterId : filterIds) {
            FiltersAndMotors filtersAndMotorsToDelete = null;
            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
                if (filtersAndMotors.getFilterId().equals(filterId)) {
                    filtersAndMotorsToDelete = filtersAndMotors;
                }
            }
            Assert.notNull(filtersAndMotorsToDelete);
            filtersAndMotorsToDeleteList.add(filtersAndMotorsToDelete);
        }

        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsToDeleteList) {
            filtersAndMotorsAdapter.delete(filtersAndMotors.getId());
        }
    }

    @Override
    public void doAttachMotorsToFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> motorsIds) {
        final List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByFilterId(filterId);

        final List<FiltersAndMotors> newFiltersAndMotorsList = new ArrayList<FiltersAndMotors>();
        for (Long motorId : motorsIds) {
            boolean alreadyAttached = false;
            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
                if (filtersAndMotors.getMotorId().equals(motorId)) {
                    alreadyAttached = true;
                }
            }
            if (alreadyAttached) continue;

            FiltersAndMotors filtersAndMotors = FiltersAndMotors.create(motorId, filterId);
            newFiltersAndMotorsList.add(filtersAndMotors);
        }

        if (newFiltersAndMotorsList.size() > 0) {
            filtersAndMotorsAdapter.save(newFiltersAndMotorsList);
        }
    }

    @Override
    public void doDetachMotorsFromFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> motorsIds) {
        List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByFilterId(filterId);
        List<FiltersAndMotors> filtersAndMotorsToDeleteList = new ArrayList<FiltersAndMotors>();
        for (Long motorId : motorsIds) {
            FiltersAndMotors filtersAndMotorsToDelete = null;
            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
                if (filtersAndMotors.getMotorId().equals(motorId)) {
                    filtersAndMotorsToDelete = filtersAndMotors;
                }
            }
            Assert.notNull(filtersAndMotorsToDelete);
            filtersAndMotorsToDeleteList.add(filtersAndMotorsToDelete);
        }

        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsToDeleteList) {
            filtersAndMotorsAdapter.delete(filtersAndMotors.getId());
        }
    }

    @Override
    public void doAttachOesToFilter(Long filterId, Set<Long> oeIds) {
        List<FiltersAndOes> filtersAndOesList = filtersAndOesAdapter.getByFilterId(filterId);

        List<FiltersAndOes> newFiltersAndOesList = new ArrayList<FiltersAndOes>();
        for (Long oeId : oeIds) {
            boolean alreadyAttached = false;
            for (FiltersAndOes filtersAndOes : filtersAndOesList) {
                if (filtersAndOes.getOeId().equals(oeId)) {
                    alreadyAttached = true;
                }
            }
            if (alreadyAttached) continue;

            FiltersAndOes filtersAndOes = FiltersAndOes.create(filterId, oeId);
            newFiltersAndOesList.add(filtersAndOes);
        }

        if (newFiltersAndOesList.size() > 0) {
            filtersAndOesAdapter.save(newFiltersAndOesList);
        }
    }

    @Override
    public void doDetachOesFromFilter(Long filterId, Set<Long> oeIds) {
        List<FiltersAndOes> filtersAndOesList = filtersAndOesAdapter.getByFilterId(filterId);
        List<FiltersAndOes> filtersAndOesToDeleteList = new ArrayList<FiltersAndOes>();
        for (Long oeId : oeIds) {
            FiltersAndOes filtersAndMotorsToDelete = null;
            for (FiltersAndOes filtersAndOes : filtersAndOesList) {
                if (filtersAndOes.getOeId().equals(oeId)) {
                    filtersAndMotorsToDelete = filtersAndOes;
                }
            }
            Assert.notNull(filtersAndMotorsToDelete);
            filtersAndOesToDeleteList.add(filtersAndMotorsToDelete);
        }

        for (FiltersAndOes filtersAndOes : filtersAndOesToDeleteList) {
            filtersAndOesAdapter.delete(filtersAndOes.getId());
        }
    }

    @Override
    public Seria getSeriaById(final Long seriaId) {
        return seriaAdapter.getById(seriaId);
    }

    @Override
    public Manufactor getManufactorById(final Long manufactorId) {
        return manufactorAdapter.getById(manufactorId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doJoinManufactors(final Long masterId, final Set<Long> slavesIds, final JoinOptions joinOptions) {
        List<Seria> series = new ArrayList<Seria>();

        if (joinOptions.isRecursiveJoinByName()) {
            // Объединяем дочерние серии

            series.addAll(seriaAdapter.getByManufatorId(masterId));
            for (Long manufactorId : slavesIds) {
                series.addAll(seriaAdapter.getByManufatorId(manufactorId));
            }

            Map<String, List<Seria>> seriesMap = new HashMap<String, List<Seria>>();
            for (Seria seria : series) {
                if (!seriesMap.containsKey(seria.getName())) {
                    seriesMap.put(seria.getName(), new ArrayList<Seria>());
                }
                seriesMap.get(seria.getName()).add(seria);
            }
            for (Map.Entry<String, List<Seria>> seriesEntry : seriesMap.entrySet()) {
                if (seriesEntry.getValue().size() > 1) {
                    Long subMasterId = seriesEntry.getValue().get(0).getId();
                    Set<Long> subSlavesIds = new HashSet<Long>();
                    for (Seria seria : seriesEntry.getValue()) {
                        subSlavesIds.add(seria.getId());
                    }

                    subSlavesIds.remove(subMasterId);
                    doJoinSeries(subMasterId, subSlavesIds, JoinOptions.defaultOnes());
                }
            }
        }

        series = new ArrayList<Seria>();
        for (Long manufactorId : slavesIds) {
            series.addAll(seriaAdapter.getByManufatorId(manufactorId));
        }

        for (Seria seria : series) {
            seria.setManufactorId(masterId);
            seriaAdapter.merge(seria);
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            //  Удаление пустых slaves
            for (Long manufactorId : slavesIds) {
                manufactorAdapter.delete(manufactorId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doJoinSeries(final Long masterId, final Set<Long> slavesIds, final JoinOptions joinOptions) {
        List<Motor> motors = new ArrayList<Motor>();

        if (joinOptions.isRecursiveJoinByName()) {
            //  Объединяем дочерние моторы
            motors.addAll(motorAdapter.getBySeriaId(masterId));
            for (Long seriaId : slavesIds) {
                motors.addAll(motorAdapter.getBySeriaId(seriaId));
            }

            Map<String, List<Motor>> motorsMap = new HashMap<String, List<Motor>>();
            for (Motor motor : motors) {
                if (!motorsMap.containsKey(motor.getFullBusinessKey())) {
                    motorsMap.put(motor.getFullBusinessKey(), new ArrayList<Motor>());
                }

                motorsMap.get(motor.getFullBusinessKey()).add(motor);
            }
            for (Map.Entry<String, List<Motor>> motorsEntry : motorsMap.entrySet()) {
                if (motorsEntry.getValue().size() > 1) {
                    Long subMasterId = motorsEntry.getValue().get(0).getId();
                    Set<Long> subSlavesIds = new HashSet<Long>();
                    for (Motor motor : motorsEntry.getValue()) {
                        subSlavesIds.add(motor.getId());
                    }

                    subSlavesIds.remove(subMasterId);
                    doJoinMotors(subMasterId, subSlavesIds, JoinOptions.defaultOnes());
                }
            }
        }

        motors = new ArrayList<Motor>();
        for (Long seriaId : slavesIds) {
            motors.addAll(motorAdapter.getBySeriaId(seriaId));
        }

        for (Motor motor : motors) {
            motor.setSeriaId(masterId);
            motorAdapter.merge(motor);
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            //  Удаление пустых slaves
            for (Long seriaId : slavesIds) {
                seriaAdapter.delete(seriaId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doJoinMotors(final Long masterId, final Set<Long> slavesIds, final JoinOptions joinOptions) {
        List<FiltersAndMotors> fams = new ArrayList<FiltersAndMotors>();
        for (Long motorId : slavesIds) {
            fams.addAll(filtersAndMotorsAdapter.getByMotorId(motorId));
        }

        List<FiltersAndMotors> masterFams = new ArrayList<FiltersAndMotors>();
        masterFams.addAll(filtersAndMotorsAdapter.getByMotorId(masterId));

        Set<Long> famsToDelete = new HashSet<Long>();
        for (FiltersAndMotors fam : fams) {
            boolean alreadyExists = false;
            for (FiltersAndMotors masterFam : masterFams) {
                if (masterFam.getFilterId().equals(fam.getFilterId())) {
                    famsToDelete.add(fam.getId());
                    alreadyExists = true;
                }
            }

            if (!alreadyExists) {
                fam.setMotorId(masterId);
                filtersAndMotorsAdapter.merge(fam);
            }
        }

        for (Long famId : famsToDelete) {
            filtersAndMotorsAdapter.delete(famId);
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            //  Удаление пустых slaves
            for (Long motorId : slavesIds) {
                motorAdapter.delete(motorId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCopyMotors(List<Motor> copyMotors, Long seriaId) {
        if (copyMotors != null) {
            List<Motor> motors = new ArrayList<Motor>();
            motors.addAll(getMotorsBySeriaId(seriaId));
            if (copyMotors.size() > 0) {
                boolean isEquals = false;
                for (Motor copyMotor : copyMotors) {
                    for (Motor motor : motors) {
                        //Слияние привязанных фильтров у одинаковых двигателей
                        if (Motor.completelyEqual(copyMotor, motor)) {
                            List<FiltersAndMotors> filtersAndMotorsFromCopy = new ArrayList<FiltersAndMotors>();
                            List<FiltersAndMotors> filtersAndMotorsDestination = new ArrayList<FiltersAndMotors>();
                            filtersAndMotorsFromCopy = filtersAndMotorsAdapter.getByMotorId(copyMotor.getId());
                            filtersAndMotorsDestination = filtersAndMotorsAdapter.getByMotorId(motor.getId());
                            for (FiltersAndMotors filtersAndMotorsFromCo : filtersAndMotorsFromCopy) {
                                boolean newFilter = true;
                                for (FiltersAndMotors filtersAndMotorsDest : filtersAndMotorsDestination) {
                                    if (filtersAndMotorsFromCo.getFilterId() == filtersAndMotorsDest.getFilterId()) {
                                        newFilter = false;
                                    }
                                }
                                if (newFilter) {
                                    FiltersAndMotors filtersAndMotorsPaste = FiltersAndMotors.create(motor.getId(), filtersAndMotorsFromCo.getFilterId());
                                    filtersAndMotorsAdapter.save(filtersAndMotorsPaste);
                                }
                            }
                            isEquals = true;
                        }
                    }
                    //Слияние привязанных фильтров двигателей у разных двигателей
                    if (!isEquals) {
                        Motor pasteMotor = Motor.copy(copyMotor);
                        pasteMotor.setSeriaId(seriaId);
                        motorAdapter.save(pasteMotor);
                        List<FiltersAndMotors> filtersAndMotorsForCopy = new ArrayList<FiltersAndMotors>();
                        filtersAndMotorsForCopy.addAll(filtersAndMotorsAdapter.getByMotorId(copyMotor.getId()));
                        if (filtersAndMotorsForCopy.size() > 0) {
                            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsForCopy) {
                                FiltersAndMotors filtersAndMotorsNew = FiltersAndMotors.create(pasteMotor.getId(), filtersAndMotors.getFilterId());
                                filtersAndMotorsAdapter.save(filtersAndMotorsNew);
                            }
                        }
                    }
                    isEquals = false;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCopySeries(List<Seria> copySeries, Long manufactorId) {
        if (copySeries != null) {
            List<Seria> series = new ArrayList<Seria>();
            series.addAll(getSeriesByManufactorId(manufactorId));
            if (copySeries.size() > 0) {
                boolean isEquals = false;
                for (Seria copySeria : copySeries) {
                    for (Seria seria : series) {
                        if (copySeria.getName().equals(seria.getName())) {
                            doCopyMotors(getMotorsBySeriaId(copySeria.getId()), seria.getId());
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Seria pasteSeria = Seria.copy(copySeria);
                        pasteSeria.setManufactorId(manufactorId);
                        seriaAdapter.save(pasteSeria);
                        doCopyMotors(getMotorsBySeriaId(copySeria.getId()), pasteSeria.getId());
                    }
                    isEquals = false;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCopyManufactors(List<Manufactor> copyManufactors, Long vechicleId) {
        if (copyManufactors != null) {
            List<Manufactor> manufactors = new ArrayList<Manufactor>();
            manufactors.addAll(getManufactorsByVechicleTypeId(vechicleId));
            if (copyManufactors.size() > 0) {
                boolean isEquals = false;
                for (Manufactor copyManufactor : copyManufactors) {
                    for (Manufactor manufactor : manufactors) {
                        if (copyManufactor.getName().equals(manufactor.getName())) {
                            doCopySeries(getSeriesByManufactorId(copyManufactor.getId()), manufactor.getId());
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Manufactor pasteManufactor = Manufactor.copy(copyManufactor);
                        pasteManufactor.setVechicleTypeId(vechicleId);
                        manufactorAdapter.save(pasteManufactor);
                        doCopySeries(getSeriesByManufactorId(copyManufactor.getId()), pasteManufactor.getId());
                    }
                    isEquals = false;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCutMotors(List<Motor> copyMotors, Long seriaId) {
        if (copyMotors != null) {
            List<Motor> motors = new ArrayList<Motor>();
            motors.addAll(getMotorsBySeriaId(seriaId));
            if ((copyMotors.size() > 0)) {
                List<FiltersAndMotors> filtersAndMotorsFromCopy = new ArrayList<FiltersAndMotors>();
                boolean isEquals = false;
                for (Motor copyMotor : copyMotors) {
                    for (Motor motor : motors) {
                        //Слияние привязанных фильтров у одинаковых двигателей
                        if (Motor.completelyEqual(copyMotor, motor)) {
                            List<FiltersAndMotors> filtersAndMotorsDestination = new ArrayList<FiltersAndMotors>();
                            filtersAndMotorsFromCopy = filtersAndMotorsAdapter.getByMotorId(copyMotor.getId());
                            filtersAndMotorsDestination = filtersAndMotorsAdapter.getByMotorId(motor.getId());
                            for (FiltersAndMotors filtersAndMotorsFromCo : filtersAndMotorsFromCopy) {
                                boolean newFilter = true;
                                for (FiltersAndMotors filtersAndMotorsDest : filtersAndMotorsDestination) {
                                    if (filtersAndMotorsFromCo.getFilterId() == filtersAndMotorsDest.getFilterId()) {
                                        newFilter = false;
                                    }
                                }
                                if (newFilter) {
                                    FiltersAndMotors filtersAndMotorsPaste = FiltersAndMotors.create(motor.getId(), filtersAndMotorsFromCo.getFilterId());
                                    filtersAndMotorsAdapter.save(filtersAndMotorsPaste);
                                }
                            }
                            isEquals = true;
                        }
                    }
                    //Слияние приявязанных фильтров двигателей у разных двигателей
                    if (!isEquals) {
                        Motor pasteMotor = Motor.copy(copyMotor);
                        pasteMotor.setSeriaId(seriaId);
                        motorAdapter.save(pasteMotor);
                        List<FiltersAndMotors> filtersAndMotorsForCopy = new ArrayList<FiltersAndMotors>();
                        filtersAndMotorsForCopy.addAll(filtersAndMotorsAdapter.getByMotorId(copyMotor.getId()));
                        if (filtersAndMotorsForCopy.size() > 0) {
                            for (FiltersAndMotors filtersAndMotors : filtersAndMotorsForCopy) {
                                FiltersAndMotors filtersAndMotorsNew = FiltersAndMotors.create(pasteMotor.getId(), filtersAndMotors.getFilterId());
                                filtersAndMotorsAdapter.save(filtersAndMotorsNew);
                            }
                        }
                    }
                    isEquals = false;
                }
                for (Motor motorTODel : copyMotors) {
                    if(motorTODel.getSeriaId()!=seriaId){
                    List<FiltersAndMotors> existList=new ArrayList<FiltersAndMotors>();
                    existList.addAll(filtersAndMotorsAdapter.getByMotorId(motorTODel.getId()));
                    if(existList.size()>0){
                    filtersAndMotorsAdapter.deleteByMotorId(motorTODel.getId());
                    }
                    motorAdapter.delete(motorTODel.getId());
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCutSeries(List<Seria> copySeries, Long manufactorId) {
        if (copySeries != null) {
            List<Seria> series = new ArrayList<Seria>();
            series.addAll(getSeriesByManufactorId(manufactorId));
            if (copySeries.size() > 0) {
                boolean isEquals = false;
                for (Seria copySeria : copySeries) {
                    for (Seria seria : series) {
                        if (copySeria.getName().equals(seria.getName())) {
                            doCutMotors(getMotorsBySeriaId(copySeria.getId()), seria.getId());
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Seria pasteSeria = Seria.copy(copySeria);
                        pasteSeria.setManufactorId(manufactorId);
                        seriaAdapter.save(pasteSeria);
                        doCutMotors(getMotorsBySeriaId(copySeria.getId()), pasteSeria.getId());
                    }
                    isEquals = false;
                }
                for(Seria seriaToDel:copySeries){
                    if(seriaToDel.getManufactorId()!=manufactorId){
                    seriaAdapter.delete(seriaToDel.getId());
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doCutManufactors(List<Manufactor> copyManufactors, Long vechicleId) {
        if (copyManufactors != null) {
            List<Manufactor> manufactors = new ArrayList<Manufactor>();
            manufactors.addAll(getManufactorsByVechicleTypeId(vechicleId));
            if (copyManufactors.size() > 0) {
                boolean isEquals = false;
                for (Manufactor copyManufactor : copyManufactors) {
                    for (Manufactor manufactor : manufactors) {
                        if (copyManufactor.getName().equals(manufactor.getName())) {
                            doCutSeries(getSeriesByManufactorId(copyManufactor.getId()), manufactor.getId());
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Manufactor pasteManufactor = Manufactor.copy(copyManufactor);
                        pasteManufactor.setVechicleTypeId(vechicleId);
                        manufactorAdapter.save(pasteManufactor);
                        doCutSeries(getSeriesByManufactorId(copyManufactor.getId()), pasteManufactor.getId());
                    }
                    isEquals = false;
                }
            }
            for(Manufactor manufactorToDel:copyManufactors){
               if(manufactorToDel.getVechicleTypeId()!=vechicleId){
                   manufactorAdapter.delete(manufactorToDel.getId());
               }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Logged
    public void doJoinFilters(final Long masterId, final Set<Long> slavesIds, final JoinOptions joinOptions) {
        //
        //  Обработка FiltersAndMotors
        //
        {
            List<FiltersAndMotors> fams = new ArrayList<FiltersAndMotors>();
            for (Long filterId : slavesIds) {
                fams.addAll(filtersAndMotorsAdapter.getByFilterId(filterId));
            }

            List<FiltersAndMotors> masterFams = new ArrayList<FiltersAndMotors>();
            masterFams.addAll(filtersAndMotorsAdapter.getByFilterId(masterId));

            Set<Long> famsToDelete = new HashSet<Long>();
            for (FiltersAndMotors fam : fams) {
                boolean alreadyExists = false;
                for (FiltersAndMotors masterFam : masterFams) {
                    if (masterFam.getMotorId().equals(fam.getMotorId())) {
                        famsToDelete.add(fam.getId());
                        alreadyExists = true;
                    }
                }

                if (!alreadyExists) {
                    fam.setFilterId(masterId);
                    filtersAndMotorsAdapter.merge(fam);
                }
            }

            for (Long famId : famsToDelete) {
                filtersAndMotorsAdapter.delete(famId);
            }
        }

        //
        //  Обработка ое
        //
        {
            List<FiltersAndOes> foes = new ArrayList<FiltersAndOes>();
            for (Long filterId : slavesIds) {
                foes.addAll(filtersAndOesAdapter.getByFilterId(filterId));
            }

            List<FiltersAndOes> masterFoes = new ArrayList<FiltersAndOes>();
            masterFoes.addAll(filtersAndOesAdapter.getByFilterId(masterId));

            Set<Long> foesToDelete = new HashSet<Long>();
            for (FiltersAndOes foe : foes) {
                boolean alreadyExists = false;
                for (FiltersAndOes masterFoe : masterFoes) {
                    if (masterFoe.getOeId().equals(foe.getOeId())) {
                        foesToDelete.add(foe.getId());
                        alreadyExists = true;
                    }
                }

                if (!alreadyExists) {
                    foe.setFilterId(masterId);
                    filtersAndOesAdapter.merge(foe);
                }
            }

            for (Long foeId : foesToDelete) {
                filtersAndOesAdapter.delete(foeId);
            }
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            //  Удаление пустых slaves
            for (Long filterId : slavesIds) {
                filterAdapter.delete(filterId);
                filtersAndOesAdapter.deleteByFilterId(filterId);
                filtersAndMotorsAdapter.deleteByFilterId(filterId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Seria> getSeriesByIds(final Set<Long> seriesIds) {
        if (seriesIds.size() == 0) return new ArrayList<Seria>();
        else return seriaAdapter.getByIds(seriesIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Manufactor> getManufactorsByIds(final Set<Long> manufactorsIds) {
        if (manufactorsIds.size() == 0) return new ArrayList<Manufactor>();
        else return manufactorAdapter.getByIds(manufactorsIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Seria> getSeriesByManufactorIds(final Set<Long> manufactorIds) {
        return seriaAdapter.getByManufatorIds(manufactorIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Motor> getMotorsBySeriaIds(final Set<Long> seriaIds) {
        return motorAdapter.getBySeriaIds(seriaIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Motor> getMotorsByManufactorsIds(final Set<Long> manufactorsIds) {
        final List<Motor> result = new ArrayList<Motor>();
        final List<Seria> series = seriaAdapter.getByManufatorIds(manufactorsIds);
        for (Seria seria : series) {
            result.addAll(motorAdapter.getBySeriaId(seria.getId()));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Long> getSeriesIdsByMotorsIds(final Set<Long> goodwillMotors) {
        List<Motor> motors = motorAdapter.getByIds(goodwillMotors);
        Set<Long> seriesIds = new HashSet<Long>();
        for (Motor motor : motors) {
            seriesIds.add(motor.getSeriaId());
        }
        return seriesIds;
    }

    @Override
    public Set<Long> getManufactorsIdsBySeriesIds(final Set<Long> goodwillSeries) {
        List<Seria> series = seriaAdapter.getByIds(goodwillSeries);
        Set<Long> manufactorsIds = new HashSet<Long>();
        for (Seria seria : series) {
            manufactorsIds.add(seria.getManufactorId());
        }
        return manufactorsIds;
    }

    public VechicleTypeAdapter getVechicleTypeAdapter() {
        return vechicleTypeAdapter;
    }

    public void setVechicleTypeAdapter(@NotNull final VechicleTypeAdapter vechicleTypeAdapter) {
        this.vechicleTypeAdapter = vechicleTypeAdapter;
    }

    public ManufactorAdapter getManufactorAdapter() {
        return manufactorAdapter;
    }

    public void setManufactorAdapter(@NotNull final ManufactorAdapter manufactorAdapter) {
        this.manufactorAdapter = manufactorAdapter;
    }

    public SeriaAdapter getSeriaAdapter() {
        return seriaAdapter;
    }

    public void setSeriaAdapter(@NotNull final SeriaAdapter seriaAdapter) {
        this.seriaAdapter = seriaAdapter;
    }

    public MotorAdapter getMotorAdapter() {
        return motorAdapter;
    }

    public void setMotorAdapter(@NotNull final MotorAdapter motorAdapter) {
        this.motorAdapter = motorAdapter;
    }

    public FilterTypeAdapter getFilterTypeAdapter() {
        return filterTypeAdapter;
    }

    public void setFilterTypeAdapter(@NotNull final FilterTypeAdapter filterTypeAdapter) {
        this.filterTypeAdapter = filterTypeAdapter;
    }

    public FiltersAndMotorsAdapter getFiltersAndMotorsAdapter() {
        return filtersAndMotorsAdapter;
    }

    public void setFiltersAndMotorsAdapter(@NotNull final FiltersAndMotorsAdapter filtersAndMotorsAdapter) {
        this.filtersAndMotorsAdapter = filtersAndMotorsAdapter;
    }

    public List<FiltersAndMotors> getFiltersAndMotorsByMotorId(long motorId){
        return getFiltersAndMotorsAdapter().getByMotorId(motorId);
    }

    public FiltersAndMotors getFilterAndMotor(final Long filterId,final Long motorId) {
        return getFiltersAndMotorsAdapter().getFilterAndMotor(filterId, motorId);
    }

    public void updateFiltersAndMotors(@NotNull FiltersAndMotors filtersAndMotors) {
        getFiltersAndMotorsAdapter().updateFiltersAndMotors(filtersAndMotors);
    }

    public List<FilterRelativeMotor> getFiltersRelativeMotor(final Long motorId) {
        List<FiltersAndMotors> filtersAndMotorsList = getFiltersAndMotorsAdapter().getByMotorId(motorId);
        List<FilterRelativeMotor> filterRelativeMotorList = new ArrayList<FilterRelativeMotor>();
        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
            Filter filter = filterAdapter.getById(filtersAndMotors.getFilterId());
            FilterRelativeMotor relativeMotor=new FilterRelativeMotor();
            relativeMotor.setId(filter.getId());
            relativeMotor.setName(filter.getName());
            relativeMotor.setDisabled(filter.getDisabled());
            relativeMotor.setFilterFormId(filter.getFilterFormId());
            relativeMotor.setFilterTypeCode(filter.getFilterTypeCode());
            relativeMotor.setComment(filtersAndMotors.getFilterComment());
            if (relativeMotor != null) {
                filterRelativeMotorList.add(relativeMotor);
            }
        }
        return filterRelativeMotorList;
    }

    public FilterRelativeMotor getRelationToFilter(final Long filterId,final Long motorId) {
        FiltersAndMotors filtersAndMotors = getFilterAndMotor(filterId, motorId);
        Filter filter = filterAdapter.getById(filtersAndMotors.getFilterId());
        FilterRelativeMotor relativeMotor=new FilterRelativeMotor();
        relativeMotor.setId(filter.getId());
        relativeMotor.setName(filter.getName());
        relativeMotor.setDisabled(filter.getDisabled());
        relativeMotor.setFilterFormId(filter.getFilterFormId());
        relativeMotor.setFilterTypeCode(filter.getFilterTypeCode());
        relativeMotor.setComment(filtersAndMotors.getFilterComment());
        return relativeMotor;
    }
}
