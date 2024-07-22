package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.DashboardInputDto;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import in.kpmg.auro.project.services.DashboardServicesMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/dashboard")
public class DashboardControllerMain {


    @Autowired
    private DashboardServicesMain dashboardServicesMain;

    @PostMapping("/student-count")
    public ApiResponse2<?> studentCountStats(@RequestBody DashboardInputDto inputDto){
        return dashboardServicesMain.studentCountStats(inputDto);
    }

    @PostMapping("/parent-count")
    public ApiResponse2<?> parentCountStats(@RequestBody DashboardInputDto payloadDto){
        return dashboardServicesMain.parentCountStats(payloadDto);
    }


    @PostMapping("/teacher-count")
    public ApiResponse2<?> teacherCountStats(@RequestBody TeachersPayloadDto payloadDto){
        return dashboardServicesMain.teacherCountStats(payloadDto);
    }

    @GetMapping("/school-count")
    public ApiResponse2<?> schoolCountStats(){
        return dashboardServicesMain.schoolCountStats();
    }


}
