package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.cfg.Settings;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Seria;
import ru.goodfil.catalog.domain.Setting;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SettingAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class SettingAdapter extends TableAdapter<Setting> {
    @Inject
    public SettingAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Setting.class, sessionProvider, validationProvider);
    }

    public Setting getByName(@NotNull @NotBlank String name) {
        List<Setting> settings = getByCriteria(Restrictions.eq("name", name));
        Assert.isTrue(settings.size() < 2);

        if (settings.size() == 1) {
            return settings.get(0);
        } else {
            return null;
        }
    }
}
