package com.inventoryms.api.dto.category;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
