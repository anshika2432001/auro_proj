import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';

const dropdownOptions = [
  { id: 1, value: 'Hours of individual study/practice per day' },
  { id: 2, value: 'Student learning style preferences' },
  { id: 3, value: 'Student collaborative learning style preferences' },
  { id: 4, value: 'Paid Private Tuition Hours' },
  { id: 5, value: 'Children who read other materials in addition to textbooks' },
  { id: 6, value: 'Paid Private Tuition Subjectwise' }
];

const attributeBasedDropdowns = {
  1: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  2: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  3: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  4: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  5: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade'],
  6: ['Gender', 'School Location', 'Age Group','School Type','Board of Education','Grade']
};

const StudentLearningBehaviour = () => {
  return (
    <div>
      <h2>Student Learning Behaviour</h2>
      <Grid container spacing={2}>
        {dropdownOptions.map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={index}>
            <CardComponent 
              title={option} 
              dropdownOptions={dropdownOptions} 
              attributeBasedDropdowns={attributeBasedDropdowns} 
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <TableComponent title={dropdownOptions[0]} dropdownOptions={dropdownOptions} />
        </Grid>
      </Grid>
    </div>
  );
};

export default StudentLearningBehaviour;
