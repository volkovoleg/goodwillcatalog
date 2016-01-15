package ru.goodfil.catalog.services;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.domain.parts.FilterRelativeMotor;
import ru.goodfil.catalog.utils.JoinOptions;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CarsService.java 178 2014-07-10 13:25:32Z chezxxx@gmail.com $
 */
public interface CarsService {
    //
    //  Выборка
    //

    List<VechicleType> getVechicleTypes();

    List<FilterType> getFilterTypes();

    FilterType getFilterTypeById(@NotNull final Long filterTypeId);

    FilterType getFilterTypeByCode(@NotNull @NotBlank final String filterTypeCode);

    List<Manufactor> getManufactorsByVechicleTypeId(@NotNull final Long vechicleTypeId);

    List<Manufactor> getAllManufactors();

    List<Seria> getSeriesByManufactorId(@NotNull final Long manufactorId);

    List<Motor> getMotorsBySeriaId(@NotNull final Long seriaId);

    List<FilterForm> getFilterFormsByFilterTypeCode(@NotNull @NotBlank final String filterTypeCode);

    List<FilterForm> getFilterForms();

    FilterForm getFilterFormById(@NotNull final Long filterFormId);

    //
    //  Наполнение
    //
    void addVechicleType(@NotNull @Valid final VechicleType vechicleType);

    void removeVechicleType(@NotNull final Long venchicleTypeId);

    void addManufactor(@NotNull @Valid final Manufactor manufactor);

    void removeManufactor(@NotNull final Long manufactorId);

    void addSeria(@NotNull @Valid final Seria seria);

    void removeSeria(@NotNull final Long seriaId);

    void addMotor(@NotNull @Valid final Motor motor);

    void removeMotor(@NotNull final Long motorId);

    void updateVechicleType(@NotNull @Valid final VechicleType vechicleType);

    void updateManufactor(@NotNull @Valid final Manufactor manufactor);

    void updateSeria(@NotNull @Valid final Seria seria);

    void updateMotor(@NotNull @Valid final Motor motor);

