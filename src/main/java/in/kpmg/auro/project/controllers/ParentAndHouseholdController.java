package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.ParentAndHouseholdServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/parent-household")

public class ParentAndHouseholdController {

    @Autowired
    private ParentAndHouseholdServices parentAndHouseholdServices;


    @PostMapping("/immigrant background-stats")
    public ApiResponse2<?> immigrantBackgroudStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.immigrantBackgroudStats(payloadDto);
    }

    @PostMapping("/parents-studied-twelfth-stats")
    public ApiResponse2<?> parentStudiedTwelfthStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentStudiedTwelfthStats(payloadDto);
    }

    @PostMapping("/parents-annual-expenditure-child-education-stats")
    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentAnnualExpenditureOnChildEducationStats(payloadDto);
    }


    @PostMapping("/household-with-internet-connection")
    public ApiResponse2<?> householdWithInternetConnectionStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.householdWithInternetConnectionStats(payloadDto);
    }

}
