package com.inventoryms.api.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ReportGenerateRequest {

    @NotBlank(message = "Report name is required")
    private String name;

    @NotBlank(message = "Format is required")
    @Pattern(regexp = "^(CSV|PDF|EXCEL)$", message = "Format must be CSV, PDF, or EXCEL")
    private String format;

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
}
