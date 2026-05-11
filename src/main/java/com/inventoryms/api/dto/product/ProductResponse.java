package com.inventoryms.api.dto.product;

import com.inventoryms.api.entity.Product;
import java.math.BigDecimal;

public class ProductResponse {
    private int id;
    private String name;
    private String sku;
    private BigDecimal costPrice;
    private BigDecimal salesPrice;
    private Integer categoryId;

    public ProductResponse() {}

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.costPrice = product.getCostPrice();
        this.salesPrice = product.getSalesPrice();
        this.categoryId = product.getCategoryId();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSku() {
        return sku;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
