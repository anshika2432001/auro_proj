package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.services.BudgetStateServices;
import in.kpmg.auro.project.services.DashboardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BudgetStateController {

    @Autowired
    private BudgetStateServices budgetStateServices;

    @GetMapping("/budget-state-data")
    public ApiResponse2<?> budgetStateDataFetch(){
        return budgetStateServices.getBudgetStateData();
    }
}
