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
public class ParentAndHouseholdServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public ApiResponse2<?> immigrantBackgroudStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.parent_immigrant_background = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_immigrant_background,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.parent_immigrant_background = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_immigrant_background,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n ");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.parent_immigrant_background;\n");

        query2.append("GROUP BY\n" +
                "         ped.parent_immigrant_background;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.parent_immigrant_background;\n");

        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {
            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Immigrant Background Stats Fetched",response, HttpStatus.OK.value());
        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Immigrant Background Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> parentStudiedTwelfthStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.anyone_12_passed_out = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_12_passed_out,\n" +
                "   COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.anyone_12_passed_out = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_12_passed_out,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.anyone_12_passed_out;\n");

        query2.append("GROUP BY\n" +
                "         ped.anyone_12_passed_out;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.anyone_12_passed_out;\n");

        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parent Who Studied 12th Stats Fetched",response, HttpStatus.OK.value());
        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parent Who Studied 12th Stats",null, HttpStatus.NO_CONTENT.value());

        }
    }


    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.parent_annual_expenditure_on_student = 1 THEN 'Less than INR 500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 500 - INR 1,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 1,001 - INR 2,500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 2,501 - INR 5,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 5,001 - INR 10,000'\n" +
                "        ELSE 'More than INR 10,001' \n" +
                "\tEND AS parent_annual_expenditure_on_student,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.parent_annual_expenditure_on_student = 1 THEN 'Less than INR 500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 500 - INR 1,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 1,001 - INR 2,500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 2,501 - INR 5,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 5,001 - INR 10,000'\n" +
                "        ELSE 'More than INR 10,001' \n" +
                "\tEND AS parent_annual_expenditure_on_student,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.parent_annual_expenditure_on_student\n" +
                "ORDER BY\n" +
                "         ped.parent_annual_expenditure_on_student;\n");

        query2.append("GROUP BY\n" +
                "         ped.parent_annual_expenditure_on_student\n" +
                "ORDER BY\n" +
                "         ped.parent_annual_expenditure_on_student;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.parent_annual_expenditure_on_student\n" +
                "ORDER BY\n" +
                "         ped.parent_annual_expenditure_on_student;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parents Annual Expenditure On Child Education Stats Fetched",response, HttpStatus.OK.value());


        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parents Annual Expenditure On Child Education Stats ",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> householdWithInternetConnectionStats(ROnePayloadDto payloadDto) {



        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.household_internet_connection=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS household_internet_connection,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.household_internet_connection=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS household_internet_connection,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.household_internet_connection;\n");

        query2.append("GROUP BY\n" +
                "         ped.household_internet_connection;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.household_internet_connection;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Household with Internet Connection Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){

            return  new ApiResponse2<>(true, "Facing Problem While Fetching - Household with Internet Connection Stats",response, HttpStatus.OK.value());

        }





    }

    public ApiResponse2<?> motherEducationQualificationStats(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_mother_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_mother_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_mother_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_mother_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_mother_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_mother_qualification,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_mother_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_mother_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_mother_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_mother_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_mother_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_mother_qualification,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.child_mother_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_mother_qualification;\n");

        query2.append("GROUP BY\n" +
                "         ped.child_mother_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_mother_qualification;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.child_mother_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_mother_qualification;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Mother's Education Qualification Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Mother's Education Qualification Stats",null, HttpStatus.NO_CONTENT.value());


        }
    }

    public ApiResponse2<?> anyoneInTheFamilyWhoKnowsToOperateComputerStats(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.anyone_operate_computer = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_operate_computer,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.anyone_operate_computer = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_operate_computer,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.anyone_operate_computer;\n");

        query2.append("GROUP BY\n" +
                "         ped.anyone_operate_computer;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.anyone_operate_computer;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Anyone in Family Know How to Operate Computer Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Anyone in Family Know How to Operate Computer Stats",null, HttpStatus.NO_CONTENT.value());


        }
    }


    public ApiResponse2<?> houseTypeStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.house_type = 1 THEN 'Pucca'\n" +
                "\t\tWHEN ped.house_type = 2 THEN 'Kuchha'\n" +
                "\t\tWHEN ped.house_type = 3 THEN 'Semi Pucca'\n" +
                "\t\tELSE 'Other'\n" +
                "\tEND AS house_type,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.house_type = 1 THEN 'Pucca'\n" +
                "\t\tWHEN ped.house_type = 2 THEN 'Kuchha'\n" +
                "\t\tWHEN ped.house_type = 3 THEN 'Semi Pucca'\n" +
                "\t\tELSE 'Other'\n" +
                "\tEND AS house_type,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.house_type\n" +
                "ORDER BY\n" +
                "         ped.house_type;\n");

        query2.append("GROUP BY\n" +
                "         ped.house_type\n" +
                "ORDER BY\n" +
                "         ped.house_type;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.house_type\n" +
                "ORDER BY\n" +
                "         ped.house_type;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "House Type Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - House Type Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> householdWhichHaveOtherReadingMaterialsStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.have_reading_materials = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS have_reading_materials,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.have_reading_materials = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS have_reading_materials,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.have_reading_materials;\n");

        query2.append("GROUP BY\n" +
                "         ped.have_reading_materials;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.have_reading_materials;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Households Which Have Other Reading Materials Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Households Which Have Other Reading Materials Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> fatherEducationQualificationStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_father_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_father_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_father_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_father_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_father_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_father_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_father_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_father_qualification,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_father_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_father_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_father_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_father_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_father_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_father_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_father_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_father_qualification,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.child_father_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_father_qualification;\n");

        query2.append("GROUP BY\n" +
                "         ped.child_father_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_father_qualification;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.child_father_qualification\n" +
                "ORDER BY\n" +
                "         ped.child_father_qualification;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Father's Education Qualification Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Father's Education Qualification Stats",null, HttpStatus.NO_CONTENT.value());


        }
    }

    public ApiResponse2<?> averageIncomeOfHouseholdStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.household_income = 1 THEN 'Over INR 30 Lakhs'\n" +
                "        WHEN ped.household_income = 2 THEN 'INR 5 Lakhs - 30 Lakhs'\n" +
                "        WHEN ped.household_income = 3 THEN 'INR 1.25 Lakhs - 5 Lakhs'\n" +
                "        WHEN ped.household_income = 4 THEN 'Less than INR 1.25 Lakhs'\n" +
                "\tEND AS household_income,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n" +
                "    AND ped.household_income IN ('1','2','3','4')\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.household_income = 1 THEN 'Over INR 30 Lakhs'\n" +
                "        WHEN ped.household_income = 2 THEN 'INR 5 Lakhs - 30 Lakhs'\n" +
                "        WHEN ped.household_income = 3 THEN 'INR 1.25 Lakhs - 5 Lakhs'\n" +
                "        WHEN ped.household_income = 4 THEN 'Less than INR 1.25 Lakhs'\n" +
                "\tEND AS household_income,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n" +
                "    AND ped.household_income IN ('1','2','3','4')\n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.household_income\n" +
                "ORDER BY\n" +
                "         ped.household_income;\n");

        query2.append("GROUP BY\n" +
                "         ped.household_income\n" +
                "ORDER BY\n" +
                "         ped.household_income;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.household_income\n" +
                "ORDER BY\n" +
                "         ped.household_income;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try {

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Average Income of Household Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){

            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Average Income of Household Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> householdWithElectricityConnectionStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.is_electricity_available=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS is_electricity_available,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.is_electricity_available=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS is_electricity_available,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.is_electricity_available;\n");

        query2.append("GROUP BY\n" +
                "         ped.is_electricity_available;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.is_electricity_available;\n");

        
        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Household With Electricity Connection Stats Fetched",response, HttpStatus.OK.value());
            
        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Household With Electricity Connection Stats ",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> adultAtHomeReadsWithChildEverydayStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.reads_with_child_every_day=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS reads_with_child_every_day,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.reads_with_child_every_day=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS reads_with_child_every_day,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.reads_with_child_every_day;\n");

        query2.append("GROUP BY\n" +
                "         ped.reads_with_child_every_day;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.reads_with_child_every_day;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Some Adult At Home Reads With Child Everyday Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Some Adult At Home Reads With Child Everyday Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> communicationWithTeacherOnceAMonthStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.communication_with_teacher_once_a_month=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS communication_with_teacher_once_a_month,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.communication_with_teacher_once_a_month=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS communication_with_teacher_once_a_month,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.communication_with_teacher_once_a_month;\n");

        query2.append("GROUP BY\n" +
                "         ped.communication_with_teacher_once_a_month;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.communication_with_teacher_once_a_month;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Communication With Teacher Once a Month Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Communication With Teacher Once a Month Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> parentExpectsChildWillGraduateFromHighSchoolOneDayStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_expectation_for_high_school=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_expectation_for_high_school,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_expectation_for_high_school=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_expectation_for_high_school,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.child_expectation_for_high_school;\n");

        query2.append("GROUP BY\n" +
                "         ped.child_expectation_for_high_school;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.child_expectation_for_high_school;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parents Expects Child Will Graduate From High School One Day Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parents Expects Child Will Graduate From High School One Day Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> parentExpectsChildWillGoToCollegeOneDayStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_expectation_for_college=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_expectation_for_college,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_expectation_for_college=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_expectation_for_college,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.child_expectation_for_college;\n");

        query2.append("GROUP BY\n" +
                "         ped.child_expectation_for_college;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.child_expectation_for_college;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parents Expects Child Will Go To College One Day Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parents Expects Child Will Go To College One Day Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> frequencyOfParentTeacherMeetingStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE \n" +
                "\t\tWHEN ped. parent_teacher_meeting_frequency=1 THEN 'Monthly'\n" +
                "\t\tWHEN ped. parent_teacher_meeting_frequency=2 THEN 'Once in two months'\n" +
                "\t\tWHEN ped. parent_teacher_meeting_frequency=3 THEN 'Quarterly'\n" +
                "\t\tWHEN ped. parent_teacher_meeting_frequency=4 THEN 'Yearly'    \n" +
                "        ELSE 'Other'\n" +
                "    END AS parent_teacher_meeting_frequency,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=1 THEN 'Monthly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=2 THEN 'Once in two months'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=3 THEN 'Quarterly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=4 THEN 'Yearly'    \n" +
                "        ELSE 'Other'\n" +
                "    END AS parent_teacher_meeting_frequency,\n" +
                "\tCOUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "\tCOUNT(DISTINCT um.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         parent_teacher_meeting_frequency;\n");

        query2.append("GROUP BY\n" +
                "         parent_teacher_meeting_frequency;\n");

        queryNation.append("GROUP BY\n" +
                "         parent_teacher_meeting_frequency;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Frequency of Parent Teacher Meeting Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Frequency of Parent Teacher Meeting Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> schoolProvidesGuidanceOnHowParentSupportChildrenStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.school_guidance_for_children_learning=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS school_guidance_for_children_learning,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.school_guidance_for_children_learning=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS school_guidance_for_children_learning,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.school_guidance_for_children_learning;\n");

        query2.append("GROUP BY\n" +
                "         ped.school_guidance_for_children_learning;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.school_guidance_for_children_learning;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "School Provides Guidance On How Parent Support Children Learning Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - School Provides Guidance On How Parent Support Children Learning Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> percentOfSchoolsParentsAwareOfLearningLevelsStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.aware_of_learning_levels=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS aware_of_learning_levels,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.aware_of_learning_levels=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS aware_of_learning_levels,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.aware_of_learning_levels;\n");

        query2.append("GROUP BY\n" +
                "         ped.aware_of_learning_levels;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.aware_of_learning_levels;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Percent Of Schools Parents Are Aware Of Learning Levels Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Percent Of Schools Parents Are Aware Of Learning Levels Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> schoolsInformParentsAboutSchoolActivityStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN school_informing_about_activity=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS school_informing_about_activity,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN school_informing_about_activity=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS school_informing_about_activity,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.school_informing_about_activity;\n");

        query2.append("GROUP BY\n" +
                "         ped.school_informing_about_activity;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.school_informing_about_activity;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Schools Inform Parents About School Activities Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Schools Inform Parents About School Activities Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }

    public ApiResponse2<?> attendParentTeacherConferencesStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.parent_teacher_confrences=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_teacher_confrences,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.parent_teacher_confrences=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_teacher_confrences,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.parent_teacher_confrences;\n");

        query2.append("GROUP BY\n" +
                "         ped.parent_teacher_confrences;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.parent_teacher_confrences;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parents Teacher Conferences Attended Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parents Teacher Conferences Attended Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


    public ApiResponse2<?> parentParticipateInEventAtChildSchoolStats(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_school_participation =1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_school_participation,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "       CASE \n" +
                "         WHEN ped.child_school_participation =1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS child_school_participation,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5') \n");

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
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?\n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?\n");
            nationParameters.add(payloadDto.getGrades());
        }

        if (payloadDto.getSubject() != null){
            query.append("\t\tAND ed.subject = ? \n");
            parameters.add(payloadDto.getSubject());

            query2.append("\t\tAND ed.subject = ? \n");
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
        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

            query2.append("\tAND sd.district_id = ? \n");
            parameters2.add(payloadDto.getDistrictId());
        }

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND sd.gender = ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());

            queryNation.append("\tAND sed.social_group = ? \n");
            nationParameters.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());

            queryNation.append("\tAND sed.cwsn = ? \n");
            nationParameters.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());

            queryNation.append("\tAND sd.education_board = ? \n");
            nationParameters.add(payloadDto.getEducationBoard());
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

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());

            queryNation.append("\tAND sd.school_management = ? \n");
            nationParameters.add(payloadDto.getSchoolManagement());
        }

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

            query2.append("\tAND sed.school_category = ? \n");
            parameters2.add(payloadDto.getSchoolCategory());

            queryNation.append("\tAND sed.school_category = ? \n");
            nationParameters.add(payloadDto.getSchoolCategory());
        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

            query2.append("\tAND sed.school_type = ? \n");
            parameters2.add(payloadDto.getSchoolType());

            queryNation.append("\tAND sed.school_type = ? \n");
            nationParameters.add(payloadDto.getSchoolType());
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
                "         ped.child_school_participation;\n");

        query2.append("GROUP BY\n" +
                "         ped.child_school_participation;\n");

        queryNation.append("GROUP BY\n" +
                "         ped.child_school_participation;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        try{

            response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
            response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

            response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

            return  new ApiResponse2<>(true, "Parents Participate In Event At Child School Stats Fetched",response, HttpStatus.OK.value());

        } catch (Exception e){
            return  new ApiResponse2<>(false, "Facing Problem While Fetching - Parents Participate In Event At Child School Stats",null, HttpStatus.NO_CONTENT.value());

        }

    }


