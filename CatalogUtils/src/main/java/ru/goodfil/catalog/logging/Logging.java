package ru.goodfil.catalog.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Logging.java 93 2012-09-23 06:43:51Z sazonovkirill $
 */
public abstract class Logging {
    public static void configure() {
        System.out.println("Configuring logging..");

        Logger logger = Logger.getLogger("ru.goodfil.catalog.services.impl.CarsService");
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.FINEST);
    }
}
