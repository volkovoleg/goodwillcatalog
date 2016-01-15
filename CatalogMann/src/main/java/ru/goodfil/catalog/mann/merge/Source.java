package ru.goodfil.catalog.mann.merge;

/**
 * User: sazonovkirill@gmail.com
 * Date: 09.12.12
 */
public enum Source {
    BASE("BASE"),
    MANN("MANN");

    private final String id;

    private Source(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}
