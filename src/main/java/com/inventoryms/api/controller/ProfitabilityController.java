package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.profitability.CategoryProfitability;
import com.inventoryms.api.dto.profitability.ProductProfitability;
import com.inventoryms.api.dto.profitability.ProfitabilitySummary;
import com.inventoryms.api.service.ProfitabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profitability")
public class ProfitabilityController {

    private final ProfitabilityService profitabilityService;

    public ProfitabilityController(ProfitabilityService profitabilityService) {
        this.profitabilityService = profitabilityService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProfitabilitySummary>> getSummary() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Profitability summary fetched", profitabilityService.getSummary()));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryProfitability>>> getCategories() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Category profitability fetched", profitabilityService.getCategoryProfitability()));
    }

    @GetMapping("/products/top")
    public ResponseEntity<ApiResponse<List<ProductProfitability>>> getTopProducts() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Top products fetched", profitabilityService.getTopProductsByProfit()));
    }
}
