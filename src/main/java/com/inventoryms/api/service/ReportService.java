package com.inventoryms.api.service;

import com.inventoryms.api.dto.report.ReportGenerateRequest;
import com.inventoryms.api.entity.StockMovement;
import com.inventoryms.api.entity.StoredReport;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.StoredReportRepository;
import com.inventoryms.api.repository.UserRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.fonts.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final StockMovementRepository stockMovementRepository;
    private final StoredReportRepository storedReportRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReportService(StockMovementRepository stockMovementRepository, StoredReportRepository storedReportRepository,
            UserRepository userRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.storedReportRepository = storedReportRepository;
        this.userRepository = userRepository;
    }

    public List<StoredReport> getAllStoredReports() {
        return storedReportRepository.findAllOrderByGeneratedAtDesc();
    }

    public StoredReport getStoredReport(int id) {
        return storedReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found!"));
    }

    @Transactional
    public StoredReport generateReport(ReportGenerateRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof User) ? ((User) principal).getUsername() : principal.toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] fileData;
        String format = request.getFormat().toUpperCase();
        if ("CSV".equals(format)) {
            fileData = exportMovementsCsv();
        } else if ("EXCEL".equals(format)) {
            fileData = exportMovementsExcel();
        } else if ("PDF".equals(format)) {
            fileData = exportMovementsPdf();
        } else {
            throw new IllegalArgumentException("Unsupported format");
        }

        StoredReport report = new StoredReport();
        report.setName(request.getName());
        report.setFormat(format);
        report.setGeneratedAt(LocalDateTime.now());
        report.setGeneratedBy(user);
        report.setFileData(fileData);

        return storedReportRepository.save(report);
    }

    private List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    private byte[] exportMovementsCsv() {
        List<StockMovement> movements = getAllMovements();
        StringWriter sw = new StringWriter();
        try (CSVPrinter printer = new CSVPrinter(sw,
                CSVFormat.DEFAULT.withHeader("ID", "Product", "SKU", "Type", "Quantity", "User", "Date", "Reason"))) {
            for (StockMovement m : movements) {
                printer.printRecord(
                        m.getId(),
                        m.getProduct() != null ? m.getProduct().getName() : "N/A",
                        m.getProduct() != null ? m.getProduct().getSku() : "N/A",
                        m.getType().name(),
                        m.getQuantity(),
                        m.getUser() != null ? m.getUser().getUsername() : "System",
                        m.getTimestamp().format(dtf),
                        m.getReason());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate CSV");
        }
        return sw.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] exportMovementsExcel() {
        List<StockMovement> movements = getAllMovements();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock Movements");

            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] columns = { "ID", "Product", "SKU", "Type", "Quantity", "User", "Date", "Reason" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (StockMovement m : movements) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getId());
                row.createCell(1).setCellValue(m.getProduct() != null ? m.getProduct().getName() : "N/A");
                row.createCell(2).setCellValue(m.getProduct() != null ? m.getProduct().getSku() : "N/A");
                row.createCell(3).setCellValue(m.getType().name());
                row.createCell(4).setCellValue(m.getQuantity());
                row.createCell(5).setCellValue(m.getUser() != null ? m.getUser().getUsername() : "System");
                row.createCell(6).setCellValue(m.getTimestamp().format(dtf));
                row.createCell(7).setCellValue(m.getReason() != null ? m.getReason() : "");
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel");
        }
    }

    private static final Color C_GREEN_900 = new Color(0x14, 0x53, 0x2D);
    private static final Color C_GREEN_800 = new Color(0x16, 0x65, 0x34);
    private static final Color C_GREEN_600 = new Color(0x16, 0xA3, 0x4A);
    private static final Color C_GREEN_100 = new Color(0xDC, 0xFC, 0xE7);
    private static final Color C_GREEN_50 = new Color(0xF0, 0xFD, 0xF4);
    private static final Color C_RED_100 = new Color(0xFE, 0xE2, 0xE2);
    private static final Color C_RED_700 = new Color(0xB9, 0x1C, 0x1C);
    private static final Color C_SLATE_900 = new Color(0x0F, 0x17, 0x2A);
    private static final Color C_SLATE_500 = new Color(0x64, 0x74, 0x8B);
    private static final Color C_SLATE_200 = new Color(0xE2, 0xE8, 0xF0);
    private static final Color C_SLATE_50 = new Color(0xF8, 0xFA, 0xFC);
    private static final Color C_WHITE = Color.WHITE;

    private byte[] exportMovementsPdf() {
        List<StockMovement> movements = getAllMovements();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 40, 40, 40, 50);
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            DateTimeFormatter humanDtf = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");

            PdfPTable headerBand = new PdfPTable(2);
            headerBand.setWidthPercentage(100);
            headerBand.setWidths(new float[] { 62f, 38f });
            headerBand.setSpacingAfter(14);

            com.lowagie.text.Font fCompany = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, C_WHITE);
            com.lowagie.text.Font fSubtitle = FontFactory.getFont(FontFactory.HELVETICA, 9,
                    new Color(0xBB, 0xF7, 0xD0));

            Phrase leftPhrase = new Phrase();
            leftPhrase.add(new Chunk("Inventory Management System\n", fCompany));
            leftPhrase.add(new Chunk("Stock Movement Report", fSubtitle));

            PdfPCell cLeft = new PdfPCell(leftPhrase);
            cLeft.setBackgroundColor(C_GREEN_900);
            cLeft.setPaddingTop(14);
            cLeft.setPaddingBottom(14);
            cLeft.setPaddingLeft(16);
            cLeft.setPaddingRight(8);
            cLeft.setBorder(0);
            cLeft.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerBand.addCell(cLeft);

            com.lowagie.text.Font fMetaLabel = FontFactory.getFont(FontFactory.HELVETICA, 7,
                    new Color(0xBB, 0xF7, 0xD0));
            com.lowagie.text.Font fMetaValue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, C_WHITE);

            Phrase rightPhrase = new Phrase();
            rightPhrase.add(new Chunk("GENERATED\n", fMetaLabel));
            rightPhrase.add(new Chunk(LocalDateTime.now().format(humanDtf) + "\n\n", fMetaValue));
            rightPhrase.add(new Chunk("TOTAL RECORDS\n", fMetaLabel));
            rightPhrase.add(new Chunk(String.valueOf(movements.size()), fMetaValue));

            PdfPCell cRight = new PdfPCell(rightPhrase);
            cRight.setBackgroundColor(C_GREEN_900);
            cRight.setPaddingTop(14);
            cRight.setPaddingBottom(14);
            cRight.setPaddingLeft(8);
            cRight.setPaddingRight(16);
            cRight.setBorder(0);
            cRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cRight.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerBand.addCell(cRight);

            document.add(headerBand);

            long cntIn = movements.stream().filter(m -> "IN".equals(m.getType().name())).count();
            long cntOut = movements.stream().filter(m -> "OUT".equals(m.getType().name())).count();
            int sumIn = movements.stream().filter(m -> "IN".equals(m.getType().name()))
                    .mapToInt(StockMovement::getQuantity).sum();
            int sumOut = movements.stream().filter(m -> "OUT".equals(m.getType().name()))
                    .mapToInt(StockMovement::getQuantity).sum();
            int net = sumIn - sumOut;

            PdfPTable kpiRow = new PdfPTable(4);
            kpiRow.setWidthPercentage(100);
            kpiRow.setSpacingAfter(14);

            kpiRow.addCell(buildKpiCell("TOTAL RECORDS", String.valueOf(movements.size()), C_SLATE_900));
            kpiRow.addCell(buildKpiCell("STOCK IN", "+" + sumIn + " units", C_GREEN_600));
            kpiRow.addCell(buildKpiCell("STOCK OUT", "−" + sumOut + " units", C_RED_700));
            kpiRow.addCell(buildKpiCell("NET CHANGE", (net >= 0 ? "+" : "") + net + " units",
                    net >= 0 ? C_GREEN_600 : C_RED_700));
            document.add(kpiRow);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 5f, 22f, 14f, 10f, 8f, 16f, 18f });
            table.setHeaderRows(1);

            com.lowagie.text.Font fColHdr = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, C_WHITE);
            String[] cols = { "#", "Product", "SKU", "Type", "Qty", "User", "Date" };
            for (String col : cols) {
                PdfPCell hdr = new PdfPCell(new Paragraph(col, fColHdr));
                hdr.setBackgroundColor(C_GREEN_800);
                hdr.setHorizontalAlignment(Element.ALIGN_CENTER);
                hdr.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hdr.setPaddingTop(8);
                hdr.setPaddingBottom(8);
                hdr.setPaddingLeft(5);
                hdr.setPaddingRight(5);
                hdr.setBorder(0);
                hdr.setBorderWidthBottom(2);
                hdr.setBorderColorBottom(C_GREEN_600);
                table.addCell(hdr);
            }

            com.lowagie.text.Font fRow = FontFactory.getFont(FontFactory.HELVETICA, 8, C_SLATE_900);
            com.lowagie.text.Font fMuted = FontFactory.getFont(FontFactory.HELVETICA, 8, C_SLATE_500);
            com.lowagie.text.Font fMono = FontFactory.getFont(FontFactory.COURIER, 8, C_SLATE_500);
            DateTimeFormatter rowDtf = DateTimeFormatter.ofPattern("dd MMM yy HH:mm");

            int idx = 0;
            for (StockMovement m : movements) {
                Color rowBg = (idx % 2 == 0) ? C_GREEN_50 : C_WHITE;
                boolean isOut = "OUT".equals(m.getType().name());

                table.addCell(pdfCell(String.valueOf(idx + 1), fMuted, rowBg, Element.ALIGN_CENTER));

                table.addCell(pdfCell(
                        m.getProduct() != null ? m.getProduct().getName() : "—",
                        fRow, rowBg, Element.ALIGN_LEFT));

                table.addCell(pdfCell(
                        m.getProduct() != null ? m.getProduct().getSku() : "—",
                        fMono, rowBg, Element.ALIGN_LEFT));
                table.addCell(pdfBadgeCell(
                        m.getType().name(), rowBg,
                        isOut ? C_RED_100 : C_GREEN_100,
                        isOut ? C_RED_700 : C_GREEN_600));

                com.lowagie.text.Font fQty = FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD, 8, isOut ? C_RED_700 : C_GREEN_600);
                table.addCell(pdfCell(
                        (isOut ? "\u2212" : "+") + m.getQuantity(),
                        fQty, rowBg, Element.ALIGN_CENTER));

                table.addCell(pdfCell(
                        m.getUser() != null ? m.getUser().getUsername() : "System",
                        fMuted, rowBg, Element.ALIGN_CENTER));
                table.addCell(pdfCell(
                        m.getTimestamp().format(rowDtf),
                        fMuted, rowBg, Element.ALIGN_CENTER));

                idx++;
            }
            document.add(table);

            com.lowagie.text.Font fFooter = FontFactory.getFont(FontFactory.HELVETICA, 7, C_SLATE_500);
            Paragraph footer = new Paragraph(
                    "Confidential — generated automatically by Inventory Management System. " +
                            "Not for external distribution.",
                    fFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(14);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private PdfPCell buildKpiCell(String label, String value, Color valueColor) {
        com.lowagie.text.Font fLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, C_SLATE_500);
        com.lowagie.text.Font fValue = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, valueColor);

        Phrase phrase = new Phrase();
        phrase.add(new Chunk(label + "\n", fLabel));
        phrase.add(new Chunk(value, fValue));

        PdfPCell cell = new PdfPCell(phrase);
        cell.setBackgroundColor(C_SLATE_50);
        cell.setBorderColor(C_SLATE_200);
        cell.setBorderWidth(0.75f);
        cell.setBorderWidthLeft(3f);
        cell.setBorderColorLeft(C_GREEN_600);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPCell pdfCell(String text, com.lowagie.text.Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(bg);
        cell.setPaddingTop(7);
        cell.setPaddingBottom(7);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorderColor(C_SLATE_200);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorderWidthBottom(0.5f);
        return cell;
    }

    private PdfPCell pdfBadgeCell(String text, Color rowBg, Color badgeBg, Color badgeFg) {
        com.lowagie.text.Font fBadge = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7, badgeFg);

        PdfPTable inner = new PdfPTable(1);
        inner.setWidthPercentage(75);
        PdfPCell pill = new PdfPCell(new Paragraph(text, fBadge));
        pill.setBackgroundColor(badgeBg);
        pill.setHorizontalAlignment(Element.ALIGN_CENTER);
        pill.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pill.setPaddingTop(3);
        pill.setPaddingBottom(3);
        pill.setPaddingLeft(6);
        pill.setPaddingRight(6);
        pill.setBorderColor(badgeFg);
        pill.setBorderWidth(0.5f);
        inner.addCell(pill);

        PdfPCell outer = new PdfPCell(inner);
        outer.setBackgroundColor(rowBg);
        outer.setHorizontalAlignment(Element.ALIGN_CENTER);
        outer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        outer.setPaddingTop(5);
        outer.setPaddingBottom(5);
        outer.setBorderColor(C_SLATE_200);
        outer.setBorderWidthLeft(0);
        outer.setBorderWidthRight(0);
        outer.setBorderWidthTop(0);
        outer.setBorderWidthBottom(0.5f);
        return outer;
    }

    @Deprecated
    private PdfPCell createCell(String content, com.lowagie.text.Font font, Color bg, int alignment) {
        return pdfCell(content, font, bg, alignment);
    }
}