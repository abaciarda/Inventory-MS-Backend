package com.inventoryms.api.service;

import com.inventoryms.api.dto.dashboard.CategoryDistribution;
import com.inventoryms.api.dto.dashboard.DashboardResponse;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.entity.Stock;
import com.inventoryms.api.entity.StockMovement;
import com.inventoryms.api.repository.ProductRepository;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {}

    @Test
    void getDashboardData_ShouldReturnAggregatedData() {
        when(productRepository.count()).thenReturn(10L);
        when(stockRepository.calculateTotalStockValue()).thenReturn(new BigDecimal("1500.00"));
        when(stockRepository.countLowStockProducts()).thenReturn(2L);
        when(stockMovementRepository.countMovementsBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(5L);

        Product p = new Product();
        p.setId(1);
        p.setName("P1");
        p.setSku("SKU1");
        
        Stock s = new Stock();
        s.setId(1);
        s.setProduct(p);
        s.setCurrentQuantity(1);
        s.setMinThreshold(5);

        when(stockRepository.findLowStockAlerts(any(Pageable.class))).thenReturn(List.of(s));

        StockMovement sm = new StockMovement();
        sm.setId(1);
        sm.setProduct(p);
        sm.setQuantity(10);
        when(stockMovementRepository.findRecentMovements(any(Pageable.class))).thenReturn(List.of(sm));

        when(productRepository.getCategoryDistribution()).thenReturn(List.of(new CategoryDistribution("Elec", 5, 1)));

        DashboardResponse response = dashboardService.getDashboardData();

        assertNotNull(response);
        assertEquals(10L, response.getTotalProducts());
        assertEquals(new BigDecimal("1500.00"), response.getStockValue());
        assertEquals(2L, response.getLowStockAmount());
        assertEquals(5L, response.getMovementCount());
        assertEquals(1, response.getRecentMovements().size());
        assertEquals(1, response.getLowStockAlerts().size());
        assertEquals(1, response.getInventorySnapshot().size());
    }
}
