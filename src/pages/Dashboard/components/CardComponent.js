import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, TextField, Autocomplete, Box } from '@mui/material';
import { DateRangePicker, LocalizationProvider } from '@mui/lab';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

// Register the required components
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const data = {
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

const attributeBasedDropdowns = {
  'Option 1': ['Gender', 'School', 'Age'],
  'Option 2': ['Gender', 'Marks', 'Grade'],
  'Option 3': ['School', 'Age', 'Grade'],
  'Option 4': ['Marks', 'Gender', 'School'],
  // Define dropdowns for other attributes similarly...
};

const commonOptions = ['Option A', 'Option B', 'Option C'];

function CardComponent({ title, dropdownOptions }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title);
  const [dateRange1, setDateRange1] = useState([null, null]);
  const [dateRange2, setDateRange2] = useState([null, null]);

  useEffect(() => {
    setSelectedAttribute(title);
  }, [title]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value);
  };

  const dynamicDropdowns = attributeBasedDropdowns[selectedAttribute] || [];

  return (
    <Card className='mini-card'>
      <CardContent>
        <Typography variant="h6">{title}</Typography>
        <Autocomplete
          options={dropdownOptions}
          value={selectedAttribute}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" />}
          sx={{ marginY: 2 }}
        />
        <Grid container spacing={2}>
          {dynamicDropdowns.map((dropdownLabel, index) => (
            <Grid item xs={4} key={index}>
              <TextField select label={`Select ${dropdownLabel}`} SelectProps={{ native: true }} fullWidth>
                {commonOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </TextField>
            </Grid>
          ))}
        </Grid>
        <Box sx={{ marginY: 2 }}>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DateRangePicker
              startText="Date Range 1"
              endText="to"
              value={dateRange1}
              onChange={(newValue) => setDateRange1(newValue)}
              renderInput={(startProps, endProps) => (
                <>
                  <TextField {...startProps} />
                  <Box sx={{ mx: 2 }}> to </Box>
                  <TextField {...endProps} />
                </>
              )}
            />
          </LocalizationProvider>
        </Box>
        <Box sx={{ marginY: 2 }}>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DateRangePicker
              startText="Date Range 2"
              endText="to"
              value={dateRange2}
              onChange={(newValue) => setDateRange2(newValue)}
              renderInput={(startProps, endProps) => (
                <>
                  <TextField {...startProps} />
                  <Box sx={{ mx: 2 }}> to </Box>
                  <TextField {...endProps} />
                </>
              )}
            />
          </LocalizationProvider>
        </Box>
        <Bar data={data} />
      </CardContent>
    </Card>
  );
}

export default CardComponent;
