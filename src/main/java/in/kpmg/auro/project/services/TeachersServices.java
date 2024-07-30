package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.TeachersPayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeachersServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> totalTechingGradesStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.total_teaching_grades = '1' THEN '1'\n" +
                "        WHEN ted.total_teaching_grades = '2' THEN '2'\n" +
                "        WHEN ted.total_teaching_grades = '3' THEN '3'\n" +
                "        WHEN ted.total_teaching_grades = '4' THEN '4'\n" +
                "        WHEN ted.total_teaching_grades = '5' THEN 'More Than 5'\n" +
                "\tEND AS total_teaching_grades,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.total_teaching_grades = '1' THEN '1'\n" +
                "        WHEN ted.total_teaching_grades = '2' THEN '2'\n" +
                "        WHEN ted.total_teaching_grades = '3' THEN '3'\n" +
                "        WHEN ted.total_teaching_grades = '4' THEN '4'\n" +
                "        WHEN ted.total_teaching_grades = '5' THEN 'More Than 5'\n" +
                "\tEND AS total_teaching_grades,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n ");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }


        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.total_teaching_grades\n" +
                "ORDER BY \n" +
                "    ted.total_teaching_grades;\n");

        query2.append("GROUP BY\n" +
                "    ted.total_teaching_grades\n" +
                "ORDER BY \n" +
                "    ted.total_teaching_grades;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.total_teaching_grades\n" +
                "ORDER BY \n" +
                "    ted.total_teaching_grades;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return new ApiResponse2<>(true, "Total Teaching Grades Stats Fetched",response, HttpStatus.OK.value());



    }

    public ApiResponse2<?> teacherToTheNumberOfClassesRationStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "\tCASE \n" +
                "\t\tWHEN ted.teacher_classroom_type = 1 THEN 'Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 2 THEN 'Upper Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 3 THEN 'Primary and Upper Primary'\n" +
                "        WHEN ted.teacher_classroom_type = 4 THEN 'Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 5 THEN 'Higher Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 6 THEN 'Upper Primary and Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 7 THEN 'Secondary and Higher Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 8 THEN 'Pre-Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 9 THEN 'Pre Primary and Primary'\n" +
                "\tEND AS teacher_classroom_type,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score \n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                " ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "\tCASE \n" +
                "\t\tWHEN ted.teacher_classroom_type = 1 THEN 'Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 2 THEN 'Upper Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 3 THEN 'Primary and Upper Primary'\n" +
                "        WHEN ted.teacher_classroom_type = 4 THEN 'Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 5 THEN 'Higher Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 6 THEN 'Upper Primary and Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 7 THEN 'Secondary and Higher Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 8 THEN 'Pre-Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 9 THEN 'Pre Primary and Primary'\n" +
                "\tEND AS teacher_classroom_type,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.teacher_classroom_type\n" +
                "ORDER BY \n" +
                "    ted.teacher_classroom_type;\n");

        query2.append("GROUP BY\n" +
                "    ted.teacher_classroom_type\n" +
                "ORDER BY \n" +
                "    ted.teacher_classroom_type;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.teacher_classroom_type\n" +
                "ORDER BY \n" +
                "    ted.teacher_classroom_type;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Teacher To Number Of Classes Ratio Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> averageTeacherSalaryStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.monthly_salary = 1 THEN 'Below INR 37,000'\n" +
                "        WHEN ted.monthly_salary = 2 THEN 'INR 37,001 - INR 46,000'\n" +
                "        WHEN ted.monthly_salary = 3 THEN 'INR 46,001 - INR 55,000'\n" +
                "        WHEN ted.monthly_salary = 4 THEN 'Above INR 55,000'\n" +
                "\tEND AS monthly_salary,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.monthly_salary = 1 THEN 'Below INR 37,000'\n" +
                "        WHEN ted.monthly_salary = 2 THEN 'INR 37,001 - INR 46,000'\n" +
                "        WHEN ted.monthly_salary = 3 THEN 'INR 46,001 - INR 55,000'\n" +
                "        WHEN ted.monthly_salary = 4 THEN 'Above INR 55,000'\n" +
                "\tEND AS monthly_salary,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.monthly_salary\n" +
                "ORDER BY \n" +
                "    ted.monthly_salary;\n");

        query2.append("GROUP BY\n" +
                "    ted.monthly_salary\n" +
                "ORDER BY \n" +
                "    ted.monthly_salary;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.monthly_salary\n" +
                "ORDER BY \n" +
                "    ted.monthly_salary;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Average Teacher Salary Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> teachersToPupilRatioStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.pupil_ratio = 1 THEN '1:15'\n" +
                "        WHEN ted.pupil_ratio = 2 THEN '1:20'\n" +
                "        WHEN ted.pupil_ratio = 3 THEN '1:25'\n" +
                "        WHEN ted.pupil_ratio = 4 THEN '1:30'\n" +
                "        WHEN ted.pupil_ratio = 5 THEN '1:35'\n" +
                "        WHEN ted.pupil_ratio = 6 THEN '1:40'\n" +
                "\tEND AS pupil_ratio,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.pupil_ratio = 1 THEN '1:15'\n" +
                "        WHEN ted.pupil_ratio = 2 THEN '1:20'\n" +
                "        WHEN ted.pupil_ratio = 3 THEN '1:25'\n" +
                "        WHEN ted.pupil_ratio = 4 THEN '1:30'\n" +
                "        WHEN ted.pupil_ratio = 5 THEN '1:35'\n" +
                "        WHEN ted.pupil_ratio = 6 THEN '1:40'\n" +
                "\tEND AS pupil_ratio,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
                query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.pupil_ratio\n" +
                "ORDER BY \n" +
                "    ted.pupil_ratio;\n");

        query2.append("GROUP BY\n" +
                "    ted.pupil_ratio\n" +
                "ORDER BY \n" +
                "    ted.pupil_ratio;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.pupil_ratio\n" +
                "ORDER BY \n" +
                "    ted.pupil_ratio;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Teachers To Pupil Ratio Stats Fetched",response, HttpStatus.OK.value());

    }


    public ApiResponse2<?> QualificationOfTeachersStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\twhen qualification = 1 then 'Below Secondary'\n" +
                "        when qualification = 2 then 'Secondary'\n" +
                "        when qualification = 3 then 'Higher Secondary'\n" +
                "        when qualification = 4 then 'Graduate'\n" +
                "        when qualification = 5 then 'Post Graduate'\n" +
                "        when qualification = 6 then 'M.Phil.'\n" +
                "        when qualification = 7 then 'Ph.D.'\n" +
                "        when qualification = 8 then 'Post-Doctoral' \n" +
                "\tEND AS qualification,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\twhen qualification = 1 then 'Below Secondary'\n" +
                "        when qualification = 2 then 'Secondary'\n" +
                "        when qualification = 3 then 'Higher Secondary'\n" +
                "        when qualification = 4 then 'Graduate'\n" +
                "        when qualification = 5 then 'Post Graduate'\n" +
                "        when qualification = 6 then 'M.Phil.'\n" +
                "        when qualification = 7 then 'Ph.D.'\n" +
                "        when qualification = 8 then 'Post-Doctoral' \n" +
                "\tEND AS qualification,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.qualification\n" +
                "ORDER BY \n" +
                "    ted.qualification;\n");

        query2.append("GROUP BY\n" +
                "    ted.qualification\n" +
                "ORDER BY \n" +
                "    ted.qualification;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.qualification\n" +
                "ORDER BY \n" +
                "    ted.qualification;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Qualification Of Teachers Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> cceTrainingStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "     CASE\n" +
                "       WHEN ted.cce_training=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "     END AS cce_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN\n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "     CASE\n" +
                "       WHEN ted.cce_training=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "     END AS cce_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.cce_training;\n");

        query2.append("GROUP BY\n" +
                "    ted.cce_training;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.cce_training;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "CCE Training Stats Fetched",response, HttpStatus.OK.value());


    }

    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "     CASE\n" +
                "       WHEN ted.training_satisfication=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "     END AS training_satisfication,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "     CASE\n" +
                "       WHEN ted.training_satisfication=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "     END AS training_satisfication,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.training_satisfication;\n");

        query2.append("GROUP BY\n" +
                "    ted.training_satisfication;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.training_satisfication;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Training Satisfaction Of Teachers With Education Departments Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> natureOfEmploymentStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\twhen employment_nature = 1 then 'Regular'\n" +
                "        when employment_nature = 2 then 'Contract'\n" +
                "        when employment_nature = 3 then 'Part-Time/Guest' \n" +
                "\tEND AS employment_nature,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\twhen employment_nature = 1 then 'Regular'\n" +
                "        when employment_nature = 2 then 'Contract'\n" +
                "        when employment_nature = 3 then 'Part-Time/Guest' \n" +
                "\tEND AS employment_nature,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.employment_nature\n" +
                "ORDER BY \n" +
                "    ted.employment_nature;\n");

        query2.append("GROUP BY\n" +
                "    ted.employment_nature\n" +
                "ORDER BY \n" +
                "    ted.employment_nature;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.employment_nature\n" +
                "ORDER BY \n" +
                "    ted.employment_nature;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Nature Of Employment Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> daySpentByTeachersNonTeachingStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    ted.days_spend_on_non_teaching_school_activities AS days_spend_on_non_teaching_school_activities,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    ted.days_spend_on_non_teaching_school_activities AS days_spend_on_non_teaching_school_activities,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.days_spend_on_non_teaching_school_activities\n" +
                "ORDER BY \n" +
                "    ted.days_spend_on_non_teaching_school_activities;\n");

        query2.append("GROUP BY\n" +
                "    ted.days_spend_on_non_teaching_school_activities\n" +
                "ORDER BY \n" +
                "    ted.days_spend_on_non_teaching_school_activities;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.days_spend_on_non_teaching_school_activities\n" +
                "ORDER BY \n" +
                "    ted.days_spend_on_non_teaching_school_activities;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Day Spent By Teachers Non-Teaching Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.hours_given_in_mandatory_training = 1 THEN 'Less than 20 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 2 THEN '20 - 30 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 3 THEN '30 - 40 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 4 THEN '40 - 50 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 5 THEN 'More than 50 hours'\n" +
                "\tEND AS hours_given_in_mandatory_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.hours_given_in_mandatory_training = 1 THEN 'Less than 20 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 2 THEN '20 - 30 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 3 THEN '30 - 40 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 4 THEN '40 - 50 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 5 THEN 'More than 50 hours'\n" +
                "\tEND AS hours_given_in_mandatory_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.hours_given_in_mandatory_training\n" +
                "ORDER BY \n" +
                "    ted.hours_given_in_mandatory_training;\n");

        query2.append("GROUP BY\n" +
                "    ted.hours_given_in_mandatory_training\n" +
                "ORDER BY \n" +
                "    ted.hours_given_in_mandatory_training;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.hours_given_in_mandatory_training\n" +
                "ORDER BY \n" +
                "    ted.hours_given_in_mandatory_training;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Hours Teacher Spend On Yearly Mandatory Training Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.formative_assessments_periodicity = 1 THEN 'Weekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 2 THEN 'Biweekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 3 THEN 'Fortnightly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 4 THEN 'Monthly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 5 THEN 'Quarterly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 6 THEN 'Half Yearly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 7 THEN 'Annually'\n" +
                "        WHEN ted.formative_assessments_periodicity = 8 THEN 'Others'\n" +
                "\tEND AS formative_assessments_periodicity,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.formative_assessments_periodicity = 1 THEN 'Weekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 2 THEN 'Biweekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 3 THEN 'Fortnightly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 4 THEN 'Monthly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 5 THEN 'Quarterly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 6 THEN 'Half Yearly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 7 THEN 'Annually'\n" +
                "        WHEN ted.formative_assessments_periodicity = 8 THEN 'Others'\n" +
                "\tEND AS formative_assessments_periodicity,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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



        query.append("GROUP BY\n" +
                "    ted.formative_assessments_periodicity\n" +
                "ORDER BY \n" +
                "    ted.formative_assessments_periodicity;\n");

        query2.append("GROUP BY\n" +
                "    ted.formative_assessments_periodicity\n" +
                "ORDER BY \n" +
                "    ted.formative_assessments_periodicity;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.formative_assessments_periodicity\n" +
                "ORDER BY \n" +
                "    ted.formative_assessments_periodicity;\n");




        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Periodicity Of Formative Assessments In School Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.internal_brainstorm_learning_platform=1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS internal_brainstorm_learning_platform,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.internal_brainstorm_learning_platform=1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS internal_brainstorm_learning_platform,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.internal_brainstorm_learning_platform;\n");

        query2.append("GROUP BY\n" +
                "    ted.internal_brainstorm_learning_platform;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.internal_brainstorm_learning_platform;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Brainstorm Challenges Teachers Faced By Teachers During Teaching Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolsWithSMCsStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_smc =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_smc,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_smc =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_smc,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.is_smc;\n");

        query2.append("GROUP BY\n" +
                "    ted.is_smc;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.is_smc;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Schools With SMCs Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> functionalSMCsStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_smc_active =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_smc_active,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_smc_active =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_smc_active,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.is_smc_active;\n");

        query2.append("GROUP BY\n" +
                "    ted.is_smc_active;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.is_smc_active;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Functional SMCs Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_school_registered_on_vidyanjali =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_school_registered_on_vidyanjali,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "ed.attempted=1\n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "      CASE\n" +
                "        WHEN ted.is_school_registered_on_vidyanjali =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "      END AS is_school_registered_on_vidyanjali,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "JOIN \n" +
                "    student_master sm ON tsm.student_id = sm.user_id \n"+
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1\n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("GROUP BY\n" +
                "    ted.is_school_registered_on_vidyanjali;\n");

        query2.append("GROUP BY\n" +
                "    ted.is_school_registered_on_vidyanjali;\n");

        queryNation.append("GROUP BY\n" +
                "    ted.is_school_registered_on_vidyanjali;\n");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Schools Registered Vidyaanjali Portal Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> teachersStudentScoreStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("Select \n" +
                "\tstm.state_name,\n" +
                "    count(distinct tsm.teacher_id) as num_teachers,\n" +
                "    count(distinct ed.user_id) as num_students,\n" +
                "    AVG(ed.score) as avg_score\n" +
                "FROM\n" +
                "\tteacher_student_mapping tsm\n" +
                "JOIN\n" +
                "\tteacher_extra_data ted ON tsm.teacher_id = ted.user_id\n" +
                "JOIN \n" +
                "\tteacher_master tm ON tm.user_id = tsm.teacher_id\n" +
                "JOIN\n" +
                " \tstudent_demographic sd ON tsm.student_id = sd.user_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON tsm.student_id = sm.user_id\n" +
                "JOIN\n" +
                "\texam_details ed ON sm.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "WHERE\n" +
                "\ted.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("Select \n" +
                "\t'Nation' AS state_name,\n" +
                "    count(distinct tsm.teacher_id) as num_teachers,\n" +
                "    count(distinct ed.user_id) as num_students,\n" +
                "    AVG(ed.score) as avg_score\n" +
                "FROM\n" +
                "\tteacher_student_mapping tsm\n" +
                "JOIN\n" +
                "\tteacher_extra_data ted ON tsm.teacher_id = ted.user_id\n" +
                "JOIN \n" +
                "\tteacher_master tm ON tm.user_id = tsm.teacher_id\n" +
                "JOIN\n" +
                " \tstudent_demographic sd ON tsm.student_id = sd.user_id\n" +
                "JOIN\n" +
                "\tstudent_master sm ON tsm.student_id = sm.user_id\n" +
                "JOIN\n" +
                "\texam_details ed ON sm.user_id = ed.user_id\n" +
                "JOIN\n" +
                "\tstate_master stm ON sd.state_id = stm.state_id\n" +
                "WHERE\n" +
                "\ted.attempted=1 \n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("\tGROUP BY\n" +
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;");

        query2.append("\tGROUP BY\n" +
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;");

        queryNation.append("\tGROUP BY\n" +
                "\tstm.state_name\n" +
                "ORDER BY\n" +
                "\tstm.state_name;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Teacher Student Score Stats Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> frequencyParentTeacherMeetingStats(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.parent_teacher_meeting_intervals = 1 THEN 'Monthly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 2 THEN 'Once in two months'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 3 THEN 'Quarterly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 4 THEN 'Yearly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 5 THEN 'Other'\n" +
                "\tEND AS parent_teacher_meeting_intervals,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

        StringBuilder query2 = new StringBuilder(query);

        StringBuilder queryNation = new StringBuilder("SELECT\n" +
                "    CASE\n" +
                "\t\tWHEN ted.parent_teacher_meeting_intervals = 1 THEN 'Monthly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 2 THEN 'Once in two months'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 3 THEN 'Quarterly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 4 THEN 'Yearly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 5 THEN 'Other'\n" +
                "\tEND AS parent_teacher_meeting_intervals,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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
            query.append("\tAND tm.gender  = ? \n");
            parameters.add(payloadDto.getGender());

            query2.append("\tAND tm.gender =  ? \n");
            parameters2.add(payloadDto.getGender());

            queryNation.append("\tAND tm.gender =  ? \n");
            nationParameters.add(payloadDto.getGender());
        }

        if (payloadDto.getQualification() !=null){
            query.append("\tAND ted.qualification = ? \n");
            parameters.add(payloadDto.getQualification());

            query2.append("\tAND ted.qualification = ? \n");
            parameters2.add(payloadDto.getQualification());

            queryNation.append("\tAND ted.qualification = ? \n");
            nationParameters.add(payloadDto.getQualification());
        }

        if (payloadDto.getEmploymentNature() !=null){
            query.append("\tAND ted.employment_nature = ? \n");
            parameters.add(payloadDto.getEmploymentNature());

            query2.append("\tAND ted.employment_nature = ? \n");
            parameters2.add(payloadDto.getEmploymentNature());

            queryNation.append("\tAND ted.employment_nature  = ? \n");
            nationParameters.add(payloadDto.getEmploymentNature());
        }

        if (payloadDto.getGrades()!=null){
            query.append("\t\tAND sm.grade = ? \n");
            parameters.add(payloadDto.getGrades());

            query2.append("\t\tAND sm.grade = ? \n");
            parameters2.add(payloadDto.getGrades());

            queryNation.append("\t\tAND sm.grade = ? \n");
            nationParameters.add(payloadDto.getGrades());
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


        query.append("\tGROUP BY\n" +
                "    ted.parent_teacher_meeting_intervals\n" +
                "ORDER BY \n" +
                "    ted.parent_teacher_meeting_intervals;");

        query2.append("\tGROUP BY\n" +
                "    ted.parent_teacher_meeting_intervals\n" +
                "ORDER BY \n" +
                "    ted.parent_teacher_meeting_intervals;");

        queryNation.append("\tGROUP BY\n" +
                "    ted.parent_teacher_meeting_intervals\n" +
                "ORDER BY \n" +
                "    ted.parent_teacher_meeting_intervals;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");
        System.out.println(nationParameters);
        System.out.println(queryNation);

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        response.put("dataStateTwo",jdbcTemplate.queryForList(String.valueOf(query2),parameters2.toArray()));

        response.put("dataNation",jdbcTemplate.queryForList(String.valueOf(queryNation),nationParameters.toArray()));

        return  new ApiResponse2<>(true, "Parents Teacher Meeting Frequency Stats Fetched",response, HttpStatus.OK.value());

    }


//    --------------------------------TABLES------------------------------------------------
    public ApiResponse2<?> totalTeacherGradesTable(TeachersPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.total_teaching_grades,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.total_teaching_grades,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.total_teaching_grades = '1' THEN '1'\n" +
                "        WHEN ted.total_teaching_grades = '2' THEN '2'\n" +
                "        WHEN ted.total_teaching_grades = '3' THEN '3'\n" +
                "        WHEN ted.total_teaching_grades = '4' THEN '4'\n" +
                "        WHEN ted.total_teaching_grades = '5' THEN 'More Than 5'\n" +
                "\tEND AS total_teaching_grades,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.total_teaching_grades\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.total_teaching_grades \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.total_teaching_grades, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.total_teaching_grades,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.total_teaching_grades = '1' THEN '1'\n" +
                "        WHEN ted.total_teaching_grades = '2' THEN '2'\n" +
                "        WHEN ted.total_teaching_grades = '3' THEN '3'\n" +
                "        WHEN ted.total_teaching_grades = '4' THEN '4'\n" +
                "        WHEN ted.total_teaching_grades = '5' THEN 'More Than 5'\n" +
                "\tEND AS total_teaching_grades,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("GROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.total_teaching_grades\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.total_teaching_grades \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.total_teaching_grades, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.total_teaching_grades = subquery2.total_teaching_grades \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.total_teaching_grades;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Total Teaching Grades Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> teacherToTheNumberOfClassesRatioTable(TeachersPayloadDto payloadDto) {


        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.teacher_classroom_type,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.teacher_classroom_type,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE \n" +
                "\t\tWHEN ted.teacher_classroom_type = 1 THEN 'Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 2 THEN 'Upper Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 3 THEN 'Primary and Upper Primary'\n" +
                "        WHEN ted.teacher_classroom_type = 4 THEN 'Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 5 THEN 'Higher Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 6 THEN 'Upper Primary and Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 7 THEN 'Secondary and Higher Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 8 THEN 'Pre-Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 9 THEN 'Pre Primary and Primary'\n" +
                "\tEND AS teacher_classroom_type,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.teacher_classroom_type\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.teacher_classroom_type \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.teacher_classroom_type, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.teacher_classroom_type,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE \n" +
                "\t\tWHEN ted.teacher_classroom_type = 1 THEN 'Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 2 THEN 'Upper Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 3 THEN 'Primary and Upper Primary'\n" +
                "        WHEN ted.teacher_classroom_type = 4 THEN 'Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 5 THEN 'Higher Secondary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 6 THEN 'Upper Primary and Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 7 THEN 'Secondary and Higher Secondary'\n" +
                "        WHEN ted.teacher_classroom_type = 8 THEN 'Pre-Primary Only'\n" +
                "        WHEN ted.teacher_classroom_type = 9 THEN 'Pre Primary and Primary'\n" +
                "\tEND AS teacher_classroom_type,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.teacher_classroom_type\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.teacher_classroom_type \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.teacher_classroom_type, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.teacher_classroom_type = subquery2.teacher_classroom_type \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.teacher_classroom_type;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Teacher To Number Of Classes Ratio Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> averageTeacherSalaryTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.monthly_salary,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.monthly_salary,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.monthly_salary = 1 THEN 'Below INR 37,000'\n" +
                "        WHEN ted.monthly_salary = 2 THEN 'INR 37,001 - INR 46,000'\n" +
                "        WHEN ted.monthly_salary = 3 THEN 'INR 46,001 - INR 55,000'\n" +
                "        WHEN ted.monthly_salary = 4 THEN 'Above INR 55,000'\n" +
                "\tEND AS monthly_salary,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.monthly_salary\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.monthly_salary \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.monthly_salary, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.monthly_salary,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.monthly_salary = 1 THEN 'Below INR 37,000'\n" +
                "        WHEN ted.monthly_salary = 2 THEN 'INR 37,001 - INR 46,000'\n" +
                "        WHEN ted.monthly_salary = 3 THEN 'INR 46,001 - INR 55,000'\n" +
                "        WHEN ted.monthly_salary = 4 THEN 'Above INR 55,000'\n" +
                "\tEND AS monthly_salary,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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


        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.monthly_salary\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.monthly_salary \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.monthly_salary, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.monthly_salary = subquery2.monthly_salary \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.monthly_salary;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Average Teacher Salary Table Data Fetched",response, HttpStatus.OK.value());
    }

    public ApiResponse2<?> teachersToPupilRatioTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.pupil_ratio,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.pupil_ratio,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.pupil_ratio = 1 THEN '1:15'\n" +
                "        WHEN ted.pupil_ratio = 2 THEN '1:20'\n" +
                "        WHEN ted.pupil_ratio = 3 THEN '1:25'\n" +
                "        WHEN ted.pupil_ratio = 4 THEN '1:30'\n" +
                "        WHEN ted.pupil_ratio = 5 THEN '1:35'\n" +
                "        WHEN ted.pupil_ratio = 6 THEN '1:40'\n" +
                "\tEND AS pupil_ratio,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.pupil_ratio\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.pupil_ratio \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.pupil_ratio, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.pupil_ratio,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.pupil_ratio = 1 THEN '1:15'\n" +
                "        WHEN ted.pupil_ratio = 2 THEN '1:20'\n" +
                "        WHEN ted.pupil_ratio = 3 THEN '1:25'\n" +
                "        WHEN ted.pupil_ratio = 4 THEN '1:30'\n" +
                "        WHEN ted.pupil_ratio = 5 THEN '1:35'\n" +
                "        WHEN ted.pupil_ratio = 6 THEN '1:40'\n" +
                "\tEND AS pupil_ratio,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.pupil_ratio\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.pupil_ratio \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.pupil_ratio, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.pupil_ratio = subquery2.pupil_ratio \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.pupil_ratio;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Teacher To Pupil Ratio Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> qualificationOfTeachersTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.qualification,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.qualification,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\twhen qualification = 1 then 'Below Secondary'\n" +
                "        when qualification = 2 then 'Secondary'\n" +
                "        when qualification = 3 then 'Higher Secondary'\n" +
                "        when qualification = 4 then 'Graduate'\n" +
                "        when qualification = 5 then 'Post Graduate'\n" +
                "        when qualification = 6 then 'M.Phil.'\n" +
                "        when qualification = 7 then 'Ph.D.'\n" +
                "        when qualification = 8 then 'Post-Doctoral' \n" +
                "\tEND AS qualification,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.qualification\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.qualification,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\twhen qualification = 1 then 'Below Secondary'\n" +
                "        when qualification = 2 then 'Secondary'\n" +
                "        when qualification = 3 then 'Higher Secondary'\n" +
                "        when qualification = 4 then 'Graduate'\n" +
                "        when qualification = 5 then 'Post Graduate'\n" +
                "        when qualification = 6 then 'M.Phil.'\n" +
                "        when qualification = 7 then 'Ph.D.'\n" +
                "        when qualification = 8 then 'Post-Doctoral' \n" +
                "\tEND AS qualification,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.qualification\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.qualification \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.qualification, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.qualification = subquery2.qualification \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.qualification;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Qualification Of Teachers Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> cceTrainingTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.cce_training,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.cce_training,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "       WHEN ted.cce_training=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "    END AS cce_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.cce_training\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.cce_training \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.cce_training, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.cce_training,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "       WHEN ted.cce_training=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "    END AS cce_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.cce_training\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.cce_training \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.cce_training, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.cce_training = subquery2.cce_training \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.cce_training;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "CCE Training Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> trainingCertificationOfTeachersWithEducationDepartmentsTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.training_satisfication,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.training_satisfication,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "       WHEN ted.training_satisfication=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "\tEND AS training_satisfication,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.training_satisfication\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.training_satisfication \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.training_satisfication, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.training_satisfication,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "       WHEN ted.training_satisfication=1 THEN 'YES'\n" +
                "       ELSE 'NO' \n" +
                "\tEND AS training_satisfication,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.training_satisfication\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.training_satisfication \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.training_satisfication, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.training_satisfication = subquery2.training_satisfication \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.training_satisfication;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Training Satisfaction Of Teachers With Education Departments Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> natureOfEmploymentTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.employment_nature,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.employment_nature,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\twhen employment_nature = 1 then 'Regular'\n" +
                "        when employment_nature = 2 then 'Contract'\n" +
                "        when employment_nature = 3 then 'Part-Time/Guest' \n" +
                "\tEND AS employment_nature,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.employment_nature\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.employment_nature \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.employment_nature, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.employment_nature,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\twhen employment_nature = 1 then 'Regular'\n" +
                "        when employment_nature = 2 then 'Contract'\n" +
                "        when employment_nature = 3 then 'Part-Time/Guest' \n" +
                "\tEND AS employment_nature,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.employment_nature\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.employment_nature \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.employment_nature, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.employment_nature = subquery2.employment_nature \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.employment_nature;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Nature Of Employment Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> daySpentByTeachersNonTeachingTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.days_spend_on_non_teaching_school_activities,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.days_spend_on_non_teaching_school_activities,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    ted.days_spend_on_non_teaching_school_activities,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.days_spend_on_non_teaching_school_activities\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.days_spend_on_non_teaching_school_activities \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.days_spend_on_non_teaching_school_activities, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.days_spend_on_non_teaching_school_activities,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    ted.days_spend_on_non_teaching_school_activities AS days_spend_on_non_teaching_school_activities,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.days_spend_on_non_teaching_school_activities\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.days_spend_on_non_teaching_school_activities \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.days_spend_on_non_teaching_school_activities, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.days_spend_on_non_teaching_school_activities = subquery2.days_spend_on_non_teaching_school_activities \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.days_spend_on_non_teaching_school_activities;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Day Spent By Teacher Not Teaching Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> hoursTeacherSpendYearlyMandatoryTrainingTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.hours_given_in_mandatory_training,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.hours_given_in_mandatory_training,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.hours_given_in_mandatory_training = 1 THEN 'Less than 20 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 2 THEN '20 - 30 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 3 THEN '30 - 40 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 4 THEN '40 - 50 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 5 THEN 'More than 50 hours'\n" +
                "\tEND AS hours_given_in_mandatory_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.hours_given_in_mandatory_training\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.hours_given_in_mandatory_training \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.hours_given_in_mandatory_training, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.hours_given_in_mandatory_training,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.hours_given_in_mandatory_training = 1 THEN 'Less than 20 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 2 THEN '20 - 30 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 3 THEN '30 - 40 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 4 THEN '40 - 50 hours'\n" +
                "        WHEN ted.hours_given_in_mandatory_training = 5 THEN 'More than 50 hours'\n" +
                "\tEND AS hours_given_in_mandatory_training,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.hours_given_in_mandatory_training\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.hours_given_in_mandatory_training \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.hours_given_in_mandatory_training, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.hours_given_in_mandatory_training = subquery2.hours_given_in_mandatory_training \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.hours_given_in_mandatory_training;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Hours Teachers Spend Yearly In Mandatory Training Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> periodicityOfFormativeAssessmentsInSchoolTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.formative_assessments_periodicity,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.formative_assessments_periodicity,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.formative_assessments_periodicity = 1 THEN 'Weekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 2 THEN 'Biweekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 3 THEN 'Fortnightly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 4 THEN 'Monthly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 5 THEN 'Quarterly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 6 THEN 'Half Yearly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 7 THEN 'Annually'\n" +
                "        WHEN ted.formative_assessments_periodicity = 8 THEN 'Others'\n" +
                "\tEND AS formative_assessments_periodicity,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.formative_assessments_periodicity\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.formative_assessments_periodicity \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.formative_assessments_periodicity, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.formative_assessments_periodicity,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.formative_assessments_periodicity = 1 THEN 'Weekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 2 THEN 'Biweekly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 3 THEN 'Fortnightly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 4 THEN 'Monthly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 5 THEN 'Quarterly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 6 THEN 'Half Yearly'\n" +
                "        WHEN ted.formative_assessments_periodicity = 7 THEN 'Annually'\n" +
                "        WHEN ted.formative_assessments_periodicity = 8 THEN 'Others'\n" +
                "\tEND AS formative_assessments_periodicity,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.formative_assessments_periodicity\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.formative_assessments_periodicity \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.formative_assessments_periodicity, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.formative_assessments_periodicity = subquery2.formative_assessments_periodicity \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.formative_assessments_periodicity;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Periodicity Of Formative Assessments In School Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> brainstormChallengesTeachersFacedByTeacherDuringTeachingTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.internal_brainstorm_learning_platform,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.internal_brainstorm_learning_platform,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.internal_brainstorm_learning_platform=1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS internal_brainstorm_learning_platform,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.internal_brainstorm_learning_platform\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.internal_brainstorm_learning_platform \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.internal_brainstorm_learning_platform, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.internal_brainstorm_learning_platform,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.internal_brainstorm_learning_platform=1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS internal_brainstorm_learning_platform,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.internal_brainstorm_learning_platform\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.internal_brainstorm_learning_platform \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.internal_brainstorm_learning_platform, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.internal_brainstorm_learning_platform = subquery2.internal_brainstorm_learning_platform \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.internal_brainstorm_learning_platform;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Brainstorm Challenges Faced By Teachers During Teaching Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolsWithSMCsTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.is_smc,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_smc,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_smc =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_smc,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_smc\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_smc \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_smc, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_smc,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_smc =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_smc,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_smc\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_smc \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_smc, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.is_smc = subquery2.is_smc \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.is_smc;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Schools With SMCs Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> functionalSMCsTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.is_smc_active,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_smc_active,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_smc_active =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_smc_active,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_smc_active\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_smc_active \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_smc_active, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_smc_active,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_smc_active =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_smc_active,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_smc_active\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_smc_active \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_smc_active, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.is_smc_active = subquery2.is_smc_active \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.is_smc_active ;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Functional SMCs Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> schoolsRegisteredVidyaanjaliPortalTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.is_school_registered_on_vidyanjali,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_school_registered_on_vidyanjali,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_school_registered_on_vidyanjali =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_school_registered_on_vidyanjali,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_school_registered_on_vidyanjali\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_school_registered_on_vidyanjali \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_school_registered_on_vidyanjali, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.is_school_registered_on_vidyanjali,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "        WHEN ted.is_school_registered_on_vidyanjali =1 THEN 'YES'\n" +
                "        ELSE 'NO'\n" +
                "\tEND AS is_school_registered_on_vidyanjali,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.is_school_registered_on_vidyanjali\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.is_school_registered_on_vidyanjali \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.is_school_registered_on_vidyanjali, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.is_school_registered_on_vidyanjali = subquery2.is_school_registered_on_vidyanjali \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.is_school_registered_on_vidyanjali;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Schools Registered On Vidyanjali Portal Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> teachersStudentScoreTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "       main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id\n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "       main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Teacher Students Score Table Data Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> frequencyParentTeacherMeetingTable(TeachersPayloadDto payloadDto) {

        Map<String, Object> response = new HashMap<>();

        StringBuilder query = new StringBuilder("SELECT \n" +
                "    subquery1.state_id,\n" +
                "    subquery1.state_name,\n" +
                "    subquery1.district_id,\n" +
                "    subquery1.district_name,\n" +
                "    subquery1.parent_teacher_meeting_intervals,\n" +
                "    subquery1.num_teachers AS num_teachers_date1,\n" +
                "    subquery1.num_students AS num_students_date1,\n" +
                "    subquery1.avg_score AS avg_score_date1,\n" +
                "    subquery2.num_teachers AS num_teachers_date2,\n" +
                "    subquery2.num_students AS num_students_date2,\n" +
                "    subquery2.avg_score AS avg_score_date2\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_meeting_intervals,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.parent_teacher_meeting_intervals = 1 THEN 'Monthly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 2 THEN 'Once in two months'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 3 THEN 'Quarterly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 4 THEN 'Yearly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 5 THEN 'Other'\n" +
                "\tEND AS parent_teacher_meeting_intervals,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");

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

        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.parent_teacher_meeting_intervals\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.parent_teacher_meeting_intervals \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_meeting_intervals, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery1\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "\t\tmain_subquery.state_id,\n" +
                "        main_subquery.state_name,\n" +
                "        main_subquery.district_id,\n" +
                "        main_subquery.district_name,\n" +
                "        main_subquery.parent_teacher_meeting_intervals,\n" +
                "        main_subquery.num_teachers AS num_teachers,\n" +
                "        main_subquery.num_students AS num_students,\n" +
                "        main_subquery.avg_score AS avg_score\n" +
                "FROM (\n" +
                "\tSELECT \n" +
                "\tsd.state_id,\n" +
                "    stm.state_name,\n" +
                "    sd.district_id,\n" +
                "    sdm.district_name,\n" +
                "    CASE\n" +
                "\t\tWHEN ted.parent_teacher_meeting_intervals = 1 THEN 'Monthly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 2 THEN 'Once in two months'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 3 THEN 'Quarterly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 4 THEN 'Yearly'\n" +
                "        WHEN ted.parent_teacher_meeting_intervals = 5 THEN 'Other'\n" +
                "\tEND AS parent_teacher_meeting_intervals,\n" +
                "    COUNT(DISTINCT ted.user_id) AS num_teachers,\n" +
                "    COUNT(DISTINCT ed.user_id) AS num_students,\n" +
                "    AVG(ed.score) AS avg_score\n" +
                "FROM\n" +
                "    teacher_extra_data ted\n" +
                "JOIN\n" +
                "    teacher_master tm ON ted.user_id=tm.user_id\n" +
                "JOIN\n" +
                "    teacher_student_mapping tsm ON tm.user_id=tsm.teacher_id\n" +
                "LEFT JOIN \n" +
                "    exam_details ed ON tsm.student_id=ed.user_id\n" +
                "LEFT JOIN\n" +
                "    student_demographic sd ON ed.user_id = sd.user_id\n" +
                "LEFT JOIN\n" +
                "    state_master stm ON sd.state_id = stm.state_id\n" +
                "LEFT JOIN\n" +
                "    state_district_master sdm ON sd.district_id = sdm.district_id\n" +
                "JOIN\n" +
                "    student_wallet sw ON ed.eklavvya_exam_id = sw.eklavvya_exam_id\n" +
                "WHERE\n" +
                "    ed.attempted=1 \n");


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



        if (payloadDto.getStateId() !=null){
            query.append("\tAND sd.state_id = ? \n");
            parameters.add(payloadDto.getStateId());

        }

        if (payloadDto.getDistrictId() != null){
            query.append("\tAND sd.district_id = ? \n");
            parameters.add(payloadDto.getDistrictId());

        }

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


        query.append("\tGROUP BY\n" +
                "    sd.state_id, sd.district_id, ted.parent_teacher_meeting_intervals\n" +
                "HAVING\n" +
                "\tstm.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    sd.state_id, sd.district_id, ted.parent_teacher_meeting_intervals \n" +
                "    ) AS main_subquery\n" +
                "    GROUP BY \n" +
                "        main_subquery.parent_teacher_meeting_intervals, main_subquery.state_id, main_subquery.district_id\n" +
                ") AS subquery2\n" +
                "ON subquery1.state_id = subquery2.state_id AND subquery1.district_id = subquery2.district_id AND subquery1.parent_teacher_meeting_intervals = subquery2.parent_teacher_meeting_intervals \n" +
                "HAVING\n" +
                "\tsubquery1.state_name IS NOT NULL\n" +
                "ORDER BY \n" +
                "    subquery1.state_id, subquery1.district_id, subquery1.parent_teacher_meeting_intervals;");


        System.out.println(parameters);
        System.out.println(query);
        System.out.println("-------------------------");

        response.put("dataStateOne",jdbcTemplate.queryForList(String.valueOf(query),parameters.toArray()));
        return  new ApiResponse2<>(true, "Parent Teacher Meeting Intervals Table Data Fetched",response, HttpStatus.OK.value());

    }
}
