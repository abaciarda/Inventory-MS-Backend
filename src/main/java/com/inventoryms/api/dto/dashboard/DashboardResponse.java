package com.inventoryms.api.dto.dashboard;

import com.inventoryms.api.dto.stock.StockMovementResponse;
import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private long totalProducts;
    private BigDecimal stockValue;
    private long lowStockAmount;
    private long movementCount;
    private List<StockMovementResponse> recentMovements;
    private List<LowStockResponse> lowStockAlerts;
    private List<CategoryDistribution> inventorySnapshot;

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public BigDecimal getStockValue() {
        return stockValue;
    }

    public void setStockValue(BigDecimal stockValue) {
        this.stockValue = stockValue;
    }

    public long getLowStockAmount() {
        return lowStockAmount;
    }

    public void setLowStockAmount(long lowStockAmount) {
        this.lowStockAmount = lowStockAmount;
    }

    public long getMovementCount() {
        return movementCount;
    }

    public void setMovementCount(long movementCount) {
        this.movementCount = movementCount;
    }

    public List<StockMovementResponse> getRecentMovements() {
        return recentMovements;
    }

    public void setRecentMovements(List<StockMovementResponse> recentMovements) {
        this.recentMovements = recentMovements;
    }

    public List<LowStockResponse> getLowStockAlerts() {
        return lowStockAlerts;
    }

    public void setLowStockAlerts(List<LowStockResponse> lowStockAlerts) {
        this.lowStockAlerts = lowStockAlerts;
    }

    public List<CategoryDistribution> getInventorySnapshot() {
        return inventorySnapshot;
    }

    public void setInventorySnapshot(List<CategoryDistribution> inventorySnapshot) {
        this.inventorySnapshot = inventorySnapshot;
    }
}
