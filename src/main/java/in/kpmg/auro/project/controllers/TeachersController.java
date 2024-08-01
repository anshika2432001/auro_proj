package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import in.kpmg.auro.project.services.TeachersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/teacher")
public class TeachersController {

    @Autowired
    private TeachersServices teachersServices;


    @PostMapping("/total-teaching-grades-stats")
    public ApiResponse2<?> totalTeacherGradesStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.totalTechingGradesStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teacher-to-classes-ratio-stats")
    public ApiResponse2<?> teacherToTheNumberOfClassesRationStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teacherToTheNumberOfClassesRationStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/average-teacher-salary-stats")
    public ApiResponse2<?> averageTeacherSalaryStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.averageTeacherSalaryStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teachers-to-pupil-ratio-stats")
    public ApiResponse2<?> teachersToPupilRatioStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teachersToPupilRatioStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/qualification-teachers-stats")
    public ApiResponse2<?> QualificationOfTeachersStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.QualificationOfTeachersStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/cce-training-stats")
    public ApiResponse2<?> cceTrainingStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.cceTrainingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/training-certification-teachers-education-stats")
    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.trainingCertificationOfTeachersWithEducationDepartmentsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/nature-of-employment-stats")
    public ApiResponse2<?> natureOfEmploymentStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.natureOfEmploymentStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/day-spent-non-teaching-stats")
    public ApiResponse2<?> daySpentByTeachersNonTeachingStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.daySpentByTeachersNonTeachingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/hours-teachers-spend-yearly-training-stats")
    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.hoursTeacherSpendYearlyMandatoryTrainingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/periodicity-formative-assessments-school-stats")
    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.periodicityOfFormativeAssessmentsInSchoolStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/brainstorm-challenges-teacher-faced-teaching-stats")
    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.brainstormChallengesTeachersFacedByTeacherDuringTeachingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-with-smcs-stats")
    public ApiResponse2<?> schoolsWithSMCsStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.schoolsWithSMCsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/functional-smcs-stats")
    public ApiResponse2<?> functionalSMCsStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.functionalSMCsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-registered-vidyaanjali-portal-stats")
    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.schoolsRegisteredVidyaanjaliPortalStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teachers-student-score-stats")
    public ApiResponse2<?> teachersStudentScoreStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teachersStudentScoreStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-teacher-meeting-stats")
    public ApiResponse2<?> frequencyParentTeacherMeetingStats(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.frequencyParentTeacherMeetingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


//    ---------------------------------------TABLES-----------------------------------------


    @PostMapping("/total-teaching-grades-table")
    public ApiResponse2<?> totalTeacherGradesTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.totalTeacherGradesTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teacher-to-classes-ratio-table")
    public ApiResponse2<?> teacherToTheNumberOfClassesRatioTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teacherToTheNumberOfClassesRatioTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/average-teacher-salary-table")
    public ApiResponse2<?> averageTeacherSalaryTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.averageTeacherSalaryTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teachers-to-pupil-ratio-table")
    public ApiResponse2<?> teachersToPupilRatioTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teachersToPupilRatioTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/qualification-teachers-table")
    public ApiResponse2<?> QualificationOfTeachersTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.qualificationOfTeachersTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/cce-training-table")
    public ApiResponse2<?> cceTrainingTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.cceTrainingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/training-certification-teachers-education-table")
    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.trainingCertificationOfTeachersWithEducationDepartmentsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/nature-of-employment-table")
    public ApiResponse2<?> natureOfEmploymentTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.natureOfEmploymentTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/day-spent-non-teaching-table")
    public ApiResponse2<?> daySpentByTeachersNonTeachingTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.daySpentByTeachersNonTeachingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/hours-teachers-spend-yearly-training-table")
    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.hoursTeacherSpendYearlyMandatoryTrainingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/periodicity-formative-assessments-school-table")
    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.periodicityOfFormativeAssessmentsInSchoolTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/brainstorm-challenges-teacher-faced-teaching-table")
    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.brainstormChallengesTeachersFacedByTeacherDuringTeachingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-with-smcs-table")
    public ApiResponse2<?> schoolsWithSMCsTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.schoolsWithSMCsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/functional-smcs-table")
    public ApiResponse2<?> functionalSMCsTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.functionalSMCsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-registered-vidyaanjali-portal-table")
    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.schoolsRegisteredVidyaanjaliPortalTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/teachers-student-score-table")
    public ApiResponse2<?> teachersStudentScoreTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.teachersStudentScoreTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-teacher-meeting-table")
    public ApiResponse2<?> frequencyParentTeacherMeetingTable(@RequestBody TeachersPayloadDto payloadDto) {
        try {
            return teachersServices.frequencyParentTeacherMeetingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


}
