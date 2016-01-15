package ru.goodfil.catalog.web.utils;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SelectItem.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class SelectItem extends javax.faces.model.SelectItem {
    public SelectItem() {
    }

    public SelectItem(Object value) {
        super(value);
    }

    public SelectItem(Object value, String label) {
        super(value, label);
    }

    public SelectItem(Object value, String label, String description) {
        super(value, label, description);
    }

    public SelectItem(Object value, String label, String description, boolean disabled) {
        super(value, label, description, disabled);
    }

    public SelectItem(Object value, String label, String description, boolean disabled, boolean escape) {
        super(value, label, description, disabled, escape);
    }

    private String translatedLabel;

    public String getTranslatedLabel() {
        return translatedLabel;
    }

    public void setTranslatedLabel(String translatedLabel) {
        this.translatedLabel = translatedLabel;
    }
    
    public String getOriginalLabel() {
        return super.getLabel();
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }

    @Override
    public String getLabel() {
        return translatedLabel != null ? translatedLabel : super.getLabel();
    }
}
