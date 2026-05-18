package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.category.CategoryRequest;
import com.inventoryms.api.dto.category.CategoryResponse;
import com.inventoryms.api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(true, "Category created successfully", categoryResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>(true, "Categories fetched successfully", categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable int id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(true, "Category fetched successfully", category);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse category = categoryService.updateCategory(id, categoryRequest);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(true, "Category updated successfully", category);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Category deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
