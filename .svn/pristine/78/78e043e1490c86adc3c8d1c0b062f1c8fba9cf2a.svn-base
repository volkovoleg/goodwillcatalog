package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterType;

import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FilterFormsMerge extends AbstractMerge<FilterForm> {
    protected FilterFormsMerge() {
        super(FilterForm.class);
    }

    Long id = 0L;

    @Override
    public FilterForm join(List<Wrapper<FilterForm>> items) {
        Wrapper<FilterForm> filterFormWrapper = findFistFromMannOtherwiseFirst(items);
        FilterForm filterForm = filterFormWrapper.item();

        id++;
        FilterForm newFilterForm = new FilterForm();
        newFilterForm.setId(id);
        newFilterForm.setName(filterForm.getName());
        newFilterForm.setaParam(filterForm.getaParam());
        newFilterForm.setbParam(filterForm.getbParam());
        newFilterForm.setcParam(filterForm.getcParam());
        newFilterForm.setdParam(filterForm.getdParam());
        newFilterForm.seteParam(filterForm.geteParam());
        newFilterForm.setfParam(filterForm.getfParam());
        newFilterForm.setgParam(filterForm.getgParam());
        newFilterForm.sethParam(filterForm.gethParam());
        newFilterForm.setBpParam(filterForm.getBpParam());
        newFilterForm.setNrParam(filterForm.getNrParam());

        String filterTypeCode = getDestBySource(FilterType.class, filterFormWrapper.source(), filterForm.getFilterTypeCode());
        newFilterForm.setFilterTypeCode(filterTypeCode);

        return newFilterForm;
    }

    @Override
    public String getBusinessKey(Wrapper<FilterForm> item) {
        return item.item().getName();
    }

    @Override
    protected void mergeChildren(List<Wrapper<FilterForm>> itemsToJoin) {
        mergeChildren(itemsToJoin, "from Filter f where f.filterFormId = ?", Filter.class);
    }
}
