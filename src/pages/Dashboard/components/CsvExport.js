// CsvData.js
export const getCsvHeaders = (title, category, cardKey) => {
    if(cardKey == 4){
        if (category === "Teachers" || category === "Parents") {
            return [
              { label: '', key: 'attributes' },
              { label: '', key: 'dateRange1TotalValue' },
              { label: '', key: 'dateRange1StudentValue' },
              { label: '', key: 'dateRange1AvgValue' },
              { label: '', key: 'dateRange2TotalValue' },
              { label: '', key: 'dateRange2StudentValue' },
              { label: '', key: 'dateRange2AvgValue' }
            ];
          } else {
            return [
              { label: '', key: 'attributes' },
              { label: '', key: 'dateRange1TotalValue' },
              { label: '', key: 'dateRange1AvgValue' },
              { label: '', key: 'dateRange2TotalValue' },
              { label: '', key: 'dateRange2AvgValue' }
            ];
          }

    }else{
        if (category === "Teachers" || category === "Parents") {
            return [
              { label: '', key: 'attributes' },
              { label: '', key: 'dateRange1TotalValue' },
              { label: '', key: 'dateRange1StudentValue' },
              { label: '', key: 'dateRange1AvgValue' },
              { label: '', key: 'dateRange2TotalValue' },
              { label: '', key: 'dateRange2StudentValue' },
              { label: '', key: 'dateRange2AvgValue' }
            ];
          } else {
            return [
              { label: '', key: 'stateDataValue' },
              { label: '', key: 'districtDataValue' },
              { label: '', key: 'attributes' },
              { label: '', key: 'dateRange1TotalValue' },
              { label: '', key: 'dateRange1AvgValue' },
              { label: '', key: 'dateRange2TotalValue' },
              { label: '', key: 'dateRange2AvgValue' }
            ];
          } 

    }
    
  };
  
  export const getCsvDataRows = (title,selectedFilters,attributeOptions, category, tableInfo,attributeHeading,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,cardKey) => {
    console.log(tableInfo)
    const selectedFiltersWithNames = Object.fromEntries(
        Object.entries(selectedFilters).map(([key, value]) => {
         
            const options = attributeOptions[key];
          const selectedOption = options.find(option => (typeof option === 'object' ? option.id === value : option === value));
          return [key, typeof selectedOption === 'object' ? selectedOption.name : selectedOption];

        
          
        })
      );
      const filteredSelectedFiltersWithNames = Object.fromEntries(
        Object.entries(selectedFiltersWithNames).filter(([, value]) => value !== undefined)
      );
      const filterRows = Object.entries(filteredSelectedFiltersWithNames).map(([key, value]) => {
        return { attributes:  `${key}: ${value}`, dateRange1TotalValue: '', dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
        
      });
    
    let titleRow = {};
    let emptyRow = {};
    let headerValue = {};
    let emptyRow1 = {};
    let dateRangeRow = {};
    if(cardKey == 4){
        if (category === "Teachers" || category === "Parents") {
            titleRow = { attributes: title.value, dateRange1TotalValue: '', dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
            dateRangeRow = { attributes: 'Date Range 1', dateRange1TotalValue: `${dateRange1Start.format("DD/MM/YYYY")}-${dateRange1End.format("DD/MM/YYYY")}`, dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
            emptyRow = { attributes: '', dateRange1TotalValue: '', dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
            headerValue = {attributes: `${attributeHeading}`, dateRange1TotalValue: 'State Total Stakeholder Value', dateRange1StudentValue: 'State Total Students Value', dateRange1AvgValue: 'State Avg Students Value', dateRange2TotalValue: 'Pan India Total Stakeholder Value', dateRange2StudentValue: 'Pan India Total Students Value', dateRange2AvgValue: 'Pan India Avg Students Value'}
            
        }
       else{
            titleRow = { attributes: title.value, dateRange1TotalValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
            dateRangeRow = { attributes: 'Date Range 1', dateRange1TotalValue: `${dateRange1Start.format("DD/MM/YYYY")}-${dateRange1End.format("DD/MM/YYYY")}`,  dateRange1AvgValue: '', dateRange2TotalValue:'' , dateRange2StudentValue: '', dateRange2AvgValue: '' };
            emptyRow = { attributes: '', dateRange1TotalValue: '',  dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
            headerValue = {attributes: `${attributeHeading}`, dateRange1TotalValue: 'State Total Stakeholder Value',  dateRange1AvgValue: 'State Avg Students Value', dateRange2TotalValue: 'Pan India Total Stakeholder Value',  dateRange2AvgValue: 'Pan India Avg Students Value'}
    
        }

    }
    else{
        if (category === "Teachers" || category === "Parents") {
            titleRow = { attributes: title.value, dateRange1TotalValue: '', dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
            dateRangeRow = { attributes: 'Date Range 1', dateRange1TotalValue: `${dateRange1Start.format("DD/MM/YYYY")}-${dateRange1End.format("DD/MM/YYYY")}`, dateRange1StudentValue: '', dateRange1AvgValue: 'Date Range 2', dateRange2TotalValue: `${dateRange2Start.format("DD/MM/YYYY")}-${dateRange2End.format("DD/MM/YYYY")}`, dateRange2StudentValue: '', dateRange2AvgValue: '' };
            emptyRow = { attributes: '', dateRange1TotalValue: '', dateRange1StudentValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2StudentValue: '', dateRange2AvgValue: '' };
            headerValue = {attributes: `${attributeHeading}`, dateRange1TotalValue: 'Date Range 1 Total Stakeholder Value', dateRange1StudentValue: 'Date Range 1 Total Students Value', dateRange1AvgValue: 'Date Range 1 Avg Students Value', dateRange2TotalValue: 'Date Range 2 Total Stakeholder Value', dateRange2StudentValue: 'Date Range 2 Total Students Value', dateRange2AvgValue: 'Date Range 2 Avg Students Value'}
            
        }
        else{
            titleRow = { stateDataValue: title.value,districtDataValue: '',attributes: '', dateRange1TotalValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
            dateRangeRow = {stateDataValue:'Date Range 1',districtDataValue:`${dateRange1Start.format("DD/MM/YYYY")}-${dateRange1End.format("DD/MM/YYYY")}`, attributes: '', dateRange1TotalValue: 'Date Range 2',  dateRange1AvgValue: `${dateRange2Start.format("DD/MM/YYYY")}-${dateRange2End.format("DD/MM/YYYY")}`, dateRange2TotalValue:'' , dateRange2StudentValue: '', dateRange2AvgValue: '' };
            emptyRow = {stateDataValue:'',districtDataValue:'', attributes: '', dateRange1TotalValue: '',  dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
            headerValue = {stateDataValue: 'State',districtDataValue: 'District',attributes: `${attributeHeading}`, dateRange1TotalValue: 'Date Range 1 Total Stakeholder Value',  dateRange1AvgValue: 'Date Range 1 Avg Students Value', dateRange2TotalValue: 'Date Range 2 Total Stakeholder Value',  dateRange2AvgValue: 'Date Range 2 Avg Students Value'}
    
        }

    }
    
    
    
    // Generate the data rows
    if(cardKey == 4){
        const dataRows = tableInfo.map(row => {
            if (category === "Teachers" || category === "Parents") {
              return {
                attributes: row.attributes,
                dateRange1TotalValue: row.dateRange1TotalValue,
                dateRange1StudentValue: row.dateRange1StudentValue,
                dateRange1AvgValue: row.dateRange1AvgValue,
                dateRange2TotalValue: row.dateRange2TotalValue,
                dateRange2StudentValue: row.dateRange2StudentValue,
                dateRange2AvgValue: row.dateRange2AvgValue
              };
            }  else {
              return {
                attributes: row.attributes,
                dateRange1TotalValue: row.dateRange1TotalValue,
                dateRange1AvgValue: row.dateRange1AvgValue,
                dateRange2TotalValue: row.dateRange2TotalValue,
                dateRange2AvgValue: row.dateRange2AvgValue
              };
            }
          });
        
          // Return the combined rows
          return [titleRow,emptyRow1,dateRangeRow, emptyRow,headerValue, ...dataRows];

    }
    else{
        const dataRows = tableInfo.map(row => {
            if (category === "Teachers" || category === "Parents") {
              return {
                attributes: row.attributes,
                dateRange1TotalValue: row.dateRange1TotalValue,
                dateRange1StudentValue: row.dateRange1StudentValue,
                dateRange1AvgValue: row.dateRange1AvgValue,
                dateRange2TotalValue: row.dateRange2TotalValue,
                dateRange2StudentValue: row.dateRange2StudentValue,
                dateRange2AvgValue: row.dateRange2AvgValue
              };
            } else  {
              return {
                stateDataValue: row.stateDataValue,
                districtDataValue: row.districtDataValue,
                attributes: row.attributes,
                dateRange1TotalValue: row.dateRange1TotalValue,
                dateRange1AvgValue: row.dateRange1AvgValue,
                dateRange2TotalValue: row.dateRange2TotalValue,
                dateRange2AvgValue: row.dateRange2AvgValue
              };
            } 
          });
          console.log(selectedFiltersWithNames)
          console.log(filterRows)
          console.log(dataRows)
        
          // Return the combined rows
          return [titleRow,emptyRow,dateRangeRow, emptyRow,...filterRows,emptyRow,headerValue,emptyRow, ...dataRows];

    }
  
  };



//   else if(category === "student" && subtype === "r1" && title.id === 1){
//     titleRow = { stateDataValue: title.value,districtDataValue: '',attributes: '', dateRange1TotalValue: '', dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
//     emptyRow1 = {stateDataValue:'',districtDataValue:'', attributes: '', dateRange1TotalValue: '',  dateRange1AvgValue: '', dateRange2TotalValue: '',  dateRange2AvgValue: '' };
//     dateRangeRow = {stateDataValue:'Date Range 1',districtDataValue:`${dateRange1Start.format("DD/MM/YYYY")}-${dateRange1End.format("DD/MM/YYYY")}`, attributes: '', dateRange1TotalValue: '',  dateRange1AvgValue: '', dateRange2TotalValue:'' , dateRange2StudentValue: '', dateRange2AvgValue: '' };
//     emptyRow = {stateDataValue:'',districtDataValue:'', attributes: '', dateRange1TotalValue: '',  dateRange1AvgValue: '', dateRange2TotalValue: '', dateRange2AvgValue: '' };
//     headerValue = {stateDataValue: 'State',districtDataValue: 'District',attributes: `${attributeHeading}`, dateRange1TotalValue: 'State Total Stakeholder Value',  dateRange1AvgValue: 'State Avg Students Value', dateRange2TotalValue: 'Pan India Total Stakeholder Value',  dateRange2AvgValue: 'Pan India Avg Students Value'}

// }

// else if (category === "student" && subtype === "r1" && title.id === 1) {
//     return {
//       stateDataValue: row.stateDataValue,
//       districtDataValue: row.districtDataValue,
//       attributes: row.attributes,
//       dateRange1TotalValue: row.dateRange1TotalValue,
//       dateRange1AvgValue: row.dateRange1AvgValue,
//       dateRange2TotalValue: row.dateRange2TotalValue,
//       dateRange2AvgValue: row.dateRange2AvgValue
//     };
//   }