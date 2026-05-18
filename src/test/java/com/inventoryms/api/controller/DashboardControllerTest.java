package com.inventoryms.api.controller;

import com.inventoryms.api.dto.ApiResponse;
import com.inventoryms.api.dto.dashboard.DashboardResponse;
import com.inventoryms.api.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void getDashboardData_ShouldReturn200AndData() {
        DashboardResponse response = new DashboardResponse();
        response.setTotalProducts(10);
        
        when(dashboardService.getDashboardData()).thenReturn(response);

        ResponseEntity<ApiResponse<DashboardResponse>> result = dashboardController.getDashboardData();

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(true, result.getBody().isSuccess());
        assertEquals(10, result.getBody().getData().getTotalProducts());
    }
}
