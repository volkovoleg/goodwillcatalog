package ru.goodfil.catalog.adapters.core;

import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFileReader.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public interface TabFileReader {
    public TabFile read() throws IOException;

    public TabFile read(TabFileRowFilter filter) throws IOException;
}
