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
            throw new RuntimeException("Bu SKU koduna sahip bir ürün zaten mevcut!");
        }

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSku(productRequest.getSku());
        product.setCostPrice(productRequest.getCostPrice());
        product.setSalesPrice(productRequest.getSalesPrice());

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);

    }
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::convertToResponse).collect(Collectors.toList());
    }
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Ürün bulunamadı! ID: "+ id));
        return convertToResponse(product);
    }
    private ProductResponse convertToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setSku(product.getSku());
        productResponse.setCostPrice(product.getCostPrice());
        productResponse.setSalesPrice(product.getSalesPrice());
        return productResponse;

    }
}
