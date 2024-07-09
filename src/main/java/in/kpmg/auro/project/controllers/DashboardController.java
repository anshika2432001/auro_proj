package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.services.DashboardServices;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DashboardController {

    @Autowired
    private DashboardServices dashboardServices;

    @GetMapping("/dashboard-stats-data")
    public ApiResponse2<?> dashboardStatsDataFetch(){
        return dashboardServices.getDashboadStatsData();
    }


    @GetMapping("/super-query")
    public ApiResponse2<?> queryFetchData(){
        return dashboardServices.queryFetchData();
    }
}
