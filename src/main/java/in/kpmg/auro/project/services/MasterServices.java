package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.query.DashboardQuery;
import in.kpmg.auro.project.query.ROneQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MasterServices {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> getDashboadStatsData() {

        Map<String, Object> response = new HashMap<>();

        response.put("studentCount", jdbcTemplate.queryForList(ROneQuery.microScholorshipQuizAverageScore));

        return  new ApiResponse2<>(true, "Dashboard Stats Fetched",response, HttpStatus.OK.value());



    }
}
