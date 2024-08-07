package in.kpmg.auro.project.controllers;

import in.kpmg.auro.project.dtos.*;
import in.kpmg.auro.project.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserServices userServices;


    @PostMapping("/new-user-registration")
    public ApiResponse2<?> newUserRegistration(@RequestBody UserDto dto){
        return userServices.newUserRegistrationService(dto);
    }

//    async email or sms
    @PostMapping("/send-otp")
    public ApiResponse2<?> otpSend(@RequestBody UserDto dto){
        return userServices.sendOTPServices(dto);
    }

    @PostMapping("/verify-otp")
    public ApiResponse2<?> verifyOtp(@RequestBody OtpDto dto){
        return userServices.verifyOtpService(dto);
    }

    @PostMapping("/resend-otp")
    public ApiResponse2<?> verifyOtp(@RequestBody UserDto dto){
        return userServices.resendOtpService(dto);
    }

    @GetMapping("/role-dropdown")
    public ApiResponse2<?> roleDropDown(){
        return userServices.roleDropDownServices();
    }

    @PostMapping("/forgot-password")
    public ApiResponse2<?> forgotPassword(@RequestBody ForgotPasswordDto dto){
        return userServices.forgotPasswordService(dto);
    }

    @PostMapping("/role-access-fetch")
    public ApiResponse2<?> roleAccessGrant(@RequestBody UserDto dto){
        return userServices.roleAccessGrantService(dto);
    }

}
