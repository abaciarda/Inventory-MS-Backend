package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.stock.StockRequest;
import com.inventoryms.api.dto.stock.StockResponse;
import com.inventoryms.api.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockResponse>>> getAllStocks() {
        List<StockResponse> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Stocks fetched successfully", stocks));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<StockResponse>> getStockByProductId(@PathVariable int productId) {
        StockResponse stock = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Stock fetched successfully", stock));
    }

    @PutMapping("/product/{productId}/threshold")
    public ResponseEntity<ApiResponse<StockResponse>> updateMinThreshold(
            @PathVariable int productId,
            @Valid @RequestBody StockRequest request) {
        StockResponse stock = stockService.updateMinThreshold(productId, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Min threshold updated successfully", stock));
    }
}
