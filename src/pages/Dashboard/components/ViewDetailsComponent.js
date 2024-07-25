import React, { useState, useEffect,useRef } from "react";
import { useSelector } from "react-redux";
import axios from '../../../utils/axios';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from 'dayjs';
import {
  Typography,
  Table,
  Autocomplete,
  TextField,
  IconButton,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Card,
  Grid,
  CardContent,
  Button,
  Box,
  Menu, MenuItem
} from "@mui/material";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CircularProgress from '@mui/material/CircularProgress';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import * as XLSX from 'xlsx';
import { CSVLink } from "react-csv";

//table headings


const ViewDetailsComponent = () => {
  const [data, setData] = useState(null);
  const [selectedAttribute, setSelectedAttribute] = useState('');
  const [tableData, setTableData] = useState([])
  const [loading,setLoading] = useState(false);
  const [districtOptions, setDistrictOptions] = useState([]);
  const [selectedFilters, setSelectedFilters] = useState({});
  const [updatedFilters, setUpdatedFilters] = useState({});
  const [showAddMore, setShowAddMore] = useState(true);
  const [availableFilters, setAvailableFilters] = useState([]);
  const [anchorEl, setAnchorEl] = useState(null);
  const csvLinkRef = useRef(null);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  
 // Destructuring data only if it's not null
 const {
  dropdownOptions,
  filterOptions,
  attributeBasedDropdowns,
  selectedFiltersList,
  selectedAttributeId,
  dateRange1Start,
  dateRange1End,
  dateRange2Start,
  dateRange2End,
  apiEndPoints,
 cardMapping,
 cardKey,
 category,
 tableHeadings
} = data || {};

const [dateRange1StartValue, setDateRange1StartValue] = useState(null);
  const [dateRange1EndValue, setDateRange1EndValue] = useState(null);
  const [dateRange2StartValue, setDateRange2StartValue] = useState(null);
  const [dateRange2EndValue, setDateRange2EndValue] = useState(null);


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
      setDateRange1StartValue(startDate);
      setDateRange1EndValue(endDate);
      break;
    case 'dateRange2':
      newFilters = {
        ...selectedFilters,
        startDateRange2: formattedStartDate,
        endDateRange2: formattedEndDate
      };
      setDateRange2StartValue(startDate);
      setDateRange2EndValue(endDate);
      break;
    default:
      break;
  }
  setUpdatedFilters(newFilters);
  
};


