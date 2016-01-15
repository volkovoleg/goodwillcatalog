package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Clear;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.annotations.web.ManagedBean;
import ru.goodfil.catalog.annotations.web.PageAction;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.web.utils.PageBean;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: GoodwillGoodBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
@Managed
@ManagedBean
public class GoodwillGoodBean extends PageBean {
    @NotNull
    @Inject
    private CarsService carsService;

    @NotNull
    @Inject
    private FiltersService filtersService;

    @NotNull
    @Inject
    private AnalogsService analogsService;

    private String searchText;

    private final List<Filter> filters = new ArrayList<Filter>();

    private String noSpamText = "";

    public GoodwillGoodBean() {
        init();
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
    }

    @Clear
    @Logged
    public void clear() {

    }

    private List<Filter> filterFiltersByGoodwillBrand(List<Filter> filters) {
        List<Filter> result = new ArrayList<Filter>();
        for (Filter filter : filters) {
            if (filter.getOnSite()) {
                result.add(filter);
            }
        }
        return result;
    }
    
    @PageAction
    public void search() throws IOException {
        Assert.notNull(searchText);
        if (searchText == null || StringUtils.isBlank(searchText) || searchText.length() < 2) {
            //  Необходимо указать не менее 2-х символов
            return;
        }

        if(checkValueForSpam(noSpamText)) {
            return;
        }

        filters.clear();
        filters.addAll(filterFiltersByGoodwillBrand(filtersService.getFiltersByName(searchText)));
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public CarsService getCarsService() {
        return carsService;
    }

    public void setCarsService(CarsService carsService) {
        this.carsService = carsService;
    }

    public FiltersService getFiltersService() {
        return filtersService;
    }

    public void setFiltersService(FiltersService filtersService) {
        this.filtersService = filtersService;
    }

    public String getNoSpamText() {
        return noSpamText;
    }

    public void setNoSpamText(String noSpamText) {
        this.noSpamText = noSpamText;
    }
}
