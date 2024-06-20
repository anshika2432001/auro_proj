import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import "../../../App.css";

const attributeOptions = {
  Gender: ['Male', 'Female', 'Other'],
  School: ['School A', 'School B', 'School C'],
  Age: ['10-15', '16-20', '21-25'],
  Marks: ['0-50', '51-75', '76-100'],
  Grade: ['A', 'B', 'C'],
};

const attributeBasedDropdowns = {
  1: ['Gender', 'School', 'Age'],
  2: ['Gender', 'Marks', 'Grade'],
  3: ['School', 'Age', 'Grade'],
  4: ['Marks', 'Gender', 'School'],
  5: ['Marks', 'Grade', 'Age'],
  6: ['Gender', 'School', 'Marks'],
};

function TableComponent({ title, dropdownOptions }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1, setDateRange1] = useState(null);
  const [dateRange2, setDateRange2] = useState(null);
  const [tableData, setTableData] = useState([]);

  useEffect(() => {
    setSelectedAttribute(title.id);
    // Fetch and set table data based on selectedAttribute and date ranges here
    const tableInfo = [
      {
        attributes: "Visual Learners",
        dateRange: "23/03/23-10/04/24",
        numStudents: "100",
        avgScore: "23"
      },
      {
        attributes: "Auditory",
        dateRange: "23/03/23-10/04/24",
        numStudents: "350",
        avgScore: "65"
      },
      {
        attributes: "Kinesthetic",
        dateRange: "23/03/23-10/04/24",
        numStudents: "220",
        avgScore: "34"
      },
      {
        attributes: "Reading/Writing",
        dateRange: "23/03/23-10/04/24",
        numStudents: "320",
        avgScore: "45"
      },
      {
        attributes: "Any Other",
        dateRange: "21/01/24-10/04/24",
        numStudents: "280",
        avgScore: "36"
      },
    ];
    setTableData(tableInfo);
  }, [title, dateRange1, dateRange2]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value.id);
  };

  const dynamicDropdowns = attributeBasedDropdowns[selectedAttribute] || [];

  return (
    <Card className='mini-card'>
      <CardContent>
        <Typography variant="h6" sx={{ backgroundColor: '#f0f0f0', padding: '8px', borderRadius: '4px', mb: 2 }}>
          Table Format Details
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4} md={4} lg={4}>
            <Autocomplete
              options={dropdownOptions}
              getOptionLabel={(option) => option.value}
              value={dropdownOptions.find(option => option.id === selectedAttribute)}
              onChange={handleAttributeChange}
              
              renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
              size="small"
            />
          </Grid>
          {dynamicDropdowns.map((dropdownLabel, index) => (
            <Grid item xs={12} sm={4} md={4} lg={4} key={index}>
              <Autocomplete
                options={attributeOptions[dropdownLabel]}
                renderInput={(params) => <TextField {...params} label={`Select ${dropdownLabel}`} size="small" />}
                size="small"
              />
            </Grid>
          ))}
        </Grid>

        <Grid container spacing={1} sx={{ mt: 1, mb: 1 }}>
          <Grid item xs={12} sm={2} md={2} lg={2} textAlign="left">
            <Typography variant="h6" sx={{ mt: 1 }}>Date Range:</Typography>
          </Grid>
          <Grid item xs={12} sm={4} md={4} lg={4}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label="Start Date"
                value={dateRange1}
                onChange={(newValue) => setDateRange1(newValue)}
                renderInput={(params) => <TextField {...params} size="small" />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12} sm={4} md={4} lg={4}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label="End Date"
                value={dateRange2}
                onChange={(newValue) => setDateRange2(newValue)}
                renderInput={(params) => <TextField {...params} size="small" />}
              />
            </LocalizationProvider>
          </Grid>
        </Grid>

        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 ,mt:2}} aria-label="simple table">
            <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
              <TableRow>
                
                <TableCell className="TableHeading" >
                      <p className="HeadingData">Attributes </p>
                    </TableCell>
                    <TableCell className="TableHeading" >
                      <p className="HeadingData">Date Range</p>
                    </TableCell>
                    <TableCell className="TableHeading" >
                      <p className="HeadingData">Number of Students </p>
                    </TableCell>
                    <TableCell className="TableHeading" >
                      <p className="HeadingData">Average Score of Students</p>
                    </TableCell>
               
              </TableRow>
            </TableHead>
            <TableBody>
              {tableData.map((row, index) => (
                <TableRow key={index}>
                 
                  <TableCell className="BodyBorder">
                          <p className="TableData">{row.attributes}</p>
                    </TableCell>
                    <TableCell className="BodyBorder">
                          <p className="TableData">{row.dateRange}</p>
                    </TableCell>
                    <TableCell className="BodyBorder">
                          <p className="TableData">{row.numStudents}</p>
                    </TableCell>
                    <TableCell className="BodyBorder">
                          <p className="TableData">{row.avgScore}</p>
                    </TableCell>
                  
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </CardContent>
    </Card>
  );
}

export default TableComponent;
