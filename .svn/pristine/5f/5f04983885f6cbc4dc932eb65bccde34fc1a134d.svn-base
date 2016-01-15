package ru.goodfil.catalog.reporting;

import java.io.FileWriter;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.IllegalNameException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import ru.goodfil.catalog.export.domain.oeexport.OesDocumentModel;
import ru.goodfil.catalog.export.domain.oeexport.RowModel;

public class OesXmlExporter
{
    public void doExport(OesDocumentModel oesDocumentModel)
    {
        Element rootElement = new Element("Oes");
        Document document = new Document(rootElement);
        List<RowModel> rows = oesDocumentModel.getRows();
        for (RowModel row : rows) {
            try {
                Element element = new Element("good").setAttribute("code", row.getGoodwillCode())
                        .setAttribute("alt_brand", row.getBrandTitle()).setAttribute("alt_code", row.getExternalCode());
                rootElement.addContent(element); } catch (IllegalNameException e) {
                e.printStackTrace();
            }
        }
        try
        {
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            FileWriter fileWriter = new FileWriter("D:\\cross.xml");
            xmlOutputter.output(document, fileWriter);
            fileWriter.close(); } catch (Exception e) {
            e.printStackTrace();
        }
    }
}