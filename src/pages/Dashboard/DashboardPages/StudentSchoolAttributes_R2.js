import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import CardFourComponent from '../components/CardFourComponent';
import TableComponent from '../components/TableComponent';
import { useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import {  useSelector } from "react-redux";

//attribute dropdown options
const dropdownOptions = [
  { id: 1, value: 'Pre-Primary School' },
  { id: 2, value: 'Student Strength of the Classroom' },
  { id: 3, value: 'Types of Student Clubs' },
  { id: 4, value: 'Academic Stream' },
  { id: 5, value: 'Student with access to Bank Account/ UPI' },
  { id: 6, value: 'Students engagement in extra curricular activities in school' },
  { id: 7, value: 'Children whose first or home language is the same as that in the school ' },
  { id: 8, value: 'Social Group' },
  { id: 9, value: 'No. of Students in leadership positions in school clubs in the school ' },
];

//filters based on attribute id
const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

//api endpoints
const endpointMapping = {
  
  2: '/r2/student-strength-classroom-stats',
  4: '/r2/academic-streams-stats',
  5: '/r2/student-access-bank-stats',
  6: '/r2/student-engagement-activities-school-stats',
  8: '/r2/social-group-stats',
  9: '/r2/student-leadership-position-school-clubs-stats',
};

const tableEndPoints = {
  2: '/r2/student-strength-classroom-table',
  4: '/r2/academic-streams-table',
  5: '/r2/student-access-bank-table',
  6: '/r2/student-engagement-activities-school-table',
  8: '/r2/social-group-table',
  9: '/r2/student-leadership-position-school-clubs-table',
}

//default chart data for first 3 cards
const defaultChartData = {
  labels: [],
  datasets: [
    {
      label: 'No of Students (Purple)',
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
      label: 'No of Students (Blue)',
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
      label: 'Average score (Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average score (Blue)',
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

//default chart data for region vs panIndia card(fourth card)
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


//fetch table details
const tableHeadings = [
  
  'Number of Students', 
  'Average Score of Students',
  'Number of Students', 
  'Average Score of Students'
];



const StudentSchoolAttributes_R2 = () => {
  const titleId = useParams();
   const filterOptions = useSelector((state) => state.filterDropdown.data.result);
   const [cardMapping,setCardMapping] = useState('');
   const [card4Mapping,setCard4Mapping]= useState('');
  
  const defaultDateRange1Start = dayjs('2024-01-01');
  const defaultDateRange1End = dayjs('2024-01-31');
  const defaultDateRange2Start = dayjs('2024-03-01');
  const defaultDateRange2End = dayjs('2024-03-31');
  let defaultStartDateRange1= defaultDateRange1Start.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultEndDateRange1= defaultDateRange1End.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultStartDateRange2= defaultDateRange2Start.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultEndDateRange2= defaultDateRange2End.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');

  const [loadingChart,setLoadingChart]=useState({
    0: false,
    1: false,
    2: false,
    3: false,
    4: false,
  });

  const [loadingTable,setLoadingTable]=useState({
    0: false,
    1: false,
    2: false,
    3: false,
    4: false,
  });

  const [dataAvailable,setDataAvailable]=useState({
    0: false,
    1: false,
    2: false,
    3: false,
    4: false,
  });
  const [attributeNameValue, setAttributeNameValue] = useState({
    0: "",
    1: "",
    2: "",
    3: "",
    4: "",
    
  });

  const [filters, setFilters] = useState({
    1: {},
    2: {},
    3: {},
    4: {},
  });
  const [cardData, setCardData] = useState({
    1: defaultChartData,
    2: defaultChartData,
    3: defaultChartData,
    4: defaultChartData,
  });

  const [cardTitle, setCardTitle] = useState({
    1: {},
    2: dropdownOptions[1],
    3: dropdownOptions[2],
    4: dropdownOptions[3],
  });

  const [tableData, setTableData] = useState({
    0: [],
    1: [],
    2: [],
    3: [],
    4: [],
});
  

useEffect(() => {
  fetchData(2, filters[2],2);
  fetchTableInfo(2, filters[2],2)
  fetchData(3, filters[3],3);
  fetchTableInfo(3, filters[3],3)
  fetchData(4, filters[4],4);
  
  
}, []);

   useEffect(() => {
    
    if(titleId.id && [1, 2, 3, 4].includes(Number(titleId.id))){
     
    fetchData(1, filters[1],1);
    fetchTableInfo(1, filters[1],1)
    setCardTitle(prevCardTitle => ({
      ...prevCardTitle,
      1: dropdownOptions[0]
    }));
    }
    else{
     
      setCardTitle(prevCardTitle => ({
        ...prevCardTitle,
        1: dropdownOptions[Number(titleId.id)-1]
         
      }));
      fetchData(titleId.id, filters[titleId.id],1);
       fetchTableInfo(titleId.id, filters[titleId.id],1);
    }
    
     fetchTableData(); 
   
    
  }, [titleId.id]);

  //fetch table details
  const fetchTableData = () => {
    const firstOption = dropdownOptions[Number(titleId.id)-1];
      fetchTableInfo(firstOption.id, filters[firstOption.id], 0);
    
    
  };

  const fetchTableInfo = async (key, value,cardKey) => {
    setLoadingTable(prevValue => ({
      ...prevValue,
      [cardKey]: true,
    }));

    
        const endpoint = tableEndPoints[key];
      
      

    try {
      let payload = {
        transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1 : defaultStartDateRange1) : defaultStartDateRange1,
        transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1 : defaultEndDateRange1) : defaultEndDateRange1,
        transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2 : (cardKey==4)? null: defaultStartDateRange2) : (cardKey==4)? null: defaultStartDateRange2,
        transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2 : (cardKey==4)? null: defaultEndDateRange2) : defaultEndDateRange2,
        grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
        subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
        schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
        stateId: value ? ((value.State && value.State !== "All") ? value.State : (cardKey==4)? 7: null) : (cardKey==4)? 7: null,
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

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }
     
      
      const res = await axios.post(endpoint, payload);
       
       if(res.data.status && res.data.statusCode == 200){
        setLoadingTable(prevValue => ({
          ...prevValue,
          [cardKey]: false,
        }));
        
          const result = res.data.result;
          
          if(cardKey != 4){
              const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,attributeName] = parseResultData(key,"table",cardKey,result);
              
        setTableData(prevData => ({
          ...prevData,
          [cardKey]: newTableData,
        }));
        setAttributeNameValue(prevData => ({
          ...prevData,
          [cardKey]: attributeName,
        }));
       
      }
      
    }
    else{
      console.log("error")
      setLoadingTable(prevValue => ({
        ...prevValue,
        [cardKey]: false,
      }));
      setDataAvailable(prevValue => ({
        ...prevValue,
        [cardKey]: true,
      }));
      
    }
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoadingTable(prevValue => ({
        ...prevValue,
        [cardKey]: false,
      }));
      setDataAvailable(prevValue => ({
        ...prevValue,
        [cardKey]: true,
      }));
    }
  };

    //fetch card details function
  const fetchData = async (key, value,cardKey) => {
    setLoadingChart(prevValue => ({
      ...prevValue,
      [cardKey]: true,
    }));

        const endpoint = endpointMapping[key];
      

    try {
      let payload = {
        transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1 : defaultStartDateRange1) : defaultStartDateRange1,
        transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1 : defaultEndDateRange1) : defaultEndDateRange1,
        transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2 : (cardKey==4)? null: defaultStartDateRange2) : (cardKey==4)? null: defaultStartDateRange2,
        transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2 : (cardKey==4)? null: defaultEndDateRange2) : defaultEndDateRange2,
        grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
        subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
        schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
        stateId: value ? ((value.State && value.State !== "All") ? value.State : (cardKey==4)? 7: null) : (cardKey==4)? 7: null,
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

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }
     
      
      const res = await axios.post(endpoint, payload);
       
       if(res.data.status && res.data.statusCode == 200){
        setLoadingChart(prevValue => ({
          ...prevValue,
          [cardKey]: false,
        }));
        setDataAvailable(prevValue => ({
          ...prevValue,
          [cardKey]: false,
        }));
          const result = res.data.result;
          if(cardKey == 4){
           
               const stateValue = value ? ((value.State && value.State !== "All") ? value.State :  7): 7
               const defaultStateName = filterOptions ? (filterOptions.states ? (filterOptions.states.find(state => state.state_id === stateValue).state_name) : ""):"";
              
              const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,attributeName] = parseResultDataCard4(key,cardKey, result);
             
              setCardData(prevCardData => ({
                ...prevCardData,
                [cardKey]: {
                  labels: labelsData,
                  datasets: createDatasetsCard4(dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName),
                }
               }));
               
               setTableData(prevData => ({
                ...prevData,
                [cardKey]: newTableData,
              }));
           
              setAttributeNameValue(prevData => ({
                ...prevData,
                [cardKey]: attributeName,
              }));
               
          }
          else{
              const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,attributeName] = parseResultData(key,"card",cardKey,result);
              setCardData(prevCardData => ({
              ...prevCardData,
              [cardKey]: {
              labels: labelsData,
              datasets: createDatasets(dataOne, dataTwo, dataOneAvg, dataTwoAvg),
          }
        }));
       
        setAttributeNameValue(prevData => ({
          ...prevData,
          [cardKey]: attributeName,
        }));
       
      }
      
    }
    else{
      console.log("error")
      setLoadingChart(prevValue => ({
        ...prevValue,
        [cardKey]: false,
      }));
      setDataAvailable(prevValue => ({
        ...prevValue,
        [cardKey]: true,
      }));
      
    }
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoadingChart(prevValue => ({
        ...prevValue,
        [cardKey]: false,
      }));
      setDataAvailable(prevValue => ({
        ...prevValue,
        [cardKey]: true,
      }));
    }
  };

