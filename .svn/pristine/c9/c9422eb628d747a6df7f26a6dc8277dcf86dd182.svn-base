package ru.goodfil.catalog.ui.swing.renderers;

import ru.goodfil.catalog.utils.Assert;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: NiceTableCellRenderer.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class NiceTableCellRenderer extends DefaultTableCellRenderer {
    private boolean isBold;
    private boolean isItalic;
    private String fontName;

    private Color foregroundColor;
    private Color backgroundColor;

    private Color headerForegroundColor;
    private Color headerBackgroundColor;

    private boolean isHeaderBold;
    private boolean isHeaderItalic;
    private boolean headerFontName;

    private NiceTableCellRenderer() {

    }

    public static NiceTableCellRenderer bold() {
        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setBold(true);
        return result;
    }

    public static NiceTableCellRenderer bold(Color foreground) {
        Assert.notNull(foreground);

        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setBold(true);
        result.setForegroundColor(foreground);
        return result;
    }

    public static NiceTableCellRenderer bold(Color foreground, Color background) {
        Assert.notNull(foreground, background);

        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setBold(true);
        result.setForegroundColor(foreground);
        result.setForegroundColor(background);
        return result;
    }

    public static NiceTableCellRenderer italic() {
        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setItalic(true);
        return result;
    }

    public static NiceTableCellRenderer italic(Color foreground) {
        Assert.notNull(foreground);

        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setItalic(true);
        result.setForegroundColor(foreground);
        return result;
    }

    public static NiceTableCellRenderer italic(Color foreground, Color background) {
        Assert.notNull(foreground, background);

        NiceTableCellRenderer result = new NiceTableCellRenderer();
        result.setItalic(true);
        result.setForegroundColor(foreground);
        result.setForegroundColor(background);
        return result;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row == -1) {
            if (headerBackgroundColor != null) result.setBackground(headerBackgroundColor);
            if (headerForegroundColor != null) result.setForeground(headerForegroundColor);

            if (result instanceof JLabel) {
                JLabel label = (JLabel) result;
                Font font = label.getFont();
                if (isHeaderBold) font = font.deriveFont(Font.BOLD);
                if (isHeaderItalic) font = font.deriveFont(Font.ITALIC);
                label.setFont(font);
            }
        } else {
            if (backgroundColor != null) result.setBackground(backgroundColor);
            if (foregroundColor != null) result.setForeground(foregroundColor);

            if (result instanceof JLabel) {
                JLabel label = (JLabel) result;
                Font font = label.getFont();
                if (isBold) font = font.deriveFont(Font.BOLD);
                if (isItalic) font = font.deriveFont(Font.ITALIC);
                label.setFont(font);
            }
        }

        return result;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getHeaderForegroundColor() {
        return headerForegroundColor;
    }

    public void setHeaderForegroundColor(Color headerForegroundColor) {
        this.headerForegroundColor = headerForegroundColor;
    }

    public Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(Color headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public boolean isHeaderBold() {
        return isHeaderBold;
    }

    public void setHeaderBold(boolean headerBold) {
        isHeaderBold = headerBold;
    }

    public boolean isHeaderItalic() {
        return isHeaderItalic;
    }

    public void setHeaderItalic(boolean headerItalic) {
        isHeaderItalic = headerItalic;
    }

    public boolean isHeaderFontName() {
        return headerFontName;
    }

    public void setHeaderFontName(boolean headerFontName) {
        this.headerFontName = headerFontName;
    }
}
