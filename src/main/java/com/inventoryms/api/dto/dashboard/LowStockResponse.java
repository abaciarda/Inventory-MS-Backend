package com.inventoryms.api.dto.dashboard;

import com.inventoryms.api.entity.Stock;
import java.time.LocalDateTime;

public class LowStockResponse {
    private int id;
    private int currentQuantity;
    private int minThreshold;
    private LocalDateTime lastUpdated;
    private int productId;
    private String productName;
    private String productSku;

    public LowStockResponse() {}

    public LowStockResponse(Stock stock) {
        this.id = stock.getId();
        this.currentQuantity = stock.getCurrentQuantity();
        this.minThreshold = stock.getMinThreshold();
        this.lastUpdated = stock.getLastUpdated();
        if (stock.getProduct() != null) {
            this.productId = stock.getProduct().getId();
            this.productName = stock.getProduct().getName();
            this.productSku = stock.getProduct().getSku();
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }
}
