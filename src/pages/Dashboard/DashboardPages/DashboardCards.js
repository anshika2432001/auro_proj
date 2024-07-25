import React, { useEffect, useState } from 'react';
import { Typography, Card, CardContent, Grid, Autocomplete, TextField, IconButton } from '@mui/material';
import { makeStyles } from '@mui/styles';
import axios from '../../../utils/axios';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from 'dayjs';
import DashboardCardComponent from '../components/DashboardCardComponent';

const useStyles = makeStyles({

  
  
  content: {
    paddingTop: '10px',
    height: '300px',
    overflowY: 'scroll',
  },
 
});

const dropdownOptions = [
  { id: 1, value: 'Number of Students' },
  { id: 2, value: 'Number of Schools' },
  { id: 3, value: 'Number of Teachers' },
  { id: 4, value: 'Number of Parents' },

]

const attributeBasedDropdowns = {
  1: ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'],
  2: ['Board of Education', 'School Location', 'School Management', 'School Category', 'School Type', ],
  3: ['Board of Education', 'School Location', 'School Management', 'School Category', 'School Type','Qualification','Mode of Employment','Gender'],
  4: ['State', 'District', 'School', 'Grade', 'Social Group', 'Gender', 'Annual Income', 'Subject', 'Mother Education', 'Father Education', 'Age Group', 'CWSN', 'Board of Education', 'School Location', 'School Management', 'School Category', 'School Type'],
};

const endpointMapping = {
  1: '/dashboard/student-count',
  2: '/dashboard/school-count',
  3: '/dashboard/teacher-count',
  4: '/dashboard/parent-count',
}

const colors = [
  'rgba(255,99,132,1)', 'rgba(255,159,64,1)', 'rgba(255,205,86,1)', 'rgba(75,192,192,1)', 'rgba(54,162,235,1)', 'rgba(153,102,255,1)', 'rgba(201,203,207,1)',
];








