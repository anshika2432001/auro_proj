package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.query.DashboardQuery;
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
}
