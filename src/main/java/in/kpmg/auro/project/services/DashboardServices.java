package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
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


            return  new ApiResponse2<>(true, "Dashboard Stats Fetched",response, HttpStatus.OK.value());

        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse2<>(false, "Problem in fetching Dashboard Stats", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
