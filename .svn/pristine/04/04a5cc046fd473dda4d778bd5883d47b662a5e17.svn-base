package ru.goodfil.catalog.adapters;

import com.google.inject.Inject;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.utils.SearchMask;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.validation.core.ValidationProvider;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: BrandAdapter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
@Managed
public class BrandAdapter extends TableAdapter<Brand> {
    @Inject
    public BrandAdapter(SessionProvider sessionProvider, ValidationProvider validationProvider) {
        super(Brand.class, sessionProvider, validationProvider);
    }

    public List<Brand> getByIds(@NotNull Set<Long> brandIds) {
        return getByCriteria(Restrictions.in("id", brandIds));
    }

    public List<Brand> searchByName(@NotNull @NotBlank String name) {
        return getByCriteria(Restrictions.like("searchName", SearchMask.mask(name), MatchMode.START), Order.asc("name"));
    }

    public List<Brand> getByName(final @NotNull @NotBlank String name) {
        return getByCriteria(Restrictions.eq("name", name), Order.asc("name"));
    }
}
