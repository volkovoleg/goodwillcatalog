package ru.goodfil.catalog.adapters.core;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFile.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public interface TabFile {
    String[] getColumns();

    int getColumnsCount();

    int getRowsCount();

    String getValueAt(int row, int column);

    String[][] getData();
}
