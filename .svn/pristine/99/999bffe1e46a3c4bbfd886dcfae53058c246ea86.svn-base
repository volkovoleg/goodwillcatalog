package ru.goodfil.catalog.ui;

import com.jgoodies.looks.windows.WindowsLookAndFeel;
import ru.goodfil.catalog.gc.Gc;
import ru.goodfil.catalog.ui.forms.ExceptionWindow;
import ru.goodfil.catalog.ui.forms.MainWindow;
import ru.goodfil.catalog.ui.swing.MessagesRenderer;
import ru.goodfil.catalog.ui.swing.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: Application.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                } catch (Exception e) {
                }


                try {
                    int result = new Gc(Services.getSessionProvider()).collect();
                    if (result > 0) {
                        UIUtils.info("Произведена очистка базы: " + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    UIUtils.warning("Не удалось произвести очистку БД");
                }

                MessagesRenderer.setErrorIcon(new ImageIcon("/ru/neoflex/catalog/ui/error.png"));
                MessagesRenderer.setWarningIcon(new ImageIcon("/ru/neoflex/catalog/ui/warning.png"));
                MessagesRenderer.setInfoIcon(new ImageIcon("/ru/neoflex/catalog/ui/information.png"));

                EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
                queue.push(new EventQueueProxy());

                MainWindow applicationWindow = new MainWindow();
                applicationWindow.setSize(1024, 768);
                applicationWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                applicationWindow.setLocationRelativeTo(null);
                applicationWindow.setVisible(true);
            }
        });
    }

    public static class EventQueueProxy extends EventQueue {
        @Override
        protected void dispatchEvent(AWTEvent event) {
            try {
                super.dispatchEvent(event);
            } catch (Exception e) {
                ExceptionWindow exceptionWindow = new ExceptionWindow(e);
                exceptionWindow.setVisible(true);
            }
        }
    }
}