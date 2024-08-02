import React, { useState, useEffect,useRef } from "react";
import { useSelector } from "react-redux";
import axios from '../../../utils/axios';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from 'dayjs';
import { Chart } from "react-chartjs-2";
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
import { getCsvDataRows, getCsvHeaders } from './CsvExport';
import excelExport from './ExcelExport';
import pdfExport from './PdfExport';

const defaultChartDataCard4 = {
  labels: [],
  datasets: [
    {
      label: '',
      type: 'bar',
      backgroundColor: 'rgba(185,102,220,1)',
      borderColor: 'rgba(185,102,220,1)',
      borderWidth: 2,
      data: [],
      barThickness: 30,
      borderRadius: 5,
      order: 2,
    },
    {
      label: 'No of Students (Pan India)',
      type: 'bar',
      backgroundColor: 'rgba(68,198,212,1)',
      borderColor: 'rgba(68,198,212,1)',
      borderWidth: 2,
      borderRadius: 5,
      data: [],
      barThickness: 30,
      order: 2,
    },
    {
      label: '',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average score (Pan India)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
  ],
};



const ViewDetailsComponentCard4 = () => {
  const [data, setData] = useState(null);
  const chartRef = useRef(null);
  const [selectedAttribute, setSelectedAttribute] = useState('');
  const [dataAvailableChart,setDataAvailableChart] = useState(false);
  const [tableData, setTableData] = useState([])
  const [chartData, setChartData] = useState({})
  const [loading,setLoading] = useState(false);
  const [districtOptions, setDistrictOptions] = useState([]);
  const [selectedFilters, setSelectedFilters] = useState({});
  const [updatedFilters, setUpdatedFilters] = useState({});
  const [showAddMore, setShowAddMore] = useState(true);
  const [defaultStateName, setDefaultStateName] = useState([]);
  
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
  apiEndPoints,
 cardMapping,
 cardKey,
 category,
 subtype,
 tableHeadings,
 attributeHeading
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
  console.log(value)
  const stateValue = value ? ((value.State && value.State !== "All") ? value.State :  7): 7
  const stateName = filterOptions ? (filterOptions.states ? (filterOptions.states.find(state => state.state_id === stateValue).state_name) : ""):"";
  setDefaultStateName(stateName)
  console.log(defaultStateName)
    
setLoading(true)
  try {
    let payload = {}
    if(category == "Students" || category == "Parents"){
    payload = {
      transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
      transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') :  null) :  null,
      transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') :  null) : null,
      grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
      subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
      schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
      stateId: value ? ((value.State && value.State !== "All") ? value.State : 7) : 7,
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
  else if(category == "Teachers"){
   payload = {
    transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1StartValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
    transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]')) : dateRange1EndValue.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]'),
    transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') :  null) :  null,
    transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]') :  null) : null,
      grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
      stateId: value ? ((value.State && value.State !== "All") ? value.State :  7) :  7,
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
   
   
    if(category == "Teachers" || category == "Parents"){
      if(apiEndPoints != undefined){
      const endpoint = apiEndPoints[selectedAttribute.id];
    const res = await axios.post(endpoint, payload);
    if(res.data.status && res.data.statusCode == 200){
      if(res.data.result.dataStateOne.length == 0 && res.data.result.dataNation.length == 0){
        setDataAvailableChart(true)
        setLoading(false);
      }
      else{
        setDataAvailableChart(false)
        setLoading(false);
        const result = res.data.result;
          const { key: labelKey, dataOneKey, dataTwoKey,dataThreeKey, avgKey1,avgKey2 } = cardMapping[selectedAttribute.id];
        const allLabels = new Set([
          ...result.dataStateOne.map(item => item[labelKey]),
          ...result.dataNation.map(item => item[labelKey])
        ]);
       
        const labelsData = Array.from(allLabels);
      const dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
      const dataOneStudent = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
      const dataOneAvg = labelsData.map(label => {
        const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0;
        return parseFloat(value.toFixed(2));
      });
    
      const dataTwo = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
      const dataTwoStudent = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
      const dataTwoAvg = labelsData.map(label => {
        const value = result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0;
        return parseFloat(value.toFixed(2));
      });
        const newTableData = labelsData.map(label => ({
          attributes: label,
          dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
          dateRange1StudentValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
          dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
          dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
          dateRange2StudentValue: result.dataNation.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
          dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
        }));
  
        setChartData({
        
            labels: labelsData,
            datasets: createDatasetsCard4(dataOne, dataTwo, dataOneAvg, dataTwoAvg,dataOneStudent,dataTwoStudent,stateName),
          
         });
        setTableData(newTableData)
      }
     
    }
      
    }else{
        console.log("error")
    }

    }
    else if(category == "Students" && subtype == "r1" && (selectedAttribute.id == 5 || selectedAttribute.id == 6 || selectedAttribute.id == 7 || selectedAttribute.id == 8 || selectedAttribute.id == 14)){
      if(apiEndPoints != undefined){
      const endpoint = apiEndPoints[selectedAttribute.id];
      const res = await axios.post(endpoint, payload);
      if(res.data.status && res.data.statusCode == 200){
        if(res.data.result.dataStateOne.length == 0){
            setDataAvailableChart(true)
            setLoading(false);
        }
        else{
          setDataAvailableChart(false)
          setLoading(false);
        const result = res.data.result;
        
          const { key: labelKey, dataOneKey, dataTwoKey, avgKey1,avgKey2 } = cardMapping[selectedAttribute.id];
        const allLabels = new Set([
          ...result.dataStateOne.map(item => item[labelKey]),
        ]);
      
      const labelsData = Array.from(allLabels);
      const dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
      const dataOneAvg = labelsData.map(label => {
        const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0;
        return parseFloat(value.toFixed(2));
      }); 
      const dataTwo = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
     const dataTwoAvg = labelsData.map(label => {
      const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey2] || 0;
      return parseFloat(value.toFixed(2));
    });
      const newTableData = labelsData.map(label => ({
        attributes: label,
        attributes: label,
      dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
      dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
      dateRange2TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
      dateRange2AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));
      setChartData({
      
        labels: labelsData,
        datasets: createDatasetsCard5(dataOne, dataTwo, dataOneAvg, dataTwoAvg,stateName),
      
     });
      setTableData(newTableData)

        }
        
      }
          
       
      }else{
        console.log("error")
      }
  

    }else{
      console.log(apiEndPoints)
      if(apiEndPoints != undefined){

      const endpoint = apiEndPoints[selectedAttribute.id];
      const res = await axios.post(endpoint, payload);
      if(res.data.status && res.data.statusCode == 200){
        if(res.data.result.dataStateOne.length == 0 && res.data.result.dataNation.length == 0){
          setDataAvailableChart(true)
          setLoading(false);

        }
        else{
          setLoading(false);
        const result = res.data.result;
          const { key: labelKey, dataOneKey, dataTwoKey, avgKey1,avgKey2 } = cardMapping[selectedAttribute.id];
      const allLabels = new Set([
        ...result.dataStateOne.map(item => item[labelKey]),
        ...result.dataNation.map(item => item[labelKey])
      ]);
      const labelsData = Array.from(allLabels);
       const dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
      const dataOneAvg = labelsData.map(label => {
        const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0;
        return parseFloat(value.toFixed(2));
      }); 
      const dataTwo = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
     const dataTwoAvg = labelsData.map(label => {
      const value = result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0;
      return parseFloat(value.toFixed(2));
    });
      const newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));
      setChartData({
      
        labels: labelsData,
        datasets: createDatasetsCard5(dataOne, dataTwo, dataOneAvg, dataTwoAvg,stateName),
      
     });
      setTableData(newTableData)

        }
        
      }  
        
      }else{
        console.log("error")
      }
  

    }

    
   
 
  } catch (error) {
    console.error('Error fetching data:', error);
  }
};

