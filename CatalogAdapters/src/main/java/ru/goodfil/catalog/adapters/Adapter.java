package ru.goodfil.catalog.adapters;

import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Adapter.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public interface Adapter {
    void loadData(String workspace) throws IOException;
}
