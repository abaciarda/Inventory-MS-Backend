package com.inventoryms.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products", uniqueConstraints = { @UniqueConstraint(name = "uk_products_sku", columnNames = "sku")})

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false,length = 255)
    private String name;

    @Column(nullable=false,length = 255)
    private String sku;

    @Column(name="cost_price",nullable=false,precision = 19,scale=2)
    private BigDecimal costPrice;

    @Column(name="sales_price",nullable=false,precision=19,scale=2)
    private BigDecimal salesPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_id", referencedColumnName = "id")
    private Stock stock;

    public void setId(int id) {
        this.id = id;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public String getSku() {
        return sku;
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}
