package com.inventoryms.api.dto.stock;

import com.inventoryms.api.entity.MovementType;
import com.inventoryms.api.entity.StockMovement;
import java.time.LocalDateTime;

public class StockMovementResponse {
    private int id;
    private MovementType type;
    private int quantity;
    private LocalDateTime timestamp;
    private String reason;
    private int productId;
    private int userId;
    private String username;

    public StockMovementResponse() {}

    public StockMovementResponse(StockMovement movement) {
        this.id = movement.getId();
        this.type = movement.getType();
        this.quantity = movement.getQuantity();
        this.timestamp = movement.getTimestamp();
        this.reason = movement.getReason();
        if (movement.getProduct() != null) {
            this.productId = movement.getProduct().getId();
        }
        if (movement.getUser() != null) {
            this.userId = movement.getUser().getId();
            this.username = movement.getUser().getUsername();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
