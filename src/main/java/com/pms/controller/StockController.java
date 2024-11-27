package com.pms.controller;

import com.pms.model.Stock;
import com.pms.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public String getAllStocks(Model model) {
        model.addAttribute("stocks", stockService.getAllStocks());
        return "stock-list";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock addedStock = stockService.addStock(stock);
        if (addedStock != null) {
            return ResponseEntity.ok(addedStock);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/update")
    public String updateStock(@ModelAttribute Stock stock) {
        stockService.updateStock(stock);
        return "redirect:/stocks";
    }

    @PostMapping("/delete")
    public String deleteStock(@RequestParam Long id) {
        stockService.deleteStock(id);
        return "redirect:/stocks";
    }

    @PostMapping("/remove-all")
    public String removeAllStocks() {
        stockService.removeAllStocks();
        return "redirect:/stocks";
    }

    @GetMapping("/drug/{drugId}")
    public String getStocksByDrugId(@PathVariable Long drugId, Model model) {
        model.addAttribute("stocks", stockService.getStocksByDrugId(drugId));
        return "stock-list";
    }
}

