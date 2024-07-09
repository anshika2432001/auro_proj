package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ROnePayloadDto {

    private String transactionDateFrom;
    private String transactionDateTo;
    private List<Integer> grades;
    private String subject;
    private Integer schoolLocation;
    private Integer stateId;
    private Integer districtId;
    private Integer socialGroup;
    private String gender;
    private String dob;
    private String educationBoard;
    private String schoolManagement;


}
