package com.vanavil.itinerary.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.vanavil.itinerary.dto.ItineraryRequest;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfGeneratorService {

    public byte[] generateItineraryPdf(ItineraryRequest request) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Add custom margins (Left, Right, Top, Bottom)
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // Define high-contrast minimalist brand colors
            Color nearBlack = new Color(26, 26, 26);
            Color safetyOrange = new Color(239, 129, 50);
            Color lightGrey = new Color(234, 234, 234);
            Color mutedGrey = new Color(150, 150, 150);

            // Fonts
            Font brandFont = new Font(Font.HELVETICA, 18, Font.BOLD, safetyOrange);
            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD, nearBlack);
            Font labelFont = new Font(Font.HELVETICA, 10, Font.NORMAL, mutedGrey);
            Font valueFont = new Font(Font.HELVETICA, 12, Font.BOLD, nearBlack);
            Font bodyFont = new Font(Font.HELVETICA, 11, Font.NORMAL, nearBlack);

            // 1. Header Layout
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1.5f, 3.5f});

            try {
                Image logo = Image.getInstance(getClass().getResource("/logo.jpg"));
                logo.scaleToFit(110, 110);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerTable.addCell(logoCell);
            } catch (Exception e) {
                System.out.println("⚠️ LOGO ERROR: " + e.getMessage());
                PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                emptyCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(emptyCell);
            }

            PdfPCell brandCell = new PdfPCell(new Phrase("VANAVIL TOURS & HOLIDAYS", brandFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(brandCell);
            document.add(headerTable);

            // Divider line
            LineSeparator separator = new LineSeparator(2f, 100f, safetyOrange, Element.ALIGN_CENTER, -10f);
            document.add(new Chunk(separator));
            document.add(new Paragraph("\n\n"));

            // 2. Client Details Box
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setSpacingAfter(20f);

            PdfPCell clientCell = new PdfPCell();
            clientCell.setBackgroundColor(lightGrey);
            clientCell.setBorder(Rectangle.NO_BORDER);
            clientCell.setPadding(15f);
            clientCell.addElement(new Phrase("PREPARED FOR", labelFont));
            clientCell.addElement(new Phrase(request.getClientName().toUpperCase(), valueFont));
            detailsTable.addCell(clientCell);

            PdfPCell datesCell = new PdfPCell();
            datesCell.setBackgroundColor(lightGrey);
            datesCell.setBorder(Rectangle.NO_BORDER);
            datesCell.setPadding(15f);
            datesCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            Paragraph datesLabel = new Paragraph("TRAVEL DATES", labelFont);
            datesLabel.setAlignment(Element.ALIGN_RIGHT);
            Paragraph datesValue = new Paragraph(request.getTravelDates(), valueFont);
            datesValue.setAlignment(Element.ALIGN_RIGHT);
            datesCell.addElement(datesLabel);
            datesCell.addElement(datesValue);
            detailsTable.addCell(datesCell);

            document.add(detailsTable);

            // 3. Itinerary Section Title
            Paragraph sectionTitle = new Paragraph("Your Itinerary Schedule", titleFont);
            sectionTitle.setSpacingAfter(15f);
            document.add(sectionTitle);

            // 4. Formatting the DMC Text
            String rawText = request.getRawItinerary();
            if (rawText != null) {
                String[] lines = rawText.split("\n");
                for (String line : lines) {
                    if (!line.trim().isEmpty()) {
                        Paragraph p = new Paragraph(line.trim(), bodyFont);
                        p.setLeading(18f);
                        p.setSpacingAfter(8f);
                        p.setAlignment(Element.ALIGN_JUSTIFIED);
                        document.add(p);
                    }
                }
            }

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}