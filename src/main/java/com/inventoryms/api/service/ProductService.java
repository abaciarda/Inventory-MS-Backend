package com.inventoryms.api.service;

import com.inventoryms.api.dto.product.ProductRequest;
import com.inventoryms.api.dto.product.ProductResponse;
import com.inventoryms.api.entity.Category;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.entity.Stock;
import com.inventoryms.api.entity.MovementType;
import com.inventoryms.api.entity.StockMovement;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.CategoryRepository;
import com.inventoryms.api.repository.ProductRepository;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StockMovementRepository stockMovementRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.userRepository = userRepository;
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
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found! ID: " + productRequest.getCategoryId()));
        product.setCategory(category);

        Stock stock = new Stock();
        stock.setCurrentQuantity(productRequest.getInitialStockQuantity());
        stock.setMinThreshold(0);
        product.setStock(stock);

        Product savedProduct = productRepository.save(product);

        if (productRequest.getInitialStockQuantity() > 0) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = (principal instanceof User) ? ((User) principal).getUsername() : principal.toString();
            User user = userRepository.findByUsername(username).orElse(null);
            
            if (user != null) {
                StockMovement movement = new StockMovement();
                movement.setProduct(savedProduct);
                movement.setUser(user);
                movement.setType(MovementType.IN);
                movement.setQuantity(productRequest.getInitialStockQuantity());
                movement.setReason("Initial stock from product creation");
                stockMovementRepository.save(movement);
            }
        }

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
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found! ID: " + productRequest.getCategoryId()));
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return new ProductResponse(updatedProduct);
    }

    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found! ID: " + id));
        productRepository.delete(product);
    }
}
