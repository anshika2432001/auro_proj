package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.ROnePayloadDto;
import in.kpmg.auro.project.dtos.RTwoPayloadDto;
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
public class MasterServicesR2 {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> studentStrengthOfClassroom(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE\n" +
                "\t\tWHEN sed.classroom_strength = 1 THEN 'Below 15 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 2 THEN '16-25 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 3 THEN '26-35 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 4 THEN 'Above 35 Students'\n" +
                "\tEND AS classroom_strength,\n" +
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
                "\tAND sw.amount_status IN ('2','4','5') \n"
        );

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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }

//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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

        query.append("GROUP BY\n" +
                "\tsed.classroom_strength\n" +
                "ORDER BY\n" +
                "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n");

        query2.append("GROUP BY\n" +
                "\tsed.classroom_strength\n" +
                "ORDER BY\n" +
                "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));


        return  new ApiResponse2<>(true, "Student Strength Of The Classroom Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> academicStreams(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.subject_stream AS academic_stream,\n" +
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
                "\tAND sw.amount_status IN ('2','4','5')\n"
        );

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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }


//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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




        query.append("GROUP BY \n" +
                "    sed.subject_stream\n" +
                "ORDER BY \n" +
                "    sed.subject_stream;\n");

        query2.append("GROUP BY \n" +
                "    sed.subject_stream\n" +
                "ORDER BY \n" +
                "    sed.subject_stream;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);


        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Academic Stream Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> socialGroup(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.social_group,\n" +
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
                "\tAND sw.amount_status IN ('2','4','5')\n"
        );


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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }


//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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



        query.append("GROUP BY \n" +
                "    sed.social_group\n" +
                "ORDER BY \n" +
                "    sed.social_group;\n");


        query2.append("GROUP BY \n" +
                "    sed.social_group\n" +
                "ORDER BY \n" +
                "    sed.social_group;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);

        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Social Group Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> studentAccessBankAccount(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.student_access_to_bank,\n" +
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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }


//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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


        query.append("GROUP BY \n" +
                "    sed.student_access_to_bank\n" +
                "ORDER BY \n" +
                "    sed.student_access_to_bank;\n");

        query2.append("GROUP BY \n" +
                "    sed.student_access_to_bank\n" +
                "ORDER BY \n" +
                "    sed.student_access_to_bank;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);



        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Student With Access To Bank Account Stats Fetched",response, HttpStatus.OK.value());




    }

    public ApiResponse2<?> studentEngagementActivitiesSchool(RTwoPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.extra_curricular_activity,\n" +
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
                "\tAND sw.amount_status IN ('2','4','5')\n"
        );


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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }


