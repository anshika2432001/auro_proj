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

    @PostMapping("/student-hours-spend-mobile-entertainment-stats")
    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentHoursSpendMobileForEntertainmentStats(payloadDto);
    }

    @PostMapping("/paid-private-tuition-subjects-stats")
    public ApiResponse2<?> paidPrivateTuitionSubjectsStats(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.paidPrivateTuitionSubjectsStats(payloadDto);
    }


//    --------------------------------TABLES---------------------------------


    @PostMapping("/student-learning-style-table")
    public ApiResponse2<?> studentLearningStyleTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentLearningStyleTable(payloadDto);
    }

    @PostMapping("/student-collaborative-learning-table")
    public ApiResponse2<?> studentCollaborativeLearningTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentCollaborativeLearningTable(payloadDto);
    }

    @PostMapping("/student-read-material-addition-textbooks-table")
    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.childrenReadOtherMaterialsInAdditionTextbooksTable(payloadDto);
    }

    @PostMapping("/hours-individual-study-per-day-table")
    public ApiResponse2<?> hoursOfIndividualStudyPerDayTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursOfIndividualStudyPerDayTable(payloadDto);
    }

    @PostMapping("/paid-private-tuition-hours-table")
    public ApiResponse2<?> paidPrivateTuitionHourTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.paidPrivateTuitionHourTable(payloadDto);
    }

    @PostMapping("/student-hours-spend-mobile-study-table")
    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursSpentOnMobilePhonesStudyTable(payloadDto);
    }


    @PostMapping("/student-access-digital-devices-home-table")
    public ApiResponse2<?> accessToDigitalDevicesAtHomeTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentAccessToDigitalDevicesAtHomeTable(payloadDto);
    }


    @PostMapping("/student-using-learning-apps-home-table")
    public ApiResponse2<?> studentsUsingLearningAppHomeTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentsUsingLearningAppHomeTable(payloadDto);
    }


    @PostMapping("/student-with-social-media-account-table")
    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccountsTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentWhoHaveOneOrMoreSocialMediaAccountsTable(payloadDto);
    }


    @PostMapping("/type-of-sites-table")
    public ApiResponse2<?> typeOfSiteTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.typeOfSiteTable(payloadDto);
    }

    @PostMapping("/student-hours-spend-mobile-entertainment-table")
    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentHoursSpendMobileForEntertainmentTable(payloadDto);
    }

    @PostMapping("/paid-private-tuition-subjects-table")
    public ApiResponse2<?> paidPrivateTuitionSubjectsTable(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.paidPrivateTuitionSubjectsTable(payloadDto);
    }



}
