package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.services.ParentAndHouseholdServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/parent-household")

public class ParentAndHouseholdController {

    @Autowired
    private ParentAndHouseholdServices parentAndHouseholdServices;


    @PostMapping("/immigrant background-stats")
    public ApiResponse2<?> immigrantBackgroundStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.immigrantBackgroudStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-studied-twelfth-stats")
    public ApiResponse2<?> parentStudiedTwelfthStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentStudiedTwelfthStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-annual-expenditure-child-education-stats")
    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentAnnualExpenditureOnChildEducationStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/household-with-internet-connection-stats")
    public ApiResponse2<?> householdWithInternetConnectionStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWithInternetConnectionStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/mother-education-qualification-stats")
    public ApiResponse2<?> motherEducationQualificationStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.motherEducationQualificationStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/family-members-know-computer-stats")
    public ApiResponse2<?> anyoneInTheFamilyWhoKnowsToOperateComputerStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.anyoneInTheFamilyWhoKnowsToOperateComputerStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/house-type-stats")
    public ApiResponse2<?> houseTypeStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.houseTypeStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/household-with-other-reading-materials-stats")
    public ApiResponse2<?> householdWhichHaveOtherReadingMaterialsStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWhichHaveOtherReadingMaterialsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/father-education-qualification-stats")
    public ApiResponse2<?> fatherEducationQualificationStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.fatherEducationQualificationStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/average-income-household-stats")
    public ApiResponse2<?> averageIncomeOfHouseholdStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.averageIncomeOfHouseholdStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/household-with-electricity-connection-stats")
    public ApiResponse2<?> householdWithElectricityConnectionStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWithElectricityConnectionStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/adult-reads-with-child-read-everyday-stats")
    public ApiResponse2<?> adultAtHomeReadsWithChildEverydayStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.adultAtHomeReadsWithChildEverydayStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/communication-with-teacher-once-monthly-stats")
    public ApiResponse2<?> communicationWithTeacherOnceAMonthStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.communicationWithTeacherOnceAMonthStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-expects-child-graduate-from-high-school-stats")
    public ApiResponse2<?> parentExpectsChildWillGraduateFromHighSchoolOneDayStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentExpectsChildWillGraduateFromHighSchoolOneDayStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-expects-child-goto-college-stats")
    public ApiResponse2<?> parentExpectsChildWillGoToCollegeOneDayStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentExpectsChildWillGoToCollegeOneDayStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/frequency-parent-teacher-meeting-stats")
    public ApiResponse2<?> frequencyOfParentTeacherMeetingStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.frequencyOfParentTeacherMeetingStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/school-guide-parents-support-children-learning-stats")
    public ApiResponse2<?> schoolProvidesGuidanceOnHowParentSupportChildrenStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.schoolProvidesGuidanceOnHowParentSupportChildrenStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/percent-schools-parents-aware-learning-levels-stats")
    public ApiResponse2<?> percentOfSchoolsParentsAwareOfLearningLevelsStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.percentOfSchoolsParentsAwareOfLearningLevelsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-inform-parents-school-activity-stats")
    public ApiResponse2<?> schoolsInformParentsAboutSchoolActivityStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.schoolsInformParentsAboutSchoolActivityStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/attend-parent-teacher-conferences-stats")
    public ApiResponse2<?> attendParentTeacherConferencesStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.attendParentTeacherConferencesStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-participate-events-child-school-stats")
    public ApiResponse2<?> parentParticipateInEventAtChildSchoolStats(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentParticipateInEventAtChildSchoolStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


//    ---------------------------------------TABLE---------------------------------------


    @PostMapping("/immigrant background-table")
    public ApiResponse2<?> immigrantBackgroundTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.immigrantBackgroundTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-studied-twelfth-table")
    public ApiResponse2<?> parentStudiedTwelfthTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentStudiedTwelfthTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parents-annual-expenditure-child-education-table")
    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentAnnualExpenditureOnChildEducationTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/household-with-internet-connection-table")
    public ApiResponse2<?> householdWithInternetConnectionTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWithInternetConnectionTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/mother-education-qualification-table")
    public ApiResponse2<?> motherEducationQualificationTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.motherEducationQualificationTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/family-members-know-computer-table")
    public ApiResponse2<?> anyoneInTheFamilyWhoKnowsToOperateComputerTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.anyoneInTheFamilyWhoKnowsToOperateComputerTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/house-type-table")
    public ApiResponse2<?> houseTypeTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.houseTypeTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/household-with-other-reading-materials-table")
    public ApiResponse2<?> householdWhichHaveOtherReadingMaterialsTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWhichHaveOtherReadingMaterialsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/father-education-qualification-table")
    public ApiResponse2<?> fatherEducationQualificationTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.fatherEducationQualificationTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/average-income-household-table")
    public ApiResponse2<?> averageIncomeOfHouseholdTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.averageIncomeOfHouseholdTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/household-with-electricity-connection-table")
    public ApiResponse2<?> householdWithElectricityConnectionTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.householdWithElectricityConnectionTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/adult-reads-with-child-read-everyday-table")
    public ApiResponse2<?> adultAtHomeReadsWithChildEverydayTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.adultAtHomeReadsWithChildEverydayTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/communication-with-teacher-once-monthly-table")
    public ApiResponse2<?> communicationWithTeacherOnceAMonthTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.communicationWithTeacherOnceAMonthTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-expects-child-graduate-from-high-school-table")
    public ApiResponse2<?> parentExpectsChildWillGraduateFromHighSchoolOneDayTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentExpectsChildWillGraduateFromHighSchoolOneDayTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-expects-child-goto-college-table")
    public ApiResponse2<?> parentExpectsChildWillGoToCollegeOneDayTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentExpectsChildWillGoToCollegeOneDayTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/frequency-parent-teacher-meeting-table")
    public ApiResponse2<?> frequencyOfParentTeacherMeetingTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.frequencyOfParentTeacherMeetingTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/school-guide-parents-support-children-learning-table")
    public ApiResponse2<?> schoolProvidesGuidanceOnHowParentSupportChildrenTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.schoolProvidesGuidanceOnHowParentSupportChildrenTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/percent-schools-parents-aware-learning-levels-table")
    public ApiResponse2<?> percentOfSchoolsParentsAwareOfLearningLevelsTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.percentOfSchoolsParentsAwareOfLearningLevelsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/schools-inform-parents-school-activity-table")
    public ApiResponse2<?> schoolsInformParentsAboutSchoolActivityTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.schoolsInformParentsAboutSchoolActivityTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/attend-parent-teacher-conferences-table")
    public ApiResponse2<?> attendParentTeacherConferencesTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.attendParentTeacherConferencesTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/parent-participate-events-child-school-table")
    public ApiResponse2<?> parentParticipateInEventAtChildSchoolTable(@RequestBody ROnePayloadDto payloadDto) {
        try {
            return parentAndHouseholdServices.parentParticipateInEventAtChildSchoolTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


//    DONE

}
