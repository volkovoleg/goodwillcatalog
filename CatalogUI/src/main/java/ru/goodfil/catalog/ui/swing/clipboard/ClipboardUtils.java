package ru.goodfil.catalog.ui.swing.clipboard;

import ru.goodfil.catalog.ui.swing.UIUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ClipboardUtils.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class ClipboardUtils {
    public static String read() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);

        if (transferable == null) {
            UIUtils.error("Буфер обмена пуст!");
            return null;
        }

        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {

            try {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException e) {
                UIUtils.error("В буфере обмена нет текстовых данных");
            } catch (IOException e) {
                UIUtils.error("Невозможно прочесть данные из буфера обмена");
            }

        } else {
            UIUtils.error("В буфере обмена нет подходящих данных");
            return null;
        }
        return null;
    }
}