//    --------------------------------------TABLE----------------------------------------

    public ApiResponse2<?> immigrantBackgroundTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.parent_immigrant_background,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_immigrant_background,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "       CASE \n" +
                "         WHEN ped.parent_immigrant_background = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_immigrant_background,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("GROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_immigrant_background \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_immigrant_background, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_immigrant_background,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "       CASE \n" +
                "         WHEN ped.parent_immigrant_background = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS parent_immigrant_background,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("GROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_immigrant_background \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_immigrant_background, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.parent_immigrant_background = subquery2.parent_immigrant_background \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.parent_immigrant_background;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Immigrant Background Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> parentStudiedTwelfthTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.anyone_12_passed_out,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.anyone_12_passed_out,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "         WHEN ped.anyone_12_passed_out = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_12_passed_out,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.anyone_12_passed_out \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.anyone_12_passed_out, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.anyone_12_passed_out,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "     CASE \n" +
                "         WHEN ped.anyone_12_passed_out = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_12_passed_out,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.anyone_12_passed_out \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.anyone_12_passed_out, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.anyone_12_passed_out = subquery2.anyone_12_passed_out \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.anyone_12_passed_out;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parents Studied 12th Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> parentAnnualExpenditureOnChildEducationTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.parent_annual_expenditure_on_student,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_annual_expenditure_on_student,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.parent_annual_expenditure_on_student = 1 THEN 'Less than INR 500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 500 - INR 1,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 1,001 - INR 2,500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 2,501 - INR 5,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 5,001 - INR 10,000'\n" +
                "        ELSE 'More than INR 10,001' \n" +
                "\tEND AS parent_annual_expenditure_on_student,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_annual_expenditure_on_student \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_annual_expenditure_on_student, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_annual_expenditure_on_student,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "     CASE\n" +
                "\t\tWHEN ped.parent_annual_expenditure_on_student = 1 THEN 'Less than INR 500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 500 - INR 1,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 1,001 - INR 2,500'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 2,501 - INR 5,000'\n" +
                "        WHEN ped.parent_annual_expenditure_on_student = 1 THEN 'INR 5,001 - INR 10,000'\n" +
                "        ELSE 'More than INR 10,001' \n" +
                "\tEND AS parent_annual_expenditure_on_student,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_annual_expenditure_on_student \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_annual_expenditure_on_student, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.parent_annual_expenditure_on_student = subquery2.parent_annual_expenditure_on_student \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.parent_annual_expenditure_on_student;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parent's Annual Expenditure on Children Education Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> householdWithInternetConnectionTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.household_internet_connection,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.household_internet_connection,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.household_internet_connection=1 THEN 'YES' \n" +
                "        ELSE 'NO'\n" +
                "\tEND AS household_internet_connection,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.household_internet_connection \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.household_internet_connection, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.household_internet_connection,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "     CASE \n" +
                "\t\tWHEN ped.household_internet_connection=1 THEN 'YES' \n" +
                "        ELSE 'NO'\n" +
                "\tEND AS household_internet_connection,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.household_internet_connection \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.household_internet_connection, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.household_internet_connection = subquery2.household_internet_connection \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.household_internet_connection;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Household With Internet Connection Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> motherEducationQualificationTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.child_mother_qualification,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_mother_qualification,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_mother_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_mother_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_mother_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_mother_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_mother_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_mother_qualification,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_mother_qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_mother_qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_mother_qualification,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ped.child_mother_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_mother_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_mother_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_mother_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_mother_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_mother_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_mother_qualification,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_mother_qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_mother_qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.child_mother_qualification = subquery2.child_mother_qualification \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.child_mother_qualification;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Mother's Education Qualification Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> anyoneInTheFamilyWhoKnowsToOperateComputerTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.anyone_operate_computer,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.anyone_operate_computer,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "         WHEN ped.anyone_operate_computer = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_operate_computer,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.anyone_operate_computer \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.anyone_operate_computer, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.anyone_operate_computer,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE \n" +
                "         WHEN ped.anyone_operate_computer = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS anyone_operate_computer,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("GROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.anyone_operate_computer \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.anyone_operate_computer, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.anyone_operate_computer = subquery2.anyone_operate_computer \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.anyone_operate_computer;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Family Member Know How To Operate Computer Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> houseTypeTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.house_type,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.house_type,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.house_type = 1 THEN 'Pucca'\n" +
                "\t\tWHEN ped.house_type = 2 THEN 'Kuchha'\n" +
                "\t\tWHEN ped.house_type = 3 THEN 'Semi Pucca'\n" +
                "\t\tELSE 'Other'\n" +
                "\tEND AS house_type,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.house_type \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.house_type, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.house_type,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ped.house_type = 1 THEN 'Pucca'\n" +
                "\t\tWHEN ped.house_type = 2 THEN 'Kuchha'\n" +
                "\t\tWHEN ped.house_type = 3 THEN 'Semi Pucca'\n" +
                "\t\tELSE 'Other'\n" +
                "\tEND AS house_type,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.house_type \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.house_type, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.house_type = subquery2.house_type \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.house_type;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "House Type Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> householdWhichHaveOtherReadingMaterialsTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.have_reading_materials,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.have_reading_materials,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "         WHEN ped.have_reading_materials = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS have_reading_materials,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.have_reading_materials \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.have_reading_materials, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.have_reading_materials,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE \n" +
                "         WHEN ped.have_reading_materials = 1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "       END AS have_reading_materials,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.have_reading_materials \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.have_reading_materials, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.have_reading_materials = subquery2.have_reading_materials \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.have_reading_materials;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Household With Other Reading Materials Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> fatherEducationQualificationTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.child_father_qualification,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_father_qualification,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.child_father_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_father_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_father_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_father_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_father_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_father_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_father_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_father_qualification,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_father_qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_father_qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_father_qualification,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ped.child_father_qualification = 1 THEN 'No schooling'\n" +
                "        WHEN ped.child_father_qualification = 2 THEN 'Class I - V'\n" +
                "        WHEN ped.child_father_qualification = 3 THEN 'Class VI - X'\n" +
                "        WHEN ped.child_father_qualification = 4 THEN 'Class XI - XII'\n" +
                "        WHEN ped.child_father_qualification = 5 THEN 'Graduate'\n" +
                "        WHEN ped.child_father_qualification = 6 THEN 'Post Graduate'\n" +
                "        WHEN ped.child_father_qualification = 7 THEN 'PhD'\n" +
                "        ELSE NULL\n" +
                "\tEND AS child_father_qualification,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_father_qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_father_qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.child_father_qualification = subquery2.child_father_qualification \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.child_father_qualification;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Father's Education Qualification Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> averageIncomeOfHouseholdTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.household_income,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.household_income,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN ped.household_income = 1 THEN 'Over INR 30 Lakhs'\n" +
                "        WHEN ped.household_income = 2 THEN 'INR 5 Lakhs - 30 Lakhs'\n" +
                "        WHEN ped.household_income = 3 THEN 'INR 1.25 Lakhs - 5 Lakhs'\n" +
                "        WHEN ped.household_income = 4 THEN 'Less than INR 1.25 Lakhs'\n" +
                "\tEND AS household_income,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.household_income \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.household_income, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.household_income,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ped.household_income = 1 THEN 'Over INR 30 Lakhs'\n" +
                "        WHEN ped.household_income = 2 THEN 'INR 5 Lakhs - 30 Lakhs'\n" +
                "        WHEN ped.household_income = 3 THEN 'INR 1.25 Lakhs - 5 Lakhs'\n" +
                "        WHEN ped.household_income = 4 THEN 'Less than INR 1.25 Lakhs'\n" +
                "\tEND AS household_income,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.household_income \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.household_income, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.household_income = subquery2.household_income \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "    AND household_income IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.household_income;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Average Income Of Household Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> householdWithElectricityConnectionTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.is_electricity_available,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_electricity_available,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.is_electricity_available=1 THEN 'YES' \n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_electricity_available,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.is_electricity_available \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_electricity_available, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_electricity_available,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.is_electricity_available=1 THEN 'YES' \n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_electricity_available,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.is_electricity_available \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_electricity_available, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.is_electricity_available = subquery2.is_electricity_available \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.is_electricity_available;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Household Has Electricity Connection Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> adultAtHomeReadsWithChildEverydayTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.reads_with_child_every_day,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.reads_with_child_every_day,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "         WHEN ped.reads_with_child_every_day=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "\tEND AS reads_with_child_every_day,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.reads_with_child_every_day \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.reads_with_child_every_day, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.reads_with_child_every_day,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "         WHEN ped.reads_with_child_every_day=1 THEN 'YES' \n" +
                "         ELSE 'NO'\n" +
                "\tEND AS reads_with_child_every_day,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.reads_with_child_every_day \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.reads_with_child_every_day, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.reads_with_child_every_day = subquery2.reads_with_child_every_day \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.reads_with_child_every_day;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Some Adult Reads With Child Everyday Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> communicationWithTeacherOnceAMonthTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.communication_with_teacher_once_a_month,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.communication_with_teacher_once_a_month,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.communication_with_teacher_once_a_month=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS communication_with_teacher_once_a_month,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.communication_with_teacher_once_a_month \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.communication_with_teacher_once_a_month, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.communication_with_teacher_once_a_month,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.communication_with_teacher_once_a_month=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS communication_with_teacher_once_a_month,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.communication_with_teacher_once_a_month \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.communication_with_teacher_once_a_month, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.communication_with_teacher_once_a_month = subquery2.communication_with_teacher_once_a_month \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.communication_with_teacher_once_a_month;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Communication With Teacher Atleast Once a Month Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> parentExpectsChildWillGraduateFromHighSchoolOneDayTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.child_expectation_for_high_school,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_expectation_for_high_school,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.child_expectation_for_high_school=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS child_expectation_for_high_school,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\nGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_expectation_for_high_school \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_expectation_for_high_school, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_expectation_for_high_school,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.child_expectation_for_high_school=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS child_expectation_for_high_school,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\nGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_expectation_for_high_school \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_expectation_for_high_school, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.child_expectation_for_high_school = subquery2.child_expectation_for_high_school \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.child_expectation_for_high_school;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parents Expects Their Children Graduate From High School One Day Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> parentExpectsChildWillGoToCollegeOneDayTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.child_expectation_for_college,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_expectation_for_college,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.child_expectation_for_college=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS child_expectation_for_college,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_expectation_for_college \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_expectation_for_college, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_expectation_for_college,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.child_expectation_for_college=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "\tEND AS child_expectation_for_college,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_expectation_for_college \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_expectation_for_college, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.child_expectation_for_college = subquery2.child_expectation_for_college \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.child_expectation_for_college;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parents Expectations That Their Children Will Go To College One Day Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> frequencyOfParentTeacherMeetingTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.parent_teacher_meeting_frequency,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_meeting_frequency,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=1 THEN 'Monthly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=2 THEN 'Once in two months'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=3 THEN 'Quarterly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=4 THEN 'Yearly'    \n" +
                "        ELSE 'Other'\n" +
                "    END AS parent_teacher_meeting_frequency,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_teacher_meeting_frequency \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_meeting_frequency, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_meeting_frequency,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=1 THEN 'Monthly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=2 THEN 'Once in two months'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=3 THEN 'Quarterly'\n" +
                "\t\tWHEN ped.parent_teacher_meeting_frequency=4 THEN 'Yearly'    \n" +
                "        ELSE 'Other'\n" +
                "    END AS parent_teacher_meeting_frequency,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_teacher_meeting_frequency \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_meeting_frequency, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.parent_teacher_meeting_frequency = subquery2.parent_teacher_meeting_frequency \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.parent_teacher_meeting_frequency;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Frequency Of Parent Teacher Meetings Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> schoolProvidesGuidanceOnHowParentSupportChildrenTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.school_guidance_for_children_learning,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.school_guidance_for_children_learning,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.school_guidance_for_children_learning=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS school_guidance_for_children_learning,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.school_guidance_for_children_learning \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.school_guidance_for_children_learning, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.school_guidance_for_children_learning,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.school_guidance_for_children_learning=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS school_guidance_for_children_learning,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.school_guidance_for_children_learning \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.school_guidance_for_children_learning, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.school_guidance_for_children_learning = subquery2.school_guidance_for_children_learning \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.school_guidance_for_children_learning;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Schools Provide Guidance How Parents Can Support Children Learning Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> percentOfSchoolsParentsAwareOfLearningLevelsTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.aware_of_learning_levels,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.aware_of_learning_levels,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.aware_of_learning_levels=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS aware_of_learning_levels,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.aware_of_learning_levels \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.aware_of_learning_levels, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.aware_of_learning_levels,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.aware_of_learning_levels=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS aware_of_learning_levels,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.aware_of_learning_levels \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.aware_of_learning_levels, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.aware_of_learning_levels = subquery2.aware_of_learning_levels \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.aware_of_learning_levels;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Percent Of Schools Parents Are Aware Of Learning Levels Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolsInformParentsAboutSchoolActivityTable(ROnePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.school_informing_about_activity,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.school_informing_about_activity,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.school_informing_about_activity=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS school_informing_about_activity,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.school_informing_about_activity \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.school_informing_about_activity, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.school_informing_about_activity,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.school_informing_about_activity=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS school_informing_about_activity,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("GROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.school_informing_about_activity \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.school_informing_about_activity, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.school_informing_about_activity = subquery2.school_informing_about_activity \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.school_informing_about_activity;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "School Informed Parents About School Activities Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> attendParentTeacherConferencesTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.parent_teacher_confrences,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_confrences,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.parent_teacher_confrences=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS parent_teacher_confrences,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_teacher_confrences \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_confrences, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_confrences,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.parent_teacher_confrences=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS parent_teacher_confrences,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.parent_teacher_confrences \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_confrences, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.parent_teacher_confrences = subquery2.parent_teacher_confrences \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.parent_teacher_confrences;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Attend Parent Teacher Conferences Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> parentParticipateInEventAtChildSchoolTable(ROnePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.child_school_participation,\n" +
                "    subquery1.num_parents AS num_parents_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_parents AS num_parents_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_school_participation,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.child_school_participation=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS child_school_participation,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "    COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_school_participation \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_school_participation, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.child_school_participation,\n" +
                "        main_subquery.num_parents AS num_parents,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.average_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE \n" +
                "        WHEN ped.child_school_participation=1 THEN 'YES' \n" +
                "\t\tELSE 'NO'\n" +
                "    END AS child_school_participation,\n" +
                "    COUNT(DISTINCT um.parent_id) AS num_parents,\n" +
                "   COUNT(DISTINCT um.user_id) AS num_students,\n" +
                "   AVG(ed.score) AS average_score\n" +
                "FROM\n" +
                "   user_master um\n" +
                "JOIN\n" +
                "    parent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "LEFT JOIN\n" +
                "     exam_details ed ON um.user_id = ed.user_id\n" +
                "LEFT JOIN\n" +
                "     student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "LEFT JOIN\n" +
                "      student_master sm ON ed.user_id = sm.user_id\n" +
                "LEFT JOIN\n" +
                "      student_extra_data sed ON ed.user_id = sed.user_id\n" +
                "LEFT JOIN\n" +
                "      student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "        state_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "        state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                " \n" +
                "WHERE\n" +
                "ed.attempted = 1\n" +
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

        if (payloadDto.getSchoolCategory() !=null){
            query.append("\tAND sed.school_category = ? \n");
            parameters.add(payloadDto.getSchoolCategory());

        }

        if (payloadDto.getSchoolType() !=null){
            query.append("\tAND sed.school_type = ? \n");
            parameters.add(payloadDto.getSchoolType());

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


        query.append("\tGROUP BY\n" +
                "\tsd.state_id, sd.district_id, ped.child_school_participation \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.child_school_participation, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.child_school_participation = subquery2.child_school_participation \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.child_school_participation;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parents Participate In Event At Child School Table Data Fetched",response, HttpStatus.OK.value());

    }
}
