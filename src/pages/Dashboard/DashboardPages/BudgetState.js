import React, { useEffect, useState } from 'react';
import { Grid } from '@mui/material';
import TableComponent from '../components/TableComponent';
import axios from '../../../utils/axios';
import BudgetCardComponent from '../components/BudgetCardComponent';
import BudgetTableComponent from '../components/BudgetTableComponent';

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



const tableHeadings = [
  'Attributes', 
  'Date Range', 
  'Total Amount', 
  'Average Amount'
];

const BudgetState = () => {
  const [chartData, setChartData] = useState(dropdownOptions.map(() => ({})));
  const [filterDropdown, setFilterDropdown] = useState([]);
  const [titles, setTitles] = useState(dropdownOptions.map(option => option.value));
  const [selectedAttributes, setSelectedAttributes] = useState(dropdownOptions.map(option => option.id));
  const [tableData, setTableData] = useState([]);

  useEffect(() => {
    getBudgetInfo();
  }, []);

  const getBudgetInfo = async () => {
    try {
      const res = await axios.get('/budget-state-data');
      const result = res.data.result;
console.log(result)
      const processedChartData = processChartData(result);
      setChartData(processedChartData);
      setTableData(processTableData(result, 1));
    } catch (error) {
      console.log(error);
    }
  };

  const processChartData = (data) => {
    const labels = data.fundsAllocated.map(item => item.state_name);
    setFilterDropdown(labels);
    const fundsAllocatedData = data.fundsAllocated.map(item => item.funds_allocated);
    const publicExpenditureData = data.publicExpenditure.map(item => item.education_expenditure_percentage);
    const samagraSikshaFundsApprovedData = data.samagraSikshaFundsApproved.map(item => item.percentage_of_samagra_shiksha_funds_approved);
    const samagraSikshaFundsReceivedData = data.samagraSikshaFundsReceived.map(item => item.percentage_of_samagra_shiksha_funds_received);

    return dropdownOptions.map(option => ({
      [option.id]: {
        labels,
        datasets: [
          {
            label: option.value,
            type: 'bar',
            backgroundColor: 'rgba(185,102,220,1)',
            borderColor: 'rgba(185,102,220,1)',
            borderWidth: 2,
            data: option.id === 1 ? fundsAllocatedData
                : option.id === 2 ? publicExpenditureData
                : option.id === 3 ? samagraSikshaFundsApprovedData
                : samagraSikshaFundsReceivedData,
          }
        ]
      }
    }));
  };

  const processTableData = (data, attributeId) => {
    console.log(attributeId)
    console.log(data)
    const attributeData = data[Object.keys(data)[attributeId - 1]];
    console.log(attributeData)
    return attributeData.map(item => ({
     
      attributes: item.state_name,
      dateRange: "23/03/23-10/04/24", // Hardcoded or dummy value
      totalValue: attributeId === 1 ? item.funds_allocated
        : attributeId === 2 ? item.education_expenditure_percentage
        : attributeId === 3 ? item.percentage_of_samagra_shiksha_funds_approved
        : item.percentage_of_samagra_shiksha_funds_received,
      avgValue: (attributeId === 1 ? item.funds_allocated
        : attributeId === 2 ? item.education_expenditure_percentage
        : attributeId === 3 ? item.percentage_of_samagra_shiksha_funds_approved
        : item.percentage_of_samagra_shiksha_funds_received) / 10 
    }));
  };

  

  const handleCardFilterChange = async (attributeId, filterValue, cardIndex) => {
      const newTitle = dropdownOptions.find(option => option.id === attributeId).value;
    setTitles(prevTitles => {
      const newTitles = [...prevTitles];
      newTitles[cardIndex] = newTitle;
      return newTitles;
    });
    let payload = {
      attributeId: attributeId,
      stateName: filterValue
    };
    try {
      const res = await axios.post('/budget-filter', payload);
      const result = res.data.result;

      const filteredChartData = processChartDataForFilter(result, attributeId);
      setChartData(prevChartData => {
        const newChartData = [...prevChartData];
        newChartData[cardIndex] = { ...newChartData[cardIndex], [selectedAttributes[cardIndex]]: filteredChartData };
        return newChartData;
      });
    } catch (error) {
      console.log(error);
    }
  };
  const handleTableFilterChange = async (attributeId, filterValue, ) => {
    
  let payload = {
    attributeId: attributeId,
    stateName: filterValue
  };
  console.log(payload)
  try {
    const res = await axios.post('/budget-filter', payload);
    const result = res.data.result;
console.log(result)
const newTableData = processTableDataForFilter(result, attributeId);
console.log(newTableData)
setTableData(newTableData);
    
  } catch (error) {
    console.log(error);
  }
};

const processTableDataForFilter = (data,attributeId)=> {
  console.log(data)
  console.log(attributeId)
  let newTableData = [...tableData];
  return data.map(item => ({
     
    attributes: item.state_name,
    dateRange: "23/03/23-10/04/24", // Hardcoded or dummy value
    totalValue: attributeId === 1 ? item.funds_allocated
      : attributeId === 2 ? item.education_expenditure_percentage
      : attributeId === 3 ? item.percentage_of_samagra_shiksha_funds_approved
      : item.percentage_of_samagra_shiksha_funds_received,
    avgValue: (attributeId === 1 ? item.funds_allocated
      : attributeId === 2 ? item.education_expenditure_percentage
      : attributeId === 3 ? item.percentage_of_samagra_shiksha_funds_approved
      : item.percentage_of_samagra_shiksha_funds_received) / 10 
  }));

}

  const processChartDataForFilter = (data, attributeId) => {
    const labels = data.map(item => item.state_name);
    const dataValues = data.map(item => {
      switch (attributeId) {
        case 1:
          return item.funds_allocated;
        case 2:
          return item.education_expenditure_percentage;
        case 3:
          return item.percentage_of_samagra_shiksha_funds_approved;
        case 4:
          return item.percentage_of_samagra_shiksha_funds_received;
        default:
          return 0;
      }
    });

    return {
      labels,
      datasets: [
        {
          label: dropdownOptions.find(option => option.id === attributeId).value,
          type: 'bar',
          backgroundColor: 'rgba(185,102,220,1)',
          borderColor: 'rgba(185,102,220,1)',
          borderWidth: 2,
          data: dataValues,
        }
      ]
    };
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
              chartData={chartData[index][selectedAttributes[index]] || {}} 
              // onAttributeChange={(attributeId) => handleAttributeChange(attributeId, index)}
              filterDropdowns={filterDropdown}
              onCardFilterChange={(attributeId, filterValue) => handleCardFilterChange(attributeId, filterValue, index)}
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <BudgetTableComponent
            dropdownOptions={dropdownOptions} 
            attributeBasedDropdowns={attributeBasedDropdowns}  
            tableInfo={tableData} 
            tableHeadings={tableHeadings} 
            filterDropdowns={filterDropdown}
            onTableFilterChange={(attributeId, filterValue) => handleTableFilterChange(attributeId, filterValue)}
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default BudgetState;
