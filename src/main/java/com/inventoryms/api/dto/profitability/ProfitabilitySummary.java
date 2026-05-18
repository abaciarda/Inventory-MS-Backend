package com.inventoryms.api.dto.profitability;

import java.math.BigDecimal;

public class ProfitabilitySummary {
    private BigDecimal totalCostValue;
    private BigDecimal totalRetailValue;
    private BigDecimal potentialProfit;
    private BigDecimal averageProfitMargin;

    public ProfitabilitySummary() {}

    public ProfitabilitySummary(BigDecimal totalCostValue, BigDecimal totalRetailValue, BigDecimal potentialProfit, BigDecimal averageProfitMargin) {
        this.totalCostValue = totalCostValue;
        this.totalRetailValue = totalRetailValue;
        this.potentialProfit = potentialProfit;
        this.averageProfitMargin = averageProfitMargin;
    }

    public BigDecimal getTotalCostValue() {
        return totalCostValue;
    }

    public void setTotalCostValue(BigDecimal totalCostValue) {
        this.totalCostValue = totalCostValue;
    }

    public BigDecimal getTotalRetailValue() {
        return totalRetailValue;
    }

    public void setTotalRetailValue(BigDecimal totalRetailValue) {
        this.totalRetailValue = totalRetailValue;
    }

    public BigDecimal getPotentialProfit() {
        return potentialProfit;
    }

    public void setPotentialProfit(BigDecimal potentialProfit) {
        this.potentialProfit = potentialProfit;
    }

    public BigDecimal getAverageProfitMargin() {
        return averageProfitMargin;
    }

    public void setAverageProfitMargin(BigDecimal averageProfitMargin) {
        this.averageProfitMargin = averageProfitMargin;
    }
}
