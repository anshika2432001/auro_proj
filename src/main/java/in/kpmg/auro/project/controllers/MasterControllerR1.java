package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.MasterServicesR1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r1")
public class MasterControllerR1 {

    @Autowired
    private MasterServicesR1 masterServicesR1;


    @PostMapping("/micro-scholar-quiz-stats")
    public ApiResponse2<?> microScholarshipQuizDataStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.microScholarQuizStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/subject-wise-breakdown-stats")
    public ApiResponse2<?> subjectWiseBreakdownStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.subjectWiseBreakdownAvgScoreStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/grade-wise-avg-score-stats")
    public ApiResponse2<?> gradeWiseAvgScoreStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.gradeWiseAvgScoreStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }

    }

    @PostMapping("/topic-wise-avg-score-stats")
    public ApiResponse2<?> topicWiseAvgScoreStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.topicWiseAvgScoreStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/total-quiz-attempted-stats")
    public ApiResponse2<?> totalQuizAttemptedStats(@RequestBody ROnePayloadDto payloadDto) {
        try {

            return masterServicesR1.totalQuizAttemptStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-student-attempts-stats")
    public ApiResponse2<?> topicWiseStudAttemptsStats(@RequestBody ROnePayloadDto payloadDto) {

        try {

            return masterServicesR1.topicWiseStudAttemptsStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-stats")
    public ApiResponse2<?> topicWiseMicroScholarQuizStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.topicWiseMicroScholarQuizStats(payloadDto);

        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/top-performing-topics-stats")
    public ApiResponse2<?> topTopicPerformanceStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.topTopicPerformanceStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/weak-performing-topics-stats")
    public ApiResponse2<?> weakTopicPerformanceStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.weakTopicPerformanceStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/core-retake-practice-stats")
    public ApiResponse2<?> coreRetakePracticeStudentStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.coreRetakePracticeStudentStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/core-retake-improvement-stats")
    public ApiResponse2<?> coreRetakeImprovePercentStats(@RequestBody ROnePayloadDto payloadDto) {
        try {

            return masterServicesR1.coreRetakeImprovePercentStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    //    -------------------------------------------- TO BE REMOVE SOON
    @PostMapping("/subject-wise-breakdown-improve-stats")
    public ApiResponse2<?> subjectWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.subjectWiseBreakdownImproveStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/grade-wise-breakdown-improve-stats")
    public ApiResponse2<?> gradeWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.gradeWiseBreakdownImproveStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    //------------------------------------------------------
    @PostMapping("/topic-wise-breakdown-improve-stats")
    public ApiResponse2<?> topicWiseBreakdownImproveStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return masterServicesR1.topicWiseBreakdownImproveStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-breakdown-top-perform-topics-stats")
    public ApiResponse2<?> topicWiseBreakdownTopPerformingTopicsStats(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseBreakdownImproveForTopPerformingTopicsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-breakdown-weak-perform-topics-stats")
    public ApiResponse2<?> topicWiseBreakdownWeakPerformingTopicsStats(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseBreakdownWeakPerformingTopicsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/topic-wise-avg-score-data")
    public ApiResponse2<?> topicWiseAvgScoreData(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseAvgScoreData(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/topic-wise-student-attempts-data")
    public ApiResponse2<?> topicWiseStudAttemptsData(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseStudAttemptsData(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-data")
    public ApiResponse2<?> topicWiseMicroScholarQuizData(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseMicroScholarQuizData(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/top-performing-topics-data")
    public ApiResponse2<?> topTopicPerformanceData(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topTopicPerformanceData(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/weak-performing-topics-data")
    public ApiResponse2<?> weakTopicPerformanceData(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.weakTopicPerformanceData(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


//    --------------------------------TABLES---------------------------------


    @PostMapping("/micro-scholar-quiz-table")
    public ApiResponse2<?> microScholarshipQuizDataTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.microScholarshipQuizDataTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/subject-wise-breakdown-table")
    public ApiResponse2<?> subjectWiseBreakdownTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.subjectWiseBreakdownAvgScoreTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/grade-wise-avg-score-table")
    public ApiResponse2<?> gradeWiseAvgScoreTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.gradeWiseAvgScoreTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-avg-score-table")
    public ApiResponse2<?> topicWiseAvgScoreTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseAvgScoreTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/total-quiz-attempted-table")
    public ApiResponse2<?> totalQuizAttemptedTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.totalQuizAttemptTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-student-attempts-table")
    public ApiResponse2<?> topicWiseStudAttemptsTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseStudAttemptsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-micro-scholar-quiz-table")
    public ApiResponse2<?> topicWiseMicroScholarQuizTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseMicroScholarQuizTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/top-performing-topics-table")
    public ApiResponse2<?> topTopicPerformanceTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topTopicPerformanceTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/weak-performing-topics-table")
    public ApiResponse2<?> weakTopicPerformanceTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.weakTopicPerformanceTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }


    @PostMapping("/core-retake-practice-table")
    public ApiResponse2<?> coreRetakePracticeStudentTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.coreRetakePracticeStudentTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/core-retake-improvement-table")
    public ApiResponse2<?> coreRetakeImprovePercentTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.coreRetakeImprovePercentTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    //    -------------------------------------------- TO BE REMOVE SOON

    @PostMapping("/subject-wise-breakdown-improve-table")
    public ApiResponse2<?> subjectWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.subjectWiseBreakdownImproveTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/grade-wise-breakdown-improve-table")
    public ApiResponse2<?> gradeWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.gradeWiseBreakdownImproveTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    //    ---------------------------------------------------------
    @PostMapping("/topic-wise-breakdown-improve-table")
    public ApiResponse2<?> topicWiseBreakdownImproveTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseBreakdownImproveTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-breakdown-top-perform-topics-table")
    public ApiResponse2<?> topicWiseBreakdownTopPerformingTopicsTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseBreakdownTopPerformingTopicsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/topic-wise-breakdown-weak-perform-topics-table")
    public ApiResponse2<?> topicWiseBreakdownWeakPerformingTopicsTable(@RequestBody ROnePayloadDto payloadDto) {

        try {
            return masterServicesR1.topicWiseBreakdownWeakPerformingTopicsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }
}
