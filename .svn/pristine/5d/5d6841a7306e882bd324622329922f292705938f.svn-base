package ru.goodfil.catalog.adapters.core.impl;

import ru.goodfil.catalog.adapters.core.TabFile;
import ru.goodfil.catalog.utils.Assert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFileImpl.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public class TabFileImpl implements TabFile {
    private Set<String> columns = new LinkedHashSet<String>();
    private List<String[]> rows = new ArrayList<String[]>();

    @Override
    public String[] getColumns() {
        String[] result = new String[columns.size()];
        columns.toArray(result);
        return result;
    }

    @Override
    public int getColumnsCount() {
        return columns.size();
    }

    @Override
    public int getRowsCount() {
        return rows.size();
    }

    @Override
    public String getValueAt(int row, int column) {
        Assert.isTrue(row >= 0 && row < rows.size());
        Assert.isTrue(column >= 0 && column < columns.size());

        return rows.get(row)[column];
    }

    @Override
    public String[][] getData() {
        String[][] result = new String[rows.size()][];
        rows.toArray(result);
        return result;
    }

    public void addColumn(String s) {
        Assert.notBlank(s);
        columns.add(s);
    }

    public void addRow(String[] arr) {
        Assert.notNull(arr);
        Assert.isTrue(arr.length == columns.size());
        rows.add(arr);
    }
}
