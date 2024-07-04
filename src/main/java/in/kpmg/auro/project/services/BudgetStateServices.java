package in.kpmg.auro.project.services;

import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.query.BudgetStateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BudgetStateServices {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ApiResponse2<?> getBudgetStateData() {

        try {
            Map<String, Object> response = new HashMap<>();

            response.put("fundsAllocated", jdbcTemplate.queryForList(BudgetStateQuery.fundsAllocated));
            response.put("publicExpenditure", jdbcTemplate.queryForList(BudgetStateQuery.publicExpenditure));
            response.put("samagraSikshaFundsApproved", jdbcTemplate.queryForList(BudgetStateQuery.samagraSikshaFundsApproved));
            response.put("samagraSikshaFundsReceived", jdbcTemplate.queryForList(BudgetStateQuery.samagraSikshaFundsReceived));


            return new ApiResponse2<>(true, "Budget State Data Fetched", response, HttpStatus.OK.value());

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse2<>(false, "Problem in fetching Budget State Data", null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}