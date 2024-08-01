package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.RThreePayloadDto;
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
public class MasterServicesR3 {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> studentLearningStyleStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "\tCASE\n" +
                "\t\tWHEN sed.learning_preference = 1 THEN 'Visual Learners'\n" +
                "\t\tWHEN sed.learning_preference = 2 THEN 'Auditory' \n" +
                "\t\tWHEN sed.learning_preference = 3 THEN 'Kinaesthetic' \n" +
                "\t\tWHEN sed.learning_preference = 4 THEN 'Reading/Writing' \n" +
                "\t\tWHEN sed.learning_preference = 5 THEN 'Any Other'\n" +
                "\t\tELSE NULL\n" +
                "    END AS learning_preference, \n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students, \n" +
                "    AVG(ed.score) as avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "\tCASE\n" +
                "\t\tWHEN sed.learning_preference = 1 THEN 'Visual Learners'\n" +
                "\t\tWHEN sed.learning_preference = 2 THEN 'Auditory' \n" +
                "\t\tWHEN sed.learning_preference = 3 THEN 'Kinaesthetic' \n" +
                "\t\tWHEN sed.learning_preference = 4 THEN 'Reading/Writing' \n" +
                "\t\tWHEN sed.learning_preference = 5 THEN 'Any Other'\n" +
                "\t\tELSE NULL\n" +
                "    END AS learning_preference, \n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students, \n" +
                "    AVG(ed.score) as avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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


        query.append("GROUP BY \n" +
                "\tsed.learning_preference \n" +
                "ORDER BY \n" +
                "\tsed.learning_preference;\n");

        query2.append("GROUP BY \n" +
                "\tsed.learning_preference \n" +
                "ORDER BY \n" +
                "\tsed.learning_preference;\n");

        queryNation.append("GROUP BY \n" +
                "\tsed.learning_preference \n" +
                "ORDER BY \n" +
                "\tsed.learning_preference;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Learning Style Preference Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> studentCollaborativeLearningStats(RThreePayloadDto payloadDto) {
        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.collaborative_learning_style= 1 THEN 'Social'\n" +
                "        WHEN sed.collaborative_learning_style= 2 THEN 'Solitary'\n" +
                "        WHEN sed.collaborative_learning_style= 3 THEN 'Small Group'\n" +
                "        WHEN sed.collaborative_learning_style= 4 THEN 'Other'\n" +
                "\tEND AS collaborative_learning_style,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.collaborative_learning_style= 1 THEN 'Social'\n" +
                "        WHEN sed.collaborative_learning_style= 2 THEN 'Solitary'\n" +
                "        WHEN sed.collaborative_learning_style= 3 THEN 'Small Group'\n" +
                "        WHEN sed.collaborative_learning_style= 4 THEN 'Other'\n" +
                "\tEND AS collaborative_learning_style,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.collaborative_learning_style\n" +
                "ORDER BY\n" +
                "\tsed.collaborative_learning_style;\n");

