package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAccessGrantDto {

    private Integer roleId;
    private String dashboardName;
    private Integer grantAccess;

}
