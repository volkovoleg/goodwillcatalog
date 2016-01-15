package ru.goodfil.catalog.ui.adapters;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.dict.FilterStatus;
import ru.goodfil.catalog.ui.swing.resolver.Resolver;

import javax.swing.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: BigFilterTableAdapter.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
@Managed
public class BigFilterTableAdapter extends AbstractTableAdapter<Filter> {
    @NotNull
    private Resolver filterTypeResolver;

    @NotNull
    private Resolver filterFormResolver;

    public BigFilterTableAdapter(@NotNull ListModel listModel, @NotNull @Valid Resolver filterTypeResolver, @NotNull @Valid Resolver filterFormResolver) {
        super(listModel, new String[]{
                "Тип",
                "Форма",
                "Изделие",
                "Сайт",
                "Глоб",
                "Статус",
                "Доп. И",
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
                "G",
                "H",
                "PB",
                "NR",
                "Image",
        });

        this.filterFormResolver = filterFormResolver;
        this.filterTypeResolver = filterTypeResolver;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Filter filter = getRow(rowIndex);
        if (columnIndex == 0) return filterTypeResolver.resolve(filter.getFilterTypeCode());
        if (columnIndex == 1) return filterFormResolver.resolve(String.valueOf(filter.getFilterFormId()));
        if (columnIndex == 2) return filter.getName();
        if (columnIndex == 3) return getFilterOnSite(filter);
        if (columnIndex == 4) return getFilterIsGlobal(filter);
        if (columnIndex == 5) return FilterStatus.asString(filter.getStatusId());
        if (columnIndex == 6) return filter.getEan();
        if (columnIndex == 7) return filter.getaParam();
        if (columnIndex == 8) return filter.getbParam();
        if (columnIndex == 9) return filter.getcParam();
        if (columnIndex == 10) return filter.getdParam();
        if (columnIndex == 11) return filter.geteParam();
        if (columnIndex == 12) return filter.getfParam();
        if (columnIndex == 13) return filter.getgParam();
        if (columnIndex == 14) return filter.gethParam();
        if (columnIndex == 15) return filter.getPbParam();
        if (columnIndex == 16) return filter.getNrParam();
        if (columnIndex == 17) return filter.getImage();
        return null;
    }

    private String getFilterOnSite(Filter filter) {
        if (filter.getOnSite() != null && filter.getOnSite().equals(true)) {
            return "да";
        } else {
            return "нет";
        }
    }

    private String getFilterIsGlobal(Filter filter) {
        if ((filter.getApplyToAll_VT1() != null && filter.getApplyToAll_VT1().equals(true)) ||
                (filter.getApplyToAll_VT2() != null && filter.getApplyToAll_VT2().equals(true)) ||
                (filter.getApplyToAll_VT3() != null && filter.getApplyToAll_VT3().equals(true)) ||
                (filter.getApplyToAll_VT4() != null && filter.getApplyToAll_VT4().equals(true))) {

            StringBuilder sb = new StringBuilder();
            sb.append("да");

            Set<String> strs = new LinkedHashSet<String>();
            if (filter.getApplyToAll_VT1() != null && filter.getApplyToAll_VT1().equals(true)) {
                strs.add("легк.");
            }
            if (filter.getApplyToAll_VT2() != null && filter.getApplyToAll_VT2().equals(true)) {
                strs.add("груз.");
            }
            if (filter.getApplyToAll_VT3() != null && filter.getApplyToAll_VT3().equals(true)) {
                strs.add("спец.");
            }
            if (filter.getApplyToAll_VT4() != null && filter.getApplyToAll_VT4().equals(true)) {
                strs.add("кат.");
            }

            if (strs.size() > 0) {
                sb.append(" (");
                sb.append(StringUtils.join(strs, ","));
                sb.append(")");
            }

            return sb.toString();
        } else {
            return "нет";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String value = aValue.toString();

        Filter filter = getRow(rowIndex);

        if (columnIndex == 2) filter.setName(value);
        if (columnIndex == 6) filter.setEan(value);
        if (columnIndex == 7) filter.setaParam(value);
        if (columnIndex == 8) filter.setbParam(value);
        if (columnIndex == 9) filter.setcParam(value);
        if (columnIndex == 10) filter.setdParam(value);
        if (columnIndex == 11) filter.seteParam(value);
        if (columnIndex == 12) filter.setfParam(value);
        if (columnIndex == 13) filter.setgParam(value);
        if (columnIndex == 14) filter.sethParam(value);
        if (columnIndex == 15) filter.setPbParam(value);
        if (columnIndex == 16) filter.setNrParam(value);
        if (columnIndex == 17) filter.setImage(value);

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 3) {
            return false;
        } else {
            return true;
        }
    }
}