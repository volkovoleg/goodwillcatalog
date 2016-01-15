package ru.goodfil.catalog.adapters.core.impl;

import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.adapters.core.TabFile;
import ru.goodfil.catalog.adapters.core.TabFileReader;
import ru.goodfil.catalog.adapters.core.TabFileRowFilter;
import ru.goodfil.catalog.annotations.Managed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFileReaderImpl.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
@Managed
public class TabFileReaderImpl implements TabFileReader {
    private final String tabFileName;
    private String columnDelimeter = "\t";

    public TabFileReaderImpl(@NotBlank String tabFileName) {
        this.tabFileName = tabFileName;
    }

    @Override
    public TabFile read() throws IOException {
        return read(null);
    }

    @Override
    public TabFile read(TabFileRowFilter filter) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(tabFileName));
        TabFileImpl tabFile = new TabFileImpl();

        try {
            int lineNumber = 0;
            String line = reader.readLine();

            while (line != null) {
                if (line.endsWith(columnDelimeter)) {
                    line += " ";
                }
                String[] arr = line.split(columnDelimeter);

                if (lineNumber == 0) {
                    for (int i = 0; i < arr.length; i++) {
                        tabFile.addColumn(arr[i]);
                    }
                } else {
                    if (filter != null) {
                        arr = filter.filter(arr);
                    }

                    tabFile.addRow(arr);
                }

                line = reader.readLine();
                lineNumber++;
            }
        } finally {
            reader.close();
        }

        return tabFile;
    }

    public String getColumnDelimeter() {
        return columnDelimeter;
    }

    public void setColumnDelimeter(String columnDelimeter) {
        this.columnDelimeter = columnDelimeter;
    }

    public String getTabFileName() {
        return tabFileName;
    }
}
