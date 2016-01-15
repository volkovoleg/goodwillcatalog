/*
 * Created by JFormDesigner on Sun Sep 04 19:27:18 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import ru.goodfil.catalog.ui.swing.UIUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ExceptionWindow.java 126 2013-03-06 12:45:25Z chezxxx@gmail.com $
 */
public class ExceptionWindow extends JDialog {
    private final String timestamp;

    public ExceptionWindow(Exception e) {
        initComponents();

        StringBuilder sb = new StringBuilder();
        sb.append("---MESSAGE---\n");
        sb.append(e.toString() + "\n" + e.getMessage() + "\n");
        sb.append("\n---LOCAL STACK TRACE---\n");
        sb.append(getLocalStackTrace(e) + "\n");
        sb.append("\n---FULL STACK TRACE---\n");
        sb.append(getFullStackTrace(e) + "\n");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        timestamp = sdf.format(calendar.getTime());
        tbError.setText(sb.toString());
    }

    private String getLocalStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] elems = e.getStackTrace();
        for (StackTraceElement elem : elems) {
            if (elem.getClassName().startsWith("ru.goodfil.catalog")) {
                sb.append(elem.getClassName() + "." + elem.getMethodName() + " (" + elem.getFileName() + ":" + elem.getLineNumber() + ")\n");
            }
        }
        return sb.toString();
    }

    private String getFullStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] elems = e.getStackTrace();
        for (StackTraceElement elem : elems) {
            sb.append(elem.getClassName() + "." + elem.getMethodName() + " (" + elem.getFileName() + ":" + elem.getLineNumber() + ")\n");
        }
        return sb.toString();
    }

    private void btnOkActionPerformed(ActionEvent e) {
        try {
            String text = tbError.getText();
            String filename = generateAttachmentFile(text);

            sendEmailWithAttachment(SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD,
                    DEVELOPER_MAIL, SUBJECT + timestamp, TEXT, filename);
            UIUtils.info("Очет об ошибке отправлен на эл. почту разработчика.");
            dispose();
        } catch (Exception e2) {
            e2.printStackTrace();
            UIUtils.warning("Не удается отправить отчет об ошибке по эл.почте. Скопируйте текст ошибки и отправьте вручную.");
        }
    }

    private void btnCancelActionPerformed(ActionEvent e) {
        dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        contentPanel = new JPanel();
        label3 = new JLabel();
        label1 = new JLabel();
        label2 = new JLabel();
        scrollPane1 = new JScrollPane();
        tbError = new JTextArea();
        panel1 = new JPanel();
        btnOk = new JButton();
        btnCancel = new JButton();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setModal(true);
        setTitle("\u041e\u0439! \u041a \u0441\u043e\u0436\u0430\u043b\u0435\u043d\u0438\u044e \u043f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430.");
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
                "default:grow, $rgap",
                "$rgap, fill:default:grow"));

        //======== contentPanel ========
        {
            contentPanel.setLayout(new FormLayout(
                    "$rgap, default, default:grow",
                    "12dlu, 10dlu, fill:73dlu:grow, $rgap, default"));

            //---- label3 ----
            label3.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/stop.png")));
            contentPanel.add(label3, cc.xywh(2, 1, 1, 2));

            //---- label1 ----
            label1.setText("\u0422\u0435\u043a\u0441\u0442, \u0440\u0430\u0441\u043f\u043e\u043b\u043e\u0436\u0435\u043d\u044b\u0439 \u043d\u0438\u0436\u0435 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u043e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a\u0443 \u043d\u0430 \u044d\u043b.\u043f\u043e\u0447\u0442\u0443.");
            contentPanel.add(label1, cc.xy(3, 1));

            //---- label2 ----
            label2.setText("\u0412 \u0441\u043a\u043e\u0440\u043e\u043c \u0432\u0440\u0435\u043c\u0435\u043d\u0438 \u044d\u0442\u0430 \u043e\u0448\u0438\u0431\u043a\u0430 \u0431\u0443\u0434\u0435\u0442 \u043e\u0431\u044f\u0437\u0430\u0442\u0435\u043b\u044c\u043d\u043e \u0438\u0441\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u0430.");
            contentPanel.add(label2, cc.xy(3, 2));

            //======== scrollPane1 ========
            {

                //---- tbError ----
                tbError.setBackground(Color.white);
                tbError.setEditable(false);
                tbError.setAutoscrolls(false);
                tbError.setToolTipText("\u041e\u0442\u0447\u0435\u0442 \u043e\u0431 \u043e\u0448\u0438\u0431\u043a\u0435");
                scrollPane1.setViewportView(tbError);
            }
            contentPanel.add(scrollPane1, cc.xywh(2, 3, 2, 1));

            //======== panel1 ========
            {
                panel1.setLayout(new FormLayout(
                        "default:grow, 2*($lcgap, default)",
                        "default"));

                //---- btnOk ----
                btnOk.setText("\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u043e\u0442\u0447\u0435\u0442");
                btnOk.setFont(btnOk.getFont().deriveFont(btnOk.getFont().getStyle() | Font.BOLD));
                btnOk.setToolTipText("\u041e\u0442\u043f\u0440\u0430\u0432\u0438\u0442\u044c \u043e\u0442\u0447\u0435\u0442 \u043e\u0431 \u043e\u0448\u0438\u0431\u043a\u0435 \u0440\u0430\u0437\u0440\u0430\u0431\u043e\u0442\u0447\u0438\u043a\u0443 \u043f\u043e \u044d\u043b.\u043f\u043e\u0447\u0442\u0435");
                btnOk.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnOkActionPerformed(e);
                    }
                });
                panel1.add(btnOk, cc.xy(3, 1));

                //---- btnCancel ----
                btnCancel.setText("\u041d\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0442\u044c");
                btnCancel.setToolTipText("\u041d\u0435 \u043e\u0442\u043f\u0440\u0430\u0432\u043b\u044f\u0442\u044c \u043e\u0442\u0447\u0435\u0442");
                btnCancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCancelActionPerformed(e);
                    }
                });
                panel1.add(btnCancel, cc.xy(5, 1));
            }
            contentPanel.add(panel1, cc.xy(3, 5));
        }
        contentPane.add(contentPanel, cc.xy(1, 2));
        setSize(585, 390);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private final static String SMTP_HOST = "smtp.yandex.ru";
    private final static String SMTP_PORT = "25";
    private final static String SMTP_USERNAME = "goodwill-robot@yandex.ru";
    private final static String SMTP_PASSWORD = "1570920";
    private final static String DEVELOPER_MAIL = "Alfa-11@goodfil.com";
    private final static String SUBJECT = "Ошибка в приложении \"Каталог\" ";
    private final static String TEXT = "Внимание! В приложении \"Каталог\" произошла ошибка. Текст ошибки приладывается.";

    private static String generateAttachmentFile(String text) throws IOException {


        File tempFile = File.createTempFile("error", ".txt");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(text);
        writer.close();

        return tempFile.getAbsolutePath();
    }

    private static void sendEmailWithAttachment(final String smtpHost, final String port, final String username, final String password, String email, String caption, String body, String attachmentFile) throws MessagingException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", new Integer(port));
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        String[] emails = email.split(";");
        for (String em : emails) {
            em = em.trim();
            if (em.equals("")) {
                continue;
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setSubject(caption, "UTF-8");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body, "UTF-8");
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            DataSource source = new FileDataSource(attachmentFile);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("error.txt");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(em));

            Transport.send(message);
        }
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel contentPanel;
    private JLabel label3;
    private JLabel label1;
    private JLabel label2;
    private JScrollPane scrollPane1;
    private JTextArea tbError;
    private JPanel panel1;
    private JButton btnOk;
    private JButton btnCancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
