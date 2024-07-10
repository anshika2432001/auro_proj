package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.MasterServicesR1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r1")
public class MasterControllerR1 {

    @Autowired
    private MasterServicesR1 masterServicesR1;

    @PostMapping("/micro-scholar-quiz")
    public ApiResponse2<?> microScholarshipQuizData (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.getDashboadStatsData(payloadDto);
    }


    @PostMapping("/subject-wise-breakdown")
    public ApiResponse2<?> subjectWiseBreakdown (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownAvgScore(payloadDto);
    }


    @PostMapping("/grade-wise-avg-score")
    public ApiResponse2<?> gradeWiseAvgScore (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseAvgScore(payloadDto);
    }

    @PostMapping("/topic-wise-avg-score")
    public ApiResponse2<?> topicWiseAvgScore (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseAvgScore(payloadDto);
    }

}
