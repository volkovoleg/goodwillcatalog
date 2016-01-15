package ru.goodfil.catalog.services.impl;

import com.google.inject.Inject;
import org.apache.commons.collections.iterators.ArrayListIterator;
import ru.goodfil.catalog.adapters.BrandAdapter;
import ru.goodfil.catalog.adapters.FiltersAndOesAdapter;
import ru.goodfil.catalog.adapters.OeAdapter;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateBefore;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.utils.JoinOptions;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: AnalogServiceImpl.java 166 2013-09-11 13:37:16Z chezxxx@gmail.com $
 */
@Managed
public class AnalogServiceImpl implements AnalogsService {
    @NotNull
    @Inject
    private BrandAdapter brandAdapter;

    @NotNull
    @Inject
    private OeAdapter oeAdapter;

    @NotNull
    @Inject
    private FiltersAndOesAdapter filtersAndOesAdapter;

    @Logged
    @ValidateBefore
    @Override
    public List<Brand> getBrands() {
        return brandAdapter.getAll();
    }

    @Logged
    @ValidateBefore
    @Override
    public List<Brand> getBrandsForUnload() {
        List<Brand> bufferBrands = new ArrayList<Brand>();
        List<Brand> bufferBrandsToDelete = new ArrayList<Brand>();
        bufferBrands.addAll(brandAdapter.getAll());
        for (Brand brand : bufferBrands) {
            if (brand.getStandaloneStatus() != null && brand.getStandaloneStatus() == 0) {
                bufferBrandsToDelete.add(brand);
            }
        }
        if ((bufferBrandsToDelete.size() > 0) && (bufferBrands.size() > 0)) {
            bufferBrands.removeAll(bufferBrandsToDelete);
        }
        return bufferBrands;
    }

