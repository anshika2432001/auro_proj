import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import "../../../App.css";

const attributeOptions = {
  "Gender": ['All', 'Male', 'Female', 'Other', 'Prefer not to say'],
  "School Location": ['All', 'Rural', 'Urban'],
  "School Type": ['All', 'Girls', 'Boys', 'Co-Ed'],
  "Age Group": ['All', 'upto 6', '6-10', '11-13', '14-15', '16-17', '>17'],
  "Grade": ['All', 'Pre-Primary', 'Primary', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
  "Social Group": ['All', 'SC', 'ST', 'OBC', 'General', 'Other'],
  "Board of Education": ['All', 'CBSE', 'State Board', 'ICSE', 'International Board', 'Others', 'Both CBSE and State Board']
};

const attributeBasedDropdowns = {
  1: ['Gender', 'School Location', 'Age Group'],
  2: ['Gender', 'Grade', 'Age Group'],
  3: ['School Location', 'Age Group', 'Grade'],
  4: ['Grade', 'Gender', 'School Location'],
  5: ['Grade', 'Age Group', 'Board of Education'],
  6: ['Gender', 'School Location', 'Grade']
};

function TableComponent({ title, dropdownOptions }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1, setDateRange1] = useState(null);
  const [dateRange2, setDateRange2] = useState(null);
  const [tableData, setTableData] = useState([]);
  const [dropdowns, setDropdowns] = useState(attributeBasedDropdowns[selectedAttribute] || []);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const [selectedFilters, setSelectedFilters] = useState(
    attributeBasedDropdowns[selectedAttribute].reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
  );

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

  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value.id);
    const newDropdowns = attributeBasedDropdowns[value.id] || [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
  };

  const handleAddDropdown = (event, value) => {
    if (value) {
      setDropdowns((prev) => [...prev, value]);
      setSelectedFilters((prev) => ({ ...prev, [value]: 'All' }));
      setShowAddMore(true);
    }
  };

  const handleFilterChange = (dropdownLabel) => (event, value) => {
    setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value }));
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
          {dropdowns.map((dropdownLabel, index) => (
            <Grid item xs={12} sm={4} md={4} lg={4} key={index}>
              <Autocomplete
                options={attributeOptions[dropdownLabel]}
                getOptionLabel={(option) => option}
                value={selectedFilters[dropdownLabel]}
                onChange={handleFilterChange(dropdownLabel)}
                renderInput={(params) => <TextField {...params} label={`${dropdownLabel}`} size="small" />}
              />
            </Grid>
          ))}
          {availableFilters.length > 0 && showAddMore && (
            <Grid item xs={12} sm={4} md={4} lg={4}>
              <IconButton
                onClick={() => setShowAddMore(false)}
                color="primary"
                aria-label="add more filters"
                sx={{ p: 0, m: 0 }}
              >
                <AddCircleIcon />
              </IconButton>
            </Grid>
          )}
          {!showAddMore && (
            <Grid item xs={12} sm={4} md={4} lg={4}>
              <Autocomplete
                options={availableFilters}
                getOptionLabel={(option) => option}
                onChange={handleAddDropdown}
                renderInput={(params) => <TextField {...params} label="Add Filter" size="small" />}
              />
            </Grid>
          )}
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
          <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
            <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
              <TableRow>
                <TableCell className="TableHeading">
                  <p className="HeadingData">Attributes</p>
                </TableCell>
                <TableCell className="TableHeading">
                  <p className="HeadingData">Date Range</p>
                </TableCell>
                <TableCell className="TableHeading">
                  <p className="HeadingData">Number of Students</p>
                </TableCell>
                <TableCell className="TableHeading">
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
