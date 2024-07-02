import React, { useEffect, useState } from 'react';
import { Typography, Card, CardContent, Grid, Autocomplete, TextField, IconButton } from '@mui/material';
import { makeStyles } from '@mui/styles';
import axios from '../../../utils/axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";

const useStyles = makeStyles({

  
  
  content: {
    paddingTop: '10px',
    height: '300px',
    overflowY: 'scroll',
  },
});

const colors = [
  'rgba(255,99,132,1)', 'rgba(255,159,64,1)', 'rgba(255,205,86,1)', 'rgba(75,192,192,1)', 'rgba(54,162,235,1)', 'rgba(153,102,255,1)', 'rgba(208,35,81,1)',
];

const capitalizeWords = (s) => s.replace(/\b\w/g, char => char.toUpperCase());

const attributeOptions = {
  Gender: ['All', 'Male', 'Female', 'Other', 'Prefer not to say'],
  'School Location': ['All', 'Rural', 'Urban'],
  'School Type': ['All', 'Girls', 'Boys', 'Co-Ed'],
  'Age Group': ['All', 'upto 6', '6-10', '11-13', '14-15', '16-17', '>17'],
  Grade: ['All', 'Pre-Primary', 'Primary', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
  'Social Group': ['All', 'SC', 'ST', 'OBC', 'General', 'Other'],
  'Board of Education': ['All', 'CBSE', 'State Board', 'ICSE', 'International Board', 'Others', 'Both CBSE and State Board'],
};

const attributeBasedDropdowns = {
  Students: ['Gender', 'School Location', 'Age Group'],
  Schools: ['Gender', 'Grade', 'Board of Education'],
  Teachers: ['School Location', 'Age Group', 'Grade'],
  Parents: ['Grade', 'Gender', 'School Location'],
};

const ChartCard = ({ title, dataKey, data }) => {
  const classes = useStyles();
  const [selectedAttribute, setSelectedAttribute] = useState(title);
  const [dropdowns, setDropdowns] = useState(attributeBasedDropdowns[selectedAttribute] || []);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const [selectedFilters, setSelectedFilters] = useState(
    (attributeBasedDropdowns[selectedAttribute] || []).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
  );
  const [dateRangeStart, setDateRangeStart] = useState(null);
  const [dateRangeEnd, setDateRangeEnd] = useState(null);
  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleFilterChange = (dropdownLabel) => (event, value) => {
    setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value }));
  };

  const handleAddDropdown = (event, value) => {
    if (value) {
      setDropdowns((prev) => [...prev, value]);
      setSelectedFilters((prev) => ({ ...prev, [value]: 'All' }));
      setShowAddMore(true);
    }
  };

  return (
    <Card className="dashboard-card">
      <Typography 
        variant="h6" 
        sx={{ backgroundColor: '#0948a6', padding: '8px', borderRadius: '4px', color: '#fff',marginBottom:"20px" }}
      >
        Number of {title}
      </Typography>
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
        <Grid container spacing={1} sx={{ mt: 1,mb:1 }}>
          <Grid item xs={12} sm={3} md={3} lg={3}>
            <Typography variant="body1" sx={{ mt: 1, textAlign: 'center' }}>Date Range:</Typography>
           </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="From Date"
                    value={dateRangeStart}
                    onChange={(newValue) => setDateRangeStart(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="To Date"
                    value={dateRangeEnd}
                    onChange={(newValue) => setDateRangeEnd(newValue)}
                    renderInput={(params) => <TextField {...params} size="small" />}
                  />
                </LocalizationProvider>
              </Grid>
            </Grid>
          
      <CardContent className={classes.content}>
        
        <ResponsiveContainer width="100%" height={data.length * 20}>
          <BarChart
            data={data}
            layout="vertical"
            margin={{ top: 15, right: 20, bottom: 7 }}
          >
            <CartesianGrid strokeDasharray="0" />
            <XAxis
              type="number"
              tick={{ fontSize: 12 }}
              interval={0}
              domain={[0, 'dataMax']}
              label={{
                value: `Number of ${title}`,
                position: 'insideBottom',
                offset: -5,
                style: { textAnchor: 'middle' },
                x: '50%',
              }}
            />
            <YAxis
              type="category"
              dataKey="state"
              width={160} // Increase the width to accommodate longer state names
              tick={{ fontSize: 12, textAnchor: 'end' }}
              interval={0}
              label={{
                value: 'States',
                angle: -90,
                position: 'insideLeft',
                style: { textAnchor: 'middle' },
              }}
            />
            <Tooltip formatter={(value) => `${value}`} />
            <Bar dataKey={dataKey} isAnimationActive={false}>
              {data.map((entry, index) => (
                <Bar key={`bar-${index}`} fill={entry.fill} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

const DashboardCards = () => {
  const [dashboardData, setDashboardData] = useState({
    studentCount: [],
    parentCount: [],
    teacherCount: [],
    schoolCount: [],
  });

  useEffect(() => {
    getDashboardInfo();
  }, []);

  const getDashboardInfo = async () => {
    try {
      const res = await axios.get('/dashboard-stats-data');
      const result = res.data.result;

      const studentCount = result.studentCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        students: item.num_students,
        fill: colors[index % colors.length],
      }));
      const parentCount = result.parentCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        parents: item.num_parents,
        fill: colors[index % colors.length],
      }));
      const teacherCount = result.teacherCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        teachers: item.num_teachers,
        fill: colors[index % colors.length],
      }));
      const schoolCount = result.schoolCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        schools: item.num_schools,
        fill: colors[index % colors.length],
      }));

      setDashboardData({ studentCount, parentCount, teacherCount, schoolCount });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Students" dataKey="students" data={dashboardData.studentCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Schools" dataKey="schools" data={dashboardData.schoolCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Teachers" dataKey="teachers" data={dashboardData.teacherCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Parents" dataKey="parents" data={dashboardData.parentCount} />
      </Grid>
    </Grid>
  );
};

export default DashboardCards;
