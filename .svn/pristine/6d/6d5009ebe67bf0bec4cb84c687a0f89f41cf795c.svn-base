/*
 * Created by JFormDesigner on Sun Dec 11 12:24:38 MSK 2011
 */

package ru.goodfil.catalog.ui.forms.dict;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.io.FileUtils;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.PropertiesService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.EditMode;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.utils.Assert;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Кирилл Сазонов
 */
@Managed
public class FilterFormEditWindow extends CatalogJDialog {
    private CarsService carsService = Services.getCarsService();

    public static FilterFormEditWindow createFilterForm(@NotNull @NotBlank final String filterTypeCode) {
        CarsService cs = Services.getCarsService();

        FilterFormEditWindow ffew = new FilterFormEditWindow();
        ffew.editMode = EditMode.CREATE;
        ffew.filterForm = FilterForm.create(filterTypeCode);
        ffew.filterType = cs.getFilterTypeByCode(filterTypeCode);
        ffew.prepareEditMode();

        ffew.lFilterTypeName.setText(ffew.filterType.getName());

        return ffew;
    }

    public static FilterFormEditWindow editFilterForm(@NotNull final FilterForm filterForm) {
        CarsService cs = Services.getCarsService();

        FilterFormEditWindow ffew = new FilterFormEditWindow();
        ffew.editMode = EditMode.EDIT;
        ffew.filterForm = filterForm;
        ffew.filterType = cs.getFilterTypeByCode(filterForm.getFilterTypeCode());
        ffew.prepareEditMode();

        ffew.lFilterTypeName.setText(ffew.filterType.getName());
        ffew.tbName.setText(filterForm.getName());
        ffew.cbA.setSelected(filterForm.getaParam());
        ffew.cbB.setSelected(filterForm.getbParam());
        ffew.cbC.setSelected(filterForm.getcParam());
        ffew.cbD.setSelected(filterForm.getdParam());
        ffew.cbE.setSelected(filterForm.geteParam());
        ffew.cbF.setSelected(filterForm.getfParam());
        ffew.cbG.setSelected(filterForm.getgParam());
        ffew.cbH.setSelected(filterForm.gethParam());
        ffew.cbPb.setSelected(filterForm.getBpParam());
        ffew.cbNr.setSelected(filterForm.getNrParam());
        ffew.tbImage.setText(filterForm.getImage());
        return ffew;
    }

    private void prepareEditMode() {
        if (editMode == EditMode.CREATE) {
            setTitle("Добавление новой формы изделия");
            lHeader.setText("Вы собираетесь добавить новую форму изделия");
            lDescription.setText("Для добавления новой формы изделия необходимо указать название формы изделия, отличную от уже существующих. Кроме того необходимо указать, какие размеры должны задаваться для данной формы изделия (A, B, C и т.д.)");

            tbName.setEnabled(true);
            cbA.setEnabled(true);
            cbB.setEnabled(true);
            cbC.setEnabled(true);
            cbD.setEnabled(true);
            cbE.setEnabled(true);
            cbF.setEnabled(true);
            cbG.setEnabled(true);
            cbH.setEnabled(true);
            cbPb.setEnabled(true);
            cbNr.setEnabled(true);
            tbImage.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        } else if (editMode == EditMode.EDIT) {
            setTitle("Изменение новой формы изделия");
            lHeader.setText("Вы собираетесь изменить данные о форме изделия");
            lDescription.setText("Вы можете изменить наименование (но оно должно быть отлично от уже существуюищх). Кроме того, Вы можете указать рамеры, которые необходимы для данной формы изделия (A, B, C и т.д.)");

            tbName.setEnabled(true);
            cbA.setEnabled(true);
            cbB.setEnabled(true);
            cbC.setEnabled(true);
            cbD.setEnabled(true);
            cbE.setEnabled(true);
            cbF.setEnabled(true);
            cbG.setEnabled(true);
            cbH.setEnabled(true);
            cbPb.setEnabled(true);
            cbNr.setEnabled(true);
            tbImage.setEnabled(true);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        } else {
            throw new IllegalStateException("Form " + FilterFormEditWindow.class + " requires either CREATE or EDIT editMode");
        }
    }

