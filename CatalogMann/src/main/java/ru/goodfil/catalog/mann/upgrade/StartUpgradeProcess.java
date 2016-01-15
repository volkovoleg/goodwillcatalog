package ru.goodfil.catalog.mann.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: sazonovkirill@gmail.com
 * Date: 28.11.12
 */
public class StartUpgradeProcess extends AbstractUpgradeProcess {
    private static final Logger logger = LoggerFactory.getLogger(StartUpgradeProcess.class);
    
    @Override
    public String getVersion() {
        return "1.0.0.0";
    }

    @Override
    public String getBaseVersion() {
        return "0.0.0.0";
    }

    @Override
    public String getUpgradeProcessId() {
        return "InitialUpgradeProcess";
    }

    @Override
    public String getUpgradeProcessDescription() {
        return "Performs initialization of managed databases";
    }

    @Override
    public void upgrade() {
        logger.debug("Upgrade stub");
    }
    
    public static void main(String... args) {
        System.setProperty("work.dir", "D:\\Projects\\catalog\\CatalogMann\\work\\");
        new UpgradeProcessManager().applyUpgradeProcess(new StartUpgradeProcess());
    }
}
