package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.DashboardInputDto;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import in.kpmg.auro.project.services.DashboardServicesMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/dashboard")
public class DashboardControllerMain {


    @Autowired
    private DashboardServicesMain dashboardServicesMain;

    @PostMapping("/student-count")
    public ApiResponse2<?> studentCountStats(@RequestBody DashboardInputDto inputDto) {
        try {
            return dashboardServicesMain.studentCountStats(inputDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/parent-count")
    public ApiResponse2<?> parentCountStats(@RequestBody DashboardInputDto payloadDto) {
        try {
            return dashboardServicesMain.parentCountStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/teacher-count")
    public ApiResponse2<?> teacherCountStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return dashboardServicesMain.teacherCountStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @GetMapping("/school-count")
    public ApiResponse2<?> schoolCountStats() {
        try {
            return dashboardServicesMain.schoolCountStats();
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


}
