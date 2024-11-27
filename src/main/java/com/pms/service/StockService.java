package com.pms.service;

import com.pms.model.Drug;
import com.pms.model.Stock;
import com.pms.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DrugService drugService;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    public Stock addStock(Stock stock) {
        Drug drug = drugService.getDrugById(stock.getDrug().getId());
        if (drug != null) {
            stock.setDrug(drug);
            Stock savedStock = stockRepository.save(stock);
            updateDrugTotalQuantity(drug.getId());
            return savedStock;
        }
        return null;
    }

    public Stock updateStock(Stock stock) {
        Stock existingStock = stockRepository.findById(stock.getId()).orElse(null);
        if (existingStock != null) {
            existingStock.setBatchNo(stock.getBatchNo());
            existingStock.setQuantity(stock.getQuantity());
            existingStock.setExpiryDate(stock.getExpiryDate());
            existingStock.setManufacturingDate(stock.getManufacturingDate());
            Stock updatedStock = stockRepository.save(existingStock);
            updateDrugTotalQuantity(existingStock.getDrug().getId());
            return updatedStock;
        }
        return null;
    }

    public void deleteStock(Long id) {
        Stock stock = stockRepository.findById(id).orElse(null);
        if (stock != null) {
            stockRepository.deleteById(id);
            updateDrugTotalQuantity(stock.getDrug().getId());
        }
    }

    public void removeAllStocks() {
        stockRepository.deleteAll();
        // Update total quantity for all drugs
        drugService.getAllDrugs().forEach(drug -> updateDrugTotalQuantity(drug.getId()));
    }

    public List<Stock> getStocksByDrugId(Long drugId) {
        return stockRepository.findByDrugIdOrderByExpiryDateAsc(drugId);
    }
    
    public List<Stock> getStocksBelowThreshold() {
        return stockRepository.findStocksBelowThreshold();
    }

    private void updateDrugTotalQuantity(Long drugId) {
        Drug drug = drugService.getDrugById(drugId);
        if (drug != null) {
            List<Stock> stocks = getStocksByDrugId(drugId);
            int totalQuantity = stocks.stream().mapToInt(Stock::getQuantity).sum();
            drug.setTotalQuantity(totalQuantity);
            drugService.updateDrug(drug);
        }
    }
}

