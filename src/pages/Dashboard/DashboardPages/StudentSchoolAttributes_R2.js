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

const commonAttributes = ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'];

const attributeBasedDropdowns = {};
dropdownOptions.forEach(option => {
  attributeBasedDropdowns[option.id] = commonAttributes;
});

const endpointMapping = {
  
  2: '/r2/student-strength-classroom',
  4: '/r2/academic-streams',
  5: '/r2/student-access-bank',
  6: '/r2/student-engagement-activities-school',
  8: '/r2/social-group',
  9: '/r2/student-leadership-position-school-clubs',
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



const StudentSchoolAttributes_R2 = () => {
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

      let labelsData = [];
      let dataOne = [];
      let dataOneAvg = [];
      let dataTwo = [];
      let dataTwoAvg = [];
      let newTableData = [];

      switch (key) {
        
        case 2:
          labelsData = result.dataOne.map(item => item.classroom_strength);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);

          newTableData = result.dataOne.map(item => ({
            attributes:  item.classroom_strength,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.classroom_strength === item.classroom_strength)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.classroom_strength === item.classroom_strength)?.avg_score || 0
          }));
          break;
        
        case 4:
          labelsData = result.dataOne.map(item => item.academic_stream);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: item.academic_stream,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.academic_stream === item.academic_stream)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.academic_stream === item.academic_stream)?.avg_score || 0
          }));
          break;
        case 5:
          labelsData = result.dataOne.map(item => item.student_access_to_bank);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: item.student_access_to_bank,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.student_access_to_bank === item.student_access_to_bank)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.student_access_to_bank === item.student_access_to_bank)?.avg_score || 0
          }));
          break;
        case 6:
          labelsData = result.dataOne.map(item => item.extra_curricular_activity);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: item.extra_curricular_activity,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.extra_curricular_activity === item.extra_curricular_activity)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.extra_curricular_activity === item.extra_curricular_activity)?.avg_score || 0
          }));
          break;
       
          
          
          case 8:
          labelsData = result.dataOne.map(item => item.social_group);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: item.social_group,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.social_group === item.social_group)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.social_group === item.social_group)?.avg_score || 0
          }));
          break;
          case 9:
          labelsData = result.dataOne.map(item => item.is_student_in_leadership_position);
          dataOne = result.dataOne.map(item => item.num_students);
          dataOneAvg = result.dataOne.map(item => item.avg_score);
          dataTwo = result.dataTwo.map(item => item.num_students);
          dataTwoAvg = result.dataTwo.map(item => item.avg_score);
          newTableData = result.dataOne.map(item => ({
            attributes: item.is_student_in_leadership_position,
            dateRange1TotalValue: item.num_students,
            dateRange1AvgValue: item.avg_score,
            dateRange2TotalValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.num_students || 0,
            dateRange2AvgValue: result.dataTwo.find(i => i.quiz_attempt === item.quiz_attempt)?.avg_score || 0
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

export default StudentSchoolAttributes_R2;