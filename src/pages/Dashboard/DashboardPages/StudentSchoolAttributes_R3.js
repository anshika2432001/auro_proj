import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent'; 

const dropdownOptions = ['Option 1', 'Option 2', 'Option 3', 'Option 4', 'Option 5', 'Option 6'];

const defaultOptions = dropdownOptions.slice(0, 4);

const StudentSchoolAttributes_R3 = () => {
  return (
    <div>
      <h2>Student Learning Behaviour</h2>
      <Grid container spacing={2}>
        {defaultOptions.map((option, index) => (
          <Grid item xs={6} key={index}>
            <CardComponent title={option} dropdownOptions={dropdownOptions} />
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default StudentSchoolAttributes_R3;
