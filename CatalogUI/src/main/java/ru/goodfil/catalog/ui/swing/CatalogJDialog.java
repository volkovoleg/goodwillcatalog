package ru.goodfil.catalog.ui.swing;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.utils.Assert;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Базовый класс для всех форм проекта.
 * @author sazonovkirill@gmail.com
 * @version $Id: CatalogJDialog.java 95 2012-09-23 06:52:24Z sazonovkirill $
 */
public class CatalogJDialog extends JDialog {
    /**
     * Результат, с которым закрывается окно (ОК/Cancel/Yes/No/etc).
     * @see DialogResult
     */
    private DialogResult dialogResult = DialogResult.CANCEL;

    /**
     * Способ открытия окна: для редактирования, для просмотра, etc.
     * @see EditMode
     */
    protected EditMode editMode = EditMode.UNDEFINED;

    /**
     * Результат, с которым закрывается окно (ОК/Cancel/Yes/No/etc).
     * @see DialogResult
     */
    public DialogResult getDialogResult() {
        return dialogResult;
    }

    /**
     * Способ открытия окна: для редактирования, для просмотра, etc.
     * @see EditMode
     */
    public EditMode getEditMode() {
        return editMode;
    }

    /**
     * Способ открытия окна: для редактирования, для просмотра, etc.
     * @see EditMode
     */
    public void setEditMode(EditMode editMode) {
        this.editMode = editMode;
    }

    /**
     * Результат, с которым закрывается окно (ОК/Cancel/Yes/No/etc).
     * @see DialogResult
     */
    protected void setDialogResult(DialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    protected CatalogJDialog() {
    }

    protected CatalogJDialog(Frame owner) {
        super(owner);
    }

    protected CatalogJDialog(Dialog owner) {
        super(owner);
    }

    protected CatalogJDialog(Window owner) {
        super(owner);
    }

    /**
     * Закрыть окно, указав при этом результат как DialogResult.CANCEL.
     * @see DialogResult
     */
    @Override
    public void dispose() {
        setDialogResult(DialogResult.CANCEL);
        super.dispose();
    }

    /**
     * Закрыть окно, указав при этом результат закрытия.
     * @see DialogResult
     */
    public void dispose(DialogResult dialogResult) {
        setDialogResult(dialogResult);
        super.dispose();
    }
    
    //
    //  Валидация полей формы
    //

    private final Set<JComponent> componentsWithErrors = new HashSet<JComponent>();

    protected void clearFormErrors() {
        componentsWithErrors.clear();
    }

    protected boolean hasErrors() {
        return componentsWithErrors.size() > 0;
    }

    protected <T extends JComponent> void rule(@NotNull final T component, @NotNull final ValidationRule<T>... rules) {
        Assert.notNull(component, rules);
        
        for (ValidationRule rule : rules) {
            rule.apply(component);
        }
    }
    
    protected abstract class ValidationRule <T extends JComponent> {
        private String errorMsg;
        
        public ValidationRule() {}

        public ValidationRule(@NotNull @NotBlank final String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public ValidationRule errorMsg(@NotNull @NotBlank final String errorMsg) {
            setErrorMsg(errorMsg);
            return this;
        }
        
        public void apply(T component) {
            if (isCorrect(component)) {
                if (!componentsWithErrors.contains(component)) {
                    applyOkState(component);
                }
            } else {
                applyErrorState(component);
                componentsWithErrors.add(component);
            }
        }
        
        protected abstract boolean isCorrect(T component);
        
        protected void applyOkState(T component) {
            component.setBackground(UIUtils.COLOR_GOOD);
            component.setToolTipText(null);
        }
        
        protected void applyErrorState(T component) {
            component.setBackground(UIUtils.COLOR_BAD);
            if (!StringUtils.isBlank(errorMsg)) {
                component.setToolTipText(errorMsg);
            } else {
                component.setToolTipText(null);
            }
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

    protected ValidationRule<JTextField> unqie(final String errorMsg, final Set<String> items) {
        return new ValidationRule<JTextField>(errorMsg) {
            @Override
            protected boolean isCorrect(@NotNull final JTextField component) {
                String text = component.getText();
                return !items.contains(text);
            }
        };
    }
    
    protected ValidationRule<JTextField> notEmpty() {
        return new ValidationRule<JTextField>() {
            @Override
            protected boolean isCorrect(@NotNull final JTextField component) {
                return !StringUtils.isBlank(component.getText());
            }
        };
    }

    protected ValidationRule<JTextField> notEmpty(final String errorMsg) {
        return new ValidationRule<JTextField>(errorMsg) {
            @Override
            protected boolean isCorrect(@NotNull final JTextField component) {
                return !StringUtils.isBlank(component.getText());
            }
        };
    }

    //
    //  end of Валидация полей формы 
    //
}