const DashboardCards = () => {
  const defaultDateRange1Start = dayjs('2024-01-01');
  const defaultDateRange1End = dayjs('2024-01-31');

  let defaultStartDateRange1= defaultDateRange1Start.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
  let defaultEndDateRange1= defaultDateRange1End.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');
 
  const [loading,setLoading]=useState({
    1: false,
    2: false,
    3: false,
    4: false,
  });

  const [filters, setFilters] = useState({
    1: {},
    2: {},
    3: {},
    4: {},
  });
  const [cardTitle, setCardTitle] = useState({
    1: dropdownOptions[0],
    2: dropdownOptions[1],
    3: dropdownOptions[2],
    4: dropdownOptions[3],
  });

  const [cardData, setCardData] = useState({
    1: [],
    2: [],
    3: [],
    4: [],
  });

  useEffect(() => {
    // getDashboardInfo();
    fetchData(1, filters[1],1);
    fetchData(2, filters[2],2);
  fetchData(3, filters[3],3);
  fetchData(4, filters[4],4);
  }, []);


  const fetchData = async (key, value,cardKey) => {
    setLoading(prevValue => ({
      ...prevValue,
      [cardKey]: true,
    }));
    
    const endpoint = endpointMapping[key];

    try {
      let payload = {
        transactionDateFrom1: value ? (value.startDateRange1 ? value.startDateRange1 : defaultStartDateRange1) : defaultStartDateRange1,
        transactionDateTo1: value ? (value.endDateRange1 ? value.endDateRange1 : defaultEndDateRange1) : defaultEndDateRange1,
        transactionDateFrom2: null,
        transactionDateTo2: null,
        grades: value ? ((value.Grade && value.Grade !== 'All') ? value.Grade : null) : null,
        subject: value ? ((value.Subject && value.Subject !== 'All') ? value.Subject : null) : null,
        schoolLocation: value ? ((value['School Location'] && value['School Location'] !== 'All') ? value['School Location'] : null) : null,
        stateId: value ? ((value.State && value.State !== "All") ? value.State :  null) :  null,
        districtId: value ? ((value.District && value.District !== "All") ? value.District : null) : null,
        socialGroup: value ? ((value['Social Group'] && value['Social Group'] !== 'All') ? value['Social Group'] : null) : null,
        gender: value ? ((value.Gender && value.Gender !== 'All') ? value.Gender : null) : null,
        ageFrom: null,
        ageTo: null,
        educationBoard: value ? ((value['Board of Education'] && value['Board of Education'] !== 'All') ? value['Board of Education'] : null) : null,
        schoolManagement: value ? ((value['School Management'] && value['School Management'] !== 'All') ? value['School Management'] : null) : null,
        cwsn: value ? ((value['CWSN'] && value['CWSN'] !== 'All') ? value['CWSN'] : null) : null,
        childMotherQualification: value ? ((value['Mother Education'] && value['Mother Education'] !== 'All') ? value['Mother Education'] : null) : null,
        childFatherQualification: value ? ((value['Father Education'] && value['Father Education'] !== 'All') ? value['Father Education'] : null) : null,
        householdId: value ? ((value['Annual Income'] && value['Annual Income'] !== 'All') ? value['Annual Income'] : null) : null,
      };

      if (value && value['Age Group'] && value['Age Group'] !== 'All') {
        const ageRange = value['Age Group'].split('-');
        payload.ageFrom = ageRange[0] ? parseInt(ageRange[0], 10) : null;
        payload.ageTo = ageRange[1] ? parseInt(ageRange[1], 10) : null;
      }

      if(cardKey == 2){
        const res = await axios.get(endpoint);
       
        if(res.data.status && res.data.statusCode == 200){
         setLoading(prevValue => ({
           ...prevValue,
           [cardKey]: false,
         }));
           const result = res.data.result;
           const parsedData = parseResultData(key, result);
               setCardData(prevCardData => ({
               ...prevCardData,
                [cardKey]: parsedData,
               
         }));

      }
      else{
        console.log("error")
      }

      }
      else{
        const res = await axios.post(endpoint, payload);
       
        if(res.data.status && res.data.statusCode == 200){
         setLoading(prevValue => ({
           ...prevValue,
           [cardKey]: false,
         }));
           const result = res.data.result;
           const parsedData = parseResultData(key, result);
               setCardData(prevCardData => ({
               ...prevCardData,
                [cardKey]: parsedData,
               
         }));

      }
      else{
        console.log("error")
      }
    }
     

     
      
      

        
    
    
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const parseResultData = (key, result) => {
    const mappings = {
      1: { key: 'state_name', dataOneKey: 'num_students' },
      2: { key: 'state_name', dataOneKey: 'num_schools',  },
      3: { key: 'state_name', dataOneKey: 'num_teachers',},
      4: { key: 'state_name', dataOneKey: 'num_parents',  },
     
    };

    const { key: labelKey, dataOneKey} = mappings[key];
    
    const capitalizeWords = (str) => {
      return str.replace(/\w\S*/g, (word) => word.charAt(0).toUpperCase() + word.substr(1).toLowerCase());
    };

    const labelsData = result.dataOne.map((item, index) => ({
      state: capitalizeWords(item[labelKey]),
      [dataOneKey]: item[dataOneKey],
      fill: colors[index % colors.length]
    }));
  
    return labelsData;
  };

  
  const onFilterChange = (key, value,cardKey) => {
    
    setFilters(prevFilters => ({
      ...prevFilters,
      [key]: value,
    }));
    const selectedOption = dropdownOptions.find(option => option.id === key);
console.log(selectedOption)
    setCardTitle(prevData => ({
      ...prevData,
      [cardKey]: selectedOption,
    }));
     fetchData(key, value,cardKey);
  };

  console.log(cardData)


  return (
    <div>
      <Grid container spacing={2}>
      {dropdownOptions.slice(0, 4).map((option, index) => (
        
          <Grid item xs={12} sm={6} md={6} lg={6} key={option.id}>
            <DashboardCardComponent 
             key={option.id}
             
           
             title={cardTitle[option.id]} 
             
             attributeBasedDropdowns={attributeBasedDropdowns} 
             chartData={cardData[option.id] || []}
             onFilterChange={onFilterChange}
             cardKey={option.id}
             loadingStatus={loading[option.id]}
             
              
            />
          </Grid>
         
        ))}
        </Grid>
    </div>
  );
};

export default DashboardCards;
