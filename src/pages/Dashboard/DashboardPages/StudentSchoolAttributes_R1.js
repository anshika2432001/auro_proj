

import React, { useEffect, useState } from 'react';
import axios from '../../../utils/axios';
import { Grid } from '@mui/material';
import CardComponent from '../components/CardComponent';
import TableComponent from '../components/TableComponent';

let invocationCount = 0;

const dropdownOptions = [
  { id: 1, value: 'Subject Wise Breakdown - Average Score' },
  { id: 2, value: 'Gradewise - Average Score' },
  { id: 3, value: 'Microscholarship Quizzes - Average Score' },
  { id: 4, value: 'Total Quiz Attempted' },
  { id: 5, value: 'Topic wise breakdown - Average Score' },
  { id: 6, value: 'Topic wise breakdown - No. of Microscholarship Quizzes' },
  { id: 7, value: 'Top Performing Topics' },
  { id: 8, value: 'Weak Performing Topics' },
  { id: 9, value: 'Core-Retake- ( No of Students)' },
  { id: 10, value: 'Core-Retake- (Average Score)' },
  { id: 11, value: 'Subject Wise Breakdown - % Improvement' },
  { id: 12, value: 'Grade wise- % Improvement Score' },
  { id: 13, value: 'Topic wise breakdown - %Improvement' },
  { id: 15, value: 'Topic wise breakdown - Student Attempts' }
];

const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

