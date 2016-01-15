/*
 * Created by JFormDesigner on Mon Sep 19 12:10:16 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.adapters.BigFilterTableAdapter;
import ru.goodfil.catalog.ui.adapters.BigMotorTableAdapter;
import ru.goodfil.catalog.ui.adapters.sorters.BigMotorTableSorter;
import ru.goodfil.catalog.ui.cellrenderer.FilterTableCellRenderer;
import ru.goodfil.catalog.ui.cellrenderer.MannTablesCellRenderer;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.ui.swing.resolver.DefaultResolver;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.JoinOptions;
import ru.goodfil.catalog.utils.ListAsMap;

import javax.swing.*;
import javax.swing.event.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FiltersPanel.java 172 2013-10-26 10:53:33Z chezxxx@gmail.com $
 */
public class FiltersPanel extends JPanel implements ClipboardOwner {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private final CarsService carsService = Services.getCarsService();
    private final FiltersService filtersService = Services.getFiltersService();
    private final AnalogsService analogsService = Services.getAnalogsService();

    private final Resolver filterTypeResolver;
    private final Resolver filterFormResolver;

    private final SelectionInList<ManufactorModel> manufactors;
    private final SelectionInList<SeriaModel> series;

    private final ArrayListModel<Filter> filters = new ArrayListModel<Filter>();
    private final ArrayListModel<BigMotorTableAdapter.MotorModel> motors = new ArrayListModel<BigMotorTableAdapter.MotorModel>();
    private final ArrayListModel<BigMotorTableAdapter.MotorModel> allMotors = new ArrayListModel<BigMotorTableAdapter.MotorModel>();

    private final ArrayListModel<OeModel> oes = new ArrayListModel<OeModel>();
    private final ArrayListModel<OeModel> allOes = new ArrayListModel<OeModel>();

    public void doAddFilter(Filter filter) {
        filtersService.saveFilter(filter);
        doSearchFilters();
    }

    public void doSearchFilters() {
        if (!StringUtils.isBlank(tbFilterCode.getText())) {
            filters.clear();
            filters.addAll(filtersService.getFiltersByName(tbFilterCode.getText()));
        }
    }

    /**
     * Прикрепить мотор к фильтру.
     *
     * @param filterId идентификатор фильтра
     */
    public void doAttachMotorToFilter(Set<Long> motorIds, Long filterId) {
        carsService.doAttachMotorsToFilter(filterId, motorIds);
        reReadMotors(filterId);
    }

    /**
     * Открепить мотор от фильтра.
     *
     * @param filterId идентификатор фильтра
     */
    public void doDetachMotorFromFilter(Set<Long> motorIds, Long filterId) {
        carsService.doDetachMotorsFromFilter(filterId, motorIds);
        reReadMotors(filterId);
    }

    public void doSearchOes(@NotNull @NotBlank String oeName) {
        reReadAllOes(oeName);
    }

    /**
     * Прикрепить ОЕ к фильтру.
     *
     * @param oeIds    идентификаторы ОЕ.
     * @param filterId идентификатор фильтра.
     */
    public void doAttachOesToFilter(@NotNull @NotEmpty Set<Long> oeIds, @NotNull Long filterId) {
        carsService.doAttachOesToFilter(filterId, oeIds);
        reReadOes(filterId);
    }

    /**
     * Открепить ОЕ от фильтра.
     *
     * @param oeIds    идентификаторы ОЕ.
     * @param filterId идентификтаор фильтра.
     */
    public void doDetachOesFromFitler(@NotNull @NotEmpty Set<Long> oeIds, @NotNull Long filterId) {
        carsService.doDetachOesFromFilter(filterId, oeIds);
        reReadOes(filterId);
    }

    public void doUpdateFilter(Filter filter) {
        filtersService.updateFilter(filter);
        doSearchFilters();
    }

    public void doDeleteFilter(Long filterId) {
        filtersService.deleteFilter(filterId);
        doSearchFilters();
    }

    public FiltersPanel() {
        initComponents();

        filterTypeResolver = createFilterTypeResolver();
        filterFormResolver = createFilterFormResolver();

        ManufactorModelAdapter manufactorModelAdapter = new ManufactorModelAdapter();
        List<Manufactor> theManufactors = carsService.getAllManufactors();
        for (Manufactor manufactor : theManufactors) {
            manufactorModelAdapter.addManufactor(manufactor);
        }
        manufactors = new SelectionInList<ManufactorModel>(manufactorModelAdapter.getList());

        series = new SelectionInList<SeriaModel>();

        cbManufactors.setModel(new ComboBoxAdapter<ManufactorModel>(manufactors));
        cbManufactors.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ManufactorModel manufactorModel = (ManufactorModel) e.getItem();
                    reReadSeries(manufactorModel.getManufactorIds());
                }
            }
        });

        cbSeria.setModel(new ComboBoxAdapter<SeriaModel>(series));
        tblAllMotors.setModel(new BigMotorTableAdapter(allMotors));
        tblAllMotors.setRowSorter(new BigMotorTableSorter((BigMotorTableAdapter) tblAllMotors.getModel()));
        tblAllMotors.setRowHeight(20);
        tblAllMotors.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                adjustButtonsEnabled();
            }
        });

        tblMotors.setModel(new BigMotorTableAdapter(motors));
        tblMotors.setRowSorter(new BigMotorTableSorter((BigMotorTableAdapter) tblMotors.getModel()));
        tblMotors.setRowHeight(20);
        tblMotors.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                adjustButtonsEnabled();
            }
        });

        final BigFilterTableAdapter tblFiltersAdapter = new BigFilterTableAdapter(filters,
                filterTypeResolver,
                filterFormResolver);

        tblFiltersAdapter.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    BigFilterTableAdapter adapter = (BigFilterTableAdapter) e.getSource();
                    int row = e.getFirstRow();

                    Filter filter = adapter.getRow(row);
                    filtersService.updateFilter(filter);
                }
            }
        });

        tblFilters.setUpdateSelectionOnSort(true);
        tblFilters.setModel(tblFiltersAdapter);
        tblFilters.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                adjustButtonsEnabled();

                Long filterId = getSelectedFilterId();
                reReadMotors(filterId);
                reReadOes(filterId);
            }
        });
        tblFilters.setRowHeight(20);

        tblAllOes.setModel(new OeModelTableModel(allOes));
        tblAllOes.setRowHeight(20);
        tblAllOes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                adjustButtonsEnabled();
            }
        });

        tblOes.setModel(new OeModelTableModel(oes));
        tblOes.setRowHeight(20);
        tblOes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                adjustButtonsEnabled();
            }
        });
        //В зависимости от параметра подсвечивать или нет строки из манна
        if (System.getProperty("catalog.mode.fromMann") != null && System.getProperty("catalog.mode.fromMann").equals("1")) {
            tblFilters.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.FILTER));
            tblMotors.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.AMOTOR));
            tblAllMotors.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.AMOTOR));
            tblOes.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.OE));
            tblAllOes.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.OE));
        } else {
            tblFilters.setDefaultRenderer(Object.class, new FilterTableCellRenderer());
        }
        adjustButtonsEnabled();
