import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'School with Integrated Science lab facility up to Secondary level' },
  { id: 2, value: 'School having electricity connection' },
  { id: 3, value: 'School with separate room for Headmaster/Principal' },
  { id: 4, value: 'School with functional solar panel (if required)' },
  { id: 5, value: 'School with Kitchen Garden (if required)' },
  { id: 6, value: 'School with sports equipment' },
  { id: 7, value: 'School with Separate science lab facility for higher Secondary level ' },
  { id: 8, value: 'School with Functional Rainwater harvesting facility (if required)' },
  { id: 9, value: 'School with playground' },
  { id: 10, value: 'Schools having handrails' },
  { id: 11, value: 'Schools having ramps' },
  { id:12, value: 'Schools having functional toilets for girls' },
  { id: 13, value: 'Schools having functional toilets for boys' },
  { id: 14, value: 'Schools with CWSN friendly toilets' },
  { id: 15, value: 'Schools having incinerators' },
  {id:16,value:'Schools having pad vending machine'},
  { id: 17, value: '% of entitled CWSN who have received aids and appliances' },
  { id: 18, value: 'Schools with internet facility' },
  { id: 19, value: 'Schools with functional computer/ laptop used for pedagogical purposes' },
  { id: 20, value: 'Schools having computer-assisted teaching learning facility (e.g., smart classrooms used for teaching with Digital Boards/ Smart Boards/ Virtual Classrooms/ Smart TV)' },
  { id: 21, value: 'Schools having digital library' },
  { id: 22, value: 'Availability of librarian' },
  { id: 23, value: 'Availability of a school library ' },
  { id: 24, value: 'School having newspaper' },
  { id: 25, value: 'School getting Free Textbook within one month of start of academic year' },
  { id: 26, value: 'Schools getting Uniforms within one month of the start of academic year' },
  { id: 27, value: 'Schools having drinking water facility' },
  { id:28, value: 'School having water purifier' },
  { id: 29, value: 'School having handwash facility' },
  { id: 30, value: 'Schools having separate toilets for girls and boys' },
  { id: 31, value: 'Schools having Disaster Management Plan' },
  {id:32,value:'Schools having a boundary wall'},
  {id:33,value:'Schools in which girls have been given training in self-defence'},
  {id:34,value:'Schools in which girls are given hygiene workshops'},
  {id:35,value:'Schools where children have access to basic health services (like a medical room)'},
  {id:36,value:'Schools carry out health check-up of all students '},
  {id:37,value:'School having a qualified Child Counsellor/ Psychologist or Teachers who have undergone training to be designated as First level Counsellor'},
  {id:38,value:'Does the school provide counselling services'},
  {id:39,value:'School giving orientation on cyber safety to students'},
  

];

const commonAttributes = ['Board of Education', 'School Location', 'School Management', 'School Category', 'School Type', 'Pre Primary', 'School From starting class to end class'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

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

const SchoolInfrastructure = () => {
  const [filterOptions,setFilterOptions] = useState({});

  


  return (
    <div>
      <h2>School Infrastructure</h2>
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

export default SchoolInfrastructure;