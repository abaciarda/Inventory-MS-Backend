package com.inventoryms.api.dto.dashboard;

public class CategoryDistribution {
    private String category;
    private long products;
    private long lowStock;

    public CategoryDistribution() {}

    public CategoryDistribution(String category, long products, long lowStock) {
        this.category = category;
        this.products = products;
        this.lowStock = lowStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getProducts() {
        return products;
    }

    public void setProducts(long products) {
        this.products = products;
    }

    public long getLowStock() {
        return lowStock;
    }

    public void setLowStock(long lowStock) {
        this.lowStock = lowStock;
    }
}
