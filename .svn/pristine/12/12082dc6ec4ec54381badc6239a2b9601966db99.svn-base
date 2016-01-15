/*
 * Created by JFormDesigner on Sun Sep 04 18:23:08 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.goodfil.catalog.utils.Version;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: AboutWindow.java 128 2013-03-06 13:12:35Z chezxxx@gmail.com $
 */
public class AboutWindow extends JDialog {
    public AboutWindow(Frame owner) {
        super(owner);
        initComponents();

        lVersion.setText(Version.VERSION);
        lBuildDate.setText(Version.BUILD_TIMESTAMP);
    }

    public AboutWindow(Dialog owner) {
        super(owner);
        initComponents();
    }

    private void okButtonActionPerformed(ActionEvent e) {
        dispose();
    }

    private void dialogPaneKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        }
    }

    private void dialogPaneKeyPressed() {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label4 = new JLabel();
        label3 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        lVersion = new JLabel();
        label7 = new JLabel();
        lBuildDate = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u041e \u043f\u0440\u043e\u0433\u0440\u0430\u043c\u043c\u0435");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(Borders.DIALOG_BORDER);
            dialogPane.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    dialogPaneKeyPressed(e);
                }
            });
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new FormLayout(
                    "default, $lcgap, 171dlu",
                    "4*(default, $lgap), default"));

                //---- label1 ----
                label1.setText("\u0421\u0430\u0437\u043e\u043d\u043e\u0432 \u041a\u0438\u0440\u0438\u043b\u043b, \u041a\u0440\u044b\u043a\u0438\u043d \u0412\u0438\u0442\u0430\u043b\u0438\u0439 ");
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD));
                contentPanel.add(label1, cc.xywh(1, 1, 3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

                //---- label2 ----
                label2.setText("email:");
                label2.setFont(label2.getFont().deriveFont(label2.getFont().getStyle() | Font.BOLD));
                contentPanel.add(label2, cc.xy(1, 3));

                //---- label4 ----
                label4.setText("sazonovkirill@gmail.com, chezxxx@gmail.com ");
                contentPanel.add(label4, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

                //---- label3 ----
                label3.setText("skype:");
                label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD));
                contentPanel.add(label3, cc.xy(1, 5));

                //---- label5 ----
                label5.setText("sazonovkirill");
                contentPanel.add(label5, cc.xy(3, 5));

                //---- label6 ----
                label6.setText("\u0432\u0435\u0440\u0441\u0438\u044f:");
                contentPanel.add(label6, cc.xy(1, 7));

                //---- lVersion ----
                lVersion.setText("text");
                contentPanel.add(lVersion, cc.xy(3, 7));

                //---- label7 ----
                label7.setText("\u0434\u0430\u0442\u0430 \u0441\u0431\u043e\u0440\u043a\u0438:");
                contentPanel.add(label7, cc.xy(1, 9));

                //---- lBuildDate ----
                lBuildDate.setText("text");
                contentPanel.add(lBuildDate, cc.xy(3, 9));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                buttonBar.setLayout(new FormLayout(
                    "$glue, $button",
                    "pref"));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                okButton.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        dialogPaneKeyPressed();
                    }
                });
                buttonBar.add(okButton, cc.xy(2, 1));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label4;
    private JLabel label3;
    private JLabel label5;
    private JLabel label6;
    private JLabel lVersion;
    private JLabel label7;
    private JLabel lBuildDate;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
