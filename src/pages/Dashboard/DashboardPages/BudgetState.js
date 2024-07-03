import React from 'react';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';

const dropdownOptions = [
  { id: 1, value: 'Funds allocated for Assessment Cell in SCERT' },
  { id: 2, value: 'Public expenditure on education as a % of total public expenditure in the state' },
  { id: 3, value: '% of Samagra Siksha Funds approved (against funds proposed by the state) during the previous financial year - For Govt and Govt aided' },
  { id: 4, value: '% Of Samagra Siksha Funds received (against funds approved to the state) during the previous financial year - For Govt and Govt aided' }
];

const attributeBasedDropdowns = {
  1: ['State'],
  2: ['State'],
  3: ['State'],
  4: ['State']
};

const chartData = {
  labels: ['Rajasthan', 'Uttar Pradesh', 'Bihar', 'Tamil Nadu', 'West Bengal'],
  datasets: [
    {
      label: 'Amount/ Percentage of fund allocated',
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
      label: 'Amount/ Percentage of fund allocated',
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
      label: 'Average Amount',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [175, 140, 275, 225, 375],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average Amount',
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
    attributes: "Rajasthan",
    dateRange: "23/03/23-10/04/24",
    totalValue: "100",
    avgValue: "23"
  },
  {
    attributes: "Uttar Pradesh",
    dateRange: "23/03/23-10/04/24",
    totalValue: "350",
    avgValue: "65"
  },
  {
    attributes: "Bihar",
    dateRange: "23/03/23-10/04/24",
    totalValue: "220",
    avgValue: "34"
  },
  {
    attributes: "Tamil Nadu",
    dateRange: "23/03/23-10/04/24",
    totalValue: "320",
    avgValue: "45"
  },
  {
    attributes: "West Bengal",
    dateRange: "21/01/24-10/04/24",
    totalValue: "280",
    avgValue: "36"
  },
];

const tableHeadings = [
  'Attributes', 
  'Date Range', 
  'Total Amount', 
  'Average Amount'
];

const BudgetState = () => {
  return (
    <div>
      <h2>Budget and Expenditure</h2>
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

export default BudgetState;
