package ru.goodfil.catalog.adapters.core;

import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: TabFileTransformation.java 91 2012-09-23 06:25:16Z sazonovkirill $
 */
public interface TabFileTransformation<T> {
    List<T> transform(TabFile tabFile, Mapping mapping);

    List<T> transform(TabFile tabFile, Mapping mapping, TransformationFilter<T> filter);
}
