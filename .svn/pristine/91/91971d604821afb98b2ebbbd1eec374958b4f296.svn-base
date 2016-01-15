/*
 * Created by JFormDesigner on Sun Dec 11 09:54:07 MSK 2011
 */

package ru.goodfil.catalog.ui.forms.dict;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.ListAdapter;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.utils.Assert;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Кирилл Сазонов
 */
@Managed
public class FilterFormWindow extends CatalogJDialog {
    @NotNull
    private CarsService carsService = Services.getCarsService();

    @NotNull
    private FiltersService filtersService = Services.getFiltersService();

    private ListAdapter<FilterType> filterTypesAdapter;
    private ListAdapter<FilterForm> filterFormsAdapter;

    public FilterFormWindow(Frame owner) {
        super(owner);
        initComponents();
        init();
        adjustButtonsVisibility();
    }

    public FilterFormWindow(Dialog owner) {
        super(owner);
        initComponents();
        init();
        adjustButtonsVisibility();
    }

    private void adjustButtonsVisibility() {
        btnAddFilterType.setEnabled(true);
        btnEditFilterType.setEnabled(filterTypesAdapter.isOneSelected());
        btnRemoveFilterType.setEnabled(filterTypesAdapter.isOneSelected());

        btnAddFilterForm.setEnabled(filterTypesAdapter.isOneSelected());
        btnEditFilterForm.setEnabled(filterTypesAdapter.isOneSelected() && filterFormsAdapter.isOneSelected());
        btnRemoveFilterForm.setEnabled(filterTypesAdapter.isOneSelected() && filterFormsAdapter.isOneSelected());
    }

    @Init
    @ValidateAfter
    private void init() {
        filterTypesAdapter = new ListAdapter<FilterType>(FilterType.class, lstFilterTypes);
        filterFormsAdapter = new ListAdapter<FilterForm>(FilterForm.class, lstFilterForms);

        reReadFilterTypes();
    }

    private void reReadFilterTypes() {
        filterTypesAdapter.clear();
        filterTypesAdapter.addAll(carsService.getFilterTypes());
    }

    private void reReadFilterForms(@NotNull final String filterTypeName, @NotNull final String filterTypeCode) {
        lSelectedFilterType.setText(filterTypeName);
        filterFormsAdapter.clear();
        filterFormsAdapter.addAll(carsService.getFilterFormsByFilterTypeCode(filterTypeCode));
    }

    private void btnCloseActionPerformed(ActionEvent e) {
        dispose(DialogResult.OK);
    }

    /**
     * Добавить тип изделия.
     */
    private void btnAddFilterTypeActionPerformed(ActionEvent e) {
        FilterTypeEditWindow ftew = FilterTypeEditWindow.createFilterType();
        ftew.setVisible(true);

        if (ftew.getDialogResult() == DialogResult.OK) {
            filtersService.saveFilterType(ftew.getFilterType());
        }

        reReadFilterTypes();
    }

    /**
     * Переименование типа изделия.
     */
    private void btnEditFilterTypeActionPerformed(ActionEvent e) {
        FilterType selectedFilterType = filterTypesAdapter.getSelectedItem();
        FilterTypeEditWindow ftew = FilterTypeEditWindow.editFilterType(selectedFilterType);
        ftew.setVisible(true);

        if (ftew.getDialogResult() == DialogResult.OK) {
            filtersService.updateFilterType(ftew.getFilterType());
        }

        reReadFilterTypes();
    }

    private void lstFilterTypesValueChanged(ListSelectionEvent e) {
        FilterType filterType = filterTypesAdapter.getSelectedItem();
        if (filterType != null) {
            reReadFilterForms(filterType.getName(), filterType.getCode());
        }
        adjustButtonsVisibility();
    }

    private void lstFilterFormsValueChanged(ListSelectionEvent e) {
        adjustButtonsVisibility();
    }

    private void btnAddFilterFormActionPerformed(ActionEvent e) {
        FilterType filterType = filterTypesAdapter.getSelectedItem();
        Assert.notNull(filterType);

        FilterFormEditWindow ffew = FilterFormEditWindow.createFilterForm(filterType.getCode());
        ffew.setVisible(true);

        if (ffew.getDialogResult() == DialogResult.OK) {
            filtersService.saveFilterForm(ffew.getFilterForm());
            reReadFilterForms(filterType.getName(), filterType.getCode());
        }
    }

