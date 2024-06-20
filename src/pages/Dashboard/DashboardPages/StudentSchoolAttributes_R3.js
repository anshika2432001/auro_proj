import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';

const dropdownOptions = [
  { id: 1, value: 'Hours of individual study/practice per day' },
  { id: 2, value: 'Student learning style preferences' },
  { id: 3, value: 'Student collaborative learning style preferences' },
  { id: 4, value: 'Paid Private Tuition Hours' },
  { id: 5, value: 'Children who read other materials in addition to textbooks' },
  { id: 6, value: 'Paid Private Tuition Subjectwise' }
];

const defaultOptions = dropdownOptions.slice(0, 4);

const StudentSchoolAttributes_R3 = () => {
  return (
    <div>
      <h2>Student Learning Behaviour</h2>
      <Grid container spacing={2}>
        {defaultOptions.map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={index}>
            <CardComponent title={option} dropdownOptions={dropdownOptions} />
          </Grid>
        ))}
        <Grid item xs={12}>
          <TableComponent title={dropdownOptions[0]} dropdownOptions={dropdownOptions} />
        </Grid>
      </Grid>
    </div>
  );
};

export default StudentSchoolAttributes_R3;
