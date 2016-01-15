package ru.goodfil.catalog.web;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: LanguageBean.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class LanguageBean {
    private String locale = "ru";

    public void setup() {
        System.out.println("Now locale is: " + locale);
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
