package ru.goodfil.catalog.ui.adapters;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.FiltersAndMotors;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SmallFilterTableAdapter.java 179 2014-07-18 12:31:54Z chezxxx@gmail.com $
 */
public class SmallFilterTableAdapter extends AbstractTableAdapter<Filter> {
    private final Resolver filterTypeResolver;
    private final List<FiltersAndMotors> filtersAndMotorsList;

    public SmallFilterTableAdapter(ListModel listModel, Resolver filterTypeResolver) {
        super(listModel, new String[]{"Название", "Тип", "Комментарий"});
        this.filterTypeResolver = filterTypeResolver;
        this.filtersAndMotorsList=new ArrayList<FiltersAndMotors>();
    }

    public SmallFilterTableAdapter(ListModel listModel, Resolver filterTypeResolver, List<FiltersAndMotors> filtersAndMotorsList ) {
        super(listModel, new String[]{"Название", "Тип", "Комментарий"});
        this.filterTypeResolver = filterTypeResolver;
        this.filtersAndMotorsList=filtersAndMotorsList;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Filter filter = getRow(rowIndex);
        if (columnIndex == 0) {
            return filter.getName();
        }
        if (columnIndex == 1) {
            return filterTypeResolver.resolve(filter.getFilterTypeCode());
        }
        if (columnIndex == 2) {
            String s = "";
            for (FiltersAndMotors andMotors : filtersAndMotorsList) {
                if (andMotors.getFilterId().equals(filter.getId())) {
                    s = andMotors.getFilterComment();
                    break;
                }
            }
            return s;
        }
        return null;
    }
}