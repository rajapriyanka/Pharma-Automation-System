package com.pms.controller;

import com.pms.model.Stock;
import com.pms.service.StockService;
import com.pms.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private DrugService drugService;

    @GetMapping
    public String getAllStocks(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        model.addAttribute("drugs", drugService.getAllDrugs());
        return "stock-management";
    }

    @PostMapping("/add")
    public String addStock(@ModelAttribute Stock stock) {
        stockService.addStock(stock);
        return "redirect:/stocks";
    }

    @PostMapping("/delete")
    public String deleteStock(@RequestParam Long id) {
        stockService.deleteStock(id);
        return "redirect:/stocks";
    }

    @GetMapping("/below-threshold")
    public String getStocksBelowThreshold(Model model) {
        List<Stock> stocks = stockService.getStocksBelowThreshold();
        model.addAttribute("stocks", stocks);
        return "stock-below-threshold";
    }

    @PostMapping("/restock")
    public String restockStock(@RequestParam Long id, @RequestParam Integer quantity) {
        stockService.restockStock(id, quantity);
        return "redirect:/stock/below-threshold";
    }
}

