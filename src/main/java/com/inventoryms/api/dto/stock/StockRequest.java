package com.inventoryms.api.dto.stock;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StockRequest {
    @NotNull(message = "Min threshold cannot be null")
    @Min(value = 0, message = "Min threshold must be zero or greater")
    private Integer minThreshold;

    public Integer getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }
}
