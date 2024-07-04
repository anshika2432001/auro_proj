import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart} from 'react-chartjs-2';
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
  "Board of Education": ['All', 'CBSE', 'State Board', 'ICSE', 'International Board', 'Others', 'Both CBSE and State Board'],
  State: ['All', 'State 1', 'State 2', 'State 3'],
  District: ['All', 'District 1', 'District 2', 'District 3'],
  School: ['All', 'School 1', 'School 2', 'School 3'],
  "Date Period": ['All', 'Last Month', 'Last Quarter', 'Last Year'],
  Subject: ['All', 'Math', 'Science', 'History'],
  "Learning Level": ['All', 'Beginner', 'Intermediate', 'Advanced']
};

function CardComponent({ title, dropdownOptions, attributeBasedDropdowns, chartData }) {
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1Start, setDateRange1Start] = useState(null);
  const [dateRange1End, setDateRange1End] = useState(null);
  const [dateRange2Start, setDateRange2Start] = useState(null);
  const [dateRange2End, setDateRange2End] = useState(null);
  const initialDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
  const [dropdowns, setDropdowns] = useState(initialDropdowns);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const initialFilters = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}) : {};
  const [selectedFilters, setSelectedFilters] = useState(initialFilters);
  const chartRef = useRef(null);

  useEffect(() => {
    setSelectedAttribute(title.id);
    const newDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
  }, [title, attributeBasedDropdowns]);

  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  
  }, [dropdowns]);
 

  const handleAttributeChange = (event, value) => {
    setSelectedAttribute(value.id);
    const newDropdowns = attributeBasedDropdowns[value.id] ? attributeBasedDropdowns[value.id].slice(0, 3) : [];
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

  useEffect(() => {
    if (chartRef.current) {
      console.log(chartRef.current); // Check here if chartRef is populated
    }
  }, [chartRef]);

  

  const linePosition = {
    id: 'linePosition',
    beforeDatasetsDraw(chart,args,pluginOptions){
      console.log(chart.getDatasetMeta(0))
    }
  }

  return (
    <Card className='mini-card'>
       <Typography 
        variant="h6" 
        sx={{ backgroundColor: '#0948a6', padding: '8px',top: '0',
          zIndex: 10 , borderRadius: '4px',position:"sticky", color: '#fff',marginBottom:"20px" }}
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
          {attributeBasedDropdowns[selectedAttribute] && attributeBasedDropdowns[selectedAttribute].length > 3 && showAddMore && (
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
                options={availableFilters.filter(option => attributeBasedDropdowns[selectedAttribute]?.includes(option))}
                getOptionLabel={(option) => option}
                onChange={handleAddDropdown}
                renderInput={(params) => <TextField {...params} label="Add Filter" size="small" />}
              />
            </Grid>
          )}
        </Grid>

        <Grid container spacing={2} marginTop={0.5}>
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom>Date Range 1</Typography>
            <Grid container spacing={1.5}>
              <Grid item xs={12}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DatePicker
        label="Start Date"
        value={dateRange1Start}
        onChange={(newValue) => setDateRange1Start(newValue)}
        renderInput={(params) => (
          <TextField
            {...params}
            size="small"
          />
        )}
      />
    </LocalizationProvider>
              </Grid>
              <Grid item xs={12}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange1End}
                    onChange={(newValue) => setDateRange1End(newValue)}
                    renderInput={(params) => <TextField {...params} fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="h6" gutterBottom>Date Range 2</Typography>
            <Grid container spacing={1.5}>
              <Grid item xs={12}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                    value={dateRange2Start}
                    onChange={(newValue) => setDateRange2Start(newValue)}
                    renderInput={(params) => <TextField {...params} fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange2End}
                    onChange={(newValue) => setDateRange2End(newValue)}
                    renderInput={(params) => <TextField {...params} fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
        <Chart 
  ref={chartRef} 
  data={chartData} 
  options={{
    responsive: true,
    plugins: {
      legend: {
        display: false  
      },
      
    },
    scales: {
      x: {
        offset: true, 
        grid: {
          display: false
        }
      },
      y: {
        beginAtZero: true
      }
    },
  }}
  plugins={linePosition}
/>

      </CardContent>
    </Card>
  );
}

export default CardComponent;
