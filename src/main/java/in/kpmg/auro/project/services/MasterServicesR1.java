package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MasterServicesR1 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> microScholarQuizStats(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    qr.quiz_range,\n" +
                "    COALESCE(subquery.num_students, 0) AS num_students,\n" +
                "    COALESCE(subquery.avg_score, 0) AS average_score\n" +
                "FROM (\n" +
                "    SELECT 1 AS quiz_range UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 \n" +
                "    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10\n" +
                "    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15\n" +
                "    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20\n" +
                ") AS qr\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "        quiz_count,\n" +
                "        COUNT(DISTINCT user_id) AS num_students,\n" +
                "        AVG(avg_score) AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            ed.user_id,\n" +
                "            COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "            AVG(ed.score) AS avg_score\n" +
                "        FROM\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('5') "

        );

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    qr.quiz_range,\n" +
                "    COALESCE(subquery.num_students, 0) AS num_students,\n" +
                "    COALESCE(subquery.avg_score, 0) AS average_score\n" +
                "FROM (\n" +
                "    SELECT 1 AS quiz_range UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 \n" +
                "    UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10\n" +
                "    UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15\n" +
                "    UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 UNION ALL SELECT 20\n" +
                ") AS qr\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "        quiz_count,\n" +
                "        COUNT(DISTINCT user_id) AS num_students,\n" +
                "        AVG(avg_score) AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            ed.user_id,\n" +
                "            COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "            AVG(ed.score) AS avg_score\n" +
                "        FROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('5') -- Displays only the approved quizzes\n"
        );



        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();



        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
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
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        query.append("GROUP BY \n" +
                "\t\t\ted.user_id\n" +
                "    ) AS user_quiz_counts\n" +
                "    GROUP BY \n" +
                "        quiz_count\n" +
                ") AS subquery ON qr.quiz_range = subquery.quiz_count\n" +
                "ORDER BY \n" +
                "    qr.quiz_range;");

        query2.append("GROUP BY \n" +
                "\t\t\ted.user_id\n" +
                "    ) AS user_quiz_counts\n" +
                "    GROUP BY \n" +
                "        quiz_count\n" +
                ") AS subquery ON qr.quiz_range = subquery.quiz_count\n" +
                "ORDER BY \n" +
                "    qr.quiz_range;");


        queryNation.append("GROUP BY \n" +
                "\t\t\ted.user_id\n" +
                "    ) AS user_quiz_counts\n" +
                "    GROUP BY \n" +
                "        quiz_count\n" +
                ") AS subquery ON qr.quiz_range = subquery.quiz_count\n" +
                "ORDER BY \n" +
                "    qr.quiz_range;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println(query2);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(queryNation);



        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Micro scholarship Quizzes - Average Score Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> subjectWiseBreakdownAvgScoreStats(ROnePayloadDto payloadDto) {

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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') ");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') ");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }


        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("GROUP BY ed.subject\n" +
                "ORDER BY ed.subject\n" +
                "Limit 12;\n");

        query2.append("GROUP BY ed.subject\n" +
                "ORDER BY ed.subject\n" +
                "Limit 12;\n");

        queryNation.append("GROUP BY ed.subject\n" +
                "ORDER BY ed.subject\n" +
                "Limit 12;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Micro scholarship Quizzes - Average Score Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> gradeWiseAvgScoreStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') ");


        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') ");


        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n\n");
            query2.append("\t\tAND sm.grade = ?\n\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }


        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }

        query.append("GROUP BY sm.grade\n" +
                "ORDER BY sm.grade\n" +
                "Limit 12\n");

        query2.append("GROUP BY sm.grade\n" +
                "ORDER BY sm.grade\n" +
                "Limit 12\n");

        queryNation.append("GROUP BY sm.grade\n" +
                "ORDER BY sm.grade\n" +
                "Limit 12\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Grade Wise Average Score Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> topicWiseAvgScoreStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tsubquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score_region\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
                "    \tAND sm.grade = aqn.student_class\n" +
                "    \tAND ed.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tsubquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "\tAVG(subquery.score) AS avg_score_pan_india\n" +
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
                "\texam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
                "    AND sm.grade = aqn.student_class\n" +
                "    AND ed.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();

        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("\t\tAND lm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());

            queryNation.append("\t\tAND lm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("LIMIT 10000) AS subquery\n" +
                "GROUP BY\n" +
                "\tsubquery.quiz_name\n" +
                "ORDER BY\n" +
                "\tsubquery.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 10000) AS subquery\n" +
                "GROUP BY\n" +
                "\tsubquery.quiz_name\n" +
                "ORDER BY\n" +
                "\tsubquery.quiz_name\n" +
                "LIMIT 12;\n");

        queryNation.append("LIMIT 10000) AS subquery\n" +
                "GROUP BY\n" +
                "\tsubquery.quiz_name\n" +
                "ORDER BY\n" +
                "\tsubquery.quiz_name\n" +
                "LIMIT 12;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("--------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Average Score Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> totalQuizAttemptStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tquiz_range,\n" +
                "    COUNT(DISTINCT user_id) AS num_students,\n" +
                "    AVG(score) AS regional_avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_range,\n" +
                "        AVG(ed.score) AS score,\n" +
                "        sd.state_id\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tquiz_range,\n" +
                "    COUNT(DISTINCT user_id) AS num_students,\n" +
                "    AVG(score) AS national_avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_range,\n" +
                "        AVG(ed.score) AS score,\n" +
                "        sd.state_id\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

//        if (payloadDto.getQuizName() !=null){
//            query.append("\tAND aqn.quiz_name = ? \n");
//            parameters.add(payloadDto.getQuizName());
//
//            query2.append("\tAND aqn.quiz_name = ? \n");
//            parameters2.add(payloadDto.getQuizName());
//
//            queryNation.append("\tAND aqn.quiz_name = ? \n");
//            nationParameters.add(payloadDto.getQuizName());
//        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("GROUP BY\n" +
                "\t\ted.user_id\n" +
                ") sub\n" +
                "GROUP BY\n" +
                "\tquiz_range\n" +
                "ORDER BY\n" +
                "\tquiz_range\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\t\ted.user_id\n" +
                ") sub\n" +
                "GROUP BY\n" +
                "\tquiz_range\n" +
                "ORDER BY\n" +
                "\tquiz_range\n" +
                "LIMIT 12;\n");


        queryNation.append("GROUP BY\n" +
                "\t\ted.user_id\n" +
                ") sub\n" +
                "GROUP BY\n" +
                "\tquiz_range\n" +
                "ORDER BY\n" +
                "\tquiz_range\n" +
                "LIMIT 12;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Total Quiz Attempted Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> topicWiseStudAttemptsStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct case when ed.quiz_attempt = 1 THEN ed.user_id ELSE NULL END) as num_students_attempt_1,\n" +
                "    count(distinct case when ed.quiz_attempt = 2 THEN ed.user_id ELSE NULL END) as num_students_attempt_2,\n" +
                "    count(distinct case when ed.quiz_attempt = 3 THEN ed.user_id ELSE NULL END) as num_students_attempt_3,\n" +
                "    avg(case when ed.quiz_attempt = 1 THEN ed.score ELSE NULL END) AS avg_score_attempt_1_region,\n" +
                "    avg(case when ed.quiz_attempt = 2 THEN ed.score ELSE NULL END) AS avg_score_attempt_2_region,\n" +
                "    avg(case when ed.quiz_attempt = 3 THEN ed.score ELSE NULL END) AS avg_score_attempt_3_region\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
                "    AND sm.grade = aqn.student_class\n" +
                "    AND ed.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct case when ed.quiz_attempt = 1 THEN ed.user_id ELSE NULL END) as num_students_attempt_1,\n" +
                "    count(distinct case when ed.quiz_attempt = 2 THEN ed.user_id ELSE NULL END) as num_students_attempt_2,\n" +
                "    count(distinct case when ed.quiz_attempt = 3 THEN ed.user_id ELSE NULL END) as num_students_attempt_3,\n" +
                "    avg(case when ed.quiz_attempt = 1 THEN ed.score ELSE NULL END) as average_score_attempt_1_india,\n" +
                "    avg(case when ed.quiz_attempt = 2 THEN ed.score ELSE NULL END) as average_score_attempt_2_india,\n" +
                "    avg(case when ed.quiz_attempt = 3 THEN ed.score ELSE NULL END) as average_score_attempt_3_india\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt\n" +
                "    AND sm.grade = aqn.student_class\n" +
                "    AND ed.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();

        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("\t\tAND lm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());

            queryNation.append("\t\tAND lm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }

        query.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        queryNation.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Student Attempts Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> topicWiseMicroScholarQuizStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "\tAVG(subquery.avg_score) AS avg_score_state\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "\tAVG(subquery.avg_score) as avg_score_nation\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


//        if (payloadDto.getLanguageId() != null){
//            query.append("\t\tAND lm.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("\t\tAND lm.language_id = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//            queryNation.append("\t\tAND lm.language_id = ? \n");
//            nationParameters.add(payloadDto.getLanguageId());
//        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
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
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

//        if (payloadDto.getQuizName() !=null){
//            query.append("\tAND aqn.quiz_name = ? \n");
//            parameters.add(payloadDto.getQuizName());
//
//            query2.append("\tAND aqn.quiz_name = ? \n");
//            parameters2.add(payloadDto.getQuizName());
//
//            queryNation.append("\tAND aqn.quiz_name = ? \n");
//            nationParameters.add(payloadDto.getQuizName());
//        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        query2.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        queryNation.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");


        if (payloadDto.getLanguageId() != null){
            query.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());


            queryNation.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        query.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        queryNation.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Of Micro-Scholarship Quiz Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> topTopicPerformanceStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\t\n" +
                "    aqn.quiz_name as topic_name,\n" +
                "    count(distinct subquery.user_id) AS num_students_region,\n" +
                "    AVG(subquery.avg_score) AS avg_score_region\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "        AVG(ed.score) AS avg_score,\n" +
                "        sd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM exam_details ed\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\t\n" +
                "    aqn.quiz_name as topic_name,\n" +
                "    count(distinct subquery.user_id) AS num_students_nation,\n" +
                "    AVG(subquery.avg_score) AS avg_score_nation\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "        AVG(ed.score) AS avg_score,\n" +
                "        sd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM exam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


//        if (payloadDto.getLanguageId() != null){
//            query.append("\t\tAND lm.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("\t\tAND lm.language_id = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//            queryNation.append("\t\tAND lm.language_id = ? \n");
//            nationParameters.add(payloadDto.getLanguageId());
//        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        query2.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        queryNation.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");


        if (payloadDto.getLanguageId() != null){
            query.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());


            queryNation.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        query.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_region DESC\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_region DESC\n" +
                "LIMIT 12;\n");

        queryNation.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_nation DESC\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Top Performing Topics Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> weakTopicPerformanceStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct subquery.user_id) AS num_students_region,\n" +
                "    AVG(subquery.avg_score) AS avg_score_region\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "        AVG(ed.score) AS avg_score,\n" +
                "        sd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM exam_details ed\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct subquery.user_id) AS num_students_nation,\n" +
                "    AVG(subquery.avg_score) AS avg_score_nation\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "        AVG(ed.score) AS avg_score,\n" +
                "        sd.state_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM exam_details ed\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        //        if (payloadDto.getLanguageId() != null){
//            query.append("\t\tAND lm.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("\t\tAND lm.language_id = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//            queryNation.append("\t\tAND lm.language_id = ? \n");
//            nationParameters.add(payloadDto.getLanguageId());
//        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        query2.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");

        queryNation.append("\n" +
                "    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");


        if (payloadDto.getLanguageId() != null){
            query.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());


            queryNation.append("WHERE\n" +
                    "\tlm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }


        query.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_region ASC\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_region ASC\n" +
                "LIMIT 12;\n");

        queryNation.append("GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score_nation ASC\n" +
                "LIMIT 12\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Weak Performing Topics Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> coreRetakePracticeStudentStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tquiz_attempt,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students_region,\n" +
                "    AVG(ed.score) AS avg_score_region\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tquiz_attempt,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students_pan_india,\n" +
                "    AVG(ed.score) AS avg_score_pan_india\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("\t\tAND lm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());

            queryNation.append("\t\tAND lm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }




        query.append("\nGROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt\n" +
                "LIMIT 12;\n");

        query2.append("\nGROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt\n" +
                "LIMIT 12;\n");

        queryNation.append("\nGROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Core Retake Practice Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> coreRetakeImprovePercentStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\ted.quiz_attempt,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score,\n" +
                "    AVG(CASE\n" +
                "\t\tWHEN ed.quiz_attempt IN (2,3) THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
                "        ELSE NULL\n" +
                "\tEND) AS avg_improvement_region\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\texam_details ed_prev ON ed.user_id = ed_prev.user_id AND ed.quiz_attempt = ed_prev.quiz_attempt+1\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\ted.quiz_attempt,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students_nation,\n" +
                "    AVG(ed.score) AS avg_score_nation,\n" +
                "    AVG(CASE\n" +
                "\t\tWHEN ed.quiz_attempt IN (2,3) THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
                "       ELSE NULL\n" +
                "\tEND) AS avg_improvement_nation\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\texam_details ed_prev ON ed.user_id = ed_prev.user_id AND ed.quiz_attempt = ed_prev.quiz_attempt+1\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


//        if (payloadDto.getLanguageId() != null){
//            query.append("\t\tAND lm.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("\t\tAND lm.language_id = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//            queryNation.append("\t\tAND lm.language_id = ? \n");
//            nationParameters.add(payloadDto.getLanguageId());
//        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }



        query.append("GROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt;\n");

        query2.append("GROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt;\n");

        queryNation.append("GROUP BY quiz_attempt\n" +
                "ORDER BY quiz_attempt;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Core Retake Improvement Percentage Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> subjectWiseBreakdownImproveStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery.subject,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "     LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "     LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "     LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "    WHERE \n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    subquery.subject,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "     LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "    WHERE \n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();




//        if (payloadDto.getLanguageId() != null){
//            query.append("\t\tAND lm.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("\t\tAND lm.language_id = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//            queryNation.append("\t\tAND lm.language_id = ? \n");
//            nationParameters.add(payloadDto.getLanguageId());
//        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subject\n" +
                "ORDER BY \n" +
                "    Subject\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subject\n" +
                "ORDER BY \n" +
                "    Subject\n" +
                "LIMIT 12;\n");

        queryNation.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subject\n" +
                "ORDER BY \n" +
                "    Subject\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Subject Wise Breakdown Improvement Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> gradeWiseBreakdownImproveStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery.grade,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "    LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    subquery.grade,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("\t\tAND lm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());

            queryNation.append("\t\tAND lm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("AND sm.grade BETWEEN 1 AND 12 \n" +
                "LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.grade\n" +
                "ORDER BY \n" +
                "    subquery.grade\n" +
                "LIMIT 12;\n");

        query2.append("AND sm.grade BETWEEN 1 AND 12 \n" +
                "LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.grade\n" +
                "ORDER BY \n" +
                "    subquery.grade\n" +
                "LIMIT 12;\n");

        queryNation.append("AND sm.grade BETWEEN 1 AND 12 \n" +
                "LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.grade\n" +
                "ORDER BY \n" +
                "    subquery.grade\n" +
                "LIMIT 12;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Grade Wise Breakdown Improvement Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> topicWiseBreakdownImproveStats(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date,\n" +
                "        aqn.quiz_name\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND ed.subject = aqn.subject AND sm.grade = aqn.student_class\n" +
                "    JOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_nation,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_nation\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date,\n" +
                "        aqn.quiz_name\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND ed.subject = aqn.subject AND sm.grade = aqn.student_class\n" +
                "    JOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();
        List<Object> nationParameters = new ArrayList<>();


        if (payloadDto.getLanguageId() != null){
            query.append("\t\tAND lm.language_id = ? \n");
            parameters.add(payloadDto.getLanguageId());

            query2.append("\t\tAND lm.language_id = ? \n");
            parameters2.add(payloadDto.getLanguageId());

            queryNation.append("\t\tAND lm.language_id = ? \n");
            nationParameters.add(payloadDto.getLanguageId());
        }

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add(payloadDto.getTransactionDateFrom1());
            nationParameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

            queryNation.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            nationParameters.add("2022-01-11");
            nationParameters.add("2023-11-30");
        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
            parameters.add(payloadDto.getGrades());
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            query2.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());
            parameters2.add(payloadDto.getSubject());

            queryNation.append("\t\tAND ed.subject = ? \n");
            nationParameters.add(payloadDto.getSubject());
        }

        if (payloadDto.getSchoolLocation() !=null){
            query.append("\t\tAND sed.school_location = ? \n");
            parameters.add(payloadDto.getSchoolLocation());

            query2.append("\t\tAND sed.school_location = ? \n");
            parameters2.add(payloadDto.getSchoolLocation());

            queryNation.append("\t\tAND sed.school_location = ? \n");
            nationParameters.add(payloadDto.getSchoolLocation());
        }

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

            query2.append("\tAND sd.state_id = ? \n");
            parameters2.add(payloadDto.getStateId());

//            queryNation.append("\tAND sd.state_id = ? \n");
//            nationParameters.add(payloadDto.getStateId());
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());

//            queryNation.append("\tAND sd.district_id = ? \n");
//            nationParameters.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

            queryNation.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            nationParameters.add(payloadDto.getAgeFrom());
            nationParameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

            queryNation.append("\tAND aqn.quiz_name = ? \n");
            nationParameters.add(payloadDto.getQuizName());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

            queryNation.append("\tAND ped.child_mother_qualification = ? \n");
            nationParameters.add(payloadDto.getChildMotherQualification());
        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

            queryNation.append("\tAND ped.child_father_qualification = ? \n");
            nationParameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

            queryNation.append("\tAND ped.household_income = ? \n");
            nationParameters.add(payloadDto.getHouseholdId());
        }


        query.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "ORDER BY \n" +
                "    subquery.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "ORDER BY \n" +
                "    subquery.quiz_name\n" +
                "LIMIT 12;\n");

        queryNation.append("LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "ORDER BY \n" +
                "    subquery.quiz_name\n" +
                "LIMIT 12;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Improvement Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> topicWiseAvgScoreData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
                "    AVG(CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.score ELSE NULL END) AS avg_score_region,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    AVG(subquery.score) AS avg_score_nation\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        ed.score,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        ed.eklavvya_exam_id,\n" +
                "        ed.attempted,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        ed.quiz_attempt,\n" +
                "        sm.grade\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();



        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1() != null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2() != null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

        }


        query.append("LIMIT 10000) AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_region IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 10000) AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_region IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");



        System.out.println(parameters);
        System.out.println(query);


        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Average Score Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> topicWiseStudAttemptsData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct case when subquery.quiz_attempt = 1 AND subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.user_id ELSE NULL END) as num_students_attempt_1_state,\n" +
                "    count(distinct case when subquery.quiz_attempt = 2 AND subquery.state_id = 1 AND subquery.district_id = 1  THEN subquery.user_id ELSE NULL END) as num_students_attempt_2_state,\n" +
                "    count(distinct case when subquery.quiz_attempt = 3 AND subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.user_id ELSE NULL END) as num_students_attempt_3_state,\n" +
                "    avg(case when subquery.quiz_attempt = 1 AND subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_1_state,\n" +
                "    avg(case when subquery.quiz_attempt = 2 AND subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_2_state,\n" +
                "    avg(case when subquery.quiz_attempt = 3 AND subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_3_state,\n" +
                "    count(distinct case when subquery.quiz_attempt = 1 THEN subquery.user_id ELSE NULL END) as num_students_attempt_1_nation,\n" +
                "    count(distinct case when subquery.quiz_attempt = 2 THEN subquery.user_id ELSE NULL END) as num_students_attempt_2_nation,\n" +
                "    count(distinct case when subquery.quiz_attempt = 3 THEN subquery.user_id ELSE NULL END) as num_students_attempt_3_nation,\n" +
                "    avg(case when subquery.quiz_attempt = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_1_region_nation,\n" +
                "    avg(case when subquery.quiz_attempt = 2 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_2_region_nation,\n" +
                "    avg(case when subquery.quiz_attempt = 3 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_3_region_nation\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        sd.district_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5') \n");

        StringBuilder query2 = new StringBuilder(query);

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1() != null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
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
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

        }

        query.append("GROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_attempt_1_state IS NOT NULL\n" +
                "    AND avg_score_attempt_2_state IS NOT NULL\n" +
                "    AND avg_score_attempt_3_state IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_attempt_1_state IS NOT NULL\n" +
                "    AND avg_score_attempt_2_state IS NOT NULL\n" +
                "    AND avg_score_attempt_3_state IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");


        System.out.println(parameters);
        System.out.println(query);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Student Attempts Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> topicWiseMicroScholarQuizData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_region,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "\tAVG(CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_state,\n" +
                "    AVG(subquery.avg_score) AS avg_score_nation\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "\tsd.district_id,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5') \n");

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
            parameters.add("2022-01-11");
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
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
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


        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());


        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

        }


