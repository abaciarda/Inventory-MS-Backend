package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.profitability.ProfitabilitySummary;
import com.inventoryms.api.service.ProfitabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfitabilityControllerTest {

    @Mock
    private ProfitabilityService profitabilityService;

    @InjectMocks
    private ProfitabilityController profitabilityController;

    @Test
    void getSummary_ShouldReturnData() {
        ProfitabilitySummary s = new ProfitabilitySummary(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO);
        when(profitabilityService.getSummary()).thenReturn(s);
        ResponseEntity<ApiResponse<ProfitabilitySummary>> res = profitabilityController.getSummary();
        assertNotNull(res);
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(BigDecimal.TEN, res.getBody().getData().getTotalCostValue());
    }
}
