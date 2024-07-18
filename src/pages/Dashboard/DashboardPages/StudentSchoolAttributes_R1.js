

import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import CardFourComponent from '../components/CardFourComponent';
import TableComponent from '../components/TableComponent';
import { useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import { useDispatch, useSelector } from "react-redux";


const dropdownOptions = [
  { id: 1, value: 'Subject Wise Breakdown - Average Score' },
  { id: 2, value: 'Gradewise - Average Score' },
  { id: 3, value: 'Microscholarship Quizzes - Average Score' },
  { id: 4, value: 'Total Quiz Attempted' },
  { id: 5, value: 'Topic wise breakdown - Average Score' },
  { id: 6, value: 'Topic wise breakdown - No. of Microscholarship Quizzes' },
  { id: 7, value: 'Top Performing Topics' },
  { id: 8, value: 'Weak Performing Topics' },
  { id: 9, value: 'Core-Retake- ( No of Students)' },
  { id: 10, value: 'Core-Retake- (Average Score)' },
  { id: 11, value: 'Subject Wise Breakdown - % Improvement' },
  { id: 12, value: 'Grade wise- % Improvement Score' },
  { id: 13, value: 'Topic wise breakdown - %Improvement' },
  { id: 14, value: 'Topic wise breakdown - Student Attempts' }
];

const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});



const endpointMapping = {
  1: '/r1/subject-wise-breakdown-stats',
  2: '/r1/grade-wise-avg-score-stats',
  3: '/r1/micro-scholar-quiz-stats',
  4: '/r1/total-quiz-attempted-stats',
  5: '/r1/topic-wise-avg-score-stats',
  6: '/r1/topic-wise-micro-scholar-quiz-stats',
  7: '/r1/top-performing-topics-stats',
  8: '/r1/weak-performing-topics-stats',
  9: '/r1/core-retake-practice-stats',
  10: '/r1/core-retake-improvement-stats',
  11: '/r1/subject-wise-breakdown-improve-stats',
  12: '/r1/grade-wise-breakdown-improve-stats',
  13: '/r1/topic-wise-breakdown-improve-stats',
  14: '/r1/topic-wise-student-attempts-stats',
};

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



const tableHeadings = [
  
  'Number of Students', 
  'Average Score of Students',
  'Number of Students', 
  'Average Score of Students'
];

const StudentSchoolAttributes_R1 = () => {
   const titleId = useParams();
   const filterOptions = useSelector((state) => state.filterDropdown.data.result);
   console.log(titleId.id)
  
  const defaultDateRange1Start = dayjs('2024-01-01');
  const defaultDateRange1End = dayjs('2024-01-31');
  const defaultDateRange2Start = dayjs('2024-03-01');
  const defaultDateRange2End = dayjs('2024-03-31');
  let defaultStartDateRange1= defaultDateRange1Start.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultEndDateRange1= defaultDateRange1End.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultStartDateRange2= defaultDateRange2Start.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultEndDateRange2= defaultDateRange2End.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  

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
    0: []
});


