package ru.goodfil.catalog.mann.merge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import ru.goodfil.catalog.domain.Unique;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 01.12.12
 */
public interface Merge<T extends Unique> {
    public List<T> merge(List<Wrapper<T>> items);
}
