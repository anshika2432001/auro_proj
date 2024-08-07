import React, { useState, useEffect,useRef } from 'react';
import { Card, CardContent, Grid, Typography, Autocomplete, TextField, Table,Box, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton,Button,Menu, MenuItem } from '@mui/material';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from 'dayjs';
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";

import AddCircleIcon from '@mui/icons-material/AddCircle';
import { useDispatch, useSelector } from "react-redux";
import "../../../App.css";
import CircularProgress from '@mui/material/CircularProgress';
import pdfExport from './PdfExport';
import excelExport from './ExcelExport';
import { getCsvDataRows, getCsvHeaders } from './CsvExport';
import { CSVLink } from "react-csv";
import axios from '../../../utils/axios';



function TableComponent({titleId, dropdownOptions, attributeBasedDropdowns,tableInfo,tableHeadings,onFilterChange,tableKey,loadingStatusTable,dataAvailableStatus,category,subtype,attributeHeading }) {
  console.log(titleId)
  console.log(dropdownOptions)
  
  const filterOptions = useSelector((state) => state.filterDropdown.data.result);
  
  const initialAttribute = dropdownOptions.length > 0 ? dropdownOptions[Number(titleId)-1].id : '';
  const [selectedAttribute, setSelectedAttribute] = useState(initialAttribute);
  const [title, setTitle] = useState({});
  const [dateRange1Start, setDateRange1Start] = useState(dayjs('2024-01-01'));
  const [dateRange1End, setDateRange1End] = useState(dayjs('2024-01-31'));
  const [dateRange2Start, setDateRange2Start] = useState(dayjs('2024-03-01'));
  const [dateRange2End, setDateRange2End] = useState(dayjs('2024-03-31'));
  const tableData = tableInfo; 
  const [dropdowns, setDropdowns] = useState(attributeBasedDropdowns[selectedAttribute] || []);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [quizNames, setQuizNames] = useState([]);
  const [showAddMore, setShowAddMore] = useState(true);
  const defaultSubject = "Maths";
  const defaultGrade = 11;
  const defaultQuizName = "All";
  const initializeFilters = (id) => {
    if ((id === 12 || id === 13) && subtype === 'r1') {
      return {
        Subject: defaultSubject,
        Grade: defaultGrade,
        'Quiz Names': defaultQuizName,
      };
    } else {
      return attributeBasedDropdowns[id]
        ? attributeBasedDropdowns[id].slice(0, 3).reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {})
        : {};
    }
  };

  const [selectedFilters, setSelectedFilters] = useState(initializeFilters(titleId));
  const [districtOptions, setDistrictOptions] = useState([]);
  const [anchorEl, setAnchorEl] = useState(null);
  console.log(selectedFilters)

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

const mapSchoolType = (schoolTypes) => {
  return schoolTypes.map(schoolTypeObj => schoolTypeObj.school_type);
};

const mapQuizNames = (quizNames) => {
  return quizNames.map(quizNamesObj => quizNamesObj.quiz_name);
};

//filter dropdowns
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


