import React, { useEffect, useState } from 'react';
import axios from '../../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'Immigrant background (i.e one or both parents born in another state) ' },
  { id: 2, value: 'Household with at least one member who completed Std XII' },
  { id: 3, value: 'Parents monthly spend on childs education' },
  { id: 4, value: 'Household has internet connection' },
  { id: 5, value: 'Mothers level of Education' },
  { id: 6, value: 'Average income of household' },
  { id: 7, value: 'Households with at least one member who knows how to operate a computer' },
  { id: 8, value: 'House type ' },
  { id: 9, value: '% Households which have other reading material (activity books, reading books, puzzles, newspaper)' },
  { id: 10, value: "Father's level of education" },
  { id: 11, value: 'Household has electricity connection ' },
  { id: 12, value: 'I (or another adult in the home) read(s) with my child every day or nearly every day' },
  { id: 13, value: 'My child’s teacher and I communicate with each other at least once a month (in person or by notes, text, email, phone, etc.)' },
  { id: 14, value: 'I expect my child will graduate from high school ' },
  { id: 15, value: 'I expect my child will go to college one day' },

];

const attributeBasedDropdowns = {
  1:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  2:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  3:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  4:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  5:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  6:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  7:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  8:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  9:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  10:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  11:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  12:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  13:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  14:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  15:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
 
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

const ParentalandHousehold = () => {
  const [filterOptions,setFilterOptions] = useState({});
  useEffect(() => {
   
   
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
  return (
    <div>
      <h2>Parental and Household Engagement</h2>
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

export default ParentalandHousehold;
