/*
 * Created by JFormDesigner on Thu Sep 01 10:33:33 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterStatus;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.services.PropertiesService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.*;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.SelectItem;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterWindow.java 129 2013-03-14 09:21:08Z chezxxx@gmail.com $
 */
public class FilterWindow extends CatalogJDialog {
    private final Filter filter;

    private final CarsService carsService = Services.getCarsService();
    private final FiltersService filtersService = Services.getFiltersService();

    private final DefaultLongDictionary<FilterForm> filterForms = new DefaultLongDictionary<FilterForm>() {
        @Override
        protected String getLabel(FilterForm item) {
            return item.getName();
        }
    };

    private final DefaultStringDictionary<FilterType> filterTypes = new DefaultStringDictionary<FilterType>() {
        @Override
        protected String getLabel(FilterType item) {
            return item.getName();
        }

    };

    private ComboAdapter filterFormsCombo;
    private ComboAdapter filterTypesCombo;

    public FilterWindow() {
        this.filter = new Filter();

        reReadFilterTypes();

        initComponents();

        filterFormsCombo = new ComboAdapter(cbFilterForm, filterForms);
        filterTypesCombo = new ComboAdapter(cbFilterType, filterTypes);

        init(filter);

        adjustButtonsEnabled();


        Toolkit.getDefaultToolkit().addAWTEventListener(eventListener, AWTEvent.KEY_EVENT_MASK);
    }

