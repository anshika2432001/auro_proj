package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.ParentAndHouseholdServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/parent-household")

public class ParentAndHouseholdController {

    @Autowired
    private ParentAndHouseholdServices parentAndHouseholdServices;


    @PostMapping("/immigrant background-stats")
    public ApiResponse2<?> immigrantBackgroudStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.immigrantBackgroudStats(payloadDto);
    }

    @PostMapping("/parents-studied-twelfth-stats")
    public ApiResponse2<?> parentStudiedTwelfthStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentStudiedTwelfthStats(payloadDto);
    }

    @PostMapping("/parents-annual-expenditure-child-education-stats")
    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentAnnualExpenditureOnChildEducationStats(payloadDto);
    }


    @PostMapping("/household-with-internet-connection-stats")
    public ApiResponse2<?> householdWithInternetConnectionStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.householdWithInternetConnectionStats(payloadDto);
    }

    @PostMapping("/mother-education-qualification-stats")
    public ApiResponse2<?> motherEducationQualificationStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.motherEducationQualificationStats(payloadDto);
    }


    @PostMapping("/family-members-know-computer-stats")
    public ApiResponse2<?> anyoneInTheFamilyWhoKnowsToOperateComputerStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.anyoneInTheFamilyWhoKnowsToOperateComputerStats(payloadDto);
    }


    @PostMapping("/house-type-stats")
    public ApiResponse2<?> houseTypeStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.houseTypeStats(payloadDto);
    }


    @PostMapping("/household-with-other-reading-materials-stats")
    public ApiResponse2<?> householdWhichHaveOtherReadingMaterialsStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.householdWhichHaveOtherReadingMaterialsStats(payloadDto);
    }


    @PostMapping("/father-education-qualification-stats")
    public ApiResponse2<?> fatherEducationQualificationStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.fatherEducationQualificationStats(payloadDto);
    }


    @PostMapping("/average-income-household-stats")
    public ApiResponse2<?> averageIncomeOfHouseholdStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.averageIncomeOfHouseholdStats(payloadDto);
    }

    @PostMapping("/household-with-electricity-connection-stats")
    public ApiResponse2<?> householdWithElectricityConnectionStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.householdWithElectricityConnectionStats(payloadDto);
    }

    @PostMapping("/adult-reads-with-child-read-everyday-stats")
    public ApiResponse2<?> adultAtHomeReadsWithChildEverydayStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.adultAtHomeReadsWithChildEverydayStats(payloadDto);
    }

    @PostMapping("/communication-with-teacher-once-monthly-stats")
    public ApiResponse2<?> communicationWithTeacherOnceAMonthStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.communicationWithTeacherOnceAMonthStats(payloadDto);
    }

    @PostMapping("/parent-expects-child-graduate-from-high-school-stats")
    public ApiResponse2<?> parentExpectsChildWillGraduateFromHighSchoolOneDayStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentExpectsChildWillGraduateFromHighSchoolOneDayStats(payloadDto);
    }

    @PostMapping("/parent-expects-child-goto-college-stats")
    public ApiResponse2<?> parentExpectsChildWillGoToCollegeOneDayStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentExpectsChildWillGoToCollegeOneDayStats(payloadDto);
    }

    @PostMapping("/frequency-parent-teacher-meeting-stats")
    public ApiResponse2<?> frequencyOfParentTeacherMeetingStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.frequencyOfParentTeacherMeetingStats(payloadDto);
    }


    @PostMapping("/school-guide-parents-support-children-learning-stats")
    public ApiResponse2<?> schoolProvidesGuidanceOnHowParentSupportChildrenStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.schoolProvidesGuidanceOnHowParentSupportChildrenStats(payloadDto);
    }

    @PostMapping("/percent-schools-parents-aware-learning-levels-stats")
    public ApiResponse2<?> percentOfSchoolsParentsAwareOfLearningLevelsStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.percentOfSchoolsParentsAwareOfLearningLevelsStats(payloadDto);
    }

    @PostMapping("/schools-inform-parents-school-activity-stats")
    public ApiResponse2<?> schoolsInformParentsAboutSchoolActivityStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.schoolsInformParentsAboutSchoolActivityStats(payloadDto);
    }

    @PostMapping("/attend-parent-teacher-conferences-stats")
    public ApiResponse2<?> attendParentTeacherConferencesStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.attendParentTeacherConferencesStats(payloadDto);
    }

    @PostMapping("/parent-participate-events-child-school-stats")
    public ApiResponse2<?> parentParticipateInEventAtChildSchoolStats(@RequestBody ROnePayloadDto payloadDto){
        return parentAndHouseholdServices.parentParticipateInEventAtChildSchoolStats(payloadDto);
    }

}
