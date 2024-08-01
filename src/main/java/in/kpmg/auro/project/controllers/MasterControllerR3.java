package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RThreePayloadDto;
import in.kpmg.auro.project.services.MasterServicesR3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/r3")
public class MasterControllerR3 {

    @Autowired
    private MasterServicesR3 masterServicesR3;


    @PostMapping("/student-learning-style-stats")
    public ApiResponse2<?> studentLearningStyleStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentLearningStyleStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }

    }

    @PostMapping("/student-collaborative-learning-stats")
    public ApiResponse2<?> studentCollaborativeLearningStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentCollaborativeLearningStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-read-material-addition-textbooks-stats")
    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.childrenReadOtherMaterialsInAdditionTextbooksStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/hours-individual-study-per-day-stats")
    public ApiResponse2<?> hoursOfIndividualStudyPerDayStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.hoursOfIndividualStudyPerDayStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/paid-private-tuition-hours-stats")
    public ApiResponse2<?> paidPrivateTuitionHourStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.paidPrivateTuitionHourStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-hours-spend-mobile-study-stats")
    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.hoursSpentOnMobilePhonesStudyStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-access-digital-devices-home-stats")
    public ApiResponse2<?> accessToDigitalDevicesAtHomeStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentAccessToDigitalDevicesAtHomeStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-using-learning-apps-home-stats")
    public ApiResponse2<?> studentsUsingLearningAppHomeStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentsUsingLearningAppHomeStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-with-social-media-account-stats")
    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccounts(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentWhoHaveOneOrMoreSocialMediaAccounts(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/type-of-sites-stats")
    public ApiResponse2<?> typeOfSiteStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.typeOfSiteStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-hours-spend-mobile-entertainment-stats")
    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentHoursSpendMobileForEntertainmentStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/paid-private-tuition-subjects-stats")
    public ApiResponse2<?> paidPrivateTuitionSubjectsStats(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.paidPrivateTuitionSubjectsStats(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


//    --------------------------------TABLES---------------------------------


    @PostMapping("/student-learning-style-table")
    public ApiResponse2<?> studentLearningStyleTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentLearningStyleTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-collaborative-learning-table")
    public ApiResponse2<?> studentCollaborativeLearningTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentCollaborativeLearningTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-read-material-addition-textbooks-table")
    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.childrenReadOtherMaterialsInAdditionTextbooksTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/hours-individual-study-per-day-table")
    public ApiResponse2<?> hoursOfIndividualStudyPerDayTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.hoursOfIndividualStudyPerDayTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/paid-private-tuition-hours-table")
    public ApiResponse2<?> paidPrivateTuitionHourTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.paidPrivateTuitionHourTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-hours-spend-mobile-study-table")
    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.hoursSpentOnMobilePhonesStudyTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-access-digital-devices-home-table")
    public ApiResponse2<?> accessToDigitalDevicesAtHomeTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentAccessToDigitalDevicesAtHomeTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-using-learning-apps-home-table")
    public ApiResponse2<?> studentsUsingLearningAppHomeTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentsUsingLearningAppHomeTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/student-with-social-media-account-table")
    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccountsTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentWhoHaveOneOrMoreSocialMediaAccountsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


    @PostMapping("/type-of-sites-table")
    public ApiResponse2<?> typeOfSiteTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.typeOfSiteTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/student-hours-spend-mobile-entertainment-table")
    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.studentHoursSpendMobileForEntertainmentTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }

    @PostMapping("/paid-private-tuition-subjects-table")
    public ApiResponse2<?> paidPrivateTuitionSubjectsTable(@RequestBody RThreePayloadDto payloadDto) {
        try {
            return masterServicesR3.paidPrivateTuitionSubjectsTable(payloadDto);
        } catch (Exception e) {
            return new ApiResponse2<>(false, "Facing Problem While Fetching Data...", null, HttpStatus.NO_CONTENT.value());

        }
    }


}
