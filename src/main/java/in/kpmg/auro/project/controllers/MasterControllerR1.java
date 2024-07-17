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


//    ------------------------------------------------------------------------
//    state and nation data

    @PostMapping("/micro-scholar-quiz-stats")
    public ApiResponse2<?> microScholarshipQuizDataStats (@RequestBody ROnePayloadDto payloadDto){
        return masterServicesR1.getDashboadStatsDataStats(payloadDto);
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

}