    public void validateFormFields() {
        clearFormErrors();

        if (editMode == EditMode.CREATE) {
            Set<String> names = new HashSet<String>();
            List<FilterForm> filterForms = carsService.getFilterFormsByFilterTypeCode(filterForm.getFilterTypeCode());
            for (FilterForm ff : filterForms) {
                names.add(ff.getName());
            }

            rule(tbName,
                    notEmpty("Поле должно быть заполнено"),
                    unqie("Уже существует форма изделия с таким наименованием", names));
        }
        if (editMode == EditMode.EDIT) {
            Set<String> names = new HashSet<String>();
            List<FilterForm> filterForms = carsService.getFilterFormsByFilterTypeCode(filterForm.getFilterTypeCode());
            for (FilterForm ff : filterForms) {
                if (!ff.getId().equals(filterForm.getId())) {
                    names.add(ff.getName());
                }
            }

            rule(tbName,
                    notEmpty("Поле должно быть заполнено"),
                    unqie("Уже существует форма изделия с таким наименованием", names));
        }
    }

    @NotNull
    private FilterForm filterForm;

    @NotNull
    private FilterType filterType;

    private FilterFormEditWindow() {
        super();
        initComponents();
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        dispose(DialogResult.CANCEL);
    }

    private void btnSaveActionPerformed(ActionEvent e) {
        validateFormFields();

        if (!hasErrors()) {
            filterForm.setName(tbName.getText().trim());
            filterForm.setaParam(cbA.isSelected());
            filterForm.setbParam(cbB.isSelected());
            filterForm.setcParam(cbC.isSelected());
            filterForm.setdParam(cbD.isSelected());
            filterForm.seteParam(cbE.isSelected());
            filterForm.setfParam(cbF.isSelected());
            filterForm.setgParam(cbG.isSelected());
            filterForm.sethParam(cbH.isSelected());
            filterForm.setBpParam(cbPb.isSelected());
            filterForm.setNrParam(cbNr.isSelected());
            filterForm.setImage(tbImage.getText());
            dispose(DialogResult.OK);
        }
    }


    public FilterForm getFilterForm() {
        return filterForm;
    }