//        query.append("GROUP BY\n" +
//                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
//                "\t\tLIMIT 10000\n" +
//                ") AS subquery\n" +
//                "JOIN\n" +
//                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
//                "\tAND subquery.grade = aqn.student_class\n" +
//                "\tAND subquery.subject = aqn.subject\n" +
//                "JOIN\n" +
//                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");
//
//        query2.append("GROUP BY\n" +
//                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
//                "\t\tLIMIT 10000\n" +
//                ") AS subquery\n" +
//                "JOIN\n" +
//                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
//                "\tAND subquery.grade = aqn.student_class\n" +
//                "\tAND subquery.subject = aqn.subject\n" +
//                "JOIN\n" +
//                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n");



//        if (payloadDto.getLanguageId() != null){
//            query.append("WHERE\n" +
//                    "\taqn.language_id = ? \n");
//            parameters.add(payloadDto.getLanguageId());
//
//            query2.append("WHERE\n" +
//                    "\taqn.language_id  = ? \n");
//            parameters2.add(payloadDto.getLanguageId());
//
//        }

        query.append("GROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");

        query2.append("GROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY\n" +
                "\taqn.quiz_name\n" +
                "LIMIT 12;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");


        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Topic Wise Breakdown Of Micro-Scholarship Quiz Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> topTopicPerformanceData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    AVG(CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.score ELSE NULL END) AS avg_score_state,\n" +
                "    AVG(subquery.score) AS avg_score_nation\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);


        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

        }


        query.append("LIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tavg_score_state DESC, avg_score_nation DESC\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tavg_score_state DESC, avg_score_nation DESC\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Top Performing Topics Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> weakTopicPerformanceData(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT CASE WHEN subquery.state_id = 1 AND subquery.district_id THEN subquery.user_id ELSE NULL END) AS num_students_state,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_nation,\n" +
                "    AVG(CASE WHEN subquery.state_id = 1 AND subquery.district_id = 1 THEN subquery.score ELSE NULL END) AS avg_score_state,\n" +
                "    AVG(subquery.score) AS avg_score_nation\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        List<Object> parameters = new ArrayList<>();
        List<Object> parameters2 = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add(payloadDto.getTransactionDateFrom2());
            parameters2.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query2.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters2.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters2.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
            query2.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getQuizName() !=null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

            query2.append("\tAND aqn.quiz_name = ? \n");
            parameters2.add(payloadDto.getQuizName());

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

            query2.append("\tAND ped.child_mother_qualification = ? \n");
            parameters2.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

            query2.append("\tAND ped.child_father_qualification = ? \n");
            parameters2.add(payloadDto.getChildFatherQualification());


        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

            query2.append("\tAND ped.household_income = ? \n");
            parameters2.add(payloadDto.getHouseholdId());

        }


        query.append("LIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tavg_score_state ASC, avg_score_nation ASC\n" +
                "LIMIT 12;\n");

        query2.append("LIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_state IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tavg_score_state ASC, avg_score_nation ASC\n" +
                "LIMIT 12;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Weak Performing Topics Stats Fetched",response, HttpStatus.OK.value());


    }



