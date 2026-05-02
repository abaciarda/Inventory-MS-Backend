package com.inventoryms.api.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private boolean success;
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse(boolean success, String message, int status, String path) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}