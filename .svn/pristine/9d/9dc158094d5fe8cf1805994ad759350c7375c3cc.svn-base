package ru.goodfil.catalog.ui.swing;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.SimpleValidationMessage;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.ui.forms.NameWindow;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: UIUtils.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class UIUtils {
    private UIUtils() {
    }

    public static boolean parsable(String s, SimpleDateFormat sdf) {
        try {
            sdf.parse(s);
            return true;
        } catch (ParseException pe) {
            return false;
        }
    }

    public static void hint(@NotNull @NotBlank final String text) {

    }

    public static void warning(@NotNull @NotBlank final String text) {
        JOptionPane.showMessageDialog(null, text, "Внимание!", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(@NotNull @NotBlank final String text) {
        JOptionPane.showMessageDialog(null, text, "Внимание!", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(String text) {
        JOptionPane.showMessageDialog(null, text, "Внимание!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String askName() {
//      String s = JOptionPane.showInputDialog(this, "Наименование", "Тип транспортного средства", JOptionPane.WARNING_MESSAGE);
        NameWindow nameWindow = new NameWindow();
        nameWindow.setVisible(true);
        return nameWindow.getName();
    }

    public static String askName(String initialName) {
        NameWindow nameWindow = new NameWindow();
        nameWindow.setInitialName(initialName);
        nameWindow.setVisible(true);
        return nameWindow.getName();
    }

    public static boolean askContinue(String question) {
        int result = JOptionPane.showConfirmDialog(null, question, "Внимание", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) return true;
        else return false;
    }

    public static boolean askDelete() {
        int result = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите удалить выделенный объект?", "Внимание!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public static ValidationResult validateNotNull(JComboBox comboBox, String text) {
        ValidationResult result = new ValidationResult();

        if (comboBox != null) {
            if (comboBox.getSelectedIndex() == -1) {
                comboBox.setBackground(Color.PINK);

                ValidationMessage message = new SimpleValidationMessage(text, Severity.ERROR);
                result.add(message);
            } else {
                comboBox.setBackground(Color.WHITE);
            }
        }

        return result;
    }

    public static ValidationResult validateNotBlank(JTextComponent textBox, String text) {
        ValidationResult result = new ValidationResult();

        if (textBox != null) {
            if (StringUtils.isBlank(textBox.getText())) {
                textBox.setBackground(Color.PINK);

                ValidationMessage message = new SimpleValidationMessage(text, Severity.ERROR);
                result.add(message);
            } else {
                textBox.setBackground(Color.WHITE);
            }
        }

        return result;
    }

    public static boolean askQuit() {
        int result = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите завершить работу с программой?", "Внимание!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    public static final Color COLOR_BAD = new Color(255, 219, 219);
    public static final Color COLOR_GOOD = new Color(219, 255, 222);
}
