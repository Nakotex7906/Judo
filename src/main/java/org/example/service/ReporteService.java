package org.example.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.example.model.user.Judoka;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReporteService {

    public byte[] generarReporteCombate(List<Judoka> judokas) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph("Estadisticas de Combate"));
            PdfPTable table = new PdfPTable(4);
            table.addCell("Judoka");
            table.addCell("Victorias");
            table.addCell("Derrotas");
            table.addCell("Empates");
            for (Judoka j : judokas) {
                table.addCell(j.getNombre() + " " + j.getApellido());
                table.addCell(String.valueOf(j.getVictorias()));
                table.addCell(String.valueOf(j.getDerrotas()));
                table.addCell(String.valueOf(j.getEmpates()));
            }
            document.add(table);
        } catch (DocumentException e) {
            // ignored
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
}
