package ru.goodfil.catalog.web.i18n.domain;

import com.thoughtworks.xstream.XStream;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DomainTest.java 94 2012-09-23 06:47:09Z sazonovkirill $
 */
public class DomainTest {
    public static void main(String[] args) throws IOException {
        I18n i18n = new I18n();
        i18n.setContexts(new ArrayList<Context>());

        Context c = new Context();
        c.setName("context1");
        c.setItems(new ArrayList<Item>());

        Item i = new Item();
        i.setKey("Легковые автомобили");
        i.setTranslations(new ArrayList<Translation>());

        Translation t = new Translation();
        t.setLocale("en");
        t.setValue("Cars in english");
        i.getTranslations().add(t);

        t = new Translation();
        t.setLocale("fr");
        t.setValue("Cars in french");
        i.getTranslations().add(t);

        c.getItems().add(i);
        i18n.getContexts().add(c);

        i = new Item();
        i.setKey("Легковые автомобили");
        i.setTranslations(new ArrayList<Translation>());

        t = new Translation();
        t.setLocale("en");
        t.setValue("Cars in english");
        i.getTranslations().add(t);

        t = new Translation();
        t.setLocale("fr");
        t.setValue("Cars in french");
        i.getTranslations().add(t);
        i18n.setItems(new ArrayList<Item>());
        i18n.getItems().add(i);

        XStream xStream = new XStream();
        xStream.processAnnotations(new Class[]{
                I18n.class,
                Context.class,
                Item.class,
                Translation.class
        });
        xStream.toXML(i18n, new FileWriter("c:\\test.xml"));
    }
}
