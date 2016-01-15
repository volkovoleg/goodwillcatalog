/*
 * Created by JFormDesigner on Tue Sep 20 15:29:22 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import javax.swing.event.*;

import com.google.inject.Inject;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.lang.StringUtils;
import ru.goodfil.catalog.adapters.FilterAdapter;
import ru.goodfil.catalog.adapters.FiltersAndOesAdapter;
import ru.goodfil.catalog.adapters.OeAdapter;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.FiltersAndOes;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.cellrenderer.MannListsCellRenderer;
import ru.goodfil.catalog.ui.swing.DialogResult;
import ru.goodfil.catalog.ui.swing.ListAdapter;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.utils.Assert;
import ru.goodfil.catalog.utils.JoinOptions;
import ru.goodfil.catalog.utils.ListAsMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.validation.constraints.NotNull;
import java.awt.event.*;
import java.util.*;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: OePanel.java 164 2013-09-06 12:30:45Z chezxxx@gmail.com $
 */
@Managed
public class OePanel extends JPanel {
    private AnalogsService analogsService = Services.getAnalogsService();

    private final ListAdapter<Brand> brands;
    private final ListAdapter<Oe> oes;

    private final int OPERATION_COPY=1;
    private final int OPERATION_CUT=2;
    private final int OPERATION_CLEAR=0;
    private int operation=0;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public OePanel() {
        initComponents();
        brands = new ListAdapter<Brand>(Brand.class, lstBrands);
        oes = new ListAdapter<Oe>(Oe.class, lstOes);
        //В зависимости от параметра подсвечивать или нет строки из манна
        if (System.getProperty("catalog.mode.fromMann") != null && System.getProperty("catalog.mode.fromMann").equals("1")) {
            lstBrands.setCellRenderer(new MannListsCellRenderer(MannListsCellRenderer.BRAND));
            lstOes.setCellRenderer(new MannListsCellRenderer(MannListsCellRenderer.OE));
        }
        reReadBrands();
        adjustButtonsEnabled();
        adjustLstBrandsStatus();
        adjustLstOesStatus();
    }

    private void reReadBrands() {
        brands.clear();
        brands.addAll(analogsService.getBrands());

        adjustLstBrandsStatus();
    }

    private void reReadBrands(String name) {
        if (StringUtils.isBlank(name)) {
            reReadBrands();
        } else {

            brands.clear();
            brands.addAll(analogsService.searchBrandsByName(name));

            adjustLstBrandsStatus();
        }
    }

    private void reReadOes(Long brandId) {
        oes.clear();

        if (brandId != null) {
            List<Oe> theOes = analogsService.getOesByBrand(brandId);

            //
            // EXPERIMENTAL
            //
            //  Сделаем здесь следующее: будем удалять из базы дублирющиеся ОЕ
            //
            {
                Set<Long> oesToDelete = new HashSet<Long>();
                Set<String> oesHash = new HashSet<String>();

                for (Oe oe : theOes) {
                    String oeHash = oe.getName().replaceAll(" ", "").replaceAll(" ","") + String.valueOf(oe.getBrandId());
                    if (oesHash.contains(oeHash)) {
                        oesToDelete.add(oe.getId());
                    } else {
                        oesHash.add(oeHash);
                    }
                }

/*                List<FiltersAndOes> list = analogsService.getByOeIds(oesToDelete);
                System.out.println("Removed filtersandoes: " + list.size());*/

                if (oesToDelete.size() > 0) {
                  //  analogsService.deleteByOeIds(oesToDelete);
                    analogsService.removeOes(oesToDelete);
                    theOes = analogsService.getOesByBrand(brandId);
                    System.out.println("Removed oes: " + oesToDelete.size());
                }
            }

            oes.addAll(theOes);
        }

        adjustLstOesStatus();
    }