//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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



        query.append("GROUP BY \n" +
                "    sed.extra_curricular_activity\n" +
                "ORDER BY \n" +
                "    sed.extra_curricular_activity;\n");

        query2.append("GROUP BY \n" +
                "    sed.extra_curricular_activity\n" +
                "ORDER BY \n" +
                "    sed.extra_curricular_activity;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);



        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Student Engagement Extracurricular Activities In School Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> studentsInLeadershipPositionsInSchoolClubs(RTwoPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.is_student_in_leadership_position,\n" +
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
                "\tAND sw.amount_status IN ('2','4','5')\n"
        );

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

        if (payloadDto.getGender() !=null){
            query.append("\tAND sd.gender = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND sd.gender = ? \n");
            parameters2.add(payloadDto.getGender());
        }

        if (payloadDto.getSocialGroup() !=null){
            query.append("\tAND sed.social_group = ? \n");
            parameters.add(payloadDto.getSocialGroup());

            query2.append("\tAND sed.social_group = ? \n");
            parameters2.add(payloadDto.getSocialGroup());
        }

        if (payloadDto.getCwsn() != null){
            query.append("\tAND sed.cwsn = ? \n");
            parameters.add(payloadDto.getCwsn());

            query2.append("\tAND sed.cwsn = ? \n");
            parameters2.add(payloadDto.getCwsn());
        }

        if (payloadDto.getEducationBoard() !=null){
            query.append("\tAND sd.education_board = ? \n");
            parameters.add(payloadDto.getEducationBoard());

            query2.append("\tAND sd.education_board = ? \n");
            parameters2.add(payloadDto.getEducationBoard());
        }

        if (payloadDto.getAgeFrom() !=null && payloadDto.getAgeTo() != null){
            query.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters.add(payloadDto.getAgeFrom());
            parameters.add(payloadDto.getAgeTo());

            query2.append("\tAND timestampdiff(YEAR, sd.dob, CURDATE()) BETWEEN ? and ? \n");
            parameters2.add(payloadDto.getAgeFrom());
            parameters2.add(payloadDto.getAgeTo());

        }

        if (payloadDto.getSchoolManagement() != null){
            query.append("\tAND sd.school_management = ? \n");
            parameters.add(payloadDto.getSchoolManagement());

            query2.append("\tAND sd.school_management = ? \n");
            parameters2.add(payloadDto.getSchoolManagement());
        }


//        if (payloadDto.getAvgScholorFrom() != null && payloadDto.getAvgScholorTo() != null){
//            query.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters.add(payloadDto.getAvgScholorFrom());
//            parameters.add(payloadDto.getAvgScholorTo());
//
//            query2.append("\tAND avg_sch.avg_scholarship BETWEEN ? AND ? \n");
//            parameters2.add(payloadDto.getAvgScholorFrom());
//            parameters2.add(payloadDto.getAvgScholorTo());
//        }


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


        query.append("GROUP BY \n" +
                "    sed.is_student_in_leadership_position\n" +
                "ORDER BY \n" +
                "    sed.is_student_in_leadership_position;\n");

        query2.append("GROUP BY \n" +
                "    sed.is_student_in_leadership_position\n" +
                "ORDER BY \n" +
                "    sed.is_student_in_leadership_position;\n");

        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(parameters2);
        System.out.println(query2);



        response.put("dataOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        return  new ApiResponse2<>(true, "Students In Leadership Positions In School Clubs In The School Stats Fetched",response, HttpStatus.OK.value());

    }


//    ------------------------------------------------------------------------------------------

    public ApiResponse2<?> studentStrengthOfClassroomStats(RTwoPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "CASE\n" +
                "\t\tWHEN sed.classroom_strength = 1 THEN 'Below 15 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 2 THEN '16-25 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 3 THEN '26-35 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 4 THEN 'Above 35 Students'\n" +
                "\tEND AS classroom_strength,\n" +
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

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "CASE\n" +
                "\t\tWHEN sed.classroom_strength = 1 THEN 'Below 15 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 2 THEN '16-25 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 3 THEN '26-35 Students'\n" +
                "        \t\tWHEN sed.classroom_strength = 4 THEN 'Above 35 Students'\n" +
                "\tEND AS classroom_strength,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "\tsed.classroom_strength\n" +
                "ORDER BY\n" +
                "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n");

        query2.append("GROUP BY\n" +
                "\tsed.classroom_strength\n" +
                "ORDER BY\n" +
                "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n");

        queryNation.append("GROUP BY\n" +
                "\tsed.classroom_strength\n" +
                "ORDER BY\n" +
                "\tFIELD(classroom_strength, 'Below 15 Students','16-25 Students','26-35 Students','Above 35 Students')\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Strength Of The Classroom Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> academicStreamsStats(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.subject_stream AS academic_stream,\n" +
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
                "    sed.subject_stream AS academic_stream,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "    sed.subject_stream\n" +
                "ORDER BY \n" +
                "    sed.subject_stream;\n");

        query2.append("GROUP BY \n" +
                "    sed.subject_stream\n" +
                "ORDER BY \n" +
                "    sed.subject_stream;\n");

        queryNation.append("GROUP BY \n" +
                "    sed.subject_stream\n" +
                "ORDER BY \n" +
                "    sed.subject_stream;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Academic Streams Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> socialGroupStats(RTwoPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.social_group,\n" +
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
                "    sed.social_group,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "    sed.social_group\n" +
                "ORDER BY \n" +
                "    sed.social_group;\n");

        query2.append("GROUP BY \n" +
                "    sed.social_group\n" +
                "ORDER BY \n" +
                "    sed.social_group;\n");

        queryNation.append("GROUP BY \n" +
                "    sed.social_group\n" +
                "ORDER BY \n" +
                "    sed.social_group;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Social Groups Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> studentAccessBankAccountStats(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.student_access_to_bank,\n" +
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
                "    sed.student_access_to_bank,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "    sed.student_access_to_bank\n" +
                "ORDER BY \n" +
                "    sed.student_access_to_bank;\n");

        query2.append("GROUP BY \n" +
                "    sed.student_access_to_bank\n" +
                "ORDER BY \n" +
                "    sed.student_access_to_bank;\n");

        queryNation.append("GROUP BY \n" +
                "    sed.student_access_to_bank\n" +
                "ORDER BY \n" +
                "    sed.student_access_to_bank;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student With Access to Bank Account / UPI Stats Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> studentEngagementActivitiesSchoolStats(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.extra_curricular_activity,\n" +
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
                "    sed.extra_curricular_activity,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "    sed.extra_curricular_activity\n" +
                "ORDER BY \n" +
                "    sed.extra_curricular_activity;\n");

        query2.append("GROUP BY \n" +
                "    sed.extra_curricular_activity\n" +
                "ORDER BY \n" +
                "    sed.extra_curricular_activity;\n");

        queryNation.append("GROUP BY \n" +
                "    sed.extra_curricular_activity\n" +
                "ORDER BY \n" +
                "    sed.extra_curricular_activity;\n");



        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Student Engagement In Extracurricular Activities In School Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> studentsInLeadershipPositionsInSchoolClubsStats(RTwoPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    sed.is_student_in_leadership_position,\n" +
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
                "    sed.is_student_in_leadership_position,\n" +
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
            query.append("\t\tAND sm.grade = ?");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ?");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ?");
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
                "    sed.is_student_in_leadership_position\n" +
                "ORDER BY \n" +
                "    sed.is_student_in_leadership_position;\n");

        query2.append("GROUP BY \n" +
                "    sed.is_student_in_leadership_position\n" +
                "ORDER BY \n" +
                "    sed.is_student_in_leadership_position;\n");


        queryNation.append("GROUP BY \n" +
                "    sed.is_student_in_leadership_position\n" +
                "ORDER BY \n" +
                "    sed.is_student_in_leadership_position;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Students In Leadership Positions In School Clubs In The School Stats Fetched",response, HttpStatus.OK.value());

    }
}