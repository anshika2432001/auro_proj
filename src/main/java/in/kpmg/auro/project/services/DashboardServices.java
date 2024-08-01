package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.TopicNameDto;
import in.kpmg.auro.project.query.DashboardQuery;
import in.kpmg.auro.project.query.FilterDropdownsQuery;
import in.kpmg.auro.project.query.SuperQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> getDashboadStatsData() {

        try {
            Map<String, Object> response = new HashMap<>();

            response.put("studentCount", jdbcTemplate.queryForList(DashboardQuery.countOfStudent));
            response.put("parentCount", jdbcTemplate.queryForList(DashboardQuery.countOfParents));
            response.put("teacherCount", jdbcTemplate.queryForList(DashboardQuery.countOfTeachers));
            response.put("schoolCount", jdbcTemplate.queryForList(DashboardQuery.countOfSchools));

            return  new ApiResponse2<>(true, "Dashboard Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse2<>(false, "Problem in fetching Dashboard Stats", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }

    public ApiResponse2<?> queryFetchData() {

        try {
            Map<String, Object> response = new HashMap<>();

            response.put("studentCount", jdbcTemplate.queryForList(SuperQuery.superQuery));

            return  new ApiResponse2<>(true, "Dashboard Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse2<>(false, "Problem in fetching Dashboard Stats", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }


    }

    public ApiResponse2<?> filterDropdowns() {

        try {
            Map<String, Object> response = new HashMap<>();

            response.put("grades", jdbcTemplate.queryForList(FilterDropdownsQuery.grades));
            response.put("subjects", jdbcTemplate.queryForList(FilterDropdownsQuery.subjects));
            response.put("schoolLocation", jdbcTemplate.queryForList(FilterDropdownsQuery.schoolLocation));
            response.put("socialGroup", jdbcTemplate.queryForList(FilterDropdownsQuery.socialGroup));
            response.put("states", jdbcTemplate.queryForList(FilterDropdownsQuery.states));
            response.put("districts", jdbcTemplate.queryForList(FilterDropdownsQuery.districts));
            response.put("genders",jdbcTemplate.queryForList(FilterDropdownsQuery.genders));
            response.put("ageGroups",jdbcTemplate.queryForList(FilterDropdownsQuery.ageGroups));
            response.put("educationalBoard",jdbcTemplate.queryForList(FilterDropdownsQuery.educationalBoard));
            response.put("schoolManagement",jdbcTemplate.queryForList(FilterDropdownsQuery.schoolManagement));
            response.put("transactionDate",jdbcTemplate.queryForList(FilterDropdownsQuery.transactionDate));
            response.put("amountStatus",jdbcTemplate.queryForList(FilterDropdownsQuery.amountStatus));
            response.put("topics",jdbcTemplate.queryForList(FilterDropdownsQuery.topics));
            response.put("classroomStrength",jdbcTemplate.queryForList(FilterDropdownsQuery.classroomStrength));
            response.put("childMotherEducation",jdbcTemplate.queryForList(FilterDropdownsQuery.childMotherEducation));
            response.put("childFatherEducation",jdbcTemplate.queryForList(FilterDropdownsQuery.childFatherEducation));
            response.put("language",jdbcTemplate.queryForList(FilterDropdownsQuery.language));
            response.put("householdIncome",jdbcTemplate.queryForList(FilterDropdownsQuery.householdIncome));
            response.put("cwsn",jdbcTemplate.queryForList(FilterDropdownsQuery.cwsn));
            response.put("genderTeachers",jdbcTemplate.queryForList(FilterDropdownsQuery.genderTeachers));
            response.put("qualificationTeachers",jdbcTemplate.queryForList(FilterDropdownsQuery.qualificationTeachers));
            response.put("modeOfEmploymentTeacher",jdbcTemplate.queryForList(FilterDropdownsQuery.modeOfEmploymentTeacher));
            response.put("schoolCategory",jdbcTemplate.queryForList(FilterDropdownsQuery.schoolCategory));
            response.put("schoolType",jdbcTemplate.queryForList(FilterDropdownsQuery.schoolType));



            return  new ApiResponse2<>(true, "Dashboard Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse2<>(false, "Problem in fetching Dashboard Stats", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    public ApiResponse2<?> fetchTopicNames(TopicNameDto dto) {

        Map<String, Object> response = new HashMap<>();

        String queryTopTopics = "SELECT\n" +
                "\taqn.quiz_name,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM\n" +
                "\tauro_quiz_name aqn\n" +
                "JOIN (\n" +
                "\tSELECT\n" +
                "\t\ted.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade,\n" +
                "        ed.user_id,\n" +
                "        ed.score\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "        AND sm.grade = '"+dto.getGrade()+"'\n" +
                "        AND ed.subject = '"+dto.getSubject()+"'\n" +
                ") AS subquery ON aqn.quiz_attempt = subquery.exam_name AND aqn.subject = subquery.subject AND aqn.student_class = subquery.grade\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score DESC\n" +
                "LIMIT 12;\n";

        response.put("topTopicNames",jdbcTemplate.queryForList((queryTopTopics)));


        String queryWeakTopics = "SELECT\n" +
                "\taqn.quiz_name,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM\n" +
                "\tauro_quiz_name aqn\n" +
                "JOIN (\n" +
                "\tSELECT\n" +
                "\t\ted.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade,\n" +
                "        ed.user_id,\n" +
                "        ed.score\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "        AND sm.grade = '"+dto.getGrade()+"'\n" +
                "        AND ed.subject = '"+dto.getSubject()+"'\n" +
                ") AS subquery ON aqn.quiz_attempt = subquery.exam_name AND aqn.subject = subquery.subject AND aqn.student_class = subquery.grade\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score ASC\n" +
                "LIMIT 12;\n";

        response.put("weakTopicNames",jdbcTemplate.queryForList((queryWeakTopics)));




        return  new ApiResponse2<>(true, "Topic Name Fetched",response, HttpStatus.OK.value());

    }

    public ApiResponse2<?> fetchWeakTopicsName(TopicNameDto dto) {

        Map<String, Object> response = new HashMap<>();

        String query = "SELECT\n" +
                "\taqn.quiz_name,\n" +
                "    AVG(subquery.score) AS avg_score\n" +
                "FROM\n" +
                "\tauro_quiz_name aqn\n" +
                "JOIN (\n" +
                "\tSELECT\n" +
                "\t\ted.exam_name,\n" +
                "        ed.subject,\n" +
                "        sm.grade,\n" +
                "        ed.user_id,\n" +
                "        ed.score\n" +
                "\tFROM\n" +
                "\t\texam_details ed\n" +
                "\tJOIN\n" +
                "\t\tstudent_master sm ON ed.user_id = sm.user_id\n" +
                "\tWHERE\n" +
                "\t\ted.attempted = 1\n" +
                "        AND sm.grade = '"+dto.getGrade()+"'\n" +
                "        AND ed.subject = '"+dto.getSubject()+"'\n" +
                ") AS subquery ON aqn.quiz_attempt = subquery.exam_name AND aqn.subject = subquery.subject AND aqn.student_class = subquery.grade\n" +
                "WHERE\n" +
                "\taqn.language_id = 1\n" +
                "GROUP BY\n" +
                "\taqn.quiz_name\n" +
                "ORDER BY\n" +
                "\tavg_score ASC;\n";

        response.put("topicNames",jdbcTemplate.queryForList((query)));

        return  new ApiResponse2<>(true, "Weak Performing Topic Name Fetched",response, HttpStatus.OK.value());

    }
}
