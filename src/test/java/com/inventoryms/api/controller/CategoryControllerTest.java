package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.category.CategoryRequest;
import com.inventoryms.api.dto.category.CategoryResponse;
import com.inventoryms.api.entity.Category;
import com.inventoryms.api.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryResponse testCategoryResponse;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1);
        category.setName("Test Category");
        category.setDescription("Test Description");
        testCategoryResponse = new CategoryResponse(category);
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Test Category");
        request.setDescription("Test Description");

        when(categoryService.createCategory(any(CategoryRequest.class))).thenReturn(testCategoryResponse);

        ResponseEntity<ApiResponse<CategoryResponse>> responseEntity = categoryController.createCategory(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Category created successfully", responseEntity.getBody().getMessage());
        assertEquals("Test Category", responseEntity.getBody().getData().getName());

        verify(categoryService, times(1)).createCategory(request);
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategoryResponse));

        ResponseEntity<ApiResponse<List<CategoryResponse>>> responseEntity = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals(1, responseEntity.getBody().getData().size());
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        when(categoryService.getCategoryById(1)).thenReturn(testCategoryResponse);

        ResponseEntity<ApiResponse<CategoryResponse>> responseEntity = categoryController.getCategoryById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Test Category", responseEntity.getBody().getData().getName());
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Category");
        request.setDescription("Updated Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1);
        updatedCategory.setName("Updated Category");
        updatedCategory.setDescription("Updated Description");
        CategoryResponse updatedResponse = new CategoryResponse(updatedCategory);

        when(categoryService.updateCategory(eq(1), any(CategoryRequest.class))).thenReturn(updatedResponse);

        ResponseEntity<ApiResponse<CategoryResponse>> responseEntity = categoryController.updateCategory(1, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Updated Category", responseEntity.getBody().getData().getName());
    }

    @Test
    void deleteCategory_ShouldReturnVoid() {
        doNothing().when(categoryService).deleteCategory(1);

        ResponseEntity<ApiResponse<Void>> responseEntity = categoryController.deleteCategory(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody().getData());
    }
}
