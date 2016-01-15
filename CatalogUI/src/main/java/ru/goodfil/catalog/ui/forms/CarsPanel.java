package ru.goodfil.catalog.ui.forms;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.domain.dict.VechicleType;
import ru.goodfil.catalog.export.domain.fullexport.FullCatalogExportParams;
import ru.goodfil.catalog.export.domain.fullexport.FullExportDocumentModel;
import ru.goodfil.catalog.export.service.ExportService;
import ru.goodfil.catalog.reporting.FullCatalogExporter;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.adapters.MotorTableAdapter;
import ru.goodfil.catalog.ui.adapters.SmallFilterTableAdapter;
import ru.goodfil.catalog.ui.adapters.sorters.MotorTableSorter;
import ru.goodfil.catalog.ui.cellrenderer.FilterTableCellRenderer;
import ru.goodfil.catalog.ui.cellrenderer.MannListsCellRenderer;
import ru.goodfil.catalog.ui.cellrenderer.MannTablesCellRenderer;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.ui.swing.resolver.DefaultResolver;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.JoinOptions;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CarsPanel.java 177 2014-07-08 11:56:58Z chezxxx@gmail.com $
 */
public class CarsPanel extends JPanel implements ClipboardOwner {
    private final CarsService carsService = Services.getCarsService();
    private final FiltersService filtersService = Services.getFiltersService();
    private final ExportService exportService = Services.getExportService();

    private final int OPERATION_COPY=1;
    private final int OPERATION_CUT=2;
    private final int OPERATION_CLEAR=0;
    private int operation=0;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public CarsPanel() {
        initComponents();

        motorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) return;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        onMotorSelected();
                        adjustButtonsEnabledProperty();
                    }
                });
            }
        });
        motorsTable.setRowHeight(20);

        filtersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) return;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        adjustButtonsEnabledProperty();
                    }
                });
            }
        });
        filtersTable.setRowHeight(20);

        allFiltersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) return;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        adjustButtonsEnabledProperty();
                    }
                });
            }
        });
        allFiltersTable.setRowHeight(20);


        reReadVechicleTypes();
        reReadFilterTypes();

        filterTypeResolver = createFilterTypeResolver();

        vechicleTypesList.setModel(vechicleTypes);
        manufactorsList.setModel(manufactors);
        seriesList.setModel(series);
        motorsTable.setModel(new MotorTableAdapter(motors));
        motorsTable.setRowSorter(new MotorTableSorter((MotorTableAdapter) motorsTable.getModel()));

        filtersTable.setModel(new SmallFilterTableAdapter(filters, filterTypeResolver,filtersAndMotorsForComments));
        allFiltersTable.setModel(new SmallFilterTableAdapter(allFilters, filterTypeResolver));

        //В зависимости от параметра подсвечивать или нет строки из манна
        if (System.getProperty("catalog.mode.fromMann") != null && System.getProperty("catalog.mode.fromMann").equals("1")) {
            manufactorsList.setCellRenderer(new MannListsCellRenderer(MannListsCellRenderer.MANUFACTOR));
            seriesList.setCellRenderer(new MannListsCellRenderer(MannListsCellRenderer.SERIA));
            filtersTable.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.FILTER));
            allFiltersTable.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.FILTER));
            motorsTable.setDefaultRenderer(Object.class, new MannTablesCellRenderer(MannTablesCellRenderer.MOTOR));
        } else {
            filtersTable.setDefaultRenderer(Object.class, new FilterTableCellRenderer());
            allFiltersTable.setDefaultRenderer(Object.class, new FilterTableCellRenderer());
        }
        adjustButtonsEnabledProperty();
    }

    private Resolver filterTypeResolver;

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

    private final ArrayListModel<VechicleType> vechicleTypes = new ArrayListModel<VechicleType>();
    private final ArrayListModel<Manufactor> manufactors = new ArrayListModel<Manufactor>();
    private final ArrayListModel<Seria> series = new ArrayListModel<Seria>();
    private final ArrayListModel<Motor> motors = new ArrayListModel<Motor>();
    private final ArrayListModel<Filter> filters = new ArrayListModel<Filter>();
    private final ArrayListModel<FiltersAndMotors> filtersAndMotorsForComments = new ArrayListModel<FiltersAndMotors>();
    private final ArrayListModel<Filter> allFilters = new ArrayListModel<Filter>();
    private final ArrayListModel<FilterType> filterTypes = new ArrayListModel<FilterType>();

