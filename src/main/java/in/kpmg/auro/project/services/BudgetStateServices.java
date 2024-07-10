package in.kpmg.auro.project.services;


import in.kpmg.auro.project.dtos.ApiResponse2;
import in.kpmg.auro.project.dtos.BudgetStatesPayloadDto;
import in.kpmg.auro.project.query.BudgetStateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public ApiResponse2<?> fundAllocated(String state) {

        String query = BudgetStateQuery.fundsAllocated;
        if (state !=null && !state.isEmpty()){
            List<Map<String,Object>> result = jdbcTemplate.queryForList(query).stream().filter(fund ->state.equals(fund.get("state_name"))).collect(Collectors.toList());

            return new ApiResponse2<>(true, "Budget State Data Fetched", result, HttpStatus.OK.value());
        }else {
            return new ApiResponse2<>(true, "Budget State Data Fetched", jdbcTemplate.queryForList(query), HttpStatus.OK.value());

        }
    }

    public ApiResponse2<?> budgetDataWithFilters(BudgetStatesPayloadDto payloadDto) {

        String query;

        if (payloadDto.getAttributeId() == 1){

            query = BudgetStateQuery.fundsAllocated;
            if (payloadDto.getStateName() !=null && !payloadDto.getStateName().isEmpty()){

                List<Map<String,Object>> result = jdbcTemplate.queryForList(query).stream().filter(fund ->payloadDto.getStateName().contains(fund.get("state_name"))).collect(Collectors.toList());

                return new ApiResponse2<>(true, "Budget State Data Fetched", result, HttpStatus.OK.value());
            }else {
                return new ApiResponse2<>(true, "Budget State Data Fetched", jdbcTemplate.queryForList(query), HttpStatus.OK.value());

            }


        } else if (payloadDto.getAttributeId() == 2) {

            query = BudgetStateQuery.publicExpenditure;
            if (payloadDto.getStateName() !=null && !payloadDto.getStateName().isEmpty()){
                List<Map<String,Object>> result = jdbcTemplate.queryForList(query).stream().filter(fund ->payloadDto.getStateName().contains(fund.get("state_name"))).collect(Collectors.toList());

                return new ApiResponse2<>(true, "Budget State Data Fetched", result, HttpStatus.OK.value());
            }else {
                return new ApiResponse2<>(true, "Budget State Data Fetched", jdbcTemplate.queryForList(query), HttpStatus.OK.value());

            }

        } else if (payloadDto.getAttributeId() == 3) {

            query = BudgetStateQuery.samagraSikshaFundsApproved;
            if (payloadDto.getStateName() !=null && !payloadDto.getStateName().isEmpty()){
                List<Map<String,Object>> result = jdbcTemplate.queryForList(query).stream().filter(fund ->payloadDto.getStateName().contains(fund.get("state_name"))).collect(Collectors.toList());

                return new ApiResponse2<>(true, "Budget State Data Fetched", result, HttpStatus.OK.value());
            }else {
                return new ApiResponse2<>(true, "Budget State Data Fetched", jdbcTemplate.queryForList(query), HttpStatus.OK.value());

            }

        } else if (payloadDto.getAttributeId() == 4) {

            query = BudgetStateQuery.samagraSikshaFundsReceived;
            if (payloadDto.getStateName() !=null && !payloadDto.getStateName().isEmpty()){
                List<Map<String,Object>> result = jdbcTemplate.queryForList(query).stream().filter(fund ->payloadDto.getStateName().contains(fund.get("state_name"))).collect(Collectors.toList());

                return new ApiResponse2<>(true, "Budget State Data Fetched", result, HttpStatus.OK.value());
            }else {
                return new ApiResponse2<>(true, "Budget State Data Fetched", jdbcTemplate.queryForList(query), HttpStatus.OK.value());

            }

        }



         return new ApiResponse2<>(true, "Budget State Data Fetched", "", HttpStatus.OK.value());

    }



}
