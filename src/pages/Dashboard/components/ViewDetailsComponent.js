import React from "react";
import { useLocation } from "react-router-dom";
import { Chart } from "react-chartjs-2";
import {
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";

function useQuery() {
    return new URLSearchParams(useLocation().search);
  }

const ViewDetailsComponent = () => {
    const query = useQuery();
    const chartData = JSON.parse(query.get('chartData'));
    // const tableData = JSON.parse(query.get('tableData'));
    const selectedFilters = JSON.parse(query.get('selectedFilters'));
    const selectedAttribute = query.get('selectedAttribute');
    const dateRange1Start = new Date(query.get('dateRange1Start'));
    const dateRange1End = new Date(query.get('dateRange1End'));
    const dateRange2Start = new Date(query.get('dateRange2Start'));
    const dateRange2End = new Date(query.get('dateRange2End'));
  
    return (
        <div>
        <Typography variant="h4" gutterBottom>Detailed View</Typography>
        <Typography variant="h6" gutterBottom>Attribute: {selectedAttribute}</Typography>
        <Typography variant="h6" gutterBottom>Date Range 1: {dateRange1Start.toLocaleDateString()} - {dateRange1End.toLocaleDateString()}</Typography>
        <Typography variant="h6" gutterBottom>Date Range 2: {dateRange2Start.toLocaleDateString()} - {dateRange2End.toLocaleDateString()}</Typography>
        <Typography variant="h6" gutterBottom>Filters:</Typography>
        <ul>{Object.entries(selectedFilters).map(([key, value]) => <li key={key}>{key}: {typeof value === "object" ? value.name : value}</li>)}</ul>
        <div style={{ overflowX: "auto", marginTop: "1rem" }}>
          <div style={{ minWidth: "800px", minHeight: "400px" }}>
            <Chart type="bar" data={chartData} options={{ responsive: true }} />
          </div>
        </div>
        <Typography variant="h6" gutterBottom style={{ marginTop: "2rem" }}>Table Data:</Typography>
      </div>
    );
  }
  
  export default ViewDetailsComponent;
