package com.inventoryms.api.service;

import com.inventoryms.api.dto.dashboard.DashboardResponse;
import com.inventoryms.api.dto.dashboard.LowStockResponse;
import com.inventoryms.api.dto.stock.StockMovementResponse;
import com.inventoryms.api.repository.ProductRepository;
import com.inventoryms.api.repository.StockMovementRepository;
import com.inventoryms.api.repository.StockRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockMovementRepository stockMovementRepository;

    public DashboardService(ProductRepository productRepository, StockRepository stockRepository, StockMovementRepository stockMovementRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public DashboardResponse getDashboardData() {
        DashboardResponse response = new DashboardResponse();

        response.setTotalProducts(productRepository.count());
        response.setStockValue(stockRepository.calculateTotalStockValue());
        response.setLowStockAmount(stockRepository.countLowStockProducts());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59, 999999999);
        response.setMovementCount(stockMovementRepository.countMovementsBetween(startOfDay, endOfDay));

        List<StockMovementResponse> recentMovements = stockMovementRepository.findRecentMovements(PageRequest.of(0, 5))
                .stream()
                .map(StockMovementResponse::new)
                .collect(Collectors.toList());
        response.setRecentMovements(recentMovements);

        List<LowStockResponse> lowStockAlerts = stockRepository.findLowStockAlerts(PageRequest.of(0, 5))
                .stream()
                .map(LowStockResponse::new)
                .collect(Collectors.toList());
        response.setLowStockAlerts(lowStockAlerts);

        response.setInventorySnapshot(productRepository.getCategoryDistribution());

        return response;
    }
}
