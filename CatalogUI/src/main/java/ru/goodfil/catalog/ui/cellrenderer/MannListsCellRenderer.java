package ru.goodfil.catalog.ui.cellrenderer;

import com.jgoodies.binding.list.ArrayListModel;
import ru.goodfil.catalog.domain.*;
import ru.goodfil.catalog.utils.Assert;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс отрисовывает цвета из манна, для всех объектов типа JList
 *
 * @author Vit
 */
public class MannListsCellRenderer extends DefaultListCellRenderer {
    /**
     * Лист с производителями деталей
     */
    public static final String BRAND = "Brand";
    /**
     * Лист с оригинальными номерами
     */
    public static final String OE = "Oe";
    /**
     * Лист с производителями
     */
    public static final String MANUFACTOR = "Manufactor";
    /**
     * Лист с сериями
     */
    public static final String SERIA = "Seria";
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
    public MannListsCellRenderer(String adapterName) {
        Assert.notNull(adapterName);
        Assert.notBlank(adapterName);
        this.adapterName = adapterName;
    }

    /**
     * Отрисовщик вводимых изменений
     *
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (adapterName.equals(BRAND)) {
            ArrayListModel<Brand> adapter = (ArrayListModel<Brand>) list.getModel();
            Brand brand = (Brand) adapter.get(index);
            component.setForeground(Color.BLACK);
            component.setBackground(getColorByMannStatus(brand.getMannStatus(), isSelected));
            component.setFont(getFontByStandaloneStatus(brand.getStandaloneStatus()));
        }
        if (adapterName.equals(OE)) {
            ArrayListModel<Oe> adapter = (ArrayListModel<Oe>) list.getModel();
            Oe oe = (Oe) adapter.get(index);
            component.setForeground(Color.BLACK);
            component.setBackground(getColorByMannStatus(oe.getMannStatus(), isSelected));
        }
        if (adapterName.equals(MANUFACTOR)) {
            ArrayListModel<Manufactor> adapter = (ArrayListModel<Manufactor>) list.getModel();
            Manufactor manufactor = (Manufactor) adapter.get(index);
            component.setForeground(Color.BLACK);
            component.setBackground(getColorByMannStatus(manufactor.getMannStatus(), isSelected));
        }
        if (adapterName.equals(SERIA)) {
            ArrayListModel<Seria> adapter = (ArrayListModel<Seria>) list.getModel();
            Seria seria = (Seria) adapter.get(index);
            component.setForeground(Color.BLACK);
            component.setBackground(getColorByMannStatus(seria.getMannStatus(), isSelected));
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
        Assert.notNull(mannStatus);
        Color background = Color.WHITE;
        if (colors.containsKey(mannStatus)) {
            background = colors.get(mannStatus);
        }
        if (background != null) {
            if (isSelected) {
                background = background.darker();
            }
        }
        return background;
    }

    private Font getFontByStandaloneStatus(int standaloneStatus){
        Assert.notNull(standaloneStatus);
        Font font = new Font("Tahoma", Font.BOLD, 11);
        Font font2 = new Font("Tahoma", Font.PLAIN, 11);
        if(standaloneStatus==0){
            return font2;
        }
        if(standaloneStatus==1){
            return font;
        }
        return font2;
    }
}
