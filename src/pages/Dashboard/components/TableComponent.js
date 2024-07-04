import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import "../../../App.css";

const attributeOptions = {
  Gender: ['All', 'Male', 'Female', 'Other', 'Prefer not to say'],
  "School Location": ['All', 'Rural', 'Urban'],
  "School Type": ['All', 'Girls', 'Boys', 'Co-Ed'],
  "Age Group": ['All', 'upto 6', '6-10', '11-13', '14-15', '16-17', '>17'],
  Grade: ['All', 'Pre-Primary', 'Primary', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
  "Social Group": ['All', 'SC', 'ST', 'OBC', 'General', 'Other'],
  "Board of Education": ['All', 'CBSE', 'State Board', 'ICSE', 'International Board', 'Others', 'Both CBSE and State Board'],
  State: ['All', 'State 1', 'State 2', 'State 3'],
  District: ['All', 'District 1', 'District 2', 'District 3'],
  School: ['All', 'School 1', 'School 2', 'School 3'],
  "Date Period": ['All', 'Last Month', 'Last Quarter', 'Last Year'],
  Subject: ['All', 'Math', 'Science', 'History'],
  "Learning Level": ['All', 'Beginner', 'Intermediate', 'Advanced']
};

function TableComponent({ dropdownOptions, attributeBasedDropdowns,tableInfo,tableHeadings }) {
  const initialAttribute = dropdownOptions.length > 0 ? dropdownOptions[0].id : '';
  const [selectedAttribute, setSelectedAttribute] = useState(initialAttribute);
  const [dateRange1Start, setDateRange1Start] = useState(null);
  const [dateRange1End, setDateRange1End] = useState(null);
  const [dateRange2Start, setDateRange2Start] = useState(null);
  const [dateRange2End, setDateRange2End] = useState(null);
  const tableData = tableInfo; 
 
  const [dropdowns, setDropdowns] = useState(attributeBasedDropdowns[selectedAttribute] || []);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const initialFilters = attributeBasedDropdowns ? attributeBasedDropdowns[selectedAttribute]?.slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}) : {};
  const [selectedFilters, setSelectedFilters] = useState(initialFilters);

  useEffect(() => {
    const newDropdowns = attributeBasedDropdowns ? attributeBasedDropdowns[selectedAttribute]?.slice(0, 3) : [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
  }, [attributeBasedDropdowns, selectedAttribute]);

  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeBasedDropdowns[selectedAttribute]).filter(option => !usedFilters.has(option)));
 
  }, [dropdowns]);

 

  useEffect(() => {
    // Updating available filters based on selected dropdowns
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value);
    const newDropdowns = attributeBasedDropdowns[value] || [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));

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

  const handleShowMoreFilters = () => {
    setShowAddMore(false);
  };
  

  return (
    <Card className='dashboard-card'>
     
        <Typography variant="h6" sx={{ backgroundColor: '#0948a6', padding: '8px', borderRadius: '4px', mb: 2,color: '#fff',  }}>
          Table Format Details
        </Typography>
        <CardContent>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4} md={4} lg={4}>
            <Autocomplete
              options={dropdownOptions}
              getOptionLabel={(option) => option.value}
              value={dropdownOptions.find(option => option.id === selectedAttribute)}
              onChange={(event, value) => handleAttributeChange(event, value.id)}
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
          {attributeBasedDropdowns[selectedAttribute] && attributeBasedDropdowns[selectedAttribute].length > 3 && showAddMore && (
            <Grid item xs={12} sm={4} md={4} lg={4}>
              <IconButton
                onClick={handleShowMoreFilters}
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
                options={availableFilters.filter(option => attributeBasedDropdowns[selectedAttribute]?.includes(option))}
                getOptionLabel={(option) => option}
                onChange={handleAddDropdown}
                renderInput={(params) => <TextField {...params} label="Add Filter" size="small" />}
              />
            </Grid>
          )}
        </Grid>
        <Grid container spacing={1} sx={{ mt: 1, mb: 1 }}>
          <Grid item xs={12} sm={2} md={2} lg={2} textAlign="left">
            <Typography variant="h6" >Date Ranges:</Typography>
          </Grid>
          <Grid item xs={12} sm={10} md={10} lg={10}>
            <Grid container spacing={2}>
              <Grid item xs={6} sm={6} md={6} lg={6}>
              <Grid container spacing={1}>
              <Grid item xs={12}>
                <Typography
                  variant="subtitle1"
                
                >
                  Date Range 1:
                </Typography>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                    value={dateRange1Start}
                    onChange={(newValue) => setDateRange1Start(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange1End}
                    onChange={(newValue) => setDateRange1End(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
                </Grid>
                </Grid>
              </Grid>
              <Grid item xs={6} sm={6} md={6} lg={6}>
              <Grid container spacing={1}>
              <Grid item xs={12}>
                <Typography
                  variant="subtitle1"
                
                >
                  Date Range 2:
                </Typography>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                    value={dateRange2Start}
                    onChange={(newValue) => setDateRange2Start(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange2End}
                    onChange={(newValue) => setDateRange2End(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
                </Grid>
                </Grid>
              </Grid>
            </Grid>
          </Grid>
        </Grid>

        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
            <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
              <TableRow>
              {tableHeadings.map((heading, index) => (
                  <TableCell key={index} className="TableHeading">
                    <p className="HeadingData">{heading}</p>
                  </TableCell>
                ))}
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
                    <p className="TableData">{row.totalValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.avgValue}</p>
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
