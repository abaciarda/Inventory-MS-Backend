package com.inventoryms.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotBlank(message = "SKU cannot be blank")
    private String sku;

    @NotNull(message = "Cost price cannot be null")
    @Positive(message = "Cost price must be greater than zero")
    private BigDecimal costPrice;

    @NotNull(message = "Sales price cannot be null")
    @Positive(message = "Sales price must be greater than zero")
    private BigDecimal salesPrice;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @NotNull(message = "Initial stock quantity cannot be null")
    private Integer initialStockQuantity;

    public String getSku() {
        return sku;
    }

    public String getName(){
        return name;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getInitialStockQuantity() {
        return initialStockQuantity;
    }

    public void setInitialStockQuantity(Integer initialStockQuantity) {
        this.initialStockQuantity = initialStockQuantity;
    }
}
