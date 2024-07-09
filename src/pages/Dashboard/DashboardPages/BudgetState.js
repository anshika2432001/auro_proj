import React, { useEffect, useState } from 'react';
import { Grid } from '@mui/material';
import TableComponent from '../components/TableComponent';
import axios from '../../../utils/axios';
import BudgetCardComponent from '../components/BudgetCardComponent';

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
  const [chartData, setChartData] = useState({});
  const [titles, setTitles] = useState(dropdownOptions.map(option => option.value));
  const [selectedAttributes, setSelectedAttributes] = useState(dropdownOptions.map(option => option.id));

  useEffect(() => {
    getBudgetInfo();
  }, []);

  const getBudgetInfo = async () => {
    try {
      const res = await axios.get('/budget-state-data');
      const result = res.data.result;

      const processedChartData = processChartData(result);
      setChartData(processedChartData);
    } catch (error) {
      console.log(error);
    }
  };

  const processChartData = (data) => {
    const labels = data.fundsAllocated.map(item => item.state_name);
    
    const fundsAllocatedData = data.fundsAllocated.map(item => item.funds_allocated);
    const publicExpenditureData = data.publicExpenditure.map(item => item.education_expenditure_percentage);
    const samagraSikshaFundsApprovedData = data.samagraSikshaFundsApproved.map(item => item.percentage_of_samagra_shiksha_funds_approved);
    const samagraSikshaFundsReceivedData = data.samagraSikshaFundsReceived.map(item => item.percentage_of_samagra_shiksha_funds_received);

    return {
      1: {
        labels,
        datasets: [
          {
            label: 'Funds Allocated',
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: fundsAllocatedData,
          }
        ]
      },
      2: {
        labels,
        datasets: [
          {
            label: 'Public Expenditure on Education',
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: publicExpenditureData,
          }
        ]
      },
      3: {
        labels,
        datasets: [
          {
            label: 'Samagra Siksha Funds Approved',
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: samagraSikshaFundsApprovedData,
          }
        ]
      },
      4: {
        labels,
        datasets: [
          {
            label: 'Samagra Siksha Funds Received',
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: samagraSikshaFundsReceivedData,
          }
        ]
      }
    };
  };

  const handleAttributeChange = (attributeId, cardIndex) => {
    const newTitle = dropdownOptions.find(option => option.id === attributeId).value;
    setTitles(prevTitles => {
      const newTitles = [...prevTitles];
      newTitles[cardIndex] = newTitle;
      return newTitles;
    });
    setSelectedAttributes(prevAttributes => {
      const newAttributes = [...prevAttributes];
      newAttributes[cardIndex] = attributeId;
      return newAttributes;
    });
  };

  return (
    <div>
      <h2>Budget and Expenditure</h2>
      <Grid container spacing={2}>
        {dropdownOptions.map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={index}>
            <BudgetCardComponent
              title={titles[index]}
              selectedAttribute={selectedAttributes[index]}
              dropdownOptions={dropdownOptions} 
              attributeBasedDropdowns={attributeBasedDropdowns} 
              chartData={chartData[selectedAttributes[index]] || {}} 
              onAttributeChange={(attributeId) => handleAttributeChange(attributeId, index)}
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
