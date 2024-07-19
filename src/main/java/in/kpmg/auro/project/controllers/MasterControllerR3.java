package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RThreePayloadDto;
import in.kpmg.auro.project.services.MasterServicesR3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r3")
public class MasterControllerR3 {

    @Autowired
    private MasterServicesR3 masterServicesR3;


    @PostMapping("/student-learning-style-stats")
    public ApiResponse2<?> studentLearningStyleStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentLearningStyleStats(payloadDto);
    }

    @PostMapping("/student-collaborative-learning-stats")
    public ApiResponse2<?> studentCollaborativeLearningStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentCollaborativeLearningStats(payloadDto);
    }

    @PostMapping("/student-read-material-addition-textbooks-stats")
    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.childrenReadOtherMaterialsInAdditionTextbooksStats(payloadDto);
    }

    @PostMapping("/hours-individual-study-per-day-stats")
    public ApiResponse2<?> hoursOfIndividualStudyPerDayStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursOfIndividualStudyPerDayStats(payloadDto);
    }

    @PostMapping("/paid-private-tuition-hours-stats")
    public ApiResponse2<?> paidPrivateTuitionHourStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.paidPrivateTuitionHourStats(payloadDto);
    }

    @PostMapping("/student-hours-spend-mobile-study-stats")
    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursSpentOnMobilePhonesStudyStats(payloadDto);
    }


    @PostMapping("/student-access-digital-devices-home-stats")
    public ApiResponse2<?> accessToDigitalDevicesAtHomeStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentAccessToDigitalDevicesAtHomeStats(payloadDto);
    }


    @PostMapping("/student-using-learning-apps-home-stats")
    public ApiResponse2<?> studentsUsingLearningAppHomeStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentsUsingLearningAppHomeStats(payloadDto);
    }


    @PostMapping("/student-with-social-media-account-stats")
    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccounts(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentWhoHaveOneOrMoreSocialMediaAccounts(payloadDto);
    }


    @PostMapping("/type-of-sites-stats")
    public ApiResponse2<?> typeOfSiteStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.typeOfSiteStats(payloadDto);
    }

}
