package ru.goodfil.catalog.mann.upgrade;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import ru.goodfil.catalog.utils.Assert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * User: sazonovkirill@gmail.com
 * Date: 28.11.12
 */
public class UpgradeProcessManager {
    private final String workDir;
    
    public static void main(String... args) {
        new UpgradeProcessManager().showLog();
    }

    public void showLog() {
        ManagedDatabasesModel model = new ManagedDatabasesModel(workDir);
        System.out.println("Found managed databases: " + model.getDatabases().size());
        for (DatabaseDescription dd : model.getDatabases()) {
            System.out.println("Database: " + dd.getDatabaseFilename());
            System.out.println("Version: " + dd.getVersion());
            System.out.println("Created from " + dd.getBaseVersion() + " by " + dd.getCreateByUpgradeProcess());
            System.out.println("Description: " + dd.getUpgradeProcessDescription());
            System.out.println();
        }
    }

    public void applyUpgradeProcess(AbstractUpgradeProcess upgradeProcess) {
        ManagedDatabasesModel model = new ManagedDatabasesModel(workDir);
        DatabaseDescription dd = model.getByVersion(upgradeProcess.getBaseVersion());
        if (dd == null) {
            System.out.println("Nothing to apply, exit");
            System.exit(0);
        }                
        
        String filename = dd.getDatabaseFilename();
        File database = new File(filename);
        File activeDatabase = new File(database.getParent(), "GoodwillCatalog.h2.db");
        try {
            FileUtils.copyFile(database, activeDatabase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        try {
            upgradeProcess.upgrade();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        } 
        
        String currentDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        String databaseFilename = "managed_GoodwillCatalog_" + upgradeProcess.getVersion() + "_" + currentDate + ".h2.db";
        String databaseDescriptionFilename = databaseFilename + ".description";
        File finalDatabase = new File(activeDatabase.getParent(), databaseFilename);
        try {
            FileUtils.copyFile(activeDatabase, finalDatabase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        DatabaseDescription newdd = new DatabaseDescription();
        newdd.setBaseVersion(upgradeProcess.getBaseVersion());
        newdd.setVersion(upgradeProcess.getVersion());
        newdd.setCreateByUpgradeProcess(upgradeProcess.getUpgradeProcessId());
        newdd.setUpgradeProcessDescription(upgradeProcess.getUpgradeProcessDescription());
        newdd.setDatabaseFilename(databaseFilename);
        newdd.setDatabaseDescriptionFilename(databaseDescriptionFilename);
        
        XStream xs = new XStream();
        xs.processAnnotations(DatabaseDescription.class);
        
        try {
            xs.toXML(newdd, new FileWriter(new File(workDir, databaseDescriptionFilename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UpgradeProcessManager() {
        String workDir = System.getProperty("work.dir");
        this.workDir = workDir;
    } 

    public static class ManagedDatabasesModel {
        private final List<DatabaseDescription> databases = new ArrayList<DatabaseDescription>();

        public ManagedDatabasesModel(String workDirPath) {
            File workDir = new File(workDirPath);
            Assert.isTrue(workDir.exists() && workDir.isDirectory());

            String[] files = workDir.list(managedDatabasesFilter);
            for (String file : files) {
                File descriptionFile = new File(workDir, file + ".description");
                Assert.isTrue(descriptionFile.exists());

                StringWriter sw = new StringWriter();
                try {
                    IOUtils.copy(new FileReader(descriptionFile), sw);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                XStream xs = new XStream();
                xs.processAnnotations(DatabaseDescription.class);
                DatabaseDescription dd = (DatabaseDescription) xs.fromXML(sw.toString());

                databases.add(dd);
            }
        }

        private FilenameFilter managedDatabasesFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("managed_GoodwillCatalog_") && name.endsWith(".h2.db");
            }
        };

        public List<DatabaseDescription> getDatabases() {
            return databases;
        }

        public DatabaseDescription getByVersion(String version) {
            List<DatabaseDescription> result = new ArrayList<DatabaseDescription>();
            for (DatabaseDescription dd : databases) {
                if (dd.getVersion().equals(version)) {
                    result.add(dd);
                }
            }
            
            Assert.isTrue(result.size() < 2);
            if (result.size() == 1) return result.get(0);
            else return null;
        }
    }


    
    @XStreamAlias("database-description")
    public static class DatabaseDescription {
        @XStreamAlias("database-filename")
        @XStreamAsAttribute
        private String databaseFilename;

        @XStreamAlias("database-description-filename")
        @XStreamAsAttribute
        private String databaseDescriptionFilename;

        @XStreamAlias("version")
        private String version;

        @XStreamAlias("base-version")
        private String baseVersion;

        @XStreamAlias("upgrade-process")
        private String createByUpgradeProcess;

        @XStreamAlias("description")
        private String upgradeProcessDescription;

        public String getDatabaseFilename() {
            return databaseFilename;
        }

        public void setDatabaseFilename(String databaseFilename) {
            this.databaseFilename = databaseFilename;
        }

        public String getDatabaseDescriptionFilename() {
            return databaseDescriptionFilename;
        }

        public void setDatabaseDescriptionFilename(String databaseDescriptionFilename) {
            this.databaseDescriptionFilename = databaseDescriptionFilename;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getBaseVersion() {
            return baseVersion;
        }

        public void setBaseVersion(String baseVersion) {
            this.baseVersion = baseVersion;
        }

        public String getCreateByUpgradeProcess() {
            return createByUpgradeProcess;
        }

        public void setCreateByUpgradeProcess(String createByUpgradeProcess) {
            this.createByUpgradeProcess = createByUpgradeProcess;
        }

        public String getUpgradeProcessDescription() {
            return upgradeProcessDescription;
        }

        public void setUpgradeProcessDescription(String upgradeProcessDescription) {
            this.upgradeProcessDescription = upgradeProcessDescription;
        }
    }
}