        query2.append("GROUP BY\n" +
                "\tsed.collaborative_learning_style\n" +
                "ORDER BY\n" +
                "\tsed.collaborative_learning_style;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.collaborative_learning_style\n" +
                "ORDER BY\n" +
                "\tsed.collaborative_learning_style;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student preference On Collaborative Learning Style Stats Fetched",response, HttpStatus.OK.value());



    }

    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tsed.extra_learning_materials AS extra_learning_materials,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tsed.extra_learning_materials AS extra_learning_materials,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.extra_learning_materials\n" +
                "ORDER BY\n" +
                "\tsed.extra_learning_materials;\n");

        query2.append("GROUP BY\n" +
                "\tsed.extra_learning_materials\n" +
                "ORDER BY\n" +
                "\tsed.extra_learning_materials;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.extra_learning_materials\n" +
                "ORDER BY\n" +
                "\tsed.extra_learning_materials;\n");

        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Read Other Materials In Addition To Textbook Stats Fetched",response, HttpStatus.OK.value());


    }


    public ApiResponse2<?> hoursOfIndividualStudyPerDayStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.daily_study_hours = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.daily_study_hours = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.daily_study_hours = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.daily_study_hours = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.daily_study_hours = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS daily_study_hours,\n" +
                "   COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.daily_study_hours = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.daily_study_hours = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.daily_study_hours = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.daily_study_hours = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.daily_study_hours = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS daily_study_hours,\n" +
                "   COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.daily_study_hours\n" +
                "ORDER BY\n" +
                "\tsed.daily_study_hours;\n");

        query2.append("GROUP BY\n" +
                "\tsed.daily_study_hours\n" +
                "ORDER BY\n" +
                "\tsed.daily_study_hours;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.daily_study_hours\n" +
                "ORDER BY\n" +
                "\tsed.daily_study_hours;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Hours of Individual Study/Practice Per Day Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> paidPrivateTuitionHourStats(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.private_tuition_hours = 1 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.private_tuition_hours = 2 THEN '1-3 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 3 THEN '3-5 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 4 THEN 'More than 5 hrs'\n" +
                "\tEND AS private_tuition_hours,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.private_tuition_hours = 1 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.private_tuition_hours = 2 THEN '1-3 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 3 THEN '3-5 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 4 THEN 'More than 5 hrs'\n" +
                "\tEND AS private_tuition_hours,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.private_tuition_hours\n" +
                "ORDER BY\n" +
                "\tsed.private_tuition_hours;\n");

        query2.append("GROUP BY\n" +
                "\tsed.private_tuition_hours\n" +
                "ORDER BY\n" +
                "\tsed.private_tuition_hours;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.private_tuition_hours\n" +
                "ORDER BY\n" +
                "\tsed.private_tuition_hours;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Paid Private Tuition Hours Stats Fetched",response, HttpStatus.OK.value());


    }


    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.time_spent_on_mobile_study = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 2 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS time_spent_on_mobile_study,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.time_spent_on_mobile_study = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 2 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS time_spent_on_mobile_study,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.time_spent_on_mobile_study\n" +
                "ORDER BY\n" +
                "\tsed.time_spent_on_mobile_study;\n");

        query2.append("GROUP BY\n" +
                "\tsed.time_spent_on_mobile_study\n" +
                "ORDER BY\n" +
                "\tsed.time_spent_on_mobile_study;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.time_spent_on_mobile_study\n" +
                "ORDER BY\n" +
                "\tsed.time_spent_on_mobile_study;\n");

        
        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Hours Spent On Mobile Phones Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> studentAccessToDigitalDevicesAtHomeStats(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tsed.access_to_mobile_at_home AS access_to_mobile_at_home,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tsed.access_to_mobile_at_home AS access_to_mobile_at_home,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.access_to_mobile_at_home\n" +
                "ORDER BY\n" +
                "\tsed.access_to_mobile_at_home;\n");

        query2.append("GROUP BY\n" +
                "\tsed.access_to_mobile_at_home\n" +
                "ORDER BY\n" +
                "\tsed.access_to_mobile_at_home;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.access_to_mobile_at_home\n" +
                "ORDER BY\n" +
                "\tsed.access_to_mobile_at_home;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Having Access to Digital Devices at Home Stats Fetched",response, HttpStatus.OK.value());



    }

    public ApiResponse2<?> studentsUsingLearningAppHomeStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "sed.learning_apps_at_home AS learning_apps_at_home,\n" +
                "COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "sed.learning_apps_at_home AS learning_apps_at_home,\n" +
                "COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.learning_apps_at_home\n" +
                "ORDER BY\n" +
                "\tsed.learning_apps_at_home;\n");

        query2.append("GROUP BY\n" +
                "\tsed.learning_apps_at_home\n" +
                "ORDER BY\n" +
                "\tsed.learning_apps_at_home;\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.learning_apps_at_home\n" +
                "ORDER BY\n" +
                "\tsed.learning_apps_at_home;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Using Learning Apps At Home Stats Fetched",response, HttpStatus.OK.value());



    }

    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccounts(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    CASE\n" +
                "\t\tWHEN sed.social_account = 1 THEN 'Yes'\n" +
                "        ELSE 'No'\n" +
                "\tEND as social_account,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    CASE\n" +
                "\t\tWHEN sed.social_account = 1 THEN 'Yes'\n" +
                "        ELSE 'No'\n" +
                "\tEND as social_account,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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




        query.append("GROUP BY \n" +
                "\tsocial_account\n" +
                "ORDER BY \n" +
                "    social_account;");

        query2.append("GROUP BY \n" +
                "\tsocial_account\n" +
                "ORDER BY \n" +
                "    social_account;");

        queryNation.append("GROUP BY \n" +
                "\tsocial_account\n" +
                "ORDER BY \n" +
                "    social_account;");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Who Have One or More Social Media Account Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> typeOfSiteStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    CASE\n" +
                "\tWHEN swd.website_id = 1 THEN 'Edtech'\n" +
                "        WHEN swd.website_id = 2 THEN 'Entertainment (movies, games)'\n" +
                "        WHEN swd.website_id = 3 THEN 'Ecommerce &Lifestyle (flipchart, amazon, blinkit etc)'\n" +
                "        WHEN swd.website_id = 4 THEN 'Food and Beverages (Zomato, Swiggy)'\n" +
                "        WHEN swd.website_id = 5 THEN 'Travel'\n" +
                "        ELSE NULL\n" +
                "\tEND as website_id,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_websites swd\n" +
                "JOIN\n" +
                "\texam_details ed ON swd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT \n" +
                "    CASE\n" +
                "\t\tWHEN swd.website_id = 1 THEN 'Edtech'\n" +
                "        WHEN swd.website_id = 2 THEN 'Entertainment (movies, games)'\n" +
                "        WHEN swd.website_id = 3 THEN 'Ecommerce &Lifestyle (flipchart, amazon, blinkit etc)'\n" +
                "        WHEN swd.website_id = 4 THEN 'Food and Beverages (Zomato, Swiggy)'\n" +
                "        WHEN swd.website_id = 5 THEN 'Travel'\n" +
                "        ELSE NULL\n" +
                "\tEND as website_id,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_websites swd\n" +
                "JOIN\n" +
                "\texam_details ed ON swd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\tAND sw.amount_status IN ('2','4','5')");

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






        query.append("GROUP BY \n" +
                "    website_id\n" +
                "ORDER BY \n" +
                "    website_id;");

        query2.append("GROUP BY \n" +
                "    website_id\n" +
                "ORDER BY \n" +
                "    website_id;");

        queryNation.append("GROUP BY \n" +
                "    website_id\n" +
                "ORDER BY \n" +
                "    website_id;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Type of Sites Stats Fetched",response, HttpStatus.OK.value());




    }

    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentStats(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.hours_spent_on_mobile = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.hours_spent_on_mobile = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.hours_spent_on_mobile = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS hours_spent_on_mobile,\n" +
                "   COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "    AND sed.hours_spent_on_mobile NOT IN ('0')\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.hours_spent_on_mobile = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.hours_spent_on_mobile = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.hours_spent_on_mobile = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS hours_spent_on_mobile,\n" +
                "   COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "\tAVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "    AND sed.hours_spent_on_mobile NOT IN ('0')\n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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
                "\tsed.hours_spent_on_mobile\n" +
                "ORDER BY\n" +
                "\tsed.hours_spent_on_mobile;");

        query2.append("GROUP BY\n" +
                "\tsed.hours_spent_on_mobile\n" +
                "ORDER BY\n" +
                "\tsed.hours_spent_on_mobile;");

        queryNation.append("GROUP BY\n" +
                "\tsed.hours_spent_on_mobile\n" +
                "ORDER BY\n" +
                "\tsed.hours_spent_on_mobile;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Hours Spent On Mobile For Entertainment Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> paidPrivateTuitionSubjectsStats(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT  \n" +
                "\tasb.subject AS tution_subject_name,  \n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,  \n" +
                "    AVG(ed.score) AS avg_score \n" +
                "FROM  \n" +
                "\tstudent_tuition_data stds \n" +
                "JOIN  \n" +
                "\tauro_subject asb ON stds.subject_id = asb.id \n" +
                "JOIN  \n" +
                "\texam_details ed ON stds.user_id = ed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id \n" +
                "JOIN  \n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id \n" +
                "JOIN \n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id \n" +
                "LEFT JOIN \n" +
                "\tstate_master stm ON sd.state_id = stm.state_id \n" +
                "LEFT JOIN\n" +
                "\tstate_district_master sdm ON sd.district_id = sdm.district_id \n" +
                "JOIN \n" +
                "\tuser_master um ON sd.user_id = um.user_id \n" +
                "LEFT JOIN \n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id \n" +
                "WHERE  \n" +
                "\ted.attempted = 1  \n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT  \n" +
                "\tasb.subject AS tution_subject_name,  \n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,  \n" +
                "    AVG(ed.score) AS avg_score \n" +
                "FROM  \n" +
                "\tstudent_tuition_data stds \n" +
                "JOIN  \n" +
                "\tauro_subject asb ON stds.subject_id = asb.id \n" +
                "JOIN  \n" +
                "\texam_details ed ON stds.user_id = ed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id \n" +
                "JOIN  \n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id \n" +
                "JOIN \n" +
                "\tstudent_demographic sd ON ed.user_id = sd.user_id \n" +
                "JOIN \n" +
                "\tuser_master um ON sd.user_id = um.user_id \n" +
                "LEFT JOIN \n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id \n" +
                "WHERE  \n" +
                "\ted.attempted = 1  \n" +
                "\tAND sw.amount_status IN ('2','4','5')\n");

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




        query.append("GROUP BY  \n" +
                "\tasb.subject \n" +
                "ORDER BY  \n" +
                "\tasb.subject;");

        query2.append("GROUP BY  \n" +
                "\tasb.subject \n" +
                "ORDER BY  \n" +
                "\tasb.subject;");

        queryNation.append("GROUP BY  \n" +
                "\tasb.subject \n" +
                "ORDER BY  \n" +
                "\tasb.subject;");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Paid Private Tuition Subjects Stats Fetched",response, HttpStatus.OK.value());


    }

