package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RFourPayloadDto;
import in.kpmg.auro.project.services.MasterServicesR4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r4")
public class MasterControllerR4 {

    @Autowired
    private MasterServicesR4 masterServicesR4;


    @PostMapping("/students-taking-vocational-courses-stats")
    public ApiResponse2<?> studentsTakingVocationalCoursesStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentTakingVocationalCourseStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-want-access-vocational-courses-stats")
    public ApiResponse2<?> studentsWantAccessVocationalCoursesStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsWantAccessVocationalCoursesStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-want-access-internship-stats")
    public ApiResponse2<?> studentsWantAccessInternshipStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsWantAccessInternshipStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-likely-attend-higher-education-stats")
    public ApiResponse2<?> studentsLikelyAttendHigherEducationStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsLikelyAttendHigherEducationStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-confident-knowledge-career-options-stats")
    public ApiResponse2<?> numberOfStudentsConfidentKnowledgeOfCareerOptionsStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.numberOfStudentsConfidentKnowledgeOfCareerOptionsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/student-career-domains-stats")
    public ApiResponse2<?> studentCareerDomainStats(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentCareerDomainStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }

    }


//    ----------------------------------TABLE----------------------------------------

    @PostMapping("/students-taking-vocational-courses-table")
    public ApiResponse2<?> studentsTakingVocationalCoursesTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentTakingVocationalCourseTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-want-access-vocational-courses-table")
    public ApiResponse2<?> studentsWantAccessVocationalCoursesTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsWantAccessVocationalCoursesTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-want-access-internship-table")
    public ApiResponse2<?> studentsWantAccessInternshipTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsWantAccessInternshipTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-likely-attend-higher-education-table")
    public ApiResponse2<?> studentsLikelyAttendHigherEducationTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentsLikelyAttendHigherEducationTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/students-confident-knowledge-career-options-table")
    public ApiResponse2<?> numberOfStudentsConfidentKnowledgeOfCareerOptionsTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.numberOfStudentsConfidentKnowledgeOfCareerOptionsTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }
    }

    @PostMapping("/student-career-domains-table")
    public ApiResponse2<?> studentCareerDomainTables(@RequestBody RFourPayloadDto payloadDto) {
        try {
            return masterServicesR4.studentCareerDomainTables(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());
        }

    }


}