    @Logged
    @ValidateBefore
    @Override
    public List<Oe> getOesByBrand(Long brandId) {
        return oeAdapter.getByBrandId(brandId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addBrand(Brand brand) {
        brandAdapter.save(brand);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeBrand(Long brandId) {
        brandAdapter.delete(brandId);
    }

    @Logged
    @ValidateBefore
    @Override
    public void addOe(Oe oe) {
        oeAdapter.save(oe);
    }

    @Logged
    @ValidateBefore
    @Override
    public void removeOe(Long oeId) {
        oeAdapter.delete(oeId);
    }

    @Override
    public void removeOes(Set<Long> oeIds) {
        oeAdapter.deleteByIds(oeIds);
    }

    @Override
    public void updateBrand(Brand brand) {
        brandAdapter.merge(brand);
    }

    @Override
    public List<Brand> searchBrandsByName(String name) {
        return brandAdapter.searchByName(name);
    }

    @Override
    public List<Oe> getOes() {
        return oeAdapter.getAll();
    }

    @Override
    public void updateOe(Oe oe) {
        oeAdapter.merge(oe);
    }

    @Override
    public void updateOes(Collection<Oe> arg) {
        oeAdapter.merge(arg);
    }

    @Override
    public List<Oe> searchOesByBrandAndName(Long brandId, String name) {
        return oeAdapter.searchByBrandIdAndName(brandId, name);
    }

    @Override
    public List<Oe> getOesByBrandAndName(Long brandId, String name) {
        return oeAdapter.getByBrandIdAndName(brandId, name);
    }

    @Override
    public List<Oe> getOesByName(final String name) {
        return oeAdapter.getByName(name);
    }

    @Override
    public List<Oe> searchOesByName(final String name) {
        return oeAdapter.searchByName(name);
    }

    @Override
    public List<Brand> getBrandsByName(final String name) {
        return brandAdapter.getByName(name);
    }

    @Override
    public Brand getBrandById(final Long brandId) {
        return brandAdapter.getById(brandId);
    }

    @Override
    public List<Brand> getBrandsByIds(Set<Long> brandsIds) {
        return brandAdapter.getByIds(brandsIds);
    }


    @Override
    public List<FiltersAndOes> getByOeIds(Set<Long> oesIds) {
        return filtersAndOesAdapter.getByOeIds(oesIds);
    }

    @Override
    public void deleteByOeIds(Set<Long> oeIds) {
        filtersAndOesAdapter.deleteByOeIds(oeIds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doJoinBrands(final Long masterItemId, final Set<Long> slavesItemsIds, final JoinOptions joinOptions) {
        List<Oe> oes = new ArrayList<Oe>();
        for (Long brandId : slavesItemsIds) {
            oes.addAll(oeAdapter.getByBrandId(brandId));
        }

        Set<Long> oesToDelete = new HashSet<Long>();
        List<Oe> masterOes = oeAdapter.getByBrandId(masterItemId);
        for (Oe oe : oes) {
            boolean suchOeAlreadyExists = false;
            for (Oe moe : masterOes) {
                if (moe.getName().trim().equals(oe.getName().trim())) {
                    suchOeAlreadyExists = true;
                }
            }

            if (suchOeAlreadyExists) {
                oesToDelete.add(oe.getId());
            } else {
                oe.setBrandId(masterItemId);
                oeAdapter.merge(oe);
            }
        }

        if (oesToDelete.size() > 0) {
            oeAdapter.deleteByIds(oesToDelete);
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            for (Long brandId : slavesItemsIds) {
                brandAdapter.delete(brandId);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doJoinOes(final Long masterItemId, final Set<Long> slavesItemsIds, final JoinOptions joinOptions) {
        List<FiltersAndOes> foes = new ArrayList<FiltersAndOes>();
        for (Long oeId : slavesItemsIds) {
            foes.addAll(filtersAndOesAdapter.getByOeId(oeId));
        }

        List<FiltersAndOes> masterFoes = new ArrayList<FiltersAndOes>();
        masterFoes.addAll(filtersAndOesAdapter.getByOeId(masterItemId));

        Set<Long> foesToDelete = new HashSet<Long>();
        for (FiltersAndOes foe : foes) {
            boolean alreadyExists = false;
            for (FiltersAndOes masterFoe : masterFoes) {
                if (masterFoe.getFilterId().equals(foe.getFilterId())) {
                    foesToDelete.add(foe.getId());
                    alreadyExists = true;
                }
            }

            if (!alreadyExists) {
                foe.setOeId(masterItemId);
                filtersAndOesAdapter.merge(foe);
            }
        }

        for (Long foeId : foesToDelete) {
            filtersAndOesAdapter.delete(foeId);
        }

        if (joinOptions.isDeleteEmptySlaves()) {
            for (Long oeId : slavesItemsIds) {
                oeAdapter.delete(oeId);
                filtersAndOesAdapter.deleteByOeId(oeId);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doCopyOe(List<Oe> copyOes,Long brandId){
        if (copyOes != null) {
            List<Oe> oes = new ArrayList<Oe>();
            oes.addAll(getOesByBrand(brandId));
            if (oes.size() > 0) {
                boolean isEquals = false;
                for (Oe copyOe : copyOes) {
                    for (Oe oe : oes) {
                        if (copyOe.getName().equals(oe.getName())) {
                            List<FiltersAndOes> filtersAndOesFromCopy = new ArrayList<FiltersAndOes>();
                            List<FiltersAndOes> filtersAndOesDestination = new ArrayList<FiltersAndOes>();
                            filtersAndOesFromCopy = filtersAndOesAdapter.getByOeId(copyOe.getId());
                            filtersAndOesDestination = filtersAndOesAdapter.getByOeId(oe.getId());
                            for (FiltersAndOes filtersAndOesFromCo : filtersAndOesFromCopy) {
                                boolean newFilter = true;
                                for (FiltersAndOes filtersAndOesDest : filtersAndOesDestination) {
                                    if (filtersAndOesFromCo.getFilterId() == filtersAndOesDest.getFilterId()) {
                                        newFilter = false;
                                    }
                                }
                                if (newFilter) {
                                    FiltersAndOes filtersAndMotorsPaste = FiltersAndOes.create(filtersAndOesFromCo.getFilterId(), oe.getId());
                                    filtersAndOesAdapter.save(filtersAndMotorsPaste);
                                }
                            }
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Oe pasteOe = Oe.copy(copyOe);
                        pasteOe.setBrandId(brandId);
                        oeAdapter.save(pasteOe);
                        List<FiltersAndOes> filtersAndOesForCopy = new ArrayList<FiltersAndOes>();
                        filtersAndOesForCopy.addAll(filtersAndOesAdapter.getByOeId(copyOe.getId()));
                        if (filtersAndOesForCopy.size() > 0) {
                            for (FiltersAndOes filtersAndMotors : filtersAndOesForCopy) {
                                FiltersAndOes filtersAndMotorsNew = FiltersAndOes.create(filtersAndMotors.getFilterId(), pasteOe.getId());
                                filtersAndOesAdapter.save(filtersAndMotorsNew);
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
    public void doCutOe(List<Oe> copyOes,Long brandId){
        if (copyOes != null) {
            List<Oe> oes = new ArrayList<Oe>();
            oes.addAll(getOesByBrand(brandId));
            if (oes.size() > 0) {
                List<FiltersAndOes> filtersAndOesFromCopy = new ArrayList<FiltersAndOes>();
                boolean isEquals = false;
                for (Oe copyOe : copyOes) {
                    for (Oe oe : oes) {
                        if (copyOe.getName().equals(oe.getName())) {
                            filtersAndOesFromCopy = new ArrayList<FiltersAndOes>();
                            List<FiltersAndOes> filtersAndOesDestination = new ArrayList<FiltersAndOes>();
                            filtersAndOesFromCopy = filtersAndOesAdapter.getByOeId(copyOe.getId());
                            filtersAndOesDestination = filtersAndOesAdapter.getByOeId(oe.getId());
                            for (FiltersAndOes filtersAndOesFromCo : filtersAndOesFromCopy) {
                                boolean newFilter = true;
                                for (FiltersAndOes filtersAndOesDest : filtersAndOesDestination) {
                                    if (filtersAndOesFromCo.getFilterId() == filtersAndOesDest.getFilterId()) {
                                        newFilter = false;
                                    }
                                }
                                if (newFilter) {
                                    FiltersAndOes filtersAndMotorsPaste = FiltersAndOes.create(filtersAndOesFromCo.getFilterId(), oe.getId());
                                    filtersAndOesAdapter.save(filtersAndMotorsPaste);
                                }
                            }
                            isEquals = true;
                        }
                    }
                    if (!isEquals) {
                        Oe pasteOe = Oe.copy(copyOe);
                        pasteOe.setBrandId(brandId);
                        oeAdapter.save(pasteOe);
                        List<FiltersAndOes> filtersAndOesForCopy = new ArrayList<FiltersAndOes>();
                        filtersAndOesForCopy.addAll(filtersAndOesAdapter.getByOeId(copyOe.getId()));
                        if (filtersAndOesForCopy.size() > 0) {
                            for (FiltersAndOes filtersAndMotors : filtersAndOesForCopy) {
                                FiltersAndOes filtersAndMotorsNew = FiltersAndOes.create(filtersAndMotors.getFilterId(), pasteOe.getId());
                                filtersAndOesAdapter.save(filtersAndMotorsNew);
                            }
                        }
                    }
                    isEquals = false;
                }
                for (Oe oeTODel : copyOes) {
                    if(oeTODel.getBrandId()!=brandId){
                        List<FiltersAndOes> existList=new ArrayList<FiltersAndOes>();
                        existList.addAll(filtersAndOesAdapter.getByOeId(oeTODel.getId()));
                        if(existList.size()>0){
                            filtersAndOesAdapter.deleteByOeId(oeTODel.getId());
                        }
                        oeAdapter.delete(oeTODel.getId());
                    }
                }
            }
        }
    }

    public BrandAdapter getBrandAdapter() {
        return brandAdapter;
    }

    public void setBrandAdapter(@NotNull BrandAdapter brandAdapter) {
        this.brandAdapter = brandAdapter;
    }

    public OeAdapter getOeAdapter() {
        return oeAdapter;
    }

    public void setOeAdapter(@NotNull OeAdapter oeAdapter) {
        this.oeAdapter = oeAdapter;
    }
}
