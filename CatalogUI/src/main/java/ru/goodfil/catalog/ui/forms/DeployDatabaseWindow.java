/*
 * Created by JFormDesigner on Thu Dec 08 13:11:24 MSK 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.io.FileUtils;
import ru.goodfil.catalog.applications.webcatalogupdater.WebCatalogUpdater;
import ru.goodfil.catalog.services.PropertiesService;
import ru.goodfil.catalog.services.SearchAutoIndexService;
import ru.goodfil.catalog.services.SettingsService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.utils.SessionFactoryHolder;
import ru.goodfil.catalog.utils.SessionProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: DeployDatabaseWindow.java 123 2013-02-15 12:20:38Z chezxxx@gmail.com $
 */
public class DeployDatabaseWindow extends JDialog {
    private final SettingsService settingsService = Services.getSettingService();
    private final SearchAutoIndexService searchAutoIndexService = Services.getSearchAutoIndexService();

    public DeployDatabaseWindow(Frame owner) {
        super(owner);
        initComponents();
    }

    public DeployDatabaseWindow(Dialog owner) {
        super(owner);
        initComponents();
    }

    /**
     * Копирование базы в каталог.
     */
    private void btnDeployDatabaseActionPerformed() {
        btnDeployDatabase.setVisible(false);
        final SessionProvider sessionProvider = Services.getSessionProvider();
        final Vector<String> logs = new Vector<String>();
        String databasePath = SessionFactoryHolder.getDatabasePathWithPrefix();
        ;

        logs.add("Исходный файл: " + databasePath);
        File database = new File(databasePath);

        if (!database.isFile() || !database.exists()) {
            logs.add("ОШИБКА: исходный файл указан неверно!");
        } else {
            String catalogExchangePath = Services.getPropertiesService().getProperty(PropertiesService.CATALOG_WEB_EXCHANGE_PATH);
            logs.add("Целевой каталог: " + catalogExchangePath);

            File catalogExchangeDirectory = new File(catalogExchangePath);
            if (!catalogExchangeDirectory.isDirectory() || !catalogExchangeDirectory.exists()) {
                logs.add("ОШИБКА: целевой каталог указан неверно!");
            } else {
                String databaseName = WebCatalogUpdater.DATABASE_NAME;
                File targetFile = new File(catalogExchangeDirectory, databaseName);

                try {
                    logs.add("Построение инденкса");
                    searchAutoIndexService.setSessionProvider(sessionProvider);
                    searchAutoIndexService.build();

                    logs.add("Копирование файла...");
                    settingsService.set("deploy.date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));

                    sessionProvider.close();
                    FileUtils.copyFile(database, targetFile);
                    logs.add("Копирование завершено");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    logs.add("Ошибка: " + e1);
                    UIUtils.error("Не удалось сохранить файл!");
                }

                sessionProvider.init();
            }
        }




        lstLogs.setListData(logs);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Oleg V
        label1 = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        lstLogs = new JList();
        btnDeployDatabase = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0431\u0430\u0437\u044b \u043d\u0430 \u0441\u0430\u0439\u0442");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "default:grow",
            "2*(default, $lgap), fill:default:grow, $lgap, default"));

        //---- label1 ----
        label1.setText("\u0421\u0435\u0439\u0447\u0430\u0441 \u0431\u0430\u0437\u0430 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u0430 \u043d\u0430 \u0441\u0430\u0439\u0442.");
        contentPane.add(label1, cc.xy(1, 1));

        //---- label2 ----
        label2.setText("\u041e\u0431\u044b\u0447\u043d\u043e \u043d\u0430 \u044d\u0442\u043e \u0443\u0445\u043e\u0434\u0438\u0442 1-2 \u043c\u0438\u043d\u0443\u0442\u044b.");
        contentPane.add(label2, cc.xy(1, 3));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(lstLogs);
        }
        contentPane.add(scrollPane1, cc.xy(1, 5));

        //---- btnDeployDatabase ----
        btnDeployDatabase.setText("\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u0431\u0430\u0437\u0443 \u043d\u0430 \u0441\u0430\u0439\u0442");
        btnDeployDatabase.setFont(btnDeployDatabase.getFont().deriveFont(btnDeployDatabase.getFont().getStyle() | Font.BOLD));
        btnDeployDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDeployDatabaseActionPerformed();
            }
        });
        contentPane.add(btnDeployDatabase, cc.xy(1, 7));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Oleg V
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JList lstLogs;
    private JButton btnDeployDatabase;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
