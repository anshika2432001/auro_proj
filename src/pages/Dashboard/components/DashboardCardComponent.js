import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton,Box } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart } from 'react-chartjs-2';
import axios from '../../../utils/axios';
import dayjs from 'dayjs';
import { useDispatch, useSelector } from "react-redux";

import AddCircleIcon from '@mui/icons-material/AddCircle';
import CircularProgress from '@mui/material/CircularProgress';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';




function DashboardCardComponent({ title, attributeBasedDropdowns, chartData,onFilterChange,cardKey,loadingStatus }) {

const filterOptions = useSelector((state) => state.filterDropdown.data.result);
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
const [selectedFilters, setSelectedFilters] = useState(initialFilters);
const [districtOptions, setDistrictOptions] = useState([]);



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

const mapDistricts = (districts) => {
  return districts.map(districtObj => ({
    id: districtObj.district_id,
    name: districtObj.district_name,
    state_id: districtObj.state_id
  }));
};

 //filter dropdown options
const attributeOptions = {
  "State": filterOptions ? (filterOptions.states ? [{ id: 'All', name: 'All' }, ...mapStateNames(filterOptions.states)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
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
  "Qualification": ['All', 'Below Secondary', 'Secondary','Higher Secondary','Graduate','Post Graduate','M.Phil','Ph.D.','Post-Doctoral'],
  "Mode of Employment": ['All', 'Regular', 'Contract','Part-Time/Guest'],
  

};

  // Update selectedAttribute when titleId changes
useEffect(() => {
  
  setSelectedAttribute(title.id); 
}, [title.id]);

//updated selectedAttribute and filters based on title and attributeBasedDropdowns
useEffect(() => {
   setSelectedAttribute(title.id);
  const newDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
  setDropdowns(newDropdowns);
  setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
}, [title, attributeBasedDropdowns]);

//show avaialable filters in the dropdown which are not used or selected till now
useEffect(() => {
  const usedFilters = new Set(dropdowns);
  setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
}, [dropdowns]);


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
   
    if(value != null){
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
  }
  else{
    if (key === 'School Management' || key === 'Board of Education') {
      if (typeof value === 'object') {
        return list.find(item => item.name === "All") || null;
      }
      return list.find(item => item.name === "All") || null;
    } else {
      if (typeof value === 'object') {
        return list.find(item => item.id === "All") || null;
      }
      return list.find(item => item.id === "All") || list.find(item => item === "All") || null;
    }
  
  }
  };
 //filter change function
const handleFilterChange = (dropdownLabel) => (event, value) => {

  let selectedValue = value;
  let newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };

  if (dropdownLabel === 'State') {
    selectedValue = value && value.id ? value.id : null;
    newFilters = { 
      ...selectedFilters, 
      [dropdownLabel]: selectedValue, 
       'District': 'All' 
    };

    
    const filteredDistricts = filterOptions.districts.filter(district => district.state_id === selectedValue);
    setDistrictOptions([{ id: 'All', name: 'All' }, ...mapDistricts(filteredDistricts)]);

    
  } else if (dropdownLabel === 'District' || dropdownLabel === 'Social Group' || dropdownLabel === 'School Location' || dropdownLabel === 'Gender' || dropdownLabel === 'CWSN') {
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
           
          </Grid>

        
      </CardContent>
      
      {loadingStatus ? (
  <Box sx={{ display: "flex", alignItems: 'center', justifyContent: "center", width: '100%', pb: 2, mt: 2 }}>
    <CircularProgress />
  </Box>
) : (
  chartData && chartData.length > 0 ? (
    <div style={{ width: '100%', height: 600,padding:2 }}>
          <ResponsiveContainer>
      <BarChart
        data={chartData}
        layout="vertical"
        margin={{ top: 15, right: 20, bottom: 7 }}
      >
        <CartesianGrid strokeDasharray="0" />
        <XAxis
          type="number"
          tick={{ fontSize: 12 }}
          interval={0}
        //   domain={[0, 'dataMax']}
        domain={chartData.length === 1 ? ['dataMin - 1', 'dataMax + 1'] : [0, 'dataMax']}
          label={{
            value: `${title.value}`,
            position: 'insideBottom',
            offset: -5,
            style: { textAnchor: 'middle' },
            x: '50%',
          }}
        />
        <YAxis
          type="category"
          dataKey="state"
          width={160}
          tick={{ fontSize: 12, textAnchor: 'end' }}
          interval={0}
          label={{
            value: 'States',
            angle: -90,
            position: 'insideLeft',
            style: { textAnchor: 'middle' },
          }}
          
        />
        <Tooltip
      cursor={{ fill: 'transparent' }}
      formatter={(value, name) => {
        const formattedName = name === 'num_teachers' ? 'Teachers'
          : name === 'num_students' ? 'Students'
          : name === 'num_schools' ? 'Schools'
          : name === 'num_parents' ? 'Parents'
          : name;
        return [value, formattedName];
      }}
    />
        <Bar 
          dataKey={
            title.value === 'Number of Students' ? 'num_students' :
            title.value === 'Number of Schools' ? 'num_schools' :
            title.value === 'Number of Teachers' ? 'num_teachers' :
            title.value === 'Number of Parents' ? 'num_parents' :
            ''
          } 
          isAnimationActive={false}
        >
          {chartData.map((entry, index) => (
            <Bar key={`bar-${index}`} fill={entry.fill} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
    </div>
  ) : (
    <Typography variant="body1" color="error">No data available for the chart.</Typography>
  )
)}

    </Card>
);
}

export default DashboardCardComponent;