//    private Long selectedVechicleTypeId;
//    private Long selectedManufactorId;
//    private Long selectedSeriaId;
//    private Long selectedMotorId;
//    private final Set<Long> selectedFilterIds = new HashSet<Long>();

    private void reReadVechicleTypes() {
        vechicleTypes.clear();
        vechicleTypes.addAll(carsService.getVechicleTypes());
    }

    private void reReadFilterTypes() {
        filterTypes.clear();
        filterTypes.addAll(carsService.getFilterTypes());
    }

    private void reReadManufactors(Long vechicleTypeId) {
        manufactors.clear();
        manufactors.addAll(carsService.getManufactorsByVechicleTypeId(vechicleTypeId));
    }

    private void reReadSeries(Long manufactorId) {
        series.clear();
        series.addAll(carsService.getSeriesByManufactorId(manufactorId));
    }

    private void reReadMotors(Long seriaId) {
        motors.clear();
        motors.addAll(carsService.getMotorsBySeriaId(seriaId));
    }

    private void reReadFilters(Long motorId) {
        onMotorSelected();
    }

    //
    //  Получение выбранных строк
    //

    private Long getSelectedVechicleTypeId() {
        int index = vechicleTypesList.getSelectedIndex();
        if (index == -1) return null;
        return vechicleTypes.get(index).getId();
    }

    private VechicleType getSelectedVechicleType() {
        int index = vechicleTypesList.getSelectedIndex();
        if (index == -1) return null;
        return vechicleTypes.get(index);
    }

    private List<VechicleType> getSelectedVechicleTypes() {
        List<VechicleType> result=new ArrayList<VechicleType>();
        int[] indices = vechicleTypesList.getSelectedIndices();
        for (int index : indices) {
            result.add(vechicleTypes.get(index));
        }
        return result;
    }

    private Long getSelectedManufatorId() {
        int index = manufactorsList.getSelectedIndex();
        if (index == -1) return null;
        return manufactors.get(index).getId();
    }

    private List<Manufactor> getSelectedManufactors() {
        List<Manufactor> result = new ArrayList<Manufactor>();
        int[] indices = manufactorsList.getSelectedIndices();
        for (int index : indices) {
            result.add(manufactors.get(index));
        }
        return result;
    }

    private Manufactor getSelectedManufator() {
        int index = manufactorsList.getSelectedIndex();
        if (index == -1) return null;
        return manufactors.get(index);
    }

    private Long getSelectedSeriaId() {
        int index = seriesList.getSelectedIndex();
        if (index == -1) return null;
        return series.get(index).getId();
    }

    private List<Seria> getSelectedSeries() {
        List<Seria> result = new ArrayList<Seria>();
        for (int index : seriesList.getSelectedIndices()) {
            result.add(series.get(index));
        }
        return result;
    }

    private Seria getSelectedSeria() {
        int index = seriesList.getSelectedIndex();
        if (index == -1) return null;
        return series.get(index);
    }

    private Long getSelectedMotorId() {
        int index = motorsTable.getSelectedRow();
        if (index == -1) {
            return null;
        } else {
            index = motorsTable.convertRowIndexToModel(index);
            return motors.get(index).getId();
        }
    }

    private List<Motor> getSelectedMotors() {
        List<Motor> result = new ArrayList<Motor>();
        for (int index : motorsTable.getSelectedRows()) {
            index = motorsTable.convertRowIndexToModel(index);
            result.add(motors.get(index));
        }
        return result;
    }

    private Motor getSelectedMotor() {
        int index = motorsTable.getSelectedRow();
        if (index == -1) {
            return null;
        } else {
            index = motorsTable.convertRowIndexToModel(index);
            return motors.get(index);
        }
    }

    private Set<Long> getSelectedFilterIds() {
        Set<Long> result = new HashSet<Long>();
        int[] indices = filtersTable.getSelectedRows();
        for (int index : indices) {
            index = filtersTable.convertRowIndexToModel(index);
            Long filterId = filters.get(index).getId();
            result.add(filterId);
        }
        return result;
    }

    private Long getSelectedFilterId() {
        int index = filtersTable.getSelectedRow();
        if (index == -1) {
            return null;
        } else {
            index = filtersTable.convertRowIndexToModel(index);
            return filters.get(index).getId();
        }
    }

    private List<Filter> getSelectedFilters() {
        List<Filter> result = new ArrayList<Filter>();
        int[] indices = filtersTable.getSelectedRows();
        for (int index : indices) {
            index = filtersTable.convertRowIndexToModel(index);
            Filter filter = filters.get(index);
            result.add(filter);
        }
        return result;
    }

    private Set<Long> getSelectedAllFilterIds() {
        Set<Long> result = new HashSet<Long>();
        int[] indices = allFiltersTable.getSelectedRows();
        for (int index : indices) {
            index = allFiltersTable.convertRowIndexToModel(index);
            Long filterId = allFilters.get(index).getId();
            result.add(filterId);
        }
        return result;
    }

    private List<Filter> getSelectedAllFilters() {
        List<Filter> result = new ArrayList<Filter>();
        int[] indices = allFiltersTable.getSelectedRows();
        for (int index : indices) {
            index = allFiltersTable.convertRowIndexToModel(index);
            Filter filter = allFilters.get(index);
            result.add(filter);
        }
        return result;
    }

    //
    //  -----------------------------------------------------------------------
    //


    //
    //  Бизнес-операции
    //

    /**
     * Добавить тип транспортного средства.
     */
    public void doAddVechicleType(@NotNull @NotBlank final String vechicleTypeName) {
        VechicleType vechicleType = new VechicleType();
        vechicleType.setName(vechicleTypeName);

        carsService.addVechicleType(vechicleType);

        reReadVechicleTypes();
    }

    /**
     * Обновить тип транспортного средства.
     */
    public void doUpdateVechicleType(@NotNull @Valid VechicleType vechicleType) {
        carsService.updateVechicleType(vechicleType);

        reReadVechicleTypes();
    }

    /**
     * Удалить тип транспортного средства.
     */
    public void doRemoveVechicleType(@NotNull Long selectedVechicleTypeId) {
        carsService.removeVechicleType(selectedVechicleTypeId);

        reReadVechicleTypes();
    }

    /**
     * Добавить производителя.
     */
    public void doAddManufactor(@NotNull Long vechicleTypeId, @NotNull @NotBlank String name) {
        Manufactor manufactor = Manufactor.create(vechicleTypeId, name);

        carsService.addManufactor(manufactor);

        reReadManufactors(getSelectedVechicleTypeId());
    }

    /**
     * Обновить производителя.
     */
    public void doUpdateManufactor(@NotNull @Valid Manufactor manufactor) {
        carsService.updateManufactor(manufactor);

        reReadManufactors(getSelectedVechicleTypeId());
    }

    /**
     * Удалить производителя.
     */
    public void doRemoveManufactor(@NotNull Long selectedManufatorId) {
        carsService.removeManufactor(selectedManufatorId);

        reReadManufactors(getSelectedVechicleTypeId());
    }

    /**
     * Добавить серию.
     */
    public void doAddSeria(@NotNull Long manufactorId, @NotNull @NotBlank String name) {
        Seria seria = Seria.createSeria(manufactorId, name);

        carsService.addSeria(seria);

        reReadSeries(getSelectedManufatorId());
    }

    /**
     * Обновить серию.
     */
    public void doUpdateSeria(@NotNull @Valid Seria seria) {
        carsService.updateSeria(seria);

        reReadSeries(getSelectedManufatorId());
    }

    /**
     * Удалить серию.
     */
    public void doRemoveSeria(@NotNull Long selectedSeriaId) {
        carsService.removeSeria(selectedSeriaId);

        reReadSeries(getSelectedManufatorId());
    }

    /**
     * Добавление мотора.
     */
    public void doAddMotor(@NotNull @Valid Motor motor) {
        carsService.addMotor(motor);
    }

    /**
     * Обновление мотора.
     */
    public void doUpdateMotor(@NotNull @Valid Motor motor) {
        carsService.updateMotor(motor);
    }

    /**
     * Удаление мотора.
     */
    public void doRemoveMotor(@NotNull Long selectedMotorId) {
        carsService.removeMotor(selectedMotorId);
    }

    /**
     * Прикрепить фильтры.
     */
    public void doAttachFiltersToMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds) {
        carsService.doAttachFiltersToMotor(motorId, filterIds);
    }

    /**
     * Открепить фильтры.
     */
    public void doDetachFiltersFromMotor(@NotNull Long motorId, @NotNull @NotEmpty Set<Long> filterIds) {
        carsService.doDetachFiltersFromMotor(motorId, filterIds);
    }

    //
    //  -----------------------------------------------------------------------------
    //

    /**
     * Добавить тип транспортного средства.
     */
    private void createTypeBtnActionPerformed(ActionEvent e) {
        String name = UIUtils.askName();

        if (name != null) {
            doAddVechicleType(name);

            manufactorsList.clearSelection();
            manufactors.clear();

            seriesList.clearSelection();
            series.clear();

            motorsTable.clearSelection();
            motors.clear();

            filtersTable.clearSelection();
            filters.clear();
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Переименовать тип транспортного средства.
     */
    private void editTypeBtnActionPerformed(ActionEvent e) {
        if (getSelectedVechicleTypeId() != null) {
            VechicleType vechicleType = getSelectedVechicleType();
            String name = UIUtils.askName(vechicleType.getName());
            if (name != null) {
                vechicleType.setName(name);
                doUpdateVechicleType(vechicleType);
            }
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Удалить тип транспортного средства.
     */
    private void removeTypeBtnActionPerformed(ActionEvent e) {
        if (getSelectedVechicleTypeId() != null && UIUtils.askDelete()) {
            doRemoveVechicleType(getSelectedVechicleTypeId());
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Добавить производителя.
     */
    private void createManufactorBtnActionPerformed(ActionEvent e) {
        Long vechicleTypeId = getSelectedVechicleTypeId();
        if (vechicleTypeId == null) {
            UIUtils.warning("Для создания производителя необходимо выбрать тип транспортного средства.");
            return;
        }

        String name = UIUtils.askName();
        if (name != null) {
            doAddManufactor(vechicleTypeId, name);
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Редактировать производителя.
     */
    private void editManufactorBtnActionPerformed(ActionEvent e) {
        if (getSelectedManufatorId() != null) {
            Manufactor manufactor = getSelectedManufator();
            String name = UIUtils.askName(manufactor.getName());
            if (name != null) {
                manufactor.setName(name);
                doUpdateManufactor(manufactor);
            }
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Удалить производителя.
     */
    private void removeManufactorBtnActionPerformed(ActionEvent e) {
        if (getSelectedManufatorId() != null && UIUtils.askDelete()) {
            doRemoveManufactor(getSelectedManufatorId());
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Добавить серию.
     */
    private void createSeriaBtnActionPerformed(ActionEvent e) {
        Long manufactorId = getSelectedManufatorId();
        if (manufactorId == null) {
            UIUtils.warning("Для создания серии необходимо выбрать производителя.");
            return;
        }

        String name = UIUtils.askName();
        if (name != null) {
            doAddSeria(manufactorId, name);
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Редактировать серию.
     */
    private void editSeriaBtnActionPerformed(ActionEvent e) {
        if (getSelectedSeriaId() != null) {
            Seria seria = getSelectedSeria();
            String name = UIUtils.askName(seria.getName());
            if (name != null) {
                seria.setName(name);
                doUpdateSeria(seria);
            }
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Удалить серию.
     */
    private void removeSeriaBtnActionPerformed(ActionEvent e) {
        if (getSelectedSeriaId() != null && UIUtils.askDelete()) {
            doRemoveSeria(getSelectedSeriaId());
        }

        adjustButtonsEnabledProperty();
    }

    private void vechicleTypesListValueChanged(ListSelectionEvent e) {
        if (e != null && e.getValueIsAdjusting()) return;

        manufactorsList.clearSelection();
        manufactors.clear();

        seriesList.clearSelection();
        series.clear();

        motorsTable.clearSelection();
        motors.clear();

        filtersTable.clearSelection();
        filters.clear();

        Long selectedVechicleTypeId = getSelectedVechicleTypeId();
        if (selectedVechicleTypeId != null) reReadManufactors(selectedVechicleTypeId);

        adjustButtonsEnabledProperty();
    }

    private void manufactorsListValueChanged(ListSelectionEvent e) {
        if (e != null && e.getValueIsAdjusting()) return;

        seriesList.clearSelection();
        series.clear();

        motorsTable.clearSelection();
        motors.clear();

        filtersTable.clearSelection();
        filters.clear();
        Long selectedManufactorId = getSelectedManufatorId();
        if (selectedManufactorId != null) reReadSeries(selectedManufactorId);

        adjustButtonsEnabledProperty();
    }

    private void seriesListValueChanged(ListSelectionEvent e) {
        if (e != null && e.getValueIsAdjusting()) return;


        motorsTable.clearSelection();
        motors.clear();

        filtersTable.clearSelection();
        filters.clear();
        Long selectedSeriaId = getSelectedSeriaId();
        if (selectedSeriaId != null) reReadMotors(selectedSeriaId);

        adjustButtonsEnabledProperty();
    }

    /**
     * Добавить двигатель.
     */
    private void createMotorBtnActionPerformed(ActionEvent e) {
        if (getSelectedSeriaId() == null) {
            UIUtils.warning("Для создания мотора необходимо выбрать серию.");
            return;
        }

        MotorWindow motorWindow = new MotorWindow();
        motorWindow.setVisible(true);
        if (motorWindow.getDialogResult() == DialogResult.OK) {
            Motor motor = motorWindow.getMotor();
            motor.setSeriaId(getSelectedSeriaId());
            doAddMotor(motor);
            reReadMotors(getSelectedSeriaId());
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Редактировать двигатель.
     */
    private void editMotorBtnActionPerformed(ActionEvent e) {
        if (getSelectedMotorId() != null) {
            Motor motor = getSelectedMotor();

            MotorWindow motorWindow = new MotorWindow();
            motorWindow.setMotor(motor);
            motorWindow.setVisible(true);
            motor = motorWindow.getMotor();
            if (motor != null) {
                doUpdateMotor(motor);
                reReadMotors(getSelectedSeriaId());
            }
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Удалить двигатель.
     */
    private void removeMotorBtnActionPerformed(ActionEvent e) {
        if (getSelectedMotorId() != null && UIUtils.askDelete()) {
            doRemoveMotor(getSelectedMotorId());
            reReadMotors(getSelectedSeriaId());
        }

        adjustButtonsEnabledProperty();
    }

    private void filterSearchBtnActionPerformed(ActionEvent e) {
        final String filterCode = filterSearchField.getText();
        if (StringUtils.isBlank(filterCode)) {
            String msg = "Необходимо ввести значение в поле \"Фильтр\"";
            JOptionPane.showMessageDialog(null, msg, "Внимание!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker worker = new SwingWorker<Object, Void>() {
            @Override
            protected Object doInBackground() throws Exception {
                allFilters.clear();
                allFilters.addAll(filtersService.getFiltersByName(filterCode));
                return null;
            }
        };
        worker.execute();

        adjustButtonsEnabledProperty();
    }

    /**
     * Прикрепить фильтр к двигателю.
     */
    private void attachFilterBtnActionPerformed(ActionEvent e) {
        if (getSelectedMotorId() == null) {
            UIUtils.warning("Не выбран мотор.");
            return;
        }
        if (allFiltersTable.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать фильтры для прикрепления.");
            return;
        }

        Set<Long> filterIds = getSelectedAllFilterIds();
        Assert.notEmpty(filterIds);

        Long motorId = getSelectedMotorId();
        Assert.notNull(motorId);


        //  Доработка:
        //  При прикреплении фильтра к мотору вывести предупреждающее сообщение,
        //  если фильтр данного типа уже прикреплен к мотору
        {
            List<String> msgs = new ArrayList<String>();
            List<Filter> tmpFilters = filtersService.getFiltersByMotorId(motorId);
            for (Filter filter : getSelectedAllFilters()) {
                for (Filter tmpFilter : tmpFilters) {
                    if (filter.getFilterTypeCode().equals(tmpFilter.getFilterTypeCode())) {
                        String msg = filter.getName() + ": к выбранному мотору уже прикреплен один " + filterTypeResolver.resolve(filter.getFilterTypeCode()) + " фильтр (" + tmpFilter.getName() + ")";
                        msgs.add(msg);
                        break;
                    }
                }
            }

            if (msgs.size() > 0) {
                if (!UIUtils.askContinue(StringUtils.join(msgs, "\n") + "\nПродолжить?")) {
                    return;
                }
            }
        }

        doAttachFiltersToMotor(motorId, filterIds);
        onMotorSelected();

        adjustButtonsEnabledProperty();
    }

    public void onMotorSelected() {
        filters.clear();
        filtersAndMotorsForComments.clear();
        if (getSelectedMotorId() != null) {
            final Set<Long> filterIds = new HashSet<Long>();
            List<Filter> list = filtersService.getFiltersByGlobalVechicleTypeId(getSelectedVechicleTypeId());
            for (Filter filter : list) {
                if (!filterIds.contains(filter.getId())) {
                    filterIds.add(filter.getId());
                    filters.add(filter);
                }
            }

            list = filtersService.getFiltersByMotorId(getSelectedMotorId());
            for (Filter filter : list) {
                if (!filterIds.contains(filter.getId())) {
                    filterIds.add(filter.getId());
                    filters.add(filter);
                }
            }
            filtersAndMotorsForComments.addAll(carsService.getFiltersAndMotorsByMotorId(getSelectedMotorId()));
        }

        adjustButtonsEnabledProperty();
    }

    /**
     * Открепить фильтр от двигателя.
     */
    private void detachFilterBtnActionPerformed(ActionEvent e) {
        if (getSelectedMotorId() == null) {
            UIUtils.warning("Не выбран мотор.");
            return;
        }
        if (filtersTable.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать фильтры для открепления.");
            return;
        }

        Set<Long> filterIds = getSelectedFilterIds();
        Assert.notEmpty(filterIds);

        Long motorId = getSelectedMotorId();
        Assert.notNull(motorId);

        doDetachFiltersFromMotor(motorId, filterIds);
        onMotorSelected();

        adjustButtonsEnabledProperty();
    }

    private void vechicleTypesListKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            createTypeBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            removeTypeBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            editTypeBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            vechicleTypesListValueChanged(null);
        }
    }

    private void manufactorsListKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            createManufactorBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            removeManufactorBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            editManufactorBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            manufactorsListValueChanged(null);
        }
        if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedManufactors());
            this.setOperation(OPERATION_CUT);
        }
        if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedManufactors());
            this.setOperation(OPERATION_COPY);
        }
        if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            List<Manufactor> manufactors = new ArrayList<Manufactor>();
            manufactors.addAll(getFromClipboardByType(Manufactor.class));
            if (manufactors.size() > 0) {
                Long vechicleTypeIdNew = getSelectedVechicleTypeId();
                Assert.notNull(vechicleTypeIdNew);
                if(getOperation()==OPERATION_COPY){
                carsService.doCopyManufactors(manufactors, vechicleTypeIdNew);
                }
                if(getOperation()==OPERATION_CUT){
                carsService.doCutManufactors(manufactors, vechicleTypeIdNew);
                }
                reReadManufactors(vechicleTypeIdNew);
            }
        }
    }

    private void seriesListKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            createSeriaBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            removeSeriaBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            editSeriaBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            seriesListValueChanged(null);
        }
        if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedSeries());
            this.setOperation(OPERATION_CUT);
        }
        if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedSeries());
            this.setOperation(OPERATION_COPY);
        }
        if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            List<Seria> series=new ArrayList<Seria>();
            series.addAll(getFromClipboardByType(Seria.class));
            if (series.size()>0){
                Long manufactorIdNew = getSelectedManufatorId();
                Assert.notNull(manufactorIdNew);
                if(getOperation()==OPERATION_COPY){
                carsService.doCopySeries(series, manufactorIdNew);
                }
                if(getOperation()==OPERATION_CUT){
                carsService.doCutSeries(series, manufactorIdNew);
                }
                reReadSeries(manufactorIdNew);
            }
        }
    }

    private void filterSearchFieldKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            filterSearchBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            filterSearchField.setText("");
        }
    }

    private void motorsTableKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT) {
            createMotorBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            removeMotorBtnActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            editMotorBtnActionPerformed(null);
        }
        if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedMotors());
            this.setOperation(OPERATION_CUT);
        }
        if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            putObjectToMyClipboard(getSelectedMotors());
            this.setOperation(OPERATION_COPY);
        }
        if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
            List<Motor> motors =  new ArrayList<Motor>();
            motors.addAll(getFromClipboardByType(Motor.class));
            if (motors.size() > 0) {
                Long seriaIdNew = getSelectedSeriaId();
                Assert.notNull(seriaIdNew);
                if(getOperation()==OPERATION_COPY){
                    carsService.doCopyMotors(motors, seriaIdNew);
                }
                if(getOperation()==OPERATION_CUT){
                    carsService.doCutMotors(motors, seriaIdNew);
                }
                reReadMotors(seriaIdNew);
            }
        }
    }

    private void adjustButtonsEnabledProperty() {
        createTypeBtn.setEnabled(true);
        editTypeBtn.setEnabled(getSelectedVechicleType() != null);
        removeTypeBtn.setEnabled(getSelectedVechicleType() != null);

        joinManufactorsBtn.setEnabled(getSelectedManufactors().size() > 1);
        createManufactorBtn.setEnabled(getSelectedVechicleType() != null);
        editManufactorBtn.setEnabled(getSelectedVechicleType() != null && getSelectedManufator() != null);
        removeManufactorBtn.setEnabled(getSelectedVechicleType() != null && getSelectedManufator() != null);

        joinSeriesBtn.setEnabled(getSelectedSeries().size() > 1);
        createSeriaBtn.setEnabled(getSelectedManufator() != null);
        editSeriaBtn.setEnabled(getSelectedManufator() != null && getSelectedSeria() != null);
        removeSeriaBtn.setEnabled(getSelectedManufator() != null && getSelectedSeria() != null);

        joinMotorsBtn.setEnabled(getSelectedMotors().size() > 1);
        createMotorBtn.setEnabled(getSelectedSeria() != null);
        editMotorBtn.setEnabled(getSelectedSeria() != null && getSelectedMotor() != null);
        removeMotorBtn.setEnabled(getSelectedSeria() != null && getSelectedMotor() != null);

        attachFilterBtn.setEnabled(!getSelectedAllFilterIds().isEmpty());
        detachFilterBtn.setEnabled(!getSelectedFilterIds().isEmpty());
        filterRelationComment.setEnabled(!getSelectedFilterIds().isEmpty()||getSelectedFilterIds().size()==1);

        filterSearchBtn.setEnabled(!StringUtils.isBlank(filterSearchField.getText()));
    }

    private void vechicleTypesListMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (vechicleTypes.size() > 0) {
                listsPopupMenu.show(vechicleTypesList, e.getX(), e.getY());
            }
        }
    }

    private void manufactorsListMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (manufactors.size() > 0) {
                listsPopupMenu.show(manufactorsList, e.getX(), e.getY());
            }
        }
    }

    private void seriesListMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (series.size() > 0) {
                listsPopupMenu.show(seriesList, e.getX(), e.getY());
            }
        }
    }

    private void motorsTableMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (motors.size() > 0) {
                listsPopupMenu.show(motorsTable, e.getX(), e.getY());
            }
        }
    }

    private void filtersTableMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (filters.size() > 0) {
                listsPopupMenu.show(filtersTable, e.getX(), e.getY());
            }
        }
    }

    private void allFiltersTableMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (allFilters.size() > 0) {
                listsPopupMenu.show(allFiltersTable, e.getX(), e.getY());
            }
        }
    }

    private void listsPopupMenuPopupMenuWillBecomeVisible(PopupMenuEvent e) {
        miExportToExcel.setEnabled((listsPopupMenu.getInvoker() == vechicleTypesList && getSelectedVechicleType() != null) ||
                (listsPopupMenu.getInvoker() == manufactorsList && getSelectedManufator() != null) ||
                (listsPopupMenu.getInvoker() == seriesList && getSelectedSeria() != null));
        menuItem3.setEnabled(getOperation()==OPERATION_COPY);
        menuItem4.setEnabled(getOperation()==OPERATION_CUT);
    }

    class FullCatalogExportTask extends SwingWorker<File, Void> {
        private final FullCatalogExportParams fullCatalogExportParams;
        private final Component invoker;

        FullCatalogExportTask(FullCatalogExportParams fullCatalogExportParams, Component invoker) {
            this.fullCatalogExportParams = fullCatalogExportParams;
            this.invoker = invoker;
        }

        @Override
        protected File doInBackground() throws Exception {
            setProgress(10);
            FullExportDocumentModel fullExportDocument = null;

            if (listsPopupMenu.getInvoker() == vechicleTypesList) {
                Long vechicleTypeId = getSelectedVechicleTypeId();
                Assert.notNull(vechicleTypeId);
                fullExportDocument = exportService.getByVechicleTypeId(vechicleTypeId, fullCatalogExportParams);
            }
            if (listsPopupMenu.getInvoker() == manufactorsList) {
                Long manufactorId = getSelectedManufatorId();
                Assert.notNull(manufactorId);
                fullExportDocument = exportService.getByManufactorId(manufactorId, fullCatalogExportParams);
            }
            if (listsPopupMenu.getInvoker() == seriesList) {
                Long seriaId = getSelectedSeriaId();
                Assert.notNull(seriaId);
                fullExportDocument = exportService.getBySeriaId(seriaId, fullCatalogExportParams);
            }
            Assert.notNull(fullExportDocument);
            setProgress(50);

            File f = null;
            try {
                Map<String, String> columns = new LinkedHashMap<String, String>();
                for (FilterType filterType : carsService.getFilterTypes()) {
                    if (fullCatalogExportParams.getFilterTypes().contains(filterType.getId())) {
                        columns.put(filterType.getCode(), filterType.getName());
                    }
                }
                if (columns.size() == 0) {
                    for (FilterType filterType : carsService.getFilterTypes()) {
                        columns.put(filterType.getCode(), filterType.getName());
                    }
                }

                f = File.createTempFile("goodwill", "catalog");
                FullCatalogExporter.export(fullExportDocument, columns, f);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            setProgress(70);
            return f;
        }

        @Override
        protected void done() {
            setProgress(100);

            File f = null;
            try {
                f = get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

            JFileChooser saveFile = new JFileChooser();
            saveFile.setDialogTitle("Выберите файл для сохранения выгрузки по автомобилям");
            saveFile.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f == null) return false;
                    if (f.isDirectory()) return true;
                    return f.getName().toLowerCase().endsWith("xls");
                }

                @Override
                public String getDescription() {
                    return "Microsoft Excel (*.xls)";
                }
            });
            int result = saveFile.showSaveDialog(CarsPanel.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File dest = saveFile.getSelectedFile();
                if (!dest.getName().endsWith(".xls")) {
                    dest = new File(dest.getAbsolutePath() + ".xls");
                }
                try {
                    FileUtils.copyFile(f, dest);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    private void miExportToExcelActionPerformed(ActionEvent e) {
        if (listsPopupMenu.getInvoker() == vechicleTypesList ||
                listsPopupMenu.getInvoker() == manufactorsList ||
                listsPopupMenu.getInvoker() == seriesList) {

            ExportWindow exportWindow = new ExportWindow();
            exportWindow.setVisible(true);
            if (exportWindow.getDialogResult() == DialogResult.OK) {
                final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Выгрузка ОЕ", "Подождите, производится выгрузка ОЕ", 0, 100);
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToPopup(0);
                progressMonitor.setMillisToDecideToPopup(0);

                FullCatalogExportTask task = new FullCatalogExportTask(exportWindow.getExportParams(), listsPopupMenu.getInvoker());
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("progress")) {
                            progressMonitor.setProgress(new Integer(evt.getNewValue().toString()));
                        }
                    }
                });
                task.execute();
            }
        }
    }

    private void miCopyToExcelActionPerformed(ActionEvent e) {
        StringBuilder sb = new StringBuilder();

        if (listsPopupMenu.getInvoker() == vechicleTypesList) {
            for (VechicleType vechicleType : vechicleTypes) {
                sb.append(String.format("%s\n", vechicleType.getName()));
            }
        }

        if (listsPopupMenu.getInvoker() == manufactorsList) {
            for (Manufactor manufactor : manufactors) {
                sb.append(String.format("%s\n", manufactor.getName()));
            }
        }

        if (listsPopupMenu.getInvoker() == seriesList) {
            for (Seria seria : series) {
                sb.append(String.format("%s\n", seria.getName()));
            }
        }

        if (listsPopupMenu.getInvoker() == motorsTable) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            for (Motor motor : motors) {
                String dateF = motor.getDateF() != null ? sdf.format(motor.getDateF()) : "";
                String dateT = motor.getDateT() != null ? sdf.format(motor.getDateT()) : "";

                sb.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\n",
                        motor.getName(),
                        motor.getEngine(),
                        motor.getKw(),
                        motor.getHp(),
                        dateF,
                        dateT));
            }
        }

        if (listsPopupMenu.getInvoker() == filtersTable) {
            for (Filter filter : filters) {
                sb.append(String.format("%s\t%s\n", filter.getName(), filterTypeResolver.resolve(filter.getFilterTypeCode())));
            }
        }

        if (listsPopupMenu.getInvoker() == allFiltersTable) {
            for (Filter filter : allFilters) {
                sb.append(String.format("%s\t%s\n", filter.getName(), filterTypeResolver.resolve(filter.getFilterTypeCode())));
            }
        }

        if (!StringUtils.isBlank(sb.toString())) {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(sb.toString()), this);

            String text = "Данные были скопированы в буфер обмена. Теперь их можно вставить в Microsoft Excel.";
            UIUtils.info(text);
        }
    }

    /**
     * Операция объединения производителей.
     */
    private void joinManufactorsBtnActionPerformed(ActionEvent e) {
        List<Manufactor> manufactors = getSelectedManufactors();
        if (manufactors.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(manufactors);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            carsService.doJoinManufactors(masterItemId, slavesItemsIds, joinOptions);
            reReadManufactors(getSelectedVechicleTypeId());
        }
    }

    /**
     * Операция объединения серий.
     */
    private void joinSeriesBtnActionPerformed(ActionEvent e) {
        List<Seria> series = getSelectedSeries();
        if (series.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(series);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            carsService.doJoinSeries(masterItemId, slavesItemsIds, joinOptions);
            reReadSeries(getSelectedManufatorId());
        }
    }

    /**
     * Операция объединения моторов.
     */
    private void joinMotorsBtnActionPerformed(ActionEvent e) {
        List<Motor> motors = getSelectedMotors();
        if (motors.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(motors);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            carsService.doJoinMotors(masterItemId, slavesItemsIds, joinOptions);
            reReadMotors(getSelectedSeriaId());
        }
    }

    private void copyToClipboard(ActionEvent e) {
        if (listsPopupMenu.getInvoker() == vechicleTypesList) {
             putObjectToMyClipboard(getSelectedVechicleTypes());
            this.setOperation(OPERATION_COPY);
        }
        if (listsPopupMenu.getInvoker() == manufactorsList) {
            putObjectToMyClipboard(getSelectedManufactors());
            this.setOperation(OPERATION_COPY);
        }
        if (listsPopupMenu.getInvoker() == seriesList) {
            putObjectToMyClipboard(getSelectedSeries());
            this.setOperation(OPERATION_COPY);
        }
        if (listsPopupMenu.getInvoker() == motorsTable) {
            putObjectToMyClipboard(getSelectedMotors());
            this.setOperation(OPERATION_COPY);
        }
    }

    private void putObjectToMyClipboard(Collection c) {
        ru.goodfil.catalog.ui.utils.Clipboard.getInstance().put(c);
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

    private void pasteFromClipboard(ActionEvent e) {
            if (listsPopupMenu.getInvoker() == manufactorsList) {
                List<Manufactor> manufactors = new ArrayList<Manufactor>();
                manufactors.addAll(getFromClipboardByType(Manufactor.class));
                if (manufactors.size() > 0) {
                    Long vechicleTypeIdNew = getSelectedVechicleTypeId();
                    Assert.notNull(vechicleTypeIdNew);
                    if(getOperation()==OPERATION_COPY){
                    carsService.doCopyManufactors(manufactors, vechicleTypeIdNew);
                    }
                    if(getOperation()==OPERATION_CUT){
                    carsService.doCutManufactors(manufactors, vechicleTypeIdNew);
                    }
                    reReadManufactors(vechicleTypeIdNew);
                }
                this.setOperation(OPERATION_CLEAR);
            }
            if (listsPopupMenu.getInvoker() == seriesList) {
                List<Seria> series = new ArrayList<Seria>();
                series.addAll(getFromClipboardByType(Seria.class));
                if (series.size() > 0) {
                    Long manufactorIdNew = getSelectedManufatorId();
                    Assert.notNull(manufactorIdNew);
                    if(getOperation()==OPERATION_COPY){
                    carsService.doCopySeries(series, manufactorIdNew);
                    }
                    if(getOperation()==OPERATION_CUT){
                    carsService.doCutSeries(series, manufactorIdNew);
                    }
                    reReadSeries(manufactorIdNew);
                }
                this.setOperation(OPERATION_CLEAR);
            }
            if (listsPopupMenu.getInvoker() == motorsTable) {
                List<Motor> motors = new ArrayList<Motor>();
                motors.addAll(getFromClipboardByType(Motor.class));
                if (motors.size() > 0) {
                    Long seriaIdNew = getSelectedSeriaId();
                    Assert.notNull(seriaIdNew);
                    if(getOperation()==OPERATION_COPY){
                    carsService.doCopyMotors(motors, seriaIdNew);
                    }
                    if(getOperation()==OPERATION_CUT){
                        carsService.doCutMotors(motors, seriaIdNew);
                    }
                    reReadMotors(seriaIdNew);
                }
                this.setOperation(OPERATION_CLEAR);
            }
    }

    private void cutToClipboard(ActionEvent e) {
        if (listsPopupMenu.getInvoker() == vechicleTypesList) {
            putObjectToMyClipboard(getSelectedVechicleTypes());
            this.setOperation(OPERATION_CUT);
        }
        if (listsPopupMenu.getInvoker() == manufactorsList) {
            putObjectToMyClipboard(getSelectedManufactors());
            this.setOperation(OPERATION_CUT);
        }
        if (listsPopupMenu.getInvoker() == seriesList) {
            putObjectToMyClipboard(getSelectedSeries());
            this.setOperation(OPERATION_CUT);
        }
        if (listsPopupMenu.getInvoker() == motorsTable) {
            putObjectToMyClipboard(getSelectedMotors());
            this.setOperation(OPERATION_CUT);
        }
    }

    private void filterRelationCommentActionPerformed(ActionEvent e) {
        if (getSelectedMotorId() == null) {
            UIUtils.warning("Не выбран мотор.");
            return;
        }
        if (filtersTable.getSelectedRowCount() == 0) {
            UIUtils.warning("Необходимо выбрать фильтр, к которому будет написан комментарий");
            return;
        }
        FiltersAndMotors filtersAndMotors = carsService.getFilterAndMotor(getSelectedFilterId(), getSelectedMotorId());
        String comment = UIUtils.askName();
        if (comment != null) {
            filtersAndMotors.setFilterComment(comment);
            carsService.updateFiltersAndMotors(filtersAndMotors);
        }
        reReadFilters(getSelectedMotorId());
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Sasha Ivanov
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panel1 = new JPanel();
        label1 = new JLabel();
        createTypeBtn = new JButton();
        editTypeBtn = new JButton();
        removeTypeBtn = new JButton();
        label2 = new JLabel();
        joinManufactorsBtn = new JButton();
        createManufactorBtn = new JButton();
        editManufactorBtn = new JButton();
        removeManufactorBtn = new JButton();
        label3 = new JLabel();
        joinSeriesBtn = new JButton();
        createSeriaBtn = new JButton();
        editSeriaBtn = new JButton();
        removeSeriaBtn = new JButton();
        scrollPane1 = new JScrollPane();
        vechicleTypesList = new JList();
        scrollPane2 = new JScrollPane();
        manufactorsList = new JList();
        scrollPane3 = new JScrollPane();
        seriesList = new JList();
        panel10 = new JPanel();
        label5 = new JLabel();
        createMotorBtn = new JButton();
        editMotorBtn = new JButton();
        removeMotorBtn = new JButton();
        joinMotorsBtn = new JButton();
        scrollPane4 = new JScrollPane();
        motorsTable = new JTable();
        panel3 = new JPanel();
        label6 = new JLabel();
        label7 = new JLabel();
        panel4 = new JPanel();
        label4 = compFactory.createLabel("\u0424\u0438\u043b\u044c\u0442\u0440");
        filterSearchField = new JTextField();
        filterSearchBtn = new JButton();
        scrollPane5 = new JScrollPane();
        filtersTable = new JTable();
        filterRelationComment = new JButton();
        attachFilterBtn = new JButton();
        scrollPane6 = new JScrollPane();
        allFiltersTable = new JTable();
        detachFilterBtn = new JButton();
        listsPopupMenu = new JPopupMenu();
        miCopyToExcel = new JMenuItem();
        miExportToExcel = new JMenuItem();
        menuItem1 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem4 = new JMenuItem();
        CellConstraints cc = new CellConstraints();

        //======== this ========

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

        setLayout(new FormLayout(
            "default:grow",
            "fill:default:grow"));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                "default:grow, $lcgap, 3*(21pt), $lcgap, default:grow, $lcgap, 21dlu, $lcgap, 3*(21pt), $lcgap, default:grow, $lcgap, 21dlu, $lcgap, 3*(21pt)",
                "fill:21pt, fill:50dlu:grow, $lgap, min, fill:100dlu:grow, $lgap, fill:80dlu:grow"));

            //---- label1 ----
            label1.setText("\u0422\u0438\u043f \u0422\u0421");
            label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD));
            panel1.add(label1, cc.xy(1, 1));

            //---- createTypeBtn ----
            createTypeBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
            createTypeBtn.setAlignmentY(0.0F);
            createTypeBtn.setToolTipText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0442\u0438\u043f \u0442\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442\u043d\u043e\u0433\u043e \u0441\u0440\u0435\u0434\u0441\u0442\u0432\u0430");
            createTypeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createTypeBtnActionPerformed(e);
                }
            });
            panel1.add(createTypeBtn, cc.xy(3, 1));

            //---- editTypeBtn ----
            editTypeBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
            editTypeBtn.setToolTipText("\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0442\u0438\u043f \u0442\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442\u043d\u043e\u0433\u043e \u0441\u0440\u0435\u0434\u0441\u0442\u0432\u0430");
            editTypeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editTypeBtnActionPerformed(e);
                }
            });
            panel1.add(editTypeBtn, cc.xy(4, 1));

            //---- removeTypeBtn ----
            removeTypeBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
            removeTypeBtn.setToolTipText("\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0442\u0438\u043f \u0442\u0440\u0430\u043d\u0441\u043f\u043e\u0440\u0442\u043d\u043e\u0433\u043e \u0441\u0440\u0435\u0434\u0441\u0442\u0432\u0430");
            removeTypeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeTypeBtnActionPerformed(e);
                }
            });
            panel1.add(removeTypeBtn, cc.xy(5, 1));

            //---- label2 ----
            label2.setText("\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044c");
            label2.setFont(label2.getFont().deriveFont(label2.getFont().getStyle() | Font.BOLD));
            panel1.add(label2, cc.xy(7, 1));

            //---- joinManufactorsBtn ----
            joinManufactorsBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
            joinManufactorsBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinManufactorsBtnActionPerformed(e);
                }
            });
            panel1.add(joinManufactorsBtn, cc.xy(9, 1));

            //---- createManufactorBtn ----
            createManufactorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
            createManufactorBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createManufactorBtnActionPerformed(e);
                }
            });
            panel1.add(createManufactorBtn, cc.xy(11, 1));

            //---- editManufactorBtn ----
            editManufactorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
            editManufactorBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editManufactorBtnActionPerformed(e);
                }
            });
            panel1.add(editManufactorBtn, cc.xy(12, 1));

            //---- removeManufactorBtn ----
            removeManufactorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
            removeManufactorBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeManufactorBtnActionPerformed(e);
                }
            });
            panel1.add(removeManufactorBtn, cc.xy(13, 1));

            //---- label3 ----
            label3.setText("\u0421\u0435\u0440\u0438\u044f");
            label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD));
            panel1.add(label3, cc.xy(15, 1));

            //---- joinSeriesBtn ----
            joinSeriesBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
            joinSeriesBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    joinSeriesBtnActionPerformed(e);
                }
            });
            panel1.add(joinSeriesBtn, cc.xy(17, 1));

            //---- createSeriaBtn ----
            createSeriaBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
            createSeriaBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createSeriaBtnActionPerformed(e);
                }
            });
            panel1.add(createSeriaBtn, cc.xy(19, 1));

            //---- editSeriaBtn ----
            editSeriaBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
            editSeriaBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editSeriaBtnActionPerformed(e);
                }
            });
            panel1.add(editSeriaBtn, cc.xy(20, 1));

            //---- removeSeriaBtn ----
            removeSeriaBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
            removeSeriaBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeSeriaBtnActionPerformed(e);
                }
            });
            panel1.add(removeSeriaBtn, cc.xy(21, 1));

            //======== scrollPane1 ========
            {

                //---- vechicleTypesList ----
                vechicleTypesList.setDoubleBuffered(true);
                vechicleTypesList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        vechicleTypesListValueChanged(e);
                    }
                });
                vechicleTypesList.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        vechicleTypesListKeyPressed(e);
                    }
                });
                vechicleTypesList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        vechicleTypesListMouseClicked(e);
                    }
                });
                scrollPane1.setViewportView(vechicleTypesList);
            }
            panel1.add(scrollPane1, cc.xywh(1, 2, 5, 1));

            //======== scrollPane2 ========
            {

                //---- manufactorsList ----
                manufactorsList.setDoubleBuffered(true);
                manufactorsList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        manufactorsListValueChanged(e);
                    }
                });
                manufactorsList.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        manufactorsListKeyPressed(e);
                    }
                });
                manufactorsList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        manufactorsListMouseClicked(e);
                    }
                });
                scrollPane2.setViewportView(manufactorsList);
            }
            panel1.add(scrollPane2, cc.xywh(7, 2, 7, 1));

            //======== scrollPane3 ========
            {

                //---- seriesList ----
                seriesList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        seriesListValueChanged(e);
                    }
                });
                seriesList.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        seriesListKeyPressed(e);
                    }
                });
                seriesList.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        seriesListMouseClicked(e);
                    }
                });
                scrollPane3.setViewportView(seriesList);
            }
            panel1.add(scrollPane3, cc.xywh(15, 2, 7, 1));

            //======== panel10 ========
            {
                panel10.setLayout(new FormLayout(
                    "default, $lcgap, 4*(21dlu)",
                    "default"));

                //---- label5 ----
                label5.setText("\u0414\u0432\u0438\u0433\u0430\u0442\u0435\u043b\u0438");
                label5.setFont(label5.getFont().deriveFont(label5.getFont().getStyle() | Font.BOLD));
                panel10.add(label5, cc.xy(1, 1));

                //---- createMotorBtn ----
                createMotorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
                createMotorBtn.setAlignmentY(0.0F);
                createMotorBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createMotorBtnActionPerformed(e);
                    }
                });
                panel10.add(createMotorBtn, cc.xy(3, 1));

                //---- editMotorBtn ----
                editMotorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
                editMotorBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editMotorBtnActionPerformed(e);
                    }
                });
                panel10.add(editMotorBtn, cc.xy(4, 1));

                //---- removeMotorBtn ----
                removeMotorBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
                removeMotorBtn.setEnabled(false);
                removeMotorBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeMotorBtnActionPerformed(e);
                    }
                });
                panel10.add(removeMotorBtn, cc.xy(5, 1));

                //---- joinMotorsBtn ----
                joinMotorsBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
                joinMotorsBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        joinMotorsBtnActionPerformed(e);
                    }
                });
                panel10.add(joinMotorsBtn, cc.xy(6, 1));
            }
            panel1.add(panel10, cc.xywh(1, 4, 21, 1));

            //======== scrollPane4 ========
            {

                //---- motorsTable ----
                motorsTable.setAutoCreateRowSorter(true);
                motorsTable.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        motorsTableKeyPressed(e);
                    }
                });
                motorsTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        motorsTableMouseClicked(e);
                    }
                });
                scrollPane4.setViewportView(motorsTable);
            }
            panel1.add(scrollPane4, cc.xywh(1, 5, 21, 1));

            //======== panel3 ========
            {
                panel3.setLayout(new FormLayout(
                    "default:grow, $lcgap, 21dlu, $lcgap, default:grow",
                    "3*(default), $lgap, default, $lgap, fill:default:grow"));

                //---- label6 ----
                label6.setText("\u041f\u043e\u0434\u0445\u043e\u0434\u044f\u0449\u0438\u0435 \u0444\u0438\u043b\u044c\u0442\u0440\u044b");
                label6.setFont(label6.getFont().deriveFont(label6.getFont().getStyle() | Font.BOLD));
                panel3.add(label6, cc.xy(1, 1));

                //---- label7 ----
                label7.setText("\u0412\u0441\u0435 \u0444\u0438\u043b\u044c\u0442\u0440\u044b");
                label7.setFont(label7.getFont().deriveFont(label7.getFont().getStyle() | Font.BOLD));
                panel3.add(label7, cc.xy(5, 1));

                //======== panel4 ========
                {
                    panel4.setLayout(new FormLayout(
                        "default, $lcgap, default:grow, 21pt",
                        "21dlu"));
                    panel4.add(label4, cc.xy(1, 1));

                    //---- filterSearchField ----
                    filterSearchField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            filterSearchFieldKeyPressed(e);
                        }
                    });
                    panel4.add(filterSearchField, cc.xy(3, 1));

                    //---- filterSearchBtn ----
                    filterSearchBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                    filterSearchBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            filterSearchBtnActionPerformed(e);
                        }
                    });
                    panel4.add(filterSearchBtn, cc.xy(4, 1));
                }
                panel3.add(panel4, cc.xy(5, 2));

                //======== scrollPane5 ========
                {

                    //---- filtersTable ----
                    filtersTable.setAutoCreateRowSorter(true);
                    filtersTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            filtersTableMouseClicked(e);
                        }
                    });
                    scrollPane5.setViewportView(filtersTable);
                }
                panel3.add(scrollPane5, cc.xywh(1, 2, 1, 6));

                //---- filterRelationComment ----
                filterRelationComment.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit.png")));
                filterRelationComment.setToolTipText("\u041a\u043e\u043c\u043c\u0435\u043d\u0442\u0430\u0440\u0438\u0439 \u043a \u0444\u0438\u043b\u044c\u0442\u0440\u0443 \u0443 \u0434\u0430\u043d\u043d\u043e\u0439 \u043c\u0430\u0448\u0438\u043d\u044b");
                filterRelationComment.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        filterRelationCommentActionPerformed(e);
                    }
                });
                panel3.add(filterRelationComment, cc.xy(3, 2));

                //---- attachFilterBtn ----
                attachFilterBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_left_green_24.png")));
                attachFilterBtn.setToolTipText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440 \u0432 \u043f\u0440\u0438\u043c\u0435\u043d\u0438\u043c\u043e\u0441\u0442\u044c");
                attachFilterBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        attachFilterBtnActionPerformed(e);
                    }
                });
                panel3.add(attachFilterBtn, cc.xy(3, 3));

                //======== scrollPane6 ========
                {

                    //---- allFiltersTable ----
                    allFiltersTable.setAutoCreateRowSorter(true);
                    allFiltersTable.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            allFiltersTableMouseClicked(e);
                        }
                    });
                    scrollPane6.setViewportView(allFiltersTable);
                }
                panel3.add(scrollPane6, cc.xywh(5, 3, 1, 5));

                //---- detachFilterBtn ----
                detachFilterBtn.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/arrow_right_green_24.png")));
                detachFilterBtn.setToolTipText("\u0423\u0431\u0440\u0430\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440 \u0438\u0437 \u043f\u0440\u0438\u043c\u0435\u043d\u0438\u043c\u043e\u0441\u0442\u0438");
                detachFilterBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        detachFilterBtnActionPerformed(e);
                    }
                });
                panel3.add(detachFilterBtn, cc.xy(3, 5));
            }
            panel1.add(panel3, cc.xywh(1, 7, 21, 1));
        }
        add(panel1, cc.xy(1, 1));

        //======== listsPopupMenu ========
        {
            listsPopupMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    listsPopupMenuPopupMenuWillBecomeVisible(e);
                }
            });

            //---- miCopyToExcel ----
            miCopyToExcel.setText("\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 Excel");
            miCopyToExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    miCopyToExcelActionPerformed(e);
                }
            });
            listsPopupMenu.add(miCopyToExcel);

            //---- miExportToExcel ----
            miExportToExcel.setText("\u0412\u044b\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0432 Excel");
            miExportToExcel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    miExportToExcelActionPerformed(e);
                }
            });
            listsPopupMenu.add(miExportToExcel);
            listsPopupMenu.addSeparator();

            //---- menuItem1 ----
            menuItem1.setText("\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440");
            menuItem1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyToClipboard(e);
                }
            });
            listsPopupMenu.add(menuItem1);

            //---- menuItem3 ----
            menuItem3.setText("\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430");
            menuItem3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pasteFromClipboard(e);
                }
            });
            listsPopupMenu.add(menuItem3);
            listsPopupMenu.addSeparator();

            //---- menuItem2 ----
            menuItem2.setText("\u041f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440 (\u0421 \u0443\u0434\u0430\u043b\u0435\u043d\u0438\u0435\u043c)");
            menuItem2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cutToClipboard(e);
                }
            });
            listsPopupMenu.add(menuItem2);

            //---- menuItem4 ----
            menuItem4.setText("\u0412\u044b\u043d\u0435\u0441\u0442\u0438 \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430");
            menuItem4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pasteFromClipboard(e);
                }
            });
            listsPopupMenu.add(menuItem4);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Sasha Ivanov
    private JPanel panel1;
    private JLabel label1;
    private JButton createTypeBtn;
    private JButton editTypeBtn;
    private JButton removeTypeBtn;
    private JLabel label2;
    private JButton joinManufactorsBtn;
    private JButton createManufactorBtn;
    private JButton editManufactorBtn;
    private JButton removeManufactorBtn;
    private JLabel label3;
    private JButton joinSeriesBtn;
    private JButton createSeriaBtn;
    private JButton editSeriaBtn;
    private JButton removeSeriaBtn;
    private JScrollPane scrollPane1;
    private JList vechicleTypesList;
    private JScrollPane scrollPane2;
    private JList manufactorsList;
    private JScrollPane scrollPane3;
    private JList seriesList;
    private JPanel panel10;
    private JLabel label5;
    private JButton createMotorBtn;
    private JButton editMotorBtn;
    private JButton removeMotorBtn;
    private JButton joinMotorsBtn;
    private JScrollPane scrollPane4;
    private JTable motorsTable;
    private JPanel panel3;
    private JLabel label6;
    private JLabel label7;
    private JPanel panel4;
    private JLabel label4;
    private JTextField filterSearchField;
    private JButton filterSearchBtn;
    private JScrollPane scrollPane5;
    private JTable filtersTable;
    private JButton filterRelationComment;
    private JButton attachFilterBtn;
    private JScrollPane scrollPane6;
    private JTable allFiltersTable;
    private JButton detachFilterBtn;
    private JPopupMenu listsPopupMenu;
    private JMenuItem miCopyToExcel;
    private JMenuItem miExportToExcel;
    private JMenuItem menuItem1;
    private JMenuItem menuItem3;
    private JMenuItem menuItem2;
    private JMenuItem menuItem4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // nothing
    }
}
