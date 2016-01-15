/*
 * Created by JFormDesigner on Tue Aug 09 19:53:11 EEST 2011
 */

package ru.goodfil.catalog.ui.forms;

import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NameWindow.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class NameWindow extends CatalogJDialog {
    private String initialName;
    private String name;

    public String getName() {
        return name;
    }

    public NameWindow() {
        initComponents();
    }

    private void btnSaveActionPerformed(ActionEvent e) {
        this.name = tbName.getText();
        dispose(DialogResult.OK);
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        this.name = null;
        dispose(DialogResult.CANCEL);
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;

        tbName.setText(initialName);
        tbName.setSelectionStart(0);
        tbName.setSelectionEnd(initialName.length());
    }

    private void tbNameKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            btnCancelActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSaveActionPerformed(null);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        tbName = new JTextField();
        btnSave = new JButton();
        btnCancel = new JButton();

        //======== this ========
        setTitle("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435");
        setResizable(false);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435");

        //---- tbName ----
        tbName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tbNameKeyPressed(e);
            }
        });

        //---- btnSave ----
        btnSave.setText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSaveActionPerformed(e);
            }
        });

        //---- btnCancel ----
        btnCancel.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCancelActionPerformed(e);
            }
        });

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(tbName, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(tbName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addContainerGap(8, Short.MAX_VALUE))
        );
        setSize(370, 105);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        tbName.setText(initialName);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JTextField tbName;
    private JButton btnSave;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
