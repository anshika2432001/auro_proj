package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.query.DashboardQuery;
import in.kpmg.auro.project.query.ROneQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MasterServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> getDashboadStatsData(ROnePayloadDto payloadDto) {

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tquiz_count AS quiz_range,\n" +
                "\tCOUNT(DISTINCT user_id) AS num_students,\n" +
                "\tAVG(score) AS average_score\n" +
                "FROM(\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS score\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n"
        );

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom() != null
                && payloadDto.getTransactionDateTo()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom());
            parameters.add(payloadDto.getTransactionDateTo());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());
        }

        if (payloadDto.getDob() !=null){
            query.append("\tAND timestampdiff(YEAR, ?, CURDATE()) BETWEEN 11 and 13 \n");
            parameters.add(payloadDto.getDob());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());
        }

        query.append("\tGROUP BY\n" +
                "\t\ted.user_id\n" +
                ") AS exam_stats\n" +
                "GROUP BY quiz_count\n" +
                "ORDER BY quiz_count ");

        System.out.println(parameters);
        System.out.println(query);
        return  new ApiResponse2<>(true, "Dashboard Stats Fetched",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()), HttpStatus.OK.value());



    }
}
