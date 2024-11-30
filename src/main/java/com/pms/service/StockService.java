package com.pms.service;

import com.pms.model.Drug;
import com.pms.model.Stock;
import com.pms.repository.DrugRepository;
import com.pms.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DrugRepository drugRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Transactional
    public Stock addStock(Stock stock) {
        if (stock.getDrug() == null || stock.getDrug().getId() == null) {
            throw new IllegalArgumentException("Drug ID must be provided");
        }

        Drug drug = drugRepository.findById(stock.getDrug().getId())
            .orElseThrow(() -> new EntityNotFoundException("Drug not found with id: " + stock.getDrug().getId()));

        stock.setDrug(drug);
        Stock savedStock = stockRepository.save(stock);

        drug.setTotalQuantity(drug.getTotalQuantity() + stock.getQuantity());
        drugRepository.save(drug);

        return savedStock;
    }

    @Transactional
    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));
        
        Drug drug = stock.getDrug();
        drug.setTotalQuantity(drug.getTotalQuantity() - stock.getQuantity());
        drugRepository.save(drug);
        
        stockRepository.delete(stock);
    }

    public List<Stock> getStocksBelowThreshold() {
        return stockRepository.findByQuantityLessThanThreshold();
    }

    @Transactional
    public void restockStock(Long id, Integer quantity) {
        Stock stock = stockRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Stock not found with id: " + id));

        stock.setQuantity(stock.getQuantity() + quantity);
        stockRepository.save(stock);

        Drug drug = stock.getDrug();
        drug.setTotalQuantity(drug.getTotalQuantity() + quantity);
        drugRepository.save(drug);
    }
}

