package com.inventoryms.api.service;

import com.inventoryms.api.dto.stock.StockMovementRequest;
import com.inventoryms.api.dto.stock.StockMovementResponse;
import com.inventoryms.api.entity.*;
import com.inventoryms.api.repository.ProductRepository;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.StockRepository;
import com.inventoryms.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StockMovementService stockMovementService;

    private User testUser;
    private Product testProduct;
    private Stock testStock;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1);

        testStock = new Stock();
        testStock.setId(1);
        testStock.setCurrentQuantity(100);
        testStock.setProduct(testProduct);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password"));
        SecurityContextHolder.setContext(context);
    }

    @Test
    void recordMovement_WhenTypeIsIn_ShouldIncreaseStockQuantity() {
        StockMovementRequest request = new StockMovementRequest();
        request.setProductId(1);
        request.setType(MovementType.IN);
        request.setQuantity(50);
        request.setReason("Restock");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(testStock));
        
        StockMovement movement = new StockMovement();
        movement.setId(1);
        movement.setType(MovementType.IN);
        movement.setQuantity(50);
        movement.setTimestamp(LocalDateTime.now());
        movement.setReason("Restock");
        movement.setProduct(testProduct);
        movement.setUser(testUser);
        
        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(movement);

        StockMovementResponse response = stockMovementService.recordMovement(request);

        assertEquals(150, testStock.getCurrentQuantity());
        verify(stockRepository, times(1)).save(testStock);
        assertEquals(MovementType.IN, response.getType());
    }

    @Test
    void recordMovement_WhenTypeIsOut_AndSufficientStock_ShouldDecreaseStockQuantity() {
        StockMovementRequest request = new StockMovementRequest();
        request.setProductId(1);
        request.setType(MovementType.OUT);
        request.setQuantity(30);
        request.setReason("Sale");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(testStock));
        
        StockMovement movement = new StockMovement();
        movement.setId(1);
        movement.setType(MovementType.OUT);
        movement.setQuantity(30);
        movement.setTimestamp(LocalDateTime.now());
        movement.setReason("Sale");
        movement.setProduct(testProduct);
        movement.setUser(testUser);

        when(stockMovementRepository.save(any(StockMovement.class))).thenReturn(movement);

        StockMovementResponse response = stockMovementService.recordMovement(request);

        assertEquals(70, testStock.getCurrentQuantity());
        verify(stockRepository, times(1)).save(testStock);
        assertEquals(MovementType.OUT, response.getType());
    }

    @Test
    void recordMovement_WhenTypeIsOut_AndInsufficientStock_ShouldThrowException() {
        StockMovementRequest request = new StockMovementRequest();
        request.setProductId(1);
        request.setType(MovementType.OUT);
        request.setQuantity(150); // more than 100
        request.setReason("Sale");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(testStock));

        assertThrows(RuntimeException.class, () -> stockMovementService.recordMovement(request));
        verify(stockRepository, never()).save(any(Stock.class));
    }
}
