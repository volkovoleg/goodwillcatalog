package ru.goodfil.catalog.ui.swing.resolver;

import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DefaultResolver.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class DefaultResolver implements Resolver {
    private final Map<String, String> items;

    public DefaultResolver(@NotNull @NotBlank Map<String, String> items) {
        this.items = items;
    }

    @Override
    public String resolve(String code) {
        return items.get(code);
    }

    @Override
    public void update(@NotNull Map<String, String> map) {
        items.clear();
        items.putAll(map);
    }
}
