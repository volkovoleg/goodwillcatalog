/*
 * Created by JFormDesigner on Tue Sep 20 17:56:10 MSD 2011
 */

package ru.goodfil.catalog.ui.forms;

import javax.swing.border.*;
import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.thoughtworks.xstream.converters.extended.ColorConverter;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import ru.goodfil.catalog.annotations.Managed;
import ru.goodfil.catalog.domain.Brand;
import ru.goodfil.catalog.domain.Filter;
import ru.goodfil.catalog.domain.Oe;
import ru.goodfil.catalog.services.AnalogsService;
import ru.goodfil.catalog.services.CarsService;
import ru.goodfil.catalog.services.FiltersService;
import ru.goodfil.catalog.ui.Services;
import ru.goodfil.catalog.ui.swing.UIUtils;
import ru.goodfil.catalog.ui.swing.clipboard.ClipboardUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.HashSet;
/**
 * @author sazonovkirill@gmail.com
 * @version $Id: ImportOeWindow.java 172 2013-10-26 10:53:33Z chezxxx@gmail.com $
 */
public class ImportOeWindow extends JDialog implements ClipboardOwner  {
    private static final Color LIGHT_BLUE = new Color(227, 236, 227);

    private final AnalogsService analogsService = Services.getAnalogsService();

    private final ArrayListModel<OeModel> oes = new ArrayListModel<OeModel>();

  //  private final ArrayListModel<Filter> filters = new ArrayListModel<Filter>();

    private final Set<String> duplicationBrands = new HashSet<String>();

    private final FiltersService filtersService = Services.getFiltersService();

    private final CarsService carsService = Services.getCarsService();

    public ImportOeWindow(Frame owner) {
        super(owner);
        initComponents();
    }

    public ImportOeWindow(Dialog owner) {
        super(owner);
        initComponents();
    }

    /**
     * Загрузка из буфера обмена
     */
    private void btnImportActionPerformed() {
        String content = ClipboardUtils.read();
        if (content == null) return;

        oes.clear();
        duplicationBrands.clear();

        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] columns = line.split("\t");

            String brand = columns[0];
            String oetxt = columns[1];

