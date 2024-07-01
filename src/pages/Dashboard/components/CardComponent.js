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
  Gender: ['All', 'Male', 'Female', 'Other', 'Prefer not to say'],
  "School Location": ['All', 'Rural', 'Urban'],
  "School Type": ['All', 'Girls', 'Boys', 'Co-Ed'],
  "Age Group": ['All', 'upto 6', '6-10', '11-13', '14-15', '16-17', '>17'],
  Grade: ['All', 'Pre-Primary', 'Primary', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
  "Social Group": ['All', 'SC', 'ST', 'OBC', 'General', 'Other'],
  "Board of Education": ['All', 'CBSE', 'State Board', 'ICSE', 'International Board', 'Others', 'Both CBSE and State Board']
};

const attributeBasedDropdowns = {
  1: ['Gender', 'School Location', 'Age Group'],
  2: ['Gender', 'Grade'],
  3: ['School Location', 'Age Group', 'Grade'],
  4: ['Grade', 'Gender', 'School Location'],
  5: ['Grade', 'Age Group'],
  6: ['Gender', 'School Location', 'Grade']
};

const sampleChartData = {
  labels: ['Cant Say', 'Up to 1 hr', '1-3 hrs', '3-5 hrs', 'More than 5 hrs'],
  datasets: [
    {
      label: 'No of Students (Purple)',
      type: 'bar',
      backgroundColor: 'rgba(185,102,220,1)',
      borderColor: 'rgba(185,102,220,1)',
      borderWidth: 2,
      data: [200, 150, 300, 250, 400],
      barThickness: 30,
      order: 2,
    },
    {
      label: 'No of Students (Blue)',
      type: 'bar',
      backgroundColor: 'rgba(68,198,212,1)',
      borderColor: 'rgba(68,198,212,1)',
      borderWidth: 2,
      data: [100, 130, 250, 200, 350],
      barThickness: 30,
      order: 2,
    },
    {
      label: 'Average no. of students (Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [175, 140, 275, 225, 375],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average no. of students (Blue)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [75, 80, 150, 125, 225],
      spanGaps: true,
      order: 1,
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
  const [selectedFilters, setSelectedFilters] = useState(
    attributeBasedDropdowns[selectedAttribute].reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
  );

  useEffect(() => {
    setSelectedAttribute(title.id);
    const newDropdowns = attributeBasedDropdowns[title.id] || [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
  }, [title]);

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
              tooltip: {
                callbacks: {
                  label: function(tooltipItem) {
                    const datasetIndex = tooltipItem.datasetIndex;
                    const dataIndex = tooltipItem.dataIndex;
                    const barWidth = 30; // Assuming bar width is fixed at 30

                    // Calculate x position for tooltip
                    let xPosition = 0;
                    if (datasetIndex === 0) {
                      xPosition = dataIndex * barWidth + barWidth / 2;
                    } else if (datasetIndex === 1) {
                      xPosition = dataIndex * barWidth + 3 * barWidth / 2;
                    }

                    const yPosition = tooltipItem.raw;

                    return `Avg: (${xPosition.toFixed(2)}, ${yPosition.toFixed(2)})`;
                  }
                }
              },
              legend: {
                display: false
              }
            }
          }} 
        />
      </CardContent>
    </Card>
  );
}

export default CardComponent;
