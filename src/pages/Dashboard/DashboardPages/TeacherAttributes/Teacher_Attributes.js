import React, { useEffect, useState } from 'react';
import axios from '../../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import CardFourComponent from '../../components/CardFourComponent';
import TableComponent from '../../components/TableComponent';
import { useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import {  useSelector } from "react-redux";

//attribute dropdown options
const dropdownOptions = [
  { id: 1, value: 'Teacher - Grade -- how many grades is the teacher teaching?' },
  { id: 2, value: 'Teacher to number of classes ratio -- how many classrooms is the teacher teaching?' },
  { id: 3, value: 'Teacher - Student Score Range' },
  { id: 4, value: 'Teacher - Subject (No. of subjects taught by the teacher)' },
  { id: 5, value: 'Average teacher salary' },
  { id: 6, value: 'Teacher - pupil ratio' },
  { id: 7, value: 'Qualification of Teacher' },
  { id: 8, value: 'No. of teachers in school' },
  
];

//filters based on attribute id
const commonAttributes = ['State', 'District','Board of Education', 'School Location', 'School Management', 'School Category', 'School Type', 'Qualification','Mode of Employment','Gender'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

//api endpoints
const endpointMapping = {
  1: '/teacher/total-teaching-grades-stats',
  2: '/teacher/teacher-to-classes-ratio-stats',
  5: '/teacher/average-teacher-salary-stats',
  6: '/teacher/teachers-to-pupil-ratio-stats',
  7: '/teacher/qualification-teachers-stats',
 
 
};
const tableEndPoints = {
  1: '/teacher/total-teaching-grades-table',
  2: '/teacher/teacher-to-classes-ratio-table',
  5: '/teacher/average-teacher-salary-table',
  6: '/teacher/teachers-to-pupil-ratio-table',
  7: '/teacher/qualification-teachers-table',
}

//default chart data for first 3 cards
const defaultChartData = {
  labels: [],
  datasets: [
    {
      label: 'No of Teachers (Purple)',
      type: 'bar',
      backgroundColor: 'rgba(185,102,220,1)',
      borderColor: 'rgba(185,102,220,1)',
      borderWidth: 2,
      data: [],
      dataStudent: [],
      barThickness: 30,
      borderRadius: 5,
      order: 2,
    },
    {
      label: 'No of Teachers (Blue)',
      type: 'bar',
      backgroundColor: 'rgba(68,198,212,1)',
      borderColor: 'rgba(68,198,212,1)',
      borderWidth: 2,
      borderRadius: 5,
      data: [],
      dataStudent: [],
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
      dataStudent: [],
      barThickness: 30,
      borderRadius: 5,
      order: 2,
    },
    {
      label: 'No of Teachers (Pan India)',
      type: 'bar',
      backgroundColor: 'rgba(68,198,212,1)',
      borderColor: 'rgba(68,198,212,1)',
      borderWidth: 2,
      borderRadius: 5,
      data: [],
      dataStudent: [],
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

//table headings
const tableHeadings = [
  
  'Number of Teachers', 
  'Number of Students',
  'Average Score of Students',
  'Number of Teachers', 
  'Number of Students',
  'Average Score of Students'
];


const Teacher_Attributes = () => {
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

  const [filters, setFilters] = useState({
    0: false,
    1: {},
    2: {},
    3: {},
    4: {},
  });
  const [attributeNameValue, setAttributeNameValue] = useState({
    0: "",
    1: "",
    2: "",
    3: "",
    4: "",
    
  });

  const [dataAvailable,setDataAvailable]=useState({
    1: false,
    2: false,
    3: false,
    4: false,
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
  console.log(key,cardKey)
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
        stateId: value ? ((value.State && value.State !== "All") ? value.State : (cardKey==4)? 7: null) : (cardKey==4)? 7: null,
        districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
        qualification: value ? ((value.Qualification && value.Qualification !== 'All') ? value.Qualification : null) : null,
        employmentNature: value ? ((value['Mode of Employment'] && value['Mode of Employment'] !== 'All') ? value['Mode of Employment'] : null) : null,
        gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
        educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
        schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
        
      };

   
   
    
    const res = await axios.post(endpoint, payload);
     
     if(res.data.status && res.data.statusCode == 200){
      setLoadingTable(prevValue => ({
        ...prevValue,
        [cardKey]: false,
      }));
      
        const result = res.data.result;
        
        if(cardKey != 4){
            const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,dataOneStudent,dataTwoStudent,attributeName] = parseResultData(key,"table",cardKey,result);
          
            
            
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
        stateId: value ? ((value.State && value.State !== "All") ? value.State : (cardKey==4)? 7: null) : (cardKey==4)? 7: null,
        districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
        qualification: value ? ((value.Qualification && value.Qualification !== 'All') ? value.Qualification : null) : null,
        employmentNature: value ? ((value['Mode of Employment'] && value['Mode of Employment'] !== 'All') ? value['Mode of Employment'] : null) : null,
        gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
        educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
        schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
        
      };

     
     

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
              const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg,newTableData,dataOneStudent,dataTwoStudent,attributeName] = parseResultDataCard4(key,cardKey, result);
              setCardData(prevCardData => ({
                ...prevCardData,
                [cardKey]: {
                  labels: labelsData,
                  datasets: createDatasetsCard4(dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName,dataOneStudent,dataTwoStudent),
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
              const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,dataOneStudent,dataTwoStudent,attributeName] = parseResultData(key,"card",cardKey, result);
              setCardData(prevCardData => ({
              ...prevCardData,
              [cardKey]: {
              labels: labelsData,
              datasets: createDatasets(dataOne, dataTwo, dataOneAvg, dataTwoAvg,dataOneStudent,dataTwoStudent),
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
      1: { key: 'total_teaching_grades', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey: 'avg_score',attributeName:'Grades' },
      2: { key: 'teacher_classroom_type', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey: 'avg_score',attributeName:'Classes' },
      5: { key: 'monthly_salary', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey: 'avg_score',attributeName:'Salary' },
      6: { key: 'pupil_ratio', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey: 'avg_score',attributeName:'Salary' },
      7: { key: 'qualification', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey: 'avg_score',attributeName:'Qualification' },
      
     
      
    };

    const { key: labelKey, dataOneKey, dataTwoKey,dataThreeKey, avgKey,attributeName } = mappings[key];
    
    setCardMapping(mappings)
    let labelsData = [];
    let dataOne = [];
    let dataOneStudent = [];
    let dataOneAvg = [];
    let dataTwo = [];
    let dataTwoStudent = [];
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
         dataOneStudent = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
         dataOneAvg = labelsData.map(label => {
          const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0;
          return parseFloat(value.toFixed(2));
        });
      
         dataTwo = labelsData.map(label => result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
         dataTwoStudent = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
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
              dateRange1TotalValue: value.num_teachers_date1? value.num_teachers_date1: 0,
              dateRange1StudentValue:value.num_students_date1? value.num_students_date1: 0,
              dateRange1AvgValue: value.avg_score_date1? (parseFloat((value.avg_score_date1).toFixed(2))) : '0',
              dateRange2TotalValue: value.num_teachers_date2? value.num_teachers_date2: '0',
              dateRange2StudentValue:value.num_students_date2? value.num_students_date2: 0,
              dateRange2AvgValue: value.avg_score_date2 ? (parseFloat((value.avg_score_date2).toFixed(2))) : '0',

        })
      })
      }

    }
    console.log(newTableData)

  return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData,dataOneStudent,dataTwoStudent,attributeName];

  
    
  };

   //parse region vs pan India card details
  const parseResultDataCard4 = (key,cardKey, result) => {
    const mappings = {
      1: { key: 'total_teaching_grades', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Grades' },
      2: { key: 'teacher_classroom_type', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Classes' },
      5: { key: 'monthly_salary', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Salary' },
      6: { key: 'pupil_ratio', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Salary' },
      7: { key: 'qualification', dataOneKey: 'num_teachers', dataTwoKey: 'num_teachers',dataThreeKey: 'num_students', avgKey1: 'avg_score',avgKey2: 'avg_score',attributeName:'Qualification' },
      
    };

    const { key: labelKey, dataOneKey, dataTwoKey,dataThreeKey, avgKey1,avgKey2,attributeName } = mappings[key];
    
    setCard4Mapping(mappings)
    let labelsData = [];
    let dataOne = [];
    let dataOneStudent = [];
    let dataOneAvg = [];
    let dataTwo = [];
    let dataTwoStudent = [];
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
       dataOneStudent = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
       dataOneAvg = labelsData.map(label => {
        const value = result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0;
        return parseFloat(value.toFixed(2));
      });
    
       dataTwo = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
       dataTwoStudent = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataThreeKey] || 0);
       dataTwoAvg = labelsData.map(label => {
        const value = result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0;
        return parseFloat(value.toFixed(2));
      });
    
       newTableData = labelsData.map(label => ({
        attributes: label,
        dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
        dateRange1StudentValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange1AvgValue: parseFloat((result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0).toFixed(2)),
        dateRange2TotalValue: result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
        dateRange2StudentValue: result.dataNation.find(item => item[labelKey] === label)?.[dataThreeKey] || 0,
        dateRange2AvgValue: parseFloat((result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0).toFixed(2)),
      }));

    }
    
   
  
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg,newTableData,dataOneStudent,dataTwoStudent,attributeName];
  };

  //datasets creation for first three cards based on default chart data
  const createDatasets = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,dataOneStudent,dataTwoStudent) => [
    { ...defaultChartData.datasets[0], data: dataOne || [],dataStudent:dataOneStudent || [] },
    { ...defaultChartData.datasets[1], data: dataTwo || [],dataStudent:dataTwoStudent || [] },
    { ...defaultChartData.datasets[2], data: dataOneAvg || [] },
    { ...defaultChartData.datasets[3], data: dataTwoAvg || [] },
  ];

  //datasets creation for region vs panIndia card(4TH CARD) based on default chart data
  const createDatasetsCard4 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName,dataOneStudent,dataTwoStudent) => [
    { ...defaultChartDataCard4.datasets[0], data: dataOne || [],label: `No of Teachers (${defaultStateName})`,dataStudent:dataOneStudent || [] },
    { ...defaultChartDataCard4.datasets[1], data: dataTwo || [],dataStudent:dataTwoStudent || []},
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
console.log(cardData)
console.log(attributeNameValue)
 
  return (
    <div>
      <h2>Teacher Attributes</h2>
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
             category="Teachers"
             subtype = "t1"
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
              category="Teachers"
              subtype = "t1"
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
          category="Teachers"
          subtype = "t1"
          attributeHeading={attributeNameValue[0]}
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default Teacher_Attributes;