    private void reReadOes(Long brandId, String name) {
        oes.clear();

        if (brandId != null) {
            if (StringUtils.isBlank(name)) {
                oes.addAll(analogsService.getOesByBrand(brandId));
            } else {
                oes.addAll(analogsService.searchOesByBrandAndName(brandId, name));
            }
        }

        adjustLstOesStatus();
    }


    private void lstBrandsValueChanged(ListSelectionEvent e) {
        if (e != null && e.getValueIsAdjusting()) return;

        lstOes.clearSelection();
        oes.clear();

        Long selectedBrandId = brands.getSelectedItemId();
        if (selectedBrandId != null) {
            reReadOes(selectedBrandId);
        }

        adjustLstBrandsStatus();
        adjustButtonsEnabled();
    }

    private void adjustLstBrandsStatus() {
        lLstBrandsStatus.setText(String.format("Брэнды: %d из %d", brands.getSelectedCount(), brands.getCount()));
    }

    private void adjustLstOesStatus() {
        lLstOesStatus.setText(String.format("Номера ОЕ: %d из %d", oes.getSelectedCount(), oes.getCount()));
    }

    private void adjustButtonsEnabled() {
        btnCreateBrand.setEnabled(true);
        btnEditBrand.setEnabled(brands.isOneSelected());
        btnRemoveBrand.setEnabled(brands.isOneSelected());
        btnUnionBrand.setEnabled(brands.isMultipleSelected());

        btnCreateOe.setEnabled(brands.isOneSelected());
        btnEditOe.setEnabled(brands.isOneSelected() && oes.isOneSelected());
        btnRemoveOe.setEnabled(brands.isOneSelected() && oes.isOneSelected());
        btnUnionOe.setEnabled(brands.isOneSelected() && oes.isMultipleSelected());

        btnSearchBrand.setEnabled(!StringUtils.isBlank(tbSearchBrand.getText()));
        btnSearchOe.setEnabled(brands.isOneSelected() && !StringUtils.isBlank(tbSearchOe.getText()));
    }

    private void btnCreateBrandActionPerformed(ActionEvent e) {
        String name = UIUtils.askName();
        if (!StringUtils.isBlank(name)) {

            Brand brand = Brand.create(name);
            analogsService.addBrand(brand);

            reReadOes(null);
            reReadBrands();
        }

        adjustButtonsEnabled();
    }

    private void btnEditBrandActionPerformed(ActionEvent e) {
        if (brands.isOneSelected()) {
            Brand brand = brands.getSelectedItem();
            String name = UIUtils.askName(brand.getName());

            if (!StringUtils.isBlank(name)) {
                brand.setName(name);
                analogsService.updateBrand(brand);

                reReadOes(null);
                reReadBrands();
            }
        }

        adjustButtonsEnabled();
    }

    private void btnRemoveBrandActionPerformed(ActionEvent e) {
        if (brands.isOneSelected()&&UIUtils.askDelete()) {
            List<Oe> deltaOe= analogsService.getOesByBrand(brands.getSelectedItem().getId());
            for(Oe simpleOe:deltaOe){
               analogsService.removeOe(simpleOe.getId());
            }
            analogsService.removeBrand(brands.getSelectedItemId());
            reReadOes(null);
            reReadBrands();
        }

        adjustButtonsEnabled();
    }

    private void btnSearchBrandActionPerformed(ActionEvent e) {
        reReadBrands(tbSearchBrand.getText());
        reReadOes(null);

        adjustButtonsEnabled();
    }

