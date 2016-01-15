package ru.goodfil.catalog.utils;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SelectItem.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public class SelectItem {
    private final String id;
    private String label;

    public SelectItem(String id) {
        this.id = id;
    }

    public SelectItem(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectItem that = (SelectItem) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return label;
    }
}
