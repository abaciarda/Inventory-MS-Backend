package com.inventoryms.api.repository;

import com.inventoryms.api.dto.dashboard.CategoryDistribution;
import com.inventoryms.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySku(String sku);

    @Query("SELECT new com.inventoryms.api.dto.dashboard.CategoryDistribution(c.name, COUNT(p), SUM(CASE WHEN s.currentQuantity <= s.minThreshold THEN 1 ELSE 0 END)) FROM Product p JOIN p.category c LEFT JOIN p.stock s GROUP BY c.id, c.name")
    List<CategoryDistribution> getCategoryDistribution();

    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name,String sku);

}