useEffect(() => {
  setSelectedAttribute(initialAttribute); // Update selectedAttribute when titleId changes
  let titleValue = dropdownOptions.find(option => option.id === initialAttribute)
  setTitle(titleValue)
  if(initialAttribute == 12 || initialAttribute == 13){
    handleQuizNames(initialAttribute)
  }
  setSelectedFilters(initializeFilters(selectedAttribute));
 
}, [titleId]);

  useEffect(() => {
    const newDropdowns = attributeBasedDropdowns ? attributeBasedDropdowns[selectedAttribute]?.slice(0, 3) : [];
    setDropdowns(newDropdowns);
    if(initialAttribute == 12 || initialAttribute == 13){
      handleQuizNames(initialAttribute)
    }
    setSelectedFilters(initializeFilters(selectedAttribute));
    // setSelectedFilters(newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {}));
  }, [attributeBasedDropdowns, selectedAttribute]);


 

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

  //attribute change function
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
      onFilterChange(value.id, updatedFilters, tableKey);
    } else {
      const newDropdowns = attributeBasedDropdowns[value.id] ? attributeBasedDropdowns[value.id].slice(0, 3) : [];
      setDropdowns(newDropdowns);
  
      const updatedFilters = newDropdowns.reduce((acc, curr) => ({ ...acc, [curr]: 'All' }), {});
      setSelectedFilters(updatedFilters);
      setSelectedAttribute(value.id);
      onFilterChange(value.id, updatedFilters, tableKey);
    }
  };

  console.log(quizNames)

  //add more dropdowns
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
  
    if(value != undefined && list != undefined){
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
    }
    else if (dropdownLabel === 'School Management' ||  dropdownLabel === 'Board of Education') {
      selectedValue = value && value.name ? value.name : null;
      newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
    }
    console.log(newFilters)
  
    setSelectedFilters(newFilters);
    onFilterChange(selectedAttribute, newFilters,tableKey);
  };


  //date range change function
  const handleDateRangeChange = (dateRangeName, startDate, endDate) => {
    const formattedStartDate = startDate ? dayjs(startDate).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : null;
    const formattedEndDate = endDate ? dayjs(endDate).format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : null;
    let newFilters = {};
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
    onFilterChange(selectedAttribute, newFilters,tableKey);
  };
  

  

  const csvLinkRef = useRef(null);

  const exportAsPDF = () => {

    pdfExport(title, selectedFilters, attributeOptions, tableInfo, tableHeadings, category,subtype, dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,attributeHeading,tableKey)
    setAnchorEl(null);
    
    };
  
    const exportAsExcel = () => {
      excelExport(title, selectedFilters, attributeOptions, tableInfo, tableHeadings, category,subtype,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,attributeHeading,tableKey)
      setAnchorEl(null);
      
    };
    
    
  
    const exportAsCSV = () => {
      csvLinkRef.current.link.click();
      setAnchorEl(null);
    };
  
  
  
  const headers = getCsvHeaders(title,category,subtype,tableKey);
  const dataRows = getCsvDataRows(title,selectedFilters,attributeOptions,category,subtype,tableInfo,attributeHeading,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,tableKey);

  return (
    <Card className='dashboard-card'>
     
        <Typography variant="h6" sx={{ backgroundColor: '#DBEDFF', padding: '8px', borderRadius: '4px', mb: 2,color: '#082f68',border: '1px solid #082f68'  }}>
          Table Format Details
        </Typography>
        <CardContent>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={4} md={4} lg={4}>
          <Autocomplete
          options={dropdownOptions}
          getOptionLabel={(option) => option.value}
          value={dropdownOptions.find(option => option.id === selectedAttribute)}
          onChange={handleAttributeChange}
          renderInput={(params) => <TextField {...params} label="Select Attribute" size="small" />}
         
        />
          </Grid>
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
        <Grid container spacing={1} sx={{ mt: 1, mb: 1 }}>
          
          <Grid item xs={12} sm={10} md={12} lg={12}>
            <Grid container spacing={2}>
              <Grid item xs={5} sm={5} md={5} lg={5}>
              <Grid container spacing={1}>
              <Grid item xs={12}>
                <Typography
                  variant="body1" color='#082f68' mb={1}
                
                >
                  <b>Date Range 1:</b>
                </Typography>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
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
                <Grid item xs={6} sm={6} md={6} lg={6}>
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
                </Grid>
              </Grid>
              <Grid item xs={5} sm={5} md={5} lg={5}>
              <Grid container spacing={1}>
              <Grid item xs={12}>
                <Typography
                  variant="body1" color='#082f68' mb={1}
                
                >
                  <b>Date Range 2:</b>
                </Typography>
                </Grid>
                <Grid item xs={6} sm={6} md={6} lg={6}>
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
                <Grid item xs={6} sm={6} md={6} lg={6}>
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
                </Grid>
              </Grid>
             
              {loadingStatusTable ?(
                <>
              
             <Grid item xs={12} sm={2} md={2} lg={2} >
            <Button  disabled={true} sx={{mt:4.5}}>Export</Button>
            </Grid> 
            </>
             ):(
              <>
              {dataAvailableStatus ?(
                ""
              ):(

              <>
              
              <Grid item xs={12} sm={2} md={2} lg={2} >
              <Button  variant='contained' sx={{mt:4.5,border:"1px solid #082f68",fontSize:"12px" ,fontWeight:"bold",color:"#082f68" ,backgroundColor: '#DBEDFF',fontFamily: "Inter"}} onClick={handleClick}>Export</Button>
              <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
        <MenuItem onClick={exportAsPDF}>Export as PDF</MenuItem>
        <MenuItem onClick={exportAsExcel}>Export as Excel</MenuItem>
        
          <MenuItem onClick={exportAsCSV}>Export as CSV</MenuItem>
        
      </Menu>
      <CSVLink
  data={dataRows}
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
          </Grid>
        </Grid>

        {loadingStatusTable ?(
        <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
        <CircularProgress />
      </Box>
      ):(
        <>
        {dataAvailableStatus ?(
 <Typography variant="body1" color="error">No data available for the table.</Typography>
        ):(
          <>
          {(category == "Teachers" || category=="Parents" || (category=="Students" && subtype =="r1" && (titleId == 12 || titleId == 13))) ? (
            <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
          <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
          <TableRow>
          <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">State</p>
            </TableCell>
            <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">District</p>
            </TableCell>
            <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">{attributeHeading}</p>
            </TableCell>
            <TableCell className="TableHeading" colSpan={3}>
              <p className="HeadingData">Date Range 1</p>
            </TableCell>
            <TableCell className="TableHeading" colSpan={3}>
              <p className="HeadingData">Date Range 2</p>
            </TableCell>
          </TableRow>
          <TableRow>
          {tableHeadings.map((heading, index) => (
                  <TableCell key={index} className="TableHeading">
                    <p className="HeadingData">{heading}</p>
                  </TableCell>
                ))}
          </TableRow>
        </TableHead>
            <TableBody>
              {tableData && tableData.map((row, index) => (
                <TableRow key={index}>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.stateDataValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.districtDataValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.attributes}</p>
                  </TableCell>
                  
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange1TotalValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange1StudentValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange1AvgValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange2TotalValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange2StudentValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange2AvgValue}</p>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
          ):
          (<>
            
              <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
          <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
          <TableRow>
            <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">State</p>
            </TableCell>
            <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">District</p>
            </TableCell>
            <TableCell className="TableHeading" rowSpan={2}>
              <p className="HeadingData">{attributeHeading}</p>
            </TableCell>
            <TableCell className="TableHeading" colSpan={2}>
              <p className="HeadingData">Date Range 1</p>
            </TableCell>
            <TableCell className="TableHeading" colSpan={2}>
              <p className="HeadingData">Date Range 2</p>
            </TableCell>
          </TableRow>
          <TableRow>
          {tableHeadings.map((heading, index) => (
                  <TableCell key={index} className="TableHeading">
                    <p className="HeadingData">{heading}</p>
                  </TableCell>
                ))}
          </TableRow>
        </TableHead>
            <TableBody>
              {tableData && tableData.map((row, index) => (
                <TableRow key={index}>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.stateDataValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.districtDataValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.attributes}</p>
                  </TableCell>
                  
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange1TotalValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange1AvgValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange2TotalValue}</p>
                  </TableCell>
                  <TableCell className="BodyBorder">
                    <p className="TableData">{row.dateRange2AvgValue}</p>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
            
              

           
</>
          )}
          </>

        )}
        </>
      )}
      </CardContent>
    </Card>
  );
}

export default TableComponent;
