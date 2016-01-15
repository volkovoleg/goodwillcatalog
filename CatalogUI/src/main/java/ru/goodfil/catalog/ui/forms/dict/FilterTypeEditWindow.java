/*
 * Created by JFormDesigner on Sun Dec 11 10:42:55 MSK 2011
 */

package ru.goodfil.catalog.ui.forms.dict;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.dict.FilterType;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.CatalogJDialog;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.EditMode;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Кирилл Сазонов
 */
@Managed
public class FilterTypeEditWindow extends CatalogJDialog {
    /**
     * Возвращает форму для создания нового типа изделия.
     */
    public static FilterTypeEditWindow createFilterType() {
        CarsService carsService = Services.getCarsService();

        FilterTypeEditWindow filterTypeEditWindow = new FilterTypeEditWindow();
        filterTypeEditWindow.editMode = EditMode.CREATE;
        filterTypeEditWindow.prepareEditMode();
        filterTypeEditWindow.allFilterTypes = carsService.getFilterTypes();
        return filterTypeEditWindow;
    }

    /**
     * Возвращает форму для редактирования типа изделия.
     */
    public static FilterTypeEditWindow editFilterType(@NotNull final FilterType filterType) {
        CarsService carsService = Services.getCarsService();

        FilterTypeEditWindow filterTypeEditWindow = new FilterTypeEditWindow();
        filterTypeEditWindow.filterType = filterType;
        filterTypeEditWindow.editMode = EditMode.EDIT;
        filterTypeEditWindow.prepareEditMode();
        filterTypeEditWindow.allFilterTypes = carsService.getFilterTypes();

        filterTypeEditWindow.tbCode.setText(filterType.getCode());
        filterTypeEditWindow.tbName.setText(filterType.getName());

        return filterTypeEditWindow;
    }

    /**
     * Возвращает форму для просмотра информации о типе изделия.
     */
    public static FilterTypeEditWindow viewFilterType(@NotNull final FilterType filterType) {
        CarsService carsService = Services.getCarsService();

        FilterTypeEditWindow filterTypeEditWindow = new FilterTypeEditWindow();
        filterTypeEditWindow.filterType = filterType;
        filterTypeEditWindow.editMode = EditMode.VIEW;
        filterTypeEditWindow.prepareEditMode();
        filterTypeEditWindow.allFilterTypes = carsService.getFilterTypes();

        filterTypeEditWindow.tbCode.setText(filterType.getCode());
        filterTypeEditWindow.tbName.setText(filterType.getName());

        return filterTypeEditWindow;
    }

    private FilterType filterType = FilterType.create();

    @NotNull
    private List<FilterType> allFilterTypes;

    private void prepareEditMode() {
        if (editMode == EditMode.CREATE) {
            setTitle("Добавление нового типа изделия");
            lHeader.setText("Вы собираетесь добавить новый тип изделия.");
            lDescription.setText("Для добавления нового типа изделия необходимо указать наименование и код изделия, отличные от уже существующих. Код должен быть одной заглавной буквой латинского алфавита.");

            tbName.setEnabled(true);
            tbCode.setEnabled(true);

            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        }
        if (editMode == EditMode.EDIT) {
            setTitle("Переименование типа изделия");
            lHeader.setText("Вы собираетесь переименовать тип изделия.");
            lDescription.setText("Необходимо указать новое наименование изделия. При этом оно должно отличаться от уже существующих.");

            tbName.setEnabled(true);
            tbCode.setEnabled(false);

            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        }
        if (editMode == EditMode.VIEW) {
            setTitle("Просмотр типа издения");
            lHeader.setText("Вы можете только просмотреть код и наименование изделия.");
            lDescription.setText("");

            tbName.setEnabled(false);
            tbCode.setEnabled(false);

            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
        }
        if (editMode == EditMode.UNDEFINED) {
            throw new IllegalStateException("Window " + FilterTypeEditWindow.class + " requires editMode setup!");
        }
    }

    private FilterTypeEditWindow() {
        super();
        initComponents();
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        dispose(DialogResult.CANCEL);
    }

    private void btnSaveActionPerformed(ActionEvent e) {
        validateFormFields();

        if (!hasErrors()) {
            filterType.setName(tbName.getText().trim());
            filterType.setCode(tbCode.getText().trim());

            dispose(DialogResult.OK);
        }
    }

    private void validateFormFields() {
        clearFormErrors();
        rule(tbName, notEmpty("Поле должно быть заполнено"));
        rule(tbCode, notEmpty("Поле должно быть заполнено"));

        if (editMode == EditMode.CREATE) {
            final Set<String> codes = new HashSet<String>();
            for (FilterType ft : allFilterTypes) {
                codes.add(ft.getCode());
            }
            rule(tbCode, unqie("Изделие с таким кодом уже существует", codes));
        }
        if (editMode == EditMode.EDIT) {
            final Set<String> codes = new HashSet<String>();
            for (FilterType ft : allFilterTypes) {
                if (!ft.getId().equals(filterType.getId())) {
                    codes.add(ft.getCode());
                }
            }
            rule(tbCode, unqie("Изделие с таким кодом уже существует", codes));
        }
    }

