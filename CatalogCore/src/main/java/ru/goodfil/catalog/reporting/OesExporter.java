package ru.goodfil.catalog.reporting;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import ru.goodfil.catalog.export.domain.oeexport.OesDocumentModel;
import ru.goodfil.catalog.export.domain.oeexport.RowModel;
import ru.goodfil.catalog.utils.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author sazonovkirill@gmail.com
 * @version $Id: OesExporter.java 130 2013-03-14 13:05:40Z chezxxx@gmail.com $
 */
public class OesExporter {
    private static int MAX_ROWNUM = 60000;

    public static void export(OesDocumentModel oesDocumentModel, File outFile) throws IOException {
        Assert.notNull(oesDocumentModel);

        Workbook wb = new HSSFWorkbook();

        Font boldFont = wb.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        Font normalFont = wb.createFont();
        normalFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        CellStyle csBold = wb.createCellStyle();
        csBold.setFont(boldFont);

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

            int localIndex = 0;
            while (true) {
                drawRaw(sheet, 1 + localIndex, oesDocumentModel.getRows().get(globalIndex), csBold, csNormal);

                localIndex++;
                globalIndex++;

                if (globalIndex == oesDocumentModel.getRows().size()) {
                    break;
                }
                if (localIndex == MAX_ROWNUM) {
                    break;
                }
            }

            if (globalIndex == oesDocumentModel.getRows().size()) {
                break;
            }

            blockNumber++;
        }

        wb.write(new FileOutputStream(outFile));
    }

    private static void drawRaw(Sheet sheet, int rowIndex, RowModel rowModel, CellStyle csBold, CellStyle csNormal) {
        Row row = sheet.createRow(rowIndex);

        if (rowModel.isEmptyRow()) {
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(" ");
        } else if (!StringUtils.isBlank(rowModel.getBrandTitle())) {
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue("Goodwill");
            cell.setCellStyle(csBold);

            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getBrandTitle());
            cell.setCellStyle(csBold);
        } else {
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getGoodwillCode());
            cell.setCellStyle(csNormal);

            cell = row.createCell(1);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(rowModel.getExternalCode());
            cell.setCellStyle(csNormal);
        }
    }

    public static void exportToCross(OesDocumentModel oesDocumentModel, File outFile) throws IOException {
        Assert.notNull(oesDocumentModel);

        Workbook wb = new HSSFWorkbook();

        Font boldFont = wb.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        Font normalFont = wb.createFont();
        normalFont.setBoldweight(Font.BOLDWEIGHT_NORMAL);

        CellStyle csBold = wb.createCellStyle();
        csBold.setFont(boldFont);

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
            int localIndex = 0;
            while (true) {
                if (localIndex!=0){
                    drawRawCross(sheet, localIndex, oesDocumentModel.getRows().get(globalIndex), csBold, csNormal);
                }else{
                    Row row = sheet.createRow(localIndex);

                    Cell cell = row.createCell(0);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue("Бренд");
                    cell.setCellStyle(csBold);

                    cell = row.createCell(1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue("Код");
                    cell.setCellStyle(csBold);

                    cell = row.createCell(2);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue("Альтернативный производитель");
                    cell.setCellStyle(csBold);

                    cell = row.createCell(3);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue("Код альтернативного производителя");
                    cell.setCellStyle(csBold);
                }
                localIndex++;
                globalIndex++;

                if (globalIndex == oesDocumentModel.getRows().size()) {
                    break;
                }
                if (localIndex == MAX_ROWNUM) {
                    break;
                }
            }

            if (globalIndex == oesDocumentModel.getRows().size()) {
                break;
            }

            blockNumber++;
        }

        wb.write(new FileOutputStream(outFile));
    }

    private static void drawRawCross(Sheet sheet, int rowIndex, RowModel rowModel, CellStyle csBold, CellStyle csNormal) {
        Row row = sheet.createRow(rowIndex);

        Cell cell = row.createCell(0);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue("GOODWILL");
        cell.setCellStyle(csNormal);

        cell = row.createCell(1);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(rowModel.getGoodwillCode());
        cell.setCellStyle(csNormal);

        cell = row.createCell(2);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(rowModel.getBrandTitle());
        cell.setCellStyle(csNormal);

        cell = row.createCell(3);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(rowModel.getExternalCode());
        cell.setCellStyle(csNormal);

    }
}