const fetchData = async (value) => {
    
setLoading(true)
  try {
    let payload = {}
    if(category == "student" || category == "parent"){
    payload = {
      transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : (cardKey==4)? null: dateRange2StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : (cardKey==4)? null: dateRange2StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : (cardKey==4)? null: dateRange2EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : (cardKey==4)? null: dateRange2EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
      subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
      schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
      stateId: value ? ((value.State && value.State !== "All") ? value.State :  null) : null,
      districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
      socialGroup: value ? ((value['Social Group'] && value['Social Group'] !== 'All') ? value['Social Group'] : null) : null,
      gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
      ageFrom: null,
      ageTo: null,
      educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
      schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
      cwsn: value ? ((value['CWSN'] && value['CWSN'] !== 'All') ? value['CWSN'] : null) : null,
      childMotherQualification: value ? ((value['Mother Education'] && value['Mother Education'] !== 'All') ? value['Mother Education'] : null) : null,
      childFatherQualification: value ? ((value['Father Education'] && value['Father Education'] !== 'All') ? value['Father Education'] : null) : null,
      householdId: value ? ((value['Annual Income'] && value['Annual Income'] !== 'All') ? value['Annual Income'] : null) : null,
    };
  }
  else if(category == "teacher"){
   payload = {
    transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
    transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
    transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : (cardKey==4)? null: dateRange2StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : (cardKey==4)? null: dateRange2StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
    transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : (cardKey==4)? null: dateRange2EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : (cardKey==4)? null: dateRange2EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
      stateId: value ? ((value.State && value.State !== "All") ? value.State : (cardKey==4)? 7: null) : (cardKey==4)? 7: null,
      districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
      qualification: value ? ((value.Qualification && value.Qualification !== 'All') ? value.Qualification : null) : null,
      employmentNature: value ? ((value['Mode of Employment'] && value['Mode of Employment'] !== 'All') ? value['Mode of Employment'] : null) : null,
      gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
      educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
      schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
      
    };
  }
    if (value && value['Age Group'] && value['Age Group'] !== 'All') {
      const ageRange = value['Age Group'].split('-');
      payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
      payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
    }
   
   console.log(payload)
   const endpoint = apiEndPoints[selectedAttribute.id];
    const res = await axios.post(endpoint, payload);
    if(res.data.status && res.data.statusCode == 200){
      setLoading(false);
    const result = res.data.result;
    console.log(result)
    console.log(cardKey)
    if(cardKey == 4){
      if(category == "teacher" || category == "parent"){
        const { key: labelKey, dataOneKey, dataTwoKey,dataThreeKey, avgKey1,avgKey2 } = cardMapping[selectedAttribute.id];
      const allLabels = new Set([
        ...result.dataStateOne.map(item => item[labelKey]),
        ...result.dataNation.map(item => item[labelKey])
      ]);
      const labelsData = Array.from(allLabels);
      const newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1StudentValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2StudentValue: result.dataNation.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));
      setTableData(newTableData)
      }
      else{
        const { key: labelKey, dataOneKey, dataTwoKey, avgKey1,avgKey2 } = cardMapping[selectedAttribute.id];
      const allLabels = new Set([
        ...result.dataStateOne.map(item => item[labelKey]),
        ...result.dataNation.map(item => item[labelKey])
      ]);
      const labelsData = Array.from(allLabels);
      const newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));
      setTableData(newTableData)

      }
      
    }
    else{
      if(category == "teacher" || category == "parent"){
        const { key: labelKey, dataOneKey, dataTwoKey,dataThreeKey, avgKey } = cardMapping[selectedAttribute.id];
      
      const allLabels = new Set([
        ...result.dataStateOne.map(item => item[labelKey]),
        ...result.dataStateTwo.map(item => item[labelKey])
      ]);
      
      const labelsData = Array.from(allLabels);
      
  
      const newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1StudentValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2StudentValue: result.dataStateTwo.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataStateTwo.find(item => item[labelKey] === label)?.[avgKey] || 0).toFixed(2)),
      }));
     
        setTableData(newTableData)

      }
      else{
        const { key: labelKey, dataOneKey, dataTwoKey, avgKey } = cardMapping[selectedAttribute.id];
      
      const allLabels = new Set([
        ...result.dataStateOne.map(item => item[labelKey]),
        ...result.dataStateTwo.map(item => item[labelKey])
      ]);
      
      const labelsData = Array.from(allLabels);
      
  
      const newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataStateTwo.find(item => item[labelKey] === label)?.[avgKey] || 0).toFixed(2)),
      }));
     
        setTableData(newTableData)

      }
     
      
    }
   
  }
  else{
    console.log("error")
  }
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};



useEffect(() => {
  // Retrieve the params from localStorage
  const storedData = localStorage.getItem('viewDetailsData');
  if (storedData) {
    const parsedData = JSON.parse(storedData);
    setData(parsedData);
    console.log(parsedData)
    if(parsedData){
      setDateRange1StartValue(parsedData.dateRange1Start ? dayjs(parsedData.dateRange1Start) : null);
      setDateRange1EndValue(parsedData.dateRange1End ? dayjs(parsedData.dateRange1End) : null);
      setDateRange2StartValue(parsedData.dateRange2Start ? dayjs(parsedData.dateRange2Start) : null);
      setDateRange2EndValue(parsedData.dateRange2End ? dayjs(parsedData.dateRange2End) : null);
  
    }
    
    // Find the selected attribute name
    if (parsedData.dropdownOptions) {
      const selectedAttr = parsedData.dropdownOptions.find(option => option.id === parsedData.selectedAttribute);
      console.log(selectedAttr)
      if (selectedAttr) {
        setSelectedAttribute(selectedAttr);
      }
    }
    // Set the initial selected filters
    setSelectedFilters(parsedData.selectedFilters || {});
    setUpdatedFilters(parsedData.selectedFilters || {});
  }
}, []);



