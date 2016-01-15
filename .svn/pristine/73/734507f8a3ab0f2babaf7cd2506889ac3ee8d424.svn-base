package ru.goodfil.catalog.web;

import com.google.inject.Inject;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.goodfil.catalog.annotations.Logged;
import ru.goodfil.catalog.annotations.ValidateAfter;
import ru.goodfil.catalog.annotations.web.Init;
import ru.goodfil.catalog.services.SettingsService;
import ru.goodfil.catalog.utils.SessionProvider;
import ru.goodfil.catalog.web.utils.FacesUtils;
import ru.goodfil.catalog.web.utils.PageBean;
import ru.goodfil.catalog.utils.SessionFactoryHolder;
import ru.goodfil.catalog.utils.SessionProvider;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.IOException;
import java.net.*;

/**
 * @author Vit
 */
public class UpdaterBean extends PageBean {

    private final String CATALOG_URL = "http://catalog.goodfil.com/CatalogWeb/main";
    private final String CATALOG_UPLOAD_URL = "http://catalog.goodfil.com/External/";
    private String PATH_TO_DB;
    private final String DB_NAME = "GoodwillCatalog.h2.db";
    private final String DB_ARCHIVE_NAME = "GoodwillCatalog.h2.zip";
    private final String RES_PATH = "/ru/goodfil/catalog/web/loaded/";
    private String RES_ABS_PATH;
    
    public String updateState="";
    public final String EMPTY="";
    public final String ERROR="0";
    public final String NEW_VERSION="1";
    public final String BEFORE_UPDATE="2";
    public final String WAIT_FOR_UPLOAD="3";
    public final String INFO_FOR_RELOAD="4";

    private SessionProvider sessionProvider;

    @NotNull
    @Inject
    private SettingsService settingsService;

    public UpdaterBean() {
        init();
    }

    @Init
    @Logged
    @ValidateAfter
    public void init() {
        InjectorBean injectorBean = FacesUtils.getManagedBean("InjectorBean");
        sessionProvider = injectorBean.getInjector().getInstance(SessionProvider.class);
        PATH_TO_DB=SessionFactoryHolder.getDatabasePathWithoutPrefix();
    }

    /**
     * Подключаеться к каталогу, парсит его и получает версию БД
     * @param url
     * @return
     */
    private String getVersionDBFromCatalog(String url) {
        String version = "";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (IOException e) {
            return "";
        }
        Elements spains = doc.select("span");
        String string = "";
        for (Element element : spains) {
            string = element.ownText();
            if (string.indexOf("Версия базы") != -1) {
                version = string.substring(string.indexOf(":") + 1).trim();
                break;
            }
        }
        return version;
    }

    /**
     * Обновляет состояние оповещений, связанных с обновлением
     */
    public void checkForUpdate() {
        String versionDBNow = settingsService.get("deploy.date");
        String versionDBCatalog = getVersionDBFromCatalog(CATALOG_URL);
        if (versionDBCatalog.equals("")) {
            updateState = getERROR();
            System.out.println("Error connection!");
        } else if (versionDBCatalog.equals(versionDBNow)) {
            updateState = getNEW_VERSION();
            System.out.println("Not new");
        } else if (!(versionDBCatalog.equals(versionDBNow))) {
            updateState = getBEFORE_UPDATE();
            System.out.println("Download DB");
        }
    }

    /**
     * Запуск процессов по обновлению БД каталога
     */
    public void updateDB() {
        downloadDB();
        sessionProvider.close();
        dearchiveDB();
        updateState = getINFO_FOR_RELOAD();
        moveDB();

    }

    /**
     * Скачивает архив с последней БД
     */
    private void downloadDB() {
        URL urlToDBExchange = null;
        File uploadedArchive = null;
        File oldUnzipedFile = null;
        try {
            uploadedArchive = new File(getClass().getResource(RES_PATH + DB_ARCHIVE_NAME).toURI());
            RES_ABS_PATH=uploadedArchive.getParent();
            urlToDBExchange = new URL(CATALOG_UPLOAD_URL+DB_ARCHIVE_NAME);
            FileUtils.copyURLToFile(urlToDBExchange, uploadedArchive);
            oldUnzipedFile = new File(uploadedArchive.getParent() + File.separator + DB_NAME);
            if (oldUnzipedFile.exists() && oldUnzipedFile.isFile()) {
                FileUtils.deleteQuietly(oldUnzipedFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Разархивация файла с БД
     */
    private void dearchiveDB() {
        String zipFile=RES_ABS_PATH + File.separator + DB_ARCHIVE_NAME;
        String outputFolder=RES_ABS_PATH;
        try{
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();
            while(ze!=null){
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                System.out.println("File unzip : "+ newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("File unziped");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Перенос скаченной и разархивированной БД на место старой базы
     */
    private void moveDB(){
        try {
        File oldDB = new File(PATH_TO_DB.replace(";TRACE_LEVEL_FILE=0","")+ ".h2.db");
        File newDB = new File(getClass().getResource(RES_PATH + DB_NAME).toURI());
        if ((oldDB.exists() && oldDB.isFile()) && (newDB.exists() && newDB.isFile())) {
            FileUtils.deleteQuietly(oldDB);
            String DBPath=PATH_TO_DB.replace("GoodwillCatalog;TRACE_LEVEL_FILE=0","");
            File DBDir=new File(DBPath);
            FileUtils.moveFileToDirectory(newDB,DBDir,false);
            System.out.println("DB integrated!");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUpdateState() {
        return updateState;
    }

    public void setUpdateState(String updateState) {
        this.updateState = updateState;
    }

    public String getERROR() {
        return ERROR;
    }

    public String getNEW_VERSION() {
        return NEW_VERSION;
    }

    public String getBEFORE_UPDATE() {
        return BEFORE_UPDATE;
    }

    public String getWAIT_FOR_UPLOAD() {
        return WAIT_FOR_UPLOAD;
    }

    public String getINFO_FOR_RELOAD() {
        return INFO_FOR_RELOAD;
    }

    public String getEMPTY() {
        return EMPTY;
    }
}
