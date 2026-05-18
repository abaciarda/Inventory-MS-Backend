package com.inventoryms.api.repository;

import com.inventoryms.api.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {
    List<StockMovement> findByProductId(int productId);

    @Query("SELECT COUNT(m) FROM StockMovement m WHERE m.timestamp >= :startOfDay AND m.timestamp <= :endOfDay")
    long countMovementsBetween(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT m FROM StockMovement m ORDER BY m.timestamp DESC")
    List<StockMovement> findRecentMovements(Pageable pageable);
}
