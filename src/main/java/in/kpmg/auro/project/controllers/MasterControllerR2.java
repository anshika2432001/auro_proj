package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
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


    @PostMapping("/student-strength-classroom")
    public ApiResponse2<?> studentStrengthOfClassroom (@RequestBody RTwoPayloadDto payloadDto){
        return masterServicesR2.studentStrengthOfClassroom(payloadDto);
    }


    @PostMapping("/academic-streams")
    public ApiResponse2<?> academicStreams(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.academicStreams(payloadDto);
    }

    @PostMapping("/social-group")
    public ApiResponse2<?> socialGroup(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.socialGroup(payloadDto);
    }


    @PostMapping("/student-access-bank")
    public ApiResponse2<?> studentAccessBank(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentAccessBankAccount(payloadDto);
    }

    @PostMapping("/student-engagement-activities-school")
    public ApiResponse2<?> studentEngagementActivitiesSchool(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentEngagementActivitiesSchool(payloadDto);
    }

    @PostMapping("/student-leadership-position-school-clubs")
    public ApiResponse2<?> studentsInLeadershipPositionsInSchoolClubs(@RequestBody RTwoPayloadDto payloadDto){

        return masterServicesR2.studentsInLeadershipPositionsInSchoolClubs(payloadDto);
    }



//    --------------------------------------------------------------------------------------



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

}
