package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachersPayloadDto {

    private String transactionDateFrom1;
    private String transactionDateTo1;
    private String transactionDateFrom2;
    private String transactionDateTo2;

    private Integer stateId;
    private Integer districtId;
    private String gender;
    private Integer qualification;
    private Integer employmentNature;
    private Integer grades;
    private String educationBoard;
    private String schoolManagement;

    private Integer schoolCategory;
    private Integer schoolType;

}
