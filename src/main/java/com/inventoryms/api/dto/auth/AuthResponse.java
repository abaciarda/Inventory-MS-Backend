package com.inventoryms.api.dto.auth;

public class AuthResponse {
    private boolean success;
    private int userId;
    private String username;
    private String role;

    public AuthResponse(boolean success, int userId, String username, String role) {
        this.success = success;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
