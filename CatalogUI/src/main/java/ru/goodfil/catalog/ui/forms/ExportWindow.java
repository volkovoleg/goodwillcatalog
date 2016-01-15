/*
 * Created by JFormDesigner on Sun Apr 01 09:00:26 MSD 2012
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.export.domain.fullexport.FullCatalogExportParams;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ExportWindow.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class ExportWindow extends CatalogJDialog {
    private CarsService carsService = Services.getCarsService();

    private final ArrayListModel<FilterType> filterTypes = new ArrayListModel<FilterType>();

    public ExportWindow() {
        initComponents();
    }

    private void btnExportActionPerformed(ActionEvent e) {
        dispose(DialogResult.OK);
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        dispose(DialogResult.CANCEL);
    }

    private void btnSelectAllActionPerformed(ActionEvent e) {
        int[] allIndices = new int[lstFilterForms.getModel().getSize()];
        for (int i = 0; i < allIndices.length; i++) allIndices[i] = i;
        lstFilterForms.setSelectedIndices(allIndices);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        cbGoodwillOnly = new JCheckBox();
        label1 = new JLabel();
        btnSelectAll = new JButton();
        scrollPane1 = new JScrollPane();
        lstFilterForms = new JList();
        panel3 = new JPanel();
        btnExport = new JButton();
        btnCancel = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("\u0412\u044b\u0433\u0440\u0443\u0437\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445 \u041a\u0430\u0442\u0430\u043b\u043e\u0433\u0430 \u0432 Excel");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default:grow",
            "fill:default:grow, $lgap, default"));

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("\u0412\u044b\u0433\u0440\u0443\u0437\u043a\u0430 \u0430\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u0435\u0439"));
            panel1.setLayout(new FormLayout(
                "default:grow, default",
                "2*(default, $lgap), fill:default:grow"));

            //---- cbGoodwillOnly ----
            cbGoodwillOnly.setText("\u0412\u044b\u0433\u0440\u0443\u0436\u0430\u0442\u044c \u0422\u041e\u041b\u042c\u041a\u041e \u0438\u0437\u0434\u0435\u043b\u0438\u044f Goodwill");
            cbGoodwillOnly.setFont(cbGoodwillOnly.getFont().deriveFont(cbGoodwillOnly.getFont().getStyle() | Font.BOLD));
            panel1.add(cbGoodwillOnly, cc.xy(1, 1));

            //---- label1 ----
            label1.setText("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0442\u0438\u043f\u044b \u0438\u0437\u0434\u0435\u043b\u0438\u0439");
            panel1.add(label1, cc.xy(1, 3));

            //---- btnSelectAll ----
            btnSelectAll.setText("\u0412\u044b\u0431\u0440\u0430\u0442\u044c \u0432\u0441\u0435");
            btnSelectAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnSelectAllActionPerformed(e);
                }
            });
            panel1.add(btnSelectAll, cc.xy(2, 3));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(lstFilterForms);
            }
            panel1.add(scrollPane1, cc.xywh(1, 5, 2, 1));
        }
        contentPane.add(panel1, cc.xy(1, 1));

        //======== panel3 ========
        {
            panel3.setLayout(new FormLayout(
                "default:grow, 2*($lcgap, default)",
                "default"));

            //---- btnExport ----
            btnExport.setText("\u0412\u044b\u0433\u0440\u0443\u0437\u043a\u0430!");
            btnExport.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnExportActionPerformed(e);
                }
            });
            panel3.add(btnExport, cc.xy(3, 1));

            //---- btnCancel ----
            btnCancel.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });
            panel3.add(btnCancel, cc.xy(5, 1));
        }
        contentPane.add(panel3, cc.xy(1, 3));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        filterTypes.addAll(carsService.getFilterTypes());
        lstFilterForms.setModel(filterTypes);
        
        int[] allIndices = new int[lstFilterForms.getModel().getSize()];
        for (int i = 0; i < allIndices.length; i++) allIndices[i] = i;
        lstFilterForms.setSelectedIndices(allIndices);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JCheckBox cbGoodwillOnly;
    private JLabel label1;
    private JButton btnSelectAll;
    private JScrollPane scrollPane1;
    private JList lstFilterForms;
    private JPanel panel3;
    private JButton btnExport;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public FullCatalogExportParams getExportParams() {
        FullCatalogExportParams result = new FullCatalogExportParams();

        for (FilterType filterType : getSelectedFilterTypes()) {
            result.getFilterTypes().add(filterType.getId());
        }

        result.setGoodwillOnly(cbGoodwillOnly.isSelected());

        return result;
    }

    private List<FilterType> getSelectedFilterTypes() {
        List<FilterType> result = new ArrayList<FilterType>();
        for (int index : lstFilterForms.getSelectedIndices()) {
            result.add(filterTypes.get(index));
        }
        return result;
    }
}
