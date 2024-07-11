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

    @PostMapping("/total-quiz-attempted")
    public ApiResponse2<?> totalQuizAttempted (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.totalQuizAttempt(payloadDto);
    }

    @PostMapping("/topic-wise-student-attempts")
    public ApiResponse2<?> topicWiseStudAttempts (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseStudAttempts(payloadDto);
    }

    @PostMapping("/topic-wise-micro-scholar-quiz")
    public ApiResponse2<?> topicWiseMicroScholarQuiz (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseMicroScholarQuiz(payloadDto);
    }

    @PostMapping("/top-performing-topics")
    public ApiResponse2<?> topTopicPerformance (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topTopicPerformance(payloadDto);
    }

    @PostMapping("/weak-performing-topics")
    public ApiResponse2<?> weakTopicPerformance (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.weakTopicPerformance(payloadDto);
    }


    @PostMapping("/core-retake-practice")
    public ApiResponse2<?> coreRetakePracticeStudent (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakePracticeStudent(payloadDto);
    }

    @PostMapping("/core-retake-improvement")
    public ApiResponse2<?> coreRetakeImprovePercent (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakeImprovePercent(payloadDto);
    }


    @PostMapping("/subject-wise-breakdown-improve")
    public ApiResponse2<?> subjectWiseBreakdownImprove (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownImprove(payloadDto);
    }

    @PostMapping("/grade-wise-breakdown-improve")
    public ApiResponse2<?> gradeWiseBreakdownImprove (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseBreakdownImprove(payloadDto);
    }


    @PostMapping("/topic-wise-breakdown-improve")
    public ApiResponse2<?> topicWiseBreakdownImprove (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseBreakdownImprove(payloadDto);
    }



}
