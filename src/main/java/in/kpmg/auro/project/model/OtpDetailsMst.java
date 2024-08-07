package in.kpmg.auro.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "otp_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpDetailsMst {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "otp")
    private String otp;

    @Column(name = "otp_req_time")
    private Timestamp otpReqTime;
}
