package com.pms.controller;

import com.pms.model.Drug;
import com.pms.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drugs")
public class DrugController {

    @Autowired
    private DrugService drugService;

    @GetMapping
    public String getAllDrugs(Model model) {
        List<Drug> drugs = drugService.getAllDrugs();
        model.addAttribute("drugs", drugs);
        return "drug-list";
    }

    @GetMapping("/{id}")
    public String getDrugById(@PathVariable Long id, Model model) {
        Drug drug = drugService.getDrugById(id);
        model.addAttribute("drug", drug);
        return "drug-details";
    }

    @PostMapping("/add")
    public String addDrug(@ModelAttribute Drug drug) {
        drugService.saveDrug(drug);
        return "redirect:/drugs";
    }

    @PostMapping("/update")
    public String updateDrug(@ModelAttribute Drug drug) {
        drugService.updateDrug(drug);
        return "redirect:/drugs";
    }

    @PostMapping("/deactivate")
    public String deactivateDrug(@RequestParam Long id) {
        drugService.deactivateDrug(id);
        return "redirect:/drugs";
    }

    @PostMapping("/delete")
    public String deleteDrug(@RequestParam Long id) {
        drugService.deleteDrug(id);
        return "redirect:/drugs";
    }
}

