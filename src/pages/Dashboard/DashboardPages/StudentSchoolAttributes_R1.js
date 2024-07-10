import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
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
  { id: 14, value: 'LO Reporting (Difficulty level, Competency)' },
  { id: 15, value: 'Topic wise breakdown - Student Attempts' }
];

const attributeBasedDropdowns = {
  1: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  2: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  3: ['School Location', 'Subject', 'Learning Level', 'Grade'],
  4: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  5: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  6: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  7: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  8: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  9: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  10: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  11: ['Gender', 'School Location', 'Learning Level', 'Grade', 'Average Microscholarship'],
  12: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  13: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
  14: [],
  15: ['Gender', 'School Location', 'Subject', 'Learning Level', 'Grade', 'Average Microscholarship'],
};

const tableInfo = [
  {
    attributes: "Visual Learners",
    dateRange: "23/03/23-10/04/24",
    totalValue: "100",
    avgValue: "23"
  },
  {
    attributes: "Auditory",
    dateRange: "23/03/23-10/04/24",
    totalValue: "350",
    avgValue: "65"
  },
  {
    attributes: "Kinesthetic",
    dateRange: "23/03/23-10/04/24",
    totalValue: "220",
    avgValue: "34"
  },
  {
    attributes: "Reading/Writing",
    dateRange: "23/03/23-10/04/24",
    totalValue: "320",
    avgValue: "45"
  },
  {
    attributes: "Any Other",
    dateRange: "21/01/24-10/04/24",
    totalValue: "280",
    avgValue: "36"
  },
];

const tableHeadings = [
  'Attributes',
  'Date Range',
  'Number of Students',
  'Average Score of Students'
];

const StudentSchoolAttributes_R1 = () => {
  const [filterOptions,setFilterOptions] = useState({});
  const [chartData, setChartData] = useState({
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
        label: 'Average no. of students (Purple)',
        type: 'line',
        borderColor: 'rgba(177,185,192,1)',
        borderWidth: 4,
        fill: false,
        data: [],
        spanGaps: true,
        order: 1,
      },
      {
        label: 'Average no. of students (Blue)',
        type: 'line',
        borderColor: 'rgba(177,185,192,1)',
        borderWidth: 4,
        fill: false,
        data: [],
        spanGaps: true,
        order: 1,
      },
    ],
  });

  useEffect(() => {
   
    getR1MicroScholarQuizInfo();
    getFilterOptions();
  }, []);

  

const getFilterOptions = async()=> {
  try{
    const res = await axios.get('/filter-dropdowns');
    const result = res.data.result;
    console.log(result);
    setFilterOptions(result)
  }
  catch(error){
    console.log(error)
  }
}
console.log(filterOptions)
  const getR1MicroScholarQuizInfo = async () => {
    try {
      let payload = {
        transactionDateFrom1: null,
        transactionDateTo1: null,
        transactionDateFrom2: null,
        transactionDateTo2: null,
        grades: null,
        subject: null,
        schoolLocation: null,
        stateId: null,
        districtId: null,
        socialGroup: null,
        gender: null,
        ageFrom: null,
        ageTo: null,
        educationBoard: null,
        schoolManagement: null
      };
      const res = await axios.post('/r1/micro-scholar-quiz', payload);
      const result = res.data.result;
      console.log(result);

      const labels = result.dataOne.map(item => `Quiz Range ${item.quiz_range}`);
      const dataOneNumStudents = result.dataOne.map(item => item.num_students);
      const dataOneAvgScores = result.dataOne.map(item => item.average_score);
      const dataTwoNumStudents = result.dataTwo.map(item => item.num_students);
      const dataTwoAvgScores = result.dataTwo.map(item => item.average_score);

      setChartData({
        labels,
        datasets: [
          {
            label: 'No of Students (Purple)',
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: dataOneNumStudents,
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
            data: dataTwoNumStudents,
            barThickness: 30,
            order: 2,
          },
          {
            label: 'Average no. of students (Purple)',
            type: 'line',
            borderColor: 'rgba(177,185,192,1)',
            borderWidth: 4,
            fill: false,
            data: dataOneAvgScores,
            spanGaps: true,
            order: 1,
          },
          {
            label: 'Average no. of students (Blue)',
            type: 'line',
            borderColor: 'rgba(177,185,192,1)',
            borderWidth: 4,
            fill: false,
            data: dataTwoAvgScores,
            spanGaps: true,
            order: 1,
          },
        ],
      });

    } catch (error) {
      console.log(error);
    }
  };
  return (
    <div>
      <h2>Student R1 Attributes</h2>
      <Grid container spacing={2}>
      {dropdownOptions.slice(0, 4).map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={index}>
            <CardComponent 
              title={option} 
              dropdownOptions={dropdownOptions} 
              filterOptions={filterOptions}
              attributeBasedDropdowns={attributeBasedDropdowns} 
              chartData={chartData} 
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <TableComponent 
          dropdownOptions={dropdownOptions} 
          attributeBasedDropdowns={attributeBasedDropdowns} 
          tableInfo={tableInfo} 
          tableHeadings={tableHeadings} 
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default StudentSchoolAttributes_R1;