package com.inventoryms.api.dto.profitability;

import java.math.BigDecimal;

public class CategoryProfitability {
    private String categoryName;
    private BigDecimal costValue;
    private BigDecimal retailValue;
    private BigDecimal potentialProfit;
    private BigDecimal profitMargin;

    public CategoryProfitability() {}

    public CategoryProfitability(String categoryName, BigDecimal costValue, BigDecimal retailValue, BigDecimal potentialProfit, BigDecimal profitMargin) {
        this.categoryName = categoryName;
        this.costValue = costValue;
        this.retailValue = retailValue;
        this.potentialProfit = potentialProfit;
        this.profitMargin = profitMargin;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getCostValue() {
        return costValue;
    }

    public void setCostValue(BigDecimal costValue) {
        this.costValue = costValue;
    }

    public BigDecimal getRetailValue() {
        return retailValue;
    }

    public void setRetailValue(BigDecimal retailValue) {
        this.retailValue = retailValue;
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
