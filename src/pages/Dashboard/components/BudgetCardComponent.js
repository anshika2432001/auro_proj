import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton, Checkbox } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement } from 'chart.js';
import AddCircleIcon from '@mui/icons-material/AddCircle';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement);

function BudgetCardComponent({ title, selectedAttribute, dropdownOptions, attributeBasedDropdowns, chartData, filterDropdowns, onCardFilterChange }) {
  const attributeOptions = {
    State: ["All", ...filterDropdowns]
  };
  
  const [currentAttribute, setCurrentAttribute] = useState(selectedAttribute);
  const [dateRange1Start, setDateRange1Start] = useState(null);
  const [dateRange1End, setDateRange1End] = useState(null);
  const initialDropdowns = attributeBasedDropdowns[selectedAttribute] ? attributeBasedDropdowns[selectedAttribute].slice(0, 3) : [];
  const [dropdowns, setDropdowns] = useState(initialDropdowns);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const initialFilters = attributeBasedDropdowns[selectedAttribute] ? attributeBasedDropdowns[selectedAttribute].slice(0, 3).reduce((acc, curr) => {
    if (curr === 'State') {
      return { ...acc, [curr]: ["All"] };
    } else {
      return { ...acc, [curr]: [] };
    }
  }, {}) : {};
  const [selectedFilters, setSelectedFilters] = useState(initialFilters);
  const chartRef = useRef(null);

  useEffect(() => {
    setCurrentAttribute(selectedAttribute);
    const newDropdowns = attributeBasedDropdowns[selectedAttribute] ? attributeBasedDropdowns[selectedAttribute].slice(0, 3) : [];
    setDropdowns(newDropdowns);
    setSelectedFilters(newDropdowns.reduce((acc, curr) => {
      if (curr === 'State') {
        return { ...acc, [curr]: ["All"] };
      } else {
        return { ...acc, [curr]: [] };
      }
    }, {}));
  }, [selectedAttribute, attributeBasedDropdowns]);

  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleAttributeChange = (event, value) => {
    if (value) {
      setCurrentAttribute(value.id);
      onCardFilterChange(value.id, "");
    }
  };

  const handleAddDropdown = (event, value) => {
    if (value) {
      setDropdowns((prev) => [...prev, value]);
      if (value === 'State') {
        setSelectedFilters((prev) => ({ ...prev, [value]: ["All"] }));
      } else {
        setSelectedFilters((prev) => ({ ...prev, [value]: [] }));
      }
      setShowAddMore(true);
    }
  };

  const handleFilterChange = (dropdownLabel) => (event, value) => {
    if (dropdownLabel === 'State') {
      if (value.includes("All") && value.length > 1) {
        setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value.filter(v => v !== "All") }));
        onCardFilterChange(currentAttribute, value.filter(v => v !== "All"));
      } else if (value.includes("All") && value.length === 1) {
        setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: ["All"] }));
        onCardFilterChange(currentAttribute, null);
      } else {
        setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value }));
        onCardFilterChange(currentAttribute, value);
      }
    } else {
      setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value }));
      onCardFilterChange(currentAttribute, value);
    }
  };

  return (
    <Card className='mini-card'>
      <Typography
        variant="h6"
        sx={{
          backgroundColor: '#0948a6', padding: '8px', top: '0',
          zIndex: 10, borderRadius: '4px', position: "sticky", color: '#fff',
        }}
      >
        {title}
      </Typography>
      <CardContent>
        <Autocomplete
          options={dropdownOptions}
          getOptionLabel={(option) => option.value}
          value={dropdownOptions.find(option => option.id === currentAttribute)}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
          sx={{ marginY: 2 }}
        />
        <Grid container spacing={1}>
          {dropdowns.map((dropdownLabel, index) => (
            <Grid item xs={12} sm={12} md={12} lg={12} key={index}>
              <Autocomplete
                multiple
                options={attributeOptions[dropdownLabel]}
                getOptionLabel={(option) => option}
                value={selectedFilters[dropdownLabel]}
                onChange={handleFilterChange(dropdownLabel)}
                disableCloseOnSelect
                renderOption={(props, option, { selected }) => (
                  <li {...props}>
                    <Checkbox
                      style={{ marginRight: 8 }}
                      checked={selected || (selectedFilters[dropdownLabel].includes("All") && option === "All")}
                    />
                    {option}
                  </li>
                )}
                renderInput={(params) => <TextField {...params} label={dropdownLabel} size="small" />}
              />
            </Grid>
          ))}
          {attributeBasedDropdowns[currentAttribute] && attributeBasedDropdowns[currentAttribute].length > 3 && showAddMore && (
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
                options={availableFilters.filter(option => attributeBasedDropdowns[currentAttribute]?.includes(option))}
                getOptionLabel={(option) => option}
                onChange={handleAddDropdown}
                renderInput={(params) => <TextField {...params} label="Add Filter" size="small" />}
              />
            </Grid>
          )}
        </Grid>

        <Grid container spacing={1} marginTop={0.5} marginBottom={4}>
          <Grid item xs={12} sm={3.5} md={3.5} lg={3.5}>
            <Typography variant="h6" gutterBottom>Date Range:</Typography>
          </Grid>
          <Grid item xs={12} sm={4.25} md={4.25} lg={4.25}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                label="Start Date"
                value={dateRange1Start}
                onChange={(newValue) => setDateRange1Start(newValue)}
                renderInput={(params) => <TextField {...params} size="small" />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12} sm={4.25} md={4.25} lg={4.25}>
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
        {chartData && chartData.labels && chartData.datasets ? (
          <Chart
            ref={chartRef}
            data={chartData}
            type="bar"
            options={{
              responsive: true,
              plugins: {
                legend: {
                  display: false
                },
              },
            }}
          />
        ) : (
          <Typography variant="body1" color="error">No data available for the chart.</Typography>
        )}
      </CardContent>
    </Card>
  );
}

export default BudgetCardComponent;
