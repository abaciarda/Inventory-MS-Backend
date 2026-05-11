package com.inventoryms.api.service;

import com.inventoryms.api.dto.product.ProductRequest;
import com.inventoryms.api.dto.product.ProductResponse;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductRequest testProductRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setSku("SKU-TEST-1");
        testProduct.setCostPrice(new BigDecimal("10.00"));
        testProduct.setSalesPrice(new BigDecimal("20.00"));
        testProduct.setCategoryId(1);

        testProductRequest = new ProductRequest();
        testProductRequest.setName("Test Product");
        testProductRequest.setSku("SKU-TEST-1");
        testProductRequest.setCostPrice(new BigDecimal("10.00"));
        testProductRequest.setSalesPrice(new BigDecimal("20.00"));
        testProductRequest.setCategoryId(1);
    }

    @Test
    void createProduct_WhenSkuDoesNotExist_ShouldCreateAndReturnProductResponse() {
        when(productRepository.existsBySku("SKU-TEST-1")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse result = productService.createProduct(testProductRequest);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("SKU-TEST-1", result.getSku());
        verify(productRepository, times(1)).existsBySku("SKU-TEST-1");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_WhenSkuAlreadyExists_ShouldThrowException() {
        when(productRepository.existsBySku("SKU-TEST-1")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.createProduct(testProductRequest));

        assertEquals("A product with this SKU already exists!", exception.getMessage());
        verify(productRepository, times(1)).existsBySku("SKU-TEST-1");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProductResponses() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProductResponse() {
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        ProductResponse result = productService.getProductById(1);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductById(99));

        assertEquals("Product not found! ID: 99", exception.getMessage());
        verify(productRepository, times(1)).findById(99);
    }

    @Test
    void updateProduct_WhenProductExistsAndSkuNotTaken_ShouldUpdateAndReturnProductResponse() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setSku("SKU-UPDATED");
        updateRequest.setCostPrice(new BigDecimal("15.00"));
        updateRequest.setSalesPrice(new BigDecimal("25.00"));
        updateRequest.setCategoryId(2);

        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setName("Updated Product");
        updatedProduct.setSku("SKU-UPDATED");
        updatedProduct.setCostPrice(new BigDecimal("15.00"));
        updatedProduct.setSalesPrice(new BigDecimal("25.00"));
        updatedProduct.setCategoryId(2);

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.existsBySku("SKU-UPDATED")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse result = productService.updateProduct(1, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("SKU-UPDATED", result.getSku());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).existsBySku("SKU-UPDATED");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_WhenProductExistsAndSkuTakenByAnother_ShouldThrowException() {
        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setSku("SKU-TAKEN");
        updateRequest.setCostPrice(new BigDecimal("15.00"));
        updateRequest.setSalesPrice(new BigDecimal("25.00"));
        updateRequest.setCategoryId(2);

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(productRepository.existsBySku("SKU-TAKEN")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(1, updateRequest));

        assertEquals("A product with this SKU already exists!", exception.getMessage());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).existsBySku("SKU-TAKEN");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldDelete() {
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));

        assertDoesNotThrow(() -> productService.deleteProduct(1));

        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct(99));

        assertEquals("Product not found! ID: 99", exception.getMessage());
        verify(productRepository, times(1)).findById(99);
        verify(productRepository, never()).delete(any(Product.class));
    }
}