// ---------------------------------------TABLES---------------------------------------------

    public ApiResponse2<?> studentLearningStyleTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.learning_preference,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.learning_preference,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.learning_preference = 1 THEN 'Visual Learners'\n" +
                "\t\tWHEN sed.learning_preference = 2 THEN 'Auditory' \n" +
                "\t\tWHEN sed.learning_preference = 3 THEN 'Kinaesthetic' \n" +
                "\t\tWHEN sed.learning_preference = 4 THEN 'Reading/Writing' \n" +
                "\t\tWHEN sed.learning_preference = 5 THEN 'Any Other'\n" +
                "\t\tELSE NULL\n" +
                "    END AS learning_preference, \n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.learning_preference\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, learning_preference\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.learning_preference, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.learning_preference,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.learning_preference = 1 THEN 'Visual Learners'\n" +
                "\t\tWHEN sed.learning_preference = 2 THEN 'Auditory' \n" +
                "\t\tWHEN sed.learning_preference = 3 THEN 'Kinaesthetic' \n" +
                "\t\tWHEN sed.learning_preference = 4 THEN 'Reading/Writing' \n" +
                "\t\tWHEN sed.learning_preference = 5 THEN 'Any Other'\n" +
                "\t\tELSE NULL\n" +
                "    END AS learning_preference, \n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.learning_preference\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.learning_preference\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.learning_preference, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.learning_preference = subquery2.learning_preference\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.learning_preference;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Student Learning Style Preference Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> studentCollaborativeLearningTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.collaborative_learning_style,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.collaborative_learning_style,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.collaborative_learning_style= 1 THEN 'Social'\n" +
                "        WHEN sed.collaborative_learning_style= 2 THEN 'Solitary'\n" +
                "        WHEN sed.collaborative_learning_style= 3 THEN 'Small Group'\n" +
                "        WHEN sed.collaborative_learning_style= 4 THEN 'Other'\n" +
                "\tEND AS collaborative_learning_style, \n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.collaborative_learning_style\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, collaborative_learning_style\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.collaborative_learning_style, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.collaborative_learning_style,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.collaborative_learning_style= 1 THEN 'Social'\n" +
                "        WHEN sed.collaborative_learning_style= 2 THEN 'Solitary'\n" +
                "        WHEN sed.collaborative_learning_style= 3 THEN 'Small Group'\n" +
                "        WHEN sed.collaborative_learning_style= 4 THEN 'Other'\n" +
                "\tEND AS collaborative_learning_style,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.collaborative_learning_style\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.collaborative_learning_style\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.collaborative_learning_style, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.collaborative_learning_style = subquery2.collaborative_learning_style\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.collaborative_learning_style;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Student Preference On Collaborative Learning Styles Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> childrenReadOtherMaterialsInAdditionTextbooksTable(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.extra_learning_materials,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.extra_learning_materials,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.extra_learning_materials = 1 THEN 'Yes'\n" +
                "        Else 'No'\n" +
                "\tEND AS extra_learning_materials,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.extra_learning_materials\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, extra_learning_materials\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.extra_learning_materials, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.extra_learning_materials,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.extra_learning_materials = 1 THEN 'Yes'\n" +
                "        Else 'No'\n" +
                "\tEND AS extra_learning_materials,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.extra_learning_materials\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.extra_learning_materials\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.extra_learning_materials, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.extra_learning_materials = subquery2.extra_learning_materials\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.extra_learning_materials;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Student Who Read Other Materials In Addition To TextBooks Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> hoursOfIndividualStudyPerDayTable(RThreePayloadDto payloadDto) {
        
        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.daily_study_hours,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.daily_study_hours,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.daily_study_hours = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.daily_study_hours = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.daily_study_hours = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.daily_study_hours = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.daily_study_hours = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS daily_study_hours,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.daily_study_hours\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, daily_study_hours\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.daily_study_hours, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.daily_study_hours,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.daily_study_hours = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.daily_study_hours = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.daily_study_hours = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.daily_study_hours = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.daily_study_hours = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS daily_study_hours,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.daily_study_hours\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.daily_study_hours\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.daily_study_hours, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.daily_study_hours = subquery2.daily_study_hours\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.daily_study_hours;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Hours Of Individual Study/Practice Per-Day Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> paidPrivateTuitionHourTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.private_tuition_hours,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.private_tuition_hours,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.private_tuition_hours = 1 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.private_tuition_hours = 2 THEN '1-3 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 3 THEN '3-5 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 4 THEN 'More than 5 hrs'\n" +
                "\tEND AS private_tuition_hours,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.private_tuition_hours\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, private_tuition_hours\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.private_tuition_hours, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.private_tuition_hours,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "CASE\n" +
                "\t\tWHEN sed.private_tuition_hours = 1 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.private_tuition_hours = 2 THEN '1-3 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 3 THEN '3-5 hrs'\n" +
                "        WHEN sed.private_tuition_hours = 4 THEN 'More than 5 hrs'\n" +
                "\tEND AS private_tuition_hours,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.private_tuition_hours\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.private_tuition_hours\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.private_tuition_hours, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.private_tuition_hours = subquery2.private_tuition_hours\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.private_tuition_hours;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Paid Private Tution Hours Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> hoursSpentOnMobilePhonesStudyTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.time_spent_on_mobile_study,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.time_spent_on_mobile_study,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.time_spent_on_mobile_study = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 2 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS time_spent_on_mobile_study,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.time_spent_on_mobile_study\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, time_spent_on_mobile_study\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.time_spent_on_mobile_study, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.time_spent_on_mobile_study,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "CASE\n" +
                "\t\tWHEN sed.time_spent_on_mobile_study = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 2 THEN 'Up to 1 hr'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.time_spent_on_mobile_study = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS time_spent_on_mobile_study,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.time_spent_on_mobile_study\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.time_spent_on_mobile_study\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.time_spent_on_mobile_study, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.time_spent_on_mobile_study = subquery2.time_spent_on_mobile_study\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.time_spent_on_mobile_study;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Student Hours Spent On Mobile Phones Study Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> studentAccessToDigitalDevicesAtHomeTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.access_to_mobile_at_home,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.access_to_mobile_at_home,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.access_to_mobile_at_home  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS access_to_mobile_at_home,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.access_to_mobile_at_home\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, access_to_mobile_at_home\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.access_to_mobile_at_home, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.access_to_mobile_at_home,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "CASE\n" +
                "\t\tWHEN sed.access_to_mobile_at_home  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS access_to_mobile_at_home,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.access_to_mobile_at_home\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.access_to_mobile_at_home\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.access_to_mobile_at_home, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.access_to_mobile_at_home = subquery2.access_to_mobile_at_home\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.access_to_mobile_at_home;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Student Having Access To Digital Devices At Home Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> studentsUsingLearningAppHomeTable(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.learning_apps_at_home,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.learning_apps_at_home,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.learning_apps_at_home  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS learning_apps_at_home,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.learning_apps_at_home\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, learning_apps_at_home\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.learning_apps_at_home, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.learning_apps_at_home,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "CASE\n" +
                "\t\tWHEN sed.learning_apps_at_home  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS learning_apps_at_home,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.learning_apps_at_home\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.learning_apps_at_home\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.learning_apps_at_home, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.learning_apps_at_home = subquery2.learning_apps_at_home\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.learning_apps_at_home;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Students Using Learning Apps At Home Table Data Fetched",response, HttpStatus.OK.value());
    }


    public ApiResponse2<?> studentWhoHaveOneOrMoreSocialMediaAccountsTable(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.social_account,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.social_account,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.social_account  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS social_account,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.social_account\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, social_account\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.social_account, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.social_account,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "CASE\n" +
                "\t\tWHEN sed.social_account  = 1 THEN 'Yes'\n" +
                "\t\tELSE 'No'\n" +
                "\tEND AS social_account,\n" +
                "   \tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "   \t AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_extra_data sed\n" +
                "JOIN\n" +
                "\texam_details ed ON sed.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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
                "\tsd.state_id, sd.district_id, sed.social_account\n" +
                "ORDER BY\n" +
                "\tsd.state_id, sd.district_id, sed.social_account\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.social_account, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.social_account = subquery2.social_account\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.social_account;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Students Having One or More Social Media Accounts Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> typeOfSiteTable(RThreePayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.website_id,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.website_id,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\tWHEN swd.website_id = 1 THEN 'Edtech'\n" +
                "        WHEN swd.website_id = 2 THEN 'Entertainment (movies, games)'\n" +
                "        WHEN swd.website_id = 3 THEN 'Ecommerce &Lifestyle (flipchart, amazon, blinkit etc)'\n" +
                "        WHEN swd.website_id = 4 THEN 'Food and Beverages (Zomato, Swiggy)'\n" +
                "        WHEN swd.website_id = 5 THEN 'Travel'\n" +
                "        ELSE NULL\n" +
                "\tEND as website_id,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_websites swd\n" +
                "JOIN\n" +
                "\texam_details ed ON swd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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


        query.append("GROUP BY \n" +
                "\tsd.state_id, sd.district_id, website_id\n" +
                "ORDER BY \n" +
                "\tsd.state_id, sd.district_id, website_id\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.website_id, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.website_id,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\tWHEN swd.website_id = 1 THEN 'Edtech'\n" +
                "        WHEN swd.website_id = 2 THEN 'Entertainment (movies, games)'\n" +
                "        WHEN swd.website_id = 3 THEN 'Ecommerce &Lifestyle (flipchart, amazon, blinkit etc)'\n" +
                "        WHEN swd.website_id = 4 THEN 'Food and Beverages (Zomato, Swiggy)'\n" +
                "        WHEN swd.website_id = 5 THEN 'Travel'\n" +
                "        ELSE NULL\n" +
                "\tEND as website_id,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "\tstudent_websites swd\n" +
                "JOIN\n" +
                "\texam_details ed ON swd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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


        query.append("GROUP BY \n" +
                "\tsd.state_id, sd.district_id, website_id\n" +
                "ORDER BY \n" +
                "\tsd.state_id, sd.district_id, website_id\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.website_id, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.website_id = subquery2.website_id\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.website_id;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Type Of Sites Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> studentHoursSpendMobileForEntertainmentTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.hours_spent_on_mobile AS hours_spent_on_mobile,\n" +
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
                "        main_subquery.hours_spent_on_mobile,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.hours_spent_on_mobile = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.hours_spent_on_mobile = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.hours_spent_on_mobile = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS hours_spent_on_mobile,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
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
                "\ted.attempted = 1\n" +
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


        query.append("\tGROUP BY sd.state_id, sd.district_id, sed.hours_spent_on_mobile\n" +
                "\tORDER BY sd.state_id, sd.district_id, sed.hours_spent_on_mobile\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.hours_spent_on_mobile, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.hours_spent_on_mobile,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.hours_spent_on_mobile = 1 THEN 'Can''t Say'\n" +
                "        WHEN sed.hours_spent_on_mobile = 2 THEN 'Up to 1 hour'\n" +
                "        WHEN sed.hours_spent_on_mobile = 3 THEN '1-3 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 4 THEN '3-5 hrs'\n" +
                "        WHEN sed.hours_spent_on_mobile = 5 THEN 'More than 5 hrs'\n" +
                "\tEND AS hours_spent_on_mobile,\n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,\n" +
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


        query.append("\tGROUP BY sd.state_id, sd.district_id, sed.hours_spent_on_mobile\n" +
                "\tORDER BY sd.state_id, sd.district_id, sed.hours_spent_on_mobile\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.hours_spent_on_mobile, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.hours_spent_on_mobile = subquery2.hours_spent_on_mobile\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.hours_spent_on_mobile;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Student Hours Spent On Mobile Social/Entertainment Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> paidPrivateTuitionSubjectsTable(RThreePayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.tution_subject_name AS tution_subject_name,\n" +
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
                "        main_subquery.tution_subject_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tasb.subject AS tution_subject_name,  \n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,  \n" +
                "    AVG(ed.score) AS avg_score \n" +
                "FROM  \n" +
                "\tstudent_tuition_data stds \n" +
                "JOIN  \n" +
                "\tauro_subject asb ON stds.subject_id = asb.id \n" +
                "JOIN  \n" +
                "\texam_details ed ON stds.user_id = ed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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


        query.append("\tGROUP BY sd.state_id, sd.district_id, tution_subject_name\n" +
                "\tORDER BY sd.state_id, sd.district_id, tution_subject_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.tution_subject_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.tution_subject_name,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "    FROM (\n" +
                "        SELECT\n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "\tasb.subject AS tution_subject_name,  \n" +
                "\tCOUNT(DISTINCT ed.user_id) AS num_students,  \n" +
                "    AVG(ed.score) AS avg_score \n" +
                "FROM  \n" +
                "\tstudent_tuition_data stds \n" +
                "JOIN  \n" +
                "\tauro_subject asb ON stds.subject_id = asb.id \n" +
                "JOIN  \n" +
                "\texam_details ed ON stds.user_id = ed.user_id \n" +
                "JOIN  \n" +
                "\tstudent_extra_data sed ON ed.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON ed.user_id = sm.user_id\n" +
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


        query.append("\tGROUP BY sd.state_id, sd.district_id, tution_subject_name\n" +
                "\tORDER BY sd.state_id, sd.district_id, tution_subject_name\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.tution_subject_name, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.tution_subject_name = subquery2.tution_subject_name\n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.tution_subject_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Paid Private Tuition Subjects Table Data Fetched",response, HttpStatus.OK.value());

    }

}
