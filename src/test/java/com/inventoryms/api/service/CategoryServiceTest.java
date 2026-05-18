package com.inventoryms.api.service;

import com.inventoryms.api.dto.category.CategoryRequest;
import com.inventoryms.api.dto.category.CategoryResponse;
import com.inventoryms.api.entity.Category;
import com.inventoryms.api.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryRequest testCategoryRequest;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");

        testCategoryRequest = new CategoryRequest();
        testCategoryRequest.setName("Test Category");
        testCategoryRequest.setDescription("Test Description");
    }

    @Test
    void createCategory_WhenNameDoesNotExist_ShouldCreateAndReturnResponse() {
        when(categoryRepository.existsByName("Test Category")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        CategoryResponse result = categoryService.createCategory(testCategoryRequest);

        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository, times(1)).existsByName("Test Category");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_WhenNameAlreadyExists_ShouldThrowException() {
        when(categoryRepository.existsByName("Test Category")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.createCategory(testCategoryRequest));

        assertEquals("A category with this name already exists!", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory));

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void getCategoryById_WhenExists_ShouldReturnResponse() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));

        CategoryResponse result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals("Test Category", result.getName());
    }

    @Test
    void updateCategory_WhenExistsAndNameNotTaken_ShouldUpdate() {
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setName("Updated Category");
        updateRequest.setDescription("Updated Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1);
        updatedCategory.setName("Updated Category");
        updatedCategory.setDescription("Updated Description");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.existsByName("Updated Category")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryResponse result = categoryService.updateCategory(1, updateRequest);

        assertEquals("Updated Category", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory_WhenExists_ShouldDelete() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));

        assertDoesNotThrow(() -> categoryService.deleteCategory(1));

        verify(categoryRepository, times(1)).delete(testCategory);
    }
}
