package ru.goodfil.catalog.utils;

/**
 * Настройки объединения позиций.
 * @author sazonovkirill@gmail.com
 * @version $Id: JoinOptions.java 110 2012-12-22 09:29:51Z sazonovkirill $
 */
public class JoinOptions {
    private boolean deleteEmptySlaves = true;

    /**
     * Если это значение установлено в true, то после объединения выбранных позиций,
     * все дочерние элементы с одинаковыми именами будут также объединены.
     */
    private boolean recursiveJoinByName = true;

    private JoinOptions() {}

    /**
     * Настройки "по-умолчанию".
     */
    public static JoinOptions defaultOnes() {
        return new JoinOptions();
    }

    public boolean isDeleteEmptySlaves() {
        return deleteEmptySlaves;
    }

    public void setDeleteEmptySlaves(boolean deleteEmptySlaves) {
        this.deleteEmptySlaves = deleteEmptySlaves;
    }

    public boolean isRecursiveJoinByName() {
        return recursiveJoinByName;
    }

    public void setRecursiveJoinByName(boolean recursiveJoinByName) {
        this.recursiveJoinByName = recursiveJoinByName;
    }
}
