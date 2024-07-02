import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';

const dropdownOptions = [
  { id: 1, value: 'Funds Allocated' },
  { id: 2, value: 'Public Expenditure' },
  { id: 3, value: 'Samagra Siksha Funds Approved' },
  { id: 4, value: 'Samagra Siksha Funds Received' }
];

const attributeBasedDropdowns = {
  1: ['State', 'District', 'School', 'Date Period', 'Grade', 'Subject', 'Learning Level'],
  2: ['State', 'District', 'School', 'Date Period', 'Grade', 'Subject', 'Learning Level'],
  3: ['State', 'District', 'School', 'Date Period', 'Grade', 'Subject', 'Learning Level'],
  4: ['State', 'District', 'School', 'Date Period', 'Grade', 'Subject', 'Learning Level']
};

const BudgetState = () => {
  return (
    <div>
      <h2>Budget and Expenditure</h2>
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

export default BudgetState;
