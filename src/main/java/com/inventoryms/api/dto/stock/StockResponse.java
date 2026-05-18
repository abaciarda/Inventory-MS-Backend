package com.inventoryms.api.dto.stock;

import com.inventoryms.api.entity.Stock;
import java.time.LocalDateTime;

public class StockResponse {
    private int id;
    private int currentQuantity;
    private int minThreshold;
    private LocalDateTime lastUpdated;
    private int productId;

    public StockResponse() {}

    public StockResponse(Stock stock) {
        this.id = stock.getId();
        this.currentQuantity = stock.getCurrentQuantity();
        this.minThreshold = stock.getMinThreshold();
        this.lastUpdated = stock.getLastUpdated();
        if (stock.getProduct() != null) {
            this.productId = stock.getProduct().getId();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(int minThreshold) {
        this.minThreshold = minThreshold;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
