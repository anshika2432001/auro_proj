package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RTwoPayloadDto {


    private String transactionDateFrom1;
    private String transactionDateTo1;
    private String transactionDateFrom2;
    private String transactionDateTo2;
    private Integer grades;
    private String subject;
    private Integer schoolLocation;
    private Integer stateId;
    private Integer districtId;
    private Integer socialGroup;
    private String gender;
    private Integer ageFrom;
    private Integer ageTo;
    private String educationBoard;
    private String schoolManagement;
    private Integer cwsn;
    private Integer childMotherQualification;
    private Integer childFatherQualification;

}
