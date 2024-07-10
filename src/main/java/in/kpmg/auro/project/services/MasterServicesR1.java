package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MasterServicesR1 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> getDashboadStatsData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

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

        StringBuilder query2 = new StringBuilder(query);

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2023-11-01");
            parameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters2.add("2023-12-01");
            parameters2.add("2023-12-31");
        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?");
            query2.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }

        query.append("\tGROUP BY\n" +
                "\t\ted.user_id\n" +
                ") AS exam_stats\n" +
                "GROUP BY quiz_count\n" +
                "ORDER BY quiz_count ");

        query2.append("\tGROUP BY\n" +
                "\t\ted.user_id\n" +
                ") AS exam_stats\n" +
                "GROUP BY quiz_count\n" +
                "ORDER BY quiz_count ");

        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------------------------");
        System.out.println(parameters2);
        System.out.println(query2);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));


        return  new ApiResponse2<>(true, "Micro scholarship Quizzes - Average Score Stats Fetched",response, HttpStatus.OK.value());



    }

    public ApiResponse2<?> subjectWiseBreakdownAvgScore(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\ted.subject,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(score) AS average_score\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN(\n" +
                "\tSELECT\n" +
                "\t\tuser_id,\n" +
                "        AVG(amount) as avg_scholarship\n" +
                "\tFROM\n" +
                "\t\tstudent_wallet\n" +
                "\tWHERE\n" +
                "\t\tamount_status IN ('2','4','5')\n" +
                "\tGROUP BY\n" +
                "\t\tuser_id\n" +
                ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());
        }

//        if (!payloadDto.getGrades().isEmpty() && payloadDto.getGrades()!=null){
//            query.append("\t\tAND sm.grade IN (");
//            for (int i=0 ; i< payloadDto.getGrades().size();i++){
//                query.append("?");
//                if (i<payloadDto.getGrades().size() -1){
//                    query.append(", ");
//                }
//            }
//            query.append(") \n");
//            parameters.add(payloadDto.getGrades());
//        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());
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

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getAvgScholorFrom());
            parameters.add(payloadDto.getAvgScholorTo());
        }

        query.append("GROUP BY ed.subject\n" +
                        "ORDER BY ed.subject ");

        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Subject Wise Breakdown Average Score Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> gradeWiseAvgScore(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder( "SELECT\n" +
                "\tsm.grade,\n" +
                "\tAVG(ed.score) AS average_score,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students_attempting\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN(\n" +
                "\tSELECT\n" +
                "\t\tuser_id,\n" +
                "        AVG(amount) as avg_scholarship\n" +
                "\tFROM\n" +
                "\t\tstudent_wallet\n" +
                "\tWHERE\n" +
                "\t\tamount_status IN ('2','4','5')\n" +
                "\tGROUP BY\n" +
                "\t\tuser_id\n" +
                ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
//                "\t\tAND ed.quiz_attempt IN (1,2,3)\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n"
        );


        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());
        }

//        if (!payloadDto.getGrades().isEmpty() && payloadDto.getGrades()!=null){
//            query.append("\t\tAND sm.grade IN (");
//            for (int i=0 ; i< payloadDto.getGrades().size();i++){
//                query.append("?");
//                if (i<payloadDto.getGrades().size() -1){
//                    query.append(", ");
//                }
//            }
//            query.append(") \n");
//            parameters.add(payloadDto.getGrades());
//        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());
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

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getAvgScholorFrom());
            parameters.add(payloadDto.getAvgScholorTo());
        }

        query.append("GROUP BY sm.grade\n" +
                "ORDER BY sm.grade ");

        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Grade Wise Average Score Stats Fetched",response, HttpStatus.OK.value());


    }


    public ApiResponse2<?> topicWiseAvgScore(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tsubquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(CASE WHEN subquery.state_id = 1 THEN subquery.score ELSE NULL END) AS avg_score_region,\n" +
                "    AVG(subquery.score) AS avg_score_pan_india\n" +
                "    \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        ed.score,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        ed.eklavvya_exam_id,\n" +
                "        ed.attempted,\n" +
                "        sm.grade,\n" +
                "        aqn.quiz_name,\n" +
                "        sw.amount_status,\n" +
                "        sw.transaction_date,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sed.school_location,\n" +
                "        sed.social_group,\n" +
                "        aqn.language_id,\n" +
                "        lm.language_name\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
                "\t\tAND sm.grade = aqn. student_class\n" +
                "        AND ed.subject = aqn.subject\n" +
                "\tJOIN\n" +
                "\t\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN(\n" +
                "\tSELECT\n" +
                "\t\tuser_id,\n" +
                "        AVG(amount) as avg_scholarship\n" +
                "\tFROM\n" +
                "\t\tstudent_wallet\n" +
                "\tWHERE\n" +
                "\t\tamount_status IN ('2','4','5')\n" +
                "\tGROUP BY\n" +
                "\t\tuser_id\n" +
                ") avg_sch ON ed.user_id = avg_sch.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "        AND sw.amount_status IN ('2','4','5')\n"
        );

        List<Object> parameters = new ArrayList<>();


        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ?");
            parameters.add(payloadDto.getLanguageId());
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

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getAvgScholorFrom());
            parameters.add(payloadDto.getAvgScholorTo());
        }

        query.append("\tLIMIT 10000) AS subquery \n" +
                " GROUP BY \n" +
                "\tsubquery.quiz_name\n" +
                " ORDER BY \n" +
                "\tsubquery.quiz_name");

        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Average Score Stats Fetched",response, HttpStatus.OK.value());

    }




}
