package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RTwoPayloadDto;
import in.kpmg.auro.project.services.MasterServicesR2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r2")
public class MasterControllerR2 {

    @Autowired
    private MasterServicesR2 masterServicesR2;


    @PostMapping("/student-strength-classroom-stats")
    public ApiResponse2<?> studentStrengthOfClassroomStats(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentStrengthOfClassroomStats(payloadDto);
    }


    @PostMapping("/academic-streams-stats")
    public ApiResponse2<?> academicStreamsStats(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.academicStreamsStats(payloadDto);
    }

    @PostMapping("/social-group-stats")
    public ApiResponse2<?> socialGroupStats(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.socialGroupStats(payloadDto);
    }


    @PostMapping("/student-access-bank-stats")
    public ApiResponse2<?> studentAccessBankStats(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentAccessBankAccountStats(payloadDto);
    }

    @PostMapping("/student-engagement-activities-school-stats")
    public ApiResponse2<?> studentEngagementActivitiesSchoolStats(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentEngagementActivitiesSchoolStats(payloadDto);
    }

    @PostMapping("/student-leadership-position-school-clubs-stats")
    public ApiResponse2<?> studentsInLeadershipPositionsInSchoolClubsStats(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentsInLeadershipPositionsInSchoolClubsStats(payloadDto);
    }


//    --------------------------------TABLES---------------------------------

    @PostMapping("/student-strength-classroom-table")
    public ApiResponse2<?> studentStrengthOfClassroomTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentStrengthOfClassroomTable(payloadDto);
    }


    @PostMapping("/academic-streams-table")
    public ApiResponse2<?> academicStreamsTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.academicStreamsTable(payloadDto);
    }

    @PostMapping("/social-group-table")
    public ApiResponse2<?> socialGroupTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.socialGroupTable(payloadDto);
    }


    @PostMapping("/student-access-bank-table")
    public ApiResponse2<?> studentAccessBankTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentAccessBankAccountTable(payloadDto);
    }

    @PostMapping("/student-engagement-activities-school-table")
    public ApiResponse2<?> studentEngagementActivitiesSchoolTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentEngagementActivitiesSchoolTable(payloadDto);
    }

    @PostMapping("/student-leadership-position-school-clubs-table")
    public ApiResponse2<?> studentsInLeadershipPositionsInSchoolClubsTable(@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentsInLeadershipPositionsInSchoolClubsTable(payloadDto);
    }

}
