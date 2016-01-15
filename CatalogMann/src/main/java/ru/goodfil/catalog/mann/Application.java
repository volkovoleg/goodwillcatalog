package ru.goodfil.catalog.mann;

import org.apache.commons.io.FileUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.goodfil.catalog.mann.services.MannEtlService;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 */
public class Application {
    public static void main(String[] args) throws IOException {
        File baseDir = new File(System.getProperty("base.dir"));

        if (!baseDir.exists() || !baseDir.isDirectory()) throw new RuntimeException("Base directory doesnt exist");
        String[] files = baseDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".h2.db") ||
                       name.endsWith(".lock.db") ||
                       name.endsWith(".trace.db");
            }
        });

        for (String file : files) {
            FileUtils.forceDelete(new File(baseDir, file));
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MannEtlService service = context.getBean(MannEtlService.class);
        service.run();
    }
}