useEffect(() => {
  fetchData(2, filters[2],2);
  fetchData(3, filters[3],3);
  fetchData(4, filters[4],4);
  
}, []);


  useEffect(() => {
    
   
    if(titleId.id && [1, 2, 3, 4].includes(Number(titleId.id))){
     
    fetchData(1, filters[1],1);
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
    }
    
    fetchTableData(); 
   
    
  }, [titleId.id]);



 
  const fetchTableData = () => {
    const firstOption = dropdownOptions[Number(titleId.id)-1];
    fetchData(firstOption.id, filters[firstOption.id], 0);
  };

  const fetchData = async (key, value,cardKey) => {
    
    
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
        
        
      
      const result = res.data.result;

    

      if(cardKey == 4){
        console.log(filterOptions.states)
        const stateValue = value ? ((value.State && value.State !== "All") ? value.State :  7): 7
        const defaultStateName = filterOptions.states.find(state => state.state_id === stateValue)?.state_name
        console.log(defaultStateName)
        const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg] = parseResultDataCard4(key, result);
        
        setCardData(prevCardData => ({
          ...prevCardData,
          [cardKey]: {
            labels: labelsData,
            datasets: createDatasetsCard4(dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName),
          }
        }));
      }
      else{
        const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData] = parseResultData(key, result);
        setCardData(prevCardData => ({
          ...prevCardData,
          [cardKey]: {
            labels: labelsData,
            datasets: createDatasets(dataOne, dataTwo, dataOneAvg, dataTwoAvg),
          }
        }));
      }
      

      if (cardKey === 0) {
        const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData] = parseResultData(key, result);
        setTableData(prevData => ({
          ...prevData,
          [cardKey]: newTableData,
        }));
      }
    }
    else{
      console.log("error")
    }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const parseResultData = (key, result) => {
    const mappings = {
      1: { key: 'subject', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'average_score' },
      2: { key: 'grade', dataOneKey: 'num_students_attempting', dataTwoKey: 'num_students_attempting', avgKey: 'average_score' },
      3: { key: 'quiz_range', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'average_score' },
      4: { key: 'quiz_range', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'regional_avg_score' },
      5: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score_region' },
      6: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score_state' },
      7: { key: 'topic_name', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_region', avgKey: 'avg_score_region' },
      8: { key: 'topic_name', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_region', avgKey: 'avg_score_region' },
      9: { key: 'quiz_attempt', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_region', avgKey: 'avg_score_region' },
      10: { key: 'quiz_attempt', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_improvement_region' },
      11: { key: 'subject', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_state', avgKey: 'percent_improvement_second_state' },
      12: { key: 'grade', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_state', avgKey: 'percent_improvement_second_state' },
      13: { key: 'topic_name', dataOneKey: 'num_students_state',dataTwoKey: 'num_students_state', avgKey: 'percent_improvement_second_state' },
      14: { key: 'topic_name', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_state', avgKey: 'percent_improvement_second_state' },
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey } = mappings[key];
    
    const allLabels = new Set([
      ...result.dataStateOne.map(item => item[labelKey]),
      ...result.dataStateTwo.map(item => item[labelKey])
    ]);
  
    const labelsData = Array.from(allLabels);
    const dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
    const dataOneAvg = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0);
    const dataTwo = labelsData.map(label => result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
    const dataTwoAvg = labelsData.map(label => result.dataStateTwo.find(item => item[labelKey] === label)?.[avgKey] || 0);
  
    const newTableData = labelsData.map(label => ({
      attributes: label,
      dateRange1TotalValue: result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
      dateRange1AvgValue: result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey] || 0,
      dateRange2TotalValue: result.dataStateTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
      dateRange2AvgValue: result.dataStateTwo.find(item => item[labelKey] === label)?.[avgKey] || 0,
    }));
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData];
  };

  const parseResultDataCard4 = (key, result) => {
    const mappings = {
      1: { key: 'subject', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'average_score',avgKey2: 'average_score' },
      2: { key: 'grade', dataOneKey: 'num_students_attempting', dataTwoKey: 'num_students_attempting', avgKey1: 'average_score',avgKey2: 'average_score' },
      3: { key: 'quiz_range', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'average_score',avgKey2: 'average_score' },
      4: { key: 'quiz_range', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey1: 'regional_avg_score',avgKey2: 'national_avg_score' },
      5: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students',avgKey1: 'avg_score_region', avgKey2: 'avg_score_pan_india' },
      6: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students',avgKey1: 'avg_score_state', avgKey2: 'avg_score_nation' },
      7: { key: 'topic_name', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_nation',avgKey1: 'avg_score_region', avgKey2: 'avg_score_nation' },
      8: { key: 'topic_name', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_nation', avgKey1: 'avg_score_region', avgKey2: 'avg_score_nation' },
      9: { key: 'quiz_attempt', dataOneKey: 'num_students_region', dataTwoKey: 'num_students_pan_india',avgKey1: 'avg_score_region', avgKey2: 'avg_score_pan_india'},
      10: { key: 'quiz_attempt', dataOneKey: 'num_students', dataTwoKey: 'num_students',avgKey1: 'avg_improvement_region', avgKey2: 'avg_improvement_nation' },
      11: { key: 'subject', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_nation', avgKey1: 'percent_improvement_second_state',avgKey2: 'percent_improvement_second_nation' },
      12: { key: 'grade', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_nation', avgKey1: 'percent_improvement_second_state',avgKey2: 'percent_improvement_second_nation' },
      13: { key: 'topic_name', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_nation', avgKey1: 'percent_improvement_second_state',avgKey2: 'percent_improvement_second_nation' },
      14: { key: 'topic_name', dataOneKey: 'num_students_state', dataTwoKey: 'num_students_nation', avgKey1: 'percent_improvement_second_state',avgKey2: 'percent_improvement_second_nation' },
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey1,avgKey2 } = mappings[key];
    
    const allLabels = new Set([
      ...result.dataStateOne.map(item => item[labelKey]),
      ...result.dataNation.map(item => item[labelKey])
    ]);
  
    const labelsData = Array.from(allLabels);
    const dataOne = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
    const dataOneAvg = labelsData.map(label => result.dataStateOne.find(item => item[labelKey] === label)?.[avgKey1] || 0);
    const dataTwo = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
    const dataTwoAvg = labelsData.map(label => result.dataNation.find(item => item[labelKey] === label)?.[avgKey2] || 0);
  
    
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg];
  };

  const createDatasets = (dataOne, dataTwo, dataOneAvg, dataTwoAvg) => [
    { ...defaultChartData.datasets[0], data: dataOne || [] },
    { ...defaultChartData.datasets[1], data: dataTwo || [] },
    { ...defaultChartData.datasets[2], data: dataOneAvg || [] },
    { ...defaultChartData.datasets[3], data: dataTwoAvg || [] },
  ];

  const createDatasetsCard4 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg,defaultStateName) => [
    { ...defaultChartDataCard4.datasets[0], data: dataOne || [],label: `No of Students (${defaultStateName})`, },
    { ...defaultChartDataCard4.datasets[1], data: dataTwo || []},
    { ...defaultChartDataCard4.datasets[2], data: dataOneAvg || [],label: `Average score (${defaultStateName})` },
    { ...defaultChartDataCard4.datasets[3], data: dataTwoAvg || [] },
  ];

  const onFilterChange = (key, value,cardKey) => {
    
    setFilters(prevFilters => ({
      ...prevFilters,
      [key]: value,
    }));
    const selectedOption = dropdownOptions.find(option => option.id === key);
console.log(selectedOption)
    setCardTitle(prevData => ({
      ...prevData,
      [cardKey]: selectedOption,
    }));
     fetchData(key, value,cardKey);
  };

  console.log(cardTitle)
 
  
    return (
    <div>
      <h2>Student R1 Attributes</h2>
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
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default StudentSchoolAttributes_R1;