    private void btnEditFilterFormActionPerformed(ActionEvent e) {
        FilterType filterType = filterTypesAdapter.getSelectedItem();
        Assert.notNull(filterType);

        FilterForm filterForm = filterFormsAdapter.getSelectedItem();
        Assert.notNull(filterForm);

        FilterFormEditWindow ffew = FilterFormEditWindow.editFilterForm(filterForm);
        ffew.setVisible(true);

        if (ffew.getDialogResult() == DialogResult.OK) {
            filtersService.updateFilterForm(ffew.getFilterForm());
            reReadFilterForms(filterType.getName(), filterType.getCode());
        }
    }

    /**
     * Удаление типа фильтра.
     */
    private void btnRemoveFilterTypeActionPerformed(ActionEvent e) {
        FilterType filterType = filterTypesAdapter.getSelectedItem();
        Assert.notNull(filterType);

        List<FilterForm> filterForms = new ArrayList<FilterForm>();
        filterForms.addAll(carsService.getFilterFormsByFilterTypeCode(filterType.getCode()));
        if (filterForms.size() > 0) {
            UIUtils.warning("Удалить тип изделия можно только, если к нему не привязаны формы изделия.\n");
        }
        if (filterForms.size() == 0 && UIUtils.askDelete()) {
            filtersService.deleteFilterTypeById(filterType.getId());
            reReadFilterTypes();
        }
    }

