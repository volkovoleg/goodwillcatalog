package ru.goodfil.catalog.domain;

/**
 * User: Vit
 * Date: 07.08.13
 */
public interface StandaloneBrandStatus {
    public static final int DONT_USE_IN_STANDALONE = 0;
    public static final int USE_IN_STANDALONE = 1;

    void setStandaloneStatus(Integer standaloneStatus);
}
