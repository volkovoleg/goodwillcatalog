package ru.goodfil.catalog.ui.cellrenderer;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import ru.goodfil.catalog.domain.Filter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilterTableCellRenderer.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class FilterTableCellRenderer extends DefaultTableCellRenderer {
    private static final Color MEDIUM_TURQOISE = new Color(208, 242, 241);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color TAN = new Color(254, 254, 254);
    private static final Color PEACH_PUFF = new Color(255, 240, 227);

//    private static final Color MEDIUM_TURQOISE = new Color(72, 209, 204);
//    private static final Color WHITE = new Color(255, 255, 255);
//    private static final Color TAN = new Color(210, 180, 140);
//    private static final Color PEACH_PUFF = new Color(255, 218, 185);

    private static final Map<String, Color> colors = new HashMap<String, Color>() {{
        put("A", MEDIUM_TURQOISE);
        put("BP", WHITE);
        put("F", TAN);
        put("IR", WHITE);
        put("O", PEACH_PUFF);
        put("SP", WHITE);
        put("WAG", WHITE);
        put("WB", WHITE);
    }};

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int rowIndex = table.convertRowIndexToModel(row);
        AbstractTableAdapter<Filter> adapter = (AbstractTableAdapter<Filter>) table.getModel();

        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        component.setForeground(Color.BLACK);

        Filter filter = adapter.getRow(rowIndex);
        if (column == 3) {
            Color background = Color.LIGHT_GRAY;
            if (filter.getOnSite() != null && filter.getOnSite().equals(true)) {
                background = Color.YELLOW;
            }

            if (isSelected) {
                background = background.darker();
            }
            component.setBackground(background);
        } else {
            if (colors.containsKey(filter.getFilterTypeCode())) {
                Color background = colors.get(filter.getFilterTypeCode());
                if (background != null) {
                    if (isSelected) {
                        background = background.darker();
                    }
                    component.setBackground(background);
                }
            }
        }

        if (isSelected) {
            Font f = component.getFont().deriveFont(Font.BOLD);
            component.setFont(f);

        }

        return component;
    }
}
