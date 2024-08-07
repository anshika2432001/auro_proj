package in.kpmg.auro.project.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.UserDto;
import in.kpmg.auro.project.model.RoleAccessGrantsMst;
import in.kpmg.auro.project.model.RoleMst;
import in.kpmg.auro.project.model.UserLogin;
import in.kpmg.auro.project.model.UserMst;
import in.kpmg.auro.project.repo.LoginRepo;
import in.kpmg.auro.project.repo.RoleAccessGrantsRepo;
import in.kpmg.auro.project.repo.RoleRepo;
import in.kpmg.auro.project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;


//    private UserMapper userMapper;

//    @Autowired
//    private UserRoleMapRepo userRoleRepo;
//
//    @Autowired
//    private RoleMenuRightMapRepo menuRightMapRepo;

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private RoleAccessGrantsRepo accessGrantsRepo;

    private final ObjectMapper objectMapper= new ObjectMapper();


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserMst userdata = userRepo.getUserDataByUserName(username.toLowerCase());
        if (userdata == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(userdata.getEmail(), userdata.getPassword(),
                new ArrayList<>());
    }


    public Boolean checkPasswordHash(String username, String password) {
        Integer hashTimes = loginRepo.checkPasswordHash(username, password);
        System.out.println(hashTimes+"--------------------------------");
        return hashTimes > 0 ? false : true;

    }


    @Transactional
    public Integer saveUserLogin(String username, Integer status, String passowrd, String ipAddress) {
        UserLogin loginDetails = new UserLogin();
        int k = 0;
        try {
            loginDetails.setUserName(username);
            loginDetails.setStatus(status);
            loginDetails.setPassword_hash(passowrd);
            loginDetails.setIpAddress(ipAddress);
            loginRepo.save(loginDetails);


        } catch (Exception ex) {
            ex.getMessage();
        }

        return k;
    }

    public Map<String, Object> getLoginUserData(String username) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        UserMst userData = userRepo.getUserDataByUserName(username);
//        UserDto useDto = userMapper.modelToDto(userData);
        UserDto userDto= new UserDto();
        userDto.setUserId(userData.getUserId());
        userDto.setEmail(userData.getEmail());
        userDto.setUserName(userData.getName());
//        userDto.setMobileNo(userData.getMobileNo());
        userDto.setRoleId(userData.getRoleId());
        Map detailsMap= objectMapper.readValue(userData.getRoleTypeDetails(),Map.class);
        userDto.setRoleTypeDetails(detailsMap);


        if (userData.getRoleId() !=null){
            Optional<RoleMst> roleMaps = roleRepo.findById(userData.getRoleId());
            response.put("roleMap", roleMaps);
        }


        response.put("user", userDto);

        return response;
    }

    public UserMst getUserDetailsByUserName(String username) {
        return userRepo.getUserDataByUserName(username);
    }

    public ResponseEntity<?> validateTokenUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {

                return new ResponseEntity<>(
                        new ApiResponse2<>(true, "Validation Successful", null, HttpStatus.OK.value()), HttpStatus.OK);

            } else
                throw new SecurityException("Unauthorized Access!");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse2<>(false, e.getMessage(), null, HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED);
        }
    }


//    public Map<String, Object> getDynamicMenu(Integer userId) {
//        Map<String, Object> response = new HashMap<>();
//
//        UserMst user;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//
//            Optional<UserMst> optionalAuthenticationUser = this.userRepo.findByUserName(authentication.getName());
//
//            if (!optionalAuthenticationUser.isPresent())
//                throw new SecurityException("Unauthorized Access!");
//
//            user = optionalAuthenticationUser.get();
//
//        } else {
//            throw new SecurityException("Unauthorized Access!");
//        }
//
//        List<RoleMenuRightProj> parentMenu = menuRightMapRepo.getMainMenuList(user.getUserId());
//
//        List<RoleMenuRightProj> parentMenuWOduplicate = parentMenu.stream().collect(
//                collectingAndThen(toCollection(() -> new TreeSet<>(comparingInt(RoleMenuRightProj::getMenuId))),
//                        ArrayList::new));
//        List<RoleMenuRightProj> subMenu = menuRightMapRepo.getSubMenuList(user.getUserId());
//        List<RoleMenuRightProj> subMenuWoDuplicate = subMenu.stream().collect(collectingAndThen(
//                toCollection(() -> new TreeSet<>(comparingInt(RoleMenuRightProj::getMenuId))), ArrayList::new));
//        response.put("parentMenuList", parentMenuWOduplicate);
//        response.put("subMenuList", subMenuWoDuplicate);
//
//        return response;
//    }


}
