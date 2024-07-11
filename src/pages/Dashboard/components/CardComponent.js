import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart } from 'react-chartjs-2';
import axios from '../../../utils/axios';
import { useDispatch, useSelector } from "react-redux";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement } from 'chart.js';
import AddCircleIcon from '@mui/icons-material/AddCircle';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement);



function CardComponent({ title, dropdownOptions, attributeBasedDropdowns, chartData,onFilterChange }) {
  
  const filterOptions = useSelector((state) => state.filterDropdown.data.result);

  console.log(filterOptions)
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

  const mapGenders = (genders) => {
    return genders.map(genderObj => {
      if (genderObj.gender === 'M') return 'Male';
      if (genderObj.gender === 'F') return 'Female';
      if (genderObj.gender === 'O') return 'Other';
      if (genderObj.gender === '' ) return 'Prefer not to say';
    });
  };
  const mapSchoolLocation = (locations) => {
    return locations.map(locationObj => locationObj.school_location);
  };
  const mapSubjects = (subjects) => {
    return subjects.map(subjectObj => subjectObj.subject);
  };
  const mapAgeGroups = (ageGroups) => {
    return ageGroups.map(ageGroupObj => ageGroupObj.age_group);
  };
  const mapSocialGroup = (socialGroups) => {
    return socialGroups.map(socialGroupObj => socialGroupObj.social_group);
  };
  const mapEducationBoard = (educationBoards) => {
    return educationBoards.map(educationBoardObj => educationBoardObj.education_board);
  };
  const mapGrades = (grades) => {
    return grades.map(gradesObj => gradesObj.grade);
  };
  const mapFatherEducation = (fatherEducation) => {
    return fatherEducation.map(fatherEducationObj => fatherEducationObj.child_father_qualification);
  };
  const mapMotherEducation = (motherEducation) => {
    return motherEducation.map(motherEducationObj => motherEducationObj.child_mother_qualification);
  };
  const mapSchoolManagement = (schoolManagement) => {
    return schoolManagement.map(schoolManagementObj => schoolManagementObj.school_management);
  };
  const mapStateNames = (states)=> {
    return states.map(stateObj => stateObj.state_name);
 }

 const mapDistricts = (districts)=> {
  return districts.map(districtObj => districtObj.district_name);
}
//['State','District','School','Grade','Social Group','Gender','Annual Income','Subject','Mother Education','Father Education','Age Group','CWSN','Board of Education', 'School Location','School Management','School Category','School Type','Pre Primary' , 'School From starting class to end class']
 
  const attributeOptions = {
    "State": filterOptions.states ? ['All', ...mapStateNames(filterOptions.states)] : ['All'],
    District: filterOptions.districts ? ['All', ...mapDistricts(filterOptions.districts)] : ['All'],
    "School": ['All', 'School 1', 'School 2', 'School 3','School 4','School 5','School 6'],
    Grade: filterOptions.grades ? ['All', ...mapGrades(filterOptions.grades)] : ['All'],
    "Social Group": filterOptions.socialGroup ? ['All', ...mapSocialGroup(filterOptions.socialGroup)] : ['All'],
    Gender: filterOptions.genders ? ['All', ...mapGenders(filterOptions.genders)] : ['All'],
    "Annual Income":filterOptions.genders ? ['All', ...mapGenders(filterOptions.genders)] : ['All'],
    Subject: filterOptions.subjects ? ['All', ...mapSubjects(filterOptions.subjects)] : ['All'],
    "Mother Education":filterOptions.childMotherEducation ? ['All', ...mapMotherEducation(filterOptions.childMotherEducation)] : ['All'],
    "Father Education":filterOptions.childFatherEducation ? ['All', ...mapFatherEducation(filterOptions.childFatherEducation)] : ['All'],
    "Age Group": filterOptions.ageGroups ? ['All', ...mapAgeGroups(filterOptions.ageGroups)] : ['All'],
    "CWSN": ['All','Yes','No'],
    "Board of Education": filterOptions.educationalBoard ? ['All', ...mapEducationBoard(filterOptions.educationalBoard)] : ['All'],
    "School Location": filterOptions.schoolLocation ? ['All', ...mapSchoolLocation(filterOptions.schoolLocation)] : ['All'],
    "School Management": filterOptions.schoolManagement ? ['All', ...mapSchoolManagement(filterOptions.schoolManagement)] : ['All'],
    "School Category": filterOptions.schoolLocation ? ['All', ...mapSchoolLocation(filterOptions.schoolLocation)] : ['All'],
    "School Type": ['All', 'Girls', 'Boys', 'Co-Ed'],
    "Pre Primary": ['All', 'Not Attached', 'Govt Pre Primary Section','Private','Anganwadi'],
    "School From starting class to end class": ['All', 'Class I-V','Class I-VIII','Class I-IX','Class I-XII','Class VI-VIII','Class VI-X','Class VI-XII','Class IX-X','Class IX-XII'],
    
    
    
    
    
    
    
    
    "Date Period": ['All', 'Last Month', 'Last Quarter', 'Last Year'],
    
    "Learning Level": ['All', 'Beginner', 'Intermediate', 'Advanced']
  };
console.log(attributeBasedDropdowns)
 

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
    // Invoke callback to parent component
    onFilterChange(selectedAttribute, { ...selectedFilters, [dropdownLabel]: value });
  };

  const handleDateRangeChange = (dateRangeName, startDate, endDate) => {
    let newFilters = {};
    switch (dateRangeName) {
      case 'dateRange1':
        newFilters = {
          ...selectedFilters,
          startdateRange1: startDate,
          endDateRange1: endDate
        };
        setDateRange1Start(startDate);
        setDateRange1End(endDate);
        break;
      case 'dateRange2':
        newFilters = {
          ...selectedFilters,
          startDateRange2: startDate,
          enddaterange2: endDate
        };
        setDateRange2Start(startDate);
        setDateRange2End(endDate);
        break;
      default:
        break;
    }
    setSelectedFilters(newFilters);
    onFilterChange(selectedAttribute, newFilters);
  };
  // const handleFilterChange = (dropdownLabel) => (event, value) => {
  //   console.log('DropdownLabel:', dropdownLabel);
  //   console.log('Options:', attributeOptions[dropdownLabel]);
  //   console.log('Value:', value);
  //   setSelectedFilters((prev) => ({ ...prev, [dropdownLabel]: value }));
  // };

  // useEffect(() => {
  //   if (chartRef.current) {
  //     console.log(chartRef.current); // Check here if chartRef is populated
  //   }
  // }, [chartRef]);

  const linePosition = {
    id: 'linePosition',
    beforeDatasetsDraw(chart, args, pluginOptions) {
      chart.getDatasetMeta(2).data.forEach((datapoint, index) => {
        datapoint.x = chart.getDatasetMeta(0).data[index].x;
      });
      chart.getDatasetMeta(3).data.forEach((datapoint, index) => {
        datapoint.x = chart.getDatasetMeta(1).data[index].x;
      });
    }
  };

  return (
    <Card className='mini-card'>
      <Typography 
        variant="h6" 
        sx={{ backgroundColor: '#0948a6', padding: '8px', top: '0',
          zIndex: 10 , borderRadius: '4px', position:"sticky", color: '#fff' }}
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
        <Grid container rowSpacing={2} columnSpacing={1}>
          {dropdowns.map((dropdownLabel, index) => (
            <Grid item xs={12} sm={4} md={4} lg={4} key={index}>
              <Autocomplete
                options={attributeOptions[dropdownLabel] || []}
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
                    onChange={(newValue) => handleDateRangeChange('dateRange1', newValue, dateRange1End)}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange1End}
                    onChange={(newValue) => handleDateRangeChange('dateRange1', dateRange1Start, newValue)}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
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
                    onChange={(newValue) => handleDateRangeChange('dateRange2', newValue, dateRange2End)}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                    value={dateRange2End}
                    onChange={(newValue) => handleDateRangeChange('dateRange2', dateRange2Start, newValue)}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
            </Grid>
          </Grid>
        </Grid>
      </CardContent>
        <div style={{ overflowX: "auto", marginTop: "1rem", width: "100%", }}>
        <div style={{ minWidth: "800px",minHeight:"400px" }}>
          <Chart
            ref={chartRef}
            type="bar"
            data={chartData}
            options={{
              indexAxis: 'x', 
              responsive: true,
              plugins: {
                legend: {
                  display: false
                },
              },
              maintainAspectRatio: false, 
              scales: {
                y: {
                  beginAtZero: true,
                  ticks: {
                    stepSize: 30,
                    min: 0,
                    callback: function(value) {
                      return value;
                    }
                  }
                }
              }
            }}
            plugins={[linePosition]}
          />
        </div>
      </div>
     
    </Card>
 );
}

export default CardComponent;
