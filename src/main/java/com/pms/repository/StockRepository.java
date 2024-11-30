package com.pms.repository;

import com.pms.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("SELECT s FROM Stock s WHERE s.quantity < s.threshold")
    List<Stock> findByQuantityLessThanThreshold();
}