const createDatasetsCard4 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,dataOneStudent,dataTwoStudent,stateName) => [
  { ...defaultChartDataCard4.datasets[0], data: dataOne || [],label: `No of ${category} (${stateName})`,dataStudent:dataOneStudent || [] },
    { ...defaultChartDataCard4.datasets[1], data: dataTwo || [],dataStudent:dataTwoStudent || []},
    { ...defaultChartDataCard4.datasets[2], data: dataOneAvg || [],label: `Average score (${stateName})` },
    { ...defaultChartDataCard4.datasets[3], data: dataTwoAvg || [] },
  ];

const createDatasetsCard5 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,stateName) => [
    { ...defaultChartDataCard4.datasets[0], data: dataOne || [],label: `No of ${category} (${stateName})` },
      { ...defaultChartDataCard4.datasets[1], data: dataTwo || []},
      { ...defaultChartDataCard4.datasets[2], data: dataOneAvg || [],label: `Average score (${stateName})` },
      { ...defaultChartDataCard4.datasets[3], data: dataTwoAvg || [] },
    ];

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

const selectedFiltersWithNames = Object.fromEntries(
  Object.entries(selectedFilters).map(([key, value]) => {
    const options = attributeOptions[key];
    const selectedOption = options.find(option => (typeof option === 'object' ? option.id === value : option === value));
    return [key, typeof selectedOption === 'object' ? selectedOption.name : selectedOption];
  })
);

