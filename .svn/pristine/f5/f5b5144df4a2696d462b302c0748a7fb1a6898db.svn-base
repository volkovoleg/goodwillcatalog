package ru.goodfil.catalog.runner;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.catalina.startup.Bootstrap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * The main class of CatalogRunner, utility, intended to run tomcat with embeded jdk and h2 database andcatalog application on any platform (XP/Vista/7).
 * @author sazonovkirill@gmail.com
 * @version $Id: Application.java 97 2012-09-28 10:24:21Z sazonovkirill $
 */
public class Application {
    private static final Object mutex = new Object();

    private static boolean processExists() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("tasklist.exe /v /fo csv");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (line.contains("Goodwill") && line.contains("java")) {
                    System.out.println("Catalog already started");
                    return true;
                }
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) throws Exception, InterruptedException {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (Exception e) {
        }

        if (processExists()) {
            JOptionPane.showMessageDialog(null, "Приложение уже запущено.", "Внимание!", JOptionPane.ERROR_MESSAGE);
            openBrowser();
        } else {
            TrayIcon trayIcon = installTrayIcon();

            Splash splash = new Splash();
            splash.setUndecorated(true);
            splash.setVisible(true);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.setCatalinaBase("apache-tomcat-catalog");
            bootstrap.start();

            splash.setVisible(false);
            openBrowser();

            synchronized (mutex) {
                mutex.wait();
            }

            bootstrap.stop();

            uninstallTrayIcon(trayIcon);
            System.exit(0);
        }
    }

    // region Tray icon management

    private static TrayIcon installTrayIcon() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem closeAppMenuItem = new MenuItem("Exit");
        closeAppMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (mutex) {
                    mutex.notify();
                }
            }
        });

        popupMenu.add(closeAppMenuItem);
        TrayIcon trayicon = new TrayIcon(createImage("icon.gif", "Catalog"));
        trayicon.setPopupMenu(popupMenu);

        try {
            SystemTray.getSystemTray().add(trayicon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        return trayicon;
    }

    private static void uninstallTrayIcon(TrayIcon trayIcon) {
        SystemTray.getSystemTray().remove(trayIcon);
    }

    // endregion

    protected static void openBrowser() {
        final String URL = "http://localhost:8081/CatalogWeb";

        try {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(URL));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = Application.class.getClassLoader().getResource(path);
        return new ImageIcon(imageURL, description).getImage();
    }
}
