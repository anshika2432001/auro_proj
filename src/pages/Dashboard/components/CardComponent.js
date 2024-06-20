import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

// Register the required components
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// Define options for each specific attribute
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

const sampleChartData = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
  datasets: [
    {
      label: 'Dataset 1',
      backgroundColor: 'rgba(75,192,192,1)',
      borderColor: 'rgba(0,0,0,1)',
      borderWidth: 2,
      data: [65, 59, 80, 81, 56, 55],
    },
  ],
};

function CardComponent({ title, dropdownOptions }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1, setDateRange1] = useState(null);
  const [dateRange2, setDateRange2] = useState(null);

  useEffect(() => {
    setSelectedAttribute(title.id);
  }, [title]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value.id);
  };

  const dynamicDropdowns = attributeBasedDropdowns[selectedAttribute] || [];

  return (
    <Card className='mini-card'>
      <CardContent>
        <Typography 
          variant="h6" 
          sx={{ backgroundColor: '#f0f0f0', padding: '8px', borderRadius: '4px' }}
        >
          {title.value}
        </Typography>
        <Autocomplete
          options={dropdownOptions}
          getOptionLabel={(option) => option.value}
          value={dropdownOptions.find(option => option.id === selectedAttribute)}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
          sx={{ marginY: 2 }}
          size="small"
        />
        <Grid container spacing={1}>
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
          <Grid item xs={12} sm={3} md={3} lg={3} textAlign="center">
            <Typography variant="h6" sx={{ mt: 1 }}>Date Range:</Typography>
          </Grid>
          <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label="Start Date"
                value={dateRange1}
                onChange={(newValue) => setDateRange1(newValue)}
                renderInput={(params) => <TextField {...params} size="small" />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
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

        <Bar data={sampleChartData} />
      </CardContent>
    </Card>
  );
}

export default CardComponent;
