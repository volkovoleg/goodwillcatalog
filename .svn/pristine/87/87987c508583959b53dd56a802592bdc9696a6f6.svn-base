package ru.goodfil.catalog.mann.merge;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FilterForm;
import ru.goodfil.catalog.domain.dict.FilterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
@Component
@Lazy(true)
public class FilterTypesMerge extends AbstractMerge<FilterType> {
    protected FilterTypesMerge() {
        super(FilterType.class);
    }

    Long id = 0L;
    
    @Override
    public FilterType join(List<Wrapper<FilterType>> items) {
        Wrapper<FilterType> filterTypeWrapper = findFistFromMannOtherwiseFirst(items);
        FilterType filterType = filterTypeWrapper.item();

        id++;
        FilterType newFilterType = new FilterType();
        newFilterType.setId(id);
        newFilterType.setCode(filterType.getCode());
        newFilterType.setName(filterType.getName());

        return newFilterType;
    }

    @Override
    public List<FilterType> merge(List<Wrapper<FilterType>> items) {
        Map<String/*businessKey*/, List<Wrapper<FilterType>>> keyMap = new HashMap<String, List<Wrapper<FilterType>>>();
        logger.trace("Merge for " + klass.getSimpleName());

        final List<FilterType> result = new ArrayList<FilterType>();

        // Groupping
        for (Wrapper<FilterType> item : items) {
            String businessKey = getBusinessKey(item);

            if (!keyMap.containsKey(businessKey)) {
                keyMap.put(businessKey, new ArrayList<Wrapper<FilterType>>());
            }

            keyMap.get(businessKey).add(item);
        }

        // Joining
        int counter = 0;
        for (Map.Entry<String, List<Wrapper<FilterType>>> item : keyMap.entrySet()) {
            counter++;
            if (counter % 100 == 0)
                logger.debug("Processing " + klass + " " + counter + " of " + keyMap.size());

            String businessKey = item.getKey();
            List<Wrapper<FilterType>> itemsToJoin = item.getValue();

            FilterType newItem = join(itemsToJoin);
            finalTemplate.save(newItem);
            for (Wrapper<FilterType> itemToJoin : itemsToJoin) {
                addToContext(itemToJoin.source(), itemToJoin.item().getCode(), newItem.getCode());
            }

            mergeChildren(itemsToJoin);
            result.add(newItem);
        }

        return result;
    }

    @Override
    public String getBusinessKey(Wrapper<FilterType> item) {
        return item.item().getCode();
    }

    @Override
    protected void mergeChildren(List<Wrapper<FilterType>> itemsToJoin) {
        List<Wrapper<FilterForm>> children = new ArrayList<Wrapper<FilterForm>>();

        String query = "from FilterForm f where f.filterTypeCode = ?";
        for (Wrapper<FilterType> wrapper : itemsToJoin) {
            if (wrapper.source().equals(Source.MANN.id())) {
                List<FilterForm> l = mannTemplate.find(query, wrapper.item().getCode());
                children.addAll(Wrapper.bySource(l, Source.MANN.id()));
            }
            if (wrapper.source().equals(Source.BASE.id())) {
                List<FilterForm> l = baseTemplate.find(query, wrapper.item().getCode());
                children.addAll(Wrapper.bySource(l, Source.BASE.id()));
            }
        }

        mergers.byClass(FilterForm.class).merge(children);
    }
}
