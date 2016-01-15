package ru.goodfil.catalog.applications.webcatalogupdater;

import org.apache.commons.io.FileUtils;
import ru.goodfil.catalog.utils.Assert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import static java.lang.Thread.sleep;

/**
 * Использование:
 * java -jar WebCatalogUpdater.jar C:\webcatalogupdate
 * @author sazonovkirill@gmail.com, Vit
 * @version $Id: WebCatalogUpdater.java 168 2013-09-27 09:38:34Z chezxxx@gmail.com $
 */
public class WebCatalogUpdater {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:Ss");

    public static String SOURCE_PATH = "C:\\catalogwebupdate";
    public static String DATABASE_NAME = "GoodwillCatalog.h2.db";
    public static String DATABASE_NAME_LOGS = "GoodwillCatalog.trace.db";
    public static String TARGET_PATH = "C:\\apache-tomcat-6.0.32-catalog\\bin\\.database";
    public static String PROCESS_NAME = "CatalogWebTomcat";
    public static String START_CMD = "C:\\apache-tomcat-6.0.32-catalog\\bin\\startup.bat";
    public static String STOP_CMD = "C:\\apache-tomcat-6.0.32-catalog\\bin\\shutdown.bat";

    /**
     * Путь до архивирующего скрипта
     */
    public static String START_COMPRESS="";

    public static void main(String[] args) throws InterruptedException, IOException {
        log("WebCatalogUpdater started");
        File propsFile = new File("WebCatalogUpdater.properties");
        if (!propsFile.exists()) {
            log("Settings created");
            Properties props = new Properties();
            props.setProperty("database.name", DATABASE_NAME);
            props.setProperty("database.logs", DATABASE_NAME_LOGS);
            props.setProperty("process.name", PROCESS_NAME);
            props.setProperty("target.path", TARGET_PATH);
            props.setProperty("start.cmd", START_CMD);
            props.setProperty("stop.cmd", STOP_CMD);
            props.setProperty("source.path", SOURCE_PATH);
            props.setProperty("start.compress", START_COMPRESS);
            props.store(new FileWriter(propsFile), null);
        }

        Properties props = new Properties();
        props.load(new FileReader(propsFile));
        DATABASE_NAME = props.getProperty("database.name");
        DATABASE_NAME_LOGS = props.getProperty("database.logs");
        PROCESS_NAME = props.getProperty("process.name");
        TARGET_PATH = props.getProperty("target.path");
        START_CMD = props.getProperty("start.cmd");
        STOP_CMD = props.getProperty("stop.cmd");
        SOURCE_PATH = props.getProperty("source.path");
        START_COMPRESS = props.getProperty("start.compress");

        File directory = new File(SOURCE_PATH);
        Assert.isTrue(directory.exists());
        Assert.isTrue(directory.isAbsolute());

        while (true) {
            Collection<File> files = FileUtils.listFiles(directory, new String[]{"db"}, false);
            for (File file : files) {
                if (file.getName().equals(DATABASE_NAME)) {
                    log("New version of catalog found!");

                    sleep(1000 * 10);
                    if (getTomcatIsRunning()) {
                        log("Tomcat is running. Stopping...");
                        stopTomcat();
                        log("Stopped");
                    }

                    log("Database copy...");
                    copyDatabase();
                    log("Complete");

                    startCompress();
                    log("Compress complete");

                    sleep(1000 * 30);
                    log("Starting tomcat...");
                    startTomcat();
                    log("Started");
                }
            }

            Thread.sleep(5000);
        }

    }

    public static void copyDatabase() throws IOException {
        FileUtils.deleteQuietly(new File(TARGET_PATH + File.separator + DATABASE_NAME));
        FileUtils.deleteQuietly(new File(TARGET_PATH + File.separator + DATABASE_NAME_LOGS));
        FileUtils.moveFile(new File(SOURCE_PATH + File.separator + DATABASE_NAME),
                           new File(TARGET_PATH + File.separator + DATABASE_NAME));
    }

    public static void stopTomcat() throws IOException, InterruptedException {
        log(STOP_CMD);
        Runtime.getRuntime().exec(STOP_CMD);
        sleep(3000 * 10);
    }

    public static void startTomcat() throws IOException, InterruptedException {
        log(START_CMD);
        Runtime.getRuntime().exec(START_CMD);
        sleep(1000 * 10);
    }

    public static void startCompress() throws IOException, InterruptedException {
        log(START_COMPRESS);
        Runtime.getRuntime().exec(START_COMPRESS);
        sleep(1500 * 10);
    }

    public static boolean getTomcatIsRunning() throws IOException, InterruptedException {
        return ru.goodfil.catalog.process.Process.exists(PROCESS_NAME);
    }

    public static void log(String msg) {
        System.out.println(String.format("%-10s %s", sdf.format(Calendar.getInstance().getTime()), msg));
    }
}