const endpointMapping = {
  1: '/r1/subject-wise-breakdown',
  2: '/r1/grade-wise-avg-score',
  3: '/r1/micro-scholar-quiz',
  4: '/r1/total-quiz-attempted',
  5: '/r1/topic-wise-avg-score',
  6: '/r1/topic-wise-micro-scholar-quiz',
  7: '/r1/top-performing-topics',
  8: '/r1/weak-performing-topics',
  9: '/r1/core-retake-practice',
  10: '/r1/core-retake-improvement',
  11: '/r1/subject-wise-breakdown-improve',
  12: '/r1/grade-wise-breakdown-improve',
  13: '/r1/topic-wise-breakdown-improve',
  15: '/r1/topic-wise-student-attempts',
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
      label: 'Average no. of students (Purple)',
      type: 'line',
      borderColor: 'rgba(177,185,192,1)',
      borderWidth: 4,
      fill: false,
      data: [],
      spanGaps: true,
      order: 1,
    },
    {
      label: 'Average no. of students (Blue)',
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

const StudentSchoolAttributes_R1 = () => {
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
      };

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }

      const res = await axios.post(endpoint, payload);
      const result = res.data.result;

      let labelsData = [];
      let dataOne = [];
      let dataOneAvg = [];
      let dataTwo = [];
      let dataTwoAvg = [];
      let newTableData = [];

      switch (key) {
        case 1:
          labelsData = result.dataOne.map(item => item.subject);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.average_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.average_score);
          
          newTableData = result.dataOne.map(item => ({
            attributes: item.subject,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.average_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.subject === item.subject)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.subject === item.subject)?.average_score || 0
          }));
          break;
        case 2:
          labelsData = result.dataOne.map(item => `Grade ${item.grade}`);
          dataOne = result.dataOne.map(item => item.num_students_attempting);
          dataOneAvg = result.dataOne.map(item => item.average_score);
          dataTwo = result.dataTwo.map(item => item.num_students_attempting);
          dataTwoAvg = result.dataTwo.map(item => item.average_score);

          newTableData = result.dataOne.map(item => ({
            attributes:  `Grade ${item.grade}`,
            dateRange1TotalValue: item.num_students_attempting,
            dateRange1AvgValue: item.average_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.grade === item.grade)?.num_students_attempting || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.grade === item.grade)?.average_score || 0
          }));
          break;
        case 3:
          labelsData = result.dataOne.map(item => `Quiz Range ${item.quiz_range}`);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.average_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.average_score);
          newTableData = result.dataOne.map(item => ({
            attributes: `Quiz Range ${item.quiz_range}`,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.average_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.quiz_range === item.quiz_range)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.quiz_range === item.quiz_range)?.average_score || 0
          }));
          break;
        case 4:
          labelsData = result.dataOne.map(item => `Quiz Range ${item.quiz_range}`);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.national_avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.national_avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: `Quiz Range ${item.quiz_range}`,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.national_avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.quiz_range === item.quiz_range)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.quiz_range === item.quiz_range)?.national_avg_score || 0
          }));
          break;
        case 5:
        case 6:
          labelsData = result.dataOne.map(item => item.topic_name);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score_pan_india);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score_pan_india);
          newTableData = result.dataOne.map(item => ({
            attributes: item.topic_name,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score_pan_india,
            dateRange2TotalValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.avg_score_pan_india || 0
          }));
          break;
       
          
          case 7:
          case 8:
          labelsData = result.dataOne.map(item => item.topic_name);
          dataOne = result.dataOne.map(item => item.num_students_nation);
          dataOneAvg = result.dataOne.map(item => item.avg_score_nation);
          dataTwo = result.dataTwo.map(item => item.num_students_nation);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score_nation);
          newTableData = result.dataOne.map(item => ({
            attributes: item.topic_name,
            dateRange1TotalValue: item.num_students_nation,
            dateRange1AvgValue: item.avg_score_nation,
            dateRange2TotalValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.num_students_nation || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.avg_score_nation || 0
          }));
          break;
          case 9:
          labelsData = result.dataOne.map(item => item.quiz_attempt);
          dataOne = result.dataOne.map(item => item.num_students_pan_india);
          dataOneAvg = result.dataOne.map(item => item.avg_score_pan_india);
          dataTwo = result.dataTwo.map(item => item.num_students_pan_india);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score_pan_india);
          newTableData = result.dataOne.map(item => ({
            attributes: item.quiz_attempt,
            dateRange1TotalValue: item.num_students_pan_india,
            dateRange1AvgValue: item.avg_score_pan_india,
            dateRange2TotalValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.num_students_pan_india || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.avg_score_pan_india || 0
          }));
          break;
          case 10:
            labelsData = result.dataOne.map(item => item.quiz_attempt);
            dataOne = result.dataOne.map(item => item.num_students);
            dataOneAvg = result.dataOne.map(item => item.avg_improvement_nation);
            dataTwo = result.dataTwo.map(item => item.num_students);
            dataTwoAvg = result.dataTwo.map(item => item.avg_improvement_nation);
            newTableData = result.dataOne.map(item => ({
              attributes: item.quiz_attempt,
              dateRange1TotalValue: item.num_students,
              dateRange1AvgValue: item.avg_improvement_nation,
              dateRange2TotalValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.num_students || 0,
              dateRange2AvgValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.avg_improvement_nation || 0
            }));
          break;
          case 11:
            labelsData = result.dataOne.map(item => item.subject);
            dataOne = result.dataOne.map(item => item.num_students_nation);
            dataOneAvg = result.dataOne.map(item => item.percent_improvement_second_nation);
            dataTwo = result.dataTwo.map(item => item.num_students_nation);
            dataTwoAvg = result.dataTwo.map(item => item.percent_improvement_second_nation);
            newTableData = result.dataOne.map(item => ({
              attributes: item.subject,
              dateRange1TotalValue: item.num_students_nation,
              dateRange1AvgValue: item.percent_improvement_second_nation,
              dateRange2TotalValue: result.dataTwo.find(i => i.subject === item.subject)?.num_students_nation || 0,
              dateRange2AvgValue: result.dataTwo.find(i => i.subject === item.subject)?.percent_improvement_second_nation || 0
            }));
          break;
          case 12:
            labelsData = result.dataOne.map(item => item.grade);
            dataOne = result.dataOne.map(item => item.num_students_nation);
            dataOneAvg = result.dataOne.map(item => item.percent_improvement_second_nation);
            dataTwo = result.dataTwo.map(item => item.num_students_nation);
            dataTwoAvg = result.dataTwo.map(item => item.percent_improvement_second_nation);
            newTableData = result.dataOne.map(item => ({
              attributes: item.grade,
              dateRange1TotalValue: item.num_students_nation,
              dateRange1AvgValue: item.percent_improvement_second_nation,
              dateRange2TotalValue: result.dataTwo.find(i => i.grade === item.grade)?.num_students_nation || 0,
              dateRange2AvgValue: result.dataTwo.find(i => i.grade === item.grade)?.percent_improvement_second_nation || 0
            }));
          break;
          case 13:
            labelsData = result.dataOne.map(item => item.topic_name);
            dataOne = result.dataOne.map(item => item.num_students_nation);
            dataOneAvg = result.dataOne.map(item => item.percent_improvement_second_nation);
            dataTwo = result.dataTwo.map(item => item.num_students_nation);
            dataTwoAvg = result.dataTwo.map(item => item.percent_improvement_second_nation);
            newTableData = result.dataOne.map(item => ({
              attributes: item.topic_name,
              dateRange1TotalValue: item.num_students_nation,
              dateRange1AvgValue: item.percent_improvement_second_nation,
              dateRange2TotalValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.num_students_nation || 0,
              dateRange2AvgValue: result.dataTwo.find(i => i.subject === item.subject)?.percent_improvement_second_nation || 0
            }));
          break;
          case 15:
            labelsData = result.dataOne.map(item => item.topic_name);
            dataOne = result.dataOne.map(item => item.num_students_nation);
            dataOneAvg = result.dataOne.map(item => item.percent_improvement_second_nation);
            dataTwo = result.dataTwo.map(item => item.num_students_nation);
            dataTwoAvg = result.dataTwo.map(item => item.percent_improvement_second_nation);
            newTableData = result.dataOne.map(item => ({
              attributes: item.topic_name,
              dateRange1TotalValue: item.num_students_nation,
              dateRange1AvgValue: item.percent_improvement_second_nation,
              dateRange2TotalValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.num_students_nation || 0,
              dateRange2AvgValue: result.dataTwo.find(i => i.topic_name === item.topic_name)?.percent_improvement_second_nation || 0
            }));
          break;
          
        
        default:
          break;
      }

      setCardData(prevCardData => ({
        ...prevCardData,
        [cardKey]: {
          labels: labelsData,
          datasets: [
            {
              label: 'No of Students (Purple)',
              type: 'bar',
              backgroundColor: 'rgba(185,102,220,1)',
              borderColor: 'rgba(185,102,220,1)',
              borderWidth: 2,
              data: dataOne,
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
              data: dataTwo,
              barThickness: 30,
              order: 2,
            },
            {
              label: 'Average score (Purple)',
              type: 'line',
              borderColor: 'rgba(177,185,192,1)',
              borderWidth: 4,
              fill: false,
              data: dataOneAvg,
              spanGaps: true,
              order: 1,
            },
            {
              label: 'Average score (Blue)',
              type: 'line',
              borderColor: 'rgba(177,185,192,1)',
              borderWidth: 4,
              fill: false,
              data: dataTwoAvg,
              spanGaps: true,
              order: 1,
            },
          ],
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

  console.log(tableData)
  console.log(cardData)
    return (
    <div>
      <h2>Student R1 Attributes</h2>
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

export default StudentSchoolAttributes_R1;
