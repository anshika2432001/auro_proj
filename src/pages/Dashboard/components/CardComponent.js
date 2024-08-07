import React, { useState, useEffect, useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, IconButton,Box,Button,Menu, MenuItem } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import { Chart } from 'react-chartjs-2';
import axios from '../../../utils/axios';
import dayjs from 'dayjs';
import { useNavigate } from "react-router-dom";
import CircularProgress from '@mui/material/CircularProgress';

import { useSelector } from "react-redux";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement } from 'chart.js';
import AddCircleIcon from '@mui/icons-material/AddCircle';

import { CSVLink } from "react-csv";
import pdfExport from './PdfExport';
import excelExport from './ExcelExport';
import { getCsvDataRows, getCsvHeaders } from './CsvExport';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, LineElement, PointElement);



function CardComponent({ title, dropdownOptions, attributeBasedDropdowns, chartData,onFilterChange,cardKey,loadingStatusChart,loadingStatusTable,apiEndPoints,apiEndPointsTable,cardMapping,dataAvailableStatus,category,subtype,tableInfo,tableHeadings,attributeHeading }) {
console.log(loadingStatusChart,loadingStatusTable)
console.log(chartData)
const chartWidth = chartData.labels.length <= 3 ? '400px' : '800px';
  const filterOptions = useSelector((state) => state.filterDropdown.data.result);
  const navigate = useNavigate();
  const csvLinkRef = useRef(null);
  const [selectedAttribute, setSelectedAttribute] = useState(title.id);
  const [quizNames, setQuizNames] = useState([]);
  const [dateRange1Start, setDateRange1Start] = useState(dayjs('2024-01-01'));
  const [dateRange1End, setDateRange1End] = useState(dayjs('2024-01-31'));
  const [dateRange2Start, setDateRange2Start] = useState(dayjs('2024-03-01'));
  const [dateRange2End, setDateRange2End] = useState(dayjs('2024-03-31'));
  const initialDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];

  const [dropdowns, setDropdowns] = useState(initialDropdowns);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const defaultSubject = "Maths";
  const defaultGrade = 11;
  const defaultQuizName = "All";

  const initializeFilters = (id) => {
    if ((id == 12 || id == 13) && subtype == "r1") {
      return {
        Subject: defaultSubject,
        Grade: defaultGrade,
        "Quiz Names": defaultQuizName,
      };
    }
    else{
      return attributeBasedDropdowns[id]
      ? attributeBasedDropdowns[id].slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
      : {};

    }

  
  };

  const initialFilters = initializeFilters(title.id);
  const [selectedFilters, setSelectedFilters] = useState(initialFilters);
  const [districtOptions, setDistrictOptions] = useState([]);
  const chartRef = useRef(null);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const mapGenders = (genders) => {
    
      return genders.map(genderObj => ({ id: genderObj.gender, name: genderObj.gender_name }));
    
  };
  const mapSchoolLocation = (locations) => {
    return locations.map(locationObj => ({ id: locationObj.school_location, name: locationObj.location_name }));
  };
  const mapSubjects = (subjects) => {
    return subjects.map(subjectObj => subjectObj.subject);
  };
  const mapSchoolType = (schoolTypes) => {
    return schoolTypes.map(schoolTypeObj => schoolTypeObj.school_type);
  };
  const mapAgeGroups = (ageGroups) => {
    return ageGroups.map(ageGroupObj => ageGroupObj.age_group);
  };
  const mapSocialGroup = (socialGroups) => {
    return socialGroups.map(socialGroupObj => ({ id: socialGroupObj.social_group, name: socialGroupObj.social_group_name }));
  };
  const mapQualification = (qualifications) => {
    return qualifications.map(qualificationObj => ({ id: qualificationObj.qualification, name: qualificationObj.qualification_name }));
  };
  const mapEmployment = (employments) => {
    return employments.map(employmentObj => ({ id: employmentObj.employment_nature, name: employmentObj.employment_nature_name }));
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
 
  const mapQuizNames = (quizNames) => {
    return quizNames.map(quizNamesObj => quizNamesObj.quiz_name);
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
    "School Type": filterOptions ? (filterOptions.schoolType ? ['All', ...mapSchoolType(filterOptions.schoolType)] : ['All']) : ['All'],
    "Qualification": filterOptions ? (filterOptions.qualificationTeachers ? [{ id: 'All', name: 'All' }, ...mapQualification(filterOptions.qualificationTeachers)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "Mode of Employment": filterOptions ? (filterOptions.modeOfEmploymentTeacher ? [{ id: 'All', name: 'All' }, ...mapEmployment(filterOptions.modeOfEmploymentTeacher)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
    "Quiz Names": quizNames ? quizNames: ['All'],
    
  };

  // Update selectedAttribute when titleId changes
  useEffect(() => {
    
    setSelectedAttribute(title.id); 
    const initialFilters = initializeFilters(title.id);
    setSelectedFilters(initialFilters);
    if(title.id == 12 || title.id == 13){
      handleQuizNames(title.id)
    }
   
  }, [title.id]);

 
 
//updated selectedAttribute and filters based on title and attributeBasedDropdowns
useEffect(() => {
  setSelectedAttribute(title.id);
  const newDropdowns = attributeBasedDropdowns[title.id] ? attributeBasedDropdowns[title.id].slice(0, 3) : [];
  setDropdowns(newDropdowns);

  const initialFilters = initializeFilters(title.id);
  setSelectedFilters(initialFilters);
}, [title, attributeBasedDropdowns]);


//show avaialable filters in the dropdown which are not used or selected till now
  useEffect(() => {
    const usedFilters = new Set(dropdowns);
    
    setAvailableFilters(Object.keys(attributeOptions).filter(option => !usedFilters.has(option)));
  }, [dropdowns]);

  const handleQuizNames= async (value)=> {
    try {
      const response = await axios.post("/fetch-topics", {
        subject: defaultSubject,
        grade: defaultGrade,
      });
      if(value == 12){
      const topicNames = response.data.result.topTopicNames;
      setQuizNames(['All', ...mapQuizNames(topicNames)]);
      }
      else if(value == 13){
        const topicNames = response.data.result.weakTopicNames;
      setQuizNames(['All', ...mapQuizNames(topicNames)]);

      }
    } catch (error) {
      console.error('Error fetching topics:', error);
    }

  }

  //handle attribute change function
  const handleAttributeChange = async (event, value) => {
    if ((value.id === 12 || value.id === 13) && subtype == "r1" ) {
      const newDropdowns = attributeBasedDropdowns[value.id] ? attributeBasedDropdowns[value.id].slice(0, 3) : [];
      setDropdowns(newDropdowns);
      
  
      const updatedFilters = {
        Subject: defaultSubject,
        Grade: defaultGrade,
        "Quiz Names": defaultQuizName,
      };
  
      handleQuizNames(value.id);
  
      setSelectedAttribute(value.id);
      setSelectedFilters(updatedFilters);
      onFilterChange(value.id, updatedFilters, cardKey);
    } else {
      const newDropdowns = attributeBasedDropdowns[value.id] ? attributeBasedDropdowns[value.id].slice(0, 3) : [];
      setDropdowns(newDropdowns);
  
      const updatedFilters = newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {});
      setSelectedFilters(updatedFilters);
      setSelectedAttribute(value.id);
      onFilterChange(value.id, updatedFilters, cardKey);
    }
  };
console.log(quizNames)

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
    console.log(list)
    console.log(value)
    console.log(key)
   
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
    } else if (dropdownLabel === 'District' || dropdownLabel === 'Social Group' || dropdownLabel === 'School Location' || dropdownLabel === 'Gender' || dropdownLabel === 'CWSN' || dropdownLabel === 'Qualification'|| dropdownLabel === 'Mode of Employment') {
      selectedValue = value && value.id ? value.id : null;
      newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
    } else if (dropdownLabel === 'School Management' || dropdownLabel === 'Board of Education') {
      selectedValue = value && value.name ? value.name : null;
      newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
    }
  
    setSelectedFilters(newFilters);
    onFilterChange(selectedAttribute, newFilters, cardKey);
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
      case 'dateRange2':
        newFilters = {
          ...selectedFilters,
          startDateRange2: formattedStartDate,
          endDateRange2: formattedEndDate
        };
        setDateRange2Start(startDate);
        setDateRange2End(endDate);
        break;
      default:
        break;
    }
    // setSelectedFilters(newFilters);
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
      dateRange2Start: dateRange2Start,
      dateRange2End: dateRange2End,
      apiEndPoints,
      apiEndPointsTable,
      cardMapping,
      cardKey,
      category,
      subtype,
      tableHeadings,
      attributeHeading,
      quizNames
      
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
  




const exportAsPDF = () => {

  pdfExport(title, selectedFilters, attributeOptions, tableInfo, tableHeadings, category,subtype,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,attributeHeading,cardKey)
  setAnchorEl(null);
  
  };

  const exportAsExcel = () => {
    excelExport(title, selectedFilters, attributeOptions, tableInfo, tableHeadings, category,subtype, dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,attributeHeading,cardKey)
    setAnchorEl(null);
    
  };
  
  

  const exportAsCSV = () => {
    csvLinkRef.current.link.click();
    setAnchorEl(null);
  };



const headers = getCsvHeaders(title,category,subtype,cardKey);
const dataRows = getCsvDataRows(title,selectedFilters,attributeOptions,category,subtype,tableInfo,attributeHeading,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,cardKey);

console.log(dataRows)
  return (
    <Card className='mini-card'>
      <Typography 
        variant="h6" 
        sx={{ backgroundColor: '#DBEDFF', padding: '8px', top: '0',
          zIndex: 10 , position:"sticky", color: '#082f68',borderBottom: '1px solid #082f68', }}
      >
        {title.value}
      </Typography>
      <CardContent sx={{padding:"12px",height: '500px', overflowY: 'scroll',}}>
        <Autocomplete
          options={dropdownOptions}
          getOptionLabel={(option) => option.value}
          value={dropdownOptions.find(option => option.id === selectedAttribute) || null}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
          sx={{ marginTop: 1,marginBottom: 2 }}
        />
        <Grid container rowSpacing={2} columnSpacing={1}>
  {dropdowns.map((dropdownLabel, index) => (
    <Grid item xs={12} sm={4} md={4} lg={4} key={index}>
      <Autocomplete
        options={attributeOptions[dropdownLabel] || []}
        getOptionLabel={(option) => typeof option === 'object' ? option.name : option}
        value={getValueFromList(attributeOptions[dropdownLabel], selectedFilters[dropdownLabel], dropdownLabel)}
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

        <Grid container columnSpacing={0.5} rowSpacing={2} marginTop={0.5}>
          <Grid item xs={12} sm={3} md={3} lg={3}>
            <Typography variant="body1" color='#082f68' mt={1}><b>Date Range 1:</b></Typography>
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
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
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
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
           
          

          <Grid item xs={12} sm={3} md={3} lg={3} >
            <Typography variant="body1" color='#082f68' mt={1}><b>Date Range 2:</b></Typography>
            </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange2Start}
                    onChange={(newValue) => handleDateRangeChange('dateRange2', newValue, dateRange2End)}
                    maxDate={dateRange2End}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="End Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange2End}
                    onChange={(newValue) => handleDateRangeChange('dateRange2', dateRange2Start, newValue)}
                    minDate={dateRange2Start}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
            
              {((loadingStatusTable || loadingStatusChart)) ?(
                <>
              <Grid item xs={12} sm={4} md={4} lg={4} >
            <Button   disabled={true} sx={{m:0}} onClick={()=> viewDetailsPage()}>View Table</Button>
            </Grid>
             <Grid item xs={12} sm={3} md={3} lg={3} >
            <Button   disabled={true} sx={{m:0}}>Export</Button>
            </Grid> 
            </>
             ):(
              <>
              {dataAvailableStatus ?(
                ""
              ):(

              <>
              <Grid item xs={12} sm={4} md={4} lg={4} >
              <Button  className='card-button'  onClick={()=> viewDetailsPage()}>View Table</Button>
              </Grid>
              <Grid item xs={12} sm={3} md={3} lg={3} >
              <Button  className='card-button'  onClick={handleClick}>Export</Button>
              <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
        <MenuItem onClick={exportAsPDF}>Export as PDF</MenuItem>
        <MenuItem onClick={exportAsExcel}>Export as Excel</MenuItem>
        
          <MenuItem onClick={exportAsCSV}>Export as CSV</MenuItem>
        
      </Menu>
      <CSVLink
 data={dataRows}
 newLineSeparator="\r\n"
  headers={headers}
  filename={`${title.value}.csv`}
  ref={csvLinkRef}
  style={{ display: 'none' }}
/>
              </Grid> 
              </>
            )}
            </>
             )}
            
        </Grid>
     
      {loadingStatusChart ?(
        <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
        <CircularProgress />
      </Box>
      ):(
        <>
        {dataAvailableStatus ?(
 <Typography variant="body1" color="error">No data available for the chart.</Typography>
        ):(
        <div style={{ overflowX: "auto", marginTop: "1rem", width: "100%", }}>
        <div style={{ minWidth: chartWidth,minHeight:"400px" }}>
          <Chart
            ref={chartRef}
            type="bar"
            data={chartData}
            options={{
              indexAxis: 'x', 
              responsive: true,
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
              },
              plugins: {
                  legend: {
                  display: true
                },
              
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                  label += ': ';
                              }
                              let dataPoint = "";
                              let dataPoint1 = "";
                              let dataPoint2 = "";
                              console.log(context.dataset)
                              if(context.dataset.dataStudent){
                                dataPoint = context.dataset.dataStudent[context.dataIndex]
                                const customValue1 = dataPoint;
                                label +=`${context.parsed.y}, No of Students: ${customValue1}`;
                                return label;
                              }
                              else if(context.dataset.dataAvg && context.dataset.dataImprovement){
                                dataPoint1 = context.dataset.dataImprovement[context.dataIndex]
                                dataPoint2 = context.dataset.dataAvg[context.dataIndex]
                                const customValue1 = dataPoint1;
                                const customValue2 = dataPoint2;
                                label +=`${context.parsed.y}, Number of Students: ${customValue1},Average Score: ${customValue2}`;
                                return label;

                              }
                              else{
                                label +=`${context.parsed.y}`
                                return label;
                              }
                             
                            
                           
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
      </CardContent>
    </Card>
 );
}

export default CardComponent;
