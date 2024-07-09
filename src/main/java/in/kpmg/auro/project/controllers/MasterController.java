package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.services.DashboardServices;
import in.kpmg.auro.project.services.MasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterController {

    @Autowired
    private MasterServices masterServices;

    @GetMapping("/micro-scholar-quiz")
    public ApiResponse2<?> microScholarshipQuizData (){
        return masterServices.getDashboadStatsData();
    }

}
