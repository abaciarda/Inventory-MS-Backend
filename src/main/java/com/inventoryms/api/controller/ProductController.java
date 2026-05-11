package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.product.ProductRequest;
import com.inventoryms.api.dto.product.ProductResponse;
import com.inventoryms.api.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.createProduct(productRequest);
        ApiResponse<ProductResponse> response = new ApiResponse<>(true, "Product created successfully", productResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(){
        List<ProductResponse> products = productService.getAllProducts();
        ApiResponse<List<ProductResponse>> response = new ApiResponse<>(true, "Products fetched successfully", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable int id){
        ProductResponse product = productService.getProductById(id);
        ApiResponse<ProductResponse> response = new ApiResponse<>(true, "Product fetched successfully", product);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequest productRequest){
        ProductResponse product = productService.updateProduct(id, productRequest);
        ApiResponse<ProductResponse> response = new ApiResponse<>(true, "Product updated successfully", product);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int id){
        productService.deleteProduct(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Product deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
