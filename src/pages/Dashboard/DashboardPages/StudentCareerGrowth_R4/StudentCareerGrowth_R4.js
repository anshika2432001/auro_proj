import React, { useEffect, useState } from 'react';
import axios from '../../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'Students taking vocational courses' },
  { id: 2, value: 'Students who want to access internship' },
  { id: 3, value: 'Students likely to attend higher education' },
  { id: 4, value: 'Number of students who are confident that they have knowledge of career options' },
  { id: 5, value: 'Student career domains' },
  { id: 6, value: 'Students confident in the communication skills, problem solving, team building' },
  { id: 7, value: 'Number of students accessing preparatory classes for higher education (college)' },
  { id: 8, value: 'Students who want to access vocational courses ' },
  
];

const attributeBasedDropdowns = {
  1: ['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  2:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  3:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  4:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  5:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  6:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  7:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  8:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
};

const chartData = {
  labels: ['Cant Say', 'Up to 1 hr', '1-3 hrs', '3-5 hrs', 'More than 5 hrs'],
  datasets: [
    {
      label: 'No of Students (Purple)',
      type: 'bar',
      backgroundColor: 'rgba(185,102,220,1)',
      borderColor: 'rgba(185,102,220,1)',
      borderWidth: 2,
      data: [200, 150, 300, 250, 400],
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
      data: [100, 130, 250, 200, 350],
      barThickness: 30,
      order: 2,
    },
    {
      label: 'Average no. of students (Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [175, 140, 275, 225, 375],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average no. of students (Blue)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [75, 80, 150, 125, 225],
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

const StudentCareerGrowth_R4 = () => {
  const [filterOptions,setFilterOptions] = useState({});
 

  return (
    <div>
      <h2>Student R4 Career Growth Outlook</h2>
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

export default StudentCareerGrowth_R4;
