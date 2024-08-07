package in.kpmg.auro.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "role_access_grants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AccessGrantModel.class)
public class RoleAccessGrantsMst {

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Id
    @Column(name = "dashboard_name")
    private String dashboardName;

    @Column(name = "grant_access")
    private Integer grantAccess;


}