//parse data for first three cards based on key values
  const parseResultData = (key,type,cardKey, result) => {
    const mappings = {
      2: { key: 'classroom_strength', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Classroom Strength' },
      4: { key: 'academic_stream', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Academic Stream' },
      5: { key: 'student_access_to_bank', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Student Access To Bank' },
      6: { key: 'extra_curricular_activity', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Extra Curricular Activity' },
      8: { key: 'social_group', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Social Group' },
      9: { key: 'is_student_in_leadership_position', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score',attributeName:'Leadership Position' },
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey,attributeName } = mappings[key];
    setCardMapping(mappings)
    let labelsData = [];
    let dataOne = [];
    let dataOneAvg = [];
    let dataTwo = [];
    let dataTwoAvg = [];
    let newTableData = [];
    if(type == "card"){
      
        if(result.dataStateOne.length == 0 && result.dataStateTwo.length == 0){
          setDataAvailable(prevValue => ({
            ...prevValue,
            [cardKey]: true,
          }));
        }
        else{
          setDataAvailable(prevValue => ({
            ...prevValue,
            [cardKey]: false,
          }));
        
         

        const allLabels = new Set([
          ...result.dataStateOne.map(item => item[labelKey]),
          ...result.dataStateTwo.map(item => item[labelKey])
        ]);
      
       
    
         labelsData = Array.from(allLabels);
         dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
         dataOneAvg = labelsData.map(label => {
          const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0;
          return parseFloat(value.toFixed(2));
        });
      
         dataTwo = labelsData.map(label => result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
         dataTwoAvg = labelsData.map(label => {
          const value = result.dataStateTwo.find(item => item[labelKey] === label)?.[avgKey] || 0;
          return parseFloat(value.toFixed(2));
        });


      }
     
    }
    
     
if(type == "table"){
  if(result.dataStateOne.length == 0){
    setDataAvailable(prevValue => ({
      ...prevValue,
      [cardKey]: true,
    }));

  }
  else{
  setDataAvailable(prevValue => ({
    ...prevValue,
    [cardKey]: false,
  }));
  const labelValue = mappings[key].key

     result.dataStateOne.map(value=>{
            newTableData.push({
              stateDataValue: value.state_name,
              districtDataValue: value.district_name,
              attributes: value[labelValue],
              dateRange1TotalValue: value.num_students_date1? value.num_students_date1: 0,
              dateRange1AvgValue: value.average_score_date1? (parseFloat((value.average_score_date1).toFixed(2))) : '0',
              dateRange2TotalValue: value.num_students_date2? value.num_students_date2: '0',
              dateRange2AvgValue: value.average_score_date2 ? (parseFloat((value.average_score_date2).toFixed(2))) : '0',

        })
      })
 
}
 
    }
   
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,attributeName];
  };

   //parse region vs pan India card details
  const parseResultDataCard4 = (key,cardKey, result) => {
    const mappings = {
      2: { key: 'classroom_strength', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Classroom Strength' },
      4: { key: 'academic_stream', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Academic Stream' },
      5: { key: 'student_access_to_bank', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Student Access To Bank' },
      6: { key: 'extra_curricular_activity', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Extra Curricular Activity' },
      8: { key: 'social_group', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Social Group' },
      9: { key: 'is_student_in_leadership_position', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'avg_score',avgKey2:'avg_score',attributeName:'Leadership Position' },
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey1,avgKey2,attributeName } = mappings[key];
    setCard4Mapping(mappings)
    let labelsData = [];
    let dataOne = [];
    let dataOneAvg = [];
    let dataTwo = [];
    let dataTwoAvg = [];
    let newTableData = [];
    
    
   
      if(result.dataStateOne.length == 0  && result.dataNation.length == 0){
        setDataAvailable(prevValue => ({
          ...prevValue,
          [cardKey]: true,
        }));
      }
      else{

        setDataAvailable(prevValue => ({
          ...prevValue,
          [cardKey]: false,
        }));

        const allLabels = new Set([
          ...result.dataStateOne.map(item => item[labelKey]),
          ...result.dataNation.map(item => item[labelKey])
        ]);
      
       labelsData = Array.from(allLabels);
         dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
        dataOneAvg = labelsData.map(label => {
          const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0;
          return parseFloat(value.toFixed(2));
        }); 
         dataTwo = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
       dataTwoAvg = labelsData.map(label => {
        const value = result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0;
        return parseFloat(value.toFixed(2));
      });
       newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));

      }
  
    
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg,newTableData,attributeName];
  };

  //datasets creation for first three cards based on default chart data
  const createDatasets = (dataOne, dataTwo, dataOneAvg, dataTwoAvg) => [
    { ...defaultChartData.datasets[0], data: dataOne },
    { ...defaultChartData.datasets[1], data: dataTwo },
    { ...defaultChartData.datasets[2], data: dataOneAvg },
    { ...defaultChartData.datasets[3], data: dataTwoAvg },
  ];

  //datasets creation for region vs panIndia card(4TH CARD) based on default chart data
  const createDatasetsCard4 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName) => [
    { ...defaultChartDataCard4.datasets[0], data: dataOne || [],label: `No of Students (${defaultStateName})`, },
    { ...defaultChartDataCard4.datasets[1], data: dataTwo || []},
    { ...defaultChartDataCard4.datasets[2], data: dataOneAvg || [],label: `Average score (${defaultStateName})` },
    { ...defaultChartDataCard4.datasets[3], data: dataTwoAvg || [] },
  ];

  //callback function when filter or attribute is changed
  const onFilterChange = (key, value,cardKey) => {
   
    setFilters(prevFilters => ({
      ...prevFilters,
      [key]: value,
    }));
    const selectedOption = dropdownOptions.find(option => option.id === key);

    setCardTitle(prevData => ({
      ...prevData,
      [cardKey]: selectedOption,
    }));
    if(cardKey == 4){
      fetchData(key, value,cardKey);
      
    }
    else if(cardKey == 0){
      fetchTableInfo(key, value,cardKey);
    }
    else{
      fetchData(key, value,cardKey);
      fetchTableInfo(key, value,cardKey);
    }
     
  };



    return (
    <div>
      <h2>Student R2 Attributes</h2>
      <Grid container spacing={2}>
      {dropdownOptions.slice(0, 4).map((option, index) => (
        option.id != 4 ? (
          <Grid item xs={12} sm={6} md={6} lg={6} key={option.id}>
            <CardComponent 
             key={option.id}
             title={cardTitle[option.id]} 
             dropdownOptions={dropdownOptions} 
             attributeBasedDropdowns={attributeBasedDropdowns} 
             chartData={cardData[option.id] || defaultChartData}
             onFilterChange={onFilterChange}
             cardKey={option.id}
             loadingStatusChart={loadingChart[option.id]}
             loadingStatusTable={loadingTable[option.id]}
             apiEndPoints={endpointMapping}
             apiEndPointsTable={tableEndPoints}
             cardMapping={cardMapping}
             dataAvailableStatus={dataAvailable[option.id]}
             category="Students"
             subtype = "r2"
             tableInfo={tableData[option.id]} 
             tableHeadings={tableHeadings} 
             attributeHeading={attributeNameValue[option.id]}
              
            />
          </Grid>
         ):(
              <Grid item xs={12} sm={6} md={6} lg={6} key={option.id}>
            <CardFourComponent 
              key={option.id}
              title={cardTitle[option.id]} 
              dropdownOptions={dropdownOptions} 
              attributeBasedDropdowns={attributeBasedDropdowns} 
              chartData={cardData[option.id] || defaultChartData}
              onFilterChange={onFilterChange}
              cardKey={option.id}
              loadingStatusChart={loadingChart[option.id]}
              loadingStatusTable={loadingTable[option.id]}
              apiEndPoints={endpointMapping}
              cardMapping={card4Mapping}
              apiEndPointsTable={tableEndPoints}
              dataAvailableStatus={dataAvailable[option.id]}
              category="Students"
              subtype = "r2"
              tableInfo={tableData[option.id]} 
             tableHeadings={tableHeadings} 
             attributeHeading={attributeNameValue[option.id]}
              
            />
          </Grid>
         )
        ))}
       
        <Grid item xs={12}>
          <TableComponent 
          titleId={titleId.id}
          dropdownOptions={dropdownOptions} 
          attributeBasedDropdowns={attributeBasedDropdowns} 
          tableInfo={tableData[0]} 
          tableHeadings={tableHeadings} 
          onFilterChange={onFilterChange}
          tableKey={0}
          loadingStatusTable={loadingTable[0]}
          dataAvailableStatus={dataAvailable[0]}
          category="Students"
          subtype = "r2"
          attributeHeading={attributeNameValue[0]}
          />
        </Grid>
      </Grid>
    </div>
  );
};
export default StudentSchoolAttributes_R2;