//        globalHotkeys();
    }

    private void globalHotkeys() {
        class FiltersPanelGlobalHotkey extends EventQueue {
            protected void dispatchEvent(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent) event;
                    if (keyEvent.getKeyCode() == KeyEvent.VK_F1 && keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                    } else {
                        super.dispatchEvent(event);
                    }
                } else {
                    super.dispatchEvent(event);
                }
            }
        }

        EventQueue ev = Toolkit.getDefaultToolkit().getSystemEventQueue();
        ev.push(new FiltersPanelGlobalHotkey());
    }

    private void reReadSeries(Set<Long> manufactorIds) {
        List<Seria> theSeries = carsService.getSeriesByManufactorIds(manufactorIds);
        SeriaModelAdapter adapter = new SeriaModelAdapter();
        for (Seria theSeria : theSeries) {
            adapter.addSeria(theSeria);
        }
        series.setList(adapter.getList());
    }

    private void reReadAllMotors(MotorSearchModel searchModel) {
        if (searchModel.getManufactorsIds() == null && searchModel.getSeriesIds() == null) {
            return;
        }

        allMotors.clear();

        //
        //  Поиск
        //
        List<Motor> theMotors = null;

        if (searchModel.getSeriesIds() != null) {
            theMotors = carsService.getMotorsBySeriaIds(searchModel.getSeriesIds());
        } else {
            theMotors = carsService.getMotorsByManufactorsIds(searchModel.getManufactorsIds());
        }

        Assert.notNull(theMotors);

        //
        //  Начитка серий
        //
        Set<Long> seriesIds = new HashSet<Long>();
        for (Motor theMotor : theMotors) {
            seriesIds.add(theMotor.getSeriaId());
        }

        List<Seria> series = carsService.getSeriesByIds(seriesIds);
        Map<Long, Seria> seriesMap = ListAsMap.get(series);

        Set<Long> manufactorsIds = new HashSet<Long>();
        for (Seria theSeria : series) {
            manufactorsIds.add(theSeria.getManufactorId());
        }

        List<Manufactor> manufactors = carsService.getManufactorsByIds(manufactorsIds);
        Map<Long, Manufactor> manufactorsMap = ListAsMap.get(manufactors);

        List<BigMotorTableAdapter.MotorModel> notFilteredMotors = new ArrayList<BigMotorTableAdapter.MotorModel>();
        for (Motor theMotor : theMotors) {
            Seria seria = seriesMap.get(theMotor.getSeriaId());
            Manufactor manufactor = manufactorsMap.get(seria.getManufactorId());

            BigMotorTableAdapter.MotorModel motorModel = new BigMotorTableAdapter.MotorModel();
            motorModel.setSeriaId(seria.getId());
            motorModel.setManufactorId(manufactor.getId());
            motorModel.setMotorId(theMotor.getId());

            motorModel.setSeria(seria.getName());
            motorModel.setManufactor(manufactor.getName());
            motorModel.setName(theMotor.getName());
            motorModel.setEngine(theMotor.getEngine());
            motorModel.setKw(theMotor.getKw());
            motorModel.setHp(theMotor.getHp());
            motorModel.setMannStatus(theMotor.getMannStatus());
            motorModel.setDateF(date(theMotor.getDateF()));
            motorModel.setDateT(date(theMotor.getDateT()));

            notFilteredMotors.add(motorModel);
        }

        //
        //  Фильтрация
        //
        for (BigMotorTableAdapter.MotorModel motorModel : notFilteredMotors) {
            if (!StringUtils.isBlank(searchModel.getSeriaName()) &&
                    !mask(motorModel.getSeria()).startsWith(mask(searchModel.getSeriaName()))) {
                continue;
            }
            if (!StringUtils.isBlank(searchModel.getModelName()) &&
                    !mask(motorModel.getName()).startsWith(mask(searchModel.getModelName()))) {
                continue;
            }
            if (!StringUtils.isBlank(searchModel.getEngineName()) &&
                    !mask(motorModel.getEngine()).startsWith(mask(searchModel.getEngineName()))) {
                continue;
            }
            if (!StringUtils.isBlank(searchModel.getDateF()) &&
                    !mask(motorModel.getDateF()).contains(mask(searchModel.getDateF()))) {
                continue;
            }
            if (!StringUtils.isBlank(searchModel.getDateT()) &&
                    !mask(motorModel.getDateT()).contains(mask(searchModel.getDateT()))) {
                continue;
            }

            allMotors.add(motorModel);
        }
    }

    private static String mask(String source) {
        StringBuilder sb = new StringBuilder();
        for (char c : source.toCharArray()) {
            if (Character.isLetter(c) || Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString().trim().toUpperCase();
    }

    private String date(Date date) {
        if (date == null) return "";
        else return sdf.format(date);
    }

    private void reReadMotors(Long filterId) {
        motors.clear();
        if (filterId != null) {
            List<Motor> theMotors = filtersService.getMotorsByFilterId(filterId);

            Set<Long> seriesIds = new HashSet<Long>();
            for (Motor theMotor : theMotors) {
                seriesIds.add(theMotor.getSeriaId());
            }

            List<Seria> series = carsService.getSeriesByIds(seriesIds);
            Map<Long, Seria> seriesMap = ListAsMap.get(series);

            Set<Long> manufactorsIds = new HashSet<Long>();
            for (Seria theSeria : series) {
                manufactorsIds.add(theSeria.getManufactorId());
            }

            List<Manufactor> manufactors = carsService.getManufactorsByIds(manufactorsIds);
            Map<Long, Manufactor> manufactorsMap = ListAsMap.get(manufactors);


            for (Motor theMotor : theMotors) {
                Seria seria = seriesMap.get(theMotor.getSeriaId());
                if(seria!=null){
                Manufactor manufactor = manufactorsMap.get(seria.getManufactorId());

                BigMotorTableAdapter.MotorModel motorModel = new BigMotorTableAdapter.MotorModel();
                motorModel.setSeriaId(seria.getId());
                motorModel.setManufactorId(manufactor.getId());
                motorModel.setMotorId(theMotor.getId());

                motorModel.setSeria(seria.getName());
                motorModel.setManufactor(manufactor.getName());
                motorModel.setName(theMotor.getName());
                motorModel.setEngine(theMotor.getEngine());
                motorModel.setKw(theMotor.getKw());
                motorModel.setHp(theMotor.getHp());
                motorModel.setMannStatus(theMotor.getMannStatus());
                motorModel.setDateF(date(theMotor.getDateF()));
                motorModel.setDateT(date(theMotor.getDateT()));

                motors.add(motorModel);
                }
            }
        }

        Collections.sort(motors, new Comparator<BigMotorTableAdapter.MotorModel>() {
            @Override
            public int compare(BigMotorTableAdapter.MotorModel o1, BigMotorTableAdapter.MotorModel o2) {
                int r = o1.getManufactor().compareTo(o2.getManufactor());
                if (r != 0) return r;
                else {
                    r = o1.getSeria().compareTo(o2.getSeria());
                    if (r != 0) return r;
                    else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            }
        });
    }

    private void reReadOes(Long filterId) {
        this.oes.clear();

        if (filterId != null) {
            List<Oe> oes = filtersService.getOesByFilter(filterId);

            //
            // EXPERIMENTAL
            //
            //  Сделаем здесь следующее: будем удалять из базы дублирющиеся ОЕ
            //
            {
                Set<Long> oesToDelete = new HashSet<Long>();
                Set<String> oesHash = new HashSet<String>();
                for (Oe oe : oes) {
                    String oeHash = oe.getName().trim() + String.valueOf(oe.getBrandId());
                    if (oesHash.contains(oeHash)) {
                        oesToDelete.add(oe.getId());
                    } else {
                        oesHash.add(oeHash);
                    }
                }

                if (oesToDelete.size() > 0) {
                    analogsService.removeOes(oesToDelete);
                    oes = filtersService.getOesByFilter(filterId);

                    System.out.println("Removed oes: " + oesToDelete.size());
                }
            }


            Set<Long> brandsIds = new HashSet<Long>();
            for (Oe oe : oes) {
                brandsIds.add(oe.getBrandId());
            }
            List<Brand> brands = filtersService.getBrandsByIds(brandsIds);
            Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
            for (Brand brand : brands) {
                brandMap.put(brand.getId(), brand);
            }


            for (Oe oe : oes) {
                OeModel oeModel = new OeModel();
                oeModel.setOeId(oe.getId());
                oeModel.setOe(oe.getName());
                oeModel.setMannStatus(oe.getMannStatus());
                Brand brand = brandMap.get(oe.getBrandId());
                oeModel.setBrand(brand.getName());
                oeModel.setBrandId(brand.getId());
                oeModel.setMannStatus(oe.getMannStatus());
                this.oes.add(oeModel);
            }
        }
    }

    private void reReadAllOes(String oeName) {
        List<Oe> oes = filtersService.getOesByName(oeName);
        Set<Long> brandsIds = new HashSet<Long>();
        for (Oe oe : oes) {
            brandsIds.add(oe.getBrandId());
        }
        List<Brand> brands = filtersService.getBrandsByIds(brandsIds);
        Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
        for (Brand brand : brands) {
            brandMap.put(brand.getId(), brand);
        }

        this.allOes.clear();
        for (Oe oe : oes) {
            OeModel oeModel = new OeModel();
            oeModel.setOeId(oe.getId());
            oeModel.setOe(oe.getName());
            oeModel.setMannStatus(oe.getMannStatus());
            Brand brand = brandMap.get(oe.getBrandId());
            oeModel.setBrand(brand.getName());
            oeModel.setBrandId(brand.getId());
            oeModel.setMannStatus(oe.getMannStatus());
            this.allOes.add(oeModel);
        }
    }

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

    private Resolver createFilterFormResolver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "");
        map.put("null", "");
        List<FilterForm> filterForms = carsService.getFilterForms();
        for (FilterForm filterForm : filterForms) {
            map.put(filterForm.getId().toString(), filterForm.getName());
        }
        return new DefaultResolver(map);
    }

    private ManufactorModel getSelectedManufactor() {
        return manufactors.getSelection();
    }

    private SeriaModel getSelectedSeria() {
        return series.getSelection();
    }

    private Long getSelectedFilterId() {
        if (getSelectedFilter() == null) return null;
        else return getSelectedFilter().getId();
    }

    private Filter getSelectedFilter() {
        if (tblFilters.getSelectedRowCount() == 1) {
            int index = tblFilters.getSelectedRow();
            index = tblFilters.convertRowIndexToModel(index);

            return ((BigFilterTableAdapter) tblFilters.getModel()).getRow(index);
        }
        return null;
    }

    private List<Filter> getSelectedFilters() {
        List<Filter> result = new ArrayList<Filter>();
        for (int index : tblFilters.getSelectedRows()) {
            index = tblFilters.convertRowIndexToModel(index);

            Filter filter = ((BigFilterTableAdapter) tblFilters.getModel()).getRow(index);
            result.add(filter);
        }
        return result;
    }

    private List<BigMotorTableAdapter.MotorModel> getSelectedAllMotors() {
        List<BigMotorTableAdapter.MotorModel> result = new ArrayList<BigMotorTableAdapter.MotorModel>();

        int[] rowIndices = tblAllMotors.getSelectedRows();
        for (int rowIndex : rowIndices) {
            rowIndex = tblAllMotors.convertRowIndexToModel(rowIndex);
            result.add(((BigMotorTableAdapter) tblAllMotors.getModel()).getRow(rowIndex));
        }

        return result;
    }

    private Set<Long> getSelectedAllMotorIds() {
        Set<Long> result = new HashSet<Long>();
        for (BigMotorTableAdapter.MotorModel motor : getSelectedAllMotors()) {
            result.add(motor.getMotorId());
        }
        return result;
    }

    private List<BigMotorTableAdapter.MotorModel> getSelectedMotors() {
        List<BigMotorTableAdapter.MotorModel> result = new ArrayList<BigMotorTableAdapter.MotorModel>();

        int[] rowIndices = tblMotors.getSelectedRows();
        for (int rowIndex : rowIndices) {
            rowIndex = tblMotors.convertRowIndexToModel(rowIndex);
            result.add(((BigMotorTableAdapter) tblMotors.getModel()).getRow(rowIndex));
        }

        return result;
    }

    private Set<Long> getSelectedMotorIds() {
        Set<Long> result = new HashSet<Long>();
        for (BigMotorTableAdapter.MotorModel motor : getSelectedMotors()) {
            result.add(motor.getMotorId());
        }
        return result;
    }

    private List<OeModel> getSelectedAllOes() {
        List<OeModel> result = new ArrayList<OeModel>();

        int[] rowIndices = tblAllOes.getSelectedRows();
        for (int rowIndex : rowIndices) {
            rowIndex = tblAllOes.convertRowIndexToModel(rowIndex);
            result.add(((OeModelTableModel) tblAllOes.getModel()).getRow(rowIndex));
        }

        return result;
    }

    private Set<Long> getSelectedAllOeIds() {
        Set<Long> result = new HashSet<Long>();
        for (OeModel oeModel : getSelectedAllOes()) {
            result.add(oeModel.getOeId());
        }
        return result;
    }

    private List<OeModel> getSelectedOes() {
        List<OeModel> result = new ArrayList<OeModel>();

        int[] rowIndices = tblOes.getSelectedRows();
        for (int rowIndex : rowIndices) {
            rowIndex = tblOes.convertRowIndexToModel(rowIndex);
            result.add(((OeModelTableModel) tblOes.getModel()).getRow(rowIndex));
        }

        return result;
    }

    private Set<Long> getSelectedOeIds() {
        Set<Long> result = new HashSet<Long>();
        for (OeModel oeModel : getSelectedOes()) {
            result.add(oeModel.getOeId());
        }
        return result;
    }

    /**
     * Поиск по фильтрам
     */
    private void btnSearchFilterActionPerformed(ActionEvent e) {
        if (StringUtils.isBlank(tbFilterCode.getText())) {
            UIUtils.warning("Необходимо ввести код фильтра для осуществления поиска");
            return;
        }

        doSearchFilters();
        adjustButtonsEnabled();
    }

    /**
     * Добавление фильтра
     */
    private void btnCreateFilterActionPerformed(ActionEvent e) {
        FilterWindow filterWindow = new FilterWindow();
        filterWindow.setVisible(true);

        if (filterWindow.getDialogResult() == DialogResult.OK) {
            Filter filter = filterWindow.release();
            doAddFilter(filter);
        }

        adjustButtonsEnabled();
    }

    private void btnEditFilterActionPerformed(ActionEvent e) {
        Filter filter = getSelectedFilter();
        if (filter == null) {
            UIUtils.warning("Необходимо выбрать фильтр для редактирования");
            return;
        }

        FilterWindow filterWindow = new FilterWindow(filter);
        filterWindow.setVisible(true);

        if (filterWindow.getDialogResult() == DialogResult.OK) {
            Filter filter2 = filterWindow.release();
            doUpdateFilter(filter2);
        }

        doSearchFilters();
        adjustButtonsEnabled();
    }

    private void btnRemoveFilterActionPerformed(ActionEvent e) {
        Filter filter = getSelectedFilter();
        if (filter == null) {
            UIUtils.warning("Необходимо выбрать фильтр для удаления");
            return;
        }

        if (UIUtils.askDelete()) {
            doDeleteFilter(filter.getId());
        }

        adjustButtonsEnabled();
    }

    private void btnSearchMotorsActionPerformed(ActionEvent e) {
        ManufactorModel manufactorModel = (ManufactorModel) cbManufactors.getSelectedItem();
        SeriaModel seriaModel = (SeriaModel) cbSeria.getSelectedItem();

        if (manufactorModel == null && seriaModel == null) {
            UIUtils.info("Необходимо задать критерии поиска");
            return;
        }

        MotorSearchModel searchModel = new MotorSearchModel();
        if (manufactorModel != null) {
            searchModel.setManufactorsIds(manufactorModel.getManufactorIds());
        }
        if (seriaModel != null) {
            searchModel.setSeriesIds(seriaModel.getSeriaIds());
        }
        searchModel.setSeriaName(tbSeria.getText());
        searchModel.setModelName(tbModel.getText());
        searchModel.setEngineName(tbEngine.getText());
        searchModel.setDateF(tbDateF.getText());
        searchModel.setDateT(tbDateT.getText());

        if (!searchModel.isEmpty()) {
            reReadAllMotors(searchModel);
        }

        adjustButtonsEnabled();
    }

    private void btnAttachMotorActionPerformed(ActionEvent e) {
        if (tblAllMotors.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать мотор!");
            return;
        }

        Set<Long> motorIds = getSelectedAllMotorIds();
        Long filterId = getSelectedFilterId();

        if (motorIds.size() == 1) {
            boolean result = filtersService.getFilterHasMotor(filterId, motorIds.iterator().next());
            if (result) {
                UIUtils.warning("Данная позиция уже присутствует в списке");
                return;
            }
        } else {
            boolean result = filtersService.getFilterHasAtLeastOneMotor(filterId, motorIds);
            if (result) {
                boolean r2 = UIUtils.askContinue("Часть позиций уже присутствует в списке и не будут прикреплены повторно");
                if (!r2) {
                    return;
                }
            }
        }

        doAttachMotorToFilter(motorIds, filterId);

        adjustButtonsEnabled();
    }

    private void btnDetachMotorActionPerformed(ActionEvent e) {
        if (tblMotors.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать мотор!");
            return;
        }

        Set<Long> motorIds = getSelectedMotorIds();
        Long filterId = getSelectedFilterId();

        doDetachMotorFromFilter(motorIds, filterId);

        adjustButtonsEnabled();
    }

    private void btnOeSearchActionPerformed(ActionEvent e) {
        if (StringUtils.isBlank(tbOe.getText())) {
            UIUtils.warning("Введите значение для поиска в поле ОЕ");
            return;
        }

        doSearchOes(tbOe.getText());

        adjustButtonsEnabled();
    }

    /**
     * Прикрепить ОЕ к фильтру
     */
    private void btnAttachOeActionPerformed(ActionEvent e) {
        if (tblAllOes.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать ОЕ для прикрепления к фильтру");
            return;
        }

        Set<Long> oeIds = getSelectedAllOeIds();
        Long filterId = getSelectedFilterId();

        if (oeIds.size() == 0) {
            UIUtils.warning("Необходимо выбрать ОЕ для прикрепления к фильтру");
            return;
        }
        if (filterId == null) {
            UIUtils.warning("Необходимо выбрать фильтр, к которому нужно прикрепить ОЕ");
            return;
        }

        if (oeIds.size() == 1) {
            boolean result = filtersService.getFilterHasOe(filterId, oeIds.iterator().next());
            if (result) {
                UIUtils.warning("Данная позиция уже присутствует в списке");
                return;
            }
        } else {
            boolean result = filtersService.getFilterHasAtLeastOneOe(filterId, oeIds);
            if (result) {
                boolean r2 = UIUtils.askContinue("Часть позиций уже присутствует в списке и не будут прикреплены повторно");
                if (!r2) {
                    return;
                }
            }
        }

        doAttachOesToFilter(oeIds, filterId);

        adjustButtonsEnabled();
    }

    /**
     * Открепить ОЕ от фильтру
     */
    private void btnDetachOeActionPerformed(ActionEvent e) {
        if (tblOes.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать ОЕ для открепления");
            return;
        }

        Set<Long> oeIds = getSelectedOeIds();
        Long filterId = getSelectedFilterId();

        doDetachOesFromFitler(oeIds, filterId);

        adjustButtonsEnabled();
    }

    private void tbFilterCodeKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchFilterActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbFilterCode.setText("");
        }

        adjustButtonsEnabled();
    }

    private void tbFilterCodeFocusGained(FocusEvent e) {
        adjustButtonsEnabled();
    }

    private void cbManufactorsActionPerformed(ActionEvent e) {
        adjustButtonsEnabled();
    }

    private void cbSeriaActionPerformed(ActionEvent e) {
        adjustButtonsEnabled();
    }

    private void tblFiltersKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            btnCreateFilterActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            btnRemoveFilterActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            btnEditFilterActionPerformed(null);
        }
    }

    private void tblMotorsKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            btnDetachMotorActionPerformed(null);
        }
    }

    private void tblAllMotorsKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            btnAttachMotorActionPerformed(null);
        }
    }

    private void tblOesKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            btnDetachOeActionPerformed(null);
        }
    }

    private void tblAllOesKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            btnAttachOeActionPerformed(null);
        }
    }

    private void tbOeKeyTyped(KeyEvent e) {
        if (e != null) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnOeSearchActionPerformed(null);
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                tbOe.setText("");
            }
        }

        adjustButtonsEnabled();
    }

    private void cbManufactorsKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        }
    }

    private void cbSeriaKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        }
    }

    private void tbOeFocusGained(FocusEvent e) {
        tbOeKeyTyped(null);
    }

    private void tblFiltersMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (filters.size() > 0) {
                tablesPopupMenu.show(tblFilters, e.getX(), e.getY());
            }
        }
    }

    private void tblMotorsMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (motors.size() > 0) {
                tablesPopupMenu.show(tblMotors, e.getX(), e.getY());
            }
        }
    }

    private void tblAllMotorsMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (allMotors.size() > 0) {
                tablesPopupMenu.show(tblAllMotors, e.getX(), e.getY());
            }
        }
    }

    private void tblOesMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (oes.size() > 0) {
                tablesPopupMenu.show(tblOes, e.getX(), e.getY());
            }
        }
    }

    private void tblAllOesMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (allOes.size() > 0) {
                tablesPopupMenu.show(tblAllOes, e.getX(), e.getY());
            }
        }
    }

    /**
     * Операция объединения фильтров
     */
    private void btnJoinFiltersActionPerformed(ActionEvent e) {
        List<Filter> filters = getSelectedFilters();
        if (filters.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(filters);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            carsService.doJoinFilters(masterItemId, slavesItemsIds, joinOptions);
            doSearchFilters();
        }
    }

    private static void filterToExcel(@NotNull Filter filter, @NotNull StringBuilder sb, @NotNull Resolver filterTypeResolver, @NotNull Resolver filterFormResolver) {
        Assert.notNull(filter);
        Assert.notNull(sb);
        Assert.notNull(filterTypeResolver);
        Assert.notNull(filterFormResolver);

        sb.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                filterTypeResolver.resolve(filter.getFilterTypeCode()),
                filterFormResolver.resolve(filter.getFilterFormId().toString()),
                filter.getName(),
                filter.getEan(),
                filter.getaParam(),
                filter.getbParam(),
                filter.getcParam(),
                filter.getdParam(),
                filter.geteParam(),
                filter.getfParam(),
                filter.getgParam(),
                filter.gethParam(),
                filter.getPbParam(),
                filter.getNrParam(),
                filter.getImage()));

    }

    private static void motorModelToExcel(@NotNull BigMotorTableAdapter.MotorModel motor, @NotNull StringBuilder sb, @NotNull SimpleDateFormat sdf) {
        Assert.notNull(motor);
        Assert.notNull(sb);
        Assert.notNull(sdf);

        sb.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                motor.getManufactor(),
                motor.getSeria(),
                motor.getName(),
                motor.getEngine(),
                motor.getKw(),
                motor.getHp(),
                motor.getDateF(),
                motor.getDateT()));
    }

    private static void motorToExcel(@NotNull Motor motor, @NotNull StringBuilder sb, @NotNull SimpleDateFormat sdf) {
        Assert.notNull(motor);
        Assert.notNull(sb);
        Assert.notNull(sdf);

        String dateF = motor.getDateF() != null ? sdf.format(motor.getDateF()) : "";
        String dateT = motor.getDateF() != null ? sdf.format(motor.getDateT()) : "";

        sb.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\n",
                motor.getName(),
                motor.getEngine(),
                motor.getKw(),
                motor.getHp(),
                dateF,
                dateT));
    }

    private static void oeToExcel(@NotNull Oe oe, @NotNull StringBuilder sb) {
        Assert.notNull(oe);
        Assert.notNull(sb);

        sb.append(String.format("%s\n",
                oe.getName()));
    }

    private static void oeModelToExcel(@NotNull OeModel oe, @NotNull StringBuilder sb) {
        Assert.notNull(oe);
        Assert.notNull(sb);

        sb.append(String.format("%s\t%s\n",
                oe.getOe(),
                oe.getBrand()));
    }

    /**
     * Скопировать в системный буффер обмена (для Excel) все строки выделенной таблицы.
     */
    private void miCopyToExcelActionPerformed(ActionEvent e) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        final StringBuilder sb = new StringBuilder();

        if (tablesPopupMenu.getInvoker() == tblFilters) {
            for (Filter filter : filters) {
                filterToExcel(filter, sb, filterTypeResolver, filterFormResolver);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblMotors) {
            for (BigMotorTableAdapter.MotorModel motor : motors) {
                motorModelToExcel(motor, sb, sdf);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblAllMotors) {
            for (BigMotorTableAdapter.MotorModel motor : allMotors) {
                motorModelToExcel(motor, sb, sdf);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblOes) {
            for (OeModel oe : oes) {
                oeModelToExcel(oe, sb);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblAllOes) {
            for (OeModel oe : allOes) {
                oeModelToExcel(oe, sb);
            }
        }

        if (!StringUtils.isBlank(sb.toString())) {
            putStringToClipboard(sb.toString());

            String text = "Данные были скопированы в буфер обмена. Теперь их можно вставить в Microsoft Excel.";
            UIUtils.hint(text);
        }
    }

    private void putStringToClipboard(String s) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(s), this);
    }

    private void putObjectToMyClipboard(Collection c) {
        ru.goodfil.catalog.ui.utils.Clipboard.getInstance().put(c);
    }

    /**
     * Скопировать в системный буффер обмена (для Excel) выделенные строки выделенной таблицы.
     */
    private void miCopySelectedToExcelActionPerformed(ActionEvent e) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        final StringBuilder sb = new StringBuilder();

        if (tablesPopupMenu.getInvoker() == tblFilters) {

            for (Filter filter : getSelectedFilters()) {
                filterToExcel(filter, sb, filterTypeResolver, filterFormResolver);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblMotors) {
            for (BigMotorTableAdapter.MotorModel motor : getSelectedMotors()) {
                motorModelToExcel(motor, sb, sdf);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblAllMotors) {
            for (BigMotorTableAdapter.MotorModel motor : getSelectedAllMotors()) {
                motorModelToExcel(motor, sb, sdf);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblOes) {
            for (OeModel oe : getSelectedOes()) {
                oeModelToExcel(oe, sb);
            }
        }
        if (tablesPopupMenu.getInvoker() == tblAllOes) {
            for (OeModel oe : getSelectedAllOes()) {
                oeModelToExcel(oe, sb);
            }
        }

        if (!StringUtils.isBlank(sb.toString())) {
            putStringToClipboard(sb.toString());

            String text = "Данные были скопированы в буфер обмена. Теперь их можно вставить в Microsoft Excel.";
            UIUtils.info(text);
        }
    }

    private void miPasteFromBufferActionPerformed(ActionEvent e) {
        if (tablesPopupMenu.getInvoker() == tblMotors) {
            List<BigMotorTableAdapter.MotorModel> motors = getFromClipboardByType(BigMotorTableAdapter.MotorModel.class);

            if (motors.size() > 0) {
                Long selectedFilterId = getSelectedFilterId();
                Assert.notNull(selectedFilterId);

                pasteMotorsToFilter(motors, selectedFilterId);
            }
        }

        if (tablesPopupMenu.getInvoker() == tblOes) {
            List<OeModel> oes = getFromClipboardByType(OeModel.class);

            if (oes.size() > 0) {
                Long selectedFilterId = getSelectedFilterId();
                Assert.notNull(selectedFilterId);

                pasteOesToFilter(oes, selectedFilterId);
            }
        }
    }


    private <T> List<T> getFromClipboardByType(Class<T> klass) {
        List result = new ArrayList();
        Iterator i = ru.goodfil.catalog.ui.utils.Clipboard.getInstance().get().iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o.getClass().equals(klass)) {
                result.add(o);
            }
        }

        return result;
    }

    private void pasteMotorsToFilter(Collection<BigMotorTableAdapter.MotorModel> motors, Long filterId) {
        Set<Long> motorsIds = new HashSet<Long>();
        for (BigMotorTableAdapter.MotorModel motor : motors) {
            motorsIds.add(motor.getMotorId());
        }

        //  Прикрепляем моторы к данному фильтру
        carsService.doAttachMotorsToFilter(filterId, motorsIds);

        reReadMotors(getSelectedFilterId());
    }

    private void pasteOesToFilter(Collection<OeModel> oes, Long filterId) {
        Set<Long> oesIds = new HashSet<Long>();
        for (OeModel oe : oes) {
            oesIds.add(oe.getOeId());
        }

        //  Прикрепляем ое к выбранному фильтру
        carsService.doAttachOesToFilter(filterId, oesIds);

        reReadMotors(getSelectedFilterId());
        reReadOes(getSelectedFilterId());
    }

    /**
     * Скопировать выделенные объекты в буффер.
     */
    private void miCopySelectedToBufferActionPerformed(ActionEvent e) {
        if (tablesPopupMenu.getInvoker() == tblFilters) {
            putObjectToMyClipboard(getSelectedFilters());
        }
        if (tablesPopupMenu.getInvoker() == tblMotors) {
            putObjectToMyClipboard(getSelectedMotors());
        }
        if (tablesPopupMenu.getInvoker() == tblAllMotors) {
            putObjectToMyClipboard(getSelectedAllMotors());
        }
        if (tablesPopupMenu.getInvoker() == tblOes) {
            putObjectToMyClipboard(getSelectedOes());
        }
        if (tablesPopupMenu.getInvoker() == tblAllOes) {
            putObjectToMyClipboard(getSelectedAllOes());
        }
    }

    /**
     * Скопировать все объекты из выделенной таблицы в буффер.
     */
    private void miCopyToBufferActionPerformed(ActionEvent e) {
        if (tablesPopupMenu.getInvoker() == tblFilters) {
            putObjectToMyClipboard(filters);
        }
        if (tablesPopupMenu.getInvoker() == tblMotors) {
            putObjectToMyClipboard(motors);
        }
        if (tablesPopupMenu.getInvoker() == tblAllMotors) {
            putObjectToMyClipboard(allMotors);
        }
        if (tablesPopupMenu.getInvoker() == tblOes) {
            putObjectToMyClipboard(oes);
        }
        if (tablesPopupMenu.getInvoker() == tblAllOes) {
            putObjectToMyClipboard(allOes);
        }
    }

    private void btnClearSearchModelActionPerformed() {
        cbSeria.setSelectedIndex(-1);
        tbSeria.setText("");
        tbModel.setText("");
        tbEngine.setText("");
        tbDateF.setText("");
        tbDateT.setText("");
    }

    private void tbSeriaKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbSeria.setText("");
        }
    }

    private void tbModelKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbModel.setText("");
        }
    }

    private void tbEngineKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbEngine.setText("");
        }
    }

    private void tbDateFKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbDateF.setText("");
        }
    }

    private void tbDateTKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSearchMotorsActionPerformed(null);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            tbDateT.setText("");
        }
    }

    private void tablesPopupMenuPopupMenuWillBecomeVisible(PopupMenuEvent e) {
        miPasteFromBuffer.setEnabled(ru.goodfil.catalog.ui.utils.Clipboard.getInstance().hasSomething());
        miFullCopyToExcel.setVisible(tablesPopupMenu.getInvoker() == tblFilters);
        miShowOnSiteMenu.setVisible(tablesPopupMenu.getInvoker() == tblFilters);
    }

    private void miFullCopyToExcelActionPerformed(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        for (Filter filter : getSelectedFilters()) {
            filterToExcel(filter, sb, filterTypeResolver, filterFormResolver);

            List<Motor> theMotors = filtersService.getMotorsByFilterId(filter.getId());

            Set<Long> seriesIds = new HashSet<Long>();
            for (Motor theMotor : theMotors) {
                seriesIds.add(theMotor.getSeriaId());
            }

            List<Seria> series = carsService.getSeriesByIds(seriesIds);
            Map<Long, Seria> seriesMap = ListAsMap.get(series);

            Set<Long> manufactorsIds = new HashSet<Long>();
            for (Seria theSeria : series) {
                manufactorsIds.add(theSeria.getManufactorId());
            }

            List<Manufactor> manufactors = carsService.getManufactorsByIds(manufactorsIds);
            Map<Long, Manufactor> manufactorsMap = ListAsMap.get(manufactors);

            List<BigMotorTableAdapter.MotorModel> motorModels = new ArrayList<BigMotorTableAdapter.MotorModel>();
            for (Motor theMotor : theMotors) {
                Seria seria = seriesMap.get(theMotor.getSeriaId());
                if(seria!=null){
                Manufactor manufactor = manufactorsMap.get(seria.getManufactorId());
                BigMotorTableAdapter.MotorModel motorModel = new BigMotorTableAdapter.MotorModel();
                motorModel.setSeriaId(seria.getId());
                motorModel.setManufactorId(manufactor.getId());
                motorModel.setMotorId(theMotor.getId());
                motorModel.setSeria(seria.getName());
                motorModel.setManufactor(manufactor.getName());
                motorModel.setName(theMotor.getName());
                motorModel.setEngine(theMotor.getEngine());
                motorModel.setKw(theMotor.getKw());
                motorModel.setHp(theMotor.getHp());
                motorModel.setDateF(date(theMotor.getDateF()));
                motorModel.setDateT(date(theMotor.getDateT()));
                motorModels.add(motorModel);
                }
            }

            sb.append("\n\nПРИМЕНИМОСТЬ\n");
            for (BigMotorTableAdapter.MotorModel motorModel : motorModels) {
                motorModelToExcel(motorModel, sb, sdf);
            }

            List<Oe> oes = filtersService.getOesByFilter(filter.getId());

            //
            // EXPERIMENTAL
            //
            //  Сделаем здесь следующее: будем удалять из базы дублирющиеся ОЕ
            //
            {
                Set<Long> oesToDelete = new HashSet<Long>();
                Set<String> oesHash = new HashSet<String>();
                for (Oe oe : oes) {
                    String oeHash = oe.getName().trim() + String.valueOf(oe.getBrandId());
                    if (oesHash.contains(oeHash)) {
                        oesToDelete.add(oe.getId());
                    } else {
                        oesHash.add(oeHash);
                    }
                }

                if (oesToDelete.size() > 0) {
                    analogsService.removeOes(oesToDelete);
                    oes = filtersService.getOesByFilter(filter.getId());

                    System.out.println("Removed oes: " + oesToDelete.size());
                }
            }


            Set<Long> brandsIds = new HashSet<Long>();
            for (Oe oe : oes) {
                brandsIds.add(oe.getBrandId());
            }
            List<Brand> brands = filtersService.getBrandsByIds(brandsIds);
            Map<Long, Brand> brandMap = new HashMap<Long, Brand>();
            for (Brand brand : brands) {
                brandMap.put(brand.getId(), brand);
            }

            List<OeModel> oeModels = new ArrayList<OeModel>();
            for (Oe oe : oes) {
                OeModel oeModel = new OeModel();
                oeModel.setOeId(oe.getId());
                oeModel.setOe(oe.getName());

                Brand brand = brandMap.get(oe.getBrandId());
                oeModel.setBrand(brand.getName());
                oeModel.setBrandId(brand.getId());

                oeModels.add(oeModel);
            }

            sb.append("\n\nОЕ\n");
            for (OeModel oeModel : oeModels) {
                oeModelToExcel(oeModel, sb);
            }
        }

        if (!StringUtils.isBlank(sb.toString())) {
            putStringToClipboard(sb.toString());

            String text = "Данные были скопированы в буфер обмена. Теперь их можно вставить в Microsoft Excel.";
            UIUtils.info(text);
        }
    }

    private void miShowOnSiteActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (!filter.getOnSite()) {
                filter.setOnSite(true);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miDontShowOnSiteActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (filter.getOnSite()) {
                filter.setOnSite(false);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miApplyToVT1ActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (!filter.getApplyToAll_VT1()) {
                filter.setApplyToAll_VT1(true);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miApplyToVT2ActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (!filter.getApplyToAll_VT2()) {
                filter.setApplyToAll_VT2(true);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miApplyToVT3ActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (!filter.getApplyToAll_VT3()) {
                filter.setApplyToAll_VT3(true);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miApplyToVT4ActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (!filter.getApplyToAll_VT4()) {
                filter.setApplyToAll_VT4(true);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void miDontApplyToAllActionPerformed(ActionEvent e) {
        final List<Filter> filters = getSelectedFilters();
        final List<Filter> filtersToUpdate = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (filter.getApplyToAll_VT1() || filter.getApplyToAll_VT2() || filter.getApplyToAll_VT3() || filter.getApplyToAll_VT4()) {
                filter.setApplyToAll_VT1(false);
                filter.setApplyToAll_VT2(false);
                filter.setApplyToAll_VT3(false);
                filter.setApplyToAll_VT4(false);
                filtersToUpdate.add(filter);
            }
        }

        if (filtersToUpdate.size() > 0) {
            filtersService.updateFilters(filtersToUpdate);
        }

        doSearchFilters();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - ÐÐ²Ð°Ð½ ÐÐ²Ð°Ð½Ð¾Ð²
        panel2 = new JPanel();
        panel12 = new JPanel();
        label5 = new JLabel();
        tbFilterCode = new JTextField();
        btnSearchFilter = new JButton();
        panel13 = new JPanel();
        hSpacer1 = new JPanel(null);
        btnJoinFilters = new JButton();
        btnCreateFilter = new JButton();
        btnEditFilter = new JButton();
        btnRemoveFilter = new JButton();
        srcTblFilters = new JScrollPane();
        tblFilters = new JTable();
        panel15 = new JPanel();
        label6 = new JLabel();
        cbManufactors = new JComboBox();
        label7 = new JLabel();
        cbSeria = new JComboBox();
        btnClearSearchModel = new JButton();
        btnSearchMotors = new JButton();
        panel1 = new JPanel();
        label1 = new JLabel();
        tbSeria = new JTextField();
        label2 = new JLabel();
        tbModel = new JTextField();
        label3 = new JLabel();
        tbEngine = new JTextField();
        label4 = new JLabel();
        tbDateF = new JTextField();
        label9 = new JLabel();
        tbDateT = new JTextField();
        scrollPane11 = new JScrollPane();
        tblMotors = new JTable();
        panel18 = new JPanel();
        btnAttachMotor = new JButton();
        btnDetachMotor = new JButton();
        vSpacer2 = new JPanel(null);
        scrollPane12 = new JScrollPane();
        tblAllMotors = new JTable();
        panel16 = new JPanel();
        hSpacer3 = new JPanel(null);
        panel17 = new JPanel();
        label8 = new JLabel();
        tbOe = new JTextField();
        btnOeSearch = new JButton();
        scrollPane7 = new JScrollPane();
        tblOes = new JTable();
        panel19 = new JPanel();
        btnAttachOe = new JButton();
        btnDetachOe = new JButton();
        vSpacer3 = new JPanel(null);
        scrollPane8 = new JScrollPane();
        tblAllOes = new JTable();
        tablesPopupMenu = new JPopupMenu();
        miFullCopyToExcel = new JMenuItem();
        menu1 = new JMenu();
        miCopySelectedToExcel = new JMenuItem();
        miCopyToExcel = new JMenuItem();
        menu2 = new JMenu();
        miCopySelectedToBuffer = new JMenuItem();
        miCopyToBuffer = new JMenuItem();
        miPasteFromBuffer = new JMenuItem();
        miShowOnSiteMenu = new JMenu();
        miShowOnSite = new JMenuItem();
        miDontShowOnSite = new JMenuItem();
        miApplyToAllMenu = new JMenu();
        miApplyToVT1 = new JMenuItem();
        miApplyToVT2 = new JMenuItem();
        miApplyToVT3 = new JMenuItem();
        miApplyToVT4 = new JMenuItem();
        miDontApplyToAll = new JMenuItem();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setMinimumSize(new Dimension(300, 207));

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

        setLayout(new FormLayout(
            "default:grow",
            "fill:default:grow"));

        //======== panel2 ========
        {
            panel2.setLayout(new FormLayout(
                "default:grow, $lcgap, 21dlu, $lcgap, default:grow",
                "default, default:grow, $lgap, default, fill:default:grow, $lgap, default, fill:default:grow"));

            //======== panel12 ========
            {
                panel12.setLayout(new FormLayout(
                    "default, default:grow, 21dlu",
                    "default"));

                //---- label5 ----
                label5.setText("\u0418\u0437\u0434\u0435\u043b\u0438\u0435");
                panel12.add(label5, cc.xy(1, 1));

                //---- tbFilterCode ----
                tbFilterCode.setToolTipText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0442\u0435\u043a\u0441\u0442 \u0434\u043b\u044f \u043f\u043e\u0438\u0441\u043a\u0430 \u0444\u0438\u043b\u044c\u0442\u0440\u0430");
                tbFilterCode.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tbFilterCodeKeyPressed(e);
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {
                        tbFilterCodeKeyPressed(e);
                    }
                });
                tbFilterCode.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbFilterCodeFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbFilterCodeFocusGained(e);
                    }
                });
                panel12.add(tbFilterCode, cc.xy(2, 1));

                //---- btnSearchFilter ----
                btnSearchFilter.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                btnSearchFilter.setToolTipText("\u041f\u043e\u0438\u0441\u043a");
                btnSearchFilter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnSearchFilterActionPerformed(e);
                    }
                });
                panel12.add(btnSearchFilter, cc.xy(3, 1));
            }
            panel2.add(panel12, cc.xy(1, 1));

            //======== panel13 ========
            {
                panel13.setLayout(new FormLayout(
                    "default:grow, 2*($lcgap, 21dlu), 2*(21dlu)",
                    "default"));
                panel13.add(hSpacer1, cc.xy(1, 1));

                //---- btnJoinFilters ----
                btnJoinFilters.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
                btnJoinFilters.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnJoinFiltersActionPerformed(e);
                    }
                });
                panel13.add(btnJoinFilters, cc.xy(3, 1));

                //---- btnCreateFilter ----
                btnCreateFilter.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
                btnCreateFilter.setToolTipText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnCreateFilter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCreateFilterActionPerformed(e);
                    }
                });
                panel13.add(btnCreateFilter, cc.xy(5, 1));

                //---- btnEditFilter ----
                btnEditFilter.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
                btnEditFilter.setToolTipText("\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnEditFilter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnEditFilterActionPerformed(e);
                    }
                });
                panel13.add(btnEditFilter, cc.xy(6, 1));

                //---- btnRemoveFilter ----
                btnRemoveFilter.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
                btnRemoveFilter.setToolTipText("\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnRemoveFilter.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveFilterActionPerformed(e);
                    }
                });
                panel13.add(btnRemoveFilter, cc.xy(7, 1));
            }
            panel2.add(panel13, cc.xy(5, 1));

            //======== srcTblFilters ========
            {

                //---- tblFilters ----
                tblFilters.setDoubleBuffered(true);
                tblFilters.setAutoCreateRowSorter(true);
                tblFilters.setToolTipText("\u0424\u0438\u043b\u044c\u0442\u0440\u044b, \u043f\u043e\u0434\u043f\u0430\u0434\u0430\u044e\u0449\u0438\u0435 \u043f\u043e\u0434 \u0443\u0441\u043b\u043e\u0432\u0438\u044f \u043f\u043e\u0438\u0441\u043a\u0430");
                tblFilters.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tblFiltersKeyPressed(e);
                    }
                });
                tblFilters.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tblFiltersMouseClicked(e);
                    }
                });
                srcTblFilters.setViewportView(tblFilters);
            }
            panel2.add(srcTblFilters, cc.xywh(1, 2, 5, 1));

            //======== panel15 ========
            {
                panel15.setLayout(new FormLayout(
                    "default, default:grow, $lcgap, default, default:grow, $rgap, $lcgap, default, $rgap, default, $lcgap",
                    "2*(default)"));

                //---- label6 ----
                label6.setText("\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044c");
                panel15.add(label6, cc.xy(1, 1));

                //---- cbManufactors ----
                cbManufactors.setToolTipText("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044f");
                cbManufactors.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cbManufactorsActionPerformed(e);
                    }
                });
                cbManufactors.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        cbManufactorsKeyPressed(e);
                    }
                });
                panel15.add(cbManufactors, cc.xy(2, 1));

                //---- label7 ----
                label7.setText("\u0421\u0435\u0440\u0438\u044f");
                panel15.add(label7, cc.xy(4, 1));

                //---- cbSeria ----
                cbSeria.setToolTipText("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0441\u0435\u0440\u0438\u044e");
                cbSeria.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cbSeriaActionPerformed(e);
                    }
                });
                cbSeria.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        cbSeriaKeyPressed(e);
                    }
                });
                panel15.add(cbSeria, cc.xywh(5, 1, 2, 1));

                //---- btnClearSearchModel ----
                btnClearSearchModel.setText("\u041e\u0447\u0438\u0441\u0442\u0438\u0442\u044c");
                btnClearSearchModel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnClearSearchModelActionPerformed();
                    }
                });
                panel15.add(btnClearSearchModel, cc.xy(8, 1));

                //---- btnSearchMotors ----
                btnSearchMotors.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                btnSearchMotors.setToolTipText("\u041f\u043e\u0438\u0441\u043a \u043c\u043e\u0442\u043e\u0440\u043e\u0432 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044f \u0438\u0437 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u0441\u0435\u0440\u0438\u0438");
                btnSearchMotors.setText("\u041f\u043e\u0438\u0441\u043a");
                btnSearchMotors.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnSearchMotorsActionPerformed(e);
                    }
                });
                panel15.add(btnSearchMotors, cc.xy(10, 1));

                //======== panel1 ========
                {
                    panel1.setLayout(new FormLayout(
                        "default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow, $lcgap, default, $lcgap, default:grow",
                        "default"));

                    //---- label1 ----
                    label1.setText("\u0421\u0435\u0440\u0438\u044f");
                    panel1.add(label1, cc.xy(1, 1));

                    //---- tbSeria ----
                    tbSeria.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tbSeriaKeyPressed(e);
                        }
                        @Override
                        public void keyTyped(KeyEvent e) {
                            tbSeriaKeyPressed(e);
                        }
                    });
                    panel1.add(tbSeria, cc.xy(3, 1));

                    //---- label2 ----
                    label2.setText("\u041c\u043e\u0434\u0435\u043b\u044c");
                    panel1.add(label2, cc.xy(5, 1));

                    //---- tbModel ----
                    tbModel.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tbModelKeyPressed(e);
                        }
                        @Override
                        public void keyTyped(KeyEvent e) {
                            tbModelKeyPressed(e);
                        }
                    });
                    panel1.add(tbModel, cc.xy(7, 1));

                    //---- label3 ----
                    label3.setText("\u0414\u0432\u0438\u0433\u0430\u0442\u0435\u043b\u044c");
                    panel1.add(label3, cc.xy(9, 1));

                    //---- tbEngine ----
                    tbEngine.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tbEngineKeyPressed(e);
                        }
                        @Override
                        public void keyTyped(KeyEvent e) {
                            tbEngineKeyPressed(e);
                        }
                    });
                    panel1.add(tbEngine, cc.xy(11, 1));

                    //---- label4 ----
                    label4.setText("\u0414\u0430\u0442\u0430 \"\u0421\"");
                    panel1.add(label4, cc.xy(13, 1));

                    //---- tbDateF ----
                    tbDateF.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tbDateFKeyPressed(e);
                        }
                        @Override
                        public void keyTyped(KeyEvent e) {
                            tbDateFKeyPressed(e);
                        }
                    });
                    panel1.add(tbDateF, cc.xy(15, 1));

                    //---- label9 ----
                    label9.setText("\u0414\u0430\u0442\u0430 \"\u041f\u041e\"");
                    panel1.add(label9, cc.xy(17, 1));

                    //---- tbDateT ----
                    tbDateT.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            tbDateTKeyPressed(e);
                        }
                        @Override
                        public void keyTyped(KeyEvent e) {
                            tbDateTKeyPressed(e);
                        }
                    });
                    panel1.add(tbDateT, cc.xy(19, 1));
                }
                panel15.add(panel1, cc.xywh(1, 2, 10, 1));
            }
            panel2.add(panel15, cc.xywh(1, 4, 5, 1));

            //======== scrollPane11 ========
            {

                //---- tblMotors ----
                tblMotors.setToolTipText("\u041c\u043e\u0442\u043e\u0440\u044b, \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d\u043d\u044b\u0435 \u043a \u0434\u0430\u043d\u043d\u043e\u043c\u0443 \u0444\u0438\u043b\u044c\u0442\u0440\u0443");
                tblMotors.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tblMotorsKeyPressed(e);
                    }
                });
                tblMotors.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tblMotorsMouseClicked(e);
                    }
                });
                scrollPane11.setViewportView(tblMotors);
            }
            panel2.add(scrollPane11, cc.xy(1, 5));

            //======== panel18 ========
            {
                panel18.setLayout(new FormLayout(
                    "21dlu",
                    "2*(default, $lgap), fill:default:grow"));

                //---- btnAttachMotor ----
                btnAttachMotor.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_left_green_24.png")));
                btnAttachMotor.setToolTipText("\u041f\u0440\u0438\u0432\u044f\u0437\u0430\u0442\u044c \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043c\u043e\u0442\u043e\u0440 \u043a \u0444\u0438\u043b\u044c\u0442\u0440\u0443");
                btnAttachMotor.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAttachMotorActionPerformed(e);
                    }
                });
                panel18.add(btnAttachMotor, cc.xy(1, 1));

                //---- btnDetachMotor ----
                btnDetachMotor.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_right_green_24.png")));
                btnDetachMotor.setToolTipText("\u041e\u0442\u0432\u044f\u0437\u0430\u0442\u044c \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0439 \u043c\u043e\u0442\u043e\u0440 \u043e\u0442 \u0444\u0438\u043b\u044c\u0442\u0440\u0430");
                btnDetachMotor.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnDetachMotorActionPerformed(e);
                    }
                });
                panel18.add(btnDetachMotor, cc.xy(1, 3));
                panel18.add(vSpacer2, cc.xy(1, 5));
            }
            panel2.add(panel18, cc.xy(3, 5));

            //======== scrollPane12 ========
            {

                //---- tblAllMotors ----
                tblAllMotors.setToolTipText("\u041c\u043e\u0442\u043e\u0440\u044b \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044f \u0438 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u0441\u0435\u0440\u0438\u0438");
                tblAllMotors.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tblAllMotorsKeyPressed(e);
                    }
                });
                tblAllMotors.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tblAllMotorsMouseClicked(e);
                    }
                });
                scrollPane12.setViewportView(tblAllMotors);
            }
            panel2.add(scrollPane12, cc.xy(5, 5));

            //======== panel16 ========
            {
                panel16.setLayout(new FormLayout(
                    "default:grow",
                    "default"));
                panel16.add(hSpacer3, cc.xy(1, 1));
            }
            panel2.add(panel16, cc.xy(1, 7));

            //======== panel17 ========
            {
                panel17.setLayout(new FormLayout(
                    "default, default:grow, 21dlu",
                    "default"));

                //---- label8 ----
                label8.setText("\u041d\u043e\u043c\u0435\u0440 \u041e\u0415");
                panel17.add(label8, cc.xy(1, 1));

                //---- tbOe ----
                tbOe.setToolTipText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u041e\u0415 \u0434\u043b\u044f \u043f\u043e\u0438\u0441\u043a\u0430");
                tbOe.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbOeKeyTyped(e);
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {
                        tbOeKeyTyped(e);
                    }
                });
                tbOe.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbOeFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbOeFocusGained(e);
                    }
                });
                panel17.add(tbOe, cc.xy(2, 1));

                //---- btnOeSearch ----
                btnOeSearch.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                btnOeSearch.setToolTipText("\u041f\u043e\u0438\u0441\u043a \u043f\u043e \u041e\u0415");
                btnOeSearch.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnOeSearchActionPerformed(e);
                    }
                });
                panel17.add(btnOeSearch, cc.xy(3, 1));
            }
            panel2.add(panel17, cc.xy(5, 7));

            //======== scrollPane7 ========
            {

                //---- tblOes ----
                tblOes.setSurrendersFocusOnKeystroke(true);
                tblOes.setToolTipText("\u041e\u0415, \u043f\u0440\u0438\u0432\u044f\u0437\u0430\u043d\u043d\u044b\u0435 \u043a \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u043c\u0443 \u0444\u0438\u043b\u044c\u0442\u0440\u0443");
                tblOes.setAutoCreateRowSorter(true);
                tblOes.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tblOesKeyPressed(e);
                    }
                });
                tblOes.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tblOesMouseClicked(e);
                    }
                });
                scrollPane7.setViewportView(tblOes);
            }
            panel2.add(scrollPane7, cc.xy(1, 8));

            //======== panel19 ========
            {
                panel19.setLayout(new FormLayout(
                    "21dlu",
                    "fill:default, $lgap, default, $lgap, fill:default:grow"));

                //---- btnAttachOe ----
                btnAttachOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_left_green_24.png")));
                btnAttachOe.setToolTipText("\u041f\u0440\u0438\u0432\u044f\u0437\u0430\u0442\u044c \u041e\u0415 \u043a \u0444\u0438\u043b\u044c\u0442\u0440\u0443");
                btnAttachOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAttachOeActionPerformed(e);
                    }
                });
                panel19.add(btnAttachOe, cc.xy(1, 1));

                //---- btnDetachOe ----
                btnDetachOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_right_green_24.png")));
                btnDetachOe.setToolTipText("\u041e\u0442\u0432\u044f\u0437\u0430\u0442\u044c \u041e\u0415 \u043e\u0442 \u0444\u0438\u043b\u044c\u0442\u0440\u0430");
                btnDetachOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnDetachOeActionPerformed(e);
                    }
                });
                panel19.add(btnDetachOe, cc.xy(1, 3));
                panel19.add(vSpacer3, cc.xy(1, 5));
            }
            panel2.add(panel19, cc.xy(3, 8));

            //======== scrollPane8 ========
            {

                //---- tblAllOes ----
                tblAllOes.setToolTipText("\u041f\u0435\u0440\u0435\u0447\u0435\u043d\u044c \u041e\u0415, \u043f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0438\u0445 \u043f\u043e\u0434 \u0443\u0441\u043b\u043e\u0432\u0438\u0435 \u043f\u043e\u0438\u0441\u043a\u0430");
                tblAllOes.setAutoCreateRowSorter(true);
                tblAllOes.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tblAllOesKeyPressed(e);
                    }
                });
                tblAllOes.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        tblAllOesMouseClicked(e);
                    }
                });
                scrollPane8.setViewportView(tblAllOes);
            }
            panel2.add(scrollPane8, cc.xy(5, 8));
        }
        add(panel2, cc.xy(1, 1));

        //======== tablesPopupMenu ========
        {
            tablesPopupMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    tablesPopupMenuPopupMenuWillBecomeVisible(e);
                }
            });

            //---- miFullCopyToExcel ----
            miFullCopyToExcel.setText("\u041f\u043e\u043b\u043d\u0430\u044f \u0432\u044b\u0433\u0440\u0443\u0437\u043a\u0430 \u0438\u0437\u0434\u0435\u043b\u0438\u044f \u0432 Excel");
            miFullCopyToExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    miFullCopyToExcelActionPerformed(e);
                }
            });
            tablesPopupMenu.add(miFullCopyToExcel);

            //======== menu1 ========
            {
                menu1.setText("\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 Excel");

                //---- miCopySelectedToExcel ----
                miCopySelectedToExcel.setText("\u0422\u043e\u043b\u044c\u043a\u043e \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u043d\u044b\u0435 \u0441\u0442\u0440\u043e\u043a\u0438");
                miCopySelectedToExcel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miCopySelectedToExcelActionPerformed(e);
                    }
                });
                menu1.add(miCopySelectedToExcel);

                //---- miCopyToExcel ----
                miCopyToExcel.setText("\u0412\u0441\u0435 \u0441\u0442\u0440\u043e\u043a\u0438");
                miCopyToExcel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miCopyToExcelActionPerformed(e);
                    }
                });
                menu1.add(miCopyToExcel);
            }
            tablesPopupMenu.add(menu1);
            tablesPopupMenu.addSeparator();

            //======== menu2 ========
            {
                menu2.setText("\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440");

                //---- miCopySelectedToBuffer ----
                miCopySelectedToBuffer.setText("\u0422\u043e\u043b\u044c\u043a\u043e \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u043d\u044b\u0435 \u0441\u0442\u0440\u043e\u043a\u0438");
                miCopySelectedToBuffer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miCopySelectedToBufferActionPerformed(e);
                    }
                });
                menu2.add(miCopySelectedToBuffer);

                //---- miCopyToBuffer ----
                miCopyToBuffer.setText("\u0412\u0441\u0435 \u0441\u0442\u0440\u043e\u043a\u0438");
                miCopyToBuffer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miCopyToBufferActionPerformed(e);
                    }
                });
                menu2.add(miCopyToBuffer);
            }
            tablesPopupMenu.add(menu2);

            //---- miPasteFromBuffer ----
            miPasteFromBuffer.setText("\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430");
            miPasteFromBuffer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    miPasteFromBufferActionPerformed(e);
                }
            });
            tablesPopupMenu.add(miPasteFromBuffer);
            tablesPopupMenu.addSeparator();

            //======== miShowOnSiteMenu ========
            {
                miShowOnSiteMenu.setText("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c \u043d\u0430 \u0441\u0430\u0439\u0442\u0435?");

                //---- miShowOnSite ----
                miShowOnSite.setText("\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c");
                miShowOnSite.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miShowOnSiteActionPerformed(e);
                    }
                });
                miShowOnSiteMenu.add(miShowOnSite);

                //---- miDontShowOnSite ----
                miDontShowOnSite.setText("\u041d\u0435 \u043f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0442\u044c");
                miDontShowOnSite.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miDontShowOnSiteActionPerformed(e);
                    }
                });
                miShowOnSiteMenu.add(miDontShowOnSite);
            }
            tablesPopupMenu.add(miShowOnSiteMenu);

            //======== miApplyToAllMenu ========
            {
                miApplyToAllMenu.setText("\u0413\u043b\u043e\u0431\u043b\u044c\u043d\u0430\u044f \u043f\u0440\u0438\u0432\u044f\u0437\u043a\u0430?");

                //---- miApplyToVT1 ----
                miApplyToVT1.setText("\u041a \u043b\u0435\u0433\u043a\u043e\u0432\u044b\u043c \u0430\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u044f\u043c");
                miApplyToVT1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miApplyToVT1ActionPerformed(e);
                    }
                });
                miApplyToAllMenu.add(miApplyToVT1);

                //---- miApplyToVT2 ----
                miApplyToVT2.setText("\u041a \u0433\u0440\u0443\u0437\u043e\u0432\u044b\u043c \u0430\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u044f\u043c");
                miApplyToVT2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miApplyToVT2ActionPerformed(e);
                    }
                });
                miApplyToAllMenu.add(miApplyToVT2);

                //---- miApplyToVT3 ----
                miApplyToVT3.setText("\u041a \u0441\u043f\u0435\u0446\u0438\u0430\u043b\u044c\u043d\u043e\u0439 \u0442\u0435\u0445\u043d\u0438\u043a\u0435");
                miApplyToVT3.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miApplyToVT3ActionPerformed(e);
                    }
                });
                miApplyToAllMenu.add(miApplyToVT3);

                //---- miApplyToVT4 ----
                miApplyToVT4.setText("\u041a \u043a\u0430\u0442\u0435\u0440\u0430\u043c \u0438 \u043c\u043e\u0442\u043e\u0446\u0438\u043a\u043b\u0430\u043c");
                miApplyToVT4.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miApplyToVT4ActionPerformed(e);
                    }
                });
                miApplyToAllMenu.add(miApplyToVT4);

                //---- miDontApplyToAll ----
                miDontApplyToAll.setText("\u0423\u0431\u0440\u0430\u0442\u044c \u0433\u043b\u043e\u0431\u0430\u043b\u044c\u043d\u0443\u044e \u043f\u0440\u0438\u0432\u044f\u0437\u043a\u0443");
                miDontApplyToAll.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        miDontApplyToAllActionPerformed(e);
                    }
                });
                miApplyToAllMenu.add(miDontApplyToAll);
            }
            tablesPopupMenu.add(miApplyToAllMenu);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - ÐÐ²Ð°Ð½ ÐÐ²Ð°Ð½Ð¾Ð²
    private JPanel panel2;
    private JPanel panel12;
    private JLabel label5;
    private JTextField tbFilterCode;
    private JButton btnSearchFilter;
    private JPanel panel13;
    private JPanel hSpacer1;
    private JButton btnJoinFilters;
    private JButton btnCreateFilter;
    private JButton btnEditFilter;
    private JButton btnRemoveFilter;
    private JScrollPane srcTblFilters;
    private JTable tblFilters;
    private JPanel panel15;
    private JLabel label6;
    private JComboBox cbManufactors;
    private JLabel label7;
    private JComboBox cbSeria;
    private JButton btnClearSearchModel;
    private JButton btnSearchMotors;
    private JPanel panel1;
    private JLabel label1;
    private JTextField tbSeria;
    private JLabel label2;
    private JTextField tbModel;
    private JLabel label3;
    private JTextField tbEngine;
    private JLabel label4;
    private JTextField tbDateF;
    private JLabel label9;
    private JTextField tbDateT;
    private JScrollPane scrollPane11;
    private JTable tblMotors;
    private JPanel panel18;
    private JButton btnAttachMotor;
    private JButton btnDetachMotor;
    private JPanel vSpacer2;
    private JScrollPane scrollPane12;
    private JTable tblAllMotors;
    private JPanel panel16;
    private JPanel hSpacer3;
    private JPanel panel17;
    private JLabel label8;
    private JTextField tbOe;
    private JButton btnOeSearch;
    private JScrollPane scrollPane7;
    private JTable tblOes;
    private JPanel panel19;
    private JButton btnAttachOe;
    private JButton btnDetachOe;
    private JPanel vSpacer3;
    private JScrollPane scrollPane8;
    private JTable tblAllOes;
    private JPopupMenu tablesPopupMenu;
    private JMenuItem miFullCopyToExcel;
    private JMenu menu1;
    private JMenuItem miCopySelectedToExcel;
    private JMenuItem miCopyToExcel;
    private JMenu menu2;
    private JMenuItem miCopySelectedToBuffer;
    private JMenuItem miCopyToBuffer;
    private JMenuItem miPasteFromBuffer;
    private JMenu miShowOnSiteMenu;
    private JMenuItem miShowOnSite;
    private JMenuItem miDontShowOnSite;
    private JMenu miApplyToAllMenu;
    private JMenuItem miApplyToVT1;
    private JMenuItem miApplyToVT2;
    private JMenuItem miApplyToVT3;
    private JMenuItem miApplyToVT4;
    private JMenuItem miDontApplyToAll;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    public void updateFilterTypeResolver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "");
        map.put("null", "");
        List<FilterType> filterTypes = carsService.getFilterTypes();
        for (FilterType filterType : filterTypes) {
            map.put(filterType.getCode(), filterType.getName());
        }
        filterTypeResolver.update(map);
    }

    public void updateFilterFormResolver() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "");
        map.put("null", "");
        List<FilterForm> filterForms = carsService.getFilterForms();
        for (FilterForm filterForm : filterForms) {
            map.put(filterForm.getId().toString(), filterForm.getName());
        }
        filterFormResolver.update(map);
    }

    public static class OeModel {
        private Long oeId;
        private Long brandId;
        private String oe;
        private String brand;
        private int mannStatus;

        public int getMannStatus() {
            return mannStatus;
        }

        public void setMannStatus(int mannStatus) {
            this.mannStatus = mannStatus;
        }

        public Long getOeId() {
            return oeId;
        }

        public void setOeId(Long oeId) {
            this.oeId = oeId;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getOe() {
            return oe;
        }

        public void setOe(String oe) {
            this.oe = oe;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

    public static class OeModelTableModel extends AbstractTableAdapter<OeModel> {
        public OeModelTableModel(ListModel listModel) {
            super(listModel, new String[]{
                    "ОЕ",
                    "Производитель"
            });
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            OeModel oe = getRow(rowIndex);
            if (columnIndex == 0) return oe.getOe();
            if (columnIndex == 1) return oe.getBrand();
            return null;
        }
    }

    private void adjustButtonsEnabled() {
        btnSearchFilter.setEnabled(!StringUtils.isBlank(tbFilterCode.getText()));

        btnCreateFilter.setEnabled(true);
        btnEditFilter.setEnabled(getSelectedFilter() != null);
        btnRemoveFilter.setEnabled(getSelectedFilter() != null);

        btnSearchMotors.setEnabled(getSelectedManufactor() != null);

        btnOeSearch.setEnabled(!StringUtils.isBlank(tbOe.getText()));

        btnAttachMotor.setEnabled(getSelectedAllMotors().size() > 0);
        btnDetachMotor.setEnabled(getSelectedMotors().size() > 0);

        btnAttachOe.setEnabled(getSelectedAllOes().size() > 0);
        btnDetachOe.setEnabled(getSelectedOes().size() > 0);

    }

    @Managed
    public static class SeriaModelAdapter {
        private final Map<String, List<Seria>> series = new HashMap<String, List<Seria>>();

        public void addSeria(@NotNull final Seria seria) {
            String name = seria.getName();
            if (!series.containsKey(name)) {
                series.put(name, new ArrayList<Seria>());
            }
            series.get(name).add(seria);
        }

        public List<SeriaModel> getList() {
            List<SeriaModel> result = new ArrayList<SeriaModel>();
            for (String name : series.keySet()) {
                SeriaModel seriaModel = new SeriaModel(name);

                List<Seria> subitems = series.get(name);
                for (Seria subitem : subitems) {
                    seriaModel.addSubitem(subitem);
                }
                result.add(seriaModel);
            }

            Collections.sort(result, new Comparator<SeriaModel>() {
                @Override
                public int compare(SeriaModel o1, SeriaModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            return Collections.unmodifiableList(result);
        }
    }

    @Managed
    public static class ManufactorModelAdapter {
        private final Map<String, List<Manufactor>> manufactors = new HashMap<String, List<Manufactor>>();

        public void addManufactor(@NotNull final Manufactor manufactor) {
            String name = manufactor.getName();
            if (!manufactors.containsKey(name)) {
                manufactors.put(name, new ArrayList<Manufactor>());
            }
            manufactors.get(name).add(manufactor);
        }

        public List<ManufactorModel> getList() {
            List<ManufactorModel> result = new ArrayList<ManufactorModel>();
            for (String name : manufactors.keySet()) {
                ManufactorModel manufactorModel = new ManufactorModel(name);

                List<Manufactor> subitems = manufactors.get(name);
                for (Manufactor subitem : subitems) {
                    manufactorModel.addSubitem(subitem);
                }
                result.add(manufactorModel);
            }

            Collections.sort(result, new Comparator<ManufactorModel>() {
                @Override
                public int compare(ManufactorModel o1, ManufactorModel o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });

            return Collections.unmodifiableList(result);
        }
    }

    @Managed
    public static class ManufactorModel {
        private final String name;
        private final Set<VechicleTypeAssigment> subitems = new HashSet<VechicleTypeAssigment>();

        public ManufactorModel(@NotNull @NotBlank final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSubitem(@NotNull final Manufactor manufactor) {
            subitems.add(new VechicleTypeAssigment(manufactor.getVechicleTypeId(), manufactor.getId()));
        }

        public Set<VechicleTypeAssigment> getSubitems() {
            return Collections.unmodifiableSet(subitems);
        }

        public Set<Long> getManufactorIds() {
            Set<Long> result = new HashSet<Long>();
            for (VechicleTypeAssigment subitem : subitems) {
                result.add(subitem.getManufactorId());
            }
            return result;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Managed
    public static class SeriaModel {
        private final String name;
        private final Set<ManufactorAssigment> subitems = new HashSet<ManufactorAssigment>();

        public SeriaModel(@NotNull @NotBlank final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSubitem(@NotNull final Seria seria) {
            subitems.add(new ManufactorAssigment(seria.getId(), seria.getManufactorId()));
        }

        public Set<ManufactorAssigment> getSubitems() {
            return Collections.unmodifiableSet(subitems);
        }

        public Set<Long> getSeriaIds() {
            Set<Long> result = new HashSet<Long>();
            for (ManufactorAssigment subitem : subitems) {
                result.add(subitem.getSeriaId());
            }
            return result;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Managed
    public static class VechicleTypeAssigment {
        private final Long vechicleTypeId;
        private final Long manufactorId;

        public VechicleTypeAssigment(@NotNull final Long vechicleTypeId, @NotNull final Long manufactorId) {
            this.vechicleTypeId = vechicleTypeId;
            this.manufactorId = manufactorId;
        }

        public Long getVechicleTypeId() {
            return vechicleTypeId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }
    }

    @Managed
    public static class ManufactorAssigment {
        private final Long manufactorId;
        private final Long seriaId;

        public ManufactorAssigment(@NotNull final Long seriaId, @NotNull final Long manufactorId) {
            this.seriaId = seriaId;
            this.manufactorId = manufactorId;
        }

        public Long getSeriaId() {
            return seriaId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }
    }

    public static class MotorSearchModel {
        private Set<Long> manufactorsIds;
        private Set<Long> seriesIds;
        private String seriaName;
        private String modelName;
        private String engineName;
        private String dateF;
        private String dateT;
        private boolean empty;

        public Set<Long> getManufactorsIds() {
            return manufactorsIds;
        }

        public void setManufactorsIds(Set<Long> manufactorsIds) {
            this.manufactorsIds = manufactorsIds;
        }

        public Set<Long> getSeriesIds() {
            return seriesIds;
        }

        public void setSeriesIds(Set<Long> seriesIds) {
            this.seriesIds = seriesIds;
        }

        public String getSeriaName() {
            return seriaName;
        }

        public void setSeriaName(String seriaName) {
            this.seriaName = seriaName;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getEngineName() {
            return engineName;
        }

        public void setEngineName(String engineName) {
            this.engineName = engineName;
        }

        public String getDateF() {
            return dateF;
        }

        public void setDateF(String dateF) {
            this.dateF = dateF;
        }

        public String getDateT() {
            return dateT;
        }

        public void setDateT(String dateT) {
            this.dateT = dateT;
        }

        public boolean isEmpty() {
            return seriesIds == null && manufactorsIds == null;
        }
    }
}
