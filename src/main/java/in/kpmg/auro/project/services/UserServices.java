package in.kpmg.auro.project.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.kpmg.auro.project.config.AesUtil;
import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ForgotPasswordDto;
import in.kpmg.auro.project.dtos.OtpDto;
import in.kpmg.auro.project.dtos.UserDto;
import in.kpmg.auro.project.model.OtpDetailsMst;
import in.kpmg.auro.project.model.RoleAccessGrantsMst;
import in.kpmg.auro.project.model.RoleMst;
import in.kpmg.auro.project.model.UserMst;
import in.kpmg.auro.project.repo.OtpDetailsRepo;
import in.kpmg.auro.project.repo.RoleAccessGrantsRepo;
import in.kpmg.auro.project.repo.RoleRepo;
import in.kpmg.auro.project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices {


    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpDetailsRepo otpDetailsRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private RoleAccessGrantsRepo accessGrantsRepo;

    private final ObjectMapper objectMapper= new ObjectMapper();


    public ApiResponse2<?> newUserRegistrationService(UserDto dto) {

        try {
            Optional<UserMst> existingData = userRepo.findByEmail(dto.getEmail());
            if (existingData.isPresent()) {
                return new ApiResponse2<>(true, "User Already Exists", null, HttpStatus.FOUND.value());

            }
            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
            UserMst newData = new UserMst();
            AesUtil aes = new AesUtil();
            newData.setName(dto.getUserName());
            newData.setEmail(dto.getEmail());
            newData.setPassword(aes.encrypt(dto.getPassword()));
            newData.setRoleId(dto.getRoleId());
            newData.setMobileNo(dto.getMobileNo());
            String detailsJson= objectMapper.writeValueAsString(dto.getRoleTypeDetails());
            newData.setRoleTypeDetails(detailsJson);
            newData.setCreatedOn(curTimestamp);

            userRepo.save(newData);

            return new ApiResponse2<>(true, "User Registered Successfully", "", HttpStatus.OK.value());
        }catch (Exception e){
            return new ApiResponse2<>(false, "Facing Problem For New Registration", null, HttpStatus.BAD_REQUEST.value());
        }

    }

    public ApiResponse2<?> sendOTPServices(UserDto dto) {

        try {

//        random 6 digit code generation
            String otpCode = generateOtp(6);

//        SMTP service to send otp to the email or phone number
//        dto.getUserName(); to send email or sms


//            store otp details
            OtpDetailsMst newData= new OtpDetailsMst();
            newData.setEmail(dto.getEmail());
            newData.setOtp(otpCode);
            Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
            newData.setOtpReqTime(curTimestamp);

            otpDetailsRepo.save(newData);

            return  new ApiResponse2<>(true, "OTP send Successfully","", HttpStatus.OK.value());

        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return  new ApiResponse2<>(false, "Facing Problem While Sending OTP","", HttpStatus.BAD_REQUEST.value());
        }

    }


    public ApiResponse2<?> verifyOtpService(OtpDto dto) {

        String tempOTP="000111";
        try{

//            Optional<OtpDetailsMst> otpData= otpDetailsRepo.findById(dto.getEmail());
//            if (otpData.isPresent()) {
//
//                OtpDetailsMst data= otpData.get();
//                Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());

//                if (curTimestamp.before(Timestamp.valueOf(data.getOtpReqTime().toLocalDateTime().plusMinutes(3)))){

                    if (tempOTP.equals(dto.getOtp())){
                        return  new ApiResponse2<>(true, "OTP Verified Successfully",true, HttpStatus.OK.value());
                    }else {
                        return  new ApiResponse2<>(false, "Invalid OTP",false, HttpStatus.BAD_REQUEST.value());

                    }

//                }
//                else {
//                    return  new ApiResponse2<>(false, "Entered OTP Expired",false, HttpStatus.BAD_REQUEST.value());
//
//                }

//
//
//            }


        }catch (Exception e){
            System.out.println(e.getStackTrace());
            return  new ApiResponse2<>(false, "Facing Problem While Verifying OTP ",false, HttpStatus.BAD_REQUEST.value());

        }

    }


    public ApiResponse2<?> resendOtpService(UserDto dto) {

        try{

            Optional<OtpDetailsMst> otpData= otpDetailsRepo.findById(dto.getEmail());
            if (otpData.isPresent()){
                OtpDetailsMst data= otpData.get();
                Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());

                if (curTimestamp.before(Timestamp.valueOf(data.getOtpReqTime().toLocalDateTime().plusMinutes(3)))){

//                    resend the otp the email
                    System.out.println(data.getOtp());
                }
                else {
                    String otpCode = generateOtp(6);
                    data.setOtp(otpCode);
                    data.setOtpReqTime(curTimestamp);

                    otpDetailsRepo.save(data);

                }


            }
            return  new ApiResponse2<>(true, "OTP Resend Successfully",true, HttpStatus.OK.value());

        }catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Resending OTP ",false, HttpStatus.BAD_REQUEST.value());

        }
    }

    private String generateOtp(int size) {

        final String DIGITS="0123456789";
        final SecureRandom RANDOM= new SecureRandom();

        StringBuilder otpCode= new StringBuilder(size);
        for (int i=0 ; i<size;i++){
            otpCode.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        }
        System.out.println(otpCode);

        return otpCode.toString();
    }


    public ApiResponse2<?> roleDropDownServices() {

        List<RoleMst> roleMstList= roleRepo.findAll();

        return  new ApiResponse2<>(true, "Roles Dropdown Fetched",roleMstList, HttpStatus.OK.value());

    }

    public ApiResponse2<?> forgotPasswordService(ForgotPasswordDto dto) {

        try {
            if (dto.getStatus()){
                Optional<UserMst> userMst = userRepo.findByEmail(dto.getEmail());
                if (userMst.isPresent()){
                    UserMst data= userMst.get();

                    AesUtil aes = new AesUtil();
                    data.setPassword(aes.encrypt(dto.getPass()));

                    userRepo.save(data);
                }
                else {
                    return  new ApiResponse2<>(false, "No User Found",null, HttpStatus.NOT_FOUND.value());

                }

                return  new ApiResponse2<>(true, "Password Updated Successfully",true, HttpStatus.OK.value());


            }else {
                return  new ApiResponse2<>(false, "Please Verify Your Account",null, HttpStatus.BAD_REQUEST.value());

            }


        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem At this Moment...",null, HttpStatus.BAD_REQUEST.value());


        }

    }

    public ApiResponse2<?> roleAccessGrantService(UserDto dto) {


        List<RoleAccessGrantsMst> roleAccessGrantsMstList= accessGrantsRepo.findAll();


        return  new ApiResponse2<>(true, "Role Based Access Fetch",roleAccessGrantsMstList, HttpStatus.OK.value());

    }
}
