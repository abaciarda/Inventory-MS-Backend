package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.stock.StockMovementRequest;
import com.inventoryms.api.dto.stock.StockMovementResponse;
import com.inventoryms.api.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordMovement(@Valid @RequestBody StockMovementRequest request) {
        StockMovementResponse responseDto = stockMovementService.recordMovement(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Stock movement recorded successfully", responseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getAllMovements() {
        List<StockMovementResponse> movements = stockMovementService.getAllMovements();
        return ResponseEntity.ok(new ApiResponse<>(true, "Movements fetched successfully", movements));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<StockMovementResponse>>> getMovementsByProductId(@PathVariable int productId) {
        List<StockMovementResponse> movements = stockMovementService.getMovementsByProductId(productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product movements fetched successfully", movements));
    }
}
