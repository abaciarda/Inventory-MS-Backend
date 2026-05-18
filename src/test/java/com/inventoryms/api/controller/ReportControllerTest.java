package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.report.ReportGenerateRequest;
import com.inventoryms.api.dto.report.StoredReportResponse;
import com.inventoryms.api.entity.StoredReport;
import com.inventoryms.api.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    void getStoredReports_ShouldReturnList() {
        StoredReport r = new StoredReport();
        r.setId(1);
        r.setName("Test");
        r.setFormat("PDF");
        r.setGeneratedAt(LocalDateTime.now());
        
        when(reportService.getAllStoredReports()).thenReturn(List.of(r));
        ResponseEntity<ApiResponse<List<StoredReportResponse>>> res = reportController.getStoredReports();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().getData().size());
    }

    @Test
    void generateReport_ShouldReturnSavedReport() {
        ReportGenerateRequest req = new ReportGenerateRequest();
        req.setName("My Report");
        req.setFormat("CSV");
        
        StoredReport r = new StoredReport();
        r.setId(1);
        r.setName("My Report");
        r.setFormat("CSV");
        r.setGeneratedAt(LocalDateTime.now());

        when(reportService.generateReport(any())).thenReturn(r);
        ResponseEntity<ApiResponse<StoredReportResponse>> res = reportController.generateReport(req);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().getData().getId());
    }

    @Test
    void downloadReport_ShouldReturnFile() {
        StoredReport r = new StoredReport();
        r.setId(1);
        r.setName("Test");
        r.setFormat("PDF");
        r.setFileData("test".getBytes());

        when(reportService.getStoredReport(1)).thenReturn(r);
        ResponseEntity<byte[]> res = reportController.downloadReport(1);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
    }
}
