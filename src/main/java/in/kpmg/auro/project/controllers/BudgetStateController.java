package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.BudgetStatesPayloadDto;
import in.kpmg.auro.project.services.BudgetStateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BudgetStateController {


    @Autowired
    private BudgetStateServices budgetStateServices;

    @GetMapping("/budget-state-data")
    public ApiResponse2<?> budgetStateDataFetch(){
        return budgetStateServices.getBudgetStateData();
    }

    @GetMapping("/fund-allocated")
    public ApiResponse2<?> fundAllocated(@RequestParam(required = false) String state){
        return budgetStateServices.fundAllocated(state);
    }


    @PostMapping("/budget-filter")
    public ApiResponse2<?> budgetDataWithFilters(@RequestBody BudgetStatesPayloadDto payloadDto){
        return budgetStateServices.budgetDataWithFilters(payloadDto);
    }

}
