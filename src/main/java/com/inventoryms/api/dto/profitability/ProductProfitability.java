package com.inventoryms.api.dto.profitability;

import java.math.BigDecimal;

public class ProductProfitability {
    private int productId;
    private String productName;
    private String sku;
    private BigDecimal costPrice;
    private BigDecimal salesPrice;
    private int quantity;
    private BigDecimal potentialProfit;
    private BigDecimal profitMargin;

    public ProductProfitability() {}

    public ProductProfitability(int productId, String productName, String sku, BigDecimal costPrice, BigDecimal salesPrice, int quantity, BigDecimal potentialProfit, BigDecimal profitMargin) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.costPrice = costPrice;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.potentialProfit = potentialProfit;
        this.profitMargin = profitMargin;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPotentialProfit() {
        return potentialProfit;
    }

    public void setPotentialProfit(BigDecimal potentialProfit) {
        this.potentialProfit = potentialProfit;
    }

    public BigDecimal getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(BigDecimal profitMargin) {
        this.profitMargin = profitMargin;
    }
}
