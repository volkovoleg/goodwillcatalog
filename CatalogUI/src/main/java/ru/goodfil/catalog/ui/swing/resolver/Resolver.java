package ru.goodfil.catalog.ui.swing.resolver;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Resolver.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public interface Resolver {
    String resolve(@NotNull @NotBlank String code);

    void update(@NotNull Map<String, String> map);
}