    /**
     * Удаление формы фильтра.
     */
    private void btnRemoveFilterFormActionPerformed(ActionEvent e) {
        FilterForm filterForm = filterFormsAdapter.getSelectedItem();
        Assert.notNull(filterForm);
        List<Filter> filters = new ArrayList<Filter>();
        filters.addAll(filtersService.getFiltersByFilterFormId(filterForm.getId()));
        if (filters.size() > 0) {
            UIUtils.warning("Удалить вид изделия можно только в случае, если нет созданных с ним позиций.\n ");
        }
        if (filters.size() == 0 && UIUtils.askDelete()) {
            filtersService.deleteFilterFormById(filterForm.getId());
            reReadFilterForms(filterTypesAdapter.getSelectedItem().getName(), filterTypesAdapter.getSelectedItem().getCode());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        panel3 = new JPanel();
        btnAddFilterType = new JButton();
        btnEditFilterType = new JButton();
        btnRemoveFilterType = new JButton();
        scrollPane1 = new JScrollPane();
        lstFilterTypes = new JList();
        panel2 = new JPanel();
        panel5 = new JPanel();
        label1 = new JLabel();
        lSelectedFilterType = new JLabel();
        panel4 = new JPanel();
        btnAddFilterForm = new JButton();
        btnEditFilterForm = new JButton();
        btnRemoveFilterForm = new JButton();
        scrollPane2 = new JScrollPane();
        lstFilterForms = new JList();
        panel6 = new JPanel();
        btnClose = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("\u0422\u0438\u043f\u044b \u0438 \u0444\u043e\u0440\u043c\u044b \u0444\u0438\u043b\u044c\u0442\u0440\u043e\u0432");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default:grow, $lcgap, default:grow",
            "fill:default:grow, $lgap, default"));

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("\u0422\u0438\u043f\u044b \u0444\u0438\u043b\u044c\u0442\u0440\u043e\u0432"));
            panel1.setLayout(new FormLayout(
                "default:grow",
                "default, $lgap, fill:default:grow"));

            //======== panel3 ========
            {
                panel3.setLayout(new FormLayout(
                    "3*(default, $lcgap), default",
                    "default"));

                //---- btnAddFilterType ----
                btnAddFilterType.setText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c");
                btnAddFilterType.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add.png")));
                btnAddFilterType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAddFilterTypeActionPerformed(e);
                    }
                });
                panel3.add(btnAddFilterType, cc.xy(1, 1));

                //---- btnEditFilterType ----
                btnEditFilterType.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit.png")));
                btnEditFilterType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnEditFilterTypeActionPerformed(e);
                    }
                });
                panel3.add(btnEditFilterType, cc.xy(3, 1));

                //---- btnRemoveFilterType ----
                btnRemoveFilterType.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete.png")));
                btnRemoveFilterType.setEnabled(false);
                btnRemoveFilterType.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveFilterTypeActionPerformed(e);
                    }
                });
                panel3.add(btnRemoveFilterType, cc.xy(5, 1));
            }
            panel1.add(panel3, cc.xy(1, 1));

            //======== scrollPane1 ========
            {

                //---- lstFilterTypes ----
                lstFilterTypes.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        lstFilterTypesValueChanged(e);
                    }
                });
                scrollPane1.setViewportView(lstFilterTypes);
            }
            panel1.add(scrollPane1, cc.xy(1, 3));
        }
        contentPane.add(panel1, cc.xy(1, 1));

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("\u0424\u043e\u0440\u043c\u044b \u0444\u0438\u043b\u044c\u0442\u0440\u043e\u0432"));
            panel2.setLayout(new FormLayout(
                "default:grow",
                "2*(default, $lgap), fill:default:grow"));

            //======== panel5 ========
            {
                panel5.setLayout(new FormLayout(
                    "default, $lcgap, default:grow",
                    "default"));

                //---- label1 ----
                label1.setText("\u0422\u0438\u043f \u0444\u0438\u043b\u044c\u0442\u0440\u0430:");
                panel5.add(label1, cc.xy(1, 1));

                //---- lSelectedFilterType ----
                lSelectedFilterType.setFont(lSelectedFilterType.getFont().deriveFont(lSelectedFilterType.getFont().getStyle() | Font.BOLD));
                panel5.add(lSelectedFilterType, cc.xy(3, 1));
            }
            panel2.add(panel5, cc.xy(1, 1));

            //======== panel4 ========
            {
                panel4.setLayout(new FormLayout(
                    "3*(default, $lcgap), default",
                    "default"));

                //---- btnAddFilterForm ----
                btnAddFilterForm.setText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c");
                btnAddFilterForm.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add.png")));
                btnAddFilterForm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnAddFilterFormActionPerformed(e);
                    }
                });
                panel4.add(btnAddFilterForm, cc.xy(1, 1));

                //---- btnEditFilterForm ----
                btnEditFilterForm.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit.png")));
                btnEditFilterForm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnEditFilterFormActionPerformed(e);
                    }
                });
                panel4.add(btnEditFilterForm, cc.xy(3, 1));

                //---- btnRemoveFilterForm ----
                btnRemoveFilterForm.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete.png")));
                btnRemoveFilterForm.setEnabled(false);
                btnRemoveFilterForm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveFilterFormActionPerformed(e);
                    }
                });
                panel4.add(btnRemoveFilterForm, cc.xy(5, 1));
            }
            panel2.add(panel4, cc.xy(1, 3));

            //======== scrollPane2 ========
            {

                //---- lstFilterForms ----
                lstFilterForms.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        lstFilterFormsValueChanged(e);
                    }
                });
                scrollPane2.setViewportView(lstFilterForms);
            }
            panel2.add(scrollPane2, cc.xy(1, 5));
        }
        contentPane.add(panel2, cc.xy(3, 1));

        //======== panel6 ========
        {
            panel6.setLayout(new FormLayout(
                "default:grow, $lcgap, default",
                "default"));

            //---- btnClose ----
            btnClose.setText("\u0417\u0430\u043a\u0440\u044b\u0442\u044c");
            btnClose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCloseActionPerformed(e);
                }
            });
            panel6.add(btnClose, cc.xy(3, 1));
        }
        contentPane.add(panel6, cc.xy(3, 3));
        setSize(725, 465);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JPanel panel3;
    private JButton btnAddFilterType;
    private JButton btnEditFilterType;
    private JButton btnRemoveFilterType;
    private JScrollPane scrollPane1;
    private JList lstFilterTypes;
    private JPanel panel2;
    private JPanel panel5;
    private JLabel label1;
    private JLabel lSelectedFilterType;
    private JPanel panel4;
    private JButton btnAddFilterForm;
    private JButton btnEditFilterForm;
    private JButton btnRemoveFilterForm;
    private JScrollPane scrollPane2;
    private JList lstFilterForms;
    private JPanel panel6;
    private JButton btnClose;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
