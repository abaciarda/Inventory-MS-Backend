package com.inventoryms.api.repository;

import com.inventoryms.api.entity.StoredReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoredReportRepository extends JpaRepository<StoredReport, Integer> {
    
    @Query("SELECT r FROM StoredReport r ORDER BY r.generatedAt DESC")
    List<StoredReport> findAllOrderByGeneratedAtDesc();
}
