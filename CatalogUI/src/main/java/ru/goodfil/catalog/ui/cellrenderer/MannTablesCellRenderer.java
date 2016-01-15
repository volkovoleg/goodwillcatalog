package ru.goodfil.catalog.ui.cellrenderer;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import ru.goodfil.catalog.ui.adapters.BigMotorTableAdapter;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.domain.dict.*;
import ru.goodfil.catalog.ui.forms.FiltersPanel;
import ru.goodfil.catalog.utils.Assert;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс предначен для расскраски строк, в зависимости от источника данных
 *
 * @author Vit
 */
public class MannTablesCellRenderer extends DefaultTableCellRenderer {
    /**
     * Таблица с моторами
     */
    public static final String MOTOR = "Motor";
    /**
     * Дополнительная таблица с моторами
     */
    public static final String AMOTOR = "AMotor";
    /**
     * Таблица с фильтрами
     */
    public static final String FILTER = "Filter";
    /**
     * Таблица с оригинальными номерами
     */
    public static final String OE = "Oe";
    /**
     * Лист таблицы
     */
    private final String adapterName;
    /**
     * Темно-зеленый цвет
     */
    private static final Color GREEN = new Color(194, 255, 222);
    /**
     * Желтый цвет
     */
    private static final Color YELLOW = new Color(255,255,0);
    /**
     * Белый цвет
     */
    private static final Color WHITE = new Color(255, 255, 255);
    /**
     * Массив с основными цветами
     */
    private static final Map<Integer, Color> colors = new HashMap<Integer, Color>() {{
        put(1, GREEN);
        put(2, YELLOW);
        put(0, WHITE);
    }};

    /**
     * Конструктор, с указанием, какое поле загружается
     *
     * @param adapterName
     */
    public MannTablesCellRenderer(String adapterName) {
        Assert.notNull(adapterName);
        Assert.notBlank(adapterName);
        this.adapterName = adapterName;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int rowIndex = table.convertRowIndexToModel(row);
        if ((adapterName != null) && (!adapterName.equals(""))) {
            if (adapterName.equals(MOTOR)) {
                AbstractTableAdapter<Motor> adapter = (AbstractTableAdapter<Motor>) table.getModel();
                Motor motor = adapter.getRow(rowIndex);
                component.setForeground(Color.BLACK);
                component.setBackground(getColorByMannStatus(motor.getMannStatus(), isSelected));
            }
            if (adapterName.equals(AMOTOR)) {
                AbstractTableAdapter<BigMotorTableAdapter.MotorModel> adapter = (AbstractTableAdapter<BigMotorTableAdapter.MotorModel>) table.getModel();
                BigMotorTableAdapter.MotorModel motor = adapter.getRow(rowIndex);
                component.setForeground(Color.BLACK);
                component.setBackground(getColorByMannStatus(motor.getMannStatus(), isSelected));
            }
            if (adapterName.equals(FILTER)) {
                AbstractTableAdapter<Filter> adapter = (AbstractTableAdapter<Filter>) table.getModel();
                Filter filter = adapter.getRow(rowIndex);
                component.setForeground(Color.BLACK);
                component.setBackground(getColorByMannStatus(filter.getMannStatus(), isSelected));
            }
            if (adapterName.equals(OE)) {
                AbstractTableAdapter<FiltersPanel.OeModel> adapter = (AbstractTableAdapter<FiltersPanel.OeModel>) table.getModel();
                FiltersPanel.OeModel oe = adapter.getRow(rowIndex);
                component.setForeground(Color.BLACK);
                component.setBackground(getColorByMannStatus(oe.getMannStatus(), isSelected));
            }
        }
        return component;
    }

    /**
     * Возвращает цвет, в зависисмости от mannStatus и в ситуации нажатия на строку в таблице.
     *
     * @param mannStatus Загрузка из Манна
     * @param isSelected Выбрана ли в данный момент строка с данными
     * @return Цвет, с  учетом всех входных данных
     */
    private Color getColorByMannStatus(int mannStatus, boolean isSelected) {
        Assert.notNull(mannStatus,isSelected);
        Color background = Color.WHITE;
        if (colors.containsKey(mannStatus)) {
            background = colors.get(mannStatus);
            if (background != null) {
                if (isSelected) {
                    background = background.darker();
                }
            }
        }
        return background;
    }
}
