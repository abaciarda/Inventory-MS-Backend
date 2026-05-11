package com.inventoryms.api.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductRequest {
    @NotBlank(message = "Ürün adı boş bırakılamaz")
    private String name;

    @NotBlank(message = "SKU kodu boş bırakılamaz")
    private String sku;

    @NotNull(message = "Maliyet fiyatı boş bırakılamaz")
    @Positive(message = "Maliyet fiyatı sıfırdan büyük olmalıdır")
    private BigDecimal costPrice;

    @NotBlank(message = "Maliyet fiyatı boş bırakılamaz")
    @Positive(message = "Satış fiyatı sıfırdan büyük olmalıdır")
    private BigDecimal salesPrice;

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

}
