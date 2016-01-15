/*
 * Created by JFormDesigner on Sat Aug 27 22:15:12 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Motor;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.UIUtils;

import javax.swing.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: MotorWindow.java 159 2013-07-10 09:15:01Z chezxxx@gmail.com $
 */
@Managed
public class MotorWindow extends CatalogJDialog {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private Motor motor = Motor.create();

    public MotorWindow() {
        initComponents();

        tbModelKeyReleased(null);
        tbMotorKeyReleased(null);
        tbDateFKeyReleased(null);
        tbDateTKeyReleased(null);
    }

    public MotorWindow(Frame owner) {
        super(owner);
        initComponents();

        tbModelKeyReleased(null);
        tbMotorKeyReleased(null);
        tbDateFKeyReleased(null);
        tbDateTKeyReleased(null);
    }

    public MotorWindow(Dialog owner) {
        super(owner);
        initComponents();

        tbModelKeyReleased(null);
        tbMotorKeyReleased(null);
        tbDateFKeyReleased(null);
        tbDateTKeyReleased(null);
    }

    public void setMotor(@NotNull @Valid Motor motor) {
        this.motor = motor;

        tbModel.setText(motor.getName());
        tbMotor.setText(motor.getEngine());
        tbHp.setText(motor.getHp());
        tbKw.setText(motor.getKw());
        tbDateF.setText(date(motor.getDateF()));
        tbDateT.setText(date(motor.getDateT()));

        tbModelKeyReleased(null);
        tbMotorKeyReleased(null);
        tbDateFKeyReleased(null);
        tbDateTKeyReleased(null);
    }

    public Motor getMotor() {
        return motor;
    }

    private String date(Date date) {
        if (date == null) {
            return "";
        } else {
            return sdf.format(date);
        }
    }

    private void okButtonActionPerformed(ActionEvent e) {
        //
        //  Проверки
        //
        if (!StringUtils.isBlank(tbDateF.getText()) && !UIUtils.parsable(tbDateF.getText(), sdf)) {
            UIUtils.error("Дата \"С\" задана неверно (правильный формат: дд.мм.гггг)");
            return;
        }
        if (!StringUtils.isBlank(tbDateT.getText()) && !UIUtils.parsable(tbDateT.getText(), sdf)) {
            UIUtils.error("Дата \"ПО\" задана неверно (правильный формат: дд.мм.гггг)");
            return;
        }

        motor.setName(tbModel.getText());
        motor.setEngine(tbMotor.getText());
        motor.setKw(tbKw.getText());
        motor.setHp(tbHp.getText());
        try {
            if (StringUtils.isBlank(tbDateF.getText())) {
                motor.setDateF(null);
            } else {
                motor.setDateF(sdf.parse(tbDateF.getText()));
            }

            if (StringUtils.isBlank(tbDateT.getText())) {
                motor.setDateT(null);
            } else {
                motor.setDateT(sdf.parse(tbDateT.getText()));
            }
        } catch (ParseException e1) {
            throw new RuntimeException(e1);
        }

        dispose(DialogResult.OK);
    }

    private void cancelButtonActionPerformed(ActionEvent e) {
        motor = null;
        dispose(DialogResult.CANCEL);
    }

    private void tbModelKeyReleased(KeyEvent e) {
        if (StringUtils.isBlank(tbModel.getText())) {
            tbModel.setBackground(UIUtils.COLOR_BAD);
        } else {
            tbModel.setBackground(UIUtils.COLOR_GOOD);
        }
    }

    private void tbMotorKeyReleased(KeyEvent e) {
        if (StringUtils.isBlank(tbMotor.getText())) {
            tbMotor.setBackground(UIUtils.COLOR_BAD);
        } else {
            tbMotor.setBackground(UIUtils.COLOR_GOOD);
        }
    }

