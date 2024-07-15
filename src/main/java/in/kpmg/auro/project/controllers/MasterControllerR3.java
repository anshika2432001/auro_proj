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


    @PostMapping("/student-learning-style")
    public ApiResponse2<?> studentLearningStyle(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentLearningStyle(payloadDto);
    }

    @PostMapping("/student-collaborative-learning")
    public ApiResponse2<?> studentCollaborativeLearning(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentCollaborativeLearning(payloadDto);
    }

    @PostMapping("/student-read-material-addition-textbooks")
    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooks(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.childrenReadOtherMaterialsInAdditionTextbooks(payloadDto);
    }

    @PostMapping("/hours-individual-study-per-day")
    public ApiResponse2<?> hoursOfIndividualStudyPerDay(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursOfIndividualStudyPerDay(payloadDto);
    }

    @PostMapping("/paid-private-tuition-hours")
    public ApiResponse2<?> paidPrivateTuitionHour(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.paidPrivateTuitionHour(payloadDto);
    }

    @PostMapping("/student-hours-spend-mobile-study")
    public ApiResponse2<?> hoursSpentOnMobilePhonesStudy(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.hoursSpentOnMobilePhonesStudy(payloadDto);
    }


    @PostMapping("/student-access-digital-devices-home")
    public ApiResponse2<?> accessToDigitalDevicesAtHome(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentAccessToDigitalDevicesAtHome(payloadDto);
    }


    @PostMapping("/student-using-learning-apps-home")
    public ApiResponse2<?> studentsUsingLearningAppHome(@RequestBody RThreePayloadDto payloadDto){
        return masterServicesR3.studentsUsingLearningAppHome(payloadDto);
    }



}
