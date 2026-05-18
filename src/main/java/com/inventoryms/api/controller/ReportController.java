package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.report.ReportGenerateRequest;
import com.inventoryms.api.dto.report.StoredReportResponse;
import com.inventoryms.api.entity.StoredReport;
import com.inventoryms.api.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoredReportResponse>>> getStoredReports() {
        List<StoredReportResponse> res = reportService.getAllStoredReports().stream()
                .map(StoredReportResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Stored reports fetched successfully", res));
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<StoredReportResponse>> generateReport(@Valid @RequestBody ReportGenerateRequest request) {
        StoredReport report = reportService.generateReport(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Report generated and saved successfully", new StoredReportResponse(report)));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable int id) {
        StoredReport report = reportService.getStoredReport(id);
        
        String contentType;
        String extension;
        String format = report.getFormat();
        
        if ("CSV".equals(format)) {
            contentType = "text/csv";
            extension = ".csv";
        } else if ("EXCEL".equals(format)) {
            contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            extension = ".xlsx";
        } else if ("PDF".equals(format)) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
            extension = ".pdf";
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            extension = ".bin";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getName() + extension + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(report.getFileData());
    }
}
