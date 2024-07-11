import React, { useEffect, useState } from 'react';
import axios from '../../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'Hours of individual study/practice per day' },
  { id: 2, value: 'Student learning style preferences' },
  { id: 3, value: 'Student preferences collaborative learning style preferences' },
  { id: 4, value: 'Paid Private Tuition Hours' },
  { id: 5, value: 'Children who read other materials in addition to textbooks' },
  { id: 6, value: 'Paid Private Tuition - Subject studied' },
  
];

const attributeBasedDropdowns = {
  1: ['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  2:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  3:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  4:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  5:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  6:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  
};

const chartData = {
  labels: ['Cant Say', 'Up to 1 hr', '1-3 hrs', '3-5 hrs', 'More than 5 hrs'],
  datasets: [
    {
      label: 'No of Students (Purple)',
      type: 'bar',
      backgroundColor: '#AD88F1',
      borderColor: '#AD88F1',
      borderWidth: 2,
      data: [400, 500, 600, 400, 500],
      barThickness: 30,
      borderRadius: 5, 
      order: 2,
    },
    {
      label: 'No of Students (Blue)',
      type: 'bar',
      borderColor: '#41B8D5',
      backgroundColor: '#41B8D5',
      borderWidth: 2,
      borderRadius: 5, 
      data: [100, 130, 250, 200, 350],
      barThickness: 30,
      order: 2,
    },
    {
      label: 'Average no. of students (Purple)',
      type: 'line',
      borderColor: '#CCCCCC',
      backgroundColor: '#CCCCCC',
      borderWidth: 4,
      fill: false,
      data: [200, 250, 300, 200, 250],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average no. of students (Blue)',
      type: 'line',
      borderColor: '#CCCCCC',
      backgroundColor: '#CCCCCC',
      borderWidth: 4,
      fill: false,
      data: [50, 65, 125, 100, 175],
      spanGaps: true,
      order: 1,
    },
  ],
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

const StudentLearningBehaviour = () => {
  const [filterOptions,setFilterOptions] = useState({});
 
  return (
    <div>
      <h2>Student Learning Behaviour</h2>
      <Grid container spacing={2}>
      {dropdownOptions.slice(0, 4).map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={index}>
            <CardComponent 
              title={option} 
              dropdownOptions={dropdownOptions} 
             
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

export default StudentLearningBehaviour;