    public FilterType getFilterType() {
        return filterType;
    }

    private void tbNameKeyTyped(KeyEvent e) {
        validateFormFields();
    }

    private void tbCodeKeyTyped(KeyEvent e) {
        validateFormFields();
    }

    private void tbCodeFocus(FocusEvent e) {
        validateFormFields();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel2 = new JPanel();
        lHeader = new JLabel();
        scrollPane1 = new JScrollPane();
        lDescription = new JTextPane();
        lName = new JLabel();
        tbName = new JTextField();
        lCode = new JLabel();
        panel1 = new JPanel();
        tbCode = new JTextField();
        panel3 = new JPanel();
        btnSave = new JButton();
        btnCancel = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0442\u0438\u043f \u0444\u0438\u043b\u044c\u0442\u0440\u0430");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "default, $lcgap, default:grow",
                "3*(default, $lgap), default:grow, $lgap, default"));

        //======== panel2 ========
        {
            panel2.setBackground(Color.white);
            panel2.setBorder(new EtchedBorder());
            panel2.setLayout(new FormLayout(
                    "default:grow",
                    "default, $lgap, fill:32dlu"));

            //---- lHeader ----
            lHeader.setText("\u0412\u044b \u0441\u043e\u0431\u0438\u0440\u0430\u0435\u0442\u0435\u0441\u044c \u0434\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043d\u043e\u0432\u044b\u0439 \u0442\u0438\u043f \u0438\u0437\u0434\u0435\u043b\u0438\u044f.");
            panel2.add(lHeader, cc.xy(1, 1));

            //======== scrollPane1 ========
            {
                scrollPane1.setEnabled(false);
                scrollPane1.setBorder(null);

                //---- lDescription ----
                lDescription.setText("\u0414\u043b\u044f \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u0438\u044f \u043d\u043e\u0432\u043e\u0433\u043e \u0442\u0438\u043f\u0430 \u0438\u0437\u0434\u0435\u043b\u0438\u044f \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u044c \u043d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435 \u0438 \u043a\u043e\u0434. \u041e\u043d\u0438 \u0434\u043e\u043b\u0436\u043d\u044b \u043e\u0442\u043b\u0438\u0447\u0430\u0442\u044c\u0441\u044f \u043e\u0442 \u0434\u0440\u0443\u0433\u0438\u0445 \u0442\u0438\u043f\u043e\u0432 \u0438\u0437\u0434\u0435\u043b\u0438\u0439. \u041a\u043e\u0434 \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u043e\u0434\u043d\u043e\u0439 \u0431\u0443\u043a\u0432\u043e\u0439 \u043b\u0430\u0442\u0438\u043d\u0441\u043a\u043e\u0433\u043e \u0430\u043b\u0444\u0430\u0432\u0438\u0442\u0430.");
                lDescription.setBorder(null);
                lDescription.setEditable(false);
                scrollPane1.setViewportView(lDescription);
            }
            panel2.add(scrollPane1, cc.xy(1, 3));
        }
        contentPane.add(panel2, cc.xywh(1, 1, 3, 1));

        //---- lName ----
        lName.setText("\u041d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435");
        lName.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(lName, cc.xy(1, 3));

        //---- tbName ----
        tbName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                tbNameKeyTyped(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                tbNameKeyTyped(e);
            }
        });
        contentPane.add(tbName, cc.xy(3, 3));

        //---- lCode ----
        lCode.setText("\u041a\u043e\u0434");
        lCode.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(lCode, cc.xy(1, 5));

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                    "25dlu, $lcgap, default",
                    "default"));

            //---- tbCode ----
            tbCode.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    tbCodeKeyTyped(e);
                }

                @Override
                public void keyTyped(KeyEvent e) {
                    tbCodeKeyTyped(e);
                }
            });
            tbCode.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    tbCodeFocus(e);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    tbCodeFocus(e);
                }
            });
            panel1.add(tbCode, cc.xy(1, 1));
        }
        contentPane.add(panel1, cc.xy(3, 5));

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
        contentPane.add(panel3, cc.xy(3, 9));
        setSize(400, 210);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel2;
    private JLabel lHeader;
    private JScrollPane scrollPane1;
    private JTextPane lDescription;
    private JLabel lName;
    private JTextField tbName;
    private JLabel lCode;
    private JPanel panel1;
    private JTextField tbCode;
    private JPanel panel3;
    private JButton btnSave;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
