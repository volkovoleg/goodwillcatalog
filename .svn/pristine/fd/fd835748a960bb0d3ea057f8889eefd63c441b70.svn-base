/*
 * Created by JFormDesigner on Sun Dec 04 19:18:41 MSK 2011
 */

package ru.goodfil.catalog.ui.forms;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.*;
import javax.validation.constraints.NotNull;

import com.jgoodies.forms.layout.*;
import org.hibernate.validator.constraints.NotEmpty;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Unique;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.utils.JoinOptions;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: JoinWindow.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class JoinWindow extends CatalogJDialog {
    private JoinOptions joinOptions = JoinOptions.defaultOnes();

    private Collection<? extends Unique> items;

    private Long masterId;
    private Set<Long> slavesIds;

    public JoinWindow() {
        super();
        initComponents();

        cbDeleteEmptySlaves.setSelected(joinOptions.isDeleteEmptySlaves());
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    private void btnJoinActionPerformed(ActionEvent e) {
        this.masterId = null;
        this.slavesIds = null;

        if (lstJoinPositions.getSelectedValue() == null) {
            UIUtils.warning("Необходимо выбрать позицию, в которую будут скопированы остальные");
            return;
        }

        masterId = ((Unique) lstJoinPositions.getSelectedValue()).getId();
        slavesIds = new HashSet<Long>();
        for (Unique item : items) {
            if (!item.getId().equals(masterId)) {
                slavesIds.add(item.getId());
            }
        }

        joinOptions.setDeleteEmptySlaves(cbDeleteEmptySlaves.isSelected());

        dispose(DialogResult.YES);
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        this.masterId = null;
        this.slavesIds = null;

        joinOptions.setDeleteEmptySlaves(cbDeleteEmptySlaves.isSelected());

        dispose(DialogResult.NO);
    }

    public void setItems(@NotNull @NotEmpty Collection<? extends Unique> items) {
        lstJoinPositions.setListData(new Vector<Unique>(items));
        lstJoinPositions.setSelectedIndex(0);
        this.items = items;
    }

    public JoinOptions getJoinOptions() {
        return joinOptions;
    }

    public Long getMasterId() {
        return masterId;
    }

    public Set<Long> getSlavesIds() {
        return slavesIds;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        lstJoinPositions = new JList();
        cbDeleteEmptySlaves = new JCheckBox();
        panel1 = new JPanel();
        btnJoin = new JButton();
        btnCancel = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setTitle("\u041e\u0431\u044a\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435 \u043f\u043e\u0437\u0438\u0446\u0438\u0439");
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "$rgap, default:grow, $lcgap",
            "2*(default, $lgap), fill:default:grow, $lgap, default, $lgap, 17dlu"));

        //---- label1 ----
        label1.setText("\u0412\u044b \u0443\u0432\u0435\u0440\u0435\u043d\u044b \u0432 \u0442\u043e\u043c, \u0447\u0442\u043e \u0445\u043e\u0442\u0438\u0442\u0435 \u043e\u0431\u044a\u0435\u0434\u0438\u043d\u0438\u0442\u044c \u043f\u043e\u0437\u0438\u0446\u0438\u0438?");
        contentPane.add(label1, cc.xy(2, 1));

        //---- label2 ----
        label2.setText("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u043f\u043e\u0437\u0438\u0446\u0438\u044e, \u0432 \u043a\u043e\u0442\u043e\u0440\u0443\u044e \u0441\u043b\u0435\u0434\u0443\u0435\u0442 \u043f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u043e\u0441\u0442\u0430\u043b\u044c\u043d\u044b\u0435:");
        contentPane.add(label2, cc.xy(2, 3));

        //======== scrollPane1 ========
        {

            //---- lstJoinPositions ----
            lstJoinPositions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            scrollPane1.setViewportView(lstJoinPositions);
        }
        contentPane.add(scrollPane1, cc.xy(2, 5));

        //---- cbDeleteEmptySlaves ----
        cbDeleteEmptySlaves.setText("\u0423\u0434\u0430\u043b\u044f\u0442\u044c \u043f\u0443\u0441\u0442\u044b\u0435 \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u044b \u043f\u043e\u0441\u043b\u0435 \u043e\u0431\u044a\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u044f");
        contentPane.add(cbDeleteEmptySlaves, cc.xy(2, 7));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                "default:grow, 2*($lcgap, default)",
                "default"));

            //---- btnJoin ----
            btnJoin.setText("\u041e\u0431\u044a\u0435\u0434\u0438\u043d\u0435\u043d\u0438\u0435");
            btnJoin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnJoinActionPerformed(e);
                }
            });
            panel1.add(btnJoin, cc.xy(3, 1));

            //---- btnCancel ----
            btnCancel.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });
            panel1.add(btnCancel, cc.xy(5, 1));
        }
        contentPane.add(panel1, cc.xy(2, 9));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JList lstJoinPositions;
    private JCheckBox cbDeleteEmptySlaves;
    private JPanel panel1;
    private JButton btnJoin;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
