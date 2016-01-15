package ru.goodfil.catalog.services.impl;

import com.google.inject.Inject;
import ru.goodfil.catalog.adapters.*;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateBefore;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.FiltersService;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersServiceImpl.java 179 2014-07-18 12:31:54Z chezxxx@gmail.com $
 */
@Managed
public class FiltersServiceImpl implements FiltersService {
    @NotNull
    @Inject
    private FilterAdapter filterAdapter;

    @NotNull
    @Inject
    private FiltersAndMotorsAdapter filtersAndMotorsAdapter;

    @NotNull
    @Inject
    private FiltersAndOesAdapter filtersAndOesAdapter;

    @NotNull
    @Inject
    private FilterFormAdapter filterFormAdapter;

    @NotNull
    @Inject
    private FilterTypeAdapter filterTypeAdapter;

    @NotNull
    @Inject
    private MotorAdapter motorAdapter;

    @NotNull
    @Inject
    private OeAdapter oeAdapter;

    @NotNull
    @Inject
    private BrandAdapter brandAdapter;

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public List<Filter> getFilters() {
        return filterAdapter.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public List<Filter> getFiltersByMotorId(final Long motorId) {
        List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByMotorId(motorId);
        Set<Long> filterIds = new HashSet<Long>();
        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
            filterIds.add(filtersAndMotors.getFilterId());
        }
        return filterAdapter.getByIds(filterIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilterById(final Long filterId) {
        return filterAdapter.getById(filterId);
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public List<Filter> getFiltersByMotors(final Set<Long> motorIds) {
        List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByMotorIds(motorIds);
        Set<Long> filterIds = new HashSet<Long>();
        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
            filterIds.add(filtersAndMotors.getFilterId());
        }
        return filterAdapter.getByIds(filterIds);
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public List<Filter> getFiltersByOe(final Long oeId) {
        Set<Long> filterIds = new HashSet<Long>();
        List<FiltersAndOes> filtersAndOeses = filtersAndOesAdapter.getByOeId(oeId);
        for (FiltersAndOes filtersAndOes : filtersAndOeses) {
            filterIds.add(filtersAndOes.getFilterId());
        }
        return filterAdapter.getByIds(filterIds);
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public Map<Long, List<Filter>> getFiltersByOes(final Set<Long> oesIds) {
        Map<Long, List<Filter>> result = new HashMap<Long, List<Filter>>();

        Set<Long> filterIds = new HashSet<Long>();
        List<FiltersAndOes> filtersAndOeses = filtersAndOesAdapter.getByOeIds(oesIds);
        for (FiltersAndOes filtersAndOes : filtersAndOeses) {
            filterIds.add(filtersAndOes.getFilterId());
        }

        List<Filter> filters = filterAdapter.getByIds(filterIds);

        for (Filter filter : filters) {
            Set<Long> filterOesIds = new HashSet<Long>();
            for (FiltersAndOes filtersAndOes : filtersAndOeses) {
                if (filtersAndOes.getFilterId().equals(filter.getId())) {
                    filterOesIds.add(filtersAndOes.getOeId());
                }
            }
            for (Long oeId : filterOesIds) {
                if (result.get(oeId) == null) {
                    result.put(oeId, new ArrayList<Filter>());
                }
                result.get(oeId).add(filter);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public Set<Long> getMotorIdsByFilter(final Long filterId) {
        List<FiltersAndMotors> filtersAndMotorsList = filtersAndMotorsAdapter.getByFilterId(filterId);
        Set<Long> motorIds = new HashSet<Long>();
        for (FiltersAndMotors filtersAndMotors : filtersAndMotorsList) {
            motorIds.add(filtersAndMotors.getMotorId());
        }
        return motorIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Motor> getMotorsByFilterId(final Long filterId) {
        return motorAdapter.getByIds(getMotorIdsByFilter(filterId));
    }

    /**
     * {@inheritDoc}
     */
    @Logged
    @ValidateBefore
    @Override
    public Set<Long> getOeIdsByFilter(final Long filterId) {
        List<FiltersAndOes> filtersAndOesList = filtersAndOesAdapter.getByFilterId(filterId);
        Set<Long> oesIds = new HashSet<Long>();
        for (FiltersAndOes filtersAndOes : filtersAndOesList) {
            oesIds.add(filtersAndOes.getOeId());
        }

        return oesIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Oe> getOesByName(final String oeName) {
        return oeAdapter.searchByName(oeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Oe> getOesByFilter(final Long filterId) {
        Set<Long> oesIds = getOeIdsByFilter(filterId);
        return oeAdapter.getByIds(oesIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Filter> getFiltersByName(final String filterCode) {
        return filterAdapter.getByName(filterCode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFilter(final Filter filter) {
        filterAdapter.merge(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFilters(final List<Filter> filters) {
        filterAdapter.merge(filters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFilter(final Long filterId) {
        filterAdapter.softDelete(filterId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFilter(final Filter filter) {
        filterAdapter.save(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> getBrandsByIds(final Set<Long> brandsIds) {
        return brandAdapter.getByIds(brandsIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Filter> getFiltersByFilterFormId(final Long filterFormId) {
        return filterAdapter.getFiltersByFilterFormId(filterFormId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFilterHasOe(final Long filterId, final Long oeId) {
        return filtersAndOesAdapter.getCountByFilterIdAndOeId(filterId, oeId) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFilterHasAtLeastOneOe(final Long filterId, final Set<Long> oesIds) {
        return filtersAndOesAdapter.getCountByFilterIdAndOesIds(filterId, oesIds) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFilterHasMotor(final Long filterId, final Long motorId) {
        return filtersAndMotorsAdapter.getCountByFilterIdAndMotorId(filterId, motorId) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getFilterHasAtLeastOneMotor(final Long filterId, final Set<Long> motorIds) {
        return filtersAndMotorsAdapter.getCountByFilterIdAndMotorsIds(filterId, motorIds) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFilterType(final FilterType filterType) {
        filterTypeAdapter.save(filterType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFilterType(final FilterType filterType) {
        filterTypeAdapter.merge(filterType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveFilterForm(final FilterForm filterForm) {
        filterFormAdapter.save(filterForm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFilterForm(final FilterForm filterForm) {
        filterFormAdapter.merge(filterForm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Long> getMotorsIdsByFilterIds(final Set<Long> goodwillFilters) {
        List<FiltersAndMotors> fams = filtersAndMotorsAdapter.getByFilterIds(goodwillFilters);
        Set<Long> result = new HashSet<Long>();
        for (FiltersAndMotors fam : fams) {
            result.add(fam.getMotorId());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Filter> getFiltersByGlobalVechicleTypeId(final Long selectedVechicleType) {
        return filterAdapter.getFiltersByGlobalVechicleTypeId(selectedVechicleType);
    }

    @Override
    public List<String> getAllGParams() {
        return filterAdapter.getAllGParams();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFilterTypeById(final Long filterTypeId) {
       filterTypeAdapter.deleteFilterTypeById(filterTypeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFilterFormById(final Long filterFormId) {
        filterFormAdapter.deleteFilterFormById(filterFormId);
    }


}