    void doAttachFiltersToMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds);

    void doDetachFiltersFromMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds);

    void doAttachMotorsToFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> motorsIds);

    void doDetachMotorsFromFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> motorsIds);

    void doAttachOesToFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> oeIds);

    void doDetachOesFromFilter(@NotNull Long filterId, @NotNull @NotEmpty Set<Long> oeIds);

    Seria getSeriaById(@NotNull Long seriaId);

    Manufactor getManufactorById(@NotNull Long manufactorId);

    void doCopyMotors(@NotNull List<Motor> copyMotors, @NotNull Long seriaId);
    void doCopySeries(@NotNull List<Seria> copySeries, @NotNull Long manufactorId);
    void doCopyManufactors(@NotNull List<Manufactor> copyManufactors, @NotNull Long vechicleId);
    void doCutMotors(@NotNull List<Motor> copyMotors, @NotNull Long seriaId);
    void doCutSeries(@NotNull List<Seria> copySeries, @NotNull Long manufactorId);
    void doCutManufactors(@NotNull List<Manufactor> copyManufactors, @NotNull Long vechicleId);


    //
    //  Объединение позиций
    //

    /**
     * Объединение производителей.
     * @param masterId идентификатор производителя, к которому будут добавлены дочерние элементы всех объединяемых производителей.
     * @param slavesIds идентификаторы объединяемых производителей.
     * @param joinOptions настройки объединения позиций.
     */
    @Logged
    void doJoinManufactors(@NotNull Long masterId, @NotNull @NotEmpty Set<Long> slavesIds, @NotNull JoinOptions joinOptions);

    /**
     * Объединение серий.
     * @param masterId идентификатор серии, к которому будут добавлены дочерние элементы всех объединяемых серий.
     * @param slavesIds идентификаторы объединяемых серий.
     * @param joinOptions настройки объединения позиций.
     */
    @Logged
    void doJoinSeries(@NotNull Long masterId, @NotNull @NotEmpty Set<Long> slavesIds, @NotNull JoinOptions joinOptions);

    /**
     * Объединение моторов.
     * @param masterId идентификатор мотора, к которому будут добавлены дочерние элементы всех объединяемых моторов.
     * @param slavesIds идентификаторы объединяемых моторов.
     * @param joinOptions настройки объединения позиций.
     */
    @Logged
    void doJoinMotors(@NotNull Long masterId, @NotNull @NotEmpty Set<Long> slavesIds, @NotNull JoinOptions joinOptions);

    /**
     * Объединение фильтров.
     * @param masterId идентификатор фильтра, к которому будут добавлены дочерние элементы всех объединяемых фильтров.
     * @param slavesIds идентификаторы объединяемых фильтров.
     * @param joinOptions настройки объединения позиций.
     */
    @Logged
    void doJoinFilters(@NotNull Long masterId, @NotNull @NotEmpty Set<Long> slavesIds, @NotNull JoinOptions joinOptions);

    /**
     * Возвращает серии по указанным идентификаторам.
     * @param seriesIds идентификаторы серий.
     * @return серии по указанным идентификаторам.
     */
    @Logged
    List<Seria> getSeriesByIds(@NotNull Set<Long> seriesIds);

    /**
     * Возвращает производителей по идентификаторам.
     * @param manufactorsIds идентификаторы производителей.
     * @return производители по идентификаторам.
     */
    @Logged
    List<Manufactor> getManufactorsByIds(@NotNull Set<Long> manufactorsIds);

    /**
     * Возвращает серии по идентификаторам производителей.
     * @param manufactorIds идентификаторы производителей.
     * @return серии по идентификаторам производителей.
     */
    @Logged
    List<Seria> getSeriesByManufactorIds(@NotNull Set<Long> manufactorIds);

    /**
     * Возвращает моторы по идентификаторам серий.
     * @param seriaIds идентификаторы серий.
     * @return моторы по идентификаторам серий.
     */
    @Logged
    List<Motor> getMotorsBySeriaIds(@NotNull Set<Long> seriaIds);

    /**
     * Возвращает моторы, прикрепленные к указанному производителю.
     * @param manufactorsIds идентификаторы производителей.
     * @return моторы, прикрепленные к указанному производителю.
     */
    @Logged
    List<Motor> getMotorsByManufactorsIds(@NotNull Set<Long> manufactorsIds);

    /**
     * Возвращает идентификаторы серий по идентификаторам моторов.
     * @param goodwillMotors идентификаторы моторов.
     * @return идентификаторы серий.
     */
    @Logged
    Set<Long> getSeriesIdsByMotorsIds(@NotNull Set<Long> goodwillMotors);

    /**
     * Возвращает идентификаторы производителей по идентификаторам серий.
     * @param goodwillSeries идентификаторы серий.
     * @return идентификаторы производителей.
     */
    @Logged
    Set<Long> getManufactorsIdsBySeriesIds(@NotNull Set<Long> goodwillSeries);

    /**
     * Возвращает все свзи с филтрами по мотору
     * @param motorId  мотор
     * @return
     */
    @Logged
    List<FiltersAndMotors> getFiltersAndMotorsByMotorId(@NotNull long motorId);

    FiltersAndMotors getFilterAndMotor(final Long filterId,final Long motorId);

    void updateFiltersAndMotors(@NotNull final FiltersAndMotors filtersAndMotors);

    /**
     * Возвращает позиции привязанные к конкретному мотору, т.к. привязки содержат дополнительную информацию
     * @return
     */
    List<FilterRelativeMotor> getFiltersRelativeMotor(final Long motorId);

    /**
     * Возвращает позицию с привязкой к мотору
     * @param filterId
     * @param motorId
     * @return
     */
    FilterRelativeMotor getRelationToFilter(final Long filterId,final Long motorId);
}
