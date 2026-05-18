package com.inventoryms.api.service;

import com.inventoryms.api.dto.stock.StockMovementRequest;
import com.inventoryms.api.dto.stock.StockMovementResponse;
import com.inventoryms.api.entity.MovementType;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.entity.Stock;
import com.inventoryms.api.entity.StockMovement;
import com.inventoryms.api.entity.User;
import com.inventoryms.api.repository.ProductRepository;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.StockRepository;
import com.inventoryms.api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            StockRepository stockRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public StockMovementResponse recordMovement(StockMovementRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof User) ? ((User) principal).getUsername() : principal.toString();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found! ID: " + request.getProductId()));

        Stock stock = stockRepository.findByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("Stock not found for Product ID: " + product.getId()));

        if (request.getType() == MovementType.IN) {
            stock.setCurrentQuantity(stock.getCurrentQuantity() + request.getQuantity());
        } else if (request.getType() == MovementType.OUT) {
            if (stock.getCurrentQuantity() < request.getQuantity()) {
                throw new RuntimeException("Insufficient stock for this OUT movement!");
            }
            stock.setCurrentQuantity(stock.getCurrentQuantity() - request.getQuantity());
        } else if (request.getType() == MovementType.ADJUSTMENT) {
            stock.setCurrentQuantity(request.getQuantity());
        }

        stockRepository.save(stock);

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setUser(user);
        movement.setType(request.getType());
        movement.setQuantity(request.getQuantity());
        movement.setReason(request.getReason());

        StockMovement savedMovement = stockMovementRepository.save(movement);

        return new StockMovementResponse(savedMovement);
    }

    public List<StockMovementResponse> getMovementsByProductId(int productId) {
        return stockMovementRepository.findByProductId(productId).stream()
                .map(StockMovementResponse::new)
                .collect(Collectors.toList());
    }

    public List<StockMovementResponse> getAllMovements() {
        return stockMovementRepository.findAll().stream()
                .map(StockMovementResponse::new)
                .collect(Collectors.toList());
    }
}