    private void tbDateFKeyReleased(KeyEvent e) {
        if (StringUtils.isBlank(tbDateF.getText())) return;

        try {
            sdf.parse(tbDateF.getText());
            tbDateF.setBackground(UIUtils.COLOR_GOOD);
        } catch (ParseException pe) {
            tbDateF.setBackground(UIUtils.COLOR_BAD);
        }
    }

    private void tbDateTKeyReleased(KeyEvent e) {
        if (StringUtils.isBlank(tbDateT.getText())) return;

        try {
            sdf.parse(tbDateT.getText());
            tbDateT.setBackground(UIUtils.COLOR_GOOD);
        } catch (ParseException pe) {
            tbDateT.setBackground(UIUtils.COLOR_BAD);
        }
    }

    private void tbModelFocusLost(FocusEvent e) {
        tbModelKeyReleased(null);
    }

    private void tbMotorFocusLost(FocusEvent e) {
        tbMotorKeyReleased(null);
    }

    private void tbDateFFocusLost(FocusEvent e) {
        tbDateFKeyReleased(null);
    }

    private void tbDateTFocusLost(FocusEvent e) {
        tbDateTKeyReleased(null);
    }

    private void tbDateFFocusGained(FocusEvent e) {
        tbDateFKeyReleased(null);
    }

    private void tbDateTFocusGained(FocusEvent e) {
        tbDateTKeyReleased(null);
    }

    private void tbModelFocusGained(FocusEvent e) {
        tbModelKeyReleased(null);
    }

    private void tbMotorFocusGained(FocusEvent e) {
        tbMotorKeyReleased(null);
    }

    private void btnCalendarDateFActionPerformed(ActionEvent e) {
        DateWindow dateWindow = new DateWindow(this);
        try {
            Calendar calendar = Calendar.getInstance();
            Date date = sdf.parse(tbDateF.getText());
            calendar.setTime(date);
            dateWindow.setCurrentDate(calendar);
        } catch (ParseException pe) {
        }

        dateWindow.setVisible(true);
        if (dateWindow.getDialogResult() == DialogResult.OK) {
            tbDateF.setText(sdf.format(dateWindow.getCurrentDate().getTime()));
        }
    }

    private void btnCalendarDateTActionPerformed(ActionEvent e) {
        DateWindow dateWindow = new DateWindow(this);
        try {
            Calendar calendar = Calendar.getInstance();
            Date date = sdf.parse(tbDateT.getText());
            calendar.setTime(date);
            dateWindow.setCurrentDate(calendar);
        } catch (ParseException pe) {
        }

        dateWindow.setVisible(true);
        if (dateWindow.getDialogResult() == DialogResult.OK) {
            tbDateT.setText(sdf.format(dateWindow.getCurrentDate().getTime()));
        }
    }

    private void convertToHP(ActionEvent e) {
        int KW;
        int HP;
        try {
            KW = Integer.parseInt(tbKw.getText());
            if (KW <= 0) {
                UIUtils.error("Значение поля КВ не может быть отрицательным или нулевым");
                return;
            }
            HP = (int) (KW * 1.35962);
            tbHp.setText(String.valueOf(HP));
        } catch (NumberFormatException exception) {
            UIUtils.error("Неправильно заполненно поле КВ");
        }
    }

    private void convertToKW(ActionEvent e) {
        int KW;
        int HP;
        try {
            HP = Integer.parseInt(tbHp.getText());
            if (HP <= 0) {
                UIUtils.error("Значение поля ЛС не может быть отрицательным или нулевым");
                return;
            }
            KW = (int) (HP / 1.35962);
            tbKw.setText(String.valueOf(KW));
        } catch (NumberFormatException exception) {
            UIUtils.error("Неправильно заполненно поле ЛС");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        tbModel = new JTextField();
        label2 = new JLabel();
        tbMotor = new JTextField();
        label3 = new JLabel();
        tbKw = new JTextField();
        button1 = new JButton();
        label4 = new JLabel();
        tbHp = new JTextField();
        button2 = new JButton();
        label5 = new JLabel();
        tbDateF = new JTextField();
        btnCalendarDateF = new JButton();
        label6 = new JLabel();
        tbDateT = new JTextField();
        btnCalendarDateT = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setTitle("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u043c\u043e\u0442\u043e\u0440\u0435");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "default, $lcgap, default:grow, 2*($lcgap, default), $lcgap, default:grow, $lcgap, min",
                    "3*(default, $lgap), default"));

