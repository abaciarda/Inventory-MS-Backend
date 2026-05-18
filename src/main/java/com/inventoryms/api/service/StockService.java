package com.inventoryms.api.service;

import com.inventoryms.api.dto.stock.StockRequest;
import com.inventoryms.api.dto.stock.StockResponse;
import com.inventoryms.api.entity.Stock;
import com.inventoryms.api.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(StockResponse::new)
                .collect(Collectors.toList());
    }

    public StockResponse getStockByProductId(int productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found for Product ID: " + productId));
        return new StockResponse(stock);
    }

    public StockResponse updateMinThreshold(int productId, StockRequest request) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found for Product ID: " + productId));
        
        stock.setMinThreshold(request.getMinThreshold());
        Stock savedStock = stockRepository.save(stock);
        
        return new StockResponse(savedStock);
    }
}
