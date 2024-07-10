import React,{useEffect,useState} from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';
import axios from '../../../utils/axios';

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

const attributeBasedDropdowns = {
  1:['State','District','School'],
  2:['State','District','School', 'Grade','Social Group','Gender','Annual Income','Subject','Mother Education','Father Education','Aggregate Scholarship'],
  3:['State','District','School', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  4:['Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  5:['Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  6:['Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  7:['State','District','School','Gender', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  8:['State','District','School', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
  9:['State','District','School', 'School Location', 'Subject','Learning Level','Grade','Average Microscholarship'],
};

const chartData = {
  labels: ['Below 15 students', '16-25 students', '26-35 students', '36-45 students', 'Above 45 students'],
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


const StudentSchoolAttributes_R2 = () => {
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
      <h2>Student R2 Attributes</h2>
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

export default StudentSchoolAttributes_R2;