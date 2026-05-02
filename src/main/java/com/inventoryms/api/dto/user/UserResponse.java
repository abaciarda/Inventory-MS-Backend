package com.inventoryms.api.dto.user;

import com.inventoryms.api.entity.User;

import java.time.LocalDateTime;

public class UserResponse {
    private int id;
    private String username;
    private String role;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
