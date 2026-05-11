package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.product.ProductRequest;
import com.inventoryms.api.dto.product.ProductResponse;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductResponse testProductResponse;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setSku("SKU-TEST-1");
        product.setCostPrice(new BigDecimal("10.00"));
        product.setSalesPrice(new BigDecimal("20.00"));
        product.setCategoryId(1);
        testProductResponse = new ProductResponse(product);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setSku("SKU-TEST-1");
        request.setCostPrice(new BigDecimal("10.00"));
        request.setSalesPrice(new BigDecimal("20.00"));
        request.setCategoryId(1);

        when(productService.createProduct(any(ProductRequest.class))).thenReturn(testProductResponse);

        ResponseEntity<ApiResponse<ProductResponse>> responseEntity = productController.createProduct(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Product created successfully", responseEntity.getBody().getMessage());
        assertEquals("Test Product", responseEntity.getBody().getData().getName());

        verify(productService, times(1)).createProduct(request);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        when(productService.getAllProducts()).thenReturn(List.of(testProductResponse));

        ResponseEntity<ApiResponse<List<ProductResponse>>> responseEntity = productController.getAllProducts();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Products fetched successfully", responseEntity.getBody().getMessage());
        assertEquals(1, responseEntity.getBody().getData().size());

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productService.getProductById(1)).thenReturn(testProductResponse);

        ResponseEntity<ApiResponse<ProductResponse>> responseEntity = productController.getProductById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Product fetched successfully", responseEntity.getBody().getMessage());
        assertEquals("Test Product", responseEntity.getBody().getData().getName());

        verify(productService, times(1)).getProductById(1);
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Updated Product");
        request.setSku("SKU-UPDATED");
        request.setCostPrice(new BigDecimal("15.00"));
        request.setSalesPrice(new BigDecimal("25.00"));
        request.setCategoryId(2);

        Product updatedProduct = new Product();
        updatedProduct.setId(1);
        updatedProduct.setName("Updated Product");
        updatedProduct.setSku("SKU-UPDATED");
        updatedProduct.setCostPrice(new BigDecimal("15.00"));
        updatedProduct.setSalesPrice(new BigDecimal("25.00"));
        updatedProduct.setCategoryId(2);
        ProductResponse updatedResponse = new ProductResponse(updatedProduct);

        when(productService.updateProduct(eq(1), any(ProductRequest.class))).thenReturn(updatedResponse);

        ResponseEntity<ApiResponse<ProductResponse>> responseEntity = productController.updateProduct(1, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Product updated successfully", responseEntity.getBody().getMessage());
        assertEquals("Updated Product", responseEntity.getBody().getData().getName());

        verify(productService, times(1)).updateProduct(1, request);
    }

    @Test
    void deleteProduct_ShouldReturnVoid() {
        doNothing().when(productService).deleteProduct(1);

        ResponseEntity<ApiResponse<Void>> responseEntity = productController.deleteProduct(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isSuccess());
        assertEquals("Product deleted successfully", responseEntity.getBody().getMessage());
        assertNull(responseEntity.getBody().getData());

        verify(productService, times(1)).deleteProduct(1);
    }
}
