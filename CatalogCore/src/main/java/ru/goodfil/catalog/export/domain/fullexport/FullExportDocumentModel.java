package ru.goodfil.catalog.export.domain.fullexport;

import org.apache.commons.lang.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FullExportDocumentModel.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class FullExportDocumentModel {
    private String date;

    private final List<RowModel> rows = new ArrayList<RowModel>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RowModel> getRows() {
        return rows;
    }

    public void toStdout() {
        System.out.println("Date: " + date);

        for (RowModel rowModel : rows) {
            if (!StringUtils.isBlank(rowModel.getHeader())) {
                System.out.println(rowModel.getHeader());
            } else {
                System.out.println(String.format("%s |%s |%s", rowModel.getSeria(),
                        rowModel.getModel(),
                        rowModel.getEngine()));
                for (String filterTypeCode : rowModel.getFilters().keySet()) {
                    Set<String> filters = rowModel.getFilters().get(filterTypeCode).getFilters();
                    System.out.println("    " + filterTypeCode + " " + StringUtils.join(filters, ","));
                }
            }
        }
    }

    public void toFile(String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        fw.write("Date: " + date + "\n");

        for (RowModel rowModel : rows) {
            if (!StringUtils.isBlank(rowModel.getHeader())) {
                fw.write(rowModel.getHeader() + "\n");
            } else {
                fw.write(String.format("%s |%s |%s \n", rowModel.getSeria(),
                        rowModel.getModel(),
                        rowModel.getEngine()));
                for (String filterTypeCode : rowModel.getFilters().keySet()) {
                    Set<String> filters = rowModel.getFilters().get(filterTypeCode).getFilters();
                    fw.write("    " + filterTypeCode + " " + StringUtils.join(filters, ",") + "\n");
                }
            }
        }
    }
}