                //---- label1 ----
                label1.setText("\u041c\u043e\u0434\u0435\u043b\u044c *");
                contentPanel.add(label1, cc.xy(1, 1));

                //---- tbModel ----
                tbModel.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbModelKeyReleased(e);
                    }
                });
                tbModel.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbModelFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbModelFocusLost(e);
                    }
                });
                contentPanel.add(tbModel, cc.xywh(3, 1, 9, 1));

                //---- label2 ----
                label2.setText("\u041c\u043e\u0442\u043e\u0440 *");
                contentPanel.add(label2, cc.xy(1, 3));

                //---- tbMotor ----
                tbMotor.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbMotorKeyReleased(e);
                    }
                });
                tbMotor.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbMotorFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbMotorFocusLost(e);
                    }
                });
                contentPanel.add(tbMotor, cc.xywh(3, 3, 9, 1));

                //---- label3 ----
                label3.setText("\u041a\u0412");
                contentPanel.add(label3, cc.xy(1, 5));
                contentPanel.add(tbKw, cc.xy(3, 5));

                //---- button1 ----
                button1.setText("\u0432 \u041b\u0421");
                button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        convertToHP(e);
                    }
                });
                contentPanel.add(button1, cc.xy(5, 5));

                //---- label4 ----
                label4.setText("\u041b\u0421");
                contentPanel.add(label4, cc.xy(7, 5));
                contentPanel.add(tbHp, cc.xy(9, 5));

                //---- button2 ----
                button2.setText("\u0432 \u041a\u0412");
                button2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        convertToKW(e);
                    }
                });
                contentPanel.add(button2, cc.xy(11, 5));

                //---- label5 ----
                label5.setText("\u0421");
                contentPanel.add(label5, cc.xy(1, 7));

                //---- tbDateF ----
                tbDateF.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbDateFKeyReleased(e);
                    }
                });
                tbDateF.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbDateFFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbDateFFocusLost(e);
                    }
                });
                contentPanel.add(tbDateF, cc.xy(3, 7));

                //---- btnCalendarDateF ----
                btnCalendarDateF.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/clock.png")));
                btnCalendarDateF.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCalendarDateFActionPerformed(e);
                    }
                });
                contentPanel.add(btnCalendarDateF, cc.xy(5, 7));

                //---- label6 ----
                label6.setText("\u041f\u043e");
                contentPanel.add(label6, cc.xy(7, 7));

                //---- tbDateT ----
                tbDateT.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbDateTKeyReleased(e);
                    }
                });
                tbDateT.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbDateTFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbDateTFocusLost(e);
                    }
                });
                contentPanel.add(tbDateT, cc.xy(9, 7));

                //---- btnCalendarDateT ----
                btnCalendarDateT.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/clock.png")));
                btnCalendarDateT.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCalendarDateTActionPerformed(e);
                    }
                });
                contentPanel.add(btnCalendarDateT, cc.xy(11, 7));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button, $rgap, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                buttonBar.add(okButton, cc.xy(2, 1));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                buttonBar.add(cancelButton, cc.xy(4, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(430, 210);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField tbModel;
    private JLabel label2;
    private JTextField tbMotor;
    private JLabel label3;
    private JTextField tbKw;
    private JButton button1;
    private JLabel label4;
    private JTextField tbHp;
    private JButton button2;
    private JLabel label5;
    private JTextField tbDateF;
    private JButton btnCalendarDateF;
    private JLabel label6;
    private JTextField tbDateT;
    private JButton btnCalendarDateT;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
