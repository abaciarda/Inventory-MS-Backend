package com.inventoryms.api.service;

import com.inventoryms.api.dto.product.ProductRequest;
import com.inventoryms.api.dto.product.ProductResponse;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        if(productRepository.existsBySku(productRequest.getSku())){
            throw new RuntimeException("A product with this SKU already exists!");
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setCostPrice(productRequest.getCostPrice());
        product.setSalesPrice(productRequest.getSalesPrice());
        product.setCategoryId(productRequest.getCategoryId());

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::new).collect(Collectors.toList());
    }

    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found! ID: " + id));
        return new ProductResponse(product);
    }

    public ProductResponse updateProduct(int id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found! ID: " + id));

        if (!product.getSku().equals(productRequest.getSku()) && productRepository.existsBySku(productRequest.getSku())) {
            throw new RuntimeException("A product with this SKU already exists!");
        }

        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setCostPrice(productRequest.getCostPrice());
        product.setSalesPrice(productRequest.getSalesPrice());
        product.setCategoryId(productRequest.getCategoryId());

        Product updatedProduct = productRepository.save(product);

        return new ProductResponse(updatedProduct);
    }

    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found! ID: " + id));
        productRepository.delete(product);
    }
}
