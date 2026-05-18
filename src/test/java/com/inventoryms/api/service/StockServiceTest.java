package com.inventoryms.api.service;

import com.inventoryms.api.dto.stock.StockRequest;
import com.inventoryms.api.dto.stock.StockResponse;
import com.inventoryms.api.entity.Product;
import com.inventoryms.api.entity.Stock;
import com.inventoryms.api.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock testStock;

    @BeforeEach
    void setUp() {
        testStock = new Stock();
        testStock.setId(1);
        testStock.setCurrentQuantity(100);
        testStock.setMinThreshold(10);
        testStock.setLastUpdated(LocalDateTime.now());
        
        Product product = new Product();
        product.setId(1);
        testStock.setProduct(product);
    }

    @Test
    void getAllStocks_ShouldReturnList() {
        when(stockRepository.findAll()).thenReturn(List.of(testStock));
        List<StockResponse> result = stockService.getAllStocks();
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getCurrentQuantity());
    }

    @Test
    void getStockByProductId_WhenExists_ShouldReturnStock() {
        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(testStock));
        StockResponse result = stockService.getStockByProductId(1);
        assertEquals(100, result.getCurrentQuantity());
    }

    @Test
    void updateMinThreshold_ShouldUpdateAndReturnStock() {
        StockRequest request = new StockRequest();
        request.setMinThreshold(20);

        when(stockRepository.findByProductId(1)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock); // save will return modified object

        StockResponse result = stockService.updateMinThreshold(1, request);
        
        verify(stockRepository, times(1)).save(testStock);
    }
}
