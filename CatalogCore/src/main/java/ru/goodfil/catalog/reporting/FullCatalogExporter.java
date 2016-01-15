package ru.goodfil.catalog.reporting;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.goodfil.catalog.export.domain.fullexport.FullExportDocumentModel;
import ru.goodfil.catalog.export.domain.fullexport.RowFiltersModel;
import ru.goodfil.catalog.export.domain.fullexport.RowModel;
import ru.goodfil.catalog.utils.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: FullCatalogExporter.java 92 2012-09-23 06:36:03Z sazonovkirill $
 */
public class FullCatalogExporter {
    private static int MAX_ROWNUM = 60000;

    private static boolean isRowEmpty(RowModel row, Set<String> columns) {
        boolean isEmpty = true;
        for (String filterTypeId : columns) {
            RowFiltersModel rowFiltersModel = row.getFilters().get(filterTypeId);
            if (rowFiltersModel != null && rowFiltersModel.getFilters().size() > 0) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    public static void export(FullExportDocumentModel fullExportDocument, Map<String, String> columns, File outFile) throws IOException {
        Assert.notNull(fullExportDocument);
        Assert.notNull(columns);

        Workbook wb = new HSSFWorkbook();

        Font boldFont = wb.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        Font normalFont = wb.createFont();
        normalFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        CellStyle csHeader = wb.createCellStyle();
        csHeader.setFont(boldFont);

        CellStyle csNormal = wb.createCellStyle();
        csNormal.setFont(normalFont);

        int globalIndex = 0;
        int blockNumber = 0;
        while (true) {
            wb.createSheet();

            Sheet sheet = null;
            if (blockNumber < wb.getNumberOfSheets()) {
                sheet = wb.getSheetAt(blockNumber);
            } else {
                sheet = wb.createSheet();
            }
            Assert.notNull(sheet);

            drawHeader(sheet, columns, csHeader);

            int localIndex = 0;
            RowModel headerRow = null;
            while (true) {
                if (globalIndex == fullExportDocument.getRows().size()) {
                    break;
                }
                if (localIndex == MAX_ROWNUM) {
                    break;
                }

                RowModel row = fullExportDocument.getRows().get(globalIndex);
                globalIndex++;

                if (row.getHeader() != null) {
                    headerRow = row;
                }
                
                if (isRowEmpty(row, columns.keySet())) {
                    continue;
                }

                if (headerRow != null) {
                    localIndex++;
                    drawRaw(sheet, localIndex, headerRow, columns, csNormal);
                    headerRow = null;
                }

                localIndex++;
                drawRaw(sheet, localIndex, row, columns, csNormal);
            }

            if (globalIndex == fullExportDocument.getRows().size()) {
                break;
            }

            blockNumber++;
        }

        wb.write(new FileOutputStream(outFile));
    }

    private static void drawRaw(Sheet sheet, int rowIndex, RowModel rowModel, Map<String, String> columns, CellStyle csNormal) {
        Row row = sheet.createRow(rowIndex);

        if (StringUtils.isBlank(rowModel.getHeader())) {
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getSeria());
            cell.setCellStyle(csNormal);

            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getModel());
            cell.setCellStyle(csNormal);

            cell = row.createCell(2);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getEngine());
            cell.setCellStyle(csNormal);

            cell = row.createCell(3);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getKw());
            cell.setCellStyle(csNormal);

            cell = row.createCell(4);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getHp());
            cell.setCellStyle(csNormal);

            cell = row.createCell(5);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getBegdate());
            cell.setCellStyle(csNormal);

            cell = row.createCell(6);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getEnddate());
            cell.setCellStyle(csNormal);

            List<String> filterTypeIds = new ArrayList<String>(columns.keySet());
            for (int i = 0; i < filterTypeIds.size(); i++) {
                String filterTypeId = filterTypeIds.get(i);

                cell = row.createCell(7 + i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellStyle(csNormal);
                if (rowModel.getFilters().containsKey(filterTypeId)) {
                    cell.setCellValue(StringUtils.join(rowModel.getFilters().get(filterTypeId).getFilters(), "/"));
                } else {
                    cell.setCellValue("");
                }
            }
        } else {
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getHeader());
            cell.setCellStyle(csNormal);
        }
    }

    private static void drawHeader(Sheet sheet, Map<String, String> columns, CellStyle csHeader) {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Серия");
        cell.setCellStyle(csHeader);

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Модель");
        cell.setCellStyle(csHeader);

        cell = row.createCell(2);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Двигатель");
        cell.setCellStyle(csHeader);

        cell = row.createCell(3);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("КВ");
        cell.setCellStyle(csHeader);

        cell = row.createCell(4);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("ЛС");
        cell.setCellStyle(csHeader);

        cell = row.createCell(5);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Нач. производства");
        cell.setCellStyle(csHeader);

        cell = row.createCell(6);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("Ок. производства");
        cell.setCellStyle(csHeader);

        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 2000);
        sheet.setColumnWidth(4, 2000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);

        List<String> filterTypes = new ArrayList<String>(columns.values());
        for (int i = 0; i < filterTypes.size(); i++) {
            String filterType = filterTypes.get(i);

            cell = row.createCell(7 + i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(filterType);
            cell.setCellStyle(csHeader);
            sheet.setColumnWidth(7 + i, 6000);
        }
    }
}
