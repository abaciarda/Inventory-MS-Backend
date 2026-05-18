package com.inventoryms.api.repository;

import com.inventoryms.api.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findByProductId(int productId);

    @Query("SELECT COALESCE(SUM(s.currentQuantity * p.costPrice), 0) FROM Stock s JOIN s.product p")
    BigDecimal calculateTotalStockValue();

    @Query("SELECT COUNT(s) FROM Stock s WHERE s.currentQuantity <= s.minThreshold")
    long countLowStockProducts();

    @Query("SELECT s FROM Stock s WHERE s.currentQuantity <= s.minThreshold")
    List<Stock> findLowStockAlerts(Pageable pageable);
}
