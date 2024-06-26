import React, { useState, useEffect } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement } from 'chart.js';
import AddCircleIcon from '@mui/icons-material/AddCircle';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement);

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
  labels: ['Cant Say', 'Up to 1 hr', '1-3 hrs', '3-5 hrs', 'More than 5 hrs'],
  datasets: [
    {
      label: 'Dataset 1',
      type: 'bar',
      backgroundColor: 'rgba(128,0,128,0.6)', // Purple color
      borderColor: 'rgba(128,0,128,1)',
      borderWidth: 2,
      data: [200, 150, 300, 250, 400],
      barThickness: 30, // Reduce the bar width
    },
    {
      label: 'Dataset 2',
      type: 'bar',
      backgroundColor: 'rgba(173,216,230,0.6)', // Light blue color
      borderColor: 'rgba(173,216,230,1)',
      borderWidth: 2,
      data: [180, 130, 250, 200, 350],
      barThickness: 30, // Reduce the bar width
    },
    {
      label: 'Average 1',
      type: 'line',
      borderColor: 'grey',
      borderWidth: 2,
      fill: false,
      data: [200, 150, 300, 250, 400].map((value, index) => ({
        x: index - 0.15, // Offset to the center of the first bar
        y: value,
      })),
    },
    {
      label: 'Average 2',
      type: 'line',
      borderColor: 'grey',
      borderWidth: 2,
      fill: false,
      data: [180, 130, 250, 200, 350].map((value, index) => ({
        x: index + 0.15, // Offset to the center of the second bar
        y: value,
      })),
    },
  ],
};

function CardComponent({ title, dropdownOptions }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1, setDateRange1] = useState(null);
  const [dateRange2, setDateRange2] = useState(null);
  const [dropdowns, setDropdowns] = useState(attributeBasedDropdowns[selectedAttribute] || []);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);

  useEffect(() => {
    setSelectedAttribute(title.id);
    setDropdowns(attributeBasedDropdowns[title.id] || []);
  }, [title]);

  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value.id);
    setDropdowns(attributeBasedDropdowns[value.id] || []);
  };

  const handleAddDropdown = (event, value) => {
    if (value) {
      setDropdowns((prev) => [...prev, value]);
      setShowAddMore(true);
    }
  };

  return (
    <Card className='mini-card'>
      <Typography 
        variant="h6" 
        sx={{ backgroundColor: '#f0f0f0', padding: '8px', borderRadius: '4px' }}
      >
        {title.value}
      </Typography>
      <CardContent>
        <Autocomplete
          options={dropdownOptions}
          getOptionLabel={(option) => option.value}
          value={dropdownOptions.find(option => option.id === selectedAttribute)}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
          sx={{ marginY: 2 }}
          
        />
        <Grid container spacing={1}>
          {dropdowns.map((dropdownLabel, index) => (
            <Grid item xs={12} sm={4} md={4} lg={4} key={index}>
              <Autocomplete
                options={attributeOptions[dropdownLabel]}
                renderInput={(params) => <TextField {...params} label={`Select ${dropdownLabel}`} size="small" />}
              />
            </Grid>
          ))}
          {availableFilters.length > 0 && showAddMore && (
            <Grid item xs={12} sm={4} md={4} lg={4}>
              <IconButton
                onClick={() => setShowAddMore(false)}
                color="primary"
                aria-label="add more filters"
                sx={{p:0,m:0}}
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

        <Bar 
          data={sampleChartData} 
          options={{ 
            responsive: true, 
            scales: { 
              x: { 
                type: 'category',
                stacked: false 
              }, 
              y: { 
                stacked: false 
              } 
            },
            plugins: {
              legend: {
                display: true
              }
            }
          }} 
        />
      </CardContent>
    </Card>
  );
}

export default CardComponent;