    private void lstBrandsKeyPressed(KeyEvent e) {
        if (e != null) {
            if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                btnCreateBrandActionPerformed(null);
            }
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                btnRemoveBrandActionPerformed(null);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                btnEditBrandActionPerformed(null);
            }
        }
    }

    private void tbSearchBrandKeyTyped(KeyEvent e) {
        if (e != null) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                tbSearchBrand.setText("");
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnSearchBrandActionPerformed(null);
            }
        }

        adjustButtonsEnabled();
    }

    private void btnCreateOeActionPerformed(ActionEvent e) {
        String name = UIUtils.askName();
        if (!StringUtils.isBlank(name)) {

            Oe oe = Oe.create(brands.getSelectedItemId(), name);
            analogsService.addOe(oe);

            reReadOes(brands.getSelectedItemId());
        }

        adjustButtonsEnabled();
    }

    private void btnEditOeActionPerformed(ActionEvent e) {
        if (oes.isOneSelected()) {
            Oe oe = oes.getSelectedItem();
            String name = UIUtils.askName(oe.getName());

            if (!StringUtils.isBlank(name)) {
                oe.setName(name);
                analogsService.updateOe(oe);

                reReadOes(brands.getSelectedItemId());
            }
        }

        adjustButtonsEnabled();
    }

    private void btnRemoveOeActionPerformed(ActionEvent e) {
        if (oes.isOneSelected()&&UIUtils.askDelete()) {
            analogsService.removeOe(oes.getSelectedItemId());

            reReadOes(brands.getSelectedItemId());
        }

        adjustButtonsEnabled();
    }

    private void btnSearchOeActionPerformed(ActionEvent e) {
        reReadOes(brands.getSelectedItemId(), tbSearchOe.getText());

        adjustButtonsEnabled();
    }

    private void lstOesKeyPressed(KeyEvent e) {
        if (e != null) {
            if (e.getKeyCode() == KeyEvent.VK_INSERT) {
                btnCreateOeActionPerformed(null);
            }
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                btnRemoveOeActionPerformed(null);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                btnEditOeActionPerformed(null);
            }
            if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                List<Oe> selectedOes = oes.getSelectedItems();
                putObjectToMyClipboard(selectedOes);
                this.setOperation(OPERATION_CUT);
            }
            if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                List<Oe> selectedOes = oes.getSelectedItems();
                putObjectToMyClipboard(selectedOes);
                this.setOperation(OPERATION_COPY);
            }
            if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                if (popupMenu1.getInvoker() == lstOes) {
                    List<Oe> selectedOes = new ArrayList();
                    selectedOes.addAll(getFromClipboardByType(Oe.class));
                    if (selectedOes.size() > 0) {
                        Long brand = brands.getSelectedItemId();
                        Assert.notNull(brand);
                        if (getOperation() == OPERATION_COPY) {
                            analogsService.doCopyOe(selectedOes, brand);
                        }
                        if (getOperation() == OPERATION_CUT) {
                            analogsService.doCutOe(selectedOes, brand);
                        }
                        reReadOes(brand);
                    }
                }
            }
        }
    }

    private void tbSearchOeKeyPressed(KeyEvent e) {
        if (e != null) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                tbSearchOe.setText("");
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                btnSearchOeActionPerformed(null);
            }
        }

        adjustButtonsEnabled();
    }

    private void tbSearchOeFocusGained(FocusEvent e) {
        tbSearchOeKeyPressed(null);
    }

    private void tbSearchBrandFocusGained(FocusEvent e) {
        tbSearchBrandKeyTyped(null);
    }

    private void lstOesValueChanged(ListSelectionEvent e) {
        adjustButtonsEnabled();
    }

    /**
     * Операция объединения брэндов.
     */
    private void btnUnionBrandActionPerformed(ActionEvent e) {
        List<Brand> selectedBrands = brands.getSelectedItems();
        if (selectedBrands.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(selectedBrands);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            analogsService.doJoinBrands(masterItemId, slavesItemsIds, joinOptions);
            reReadBrands();
        }
    }

    /**
     * Операция объединения ое.
     */
    private void btnUnionOeActionPerformed(ActionEvent e) {
        List<Oe> selectedOes = oes.getSelectedItems();
        if (selectedOes.size() < 2) {
            UIUtils.warning("Для объединения необходимо выбрать более одной позиции");
            return;
        }

        JoinWindow joinWindow = new JoinWindow();
        joinWindow.setItems(selectedOes);
        joinWindow.setVisible(true);

        DialogResult dr = joinWindow.getDialogResult();
        JoinOptions joinOptions = joinWindow.getJoinOptions();
        if (dr == DialogResult.YES) {
            //  Выполняем объединение
            Long masterItemId = joinWindow.getMasterId();
            Set<Long> slavesItemsIds = joinWindow.getSlavesIds();

            analogsService.doJoinOes(masterItemId, slavesItemsIds, joinOptions);
            reReadBrands();
        }
    }

    private void putObjectToMyClipboard(List<Oe> c) {
        ru.goodfil.catalog.ui.utils.Clipboard.getInstance().put(c);
    }

    private <T> List<T> getFromClipboardByType(Class<T> klass) {
        List result = new ArrayList();
        Iterator i = ru.goodfil.catalog.ui.utils.Clipboard.getInstance().get().iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o.getClass().equals(klass)) {
                result.add(o);
            }
        }

        return result;
    }

    private void copyToClipboard(ActionEvent e) {
        List<Oe> selectedOes = oes.getSelectedItems();
        putObjectToMyClipboard(selectedOes);
        this.setOperation(OPERATION_COPY);
    }

    private void cutToClipboard(ActionEvent e) {
        List<Oe> selectedOes = oes.getSelectedItems();
        putObjectToMyClipboard(selectedOes);
        this.setOperation(OPERATION_CUT);
    }

    private void popupMenu1PopupMenuWillBecomeVisible(PopupMenuEvent e) {
        menuItem2.setEnabled(getOperation() == OPERATION_COPY);
        menuItem4.setEnabled(getOperation() == OPERATION_CUT);
    }

    private void lstOesMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (oes.size() > 0) {
                popupMenu1.show(lstOes, e.getX(), e.getY());
            }
        }
    }

    private void pasteFromClipboard(ActionEvent e) {
        if (popupMenu1.getInvoker() == lstOes) {
            List<Oe> selectedOes = new ArrayList();
            selectedOes.addAll(getFromClipboardByType(Oe.class));
            if (selectedOes.size() > 0) {
                Long brand = brands.getSelectedItemId();
                Assert.notNull(brand);
                if (getOperation() == OPERATION_COPY) {
                    analogsService.doCopyOe(selectedOes, brand);
                }
                if (getOperation() == OPERATION_CUT) {
                    analogsService.doCutOe(selectedOes, brand);
                }
                reReadOes(brand);
            }
            this.setOperation(OPERATION_CLEAR);
        }
    }

    private void menuItemReprezentInStandalone(ActionEvent e) {
        Brand brand = brands.getSelectedItem();
        if (brand != null) {
            brand.setStandaloneStatus((Integer) 1);
            analogsService.updateBrand(brand);
            reReadBrands();
        }
    }

    private void menuItemDontReprezentInStandalone(ActionEvent e) {
        Brand brand = brands.getSelectedItem();
        if (brand != null) {
            brand.setStandaloneStatus((Integer) 0);
            analogsService.updateBrand(brand);
            reReadBrands();
        }
    }

    private void lstBrandsMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (brands.isOneSelected()) {
                popupMenu2.show(lstBrands, e.getX(), e.getY());
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Ð¢Ð°ÑÑÑÐ½Ð° ÐÐ¾Ð»ÐºÐ¾Ð²Ð°
        panel1 = new JPanel();
        panel2 = new JPanel();
        btnCreateBrand = new JButton();
        btnEditBrand = new JButton();
        btnRemoveBrand = new JButton();
        btnUnionBrand = new JButton();
        hSpacer1 = new JPanel(null);
        tbSearchBrand = new JTextField();
        btnSearchBrand = new JButton();
        scrollPane1 = new JScrollPane();
        lstBrands = new JList();
        lLstBrandsStatus = new JLabel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        btnCreateOe = new JButton();
        btnEditOe = new JButton();
        btnRemoveOe = new JButton();
        btnUnionOe = new JButton();
        hSpacer2 = new JPanel(null);
        tbSearchOe = new JTextField();
        btnSearchOe = new JButton();
        scrollPane2 = new JScrollPane();
        lstOes = new JList();
        lLstOesStatus = new JLabel();
        popupMenu1 = new JPopupMenu();
        menuItem1 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem4 = new JMenuItem();
        popupMenu2 = new JPopupMenu();
        menu1 = new JMenu();
        menuItem5 = new JMenuItem();
        menuItem6 = new JMenuItem();
        CellConstraints cc = new CellConstraints();

        //======== this ========

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

        setLayout(new FormLayout(
            "default:grow, $lcgap, default:grow",
            "fill:default:grow"));

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("\u041f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u0438"));
            panel1.setLayout(new FormLayout(
                "default:grow",
                "fill:21dlu, $lgap, fill:default:grow, $lgap, default"));

            //======== panel2 ========
            {
                panel2.setLayout(new FormLayout(
                    "4*(21dlu), default:grow, 100dlu, 21dlu",
                    "fill:default:grow"));

                //---- btnCreateBrand ----
                btnCreateBrand.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
                btnCreateBrand.setToolTipText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnCreateBrand.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCreateBrandActionPerformed(e);
                    }
                });
                panel2.add(btnCreateBrand, cc.xy(1, 1));

                //---- btnEditBrand ----
                btnEditBrand.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
                btnEditBrand.setToolTipText("\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnEditBrand.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnEditBrandActionPerformed(e);
                    }
                });
                panel2.add(btnEditBrand, cc.xy(2, 1));

                //---- btnRemoveBrand ----
                btnRemoveBrand.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
                btnRemoveBrand.setToolTipText("\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnRemoveBrand.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveBrandActionPerformed(e);
                    }
                });
                panel2.add(btnRemoveBrand, cc.xy(3, 1));

                //---- btnUnionBrand ----
                btnUnionBrand.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
                btnUnionBrand.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnUnionBrandActionPerformed(e);
                    }
                });
                panel2.add(btnUnionBrand, cc.xy(4, 1));
                panel2.add(hSpacer1, cc.xy(5, 1));

                //---- tbSearchBrand ----
                tbSearchBrand.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tbSearchBrandKeyTyped(e);
                    }
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbSearchBrandKeyTyped(e);
                    }
                    @Override
                    public void keyTyped(KeyEvent e) {
                        tbSearchBrandKeyTyped(e);
                    }
                });
                tbSearchBrand.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbSearchBrandFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbSearchBrandFocusGained(e);
                    }
                });
                panel2.add(tbSearchBrand, cc.xy(6, 1));

                //---- btnSearchBrand ----
                btnSearchBrand.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                btnSearchBrand.setToolTipText("\u041f\u043e\u0438\u0441\u043a \u043c\u043e\u0442\u043e\u0440\u043e\u0432 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044f \u0438\u0437 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u0441\u0435\u0440\u0438\u0438");
                btnSearchBrand.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnSearchBrandActionPerformed(e);
                    }
                });
                panel2.add(btnSearchBrand, cc.xy(7, 1));
            }
            panel1.add(panel2, cc.xy(1, 1));

            //======== scrollPane1 ========
            {

                //---- lstBrands ----
                lstBrands.setToolTipText("\u0415\u0441\u043b\u0438 \u0448\u0440\u0438\u0444\u0442 \u0411\u0440\u0435\u043d\u0434\u0430 \u0432\u044b\u0434\u0435\u043b\u0435\u043d \u043f\u043e\u043b\u0443\u0436\u0438\u0440\u043d\u044b\u043c, \u0442\u043e \u043e\u043d \u0431\u0443\u0434\u0435\u0442 \u0432\u0438\u0434\u0435\u043d \u0432 \u043e\u0442\u0447\u0443\u0436\u0434\u0430\u0435\u043c\u043e\u0439 \u043a\u043e\u043f\u0438\u0438, \u0434\u043b\u044f \u0432\u044b\u0433\u0440\u0443\u0437\u043e\u043a. \u0415\u0441\u043b\u0438 \u0436\u0435 \u043d\u0435 \u0432\u044b\u0434\u0435\u043b\u0435\u043d, \u0442\u043e \u0432\u0438\u0434\u0435\u043d \u043d\u0435 \u0431\u0443\u0434\u0435\u0442.");
                lstBrands.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        lstBrandsValueChanged(e);
                    }
                });
                lstBrands.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        lstBrandsKeyPressed(e);
                    }
                });
                lstBrands.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        lstBrandsMouseClicked(e);
                    }
                });
                scrollPane1.setViewportView(lstBrands);
            }
            panel1.add(scrollPane1, cc.xy(1, 3));

            //---- lLstBrandsStatus ----
            lLstBrandsStatus.setText("text");
            panel1.add(lLstBrandsStatus, cc.xy(1, 5));
        }
        add(panel1, cc.xy(1, 1));

        //======== panel3 ========
        {
            panel3.setBorder(new TitledBorder("\u041d\u043e\u043c\u0435\u0440\u0430 \u041e\u0415"));
            panel3.setLayout(new FormLayout(
                "default:grow",
                "default, $lgap, fill:default:grow, $lgap, default"));

            //======== panel4 ========
            {
                panel4.setLayout(new FormLayout(
                    "4*(21dlu), default:grow, 100dlu, 21dlu",
                    "fill:21dlu"));

                //---- btnCreateOe ----
                btnCreateOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/add_24.png")));
                btnCreateOe.setToolTipText("\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnCreateOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnCreateOeActionPerformed(e);
                    }
                });
                panel4.add(btnCreateOe, cc.xy(1, 1));

                //---- btnEditOe ----
                btnEditOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/edit_24.png")));
                btnEditOe.setToolTipText("\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnEditOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnEditOeActionPerformed(e);
                    }
                });
                panel4.add(btnEditOe, cc.xy(2, 1));

                //---- btnRemoveOe ----
                btnRemoveOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/delete_24.png")));
                btnRemoveOe.setToolTipText("\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0444\u0438\u043b\u044c\u0442\u0440");
                btnRemoveOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnRemoveOeActionPerformed(e);
                    }
                });
                panel4.add(btnRemoveOe, cc.xy(3, 1));

                //---- btnUnionOe ----
                btnUnionOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/recycle_24.png")));
                btnUnionOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnUnionOeActionPerformed(e);
                    }
                });
                panel4.add(btnUnionOe, cc.xy(4, 1));
                panel4.add(hSpacer2, cc.xy(5, 1));

                //---- tbSearchOe ----
                tbSearchOe.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        tbSearchOeKeyPressed(e);
                    }
                    @Override
                    public void keyReleased(KeyEvent e) {
                        tbSearchOeKeyPressed(e);
                    }
                });
                tbSearchOe.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        tbSearchOeFocusGained(e);
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        tbSearchOeFocusGained(e);
                    }
                });
                panel4.add(tbSearchOe, cc.xy(6, 1));

                //---- btnSearchOe ----
                btnSearchOe.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/find_next_24.png")));
                btnSearchOe.setToolTipText("\u041f\u043e\u0438\u0441\u043a \u043c\u043e\u0442\u043e\u0440\u043e\u0432 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0433\u043e \u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442\u0435\u043b\u044f \u0438\u0437 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u0441\u0435\u0440\u0438\u0438");
                btnSearchOe.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btnSearchOeActionPerformed(e);
                    }
                });
                panel4.add(btnSearchOe, cc.xy(7, 1));
            }
            panel3.add(panel4, cc.xy(1, 1));

            //======== scrollPane2 ========
            {

                //---- lstOes ----
                lstOes.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        lstOesKeyPressed(e);
                    }
                });
                lstOes.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        lstOesValueChanged(e);
                    }
                });
                lstOes.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        lstOesMouseClicked(e);
                    }
                });
                scrollPane2.setViewportView(lstOes);
            }
            panel3.add(scrollPane2, cc.xy(1, 3));

            //---- lLstOesStatus ----
            lLstOesStatus.setText("text");
            panel3.add(lLstOesStatus, cc.xy(1, 5));
        }
        add(panel3, cc.xy(3, 1));

        //======== popupMenu1 ========
        {
            popupMenu1.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    popupMenu1PopupMenuWillBecomeVisible(e);
                }
            });

            //---- menuItem1 ----
            menuItem1.setText("\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440");
            menuItem1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    copyToClipboard(e);
                }
            });
            popupMenu1.add(menuItem1);

            //---- menuItem2 ----
            menuItem2.setText("\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430");
            menuItem2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pasteFromClipboard(e);
                }
            });
            popupMenu1.add(menuItem2);
            popupMenu1.addSeparator();

            //---- menuItem3 ----
            menuItem3.setText("\u041f\u0435\u0440\u0435\u043c\u0435\u0441\u0442\u0438\u0442\u044c \u0432 \u0431\u0443\u0444\u0435\u0440 (\u0421 \u0443\u0434\u0430\u043b\u0435\u043d\u0438\u0435\u043c)");
            menuItem3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cutToClipboard(e);
                }
            });
            popupMenu1.add(menuItem3);

            //---- menuItem4 ----
            menuItem4.setText("\u0412\u044b\u043d\u0435\u0441\u0442\u0438 \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430");
            menuItem4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pasteFromClipboard(e);
                }
            });
            popupMenu1.add(menuItem4);
        }

        //======== popupMenu2 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u0435 \u0432 \u043e\u0442\u0447\u0443\u0436\u0434\u0430\u0435\u043c\u043e\u0439 \u043a\u043e\u043f\u0438\u0438");

                //---- menuItem5 ----
                menuItem5.setText("\u041e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0442\u044c");
                menuItem5.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuItemReprezentInStandalone(e);
                    }
                });
                menu1.add(menuItem5);

                //---- menuItem6 ----
                menuItem6.setText("\u041d\u0435 \u043e\u0442\u043e\u0431\u0440\u0430\u0436\u0430\u0442\u044c");
                menuItem6.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        menuItemDontReprezentInStandalone(e);
                    }
                });
                menu1.add(menuItem6);
            }
            popupMenu2.add(menu1);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Ð¢Ð°ÑÑÑÐ½Ð° ÐÐ¾Ð»ÐºÐ¾Ð²Ð°
    private JPanel panel1;
    private JPanel panel2;
    private JButton btnCreateBrand;
    private JButton btnEditBrand;
    private JButton btnRemoveBrand;
    private JButton btnUnionBrand;
    private JPanel hSpacer1;
    private JTextField tbSearchBrand;
    private JButton btnSearchBrand;
    private JScrollPane scrollPane1;
    private JList lstBrands;
    private JLabel lLstBrandsStatus;
    private JPanel panel3;
    private JPanel panel4;
    private JButton btnCreateOe;
    private JButton btnEditOe;
    private JButton btnRemoveOe;
    private JButton btnUnionOe;
    private JPanel hSpacer2;
    private JTextField tbSearchOe;
    private JButton btnSearchOe;
    private JScrollPane scrollPane2;
    private JList lstOes;
    private JLabel lLstOesStatus;
    private JPopupMenu popupMenu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JPopupMenu popupMenu2;
    private JMenu menu1;
    private JMenuItem menuItem5;
    private JMenuItem menuItem6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
