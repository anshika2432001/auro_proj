import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'Teacher - Grade' },
  { id: 2, value: 'Teacher to number of classes ratio' },
  { id: 3, value: 'Teacher - Student Score Range' },
  { id: 4, value: 'Teacher Subject' },
  { id: 5, value: 'Average teacher salary' },
  { id: 6, value: 'Teacher - pupil ratio' }
];

const attributeBasedDropdowns = {
  1: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  2: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  3: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  4: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  5: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  6: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade']
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

const Teacher_Attributes = () => {
  return (
    <div>
      <h2>Teacher Attributes</h2>
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

export default Teacher_Attributes;