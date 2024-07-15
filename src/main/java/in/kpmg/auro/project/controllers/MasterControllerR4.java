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


    @PostMapping("/students-taking-vocational-courses")
    public ApiResponse2<?> studentsTakingVocationalCourses(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentTakingVocationalCourse(payloadDto);
    }

    @PostMapping("/students-want-access-vocational-courses")
    public ApiResponse2<?> studentsWantAccessVocationalCourses(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessVocationalCourses(payloadDto);
    }

    @PostMapping("/students-want-access-internship")
    public ApiResponse2<?> studentsWantAccessInternship(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsWantAccessInternship(payloadDto);
    }

    @PostMapping("/students-likely-attend-higher-education")
    public ApiResponse2<?> studentsLikelyAttendHigherEducation(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentsLikelyAttendHigherEducation(payloadDto);
    }

    @PostMapping("/students-confident-knowledge-career-options")
    public ApiResponse2<?> numberOfStudentsConfidentKnowledgeOfCareerOptions(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.numberOfStudentsConfidentKnowledgeOfCareerOptions(payloadDto);
    }

    @PostMapping("/student-career-domains")
    public ApiResponse2<?> studentCareerDomain(@RequestBody RFourPayloadDto payloadDto){
        return masterServicesR4.studentCareerDomain(payloadDto);

    }

}
