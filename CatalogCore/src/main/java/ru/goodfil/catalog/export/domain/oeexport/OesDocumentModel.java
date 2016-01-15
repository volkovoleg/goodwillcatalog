package ru.goodfil.catalog.export.domain.oeexport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: OesDocumentModel.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class OesDocumentModel {
    private final List<RowModel> rows = new ArrayList<RowModel>();

    public List<RowModel> getRows() {
        return rows;
    }
}
