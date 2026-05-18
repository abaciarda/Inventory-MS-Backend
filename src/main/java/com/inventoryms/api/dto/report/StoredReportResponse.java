package com.inventoryms.api.dto.report;

import com.inventoryms.api.entity.StoredReport;
import java.time.LocalDateTime;

public class StoredReportResponse {
    private int id;
    private String name;
    private String format;
    private LocalDateTime generatedAt;
    private String generatedBy;

    public StoredReportResponse() {}

    public StoredReportResponse(StoredReport report) {
        this.id = report.getId();
        this.name = report.getName();
        this.format = report.getFormat();
        this.generatedAt = report.getGeneratedAt();
        if (report.getGeneratedBy() != null) {
            this.generatedBy = report.getGeneratedBy().getUsername();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
}
