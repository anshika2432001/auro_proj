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

    @PostMapping("/teachers-student-score-stats")
    public ApiResponse2<?> teachersStudentScoreStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teachersStudentScoreStats(payloadDto);
    }

    @PostMapping("/parents-teacher-meeting-stats")
    public ApiResponse2<?> frequencyParentTeacherMeetingStats(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.frequencyParentTeacherMeetingStats(payloadDto);
    }


//    ---------------------------------------TABLES-----------------------------------------


    @PostMapping("/total-teaching-grades-table")
    public ApiResponse2<?> totalTeacherGradesTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.totalTeacherGradesTable(payloadDto);
    }

    @PostMapping("/teacher-to-classes-ratio-table")
    public ApiResponse2<?> teacherToTheNumberOfClassesRatioTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teacherToTheNumberOfClassesRatioTable(payloadDto);
    }

    @PostMapping("/average-teacher-salary-table")
    public ApiResponse2<?> averageTeacherSalaryTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.averageTeacherSalaryTable(payloadDto);
    }

    @PostMapping("/teachers-to-pupil-ratio-table")
    public ApiResponse2<?> teachersToPupilRatioTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teachersToPupilRatioTable(payloadDto);
    }

    @PostMapping("/qualification-teachers-table")
    public ApiResponse2<?> QualificationOfTeachersTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.qualificationOfTeachersTable(payloadDto);
    }

    @PostMapping("/cce-training-table")
    public ApiResponse2<?> cceTrainingTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.cceTrainingTable(payloadDto);
    }

    @PostMapping("/training-certification-teachers-education-table")
    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.trainingCertificationOfTeachersWithEducationDepartmentsTable(payloadDto);
    }

    @PostMapping("/nature-of-employment-table")
    public ApiResponse2<?> natureOfEmploymentTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.natureOfEmploymentTable(payloadDto);
    }

    @PostMapping("/day-spent-non-teaching-table")
    public ApiResponse2<?> daySpentByTeachersNonTeachingTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.daySpentByTeachersNonTeachingTable(payloadDto);
    }

    @PostMapping("/hours-teachers-spend-yearly-training-table")
    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.hoursTeacherSpendYearlyMandatoryTrainingTable(payloadDto);
    }

    @PostMapping("/periodicity-formative-assessments-school-table")
    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.periodicityOfFormativeAssessmentsInSchoolTable(payloadDto);
    }

    @PostMapping("/brainstorm-challenges-teacher-faced-teaching-table")
    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.brainstormChallengesTeachersFacedByTeacherDuringTeachingTable(payloadDto);
    }

    @PostMapping("/schools-with-smcs-table")
    public ApiResponse2<?> schoolsWithSMCsTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.schoolsWithSMCsTable(payloadDto);
    }

    @PostMapping("/functional-smcs-table")
    public ApiResponse2<?> functionalSMCsTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.functionalSMCsTable(payloadDto);
    }

    @PostMapping("/schools-registered-vidyaanjali-portal-table")
    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.schoolsRegisteredVidyaanjaliPortalTable(payloadDto);
    }

    @PostMapping("/teachers-student-score-table")
    public ApiResponse2<?> teachersStudentScoreTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.teachersStudentScoreTable(payloadDto);
    }

    @PostMapping("/parents-teacher-meeting-table")
    public ApiResponse2<?> frequencyParentTeacherMeetingTable(@RequestBody TeachersPayloadDto payloadDto){
        return teachersServices.frequencyParentTeacherMeetingTable(payloadDto);
    }



}
