package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.Motor;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FiltersMerge extends AbstractMerge<Filter> {
    protected FiltersMerge() {
        super(Filter.class);
    }

    Long id = 0L;
    
    @Override
    public Filter join(List<Wrapper<Filter>> items) {
        Wrapper<Filter> filterWrapper = findFistFromMannOtherwiseFirst(items);
        Filter filter = filterWrapper.item();

        id++;
        Filter newFilter = new Filter();
        newFilter.setId(id);
        newFilter.setApplyToAll_VT1(false);
        newFilter.setApplyToAll_VT2(false);
        newFilter.setApplyToAll_VT3(false);
        newFilter.setApplyToAll_VT4(false);

        newFilter.setEan(filter.getEan());
        newFilter.setNrParam(filter.getNrParam());
        newFilter.setSearchName(filter.getSearchName());

        newFilter.setaParam(filter.getaParam());
        newFilter.setbParam(filter.getbParam());
        newFilter.setcParam(filter.getcParam());
        newFilter.setdParam(filter.getdParam());
        newFilter.seteParam(filter.geteParam());
        newFilter.setfParam(filter.getfParam());
        newFilter.setgParam(filter.getgParam());
        newFilter.sethParam(filter.gethParam());
        newFilter.setPbParam(filter.getPbParam());

        newFilter.setImage(filter.getImage());
        newFilter.setDisabled(false);
        newFilter.setOnSite(true);
        newFilter.setOe(filter.getOe());
        newFilter.setName(filter.getName());

        Long filterFormId = getDestBySource(FilterForm.class, filterWrapper.source(), filter.getFilterFormId());
        newFilter.setFilterFormId(filterFormId);
        newFilter.setFilterTypeCode(filter.getFilterTypeCode());

        return newFilter;
    }

    @Override
    public String getBusinessKey(Wrapper<Filter> item) {
        return item.item().getName();
    }

    @Override
    protected void mergeChildren(List<Wrapper<Filter>> itemsToJoin) {
        // Nothing
    }
}
