package ru.goodfil.catalog.export.service.impl;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.NativeSQLAdapter;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.export.domain.fullexport.FullCatalogExportParams;
import ru.goodfil.catalog.export.domain.fullexport.FullExportDocumentModel;
import ru.goodfil.catalog.export.domain.fullexport.RowFiltersModel;
import ru.goodfil.catalog.export.domain.fullexport.RowModel;
import ru.goodfil.catalog.export.domain.oeexport.OesDocumentModel;
import ru.goodfil.catalog.export.service.ExportService;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ExportServiceImpl.java 167 2013-09-18 07:54:11Z chezxxx@gmail.com $
 */
@Managed
public class ExportServiceImpl implements ExportService {
    @NotNull
    @Inject
    private NativeSQLAdapter sqlAdapter;

    private final String BASE_QUERY = "SELECT * FROM V_FULL_EXPORT WHERE 1=1 ";
    private final String BASE_QUERY_GW = "SELECT * FROM V_FULL_EXPORT_GW WHERE 1=1 ";
    
    private final String OES_QUERY_GW = "SELECT * FROM V_OE_REPORT_GW";
    private final String OES_QUERY_GW_Ordered = "SELECT * FROM V_OE_REPORT_GW ORDER BY FILTER_NAME";
    private final String OES_QUERY_GW_BY_BRAND="SELECT * FROM V_OE_REPORT_GW WHERE 1=1 ";

    @Override
    public FullExportDocumentModel getByVechicleTypeId(final Long vechicleTypeId, final FullCatalogExportParams fullCatalogExportParams) {
        prepareDataMart();

        String query = null;
        if (fullCatalogExportParams.isGoodwillOnly()) {
            query = BASE_QUERY_GW + "AND VECHICLETYPE_ID = " + String.valueOf(vechicleTypeId) + " ORDER BY MANUFACTOR_NAME, SERIA_NAME";
        } else {
            query = BASE_QUERY + "AND VECHICLETYPE_ID = " + String.valueOf(vechicleTypeId)+ " ORDER BY MANUFACTOR_NAME, SERIA_NAME";
        }

        List<Map<String, Object>> result = sqlAdapter.select(query);
        return getFullExportDocumentModel(result, fullCatalogExportParams.isIgnoreEmptyRows());
    }


    @Override
    public FullExportDocumentModel getByManufactorId(final Long manufactorId, final FullCatalogExportParams fullCatalogExportParams) {
        prepareDataMart();

        String query = null;
        if (fullCatalogExportParams.isGoodwillOnly()) {
            query = BASE_QUERY_GW + "AND MANUFACTOR_ID = " + String.valueOf(manufactorId) + " ORDER BY SERIA_NAME";
        } else {
            query = BASE_QUERY + "AND MANUFACTOR_ID = " + String.valueOf(manufactorId) + " ORDER BY SERIA_NAME";
        }

        List<Map<String, Object>> result = sqlAdapter.select(query);
        return getFullExportDocumentModel(result, fullCatalogExportParams.isIgnoreEmptyRows());
    }

    @Override
    public FullExportDocumentModel getBySeriaId(final Long seriaId, final FullCatalogExportParams fullCatalogExportParams) {
        prepareDataMart();

        String query = null;
        if (fullCatalogExportParams.isGoodwillOnly()) {
            query = BASE_QUERY_GW + "AND SERIA_ID = " + String.valueOf(seriaId);
        } else {
            query = BASE_QUERY + "AND SERIA_ID = " + String.valueOf(seriaId);
        }
        List<Map<String, Object>> result = sqlAdapter.select(query);
        return getFullExportDocumentModel(result, fullCatalogExportParams.isIgnoreEmptyRows());
    }

    @Override
    public OesDocumentModel getOes() {
        List<Map<String, Object>> result = sqlAdapter.select(OES_QUERY_GW);
        return getOesDocumentModel(result);
    }

    private OesDocumentModel getOesDocumentModel(List<Map<String, Object>> source) {
        final String BRAND_NAME = "BRAND_NAME";
        final String FILTER_NAME = "FILTER_NAME";
        final String OE_NAME = "OE_NAME";

        OesDocumentModel documentModel = new OesDocumentModel();

        String lastBrandName = null;
        for(Map<String, Object> row : source) {
            String brandName = str(row.get(BRAND_NAME));
            String filterName = str(row.get(FILTER_NAME));
            String oeName = str(row.get(OE_NAME));
            
            if (!brandName.equals(lastBrandName)) {
                documentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.emptyRow());
                documentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.sectionHeader(brandName));
                lastBrandName = brandName;
            }

            documentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.row(filterName, oeName));
        }

        return documentModel;
    }

    @Override
    public OesDocumentModel getOesByBrands(List<Brand> brandsForUnload) {
        final String BRAND_NAME = "BRAND_NAME";
        final String FILTER_NAME = "FILTER_NAME";
        final String OE_NAME = "OE_NAME";
        final String brandQuery = "AND BRAND_NAME=";
        final String sqr="'";
        OesDocumentModel oesDocumentModel = new OesDocumentModel();
        oesDocumentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.emptyRow());
        for (Brand brand : brandsForUnload) {
            String query = OES_QUERY_GW_BY_BRAND + brandQuery +sqr+brand.getName()+sqr;
            List<Map<String, Object>> result = sqlAdapter.select(query);
            for (Map<String, Object> row : result) {
                String brandName = str(row.get(BRAND_NAME));
                String filterName = str(row.get(FILTER_NAME));
                String oeName = str(row.get(OE_NAME));
                oesDocumentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.simpleRow(filterName, oeName, brandName));
            }
        }
        return oesDocumentModel;
    }

    @Override
    public OesDocumentModel getSimpleOes() {
        List<Map<String, Object>> result = sqlAdapter.select(OES_QUERY_GW_Ordered);
        return getOesSimpleDocumentModel(result);
    }

    private OesDocumentModel getOesSimpleDocumentModel(List<Map<String, Object>> source) {
        final String BRAND_NAME = "BRAND_NAME";
        final String FILTER_NAME = "FILTER_NAME";
        final String OE_NAME = "OE_NAME";
        OesDocumentModel documentModel = new OesDocumentModel();
        String lastBrandName = null;
        for(Map<String, Object> row : source) {
            String brandName = str(row.get(BRAND_NAME));
            String filterName = str(row.get(FILTER_NAME));
            String oeName = str(row.get(OE_NAME));
            documentModel.getRows().add(ru.goodfil.catalog.export.domain.oeexport.RowModel.simpleRow(filterName, oeName,brandName));
        }
        return documentModel;
    }

    private FullExportDocumentModel getFullExportDocumentModel(List<Map<String, Object>> result, boolean ignoreEmptyRows) {
        List<RowModel> rows = transformFullExportRows(result, ignoreEmptyRows);

        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        FullExportDocumentModel fullExportDocumentModel = new FullExportDocumentModel();
        fullExportDocumentModel.setDate(sdf.format(Calendar.getInstance().getTime()));
        fullExportDocumentModel.getRows().addAll(rows);
        return fullExportDocumentModel;
    }

    private List<RowModel> transformFullExportRows(List<Map<String, Object>> source, boolean ignoreEmptyRows) {
        final String MANUFACTOR_NAME = "MANUFACTOR_NAME";
        final String SERIA_NAME = "SERIA_NAME";
        final String MOTOR_ID = "MOTOR_ID";
        final String MODEL_NAME = "MOTOR_NAME";
        final String ENGINE_NAME = "ENGINE_NAME";
        final String KW = "KW";
        final String HP = "HP";
        final String DATEF = "DATEF";
        final String DATET = "DATET";
        final String FILTER_TYPE = "FILTER_TYPE";
        final String FILTERS = "FILTERS";
        final String SEPARATOR = ",";

        List<RowModel> result = new ArrayList<RowModel>();

        String lastManufactorName = null;
        RowModel lastRowMotor = null;
        for (Map<String, Object> row : source) {
            String manufactorName = str(row.get(MANUFACTOR_NAME));
            if (!StringUtils.isBlank(manufactorName) && !manufactorName.equals(lastManufactorName)) {
                if (lastRowMotor != null) {
                    if ((ignoreEmptyRows && lastRowMotor.hasAtLeastOneFilter()) || !ignoreEmptyRows) {
                        result.add(lastRowMotor);
                    }
                    lastRowMotor = null;
                }

                RowModel rowModel = new RowModel();
                rowModel.setHeader(manufactorName);
                result.add(rowModel);

                lastManufactorName = manufactorName;
            }

            String motorId = str(row.get(MOTOR_ID));
            if (!StringUtils.isBlank(motorId) && (lastRowMotor == null || !motorId.equals(lastRowMotor.getMotorId()))) {
                if (lastRowMotor != null) {
                    if ((ignoreEmptyRows && lastRowMotor.hasAtLeastOneFilter()) || !ignoreEmptyRows) {
                        result.add(lastRowMotor);
                    }
                }

                lastRowMotor = new RowModel();
                lastRowMotor.setSeria(str(row.get(SERIA_NAME)));
                lastRowMotor.setModel(str(row.get(MODEL_NAME)));
                lastRowMotor.setMotorId(str(row.get(MOTOR_ID)));
                lastRowMotor.setEngine(str(row.get(ENGINE_NAME)));
                lastRowMotor.setKw(str(row.get(KW)));
                lastRowMotor.setHp(str(row.get(HP)));
                lastRowMotor.setBegdate(str(row.get(DATEF)));
                lastRowMotor.setEnddate(str(row.get(DATET)));
            }

            String filterTypeCode = str(row.get(FILTER_TYPE));
            String filtersStr = str(row.get(FILTERS));

            if (!StringUtils.isBlank(filterTypeCode) && !StringUtils.isBlank(filtersStr)) {
                String[] filters = filtersStr.split(SEPARATOR);

                RowFiltersModel rowFiltersModel = new RowFiltersModel();
                rowFiltersModel.setFilterTypeId(filterTypeCode);
                rowFiltersModel.getFilters().addAll(Arrays.asList(filters));

                if (lastRowMotor != null) {
                    lastRowMotor.getFilters().put(filterTypeCode, rowFiltersModel);
                }
            }
        }

        if (lastRowMotor != null) {
            if ((ignoreEmptyRows && lastRowMotor.hasAtLeastOneFilter()) || !ignoreEmptyRows) {
                result.add(lastRowMotor);
            }
        }

        return result;
    }

    private static String str(Object o) {
        if (o == null) return "";
        else return o.toString();
    }

    private void prepareDataMart() {
        final String CLEAR_QUERY = "DELETE FROM DM_FILTERS_TYPES_FORMS";
        sqlAdapter.execute(CLEAR_QUERY);

        final String INSERT_QUERY = "" +
                "INSERT INTO DM_FILTERS_TYPES_FORMS (\n" +
                "\tFILTER_ID,\t\n" +
                "\tFILTER_APPLYTOALL_VT1,\n" +
                "\tFILTER_APPLYTOALL_VT2,\n" +
                "\tFILTER_APPLYTOALL_VT3,\n" +
                "\tFILTER_APPLYTOALL_VT4,\n" +
                "\tFITLER_TYPE_ID,\n" +
                "\tFILTER_TYPE_CODE,\n" +
                "\tFILTER_TYPE_NAME,\t\n" +
                "\tFILTER_FORM_ID,\n" +
                "\tFILTER_FORM_NAME,\t\n" +
                "\tFILTER_NAME,\n" +
                "\tONSITE)\n" +
                "SELECT\n" +
                "\tf.ID as FILTER_ID,\n" +
                "\t\n" +
                "\tCASE WHEN f.APPLYTOALL_VT1 = 1 THEN 1 ELSE 0 END as FILTER_APPLYTOALL_VT1,\n" +
                "\tCASE WHEN f.APPLYTOALL_VT2 = 1 THEN 2 ELSE 0 END as FILTER_APPLYTOALL_VT2,\n" +
                "\tCASE WHEN f.APPLYTOALL_VT3 = 1 THEN 3 ELSE 0 END as FILTER_APPLYTOALL_VT3,\n" +
                "\tCASE WHEN f.APPLYTOALL_VT4 = 1 THEN 4 ELSE 0 END as FILTER_APPLYTOALL_VT4,\n" +
                "\t\n" +
                "\t--\tТип фильтра\n" +
                "\tft.ID as FILTER_TYPE_ID,\n" +
                "\tft.CODE as FILTER_TYPE_CODE,\n" +
                "\tft.NAME as FILTER_TYPE_NAME,\n" +
                "\t\n" +
                "\t--\tФорма фильтра\n" +
                "\tff.ID as FILTER_FORM_ID,\n" +
                "\tff.NAME as FILTER_FORM_NAME,\n" +
                "\t\n" +
                "\t-- Фильтр\n" +
                "\tf.NAME as FILTER_NAME,\n" +
                "\t\n" +
                "\tf.ONSITE as ONSITE\n" +
                "\t\n" +
                "FROM FILTER f\n" +
                "LEFT OUTER JOIN FILTERTYPE ft ON f.FILTERTYPECODE = ft.CODE\n" +
                "LEFT OUTER JOIN FILTERFORM ff ON f.FILTERFORMID = ff.ID\n" +
                "WHERE ft.ID IS NOT NULL AND ff.ID IS NOT NULL";
        sqlAdapter.execute(INSERT_QUERY);
    }
}
