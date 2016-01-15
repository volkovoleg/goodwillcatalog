package ru.goodfil.catalog.ui.adapters;

import com.jgoodies.binding.adapter.AbstractTableAdapter;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Адаптер для таблицы с колонками:
 *  - Производитель
 *  - Серия
 *  - Мотор
 *  - Движок
 *  - кВт
 *  - ЛС
 *  - С
 *  - ПО.
 *  Таблица расположена на второй вкладке "Изделие" главого окна.
 * @author sazonovkirill@gmail.com
 * @version $Id: BigMotorTableAdapter.java 105 2012-12-20 09:15:20Z chezxxx@gmail.com $
 */
public class BigMotorTableAdapter extends AbstractTableAdapter<BigMotorTableAdapter.MotorModel> {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public static final int COL_MANUFACTOR = 0;
    public static final int COL_SERIA = 1;
    public static final int COL_MOTOR = 2;
    public static final int COL_ENGINE = 3;
    public static final int COL_KW = 4;
    public static final int COL_HP = 5;
    public static final int COL_DATEF = 6;
    public static final int COL_DATET = 7;


    public BigMotorTableAdapter(ListModel listModel) {
        super(listModel, new String[]{
                "Производитель",        // 0
                "Серия",                // 1
                "Модель",               // 2
                "Мотор",                // 3
                "КВ",                   // 4
                "ЛС",                   // 5
                "С",                    // 6
                "ПО"                    // 7
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MotorModel motor = getRow(rowIndex);
        if (columnIndex == COL_MANUFACTOR) return motor.getManufactor();
        if (columnIndex == COL_SERIA) return motor.getSeria();
        if (columnIndex == COL_MOTOR) return motor.getName();
        if (columnIndex == COL_ENGINE) return motor.getEngine();
        if (columnIndex == COL_KW) return motor.getKw();
        if (columnIndex == COL_HP) return motor.getHp();
        if (columnIndex == COL_DATEF) return motor.getDateF();
        if (columnIndex == COL_DATET) return motor.getDateT();

        return null;
    }

    private String date(Date date) {
        if (date == null) return "";
        else return sdf.format(date);
    }

    /**
     * Строка таблицы "Моторы" на второй вкладке "Изделие" главного окна.
     * @author ksazonov
     */
    public static class MotorModel {
        private Long motorId;
        private Long seriaId;
        private Long manufactorId;

        private String manufactor;
        private String seria;

        private String name;
        private String engine;
        private String kw;
        private String hp;

        private String dateF;
        private String dateT;

        private int mannStatus;


        public int getMannStatus() {
            return mannStatus;
        }

        public void setMannStatus(int mannStatus) {
            this.mannStatus = mannStatus;
        }

        public Long getMotorId() {
            return motorId;
        }

        public void setMotorId(Long motorId) {
            this.motorId = motorId;
        }

        public Long getSeriaId() {
            return seriaId;
        }

        public void setSeriaId(Long seriaId) {
            this.seriaId = seriaId;
        }

        public Long getManufactorId() {
            return manufactorId;
        }

        public void setManufactorId(Long manufactorId) {
            this.manufactorId = manufactorId;
        }

        public String getManufactor() {
            return manufactor;
        }

        public void setManufactor(String manufactor) {
            this.manufactor = manufactor;
        }

        public String getSeria() {
            return seria;
        }

        public void setSeria(String seria) {
            this.seria = seria;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEngine() {
            return engine;
        }

        public void setEngine(String engine) {
            this.engine = engine;
        }

        public String getKw() {
            return kw;
        }

        public void setKw(String kw) {
            this.kw = kw;
        }

        public String getHp() {
            return hp;
        }

        public void setHp(String hp) {
            this.hp = hp;
        }

        public String getDateF() {
            return dateF;
        }

        public void setDateF(String dateF) {
            this.dateF = dateF;
        }

        public String getDateT() {
            return dateT;
        }

        public void setDateT(String dateT) {
            this.dateT = dateT;
        }
    }
}