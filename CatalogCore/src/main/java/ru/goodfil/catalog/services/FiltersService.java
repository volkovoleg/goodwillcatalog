package ru.goodfil.catalog.services;

import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersService.java 179 2014-07-18 12:31:54Z chezxxx@gmail.com $
 */
public interface FiltersService {
    @Logged
    List<Filter> getFilters();

    @Logged
    List<Filter> getFiltersByMotorId(@NotNull final Long motorId);

    @Logged
    Filter getFilterById(@NotNull final Long filterId);

    @Logged
    List<Filter> getFiltersByMotors(@NotNull final Set<Long> motorIds);

    @Logged
    List<Filter> getFiltersByOe(@NotNull final Long oeId);

    @Logged
    Map<Long, List<Filter>> getFiltersByOes(@NotNull final Set<Long> oeId);

    @Logged
    Set<Long> getMotorIdsByFilter(@NotNull final Long filterId);

    @Logged
    List<Motor> getMotorsByFilterId(@NotNull final Long filterId);

    @Logged
    Set<Long> getOeIdsByFilter(@NotNull final Long filterId);

    @Logged
    List<Oe> getOesByName(@NotNull final String oeName);

    @Logged
    List<Oe> getOesByFilter(@NotNull final Long filterId);

    @Logged
    List<Filter> getFiltersByName(@NotNull final String filterCode);

    @Logged
    void updateFilter(@NotNull final Filter filter);

    @Logged
    void updateFilters(@NotNull @NotEmpty final List<Filter> filters);

    @Logged
    void deleteFilter(@NotNull final Long filterId);

    @Logged
    void saveFilter(@NotNull final Filter filter);

    @Logged
    List<Brand> getBrandsByIds(@NotNull Set<Long> brandsIds);

    @Logged
    List<Filter> getFiltersByFilterFormId(@NotNull Long filterFormId);

    @Logged
    void deleteFilterTypeById(@NotNull final Long filterTypeId);

    @Logged
    void deleteFilterFormById(@NotNull final Long filterFormId);

    /**
     * Возвращает true, если к данному фильтру прикреплен указанный ое.
     *
     * @param filterId идентификатор фильтра.
     * @param oeId     идентификатор ое.
     * @return true, если к данному фильтру прикреплен указанный ое.
     */
    @Logged
    boolean getFilterHasOe(@NotNull Long filterId, @NotNull Long oeId);

    /**
     * Возвращает true, если к данному фильтру прикреплен хотя бы один из указанных ое.
     *
     * @param filterId идентификатор фильтра.
     * @param oeIds    идентификаторы ое.
     * @return true, если к данному фильтру прикреплен хотя бы один из указанных ое.
     */
    @Logged
    boolean getFilterHasAtLeastOneOe(@NotNull Long filterId, @NotNull Set<Long> oeIds);

    /**
     * Возвращает true, если к данному фильтру прикреплен указанный мотор.
     *
     * @param filterId идентификатор фильтра.
     * @param motorId  идентификатор мотора.
     * @return true, если к данному фильтру прикреплен указанный мотор.
     */
    @Logged
    boolean getFilterHasMotor(@NotNull Long filterId, @NotNull Long motorId);

    /**
     * Возвращает true, если к данному фильтру прикреплен хотя бы один из указанных моторов.
     *
     * @param filterId идентификатор фильтра.
     * @param motorIds идентификаторы моторов.
     * @return true, если к данному фильтру прикреплен хотя бы один из указанных моторов.
     */
    @Logged
    boolean getFilterHasAtLeastOneMotor(@NotNull Long filterId, @NotNull Set<Long> motorIds);

    /**
     * Добавление нового типа изделия.
     *
     * @param filterType тип изделия.
     */
    @Logged
    void saveFilterType(@NotNull FilterType filterType);

    /**
     * Обновление типа изделия.
     *
     * @param filterType тип изделия.
     */
    @Logged
    void updateFilterType(@NotNull FilterType filterType);

    /**
     * Добавление новой формы изделия.
     *
     * @param filterForm форма изделия.
     */
    @Logged
    void saveFilterForm(@NotNull FilterForm filterForm);

    /**
     * Обновление формы изделия.
     *
     * @param filterForm форма изделия.
     */
    @Logged
    void updateFilterForm(@NotNull FilterForm filterForm);

    /**
     * Возвращает моторы, привязанные к данным фильтрам.
     *
     * @param goodwillFilters идентификаторы фильтров.
     * @return моторы, привязанные к данным фильтрам.
     */
    @Logged
    Set<Long> getMotorsIdsByFilterIds(@NotNull Set<Long> goodwillFilters);

    /**
     * Возвращает фильтры, привязанные к данному типу транспортного средства через механизм глобальных привязок.
     *
     * @param selectedVechicleType идентификатор типа транспортного средства.
     * @return фильтры, привязанные к данному типу транспортного средства через механизм глобальных привязок.
     */
    @Logged
    List<Filter> getFiltersByGlobalVechicleTypeId(@NotNull Long selectedVechicleType);

    List<String> getAllGParams();
}