            oes.add(new OeModel(brand, oetxt));
        }


        for (OeModel oe : oes) {
            List<Brand> brands = analogsService.getBrandsByName(oe.getBrand());
            if (brands.size() > 1) {
                duplicationBrands.add(oe.getBrand());
            }
            oe.addBrands(brands);

            List<Oe> oes = analogsService.getOesByName(oe.getOe());
            oe.addOes(oes);
        }

        tblOes.setModel(new OeModelTableModel(oes));
        lRowsCount.setText("Всего записей: " + oes.size());
    }

    /**
     * Загрузка OE
     */
    private void btnLoanActionPerformed() {
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Выполняется загрузка ОЕ...", "", 0, 100);
//        progressMonitor.
//        progressMonitor.setProgress(0);

        ImportOeOperation op = new ImportOeOperation(progressMonitor);
        op.execute();
    }

    class ImportOeOperation extends SwingWorker<Void, ImportOperationState> {
        private final ProgressMonitor progressMonitor;
        private Filter connectFilter=null;
        
        ImportOeOperation(ProgressMonitor progressMonitor) {
            this.progressMonitor = progressMonitor;
        }

        ImportOeOperation(ProgressMonitor progressMonitor, Filter filter) {
            this.progressMonitor = progressMonitor;
            this.connectFilter=filter;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Set<Long> oeIds=new HashSet<Long>();
            for (OeModel oeModel : oes) {
                setProgress((100 * oes.indexOf(oeModel)) / oes.size());
                publish(new ImportOperationState(oes.indexOf(oeModel), oes.size(), oeModel.getBrand(), oeModel.getOe()));

                final List<Brand> brands = new ArrayList<Brand>(analogsService.getBrandsByName(oeModel.getBrand().toUpperCase()));
                if (brands.size() == 0) {
                    Brand brand = Brand.create(oeModel.getBrand());
                    analogsService.addBrand(brand);
                    brands.add(brand);
                }

                //  Теперь в каждый из брндов нужно добавить ое
                for (Brand brand : brands) {
                    List<Oe> oess = analogsService.getOesByBrandAndName(brand.getId(), oeModel.getOe());
                    if (!(this.connectFilter==null)){
                        for (Oe oe : oess){
                          oeIds.add(oe.getId());
                        }
                    }
                    //  Если его уже там нет
                    if (oess.size() == 0) {
                        Oe oe = Oe.create(brand.getId(), oeModel.getOe());
                        analogsService.addOe(oe);
                        if (!(this.connectFilter==null)){
                            List<Oe> oeLocal = analogsService.getOesByBrandAndName(brand.getId(), oe.getName());
                            for (Oe o : oeLocal){
                                oeIds.add(o.getId());
                            }
                        }
                    }
                }
                }
            if (!(this.connectFilter==null)){
            carsService.doAttachOesToFilter(connectFilter.getId(), oeIds);
            }
            return null;
        }

        @Override
        protected void done() {
            UIUtils.info("Загрузка завершена!");
            dispose();
        }

        @Override
        protected void process(List<ImportOperationState> chunks) {
            ImportOperationState opState = chunks.get(chunks.size() - 1);

            String note = "Загрузка брэнда " + opState.getBrand() + " ОЕ " + opState.getOe();
            progressMonitor.setNote(note);
            progressMonitor.setProgress(getProgress());
        }
    }

    class ImportOperationState {
        private final int current;
        private final int maximum;
        private final String brand;
        private final String oe;

        ImportOperationState(int current, int maximum, String brand, String oe) {
            this.current = current;
            this.maximum = maximum;
            this.brand = brand;
            this.oe = oe;
        }

        public int getCurrent() {
            return current;
        }

        public int getMaximum() {
            return maximum;
        }

        public String getBrand() {
            return brand;
        }

        public String getOe() {
            return oe;
        }
    }

    /**
     * Метод выделяет конкрентную позицию и и присоединяет к ней все ОЕ
     */
    private void btnLoan2ActionPerformed(ActionEvent e) {
        final ProgressMonitor progressMonitor = new ProgressMonitor(this, "Выполняется загрузка и привязка ОЕ к выбранному фильтру...", "", 0, 100);
        if (!StringUtils.isBlank(textField1.getText())) {
            List <Filter> filters=filtersService.getFilters();
            Filter searchFilter=null;
            String nameFilter=textField1.getText();
            for (Filter filter:filters){
                if(nameFilter.equals(filter.getName())){
                    searchFilter=filter;
                }
            }
            if (searchFilter==null){
                textField1.setBackground(UIUtils.COLOR_BAD);
                textField1.setToolTipText("Нет такой позиции");
            }else{
                textField1.setBackground(Color.WHITE);
                textField1.setToolTipText("Позиция найдена");
                //Тут повторяем функционал загрузки ОЕ, только добавив привязку к фильтрам
                ImportOeOperation op = new ImportOeOperation(progressMonitor,searchFilter);
                op.execute();
            }
        }
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        textPane1 = new JTextPane();
        scrollPane3 = new JScrollPane();
        textPane2 = new JTextPane();
        btnImport = new JButton();
        textField1 = new JTextField();
        btnLoan2 = new JButton();
        scrollPane2 = new JScrollPane();
        tblOes = new JTable();
        btnLoan = new JButton();
        lRowsCount = new JLabel();
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setTitle("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u041e\u0415 \u0438\u0437 \u0444\u0430\u0439\u043b\u0430 Excel");
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FormLayout(
            "$rgap, default, $lcgap, 213dlu:grow, $lcgap, [153dlu,pref], $rgap, default",
            "$rgap, pref, $lgap, fill:default, $lgap, default:grow, $lgap, fill:default, $rgap, default"));

        //======== scrollPane1 ========
        {
            scrollPane1.setBorder(null);

            //---- textPane1 ----
            textPane1.setText("\u0414\u043b\u044f \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0438 \u041e\u0415 \u043d\u0435\u043e\u0431\u0445\u043e\u0434\u0438\u043c\u043e \u0441\u0434\u0435\u043b\u0430\u0442\u044c \u0432 Microsoft Excel \u0442\u0430\u0431\u043b\u0438\u0446\u0443, \u0433\u0434\u0435:\n- \u0412 \u043f\u0435\u0440\u0432\u043e\u043c \u0441\u0442\u043e\u043b\u0431\u0446\u0435 \u043d\u0430\u0438\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u0438\u0435 \u0431\u0440\u044d\u043d\u0434\u0430\n- \u0412\u043e \u0432\u0442\u043e\u0440\u043e\u043c \u0441\u0442\u043e\u043b\u0431\u0446\u0435 \u043d\u043e\u043c\u0435\u0440 \u041e\u0415");
            textPane1.setBorder(null);
            textPane1.setBackground(SystemColor.control);
            scrollPane1.setViewportView(textPane1);
        }
        contentPane.add(scrollPane1, cc.xywh(2, 2, 4, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //======== scrollPane3 ========
        {
            scrollPane3.setBorder(null);

            //---- textPane2 ----
            textPane2.setText("\u041a\u043e\u0434 \u043f\u043e\u0437\u0438\u0446\u0438\u0438 \u0434\u043b\u044f \u0441\u043b\u0438\u044f\u043d\u0438\u044f:");
            textPane2.setBorder(null);
            textPane2.setBackground(SystemColor.control);
            scrollPane3.setViewportView(textPane2);
        }
        contentPane.add(scrollPane3, cc.xy(6, 2, CellConstraints.RIGHT, CellConstraints.TOP));

        //---- btnImport ----
        btnImport.setText("\u0418\u043c\u043f\u043e\u0440\u0442");
        btnImport.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/import1.png")));
        btnImport.setToolTipText("\u0418\u043c\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0438\u0437 \u0431\u0443\u0444\u0435\u0440\u0430 \u043e\u0431\u043c\u0435\u043d\u0430");
        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnImportActionPerformed();
            }
        });
        contentPane.add(btnImport, cc.xy(2, 4));
        contentPane.add(textField1, cc.xy(6, 4));

        //---- btnLoan2 ----
        btnLoan2.setText("\u041f\u0440\u0438\u043a\u0440\u0435\u043f\u0438\u0442\u044c");
        btnLoan2.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/check.png")));
        btnLoan2.setToolTipText("\u041f\u0440\u0438\u043a\u0440\u0435\u043f\u0438\u0442\u044c \u0432\u0441\u0435 \u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u043d\u044b\u0435 \u041e\u0415 \n\u043a \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u0439 \u043f\u043e\u0437\u0438\u0446\u0438\u0438.");
        btnLoan2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLoan2ActionPerformed(e);
            }
        });
        contentPane.add(btnLoan2, cc.xy(8, 4));

        //======== scrollPane2 ========
        {
            scrollPane2.setViewportView(tblOes);
        }
        contentPane.add(scrollPane2, cc.xywh(2, 6, 5, 1));

        //---- btnLoan ----
        btnLoan.setText("\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c");
        btnLoan.setIcon(new ImageIcon(getClass().getResource("/ru/goodfil/catalog/ui/icons/check.png")));
        btnLoan.setToolTipText("\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043d\u043e\u0432\u044b\u0435 \u041e\u0415 \u0431\u0435\u0437 \u043f\u0440\u0438\u0432\u044f\u0437\u043a\u0438 \n\u043a \u043f\u043e\u0437\u0438\u0446\u0438\u0438");
        btnLoan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLoanActionPerformed();
            }
        });
        contentPane.add(btnLoan, cc.xy(8, 8));
        contentPane.add(lRowsCount, cc.xywh(4, 8, 3, 1));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        tblOes = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int row2 = convertRowIndexToModel(row);
                int col2 = convertColumnIndexToModel(column);

                OeModelTableModel model = (OeModelTableModel) getModel();
                OeModel oe = model.getRow(row2);

                if (component instanceof JLabel) {
                    if (col2 == 0) {
                        if (oe.getBrands().size() == 0) {
                            component.setBackground(LIGHT_BLUE);
                            ((JLabel) component).setToolTipText("Брэнд с таким именем будет создан");
                        } else if (oe.getBrands().size() == 1) {
                            component.setBackground(Color.WHITE);
                            ((JLabel) component).setToolTipText("Брэнд с таким именем уже существует");
                        } else {
                            component.setBackground(UIUtils.COLOR_BAD);
                            ((JLabel) component).setToolTipText("ЕСТЬ НЕСКОЛЬКО БРЭНДОВ С ТАКИМ ИМЕНЕМ");
                        }
                    }
                    if (col2 == 1) {
                        if (oe.getOes().size() > 0) {
                            component.setBackground(UIUtils.COLOR_BAD);
                            ((JLabel) component).setToolTipText("Уже есть ОЕ с таким именем");
                        } else {
                            component.setBackground(Color.WHITE);
                            ((JLabel) component).setToolTipText("Данный ОЕ будет загружен");
                        }
                    }
                }

                return component;
            }
        };
        scrollPane2.setViewportView(tblOes);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JTextPane textPane1;
    private JScrollPane scrollPane3;
    private JTextPane textPane2;
    private JButton btnImport;
    private JTextField textField1;
    private JButton btnLoan2;
    private JScrollPane scrollPane2;
    private JTable tblOes;
    private JButton btnLoan;
    private JLabel lRowsCount;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        // nothing
    }

    public static class OeModelTableModel extends AbstractTableAdapter<OeModel> {
        public OeModelTableModel(ListModel listModel) {
            super(listModel, "Производитель", "ОЕ");
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            OeModel oe = getRow(rowIndex);
            if (columnIndex == 0) {
                return oe.getBrand();
            }
            if (columnIndex == 1) {
                return oe.getOe();
            }
            return null;
        }
    }


    @Managed
    public static class OeModel {
        private final String brand;
        private final String oe;

        private final List<Brand> brands = new ArrayList<Brand>();
        private final List<Oe> oes = new ArrayList<Oe>();

        public OeModel(@NotNull @NotBlank String brand, @NotNull @NotBlank String oe) {
            this.brand = brand;
            this.oe = oe;
        }

        public void addBrands(@NotNull Collection<Brand> brands) {
            this.brands.addAll(brands);
        }

        public void addOes(@NotNull Collection<Oe> oes) {
            this.oes.addAll(oes);
        }

        public String getBrand() {
            return brand;
        }

        public String getOe() {
            return oe;
        }

        public List<Brand> getBrands() {
            return Collections.unmodifiableList(brands);
        }

        public List<Oe> getOes() {
            return Collections.unmodifiableList(oes);
        }
    }
}
