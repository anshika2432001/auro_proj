package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Integer userId;
    private String userName;
    private String password;
    private String email;

    private Long mobileNo;

    private Integer roleId;
    private Map<String,Object> roleTypeDetails;

}
