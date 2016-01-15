package ru.goodfil.catalog.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FilePublisher.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class FilePublisher {
//    private static final Logger logger = Logger.getLogger(FilePublisher.class);

    /**
     * Функция копирует файл по указанному пути в директорию сервера и возвращает ссылку на этот файл.
     * <p/>
     * Данная функция предназначена для вызова из окружения JSF и использует для получения папки приложени вызов
     * HttpServletRequest.getRealPath.
     *
     * @param request        объект, с помощью которого вызывается функция getRealPath
     * @param sourceFilePath путь к исходному файлу
     * @param targetFileName наименование конечного файла
     */
    public static String retrieveFile(HttpServletRequest request, String sourceFilePath, String targetFileName) {
        return retrieveFile(request.getRealPath("/"), sourceFilePath, targetFileName);
    }

    /**
     * Функция копирует файл по указанному пути в директорию сервера и возвращает ссылку на этот файл.
     *
     * @param realPath       директория, относительно которой выкладывается файл
     * @param sourceFilePath путь к исходному файлу
     * @param targetFileName наименование конечного файла
     */
    public static String retrieveFile(String realPath, String sourceFilePath, String targetFileName) {
        String link = "out/" + targetFileName;
        try {
            File f1 = new File(sourceFilePath);
            File f2 = new File(realPath + File.separator + "out" + File.separator + targetFileName);

            copyFile(f1, f2);

//            logger.debug("Retrieving link `" + link + "` to file `" + f2.getPath() + "`. Source: `" + f1.getPath() + "`.");
            return link;
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("Cannot retrieve file: " + e);
            return null;
        }
    }

    /**
     * Копирование файла. И почему этого нет в java?
     *
     * @param in  исходный файл
     * @param out конечный файл
     */
    private static void copyFile(java.io.File in, File out) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(in);
            fos = new FileOutputStream(out);

            byte[] buf = new byte[1024];
            int i;
            while ((i = fis.read(buf)) != -1) fos.write(buf, 0, i);
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }

    /*
    Для отладки
     */
    public static void main(String[] args) {
        retrieveFile("c:\\", "c:\\test.xls", "result.xls");
    }
}