package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.services.DashboardServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @Autowired
    private DashboardServices dashboardServices;

    @GetMapping("/dashboard-stats-data")
    public ApiResponse2<?> dashboardStatsDataFetch(){
        return dashboardServices.getDashboadStatsData();
    }
}
