package ru.goodfil.catalog.utils;

import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentityGenerator;
import ru.goodfil.catalog.domain.Unique;

import java.io.Serializable;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: CustomIdentityGenerator.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class CustomIdentityGenerator extends IdentityGenerator {
    @Override
    public Serializable generate(SessionImplementor s, Object obj) {
        Unique unique = (Unique) obj;
        if (unique.getId() != null) {
            return unique.getId();
        } else {
            Serializable result = super.generate(s, obj);
            return result;
        }
    }
}
