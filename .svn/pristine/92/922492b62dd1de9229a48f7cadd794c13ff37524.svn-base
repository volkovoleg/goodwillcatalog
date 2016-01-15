package ru.goodfil.catalog.utils;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: SearchMask.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class SearchMask {
    private SearchMask() {
        // nothing
    }

    public static String mask(String name) {
        StringBuffer sb = new StringBuffer(name.length());

        char[] arr = name.toCharArray();
        for (char c : arr) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toUpperCase(c));
            }
        }

        return sb.toString();
    }
}
