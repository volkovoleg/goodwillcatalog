package ru.goodfil.catalog.export.domain.fullexport;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: RowModel.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class RowModel {
    private String header;
    private String seria;
    private String motorId;
    private String model;
    private String engine;
    private String kw;
    private String hp;
    private String begdate;
    private String enddate;
    private final Map<String/*filterTypeCode*/, RowFiltersModel> filters = new HashMap<String, RowFiltersModel>();

    public boolean hasAtLeastOneFilter() {
        for (RowFiltersModel rowFiltersModel : filters.values()) {
            if (rowFiltersModel.getFilters().size() > 0) {
                return true;
            }
        }
        return false;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getBegdate() {
        return begdate;
    }

    public void setBegdate(String begdate) {
        this.begdate = begdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public Map<String, RowFiltersModel> getFilters() {
        return filters;
    }

    public String getMotorId() {
        return motorId;
    }

    public void setMotorId(String motorId) {
        this.motorId = motorId;
    }
}