    private void btnUploadImageActionPerformed(ActionEvent e) {
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
                } catch (IOException exception) {
                    UIUtils.error("Не удалось скопировать изображение. Обратитесь к Администратору.");
                }
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel1 = new JPanel();
        lHeader = new JLabel();
        scrollPane1 = new JScrollPane();
        lDescription = new JTextPane();
        label13 = new JLabel();
        lFilterTypeName = new JLabel();
        label1 = new JLabel();
        tbName = new JTextField();
        label2 = new JLabel();
        panel2 = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        cbA = new JCheckBox();
        cbB = new JCheckBox();
        cbC = new JCheckBox();
        cbD = new JCheckBox();
        cbE = new JCheckBox();
        cbF = new JCheckBox();
        cbG = new JCheckBox();
        cbH = new JCheckBox();
        cbPb = new JCheckBox();
        cbNr = new JCheckBox();
        label14 = new JLabel();
        panel3 = new JPanel();
        btnSave = new JButton();
        btnCancel = new JButton();
        panel4 = new JPanel();
        tbImage = new JTextField();
        btnUploadImage = new JButton();
        textField1 = new JTextField();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default, $rgap, default:grow",
            "51dlu, 3*($lgap, default), $lgap, 22dlu:grow, 2*($lgap, default)"));

        //======== panel1 ========
        {
            panel1.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            panel1.setBackground(Color.white);
            panel1.setLayout(new FormLayout(
                "default:grow",
                "default, $lgap, fill:34dlu"));

            //---- lHeader ----
            lHeader.setText("text");
            panel1.add(lHeader, cc.xy(1, 1));

            //======== scrollPane1 ========
            {
                scrollPane1.setBorder(null);

                //---- lDescription ----
                lDescription.setBorder(null);
                scrollPane1.setViewportView(lDescription);
            }
            panel1.add(scrollPane1, cc.xy(1, 3));
        }
        contentPane.add(panel1, cc.xywh(1, 1, 3, 1));

        //---- label13 ----
        label13.setText("\u0422\u0438\u043f \u0438\u0437\u0434\u0435\u043b\u0438\u044f");
        label13.setVerticalAlignment(SwingConstants.TOP);
        label13.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label13, cc.xy(1, 3));

        //---- lFilterTypeName ----
        lFilterTypeName.setFont(lFilterTypeName.getFont().deriveFont(lFilterTypeName.getFont().getStyle() | Font.BOLD));
        contentPane.add(lFilterTypeName, cc.xy(3, 3));

        //---- label1 ----
        label1.setText("\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435");
        label1.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label1, cc.xy(1, 5));
        contentPane.add(tbName, cc.xy(3, 5));

        //---- label2 ----
        label2.setText("\u041d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u044b\u0435 \u0440\u0430\u0437\u043c\u0435\u0440\u044b");
        label2.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label2, cc.xy(1, 7));

        //======== panel2 ========
        {
            panel2.setLayout(new FormLayout(
                "9*(default:grow, $lcgap), default:grow",
                "default, $lgap, default"));

            //---- label3 ----
            label3.setText("A");
            label3.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label3, cc.xy(1, 1));

            //---- label4 ----
            label4.setText("B");
            label4.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label4, cc.xy(3, 1));

            //---- label5 ----
            label5.setText("C");
            label5.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label5, cc.xy(5, 1));

            //---- label6 ----
            label6.setText("D");
            label6.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label6, cc.xy(7, 1));

            //---- label7 ----
            label7.setText("E");
            label7.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label7, cc.xy(9, 1));

            //---- label8 ----
            label8.setText("F");
            label8.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label8, cc.xy(11, 1));

            //---- label9 ----
            label9.setText("G");
            label9.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label9, cc.xy(13, 1));

            //---- label10 ----
            label10.setText("H");
            label10.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label10, cc.xy(15, 1));

            //---- label11 ----
            label11.setText("PB");
            label11.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label11, cc.xy(17, 1));

            //---- label12 ----
            label12.setText("NR");
            label12.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(label12, cc.xy(19, 1));

            //---- cbA ----
            cbA.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbA, cc.xy(1, 3));

            //---- cbB ----
            cbB.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbB, cc.xy(3, 3));

            //---- cbC ----
            cbC.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbC, cc.xy(5, 3));

            //---- cbD ----
            cbD.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbD, cc.xy(7, 3));

            //---- cbE ----
            cbE.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbE, cc.xy(9, 3));

            //---- cbF ----
            cbF.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbF, cc.xy(11, 3));

            //---- cbG ----
            cbG.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbG, cc.xy(13, 3));

            //---- cbH ----
            cbH.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbH, cc.xy(15, 3));

            //---- cbPb ----
            cbPb.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbPb, cc.xy(17, 3));

            //---- cbNr ----
            cbNr.setHorizontalAlignment(SwingConstants.CENTER);
            panel2.add(cbNr, cc.xy(19, 3));
        }
        contentPane.add(panel2, cc.xy(3, 7));

        //---- label14 ----
        label14.setText("\u0418\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0444\u043e\u0440\u043c\u044b");
        label14.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(label14, cc.xy(1, 9));

        //======== panel3 ========
        {
            panel3.setLayout(new FormLayout(
                "default:grow, 2*($lcgap, default)",
                "default"));

            //---- btnSave ----
            btnSave.setText("\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c");
            btnSave.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/save.png")));
            btnSave.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnSaveActionPerformed(e);
                }
            });
            panel3.add(btnSave, cc.xy(3, 1));

            //---- btnCancel ----
            btnCancel.setText("\u041e\u0442\u043c\u0435\u043d\u0430");
            btnCancel.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/cancel.png")));
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnCancelActionPerformed(e);
                }
            });
            panel3.add(btnCancel, cc.xy(5, 1));
        }
        contentPane.add(panel3, cc.xywh(1, 11, 3, 1));

        //======== panel4 ========
        {
            panel4.setLayout(new FormLayout(
                "default:grow, $lcgap, default",
                "default"));
            panel4.add(tbImage, cc.xy(1, 1));

            //---- btnUploadImage ----
            btnUploadImage.setText("...");
            btnUploadImage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnUploadImageActionPerformed(e);
                }
            });
            panel4.add(btnUploadImage, cc.xy(3, 1));
        }
        contentPane.add(panel4, cc.xy(3, 9));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel1;
    private JLabel lHeader;
    private JScrollPane scrollPane1;
    private JTextPane lDescription;
    private JLabel label13;
    private JLabel lFilterTypeName;
    private JLabel label1;
    private JTextField tbName;
    private JLabel label2;
    private JPanel panel2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JLabel label11;
    private JLabel label12;
    private JCheckBox cbA;
    private JCheckBox cbB;
    private JCheckBox cbC;
    private JCheckBox cbD;
    private JCheckBox cbE;
    private JCheckBox cbF;
    private JCheckBox cbG;
    private JCheckBox cbH;
    private JCheckBox cbPb;
    private JCheckBox cbNr;
    private JLabel label14;
    private JPanel panel3;
    private JButton btnSave;
    private JButton btnCancel;
    private JPanel panel4;
    private JTextField tbImage;
    private JButton btnUploadImage;
    private JTextField textField1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
