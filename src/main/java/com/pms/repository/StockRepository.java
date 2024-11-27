package com.pms.repository;

import com.pms.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE s.drug.id = :drugId ORDER BY s.expiryDate ASC")
    List<Stock> findByDrugIdOrderByExpiryDateAsc(Long drugId);

    @Query("SELECT s FROM Stock s WHERE s.quantity <= s.threshold")
    List<Stock> findStocksBelowThreshold();
}

