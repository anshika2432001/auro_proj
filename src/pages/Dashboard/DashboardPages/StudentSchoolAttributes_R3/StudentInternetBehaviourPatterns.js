import React, { useEffect, useState } from 'react';
import axios from '../../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../../components/CardComponent';
import TableComponent from '../../components/TableComponent';


const dropdownOptions = [
  { id: 1, value: 'Student hours spent on mobile phones - social/entertainment' },
  { id: 2, value: 'Children having access to digital devices at home' },
  { id: 3, value: 'Students using learning apps at home' },
  { id: 4, value: 'Edtech Product Type' },
  { id: 5, value: 'Students who have one or more social media accounts' },
  { id: 6, value: 'Types of sites' },
  { id: 7, value: 'Student hours spent on mobile phones - study' },
];

const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

const endpointMapping = {
  
  2: '/r3/student-access-digital-devices-home',
  3: '/r3/student-using-learning-apps-home',
  7: '/r3/student-hours-spend-mobile-study',
 
};
const defaultChartData = {
  labels: [],
  datasets: [
    {
      label: 'No of Students (Purple)',
      type: 'bar',
      backgroundColor: 'rgba(185,102,220,1)',
      borderColor: 'rgba(185,102,220,1)',
      borderWidth: 2,
      data: [],
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
      data: [],
      barThickness: 30,
      order: 2,
    },
    {
      label: 'Average score (Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average score (Blue)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
  ],
};
const tableHeadings = [
  
  'Number of Students', 
  'Average Score of Students',
  'Number of Students', 
  'Average Score of Students'
];

const StudentInternetBehaviourPatterns = () => {
  const [filters, setFilters] = useState({
    1: {},
    2: {},
    3: {},
    4: {},
  });
  const [cardData, setCardData] = useState({
    1: defaultChartData,
    2: defaultChartData,
    3: defaultChartData,
    4: defaultChartData,
  });

  const [cardTitle, setCardTitle] = useState({
    1: dropdownOptions[0],
    2: dropdownOptions[1],
    3: dropdownOptions[2],
    4: dropdownOptions[3],
  });

  const [tableData, setTableData] = useState({
    0: []
});
  

  useEffect(() => {
    fetchDataForAllCards();
    fetchTableData();
  }, []);

  const fetchDataForAllCards = () => {
    let count =0;
    const slicedOptions = dropdownOptions.slice(0, 4);
  console.log("Sliced Options: ", slicedOptions);

    dropdownOptions.slice(0, 4).forEach(option => {
      // count=count+1;
      // alert(count)
      fetchData(option.id, filters[option.id],option.id);
      // console.log(count)
    });
  };
  const fetchTableData = () => {
    const firstOption = dropdownOptions[0];
    fetchData(firstOption.id, filters[firstOption.id], 0);
  };

  const fetchData = async (key, value,cardKey) => {
    console.log(key)
    const endpoint = endpointMapping[key];

    try {
      let payload = {
        transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1 : null) : null,
        transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1 : null) : null,
        transactionDateFrom2: value ? (value.startDateRange2 ? value.startDateRange2 : null) : null,
        transactionDateTo2: value ? (value.endDateRange2 ? value.endDateRange2 : null) : null,
        grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
        subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
        schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
        stateId: value ? ((value.State && value.State !== "All") ? value.State : null) : null,
        districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
        socialGroup: value ? ((value['Social Group'] && value['Social Group'] !== 'All') ? value['Social Group'] : null) : null,
        gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
        ageFrom: null,
        ageTo: null,
        educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
        schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
        cwsn: null,
        childMotherQualification: value ? ((value['Mother Education'] && value['Mother Education'] !== 'All') ? value['Mother Education'] : null) : null,
        childFatherQualification: value ? ((value['Father Education'] && value['Father Education'] !== 'All') ? value['Father Education'] : null) : null,
      };

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }

      const res = await axios.post(endpoint, payload);
      const result = res.data.result;

      const [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData] = parseResultData(key, result);

      setCardData(prevCardData => ({
        ...prevCardData,
        [cardKey]: {
          labels: labelsData,
          datasets: createDatasets(dataOne, dataTwo, dataOneAvg, dataTwoAvg),
        }
      }));

      if (cardKey === 0) {
        setTableData(prevData => ({
          ...prevData,
          [cardKey]: newTableData,
        }));
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const parseResultData = (key, result) => {
    const mappings = {
      2: { key: 'mobile_access', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score' },
      3: { key: 'remote_learning', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score' },
      7: { key: 'mobile_hours_study', dataOneKey: 'num_students', dataTwoKey: 'num_students', avgKey: 'avg_score' },
     
      
    };

    const { key: labelKey, dataOneKey, dataTwoKey, avgKey } = mappings[key];
    const allLabels = new Set([
      ...result.dataOne.map(item => item[labelKey]),
      ...result.dataTwo.map(item => item[labelKey])
    ]);
  
    const labelsData = Array.from(allLabels);
    const dataOne = labelsData.map(label => result.dataOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0);
    const dataOneAvg = labelsData.map(label => result.dataOne.find(item => item[labelKey] === label)?.[avgKey] || 0);
    const dataTwo = labelsData.map(label => result.dataTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0);
    const dataTwoAvg = labelsData.map(label => result.dataTwo.find(item => item[labelKey] === label)?.[avgKey] || 0);
  
    const newTableData = labelsData.map(label => ({
      attributes: label,
      dateRange1TotalValue: result.dataOne.find(item => item[labelKey] === label)?.[dataOneKey] || 0,
      dateRange1AvgValue: result.dataOne.find(item => item[labelKey] === label)?.[avgKey] || 0,
      dateRange2TotalValue: result.dataTwo.find(item => item[labelKey] === label)?.[dataTwoKey] || 0,
      dateRange2AvgValue: result.dataTwo.find(item => item[labelKey] === label)?.[avgKey] || 0,
    }));
  
    return [labelsData, dataOne, dataOneAvg, dataTwo, dataTwoAvg, newTableData];
    
  };

  const createDatasets = (dataOne, dataTwo, dataOneAvg, dataTwoAvg) => [
    { ...defaultChartData.datasets[0], data: dataOne },
    { ...defaultChartData.datasets[1], data: dataTwo },
    { ...defaultChartData.datasets[2], data: dataOneAvg },
    { ...defaultChartData.datasets[3], data: dataTwoAvg },
  ];

  const onFilterChange = (key, value,cardKey) => {
    console.log(key)
    console.log(value)
    setFilters(prevFilters => ({
      ...prevFilters,
      [key]: value,
    }));
    const selectedOption = dropdownOptions.find(option => option.id === key);

    setCardTitle(prevData => ({
      ...prevData,
      [cardKey]: selectedOption,
    }));
    fetchData(key, value,cardKey);
  };

    return (
    <div>
      <h2>Student R2 Attributes</h2>
      <Grid container spacing={2}>
      {dropdownOptions.slice(0, 4).map((option, index) => (
          <Grid item xs={12} sm={6} md={6} lg={6} key={option.id}>
            <CardComponent 
             key={option.id}
             
           
             title={cardTitle[option.id]} 
             dropdownOptions={dropdownOptions} 
             attributeBasedDropdowns={attributeBasedDropdowns} 
             chartData={cardData[option.id] || defaultChartData}
             onFilterChange={onFilterChange}
             cardKey={option.id}
              
            />
          </Grid>
        ))}
        <Grid item xs={12}>
          <TableComponent 
          dropdownOptions={dropdownOptions} 
          attributeBasedDropdowns={attributeBasedDropdowns} 
          tableInfo={tableData[0]} 
          tableHeadings={tableHeadings} 
          onFilterChange={onFilterChange}
          tableKey={0}
          />
        </Grid>
      </Grid>
    </div>
  );
};

export default StudentInternetBehaviourPatterns;
