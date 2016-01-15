/*
 * Created by JFormDesigner on Sat Aug 27 22:32:15 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Vector;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DateWindow.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class DateWindow extends CatalogJDialog {
    private Calendar currentDate;

    private int minYear = 1990;
    private int maxYear = 2030;

    private static String getMonth(Integer i) {
        if (i == 0) return "Январь";
        if (i == 1) return "Феварь";
        if (i == 2) return "Март";
        if (i == 3) return "Апрель";
        if (i == 4) return "Май";
        if (i == 5) return "Июнь";
        if (i == 6) return "Июль";
        if (i == 7) return "Август";
        if (i == 8) return "Сентябрь";
        if (i == 9) return "Октябрь";
        if (i == 10) return "Ноябрь";
        if (i == 11) return "Декабрь";
        return null;
    }

    private static Integer getMonth(String s) {
        if ("Январь".equals(s)) return 0;
        if ("Феварь".equals(s)) return 1;
        if ("Март".equals(s)) return 2;
        if ("Апрель".equals(s)) return 3;
        if ("Май".equals(s)) return 4;
        if ("Июнь".equals(s)) return 5;
        if ("Июль".equals(s)) return 6;
        if ("Август".equals(s)) return 7;
        if ("Сентябрь".equals(s)) return 8;
        if ("Октябрь".equals(s)) return 9;
        if ("Ноябрь".equals(s)) return 10;
        if ("Декабрь".equals(s)) return 11;
        return -1;
    }

    public DateWindow(Frame owner) {
        super(owner);
        initComponents();

        setCurrentDate(Calendar.getInstance());
    }

    public DateWindow(Dialog owner) {
        super(owner);
        initComponents();

        setCurrentDate(Calendar.getInstance());
    }

    public Calendar getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Calendar currentDate) {
        this.currentDate = currentDate;

        Vector<Integer> years = new Vector<Integer>();
        int year = currentDate.get(Calendar.YEAR);
        for (int i = minYear; i <= maxYear; i++) {
            years.add(i);
        }
        cbYear.setModel(new DefaultComboBoxModel(years));
        cbYear.setSelectedItem(currentDate.get(Calendar.YEAR));

        Vector<String> months = new Vector<String>();
        int month = 0;
        for (int i = month; i < currentDate.getActualMaximum(Calendar.MONTH); i++) {
            months.add(getMonth(i));
        }
        cbMonth.setModel(new DefaultComboBoxModel(months));
        cbMonth.setSelectedItem(getMonth(currentDate.get(Calendar.MONTH)));

        Vector<Integer> days = new Vector<Integer>();
        int day = 1;
        for (int i = day; i <= currentDate.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            days.add(i);
        }
        cbDay.setModel(new DefaultComboBoxModel(days));
        cbDay.setSelectedItem(currentDate.get(Calendar.DAY_OF_MONTH));
    }

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    private void cbMonthActionPerformed(ActionEvent e) {
        try {
            int day = -1;
            if (cbDay.getSelectedItem() != null) {
                day = (Integer) cbDay.getSelectedItem();
            }

            int month = getMonth((String) cbMonth.getSelectedItem());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, month);
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            Vector<Integer> days = new Vector<Integer>();
            for (int i = 1; i <= max; i++) {
                days.add(i);
            }
            cbDay.setModel(new DefaultComboBoxModel(days));
            if (day != -1) cbDay.setSelectedItem(day);
            cbDay.repaint();
        } catch (Exception e2) {
        }
    }

    private void thisKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            btnOkActionPerformed(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            btnCancelActionPerformed(null);
        }
    }

    private void btnOkActionPerformed(ActionEvent e) {
        currentDate.set(Calendar.YEAR, (Integer) cbYear.getSelectedItem());
        currentDate.set(Calendar.MONTH, getMonth((String) cbMonth.getSelectedItem()));
        currentDate.set(Calendar.DAY_OF_MONTH, (Integer) cbDay.getSelectedItem());
        dispose(DialogResult.OK);
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        dispose(DialogResult.CANCEL);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        contentPanel = new JPanel();
        cbDay = new JComboBox();
        cbMonth = new JComboBox();
        cbYear = new JComboBox();
        panel1 = new JPanel();
        hSpacer1 = new JPanel(null);
        btnOk = new JButton();
        btnCancel = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0434\u0430\u0442\u0443");
        setResizable(false);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                thisKeyPressed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== contentPanel ========
        {
            contentPanel.setLayout(new FormLayout(
                    "30dlu, $lcgap, 70dlu, $lcgap, 50dlu",
                    "2*($lgap, default)"));

            //---- cbDay ----
            cbDay.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    thisKeyPressed(e);
                }
            });
            contentPanel.add(cbDay, cc.xy(1, 2));

            //---- cbMonth ----
            cbMonth.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cbMonthActionPerformed(e);
                }
            });
            cbMonth.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    thisKeyPressed(e);
                }
            });
            contentPanel.add(cbMonth, cc.xy(3, 2));

            //---- cbYear ----
            cbYear.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    thisKeyPressed(e);
                }
            });
            contentPanel.add(cbYear, cc.xy(5, 2));

            //======== panel1 ========
            {
                panel1.setLayout(new FormLayout(
                        "default:grow, 2*($lcgap, $button)",
                        "default"));
                panel1.add(hSpacer1, cc.xy(1, 1));

                //---- btnOk ----
                btnOk.setText("OK");
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnOkActionPerformed(e);
                    }
                });
                panel1.add(btnOk, cc.xy(3, 1));

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
            contentPanel.add(panel1, cc.xywh(1, 4, 5, 1));
        }
        contentPane.add(contentPanel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel contentPanel;
    private JComboBox cbDay;
    private JComboBox cbMonth;
    private JComboBox cbYear;
    private JPanel panel1;
    private JPanel hSpacer1;
    private JButton btnOk;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
