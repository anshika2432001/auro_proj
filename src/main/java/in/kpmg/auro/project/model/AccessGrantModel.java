package in.kpmg.auro.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class AccessGrantModel implements Serializable {

    private Integer roleId;
    private String dashboardName;

}
