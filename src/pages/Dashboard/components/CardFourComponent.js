import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton,Box,Button } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart } from 'react-chartjs-2';
import axios from '../../../utils/axios';
import dayjs from 'dayjs';
import { useDispatch, useSelector } from "react-redux";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement } from 'chart.js';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CircularProgress from '@mui/material/CircularProgress';
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement);



function CardFourComponent({ title, dropdownOptions, attributeBasedDropdowns, chartData,onFilterChange,cardKey,loadingStatus,apiEndPoints,cardMapping,dataAvailableStatus,category }) {
console.log(cardMapping)
  const filterOptions = useSelector((state) => state.filterDropdown.data.result);
  const defaultStateId = 7; 
  
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [dateRange1Start, setDateRange1Start] = useState(dayjs('2024-01-01'));
  const [dateRange1End, setDateRange1End] = useState(dayjs('2024-01-31'));
 
  const initialDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
  const [dropdowns, setDropdowns] = useState(initialDropdowns);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const initialFilters = attributeBasedDropdowns[title.id]
  ? attributeBasedDropdowns[title.id].slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
  : {};
  const [selectedFilters, setSelectedFilters] = useState({ ...initialFilters, 'State': { id: defaultStateId, name: 'Default State Name' } });
  const [districtOptions, setDistrictOptions] = useState([]);
  const chartRef = useRef(null);

  const mapGenders = (genders) => {
    
      return genders.map(genderObj => ({ id: genderObj.gender, name: genderObj.gender_name }));
    
  };
  const mapSchoolLocation = (locations) => {
    return locations.map(locationObj => ({ id: locationObj.school_location, name: locationObj.location_name }));
  };
  const mapSubjects = (subjects) => {
    return subjects.map(subjectObj => subjectObj.subject);
  };
  const mapAgeGroups = (ageGroups) => {
    return ageGroups.map(ageGroupObj => ageGroupObj.age_group);
  };
  const mapSocialGroup = (socialGroups) => {
    return socialGroups.map(socialGroupObj => ({ id: socialGroupObj.social_group, name: socialGroupObj.social_group_name }));
  };
  const mapCWSN = (cwsn) => {
    return cwsn.map(cwsnObj => ({ id: cwsnObj.cwsn, name: cwsnObj.cwsn_status }));
  };
  const mapEducationBoard = (educationBoards) => {
    return educationBoards.map(educationBoardObj =>  ({ id: educationBoardObj.education_board_name, name: educationBoardObj.education_board }));
  };
  const mapGrades = (grades) => {
    return grades.map(gradesObj => gradesObj.grade);
  };
  const mapAnnualIncome = (income) => {
    return income.map(incomeObj => incomeObj.household_income);
  };
  const mapFatherEducation = (fatherEducation) => {
    return fatherEducation.map(fatherEducationObj => fatherEducationObj.child_father_qualification);
  };
  const mapMotherEducation = (motherEducation) => {
    return motherEducation.map(motherEducationObj => motherEducationObj.child_mother_qualification);
  };
  const mapSchoolManagement = (schoolManagement) => {
    return schoolManagement.map(schoolManagementObj => ({ id: schoolManagementObj.school_management_name, name: schoolManagementObj.school_management }));
  };
  const mapStateNames = (states) => {
    return states.map(stateObj => ({ id: stateObj.state_id, name: stateObj.state_name }));
  }
  const mapQualification = (qualifications) => {
    return qualifications.map(qualificationObj => ({ id: qualificationObj.qualification, name: qualificationObj.qualification_name }));
  };
  const mapEmployment = (employments) => {
    return employments.map(employmentObj => ({ id: employmentObj.employment_nature, name: employmentObj.employment_nature_name }));
  };

  const mapDistricts = (districts) => {
    return districts.map(districtObj => ({
      id: districtObj.district_id,
      name: districtObj.district_name,
      state_id: districtObj.state_id
    }));
  };
 
  //filter dropdown options
  const attributeOptions = {
    "State": filterOptions ? (filterOptions.states ? mapStateNames(filterOptions.states) : []) : [],
    "District": districtOptions ? districtOptions: [{ id: 'All', name: 'All' }],
    "School": ['All', 'School 1', 'School 2', 'School 3','School 4','School 5','School 6'],
    Grade: filterOptions ? (filterOptions.grades ? ['All', ...mapGrades(filterOptions.grades)] : ['All']) : ['All'],
    "Social Group": filterOptions ? (filterOptions.socialGroup ? [{ id: 'All', name: 'All' }, ...mapSocialGroup(filterOptions.socialGroup)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    Gender: filterOptions ? (filterOptions.genders ? [{ id: 'All', name: 'All' }, ...mapGenders(filterOptions.genders)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "Annual Income": filterOptions ? (filterOptions.householdIncome ? ['All', ...mapAnnualIncome(filterOptions.householdIncome)] : ['All']) : ['All'],
    Subject: filterOptions ? (filterOptions.subjects ? ['All', ...mapSubjects(filterOptions.subjects)] : ['All']) : ['All'],
    "Mother Education": filterOptions ? (filterOptions.childMotherEducation ? ['All', ...mapMotherEducation(filterOptions.childMotherEducation)] : ['All']) : ['All'],
    "Father Education": filterOptions ? (filterOptions.childFatherEducation ? ['All', ...mapFatherEducation(filterOptions.childFatherEducation)] : ['All']) : ['All'],
    "Age Group": filterOptions ? (filterOptions.ageGroups ? ['All', ...mapAgeGroups(filterOptions.ageGroups)] : ['All']) : ['All'],
  "CWSN": filterOptions ? (filterOptions.cwsn ? [{ id: 'All', name: 'All' }, ...mapCWSN(filterOptions.cwsn)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "Board of Education": filterOptions ? (filterOptions.educationalBoard ? [{ id: 'All', name: 'All' }, ...mapEducationBoard(filterOptions.educationalBoard)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "School Location": filterOptions ? (filterOptions.schoolLocation ?  [{ id: 'All', name: 'All' }, ...mapSchoolLocation(filterOptions.schoolLocation)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "School Management": filterOptions ? (filterOptions.schoolManagement ? [{ id: 'All', name: 'All' }, ...mapSchoolManagement(filterOptions.schoolManagement)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }], 
    "School Category": filterOptions ? (filterOptions.schoolLocation ? ['All', ...mapSchoolLocation(filterOptions.schoolLocation)] : ['All']) : ['All'],
    "School Type": ['All', 'Girls', 'Boys', 'Co-Ed'],
    "Qualification": filterOptions ? (filterOptions.qualificationTeachers ? [{ id: 'All', name: 'All' }, ...mapQualification(filterOptions.qualificationTeachers)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "Mode of Employment": filterOptions ? (filterOptions.modeOfEmploymentTeacher ? [{ id: 'All', name: 'All' }, ...mapEmployment(filterOptions.modeOfEmploymentTeacher)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
  
  };

  
  

 
//updated selectedAttribute and filters based on title and attributeBasedDropdowns
  useEffect(() => {
    setSelectedAttribute(title.id);
    
    const newDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
    setDropdowns(newDropdowns);
    
    setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), { 'State': defaultStateId }));
  }, [title, attributeBasedDropdowns]);

 

  useEffect(() => {
   //by default set a state for which graph is shown
    setSelectedFilters({ ...initialFilters, 'State': defaultStateId });
  }, [filterOptions,selectedAttribute]);


//show avaialable filters in the dropdown which are not used or selected till now
  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);



  useEffect(() => {
    // Fetch districts for the default state ID
    if (filterOptions && filterOptions.districts) {
      const filteredDistricts = filterOptions.districts.filter(district => district.state_id === defaultStateId);
      setDistrictOptions([{ id: 'All', name: 'All' }, ...mapDistricts(filteredDistricts)]);
    }
  }, [filterOptions]);

  //handle attribute change function
  const handleAttributeChange = (event, value) => {
    console.log(value);
  setSelectedAttribute(value.id);
  const newDropdowns = attributeBasedDropdowns[value.id] ? attributeBasedDropdowns[value.id].slice(0, 3) : [];
  setDropdowns(newDropdowns);
  
  let newFilters = {};
  newDropdowns.forEach((dropdown) => {
    if (dropdown === 'State') {
      newFilters[dropdown] = defaultStateId;
    } else {
      newFilters[dropdown] = 'All';
    }
  });
  newFilters['State'] = defaultStateId;
  setSelectedFilters(newFilters);
  onFilterChange(value.id, newFilters, cardKey);
  };

//add more dropdown function
  const handleAddDropdown = (event, value) => {
    if (value) {
      setDropdowns((prev) => [...prev, value]);
      setSelectedFilters((prev) => ({ ...prev, [value]: 'All' }));
      setShowAddMore(true);
    }
  };
  

   //select values for dropdowns that will be visible
  const getValueFromList = (list, value, key) => {
    
    if (key === 'School Management' || key === 'Board of Education') {
      if (typeof value === 'object') {
        return list.find(item => item.name === value.name) || null;
      }
      return list.find(item => item.name === value) || null;
    } else {
      if (typeof value === 'object') {
        return list.find(item => item.id === value.id) || null;
      }
      return list.find(item => item.id === value) || list.find(item => item === value) || null;
    }
  };

     //filter change function
  const handleFilterChange = (dropdownLabel) => (event, value) => {
    
    let selectedValue = value;
    let newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
  
    if (dropdownLabel === 'State') {
      selectedValue = value && value.id ? value.id : null;
      console.log(selectedValue)
      newFilters = { 
        ...selectedFilters, 
        [dropdownLabel]: selectedValue, 
         'District': 'All' 
      };
  
      
      const filteredDistricts = filterOptions.districts.filter(district => district.state_id === selectedValue);
      setDistrictOptions([{ id: 'All', name: 'All' }, ...mapDistricts(filteredDistricts)]);
  
      
    } else if (dropdownLabel === 'District' || dropdownLabel === 'Social Group' || dropdownLabel === 'School Location' || dropdownLabel === 'Gender' || dropdownLabel === 'CWSN' || dropdownLabel === 'Qualification'|| dropdownLabel === 'Mode of Employment') {
      selectedValue = value && value.id ? value.id : null;
      newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
    }
    else if (dropdownLabel === 'School Management' ||  dropdownLabel === 'Board of Education') {
      selectedValue = value && value.name ? value.name : null;
      newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
    }
    setSelectedFilters(newFilters);
    onFilterChange(selectedAttribute, newFilters,cardKey);
  };

   //date range change function
  const handleDateRangeChange = (dateRangeName, startDate, endDate) => {
    let newFilters = {};
    const formattedStartDate = startDate ? dayjs(startDate).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : null;
    const formattedEndDate = endDate ? dayjs(endDate).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : null;
 

    switch (dateRangeName) {
      case 'dateRange1':
        newFilters = {
          ...selectedFilters,
          startDateRange1: formattedStartDate,
          endDateRange1: formattedEndDate
        };
        setDateRange1Start(startDate);
        setDateRange1End(endDate);
        break;
      
      default:
        break;
    }
    setSelectedFilters(newFilters);
    onFilterChange(selectedAttribute, newFilters,cardKey);
  };

  const viewDetailsPage = ()=> {
    

    const params = {
      dropdownOptions,
      filterOptions,
      attributeBasedDropdowns: attributeBasedDropdowns[selectedAttribute],
      selectedFilters,
      selectedAttribute,
      dateRange1Start: dateRange1Start,
      dateRange1End: dateRange1End,
      dateRange2Start: null,
      dateRange2End: null,
      apiEndPoints,
      cardMapping,
      cardKey,
      category
    };

    // Store the params in localStorage or sessionStorage
    localStorage.setItem('viewDetailsData', JSON.stringify(params));

    // Open the new tab
    window.open('/viewDetailsPage', '_blank');
  }
  
  //set the line chart at its proper position
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
        {`${title.value} (REGION VS PAN INDIA)`}
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
  getOptionLabel={(option) => typeof option === 'object' ? option.name : option}
 value={getValueFromList(attributeOptions[dropdownLabel], selectedFilters[dropdownLabel],dropdownLabel)}
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

        <Grid container columnSpacing={0.5} marginTop={0.5}>
          <Grid item xs={12} sm={3} md={3} lg={3}>
            <Typography variant="h6"  mb={4}>Date Range </Typography>
           </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange1Start}
                    onChange={(newValue) => handleDateRangeChange('dateRange1', newValue, dateRange1End)}
                    maxDate={dateRange1End}
                    renderInput={(params) => <TextField {...params} size="small"  />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange1End}
                    onChange={(newValue) => handleDateRangeChange('dateRange1', dateRange1Start, newValue)}
                    minDate={dateRange1Start}
                    renderInput={(params) => <TextField {...params} size="small"  />}
                  />
                </LocalizationProvider>
              </Grid>
              {loadingStatus ?(
              <Grid item xs={12} sm={5} md={5} lg={5} >
            <Button  variant='contained' sx={{m:0}} disabled={true} onClick={()=> viewDetailsPage()}>View in Detail</Button>
            </Grid>
             ):(
              <>
              {dataAvailableStatus ?(
                ""
              ):(

              
              <Grid item xs={12} sm={5} md={5} lg={5} >
              <Button  variant='contained' sx={{m:0}}  onClick={()=> viewDetailsPage()}>View in Detail</Button>
              </Grid>
            )}
            </>
             )}
           
          </Grid>

        
      </CardContent>
      {loadingStatus ?(
        <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
        <CircularProgress />
      </Box>
      ):(
        <>
        {dataAvailableStatus ?(
 <Typography variant="body1" color="error">No data available for the chart.</Typography>
        ):(
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
     )}
     </>
      )}
    </Card>
 );
}

export default CardFourComponent;
