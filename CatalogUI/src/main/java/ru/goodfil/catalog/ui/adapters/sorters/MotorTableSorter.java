package ru.goodfil.catalog.ui.adapters.sorters;

import ru.goodfil.catalog.ui.adapters.MotorTableAdapter;

import javax.swing.table.TableRowSorter;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: MotorTableSorter.java 158 2013-07-09 12:34:38Z chezxxx@gmail.com $
 */
public class MotorTableSorter extends TableRowSorter<MotorTableAdapter> {
    public MotorTableSorter(MotorTableAdapter model) {
        super(model);
    }

    @Override
    public Comparator<?> getComparator(int column) {
        Comparator<?> result = super.getComparator(column);

        if (column == MotorTableAdapter.COL_DATE_F ||
                column == MotorTableAdapter.COL_DATE_T) {
            return new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    if (o1 == null && o2 == null) {
                        return 0;
                    } else if (o1 == null) {
                        return 1;
                    } else if (o2 == null) {
                        return -1;
                    } else {
                        String s1 = o1.toString();
                        String s2 = o2.toString();

                        Date d1 = null;
                        Date d2 = null;

                        try {
                            d1 = MotorTableAdapter.sdf.parse(s1);
                        } catch (ParseException e) {
                            // nothing
                        }

                        try {
                            d2 = MotorTableAdapter.sdf.parse(s2);
                        } catch (ParseException e) {
                            // nothing
                        }

                        if (d1 == null && d2 == null) {
                            return 0;
                        } else if (d1 == null) {
                            return 1;
                        } else if (d2 == null) {
                            return -1;
                        } else {
                            return d1.compareTo(d2);
                        }
                    }
                }
            };
        }

        if (column == MotorTableAdapter.COL_HP ||
                column == MotorTableAdapter.COL_KW) {
            return new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    if (s1 == null && s2 == null) {
                        return 0;
                    } else if ((s1 == null) || (s1.equals(""))) {
                        return 1;
                    } else if ((s2 == null) || (s1.equals(""))) {
                        return -1;
                    } else {
                        Integer furst = null;
                        Integer second = null;
                        try {
                            furst = Integer.parseInt(s1);
                        } catch (NumberFormatException exception) {
                        }
                        try {
                            second = Integer.parseInt(s2);
                        } catch (NumberFormatException exception) {
                        }
                        if (furst == null && second == null) {
                            return 0;
                        } else if (furst == null) {
                            return 1;
                        } else if (second == null) {
                            return -1;
                        } else {
                            if (furst == second) {
                                return 0;
                            } else if (furst > second) {
                                return 1;
                            } else if (furst < second) {
                                return -1;
                            }
                        }
                    }
                    return 0;
                }
            };
        }

        return result;
    }
}
