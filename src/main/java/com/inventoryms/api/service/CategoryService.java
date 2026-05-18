package com.inventoryms.api.service;

import com.inventoryms.api.dto.category.CategoryRequest;
import com.inventoryms.api.dto.category.CategoryResponse;
import com.inventoryms.api.entity.Category;
import com.inventoryms.api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("A category with this name already exists!");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryResponse::new).collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found! ID: " + id));
        return new CategoryResponse(category);
    }

    public CategoryResponse updateCategory(int id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found! ID: " + id));

        if (!category.getName().equals(categoryRequest.getName()) && categoryRepository.existsByName(categoryRequest.getName())) {
            throw new RuntimeException("A category with this name already exists!");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponse(updatedCategory);
    }

    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found! ID: " + id));
        categoryRepository.delete(category);
    }
}