useEffect(() => {
  if (updatedFilters) {
    fetchData(selectedFilters);
  }
}, [updatedFilters]);



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
  "Qualification": filterOptions ? (filterOptions.qualificationTeachers ? [{ id: 'All', name: 'All' }, ...mapQualification(filterOptions.qualificationTeachers)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
  "Mode of Employment": filterOptions ? (filterOptions.modeOfEmploymentTeacher ? [{ id: 'All', name: 'All' }, ...mapEmployment(filterOptions.modeOfEmploymentTeacher)] : [{ id: 'All', name: 'All' }]) : [{ id: 'All', name: 'All' }],
  
};


  

  

  if (!data) {
    return <div>No data available</div>;
  }
  


const existingFilterKeys = Object.keys(selectedFilters);
const availableFilterKeys = attributeBasedDropdowns.filter(filterName => !existingFilterKeys.includes(filterName));



  
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

    
  } else if (dropdownLabel === 'District' || dropdownLabel === 'Social Group' || dropdownLabel === 'School Location' || dropdownLabel === 'Gender' || dropdownLabel === 'CWSN' || dropdownLabel === 'Qualification'|| dropdownLabel === 'Mode of Employment' ) {
    selectedValue = value && value.id ? value.id : null;
    newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
  }
  else if (dropdownLabel === 'School Management' ||  dropdownLabel === 'Board of Education') {
    selectedValue = value && value.name ? value.name : null;
    newFilters = { ...selectedFilters, [dropdownLabel]: selectedValue };
  }
 
  setSelectedFilters(newFilters);
  setUpdatedFilters(newFilters);
 

};



  const handleAddDropdown = (event, value) => {
    if (value) {
      setSelectedFilters((prevFilters) => ({
        ...prevFilters,
        [value]: 'All'
      }));
      setShowAddMore(true);
    }
  };

   //select values for dropdowns that will be visible
   const getValueFromList = (list, value, key) => {
    console.log(list)
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
console.log(tableData)

const exportAsPDF = () => {
  const doc = new jsPDF();

  // Define the table head with center alignment
  let head = [];
  let body = [];
  if(category == "teacher" || category == "parent"){
    if(cardKey == 4){
      head = [
        [
          { content: 'Attributes', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'State', colSpan: 3, styles: { halign: 'center' } },
          { content: 'Pan India', colSpan: 3, styles: { halign: 'center' } }
        ],
        tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
      ];
      
  
    }
    else{
      head = [
        [
          { content: 'Attributes', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'Date Range 1', colSpan: 3, styles: { halign: 'center' } },
          { content: 'Date Range 2', colSpan: 3, styles: { halign: 'center' } }
        ],
        tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
      ];
  
    }
    body = tableData.map(row => [
      { content: row.attributes, styles: { halign: 'center' } },
      { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
      { content: row.dateRange1StudentValue, styles: { halign: 'center' } },
      { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
      { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
      { content: row.dateRange2StudentValue, styles: { halign: 'center' } },
      { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
    ]);

  }else{
    if(cardKey == 4){
      head = [
        [
          { content: 'Attributes', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'State', colSpan: 2, styles: { halign: 'center' } },
          { content: 'Pan India', colSpan: 2, styles: { halign: 'center' } }
        ],
        tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
      ];
      
  
    }
    else{
      head = [
        [
          { content: 'Attributes', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'Date Range 1', colSpan: 2, styles: { halign: 'center' } },
          { content: 'Date Range 2', colSpan: 2, styles: { halign: 'center' } }
        ],
        tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
      ];

     
  
    }
    body = tableData.map(row => [
      { content: row.attributes, styles: { halign: 'center' } },
      { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
      { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
      { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
      { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
    ]);

  }
  
  doc.autoTable({
    head: head,
    body: body,
    startY: 20,
    theme: 'grid',
    headStyles: {
      halign: 'center', 
      fillColor: [255, 255, 255],
      textColor: [0, 0, 0], 
      lineWidth: 0.2, 
      lineColor: [0, 0, 0], 
    },
    bodyStyles: {
      halign: 'center', 
      lineWidth: 0.2, 
      lineColor: [0, 0, 0], 
    },
    tableLineWidth: 0.2, 
    tableLineColor: [0, 0, 0], 
  });

  // Save the PDF
  doc.save(`${selectedAttribute.value}.pdf`);
};

const exportAsExcel = () => {
  // Define the main headings and sub-headings based on tableHeadings
  if(category == "teacher" || category == "parent"){
    let headerData = [];
 if(cardKey == 4){
  headerData = [
    [
      { v: 'Attributes', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ],
    [
      { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[4], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[5], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
    ]
  ];

 }
 else{
  headerData = [
    [
      { v: 'Attributes', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 1', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 1', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 1', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 2', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 2', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        { v: 'Date Range 2', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ],
    [
      { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[4], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[5], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
    ]
  ];

 }

   

  // Create a worksheet from the header data
  const ws = XLSX.utils.aoa_to_sheet(headerData);

  // Add cell merges to the worksheet
  ws['!merges'] = [
    { s: { r: 0, c: 1 }, e: { r: 0, c: 3 } }, // Merge cells for "Date Range 1"
      { s: { r: 0, c: 4 }, e: { r: 0, c: 6 } }  // Merge cells for "Date Range 2"
  ];

  // Append data rows to the worksheet
  const dataRows = tableData.map(row => [
    row.attributes,
    row.dateRange1TotalValue,
    row.dateRange1StudentValue,
    row.dateRange1AvgValue,
    row.dateRange2TotalValue,
    row.dateRange2StudentValue,
    row.dateRange2AvgValue
  ]);

  // Append the data rows to the worksheet
  XLSX.utils.sheet_add_aoa(ws, dataRows, { origin: -1 });

  // Create a new workbook and append the worksheet
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Data');

  // Write the workbook to a file
  XLSX.writeFile(wb, `${selectedAttribute.value}.xlsx`);

  }else{

    let headerData = [];
 if(cardKey == 4){
  headerData = [
    [
      { v: 'Attributes', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ],
    [
      { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ]
  ];

 }
 else{
  headerData = [
    [
      { v: 'Attributes', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Date Range 1', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Date Range 1', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Date Range 2', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: 'Date Range 2', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ],
    [
      { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
      { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
    ]
  ];

 }

   

  // Create a worksheet from the header data
  const ws = XLSX.utils.aoa_to_sheet(headerData);

  // Add cell merges to the worksheet
  ws['!merges'] = [
    { s: { r: 0, c: 1 }, e: { r: 0, c: 2 } }, // Merge cells for "Date Range 1"
    { s: { r: 0, c: 3 }, e: { r: 0, c: 4 } }  // Merge cells for "Date Range 2"
  ];

  // Append data rows to the worksheet
  const dataRows = tableData.map(row => [
    row.attributes,
    row.dateRange1TotalValue,
    row.dateRange1AvgValue,
    row.dateRange2TotalValue,
    row.dateRange2AvgValue
  ]);

  // Append the data rows to the worksheet
  XLSX.utils.sheet_add_aoa(ws, dataRows, { origin: -1 });

  // Create a new workbook and append the worksheet
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, 'Data');

  // Write the workbook to a file
  XLSX.writeFile(wb, `${selectedAttribute.value}.xlsx`);

  }
 
};



const exportAsCSV = () => {
  // Trigger the CSV download
  csvLinkRef.current.link.click();
};

// Define header rows as a single array of objects
let headers = [];
if(category == "teacher" || category == "parent"){
  if(cardKey == 4){
    headers = [
      { label: 'Attributes', key: 'attributes' },
      { label: 'State Stakeholder Value', key: 'dateRange1TotalValue' },
      { label: 'State Student Value', key: 'dateRange1StudentValue' },
      { label: 'State Avg Value', key: 'dateRange1AvgValue' },
      { label: 'Pan India Stakeholder Value', key: 'dateRange2TotalValue' },
      { label: 'Pan India Student Value', key: 'dateRange2StudentValue' },
      { label: 'Pan India Avg Value', key: 'dateRange2AvgValue' }
      ];
  
  }
  else{
    headers = [
      { label: 'Attributes', key: 'attributes' },
      { label: 'Date Range 1 Total Stakeholder Value', key: 'dateRange1TotalValue' },
      { label: 'Date Range 1 Total Students Value', key: 'dateRange1StudentValue' },
      { label: 'Date Range 1 Avg Students Value', key: 'dateRange1AvgValue' },
      { label: 'Date Range 2 Total Stakeholders Value', key: 'dateRange2TotalValue' },
      { label: 'Date Range 2 Total Students Value', key: 'dateRange2StudentValue' },
      { label: 'Date Range 2 Avg Value', key: 'dateRange2AvgValue' }
      ];
  
  }
}
else{
  if(cardKey == 4){
    headers = [
      { label: 'Attributes', key: 'attributes' },
      { label: 'State Total Value', key: 'dateRange1TotalValue' },
      { label: 'State Avg Value', key: 'dateRange1AvgValue' },
      { label: 'Pan India Total Value', key: 'dateRange2TotalValue' },
      { label: 'Pan India Avg Value', key: 'dateRange2AvgValue' }
      ];
  
  }
  else{
    headers = [
      { label: 'Attributes', key: 'attributes' },
      { label: 'Date Range 1 Total Value', key: 'dateRange1TotalValue' },
      { label: 'Date Range 1 Avg Value', key: 'dateRange1AvgValue' },
      { label: 'Date Range 2 Total Value', key: 'dateRange2TotalValue' },
      { label: 'Date Range 2 Avg Value', key: 'dateRange2AvgValue' }
      ];
  
  }

}



// Define data rows
let dataRows = []
if(tableData.length != 0 || tableData != undefined ){
  if(category == "teacher" || category == "parent"){
    dataRows = tableData.map(row => ({
      attributes: row.attributes,
      dateRange1TotalValue: row.dateRange1TotalValue,
      dateRange1StudentValue: row.dateRange1StudentValue,
      dateRange1AvgValue: row.dateRange1AvgValue,
      dateRange2TotalValue: row.dateRange2TotalValue,
      dateRange2StudentValue: row.dateRange2StudentValue,
      dateRange2AvgValue: row.dateRange2AvgValue
    }));

  }
  else{
    dataRows = tableData.map(row => ({
      attributes: row.attributes,
      dateRange1TotalValue: row.dateRange1TotalValue,
      dateRange1AvgValue: row.dateRange1AvgValue,
      dateRange2TotalValue: row.dateRange2TotalValue,
      dateRange2AvgValue: row.dateRange2AvgValue
    }));
  }
 

}

  return (
    <Card sx={{margin:"20px 30px",borderRadius:5,padding:0,boxShadow:10}}>
      <CardContent>
      <Grid container rowSpacing={1} columnSpacing={1}>
      <Grid item xs={12} sm={12} md={12} lg={12} sx={{ backgroundColor: '#0948a6', padding: '8px', display: "flex", justifyContent: "space-between", alignItems: "center", borderRadius: 2 }}>
  <Typography 
    variant="h4" 
    sx={{ color: '#fff' }}
  >
    {selectedAttribute.value}
  </Typography>
  
  {loading ?(
                <>
              
              <Button variant="contained" disabled={true} sx={{ backgroundColor: "white", color: "#2899DB", m: 0 }}>
    Export
  </Button>
            </>
             ):(
            
             

              <>
              
             
              <Button  variant='contained' sx={{ backgroundColor: "white", color: "#2899DB", m: 0 }} onClick={handleClick}>Export</Button>
              <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
        <MenuItem onClick={exportAsPDF}>Export as PDF</MenuItem>
        <MenuItem onClick={exportAsExcel}>Export as Excel</MenuItem>
        
          <MenuItem onClick={exportAsCSV}>Export as CSV</MenuItem>
        
      </Menu>
      <CSVLink
  data={dataRows}
  headers={headers}
  filename={`${selectedAttribute.value}.csv`}
  ref={csvLinkRef}
  style={{ display: 'none' }}
/>
             
            
          
            </>
             )}
</Grid>

<Grid container spacing={2} mb={2} mt={2}>
          <Grid item xs={12} sm={3} md={3} lg={3}>
            <Typography variant="body1"  mt={1}><b>Date Range 1:</b></Typography>
            </Grid>
              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange1StartValue}
                     onChange={(newValue) => handleDateRangeChange('dateRange1', newValue, dateRange1EndValue)}
                    maxDate={dateRange1EndValue}
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
                    value={dateRange1EndValue}
                    onChange={(newValue) => handleDateRangeChange('dateRange1', dateRange1StartValue, newValue)}
                    minDate={dateRange1StartValue}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
           
          
{(cardKey != 4) &&
<>
          <Grid item xs={12} sm={3} md={3} lg={3} >
            <Typography variant="body1"  mt={1}><b>Date Range 2:</b></Typography>
            </Grid>

              <Grid item xs={12} sm={4.5} md={4.5} lg={4.5}>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                  <DatePicker
                    label="Start Date"
                     format="DD/MM/YYYY"
                     slotProps={{ textField: { size: "small" } }}
                    value={dateRange2StartValue}
                    onChange={(newValue) => handleDateRangeChange('dateRange2', newValue, dateRange2EndValue)}
                    maxDate={dateRange2EndValue}
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
                    value={dateRange2EndValue}
                    onChange={(newValue) => handleDateRangeChange('dateRange2', dateRange2StartValue, newValue)}
                    minDate={dateRange2StartValue}
                    renderInput={(params) => <TextField {...params} size="small" fullWidth />}
                  />
                </LocalizationProvider>
              </Grid>
              </>       
}
           
        </Grid>
      
      </Grid>

      <Grid container spacing={2}>
  {existingFilterKeys.map((dropdownLabel, index) => (
    <Grid item xs={12} sm={4} key={index}>
      <Autocomplete
        options={attributeOptions[dropdownLabel] || []}
        getOptionLabel={(option) => typeof option === 'object' ? option.name : option}
        value={getValueFromList(attributeOptions[dropdownLabel], selectedFilters[dropdownLabel], dropdownLabel)}
        onChange={handleFilterChange(dropdownLabel)}
        renderInput={(params) => <TextField {...params} label={`${dropdownLabel}`} size="small" />}
      />
    </Grid>
  ))}
   {availableFilterKeys && showAddMore && (
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
    <Grid item xs={12} sm={4}>
      <Autocomplete
        options={availableFilterKeys}
        getOptionLabel={(option) => attributeOptions[option]?.[0]?.value || option}
        onChange={handleAddDropdown}
        renderInput={(params) => <TextField {...params} label="Add Filter" size="small" />}
      />
    </Grid>
  )}
</Grid>

    
      <Typography variant="h5" color= "#0948a6" mt={4} gutterBottom><u>Table Data:</u></Typography>
      {loading ?(
        <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
        <CircularProgress />
      </Box>
      ):(
        <>
          {(category == "teacher" || category=="parent") ? (
 <TableContainer component={Paper}>
 <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
   <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
     <TableRow>
       {cardKey == 4? (
         <>
         <TableCell className="TableHeading" rowSpan={2}>
         <p className="HeadingData">Attributes</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">State</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">Pan india</p>
       </TableCell>
         </>

       ):(
         <>
         <TableCell className="TableHeading" rowSpan={2}>
         <p className="HeadingData">Attributes</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">Date Range 1</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">Date Range 2</p>
       </TableCell>
       </>
       )}
       
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
          ):(
            <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
              <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
                <TableRow>
                  {cardKey == 4? (
                    <>
                    <TableCell className="TableHeading" rowSpan={2}>
                    <p className="HeadingData">Attributes</p>
                  </TableCell>
                  <TableCell className="TableHeading" colSpan={2}>
                    <p className="HeadingData">State</p>
                  </TableCell>
                  <TableCell className="TableHeading" colSpan={2}>
                    <p className="HeadingData">Pan india</p>
                  </TableCell>
                    </>
    
                  ):(
                    <>
                    <TableCell className="TableHeading" rowSpan={2}>
                    <p className="HeadingData">Attributes</p>
                  </TableCell>
                  <TableCell className="TableHeading" colSpan={2}>
                    <p className="HeadingData">Date Range 1</p>
                  </TableCell>
                  <TableCell className="TableHeading" colSpan={2}>
                    <p className="HeadingData">Date Range 2</p>
                  </TableCell>
                  </>
                  )}
                  
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
          )}
     </>
      )}
      </CardContent>
    </Card>
  );
};

export default ViewDetailsComponent;
