package ru.goodfil.catalog.mann.upgrade;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: sazonovkirill@gmail.com
 * Date: 28.11.12
 */
public class CreateInitialDatabase {
    public static void main(String... args) {
        final String initialDatabse = "D:\\Projects\\catalog\\CatalogMann\\work\\GoodwillCatalog_RC1.h2.db";
        
        File activeDatabase = new File(initialDatabse);

        String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String databaseFilename = "managed_GoodwillCatalog_0.0.0.0_" + currentDate + ".h2.db";
        String databaseDescriptionFilename = databaseFilename + ".description";
        File finalDatabase = new File(activeDatabase.getParent(), databaseFilename);
        try {
            FileUtils.copyFile(activeDatabase, finalDatabase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UpgradeProcessManager.DatabaseDescription newdd = new UpgradeProcessManager.DatabaseDescription();
        newdd.setBaseVersion("0.0.0.0");
        newdd.setVersion("0.0.0.0");
        newdd.setCreateByUpgradeProcess(null);
        newdd.setUpgradeProcessDescription("Initial");
        newdd.setDatabaseFilename(databaseFilename);
        newdd.setDatabaseDescriptionFilename(databaseDescriptionFilename);

        XStream xs = new XStream();
        xs.processAnnotations(UpgradeProcessManager.DatabaseDescription.class);

        try {
            xs.toXML(newdd, new FileWriter(new File(activeDatabase.getParent(), databaseDescriptionFilename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