//    ---------------------------------------TRIAL-------------------------------------------
    public ApiResponse2<?> microScholarshipQuizDataTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.quiz_range AS quiz_range_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.quiz_range AS quiz_range_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tuser_quiz_counts.state_id,\n" +
                "        user_quiz_counts.state_name,\n" +
                "        user_quiz_counts.district_id,\n" +
                "        user_quiz_counts.district_name,\n" +
                "        user_quiz_counts.quiz_count AS quiz_range,\n" +
                "        COUNT(DISTINCT user_quiz_counts.user_id) AS num_students,\n" +
                "        AVG(user_quiz_counts.avg_score) AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            ed.user_id,\n" +
                "            COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "            AVG(ed.score) AS avg_score,\n" +
                "            sd.state_id,\n" +
                "            sd.district_id,\n" +
                "            stm.state_name,\n" +
                "            sdm.district_name\n" +
                "        FROM \n" +
                "            exam_details ed\n" +
                "        JOIN \n" +
                "            student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "        JOIN\n" +
                "            student_master sm ON ed.user_id = sm.user_id\n" +
                "        JOIN\n" +
                "            student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "        JOIN\n" +
                "            student_demographic sd ON ed.user_id = sd.user_id\n" +
                "\t\tJOIN\n" +
                "\t\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\t\tJOIN\n" +
                "\t\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "        WHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "            AND sw.amount_status IN ('5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());
        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");
        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        query.append(" GROUP BY \n" +
                "            ed.user_id, sd.state_id, sd.district_id\n" +
                "    ) AS user_quiz_counts\n" +
                "    WHERE user_quiz_counts.quiz_count < 21\n" +
                "    GROUP BY \n" +
                "        user_quiz_counts.quiz_count, user_quiz_counts.state_id, user_quiz_counts.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "        user_quiz_counts.quiz_count AS quiz_range,\n" +
                "        user_quiz_counts.state_id,\n" +
                "        user_quiz_counts.district_id,\n" +
                "        COUNT(DISTINCT user_quiz_counts.user_id) AS num_students,\n" +
                "        AVG(user_quiz_counts.avg_score) AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            ed.user_id,\n" +
                "            COUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "            AVG(ed.score) AS avg_score,\n" +
                "            sd.state_id,\n" +
                "            sd.district_id,\n" +
                "            stm.state_name,\n" +
                "            sdm.district_name\n" +
                "        FROM \n" +
                "\t\t\texam_details ed\n" +
                "        JOIN \n" +
                "            student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "        JOIN\n" +
                "            student_master sm ON ed.user_id = sm.user_id\n" +
                "        JOIN\n" +
                "            student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "        JOIN\n" +
                "            student_demographic sd ON ed.user_id = sd.user_id\n" +
                "\t\tJOIN\n" +
                "\t\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\t\tJOIN\n" +
                "\t\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "        WHERE \n" +
                "\t\t\ted.attempted = 1\n" +
                "            AND sw.amount_status IN ('5') \n");

        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }


        query.append("GROUP BY \n" +
                "            ed.user_id, sd.state_id, sd.district_id\n" +
                "    ) AS user_quiz_counts\n" +
                "    WHERE user_quiz_counts.quiz_count < 21\n" +
                "    GROUP BY \n" +
                "        user_quiz_counts.quiz_count, user_quiz_counts.state_id, user_quiz_counts.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.quiz_range = subquery2.quiz_range\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.quiz_range;");



        System.out.println(parameters);
        System.out.println(query);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Micro scholarship Quizzes-Average Score Table Data Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> subjectWiseBreakdownAvgScoreTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.subject as \"subject\",\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.subject as \"subject\",\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.subject,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();


        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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


        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){
            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("GROUP BY sd.state_id, sd.district_id, ed.subject\n" +
                "\tORDER BY sd.state_id, sd.district_id, ed.subject\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.subject, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.subject,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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


        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

        }

        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }

        query.append("GROUP BY sd.state_id, sd.district_id, ed.subject\n" +
                "\tORDER BY sd.state_id, sd.district_id, ed.subject\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.subject, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.subject = subquery2.subject\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.subject;");


        System.out.println(parameters);
        System.out.println(query);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Subject Wise Breakdown - Average Score Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> gradeWiseAvgScoreTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.grade,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.grade,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "    FROM (\n" +
                "\tSELECT\n" +
                "\t\tsd.state_id,\n" +
                "\t\tstm.state_name,\n" +
                "\t\tsd.district_id,\n" +
                "\t\tsdm.district_name,\n" +
                "\t\tsm.grade,\n" +
                "\t\tAVG(ed.score) AS average_score,\n" +
                "\t\tCOUNT(DISTINCT ed.user_id) AS num_students\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\t\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){
            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("GROUP BY sd.state_id, sd.district_id, sm.grade\n" +
                "\tORDER BY sd.state_id, sd.district_id, sm.grade\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.grade, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.grade,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tsm.grade,\n" +
                "\tAVG(ed.score) AS average_score,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append(" GROUP BY sd.state_id, sd.district_id, sm.grade\n" +
                "\tORDER BY sd.state_id, sd.district_id, sm.grade\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.grade, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.grade = subquery2.grade\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.grade;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Grade Wise Breakdown Average Score Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> topicWiseAvgScoreTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "\tSELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        ed.score,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        ed.eklavvya_exam_id,\n" +
                "        ed.attempted,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        ed.quiz_attempt,\n" +
                "        sm.grade,\n" +
                "        stm.state_name,\n" +
                "        sdm.district_name\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append(" LIMIT 10000) AS subquery\n" +
                "\tJOIN\n" +
                "\t\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\t\tAND subquery.grade = aqn.student_class\n" +
                "\t\tAND subquery.subject = aqn.subject\n" +
                "\tJOIN\n" +
                "\t\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "\tWHERE\n" +
                "\t\taqn.language_id = 1\n" +
                "\tGROUP BY\n" +
                "\t\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "\tHAVING\n" +
                "\t\tavg_score IS NOT NULL\n" +
                "\tORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        ed.score,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        ed.eklavvya_exam_id,\n" +
                "        ed.attempted,\n" +
                "        sd.state_id,\n" +
                "        sd.district_id,\n" +
                "        ed.quiz_attempt,\n" +
                "        sm.grade,\n" +
                "        stm.state_name,\n" +
                "        sdm.district_name\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append(" LIMIT 10000) AS subquery\n" +
                "\tJOIN\n" +
                "\t\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\t\tAND subquery.grade = aqn.student_class\n" +
                "\t\tAND subquery.subject = aqn.subject\n" +
                "\tJOIN\n" +
                "\t\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "\tWHERE\n" +
                "\t\taqn.language_id = 1\n" +
                "\tGROUP BY\n" +
                "\t\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "\tHAVING\n" +
                "\t\tavg_score IS NOT NULL\n" +
                "\tORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Topic Wise Breakdown Average Score Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> totalQuizAttemptTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.quiz_range,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_range,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "\tSELECT\n" +
                "\tsub.state_id,\n" +
                "    sub.state_name,\n" +
                "    sub.district_id,\n" +
                "    sub.district_name,\n" +
                "    quiz_range,\n" +
                "    COUNT(DISTINCT user_id) AS num_students,\n" +
                "    AVG(score) AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_range,\n" +
                "        AVG(ed.score) AS score,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append(" GROUP BY\n" +
                "\t\ted.user_id\n" +
                ") sub\n" +
                "WHERE\n" +
                "\tquiz_range < 21\n" +
                "GROUP BY\n" +
                "\tsub.state_id, sub.district_id, quiz_range\n" +
                "ORDER BY\n" +
                "\tsub.state_id, sub.district_id, quiz_range\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_range, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_range,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsub.state_id,\n" +
                "    sub.state_name,\n" +
                "    sub.district_id,\n" +
                "    sub.district_name,\n" +
                "    quiz_range,\n" +
                "    COUNT(DISTINCT user_id) AS num_students,\n" +
                "    AVG(score) AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "        COUNT(ed.eklavvya_exam_id) AS quiz_range,\n" +
                "        AVG(ed.score) AS score,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name\n" +
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
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){
            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append(" GROUP BY\n" +
                "\t\ted.user_id\n" +
                ") sub\n" +
                "WHERE\n" +
                "\tquiz_range < 21\n" +
                "GROUP BY\n" +
                "\tsub.state_id, sub.district_id, quiz_range\n" +
                "ORDER BY\n" +
                "\tsub.state_id, sub.district_id, quiz_range\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_range, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.quiz_range = subquery2.quiz_range\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.quiz_range;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Total Quiz Attempted Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> topicWiseStudAttemptsTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students_attempt_1 AS num_students_attempt1_date1,\n" +
                "    subquery1.num_students_attempt_2 AS num_students_attempt2_date1,\n" +
                "    subquery1.num_students_attempt_3 AS num_students_attempt3_date1,\n" +
                "    subquery1.avg_score_attempt_1 AS average_score_attempt1_date1,\n" +
                "    subquery1.avg_score_attempt_2 AS average_score_attempt2_date1,\n" +
                "    subquery1.avg_score_attempt_3 AS average_score_attempt3_date1,\n" +
                "    subquery2.num_students_attempt_1 AS num_students_attempt1_date2,\n" +
                "    subquery2.num_students_attempt_2 AS num_students_attempt2_date2,\n" +
                "    subquery2.num_students_attempt_3 AS num_students_attempt3_date2,\n" +
                "    subquery2.avg_score_attempt_1 AS average_score_attempt1_date2,\n" +
                "    subquery2.avg_score_attempt_2 AS average_score_attempt2_date2,\n" +
                "    subquery2.avg_score_attempt_3 AS average_score_attempt3_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students_attempt_1 AS num_students_attempt_1,\n" +
                "        main_subquery.num_students_attempt_2 AS num_students_attempt_2,\n" +
                "        main_subquery.num_students_attempt_3 AS num_students_attempt_3,\n" +
                "        main_subquery.avg_score_attempt_1 AS avg_score_attempt_1,\n" +
                "        main_subquery.avg_score_attempt_2 AS avg_score_attempt_2,\n" +
                "        main_subquery.avg_score_attempt_3 AS avg_score_attempt_3\n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct case when subquery.quiz_attempt = 1 THEN subquery.user_id ELSE NULL END) as num_students_attempt_1,\n" +
                "    count(distinct case when subquery.quiz_attempt = 2 THEN subquery.user_id ELSE NULL END) as num_students_attempt_2,\n" +
                "    count(distinct case when subquery.quiz_attempt = 3 THEN subquery.user_id ELSE NULL END) as num_students_attempt_3,\n" +
                "    avg(case when subquery.quiz_attempt = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_1,\n" +
                "    avg(case when subquery.quiz_attempt = 2 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_2,\n" +
                "    avg(case when subquery.quiz_attempt = 3 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_3\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        ed.exam_name,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){
            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_attempt_1 IS NOT NULL\n" +
                "    AND avg_score_attempt_2 IS NOT NULL\n" +
                "    AND avg_score_attempt_3 IS NOT NULL\n" +
                "ORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students_attempt_1 AS num_students_attempt_1,\n" +
                "        main_subquery.num_students_attempt_2 AS num_students_attempt_2,\n" +
                "        main_subquery.num_students_attempt_3 AS num_students_attempt_3,\n" +
                "        main_subquery.avg_score_attempt_1 AS avg_score_attempt_1,\n" +
                "        main_subquery.avg_score_attempt_2 AS avg_score_attempt_2,\n" +
                "        main_subquery.avg_score_attempt_3 AS avg_score_attempt_3\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name as topic_name,\n" +
                "    count(distinct case when subquery.quiz_attempt = 1 THEN subquery.user_id ELSE NULL END) as num_students_attempt_1,\n" +
                "    count(distinct case when subquery.quiz_attempt = 2 THEN subquery.user_id ELSE NULL END) as num_students_attempt_2,\n" +
                "    count(distinct case when subquery.quiz_attempt = 3 THEN subquery.user_id ELSE NULL END) as num_students_attempt_3,\n" +
                "    avg(case when subquery.quiz_attempt = 1 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_1,\n" +
                "    avg(case when subquery.quiz_attempt = 2 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_2,\n" +
                "    avg(case when subquery.quiz_attempt = 3 THEN subquery.avg_score ELSE NULL END) AS avg_score_attempt_3\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        ed.exam_name,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score_attempt_1 IS NOT NULL\n" +
                "    AND avg_score_attempt_2 IS NOT NULL\n" +
                "    AND avg_score_attempt_3 IS NOT NULL\n" +
                "ORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY\n" +
                "\tsubquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Topic Wise Breakdown Student Attempts Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> topicWiseMicroScholarQuizTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.avg_score) AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        stm.state_name,\n" +
                "\t\tsd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsubquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "\taqn.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.avg_score) AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\t\ted.user_id,\n" +
                "\t\tCOUNT(ed.eklavvya_exam_id) AS quiz_count,\n" +
                "\t\tAVG(ed.score) AS avg_score,\n" +
                "\t\tsd.state_id,\n" +
                "        stm.state_name,\n" +
                "\t\tsd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        ed.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade\n" +
                "\tFROM \n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tJOIN\n" +
                "\t\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "\tJOIN \n" +
                "\t\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "\tWHERE\n" +
                "\t\t\ted.attempted = 1\n" +
                "\t\t\tAND sw.amount_status IN ('5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND sw.transaction_date BETWEEN ? AND ? \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    \tGROUP BY\n" +
                "\t\t\ted.user_id, ed.exam_name, ed.subject, sd.state_id, sm.grade\n" +
                "\t\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tauro_quiz_name aqn ON subquery.exam_name = aqn.quiz_attempt\n" +
                "\tAND subquery.grade = aqn.student_class\n" +
                "\tAND subquery.subject = aqn.subject\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON aqn.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY\n" +
                "\tsubquery.state_id, subquery.district_id, aqn.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Topic Wise Breakdown Micro scholarship Quiz Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> topTopicPerformanceTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tJOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tJOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5') \n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tLIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tsubquery.state_id, subquery.district_id, avg_score desc\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tJOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tJOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tLIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tsubquery.state_id, subquery.district_id, avg_score desc\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Top Performing Topics Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> weakTopicPerformanceTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tJOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tJOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tLIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tsubquery.state_id, subquery.district_id, avg_score asc\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "\tCOUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        ed.exam_name,\n" +
                "        aqn.quiz_name,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        aqn.language_id\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN \n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND sm.grade = aqn.student_class AND ed.subject = aqn.subject\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN \n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN \n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "\tJOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\tJOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "\tJOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }

        if (payloadDto.getQuizName() != null){
            query.append("\tAND aqn.quiz_name = ? \n");
            parameters.add(payloadDto.getQuizName());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tLIMIT 500000\n" +
                ") AS subquery\n" +
                "JOIN\n" +
                "\tlanguage_master lm ON subquery.language_id = lm.language_id\n" +
                "WHERE\n" +
                "\tsubquery.language_id = 1\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "HAVING\n" +
                "\tavg_score IS NOT NULL\n" +
                "ORDER BY \n" +
                "\tsubquery.state_id, subquery.district_id, avg_score asc\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Weak Performing Topics Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> coreRetakePracticeStudentTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.quiz_attempt,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_attempt,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tquiz_attempt,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tGROUP BY sd.state_id, sd.district_id, quiz_attempt\n" +
                "\tORDER BY sd.state_id, sd.district_id, quiz_attempt\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_attempt, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_attempt,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tquiz_attempt,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tGROUP BY sd.state_id, sd.district_id, quiz_attempt\n" +
                "\tORDER BY sd.state_id, sd.district_id, quiz_attempt\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_attempt, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.quiz_attempt = subquery2.quiz_attempt\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.quiz_attempt;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Core Retake/Practice Students Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> coreRetakeImprovePercentTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.quiz_attempt AS quiz_attempt,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS average_score_date1,\n" +
                "    subquery1.avg_improvement AS average_improvement_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS average_score_date2,\n" +
                "    subquery2.avg_improvement AS average_improvement_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_attempt,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score,\n" +
                "        main_subquery.avg_improvement AS avg_improvement\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\ted.quiz_attempt,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score,\n" +
                "    AVG(CASE\n" +
                "\t\tWHEN ed.quiz_attempt IN (2,3) THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
                "        ELSE NULL\n" +
                "\tEND) AS avg_improvement\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\texam_details ed_prev ON ed.user_id = ed_prev.user_id AND ed.quiz_attempt = ed_prev.quiz_attempt+1\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tGROUP BY sd.state_id, sd.district_id, ed.quiz_attempt\n" +
                "\tORDER BY sd.state_id, sd.district_id, ed.quiz_attempt\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_attempt, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.quiz_attempt,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score,\n" +
                "        main_subquery.avg_improvement AS avg_improvement\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\ted.quiz_attempt,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score,\n" +
                "    AVG(CASE\n" +
                "\t\tWHEN ed.quiz_attempt IN (2,3) THEN (ed.score - ed_prev.score) / ed_prev.score * 100\n" +
                "        ELSE NULL\n" +
                "\tEND) AS avg_improvement\n" +
                "FROM\n" +
                "\texam_details ed\n" +
                "JOIN\n" +
                "\texam_details ed_prev ON ed.user_id = ed_prev.user_id AND ed.quiz_attempt = ed_prev.quiz_attempt+1\n" +
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
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tGROUP BY sd.state_id, sd.district_id, ed.quiz_attempt\n" +
                "\tORDER BY sd.state_id, sd.district_id, ed.quiz_attempt\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.quiz_attempt, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.quiz_attempt = subquery2.quiz_attempt\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.quiz_attempt;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Core Retake/Practice Percentage Improvements Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> subjectWiseBreakdownImproveTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.subject,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.percent_improvement_second_state AS percent_improvement_second_state_date1,\n" +
                "    subquery1.percent_improvement_third_state AS percent_improvement_third_state_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.percent_improvement_second_state AS percent_improvement_second_state_date2,\n" +
                "    subquery2.percent_improvement_third_state AS percent_improvement_third_state_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.subject,\n" +
                "        main_subquery.num_students_state AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.subject,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "     LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "     LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "     LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "    WHERE \n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("\tLIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subject\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, Subject\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.subject, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.subject,\n" +
                "        main_subquery.num_students_state AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "    FROM (\n" +
                "       SELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.subject,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "     LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "     LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "     LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "\n" +
                "    WHERE \n" +
                "\t\ted.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subject\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, Subject\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.subject, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.subject = subquery2.subject\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.subject;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Subject Wise Breakdown Percentage Improvement Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> gradeWiseBreakdownImproveTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.grade,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.percent_improvement_second_state AS percent_improvement_second_state_date1,\n" +
                "    subquery1.percent_improvement_third_state AS percent_improvement_third_state_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.percent_improvement_second_state AS percent_improvement_second_state_date2,\n" +
                "    subquery2.percent_improvement_third_state AS percent_improvement_third_state_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.grade,\n" +
                "        main_subquery.num_students_state AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.grade,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "\t\tsd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "    LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n" +
                "\t\tAND sm.grade BETWEEN 1 AND 12 \n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.grade\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.grade\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.grade, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.grade,\n" +
                "        main_subquery.num_students_state AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.grade,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.subject,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sed.school_location,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "\t\tsd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "    LEFT JOIN\n" +
                "state_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n" +
                "\t\tAND sm.grade BETWEEN 1 AND 12 \n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    LIMIT 10000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.grade\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.grade\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.grade, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.grade = subquery2.grade\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.grade;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Grade Wise Breakdown Percentage Improvements Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> topicWiseBreakdownImproveTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.topic_name,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.percent_improvement_second_state AS percent_improvement_second_state_date1,\n" +
                "    subquery1.percent_improvement_third_state AS percent_improvement_third_state_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.percent_improvement_second_state AS percent_improvement_second_state_date2,\n" +
                "    subquery2.percent_improvement_third_state AS percent_improvement_third_state_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date,\n" +
                "        aqn.quiz_name\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND ed.subject = aqn.subject AND sm.grade = aqn.student_class\n" +
                "    JOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");

        List<Object> parameters = new ArrayList<>();

        if (payloadDto.getTransactionDateFrom1() != null
                && payloadDto.getTransactionDateTo1()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom1());
            parameters.add(payloadDto.getTransactionDateTo1());

        }

        if (payloadDto.getTransactionDateFrom1() == null
                && payloadDto.getTransactionDateTo1() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2022-01-11");
            parameters.add("2023-11-30");

        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    LIMIT 100000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.topic_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.percent_improvement_second_state AS percent_improvement_second_state,\n" +
                "        main_subquery.percent_improvement_third_state AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "    subquery.state_id,\n" +
                "    subquery.state_name,\n" +
                "    subquery.district_id,\n" +
                "    subquery.district_name,\n" +
                "    subquery.quiz_name AS topic_name,\n" +
                "    COUNT(DISTINCT subquery.user_id) AS num_students,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 1 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_second_state,\n" +
                "    (AVG(CASE WHEN subquery.quiz_attempt = 3 THEN subquery.score ELSE NULL END) - AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END)) / AVG(CASE WHEN subquery.quiz_attempt = 2 THEN subquery.score ELSE NULL END) * 100 AS percent_improvement_third_state\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        ed.user_id,\n" +
                "        ed.score,\n" +
                "        ed.quiz_attempt,\n" +
                "        sw.amount_status,\n" +
                "        sm.grade,\n" +
                "        sd.state_id,\n" +
                "        stm.state_name,\n" +
                "        sd.district_id,\n" +
                "        sdm.district_name,\n" +
                "        sd.gender,\n" +
                "        TIMESTAMPDIFF(YEAR, sd.dob, CURDATE()) AS age,\n" +
                "        sd.education_board,\n" +
                "        sd.school_management,\n" +
                "        sw.transaction_date,\n" +
                "        aqn.quiz_name\n" +
                "    FROM \n" +
                "        exam_details ed\n" +
                "    JOIN \n" +
                "        student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "    JOIN\n" +
                "        student_master sm ON ed.user_id = sm.user_id\n" +
                "    JOIN\n" +
                "        student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "    JOIN\n" +
                "        student_demographic sd ON ed.user_id = sd.user_id\n" +
                "    JOIN\n" +
                "        auro_quiz_name aqn ON ed.exam_name = aqn.quiz_attempt AND ed.subject = aqn.subject AND sm.grade = aqn.student_class\n" +
                "    JOIN\n" +
                "\t\tuser_master um ON sd.user_id = um.user_id\n" +
                "\tLEFT JOIN\n" +
                "\t\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "    LEFT JOIN\n" +
                "\t\tstate_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "    LEFT JOIN\n" +
                "\t\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "    WHERE \n" +
                "        ed.attempted = 1\n" +
                "\t\tAND sw.amount_status IN ('2','4','5')\n");


        if (payloadDto.getTransactionDateFrom2() != null
                && payloadDto.getTransactionDateTo2()!= null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add(payloadDto.getTransactionDateFrom2());
            parameters.add(payloadDto.getTransactionDateTo2());
        }

        if (payloadDto.getTransactionDateFrom2() == null
                && payloadDto.getTransactionDateTo2() == null){
            query.append("\t\tAND (STR_TO_DATE(SUBSTRING(ed.exam_compelete,1,8), '%Y%m%d') BETWEEN ? AND ? ) \n");
            parameters.add("2023-12-01");
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String current= currentDate.format(formatter);
            parameters.add(current);
        }



        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ?\n");
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());


        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());


        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());


        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());


        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

        }


        if (payloadDto.getChildMotherQualification() !=null){
            query.append("\tAND ped.child_mother_qualification = ? \n");
            parameters.add(payloadDto.getChildMotherQualification());

        }

        if (payloadDto.getChildFatherQualification() !=null){

            query.append("\tAND ped.child_father_qualification = ? \n");
            parameters.add(payloadDto.getChildFatherQualification());

        }

        if (payloadDto.getHouseholdId() != null ){
            query.append("\tAND ped.household_income = ? \n");
            parameters.add(payloadDto.getHouseholdId());

        }


        query.append("    LIMIT 100000\n" +
                ") AS subquery\n" +
                "GROUP BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "ORDER BY \n" +
                "    subquery.state_id, subquery.district_id, subquery.quiz_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.topic_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.topic_name = subquery2.topic_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.topic_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Topic Wise Breakdown Percentage Improvement Table Data Fetched",response, HttpStatus.OK.value());

    }

}
