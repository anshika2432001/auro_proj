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


    @PostMapping("/micro-scholar-quiz-stats")
    public ApiResponse2<?> microScholarshipQuizDataStats (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.microScholarQuizStats(payloadDto);
    }

    @PostMapping("/subject-wise-breakdown-stats")
    public ApiResponse2<?> subjectWiseBreakdownStats (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownAvgScoreStats(payloadDto);
    }

    @PostMapping("/grade-wise-avg-score-stats")
    public ApiResponse2<?> gradeWiseAvgScoreStats (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseAvgScoreStats(payloadDto);
    }

    @PostMapping("/topic-wise-avg-score-stats")
    public ApiResponse2<?> topicWiseAvgScoreStats (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseAvgScoreStats(payloadDto);
    }

    @PostMapping("/total-quiz-attempted-stats")
    public ApiResponse2<?> totalQuizAttemptedStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.totalQuizAttemptStats(payloadDto);
    }

    @PostMapping("/topic-wise-student-attempts-stats")
    public ApiResponse2<?> topicWiseStudAttemptsStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseStudAttemptsStats(payloadDto);
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-stats")
    public ApiResponse2<?> topicWiseMicroScholarQuizStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseMicroScholarQuizStats(payloadDto);
    }

    @PostMapping("/top-performing-topics-stats")
    public ApiResponse2<?> topTopicPerformanceStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topTopicPerformanceStats(payloadDto);
    }

    @PostMapping("/weak-performing-topics-stats")
    public ApiResponse2<?> weakTopicPerformanceStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.weakTopicPerformanceStats(payloadDto);
    }


    @PostMapping("/core-retake-practice-stats")
    public ApiResponse2<?> coreRetakePracticeStudentStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakePracticeStudentStats(payloadDto);
    }

    @PostMapping("/core-retake-improvement-stats")
    public ApiResponse2<?> coreRetakeImprovePercentStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakeImprovePercentStats(payloadDto);
    }

    @PostMapping("/subject-wise-breakdown-improve-stats")
    public ApiResponse2<?> subjectWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownImproveStats(payloadDto);
    }

    @PostMapping("/grade-wise-breakdown-improve-stats")
    public ApiResponse2<?> gradeWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseBreakdownImproveStats(payloadDto);
    }

    @PostMapping("/topic-wise-breakdown-improve-stats")
    public ApiResponse2<?> topicWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseBreakdownImproveStats(payloadDto);
    }



    @PostMapping("/topic-wise-avg-score-data")
    public ApiResponse2<?> topicWiseAvgScoreData(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseAvgScoreData(payloadDto);
    }


    @PostMapping("/topic-wise-student-attempts-data")
    public ApiResponse2<?> topicWiseStudAttemptsData(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseStudAttemptsData(payloadDto);
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-data")
    public ApiResponse2<?> topicWiseMicroScholarQuizData(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseMicroScholarQuizData(payloadDto);
    }


    @PostMapping("/top-performing-topics-data")
    public ApiResponse2<?> topTopicPerformanceData(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topTopicPerformanceData(payloadDto);
    }

    @PostMapping("/weak-performing-topics-data")
    public ApiResponse2<?> weakTopicPerformanceData(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.weakTopicPerformanceData(payloadDto);
    }


//    --------------------------------TABLES---------------------------------


    @PostMapping("/micro-scholar-quiz-table")
    public ApiResponse2<?> microScholarshipQuizDataTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.microScholarQuizTable(payloadDto);
    }

    @PostMapping("/subject-wise-breakdown-table")
    public ApiResponse2<?> subjectWiseBreakdownTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownAvgScoreTable(payloadDto);
    }


    @PostMapping("/grade-wise-avg-score-table")
    public ApiResponse2<?> gradeWiseAvgScoreTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseAvgScoreTable(payloadDto);
    }

    @PostMapping("/topic-wise-avg-score-table")
    public ApiResponse2<?> topicWiseAvgScoreTable (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseAvgScoreTable(payloadDto);
    }

    @PostMapping("/total-quiz-attempted-table")
    public ApiResponse2<?> totalQuizAttemptedTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.totalQuizAttemptTable(payloadDto);
    }

    @PostMapping("/topic-wise-student-attempts-table")
    public ApiResponse2<?> topicWiseStudAttemptsTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseStudAttemptsTable(payloadDto);
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-table")
    public ApiResponse2<?> topicWiseMicroScholarQuizTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseMicroScholarQuizTable(payloadDto);
    }

    @PostMapping("/top-performing-topics-table")
    public ApiResponse2<?> topTopicPerformanceTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topTopicPerformanceTable(payloadDto);
    }

    @PostMapping("/weak-performing-topics-table")
    public ApiResponse2<?> weakTopicPerformanceTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.weakTopicPerformanceTable(payloadDto);
    }


    @PostMapping("/core-retake-practice-table")
    public ApiResponse2<?> coreRetakePracticeStudentTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakePracticeStudentTable(payloadDto);
    }

    @PostMapping("/core-retake-improvement-table")
    public ApiResponse2<?> coreRetakeImprovePercentTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.coreRetakeImprovePercentTable(payloadDto);
    }

    @PostMapping("/subject-wise-breakdown-improve-table")
    public ApiResponse2<?> subjectWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.subjectWiseBreakdownImproveTable(payloadDto);
    }

    @PostMapping("/grade-wise-breakdown-improve-table")
    public ApiResponse2<?> gradeWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.gradeWiseBreakdownImproveTable(payloadDto);
    }

    @PostMapping("/topic-wise-breakdown-improve-table")
    public ApiResponse2<?> topicWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.topicWiseBreakdownImproveTable(payloadDto);
    }


}
