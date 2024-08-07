package in.kpmg.auro.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpDto {

    private String otp;
    private String email;
//    private Timestamp time;
}
