package ru.goodfil.catalog.mann.upgrade;

/**
 * User: sazonovkirill@gmail.com
 * Date: 28.11.12
 */
public abstract class AbstractUpgradeProcess {
    public abstract String getVersion();
    public abstract String getBaseVersion();
    public abstract String getUpgradeProcessId();
    public abstract String getUpgradeProcessDescription();
    public abstract void upgrade();
}
