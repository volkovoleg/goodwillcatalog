package ru.goodfil.catalog.ui.swing;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationMessage;

import javax.swing.*;
import java.awt.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: MessagesRenderer.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class MessagesRenderer extends DefaultListCellRenderer {
    private static Icon infoIcon;
    private static Icon warningIcon;
    private static Icon errorIcon;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ValidationMessage message = (ValidationMessage) value;

        JLabel label = (JLabel) super.getListCellRendererComponent(list, message.formattedText(), index, isSelected, cellHasFocus);
        Icon icon = null;
        if (message.severity() == Severity.OK) icon = infoIcon;
        if (message.severity() == Severity.WARNING) icon = warningIcon;
        if (message.severity() == Severity.ERROR) icon = errorIcon;

        if (icon != null) {
            label.setIcon(icon);
        }

        return label;
    }

    public static Icon getInfoIcon() {
        return infoIcon;
    }

    public static void setInfoIcon(Icon infoIcon) {
        MessagesRenderer.infoIcon = infoIcon;
    }

    public static Icon getWarningIcon() {
        return warningIcon;
    }

    public static void setWarningIcon(Icon warningIcon) {
        MessagesRenderer.warningIcon = warningIcon;
    }

    public static Icon getErrorIcon() {
        return errorIcon;
    }

    public static void setErrorIcon(Icon errorIcon) {
        MessagesRenderer.errorIcon = errorIcon;
    }
}