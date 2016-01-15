package ru.goodfil.catalog.ui.adapters;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import ru.goodfil.catalog.domain.Motor;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: MotorTableAdapter.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class MotorTableAdapter extends AbstractTableAdapter<Motor> {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static final int COL_MODEL = 0;
    public static final int COL_MOTOR = 1;
    public static final int COL_KW = 2;
    public static final int COL_HP = 3;
    public static final int COL_DATE_F = 4;
    public static final int COL_DATE_T = 5;

    public MotorTableAdapter(ListModel listModel) {
        super(listModel, new String[]{
                "Модель",   // 0
                "Мотор",    // 1
                "КВ",       // 2
                "ЛС",       // 3
                "С",        // 4
                "ПО"        // 5
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Motor motor = getRow(rowIndex);
        if (columnIndex == COL_MODEL) return motor.getName();
        if (columnIndex == COL_MOTOR) return motor.getEngine();
        if (columnIndex == COL_KW) return motor.getKw();
        if (columnIndex == COL_HP) return motor.getHp();
        if (columnIndex == COL_DATE_F) return date(motor.getDateF());
        if (columnIndex == COL_DATE_T) return date(motor.getDateT());
        return null;
    }

    private String date(Date date) {
        if (date == null) return "";
        else return sdf.format(date);
    }


}