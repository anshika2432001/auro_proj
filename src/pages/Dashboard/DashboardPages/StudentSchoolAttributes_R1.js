

import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import CardFourComponent from '../components/CardFourComponent';
import TableComponent from '../components/TableComponent';



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
  { id: 15, value: 'Topic wise breakdown - Student Attempts' }
];

const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

const endpointMapping = {
  1: '/r1/subject-wise-breakdown',
  2: '/r1/grade-wise-avg-score',
  3: '/r1/micro-scholar-quiz',
  4: '/r1/total-quiz-attempted',
  5: '/r1/topic-wise-avg-score',
  6: '/r1/topic-wise-micro-scholar-quiz',
  7: '/r1/top-performing-topics',
  8: '/r1/weak-performing-topics',
  9: '/r1/core-retake-practice',
  10: '/r1/core-retake-improvement',
  11: '/r1/subject-wise-breakdown-improve',
  12: '/r1/grade-wise-breakdown-improve',
  13: '/r1/topic-wise-breakdown-improve',
  15: '/r1/topic-wise-student-attempts',
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
      label: 'No of Students Pan India(Purple)',
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
      label: 'No of Students Region(Blue)',
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
      label: 'Average score Pan India(Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average score Region(Blue)',
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
  const [initialCall,setInitialCall] = useState(true);
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
    1: dropdownOptions[0],
    2: dropdownOptions[1],
    3: dropdownOptions[2],
    4: dropdownOptions[3],
  });

  const [tableData, setTableData] = useState({
    0: []
});
  

  useEffect(() => {
    
    // fetchData(1, filters[1],1);
    // fetchData(2, filters[2],2);
    // fetchData(3, filters[3],3);
    // fetchData(4, filters[4],4);
    fetchDataForAllCards();
    fetchTableData();
   
    
   
 

    
  }, []);



  const fetchDataForAllCards = () => {
   

    dropdownOptions.slice(0, 4).forEach(option => {
      
      fetchData(option.id, filters[option.id],option.id);
     
    });
  };
  const fetchTableData = () => {
    const firstOption = dropdownOptions[0];
    fetchData(firstOption.id, filters[firstOption.id], 0);
  };

  const fetchData = async (key, value,cardKey) => {
    console.log(cardKey)
    const endpoint = endpointMapping[key];

    try {
      let payload = {
        transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1 : null) : null,
        transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1 : null) : null,
        transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2 : null) : null,
        transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2 : null) : null,
        grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
        subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
        schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
        stateId: value ? ((value.State && value.State !== "All") ? value.State : null) : null,
        districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
        socialGroup: value ? ((value['Social Group'] && value['Social Group'] !== 'All') ? value['Social Group'] : null) : null,
        gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
        ageFrom: null,
        ageTo: null,
        educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
        schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
      };

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }

      const res = await axios.post(endpoint, payload);
      const result = res.data.result;

      const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData] = parseResultData(key, result);

      if(cardKey == 4){
        setCardData(prevCardData => ({
          ...prevCardData,
          [cardKey]: {
            labels: labelsData,
            datasets: createDatasetsCard4(dataOne, dataTwo, dataOneAvg, dataTwoAvg),
          }
        }));
      }
      else{
        setCardData(prevCardData => ({
          ...prevCardData,
          [cardKey]: {
            labels: labelsData,
            datasets: createDatasets(dataOne, dataTwo, dataOneAvg, dataTwoAvg),
          }
        }));
      }
      

      if (cardKey === 0) {
        setTableData(prevData => ({
          ...prevData,
          [cardKey]: newTableData,
        }));
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
      4: { key: 'quiz_range', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'national_avg_score' },
      5: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score_pan_india' },
      6: { key: 'topic_name', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score_pan_india' },
      7: { key: 'topic_name', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'avg_score_nation' },
      8: { key: 'topic_name', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'avg_score_nation' },
      9: { key: 'quiz_attempt', dataOneKey: 'num_students_pan_india', dataTwoKey: 'num_students_pan_india', avgKey: 'avg_score_pan_india' },
      10: { key: 'quiz_attempt', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_improvement_nation' },
      11: { key: 'subject', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'percent_improvement_second_nation' },
      12: { key: 'grade', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'percent_improvement_second_nation' },
      13: { key: 'topic_name', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'percent_improvement_second_nation' },
      15: { key: 'topic_name', dataOneKey: 'num_students_nation', dataTwoKey: 'num_students_nation', avgKey: 'percent_improvement_second_nation' },
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey } = mappings[key];
    
    const allLabels = new Set([
      ...result.dataOne.map(item => item[labelKey]),
      ...result.dataTwo.map(item => item[labelKey])
    ]);
  
    const labelsData = Array.from(allLabels);
    const dataOne = labelsData.map(label => result.dataOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
    const dataOneAvg = labelsData.map(label => result.dataOne.find(item => item[labelKey] === label)?.[avgKey] || 0);
    const dataTwo = labelsData.map(label => result.dataTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
    const dataTwoAvg = labelsData.map(label => result.dataTwo.find(item => item[labelKey] === label)?.[avgKey] || 0);
  
    const newTableData = labelsData.map(label => ({
      attributes: label,
      dateRange1TotalValue: result.dataOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
      dateRange1AvgValue: result.dataOne.find(item => item[labelKey] === label)?.[avgKey] || 0,
      dateRange2TotalValue: result.dataTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
      dateRange2AvgValue: result.dataTwo.find(item => item[labelKey] === label)?.[avgKey] || 0,
    }));
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData];
  };

  const createDatasets = (dataOne, dataTwo, dataOneAvg, dataTwoAvg) => [
    { ...defaultChartData.datasets[0], data: dataOne || [] },
    { ...defaultChartData.datasets[1], data: dataTwo || [] },
    { ...defaultChartData.datasets[2], data: dataOneAvg || [] },
    { ...defaultChartData.datasets[3], data: dataTwoAvg || [] },
  ];

  const createDatasetsCard4 = (dataOne, dataTwo, dataOneAvg, dataTwoAvg) => [
    { ...defaultChartDataCard4.datasets[0], data: dataOne || [] },
    { ...defaultChartDataCard4.datasets[1], data: dataTwo || []},
    { ...defaultChartDataCard4.datasets[2], data: dataOneAvg || [] },
    { ...defaultChartDataCard4.datasets[3], data: dataTwoAvg || [] },
  ];

  const onFilterChange = (key, value,cardKey) => {
    console.log(key)
    console.log(value)
    setFilters(prevFilters => ({
      ...prevFilters,
      [key]: value,
    }));
    const selectedOption = dropdownOptions.find(option => option.id === key);

    setCardTitle(prevData => ({
      ...prevData,
      [cardKey]: selectedOption,
    }));
     fetchData(key, value,cardKey);
  };

  console.log(cardData)
 
  
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
