package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RFourPayloadDto;
import in.kpmg.auro.project.dtos.RThreePayloadDto;
import in.kpmg.auro.project.services.MasterServicesR4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r4")
public class MasterControllerR4 {

    @Autowired
    private MasterServicesR4 masterServicesR4;


    @PostMapping("/students-taking-vocational-courses-stats")
    public ApiResponse2<?> studentsTakingVocationalCoursesStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentTakingVocationalCourseStats(payloadDto);
    }

    @PostMapping("/students-want-access-vocational-courses-stats")
    public ApiResponse2<?> studentsWantAccessVocationalCoursesStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessVocationalCoursesStats(payloadDto);
    }

    @PostMapping("/students-want-access-internship-stats")
    public ApiResponse2<?> studentsWantAccessInternshipStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessInternshipStats(payloadDto);
    }

    @PostMapping("/students-likely-attend-higher-education-stats")
    public ApiResponse2<?> studentsLikelyAttendHigherEducationStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsLikelyAttendHigherEducationStats(payloadDto);
    }

    @PostMapping("/students-confident-knowledge-career-options-stats")
    public ApiResponse2<?> numberOfStudentsConfidentKnowledgeOfCareerOptionsStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.numberOfStudentsConfidentKnowledgeOfCareerOptionsStats(payloadDto);
    }

    @PostMapping("/student-career-domains-stats")
    public ApiResponse2<?> studentCareerDomainStats(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentCareerDomainStats(payloadDto);

    }


//    ----------------------------------TABLE----------------------------------------

    @PostMapping("/students-taking-vocational-courses-table")
    public ApiResponse2<?> studentsTakingVocationalCoursesTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentTakingVocationalCourseTables(payloadDto);
    }

    @PostMapping("/students-want-access-vocational-courses-table")
    public ApiResponse2<?> studentsWantAccessVocationalCoursesTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessVocationalCoursesTables(payloadDto);
    }

    @PostMapping("/students-want-access-internship-table")
    public ApiResponse2<?> studentsWantAccessInternshipTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessInternshipTables(payloadDto);
    }

    @PostMapping("/students-likely-attend-higher-education-table")
    public ApiResponse2<?> studentsLikelyAttendHigherEducationTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsLikelyAttendHigherEducationTables(payloadDto);
    }

    @PostMapping("/students-confident-knowledge-career-options-table")
    public ApiResponse2<?> numberOfStudentsConfidentKnowledgeOfCareerOptionsTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.numberOfStudentsConfidentKnowledgeOfCareerOptionsTables(payloadDto);
    }

    @PostMapping("/student-career-domains-table")
    public ApiResponse2<?> studentCareerDomainTables(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentCareerDomainTables(payloadDto);

    }




}
