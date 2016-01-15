package ru.goodfil.catalog.export.domain.oeexport;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: RowModel.java 130 2013-03-14 13:05:40Z chezxxx@gmail.com $
 */
public class RowModel {
    private String goodwillCode;
    private String externalCode;
    private String brandTitle;
    private boolean emptyRow = false;

    public RowModel() {
    }
    
    public static RowModel emptyRow() {
        RowModel row = new RowModel();
        row.emptyRow = true;
        return row;
    }
    
    public static RowModel sectionHeader(String brandTitle) {
        RowModel row = new RowModel();
        row.brandTitle = brandTitle;
        return row;
    }
    
    public static RowModel row(String goodwillCode, String externalCode) {
        RowModel row = new RowModel();
        row.goodwillCode = goodwillCode;
        row.externalCode = externalCode;
        return row;
    }
    
    public static RowModel simpleRow(String goodwillCode, String externalCode, String brandTitle){
        RowModel row = new RowModel();
        row.goodwillCode = goodwillCode;
        row.externalCode = externalCode;
        row.brandTitle=brandTitle;
        return row;  
    }

    public String getGoodwillCode() {
        return goodwillCode;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public String getBrandTitle() {
        return brandTitle;
    }

    public boolean isEmptyRow() {
        return emptyRow;
    }
}
