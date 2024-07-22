package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import in.kpmg.auro.project.services.TeachersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/teacher")
public class TeachersController {

    @Autowired
    private TeachersServices teachersServices;


    @PostMapping("/total-teaching-grades-stats")
    public ApiResponse2<?> totalTeacherGradesStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.totalTechingGradesStats(payloadDto);
    }

    @PostMapping("/teacher-to-classes-ratio-stats")
    public ApiResponse2<?> teacherToTheNumberOfClassesRationStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teacherToTheNumberOfClassesRationStats(payloadDto);
    }

    @PostMapping("/average-teacher-salary-stats")
    public ApiResponse2<?> averageTeacherSalaryStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.averageTeacherSalaryStats(payloadDto);
    }

    @PostMapping("/teachers-to-pupil-ratio-stats")
    public ApiResponse2<?> teachersToPupilRatioStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teachersToPupilRatioStats(payloadDto);
    }

    @PostMapping("/qualification-teachers-stats")
    public ApiResponse2<?> QualificationOfTeachersStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.QualificationOfTeachersStats(payloadDto);
    }

    @PostMapping("/cce-training-stats")
    public ApiResponse2<?> cceTrainingStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.cceTrainingStats(payloadDto);
    }

    @PostMapping("/training-certification-teachers-education-stats")
    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.trainingCertificationOfTeachersWithEducationDepartmentsStats(payloadDto);
    }

    @PostMapping("/nature-of-employment-stats")
    public ApiResponse2<?> natureOfEmploymentStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.natureOfEmploymentStats(payloadDto);
    }

    @PostMapping("/day-spent-non-teaching-stats")
    public ApiResponse2<?> daySpentByTeachersNonTeachingStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.daySpentByTeachersNonTeachingStats(payloadDto);
    }

    @PostMapping("/hours-teachers-spend-yearly-training-stats")
    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.hoursTeacherSpendYearlyMandatoryTrainingStats(payloadDto);
    }

    @PostMapping("/periodicity-formative-assessments-school-stats")
    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.periodicityOfFormativeAssessmentsInSchoolStats(payloadDto);
    }

    @PostMapping("/brainstorm-challenges-teacher-faced-teaching-stats")
    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.brainstormChallengesTeachersFacedByTeacherDuringTeachingStats(payloadDto);
    }

    @PostMapping("/schools-with-smcs-stats")
    public ApiResponse2<?> schoolsWithSMCsStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.schoolsWithSMCsStats(payloadDto);
    }

    @PostMapping("/functional-smcs-stats")
    public ApiResponse2<?> functionalSMCsStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.functionalSMCsStats(payloadDto);
    }

    @PostMapping("/schools-registered-vidyaanjali-portal-stats")
    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.schoolsRegisteredVidyaanjaliPortalStats(payloadDto);
    }

}
