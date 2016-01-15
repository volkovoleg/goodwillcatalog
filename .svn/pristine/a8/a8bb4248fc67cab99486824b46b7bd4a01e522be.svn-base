package ru.goodfil.catalog.export.service;

import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.export.domain.fullexport.FullCatalogExportParams;
import ru.goodfil.catalog.export.domain.fullexport.FullExportDocumentModel;
import ru.goodfil.catalog.export.domain.oeexport.OesDocumentModel;
import java.util.*;
import javax.validation.constraints.NotNull;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ExportService.java 167 2013-09-18 07:54:11Z chezxxx@gmail.com $
 */
public interface ExportService {
    public FullExportDocumentModel getByVechicleTypeId(@NotNull Long vechicleTypeId, @NotNull FullCatalogExportParams fullCatalogExportParams);

    public FullExportDocumentModel getByManufactorId(@NotNull Long manufactorId, @NotNull FullCatalogExportParams fullCatalogExportParams);

    public FullExportDocumentModel getBySeriaId(@NotNull Long seriaId, @NotNull FullCatalogExportParams fullCatalogExportParams);

    public OesDocumentModel getOes();

    public OesDocumentModel getSimpleOes();

    public OesDocumentModel getOesByBrands(@NotNull List<Brand> brandsForUnload);
}
