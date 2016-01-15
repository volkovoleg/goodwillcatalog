package ru.goodfil.catalog.adapters;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.core.*;
import ru.goodfil.catalog.adapters.core.impl.*;
import ru.goodfil.catalog.adapters.guice.CatalogAdaptersModule;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.Each;
import ru.goodfil.catalog.utils.MyCallable;
import ru.goodfil.catalog.utils.SearchMask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Адаптер служит для загрузки данных из файлов разделенных ;.
 * @author sazonovkirill@gmail.com
 * @version $Id: AccessTextAdapter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class AccessTextAdapter implements Adapter {
    private Injector catalogModule = Guice.createInjector(new CatalogAdaptersModule());

    @Override
    public void loadData(String workspace) throws IOException {

        Assert.notBlank(workspace);

        System.out.println("Loading brands");
        loadBrands(workspace);

        System.out.println("Loading filterTypes");
        loadFilterTypes(workspace);

        System.out.println("Loading filterForms");
        loadFilterForms(workspace);

        System.out.println("Loading vechicleTypes");
        loadVechicleTypes(workspace);

        System.out.println("Loading manufactors");
        loadManufactors(workspace);

        System.out.println("Loading series");
        loadSerias(workspace);

        System.out.println("Loading motors");
        loadMotors(workspace);

        System.out.println("Loading filters");
        loadFilters(workspace);

        System.out.println("Loading filtersAndMotors");
        loadFiltersAndMotors(workspace);

        System.out.println("Loading oes");
        loadOes(workspace);

        System.out.println("Loading filtersAndOes");
        loadFiltersAndOes(workspace);

        System.out.println("FINISHED!");
    }

    private <T1 extends Unique, T2> void loadTableCached(String workspace, String filename, Mapping mapping, Class<T1> entityClass, Class<T2> adapterClass, TransformationFilter<T1> filter) throws IOException {
        TableAdapter<T1> adapter = (TableAdapter<T1>) catalogModule.getInstance(adapterClass);

        final String columnDelimeter = "\t";
        BufferedReader reader = new BufferedReader(new FileReader(workspace + File.separator + filename));
        List<String> columnNames = new ArrayList<String>();

        TabFileImpl tabFile = null;

        try {
            long lineNumber = 0;
            String line = reader.readLine();

            while (line != null) {
                if (line.endsWith(columnDelimeter)) {
                    line += " ";
                }
                line = line.replace("&gt;", ">").replace("&amp;", "&");

                String[] arr = line.split(columnDelimeter);

                if (lineNumber == 0) {
                    for (int i = 0; i < arr.length; i++) {
                        columnNames.add(arr[i]);
                    }
                } else {
                    if (tabFile == null) {
                        tabFile = new TabFileImpl();
                        for (String columnName : columnNames) {
                            tabFile.addColumn(columnName);
                        }
                    }

                    if (arr.length != columnNames.size()) {
                        int l = 5;
                    }
                    tabFile.addRow(arr);
                    if (tabFile.getRowsCount() == 1000) {
                        System.out.println("Saving line " + lineNumber);
                        TabFileTransformation<T1> transformation = new TabFileTransformationImpl<T1>(entityClass);
                        List<T1> result = transformation.transform(tabFile, mapping, filter);

                        adapter.merge(result);

                        tabFile = null;
                    }
                }

                line = reader.readLine();
                lineNumber++;
            }

            System.out.println("Saving line " + lineNumber);
            TabFileTransformation<T1> vechicleTypeTransformation = new TabFileTransformationImpl<T1>(entityClass);
            List<T1> result = vechicleTypeTransformation.transform(tabFile, mapping, filter);

            adapter.merge(result);

            tabFile = null;
        } finally {
            reader.close();
        }
    }

    private <T1 extends Unique, T2> void loadTable(String workspace, String filename, Mapping mapping, Class<T1> entityClass, Class<T2> adapterClass) throws IOException {
        TableAdapter<T1> adapter = (TableAdapter<T1>) catalogModule.getInstance(adapterClass);

        TabFileReader reader = new TabFileReaderImpl(workspace + File.separator + filename);
        TabFile tabFile = reader.read();

        TabFileTransformation<T1> vechicleTypeTransformation = new TabFileTransformationImpl<T1>(entityClass);
        List<T1> result = vechicleTypeTransformation.transform(tabFile, mapping);

        for (T1 item : result) {
            adapter.save(item);
        }
    }

    private <T1 extends Unique> void loadTable(String workspace,
                                               String filename,
                                               Mapping mapping,
                                               Class<T1> entityClass,
                                               Class<? extends TableAdapter<T1>> adapterClass,
                                               TransformationFilter<T1> filter)
            throws IOException {
        TableAdapter<T1> adapter = catalogModule.getInstance(adapterClass);

        TabFileReader reader = new TabFileReaderImpl(workspace + File.separator + filename);
        TabFile tabFile = reader.read();

        TabFileTransformation<T1> vechicleTypeTransformation = new TabFileTransformationImpl<T1>(entityClass);
        List<T1> result = vechicleTypeTransformation.transform(tabFile, mapping, filter);

        for (T1 item : result) {
            try {
                adapter.merge(item);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Bad item: " + item);
            }
        }
    }

    private void loadManufactors(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(VechicleType.class);
        mapping.addColumnMapping("M_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("M_NAME", "name", String.class, StringCellConverter.get());
        mapping.addColumnMapping("M_TYPE", "vechicleTypeId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("ENABLED", "disabled", Boolean.class, InvertedBooleanCellConverter.get());

        loadTable(workspace, "MANUF.txt", mapping, Manufactor.class, ManufactorAdapter.class);
    }

    private void loadVechicleTypes(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(VechicleType.class);
        mapping.addColumnMapping("V_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("V_NAME", "name", String.class, StringCellConverter.get());

        TransformationFilter<VechicleType> filter = new TransformationFilter<VechicleType>() {
            @Override
            public VechicleType transformObject(VechicleType object) {
                if (object.getId().equals(0L)) {
                    return null;
                } else {
                    return object;
                }
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if (!"R".equals(row[2])) {
                    return null;
                } else {
                    return row;
                }
            }
        };

        loadTable(workspace, "VECHICLE_TYPE.txt", mapping, VechicleType.class, VechicleTypeAdapter.class, filter);
    }

    private void loadFilterTypes(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(FilterType.class);
        mapping.addColumnMapping("FT_TYPE", "code", String.class, StringCellConverter.get());
        mapping.addColumnMapping("FT_NAME", "name", String.class, StringCellConverter.get());

        TransformationFilter<FilterType> filter = new TransformationFilter<FilterType>() {
            @Override
            public FilterType transformObject(FilterType object) {
                return object;
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if (!"R".equals(row[0])) {
                    return null;
                } else {
                    return row;
                }
            }
        };

        loadTable(workspace, "F_TYPE_LOOKUP.txt", mapping, FilterType.class, FilterTypeAdapter.class, filter);
    }

    private void loadSerias(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(Seria.class);
        mapping.addColumnMapping("MS_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("M_ID", "manufactorId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("MS_NAME", "name", String.class, StringCellConverter.get());
        mapping.addColumnMapping("ENABLED", "disabled", Boolean.class, InvertedBooleanCellConverter.get());

        loadTable(workspace, "MSERIES.txt", mapping, Seria.class, SeriaAdapter.class, new TransformationFilter<Seria>() {
            @Override
            public Seria transformObject(Seria object) {
                return object;
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if (StringUtils.isBlank(row[1])) {
                    return null;
                } else {
                    return row;
                }
            }
        });
    }

    private void loadMotors(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(Motor.class);
        mapping.addColumnMapping("MT_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("MS_ID", "seriaId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("MT_NAME", "name", String.class, StringCellConverter.get());
        mapping.addColumnMapping("MT_ENGINE", "engine", String.class, StringCellConverter.get());
        mapping.addColumnMapping("MT_KW", "kw", String.class, StringCellConverter.get());
        mapping.addColumnMapping("MT_HP", "hp", String.class, StringCellConverter.get());
        mapping.addColumnMapping("MT_DATEF", "dateF", Date.class, DateCellConverter.get());
        mapping.addColumnMapping("MT_DATET", "dateT", Date.class, DateCellConverter.get());
        mapping.addColumnMapping("ENABLED", "disabled", Boolean.class, InvertedBooleanCellConverter.get());

        loadTableCached(workspace, "MOTORS.txt", mapping, Motor.class, MotorAdapter.class, new TransformationFilter<Motor>() {
            @Override
            public Motor transformObject(Motor object) {
                return object;
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if (StringUtils.isBlank(row[1])) {
                    return null;
                } else {
                    return row;
                }
            }
        });
    }

    private void loadFilterForms(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(FilterForm.class);
        mapping.addColumnMapping("FFL_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("FFL_SUB", "name", String.class, StringCellConverter.get());
        mapping.addColumnMapping("FFL_TYPE", "filterTypeCode", String.class, StringCellConverter.get());
        mapping.addColumnMapping("A", "aParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("B", "bParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("C", "cParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("D", "dParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("E", "eParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("F", "fParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("G", "gParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("H", "hParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("BP", "bpParam", String.class, BooleanCellConverter.get());
        mapping.addColumnMapping("NR", "nrParam", String.class, BooleanCellConverter.get());

        loadTable(workspace, "F_FORM_LOOKUP.txt", mapping, FilterForm.class, FilterFormAdapter.class, new TransformationFilter<FilterForm>() {
            @Override
            public FilterForm transformObject(FilterForm object) {
                return object;
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if ("R".equals(row[1])) {
                    return row;
                } else {
                    return null;
                }
            }
        });
    }

    private void loadFilters(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(Filter.class);
        mapping.addColumnMapping("F_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("F_TYPE", "filterTypeCode", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_NAME", "name", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_EAN", "ean", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_OE", "oe", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_IMAGE", "image", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_A", "aParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_B", "bParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_C", "cParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_D", "dParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_E", "eParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_F", "fParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_G", "gParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("F_H", "hParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("PB", "pbParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("NR", "nrParam", String.class, StringCellConverter.get());
        mapping.addColumnMapping("FFL_ID", "filterFormId", Long.class, NullableLongCellConverter.get());
        mapping.addColumnMapping("ENABLED", "disabled", Boolean.class, InvertedBooleanCellConverter.get());

        loadTableCached(workspace, "FILTERS.txt", mapping, Filter.class, FilterAdapter.class, null);
    }

    private void loadBrands(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(Brand.class);

        mapping.addColumnMapping("COMP_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("COMP_NAME", "name", String.class, StringCellConverter.get());

        loadTableCached(workspace, "COMP_NAMES.txt", mapping, Brand.class, BrandAdapter.class, null);
    }

    private void loadOes(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(Oe.class);

        mapping.addColumnMapping("COMPOE_ID", "id", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("COMP_ID", "brandId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("OE_NAME", "name", String.class, StringCellConverter.get());

        loadTableCached(workspace, "COMP_OE.txt", mapping, Oe.class, OeAdapter.class, new TransformationFilter<Oe>() {
            @Override
            public Oe transformObject(Oe object) {
                if (StringUtils.isBlank(object.getName())) {
                    return null;
                } else {
                    return object;
                }
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                return row;
            }
        });
    }

    private void loadFiltersAndMotors(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(FiltersAndMotors.class);
        mapping.addColumnMapping("MT_ID", "motorId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("F_ID", "filterId", Long.class, LongCellConverter.get());
        loadTableCached(workspace, "F_MT.txt", mapping, FiltersAndMotors.class, FiltersAndMotorsAdapter.class, null);
    }

    private void loadFiltersAndOes(String workspace) throws IOException {
        Mapping mapping = new MappingImpl(FiltersAndOes.class);
        mapping.addColumnMapping("F_ID", "filterId", Long.class, LongCellConverter.get());
        mapping.addColumnMapping("COMPOE_ID", "oeId", Long.class, LongCellConverter.get());

        loadTableCached(workspace, "F_COMP.txt", mapping, FiltersAndOes.class, FiltersAndOesAdapter.class, new TransformationFilter<FiltersAndOes>() {
            @Override
            public FiltersAndOes transformObject(FiltersAndOes object) {
                if (object.getFilterId() != null && object.getOeId() != null) {
                    return object;
                } else {
                    return null;
                }
            }

            @Override
            public String[] transformRow(TabFile tabFile, String[] row) {
                if (StringUtils.isBlank(row[0]) || (StringUtils.isBlank(row[1]))) {
                    return null;
                } else {
                    return row;
                }
            }
        });
    }

    public static void main(String[] args) throws IOException {
        //
        //  Скрипт для проливки базы с пустыми @GeneratedValue
        //
//        Injector catalogModule = Guice.createInjector(new CatalogModule());
//        FilterTypeAdapter adapter = catalogModule.getInstance(FilterTypeAdapter.class);
//        FilterType ft = new FilterType();
//        ft.setCode("Qwe");
//        ft.setName("Asd");
//        adapter.save(ft);
//        adapter.delete(ft.getId());

        //
        //  А вот это уже с нормальными GeneratedValue
        //
//        String workspace = "D:\\Projects\\catalog\\sourceDb";
//        AccessTextAdapter adapter = new AccessTextAdapter();
//        adapter.loadData(workspace);

        //
        //  Заполняем поля для поиска
        //

        buildSearchIndex();
    }

    public static void buildSearchIndex() {
        List<Filter> allFilters = Services.getFiltersService().getFilters();
        for (Filter filter : allFilters) {
            String name = filter.getName();
            String searchName = SearchMask.mask(name);
            filter.setSearchName(searchName);
            Services.getFiltersService().updateFilter(filter);
        }

        List<Brand> allBrands = Services.getAnalogsService().getBrands();
        for (Brand brand : allBrands) {
            String name = brand.getName();
            String searchName = SearchMask.mask(name);
            brand.setSearchName(searchName);
            Services.getAnalogsService().updateBrand(brand);
        }

        List<Oe> oes = Services.getAnalogsService().getOes();
        Each.count(oes, 1000,
                new MyCallable<Oe>() {
                    @Override
                    public void call(Oe arg) {
                        String name = arg.getName();
                        String searchName = SearchMask.mask(name);
                        arg.setSearchName(searchName);
                    }
                },
                new MyCallable<Collection<Oe>>() {
                    @Override
                    public void call(Collection<Oe> arg) {
                        Services.getAnalogsService().updateOes(arg);
                    }
                }
        );
    }
}
