package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.DashboardInputDto;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServicesMain {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public ApiResponse2<?> studentCountStats(DashboardInputDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("select \n" +
                "\tstm.state_name,\n" +
                "    count(distinct sd.user_id) as num_students\n" +
                "FROM\n" +
                "\tstudent_demographic sd\n" +
                "JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "\tstate_district_master sdm ON sd.district_id = sdm.district_id    \n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON sd.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON sd.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\texam_details ed ON sd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tuser_master um ON sd.user_id = um.user_id\n" +
                "LEFT JOIN\n" +
                "\tparent_extra_data ped ON um.parent_id = ped.user_id\n" +
                "WHERE\n" +
                "\ted.attempted = 1\n" +
                "\t-- AND sw.amount_status IN ('2','4','5')\n");

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
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;\n");


        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Student Count Stats Fetched",response, HttpStatus.OK.value());




    }

    public ApiResponse2<?> parentCountStats(DashboardInputDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("select \n" +
                "\tstm.state_name,\n" +
                "    count(distinct pm.user_id) as num_parents\n" +
                "FROM\n" +
                "\tparent_master pm\n" +
                "JOIN\n" +
                "\tuser_master um ON pm.user_id = um.parent_id\n" +
                "JOIN\n" +
                "\tstudent_demographic sd ON um.user_id = sd.user_id\n" +
                "JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON sd.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON sd.user_id = sm.user_id\n" +
                "JOIN\n" +
                "\texam_details ed ON sd.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstudent_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "JOIN\n" +
                "\tparent_extra_data ped ON pm.user_id = ped.user_id\n" +
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
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;\n");

        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Parents Count Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> teacherCountStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("select \n" +
                "\tstm.state_name,\n" +
                "    count(distinct tm.user_id) as num_teachers\n" +
                "FROM\n" +
                "\tteacher_master tm\n" +
                "JOIN\n" +
                "\tteacher_extra_data ted ON ted.user_id = tm.user_id\n" +
                "JOIN \n" +
                "\tteacher_student_mapping tsm ON tm.user_id = tsm.teacher_id\n" +
                "JOIN\n" +
                " \tstudent_demographic sd ON tsm.student_id = sd.user_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON tsm.student_id = sm.user_id\n" +
                "JOIN\n" +
                "\tstudent_extra_data sed ON sm.user_id = sed.user_id\n" +
                "JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "WHERE \n" +
                "\t  stm.state_name IS NOT NULL\n");


        List<Object> parameters = new ArrayList<>();



        if (payloadDto.getGender() !=null){
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

        }

//        if (payloadDto.getEmploymentNature() ==null){
//            query.append("\t ted.employment_nature = ? \n");
//            parameters.add(1);
//
//        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

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


        query.append("GROUP BY\n" +
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;\n");


        System.out.println(parameters);
        System.out.println(query);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));

        return  new ApiResponse2<>(true, "Teachers Count Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolCountStats() {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("select \n" +
                "\tstm.state_name,\n" +
                "    \tcount(distinct sm.SCHOOL_CODE) as num_schools\n" +
                "FROM\n" +
                "\tschoolmaster sm\n" +
                "JOIN\n" +
                "\tstate_master stm ON sm.stateid = stm.state_id\n" +
                "GROUP BY\n" +
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;\n");

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query)));

        return  new ApiResponse2<>(true, "School Count Stats Fetched",response, HttpStatus.OK.value());

    }
}
