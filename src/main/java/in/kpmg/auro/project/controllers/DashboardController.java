package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.TopicNameDto;
import in.kpmg.auro.project.services.DashboardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/filter-dropdowns")
    public ApiResponse2<?> filterDropdowns(){
        return dashboardServices.filterDropdowns();
    }


    @PostMapping("/fetch-topics")
    public ApiResponse2<?> fetchTopTopicsName(@RequestBody TopicNameDto dto){
        return dashboardServices.fetchTopicNames(dto);
    }

    @PostMapping("/fetch-weak-perform-topics")
    public ApiResponse2<?> fetchWeakTopicsName(@RequestBody TopicNameDto dto){
        return dashboardServices.fetchWeakTopicsName(dto);
    }
}
