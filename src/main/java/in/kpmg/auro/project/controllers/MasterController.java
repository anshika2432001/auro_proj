package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.DashboardServices;
import in.kpmg.auro.project.services.MasterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterController {

    @Autowired
    private MasterServices masterServices;

    @PostMapping("/micro-scholar-quiz")
    public ApiResponse2<?> microScholarshipQuizData (@RequestBody ROnePayloadDto payloadDto){
        return masterServices.getDashboadStatsData(payloadDto);
    }

}