    public FilterWindow(Filter fitler) {
        this.filter = fitler;
        reReadFilterTypes();

        initComponents();

        filterFormsCombo = new ComboAdapter(cbFilterForm, filterForms);
        filterTypesCombo = new ComboAdapter(cbFilterType, filterTypes);

        cbFilterForm.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustButtonsEnabled();
            }
        });

        cbFilterType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustButtonsEnabled();
            }
        });

        cbFilterStatus.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                adjustButtonsEnabled();
            }
        });

        init(filter);

        adjustButtonsEnabled();


        Toolkit.getDefaultToolkit().addAWTEventListener(eventListener, AWTEvent.KEY_EVENT_MASK);
    }

    private final AWTEventListener eventListener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event instanceof KeyEvent && event.getID() == KeyEvent.KEY_PRESSED) {
                KeyEvent keyEvent = (KeyEvent) event;
                if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelButtonActionPerformed(null);
                }
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && keyEvent.isControlDown()) {
                    okButtonActionPerformed(null);
                }
            }
        }
    };

    private void reReadFilterTypes() {
        filterTypes.setItems(carsService.getFilterTypes());
    }

    private void reReadFilterForms(String filterTypeCode) {
        List<FilterForm> filterFormsList = carsService.getFilterFormsByFilterTypeCode(filterTypeCode);
        filterForms.setItems(filterFormsList);
    }

    private void adjustParameters() {
        Long filterFormId = filterFormsCombo.getLongValue();

        if (filterFormId != null) {
            FilterForm filterForm = carsService.getFilterFormById(filterFormId);

            tbAParam.setEnabled(filterForm.getaParam());
            tbBParam.setEnabled(filterForm.getbParam());
            tbCParam.setEnabled(filterForm.getcParam());
            tbDParam.setEnabled(filterForm.getdParam());
            tbEParam.setEnabled(filterForm.geteParam());
            tbFParam.setEnabled(filterForm.getfParam());
            tbGParam.setEnabled(filterForm.getgParam());
            tbHParam.setEnabled(filterForm.gethParam());
            tbPbParam.setEnabled(filterForm.getBpParam());
            tbNrParam.setEnabled(filterForm.getNrParam());
        }
    }

    private void init(Filter filter) {
        tbName.setText(filter.getName());
        tbAdditionalInfo.setText(filter.getEan());

        tbImage.setText(filter.getImage());

        tbAParam.setText(filter.getaParam());
        tbBParam.setText(filter.getbParam());
        tbCParam.setText(filter.getcParam());
        tbDParam.setText(filter.getdParam());
        tbEParam.setText(filter.geteParam());
        tbFParam.setText(filter.getfParam());
        tbGParam.setText(filter.getgParam());
        tbHParam.setText(filter.gethParam());

        tbPbParam.setText(filter.getPbParam());
        tbNrParam.setText(filter.getNrParam());

        if (filter.getFilterTypeCode() != null) {
            filterTypesCombo.setValue(filter.getFilterTypeCode());
        }
        if (filter.getFilterFormId() != null) {
            filterFormsCombo.setValue(filter.getFilterFormId().toString());
        }
        /*
        * Инициализация компонента совсем простая, напрямую кинул в конструктор компонента нужные значения через
        * jformdesigner. Большой минус - жесткая привязка, но зато экономично (т.к. битовая маска) и минимум кода.
        */
        if(filter.getStatusId() != null) {
            cbFilterStatus.setSelectedItem(FilterStatus.asString(filter.getStatusId()));
        }
    }

    public Filter release() {
        Assert.notNull(filter);

        filter.setName(tbName.getText().toUpperCase());
        filter.setEan(tbAdditionalInfo.getText());

        if(tbImage.getText() == null || tbImage.getText().equals("")){
            filter.setImage(filter.getName().replace(" ","").toUpperCase() + ".jpg");
        }
        else {
            filter.setImage(tbImage.getText().toUpperCase());
        }


        filter.setaParam(tbAParam.getText());
        filter.setbParam(tbBParam.getText());
        filter.setcParam(tbCParam.getText());
        filter.setdParam(tbDParam.getText());
        filter.seteParam(tbEParam.getText());
        filter.setfParam(tbFParam.getText());
        filter.setgParam(tbGParam.getText());
        filter.sethParam(tbHParam.getText());

        filter.setPbParam(tbPbParam.getText());
        filter.setNrParam(tbNrParam.getText());
        filter.setMannStatus(new Integer(0));
        filter.setFilterTypeCode(filterTypesCombo.getValue());
        filter.setFilterFormId(filterFormsCombo.getLongValue());
        filter.setStatusId(FilterStatus.getKey(cbFilterStatus.getSelectedItem().toString()));
        return filter;
    }


    private static void showValidationPanel(JList list, ValidationResult result) {
        list.setBorder(new LineBorder(Color.BLACK));
        list.setBackground(Color.YELLOW);

        list.setCellRenderer(new MessagesRenderer());

        if (result.isEmpty()) {
            list.setVisibleRowCount(0);
            list.setVisible(false);
        } else {
            list.setVisibleRowCount(result.getErrors().size());
            list.setModel(new ArrayListModel<ValidationMessage>(result.getErrors()));
        }

        list.repaint();
    }

    @PageAction
    private void okButtonActionPerformed(ActionEvent e) {
        if (requiredFields()) {
            dispose(DialogResult.OK);
            Toolkit.getDefaultToolkit().removeAWTEventListener(eventListener);
        }
    }

    @PageAction
    private void cancelButtonActionPerformed(ActionEvent e) {
        dispose(DialogResult.CANCEL);
        Toolkit.getDefaultToolkit().removeAWTEventListener(eventListener);
    }

    private void tbNameKeyTyped(KeyEvent e) {
        adjustButtonsEnabled();
    }

    private void tbNameFocusGained(FocusEvent e) {
        adjustButtonsEnabled();
    }

    private void btnUploadImageActionPerformed() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Изображения (*.png, *.gif, *.jpg)", "png", "gif", "jpg"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();
            Assert.notNull(destinationFile);
            Assert.isTrue(destinationFile.exists());
            Assert.isTrue(destinationFile.isFile());

            String targetDirectory = Services.getPropertiesService().getProperty(PropertiesService.IMAGES_DIRECTORY_PATH);
            if (targetDirectory == null) {
                UIUtils.error("Загрузка изображения невозможна из-за неверных настроек приложения. " +
                        "\nОбратитесь к Администратору!");
                return;
            } else {
                File directory = new File(targetDirectory);
                if (!directory.isDirectory() || !directory.exists()) {
                    UIUtils.error("Загрузка изображения невозможна из-за неверных настроек приложения. " +
                            "\nОбратитесь к Администратору!");
                    return;
                }

                try {
                    File targetFile = new File(targetDirectory + File.separator + destinationFile.getName());
                    FileUtils.copyFile(destinationFile, targetFile);

                    tbImage.setText(destinationFile.getName());
                    UIUtils.info("Изображение загружено!");
                } catch (IOException e) {
                    UIUtils.error("Не удалось скопировать изображение. Обратитесь к Администратору.");
                    e.printStackTrace();
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Sasha aaa
        contentPanel = new JPanel();
        label16 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        label1 = new JLabel();
        cbFilterType = new JComboBox();
        label5 = new JLabel();
        tbAParam = new JTextField();
        label2 = new JLabel();
        cbFilterForm = new JComboBox();
        label6 = new JLabel();
        tbBParam = new JTextField();
        label3 = new JLabel();
        tbName = new JTextField();
        label7 = new JLabel();
        tbCParam = new JTextField();
        label4 = new JLabel();
        panel2 = new JPanel();
        tbImage = new JTextField();
        btnUploadImage = new JButton();
        label8 = new JLabel();
        tbDParam = new JTextField();
        label19 = new JLabel();
        cbFilterStatus = new JComboBox();
        label9 = new JLabel();
        tbEParam = new JTextField();
        label15 = new JLabel();
        scrollPane1 = new JScrollPane();
        tbAdditionalInfo = new JTextArea();
        label10 = new JLabel();
        tbFParam = new JTextField();
        label11 = new JLabel();
        tbGParam = new JTextField();
        label12 = new JLabel();
        tbHParam = new JTextField();
        label13 = new JLabel();
        tbPbParam = new JTextField();
        label14 = new JLabel();
        tbNrParam = new JTextField();
        panel1 = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setTitle("\u0424\u0438\u043b\u044c\u0442\u0440");
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default:grow",
            "fill:default:grow"));

        //======== contentPanel ========
        {

            // JFormDesigner evaluation mark
            contentPanel.setBorder(new javax.swing.border.CompoundBorder(
                new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                    "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                    javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), contentPanel.getBorder())); contentPanel.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

            contentPanel.setLayout(new FormLayout(
                "$rgap, default, $lcgap, default:grow, $lcgap, right:20dlu, $lcgap, default:grow, $rgap",
                "13*(default, $lgap), fill:default:grow, $lgap, default"));

            //---- label16 ----
            label16.setText("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044e \u043e \u0444\u0438\u043b\u044c\u0442\u0440\u0435. ");
            contentPanel.add(label16, cc.xywh(2, 1, 7, 1));

            //---- label17 ----
            label17.setText("\u041f\u043e\u043b\u044f, \u043f\u043e\u043c\u0435\u0447\u0435\u043d\u043d\u044b\u0435 \u0437\u0432\u0435\u0437\u0434\u043e\u0447\u043a\u0430\u043c\u0438, \u044f\u0432\u043b\u044f\u044e\u0442\u0441\u044f \u043e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u044b\u043c\u0438 \u0434\u043b\u044f \u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u044f.");
            contentPanel.add(label17, cc.xywh(2, 3, 7, 1));

            //---- label18 ----
            label18.setText("\u0427\u0442\u043e\u0431\u044b \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0442\u0438\u043f \u0438\u043b\u0438 \u0444\u043e\u0440\u043c\u0443 \u043f\u0435\u0440\u0435\u0439\u0434\u0438\u0442\u0435 \u0432 \"\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b\"->\"\u0422\u0438\u043f\u044b \u0438 \u0444\u043e\u0440\u043c\u044b \u0444\u0438\u043b\u044c\u0442\u0440\u043e\u0432\"");
            label18.setForeground(Color.blue);
            contentPanel.add(label18, cc.xywh(2, 5, 7, 1));

            //---- label1 ----
            label1.setText("\u0422\u0438\u043f*");
            contentPanel.add(label1, cc.xy(2, 7));
            contentPanel.add(cbFilterType, cc.xy(4, 7));

            //---- label5 ----
            label5.setText("A");
            contentPanel.add(label5, cc.xy(6, 7));
            contentPanel.add(tbAParam, cc.xy(8, 7));

            //---- label2 ----
            label2.setText("\u0424\u043e\u0440\u043c\u0430*");
            contentPanel.add(label2, cc.xy(2, 9));
            contentPanel.add(cbFilterForm, cc.xy(4, 9));

            //---- label6 ----
            label6.setText("B");
            contentPanel.add(label6, cc.xy(6, 9));
            contentPanel.add(tbBParam, cc.xy(8, 9));

            //---- label3 ----
            label3.setText("\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435*");
            contentPanel.add(label3, cc.xy(2, 11));

            //---- tbName ----
            tbName.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    tbNameKeyTyped(e);
                }
            });
            tbName.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    tbNameFocusGained(e);
                }
                @Override
                public void focusLost(FocusEvent e) {
                    tbNameFocusGained(e);
                }
            });
            contentPanel.add(tbName, cc.xy(4, 11));

            //---- label7 ----
            label7.setText("C");
            contentPanel.add(label7, cc.xy(6, 11));
            contentPanel.add(tbCParam, cc.xy(8, 11));

            //---- label4 ----
            label4.setText("\u0418\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435");
            contentPanel.add(label4, cc.xy(2, 13));

            //======== panel2 ========
            {
                panel2.setLayout(new FormLayout(
                    "default:grow, $lcgap, default",
                    "default"));
                panel2.add(tbImage, cc.xy(1, 1));

                //---- btnUploadImage ----
                btnUploadImage.setText("...");
                btnUploadImage.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnUploadImageActionPerformed();
                    }
                });
                panel2.add(btnUploadImage, cc.xy(3, 1));
            }
            contentPanel.add(panel2, cc.xy(4, 13));

            //---- label8 ----
            label8.setText("D");
            contentPanel.add(label8, cc.xy(6, 13));
            contentPanel.add(tbDParam, cc.xy(8, 13));

            //---- label19 ----
            label19.setText("\u0421\u0442\u0430\u0442\u0443\u0441");
            contentPanel.add(label19, cc.xy(2, 15));

            //---- cbFilterStatus ----
            cbFilterStatus.setModel(new DefaultComboBoxModel(new String[] {
                "\u0412 \u041f\u0420\u041e\u0418\u0417\u0412\u041e\u0414\u0421\u0422\u0412\u0415",
                "\u041f\u041e\u0421\u0422\u0410\u0412\u041b\u042f\u0415\u0422\u0421\u042f",
                "\u041d\u0415 \u041f\u041e\u0421\u0422\u0410\u0412\u041b\u042f\u0415\u0422\u0421\u042f"
            }));
            contentPanel.add(cbFilterStatus, cc.xy(4, 15));

            //---- label9 ----
            label9.setText("E");
            contentPanel.add(label9, cc.xy(6, 15));
            contentPanel.add(tbEParam, cc.xy(8, 15));

            //---- label15 ----
            label15.setText("\u0414\u043e\u043f. \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f");
            contentPanel.add(label15, cc.xy(2, 17));

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(tbAdditionalInfo);
            }
            contentPanel.add(scrollPane1, cc.xywh(4, 17, 1, 11));

            //---- label10 ----
            label10.setText("F");
            contentPanel.add(label10, cc.xy(6, 17));
            contentPanel.add(tbFParam, cc.xy(8, 17));

            //---- label11 ----
            label11.setText("G");
            contentPanel.add(label11, cc.xy(6, 19));
            contentPanel.add(tbGParam, cc.xy(8, 19));

            //---- label12 ----
            label12.setText("H");
            contentPanel.add(label12, cc.xy(6, 21));
            contentPanel.add(tbHParam, cc.xy(8, 21));

            //---- label13 ----
            label13.setText("PB");
            contentPanel.add(label13, cc.xy(6, 23));
            contentPanel.add(tbPbParam, cc.xy(8, 23));

            //---- label14 ----
            label14.setText("NR");
            contentPanel.add(label14, cc.xy(6, 25));
            contentPanel.add(tbNrParam, cc.xy(8, 25));

            //======== panel1 ========
            {
                panel1.setLayout(new FormLayout(
                    "default:grow, 2*($lcgap, $button)",
                    "default"));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        okButtonActionPerformed(e);
                    }
                });
                panel1.add(okButton, cc.xy(3, 1));

                //---- cancelButton ----
                cancelButton.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cancelButtonActionPerformed(e);
                    }
                });
                panel1.add(cancelButton, cc.xy(5, 1));
            }
            contentPanel.add(panel1, cc.xywh(2, 29, 7, 1));
        }
        contentPane.add(contentPanel, cc.xy(1, 1));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        cbFilterType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    SelectItem selectItem = (SelectItem) e.getItem();
                    FilterType ft = carsService.getFilterTypeByCode(selectItem.getId());
                    reReadFilterForms(ft.getCode());

                }
            }
        });

        cbFilterForm.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    adjustParameters();
                    adjustButtonsEnabled();
                }
            }
        });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Sasha aaa
    private JPanel contentPanel;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JLabel label1;
    private JComboBox cbFilterType;
    private JLabel label5;
    private JTextField tbAParam;
    private JLabel label2;
    private JComboBox cbFilterForm;
    private JLabel label6;
    private JTextField tbBParam;
    private JLabel label3;
    private JTextField tbName;
    private JLabel label7;
    private JTextField tbCParam;
    private JLabel label4;
    private JPanel panel2;
    private JTextField tbImage;
    private JButton btnUploadImage;
    private JLabel label8;
    private JTextField tbDParam;
    private JLabel label19;
    private JComboBox cbFilterStatus;
    private JLabel label9;
    private JTextField tbEParam;
    private JLabel label15;
    private JScrollPane scrollPane1;
    private JTextArea tbAdditionalInfo;
    private JLabel label10;
    private JTextField tbFParam;
    private JLabel label11;
    private JTextField tbGParam;
    private JLabel label12;
    private JTextField tbHParam;
    private JLabel label13;
    private JTextField tbPbParam;
    private JLabel label14;
    private JTextField tbNrParam;
    private JPanel panel1;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    private void adjustButtonsEnabled() {
        okButton.setEnabled(requiredFields());
        cancelButton.setEnabled(true);

        if (StringUtils.isBlank(tbName.getText())) {
            tbName.setBackground(UIUtils.COLOR_BAD);
        } else {
            tbName.setBackground(UIUtils.COLOR_GOOD);
        }

        if (filterFormsCombo.getValue() == null) {
            cbFilterForm.setBackground(UIUtils.COLOR_BAD);
        } else {
            cbFilterForm.setBackground(UIUtils.COLOR_GOOD);
        }

        if (filterTypesCombo.getValue() == null) {
            cbFilterType.setBackground(UIUtils.COLOR_BAD);
        } else {
            cbFilterType.setBackground(UIUtils.COLOR_GOOD);
        }
    }

    private boolean requiredFields() {
        return !StringUtils.isBlank(tbName.getText()) &&
                filterFormsCombo.getValue() != null &&
                filterTypesCombo.getValue() != null;
    }
}
