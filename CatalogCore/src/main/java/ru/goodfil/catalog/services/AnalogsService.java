package ru.goodfil.catalog.services;

import org.apache.commons.beanutils.converters.LongArrayConverter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.utils.JoinOptions;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: AnalogsService.java 166 2013-09-11 13:37:16Z chezxxx@gmail.com $
 */
public interface AnalogsService {
    //
    //  Выборка
    //
    List<Brand> getBrands();

    /**
     * Возвращает бренды для выгрузок в отчуждаемой копии
     */
    List<Brand> getBrandsForUnload();

    List<Oe> getOesByBrand(@NotNull Long brandId);

    //
    //  Наполнение
    //
    void addBrand(@NotNull Brand brand);

    void removeBrand(@NotNull Long brandId);

    void addOe(@NotNull Oe oe);

    void removeOe(@NotNull Long oeId);

    void removeOes(@NotNull Set<Long> oeIds);

    void updateBrand(@NotNull Brand brand);

    List<Brand> searchBrandsByName(@NotNull @NotBlank String name);

    List<Oe> getOes();

    void updateOe(@NotNull Oe oe);

    void updateOes(@NotNull @NotEmpty Collection<Oe> arg);

    List<Oe> searchOesByBrandAndName(@NotNull Long brandId, @NotNull @NotBlank String name);

    List<Oe> getOesByBrandAndName(@NotNull Long brandId, @NotNull @NotBlank String name);

    List<Oe> getOesByName(@NotNull @NotBlank String name);

    List<Oe> searchOesByName(@NotNull @NotBlank String name);

    List<Brand> getBrandsByName(@NotNull @NotBlank String name);

    Brand getBrandById(@NotNull Long brandId);

    List<Brand> getBrandsByIds(Set<Long> brandsIds);

    public List<FiltersAndOes> getByOeIds(@NotNull Set<Long> oesIds);

    public void deleteByOeIds(@NotNull Set<Long> oeIds);
    /**
     * Объединение брэндов.
     */
    @Logged
    void doJoinBrands(@NotNull Long masterItemId, @NotNull @NotEmpty Set<Long> slavesItemsIds, @NotNull JoinOptions joinOptions);

    /**
     * Объединение ое.
     */
    @Logged
    void doJoinOes(@NotNull Long masterItemId, @NotNull @NotEmpty Set<Long> slavesItemsIds, @NotNull JoinOptions joinOptions);

    /**
     * Копирование Ое
     */
    @Logged
    public void doCopyOe(@NotNull List<Oe> copyOes,@NotNull Long brandId);

    /**
     * Вырезание Ое
     */
    @Logged
    public void doCutOe(@NotNull List<Oe> copyOes,@NotNull Long brandId);
}