const exportAsPDF = ()=> {
  pdfExport(selectedAttribute, selectedFilters, attributeOptions, tableData, tableHeadings, category, dateRange1StartValue,dateRange1EndValue,dateRange2StartValue,dateRange2EndValue,attributeHeading,cardKey)
  setAnchorEl(null);
}

const exportAsExcel = () => {
  excelExport(selectedAttribute, selectedFilters, attributeOptions, tableData, tableHeadings, category,dateRange1StartValue,dateRange1EndValue,dateRange2StartValue,dateRange2EndValue,attributeHeading,cardKey)
  setAnchorEl(null);
  
};



const exportAsCSV = () => {
  // Trigger the CSV download
  csvLinkRef.current.link.click();
  setAnchorEl(null);
};

const headers = getCsvHeaders(selectedAttribute,category,cardKey);
const dataRows = getCsvDataRows(selectedAttribute,selectedFilters,attributeOptions,category,tableData,attributeHeading,dateRange1StartValue,dateRange1EndValue,dateRange2StartValue,dateRange2EndValue,cardKey);







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
  
  {(loading && tableData.length == 0) ?(
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
<Typography variant="h5" color= "#0948a6" mt={4} gutterBottom><u>Chart Data:</u></Typography>
{loading ?(
    <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
    <CircularProgress />
  </Box>

):(
  <>
  {dataAvailableChart? (
    <Typography variant="body1" color="error">No data available for the chart.</Typography>

  ):(
    <div style={{ overflowX: "auto", marginTop: "1rem" }}>
    <div style={{ minWidth: "800px", minHeight: "400px" }}>
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
                              if(context.dataset.dataStudent){
                                dataPoint = context.dataset.dataStudent[context.dataIndex]
                                const customValue1 = dataPoint;
                                label +=`${context.parsed.y}, No of Students: ${customValue1}`;
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
      {/* <Chart type="bar" data={chartData} options={{ responsive: true }} /> */}
    </div>
  </div>

  )}
  </>
 

)}
    
      <Typography variant="h5" color= "#0948a6" mt={4} gutterBottom><u>Table Data:</u></Typography>
      {loading ?(
        <Box sx={{ display: "flex", alignItems:'center', justifyContent: "center", width:'100%',pb:2,mt:2 }}>
        <CircularProgress />
      </Box>
      ):(
        <>
        {dataAvailableChart? (
          <Typography variant="body1" color="error">No data available for the table.</Typography>
        ):(
          <>
           {(category == "Teachers" || category=="Parents") ? (
 <TableContainer component={Paper}>
 <Table sx={{ minWidth: 650, mt: 2 }} aria-label="simple table">
   <TableHead sx={{ backgroundColor: '#f0f0f0' }}>
     <TableRow>
       
         <TableCell className="TableHeading" rowSpan={2}>
         <p className="HeadingData">{attributeHeading}</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">{defaultStateName}</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={3}>
         <p className="HeadingData">Pan india</p>
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
          
         <TableCell className="TableHeading" rowSpan={2}>
         <p className="HeadingData">{attributeHeading}</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={2}>
         <p className="HeadingData">{defaultStateName}</p>
       </TableCell>
       <TableCell className="TableHeading" colSpan={2}>
         <p className="HeadingData">Pan india</p>
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
        
         
     </>
      )}
      </CardContent>
    </Card>
  );
};

export default ViewDetailsComponentCard4;

