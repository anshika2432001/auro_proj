import React from 'react';
import * as XLSX from 'xlsx';

const ExcelExport = (title, selectedFilters, attributeOptions, tableInfo, tableHeadings, category, dateRange1Start, dateRange1End, dateRange2Start, dateRange2End, attributeHeading, cardKey) => {
    console.log(category)
    const selectedFiltersWithNames = Object.fromEntries(
        Object.entries(selectedFilters).map(([key, value]) => {
          const options = attributeOptions[key];
          const selectedOption = options.find(option => (typeof option === 'object' ? option.id === value : option === value));
          return [key, typeof selectedOption === 'object' ? selectedOption.name : selectedOption];
        })
      );

    const titleRow = [
        [{ v: title.value, s: { alignment: { horizontal: 'center' }, font: { bold: true, sz: 14 } } }]
    ];

    const dateRangeRow = [
        [
            { v: 'Date Range 1', s: { alignment: { horizontal: 'center' } } },
            { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' } } },
            { v: '', s: { alignment: { horizontal: 'center' } } },
            { v: '', s: { alignment: { horizontal: 'center' } } }
        ],
        [
            { v: (dateRange2Start && dateRange2End) ? 'Date Range 2' : '', s: { alignment: { horizontal: 'center' } } },
            { v: (dateRange2Start && dateRange2End) ? `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}` : '', s: { alignment: { horizontal: 'center' } } },
            { v: '', s: { alignment: { horizontal: 'center' } } },
            { v: '', s: { alignment: { horizontal: 'center' } } }
        ]
    ];

    // Prepare the filters row
    const filterRow = [];
    Object.entries(selectedFiltersWithNames).forEach(([key, value]) => {
        if (value != undefined) {
            // const options = attributeOptions[key];
            // const selectedOption = options.find(
            //     (option) => (typeof option === 'object' ? option.id === value : option === value)
            // );
            // const displayValue = typeof selectedOption === 'object' ? selectedOption.name : selectedOption;
            filterRow.push({ v: key, s: { font: { bold: true } } });
            filterRow.push({ v: value });
            filterRow.push({ v: '' }); // Gap of one cell
        }
    });

    // Remove the last gap cell
    if (filterRow.length > 0) {
        filterRow.pop();
    }

    // Extract the state value from the filterRow for later use
    const stateFilter = selectedFilters['State'];
    const stateOption = attributeOptions['State']?.find(option => option.id === stateFilter);
    const stateValue = stateOption ? stateOption.name : '';

    // Define header data based on the category
    let headerData;
    let dataRows;

    if (cardKey == 4) {
        if (category === "Teachers" || category === "Parents") {
            headerData = [
                [
                    { v: `${attributeHeading}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${stateValue}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${stateValue}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${stateValue}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ],
                [
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[4], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[5], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ]
            ];
            dataRows = tableInfo.map(row => [
                row.attributes,
                row.dateRange1TotalValue,
                row.dateRange1StudentValue,
                row.dateRange1AvgValue,
                row.dateRange2TotalValue,
                row.dateRange2StudentValue,
                row.dateRange2AvgValue
            ]);
        } else {
            headerData = [
                [
                    { v: `${attributeHeading}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${stateValue}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${stateValue}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ],
                [
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ]
            ];
            dataRows = tableInfo.map(row => [
                row.attributes,
                row.dateRange1TotalValue,
                row.dateRange1AvgValue,
                row.dateRange2TotalValue,
                row.dateRange2AvgValue
            ]);
        }
    } else {
        if (category === "Teachers" || category === "Parents") {
            headerData = [
                [
                    { v: `${attributeHeading}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ],
                [
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[4], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[5], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ]
            ];
            dataRows = tableInfo.map(row => [
                row.attributes,
                row.dateRange1TotalValue,
                row.dateRange1StudentValue,
                row.dateRange1AvgValue,
                row.dateRange2TotalValue,
                row.dateRange2StudentValue,
                row.dateRange2AvgValue
            ]);
        } 
                else {
            headerData = [
                [
                    { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: 'District', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${attributeHeading}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: `${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ],
                [
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
                    { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
                ]
            ];
    
            dataRows = tableInfo.map(row => [
                row.stateDataValue,
                row.districtDataValue,
                row.attributes,
                row.dateRange1TotalValue,
                row.dateRange1AvgValue,
                row.dateRange2TotalValue,
                row.dateRange2AvgValue
            ]);
        }
       
    }

    const ws = XLSX.utils.aoa_to_sheet([
        ...titleRow,
        [],
        ...dateRangeRow,
        [],
        filterRow.length > 0 ? filterRow : [],
        [],
        ...headerData,
        ...dataRows
    ]);
    const headerStartRow = titleRow.length + 1 // Gap after titleRow
    + dateRangeRow.length + 1 // Gap after dateRangeRow
    + (filterRow.length > 0 ? 1 + 1 : 0); // Gap after filterRow, if it exists

               // Add cell merges to the worksheet
               if(cardKey == 4){
                if (category === "Teachers" || category === "Parents") {
                    ws['!merges'] = [
                      { s: { r: headerStartRow, c: 1 }, e: { r: headerStartRow, c: 3 } }, // Merge cells for "Date Range 1"
                      { s: { r: headerStartRow, c: 4 }, e: { r: headerStartRow, c: 6 } }  // Merge cells for "Date Range 2"
                    ];
                  } else {
                    ws['!merges'] = [
                      { s: { r: headerStartRow, c: 1 }, e: { r: headerStartRow, c: 2 } }, // Merge cells for "Date Range 1"
                      { s: { r: headerStartRow, c: 3 }, e: { r: headerStartRow, c: 4 } }  // Merge cells for "Date Range 2"
                    ];
                  }

               }
               else{
                if (category === "Teachers" || category === "Parents") {
                    ws['!merges'] = [
                      { s: { r: headerStartRow, c: 3 }, e: { r: headerStartRow, c: 5 } }, // Merge cells for "Date Range 1"
                      { s: { r: headerStartRow, c: 6 }, e: { r: headerStartRow, c: 8 } }  // Merge cells for "Date Range 2"
                    ];
                  } else {
                    ws['!merges'] = [
                      { s: { r: headerStartRow, c: 3 }, e: { r: headerStartRow, c: 4 } }, // Merge cells for "Date Range 1"
                      { s: { r: headerStartRow, c: 5 }, e: { r: headerStartRow, c: 6 } }  // Merge cells for "Date Range 2"
                    ];
                  }

               }
            
         

    ws['!cols'] = [
        { wch: 20 },
        { wch: 20 },
        { wch: 20 },
        { wch: 20 },
        { wch: 20 },
        { wch: 20 },
        { wch: 20 }
    ];

    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Report');
    XLSX.writeFile(wb, `${title.value}.xlsx`);
}




export default ExcelExport;





 //    else if(category == "student" && subtype=="r1" && title.id == 1){
        //    headerData = [
        //       [
        //         { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: 'District', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: `${attributeHeading}`, s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: 'State', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: 'Pan India', s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
        //       ],
        //       [
        //         { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: '', s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: tableHeadings[0], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: tableHeadings[1], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: tableHeadings[2], s: { alignment: { horizontal: 'center' }, font: { bold: true } } },
        //         { v: tableHeadings[3], s: { alignment: { horizontal: 'center' }, font: { bold: true } } }
        //       ]
        //     ];
          
           
        //      dataRows = tableInfo.map(row => [
        //       row.stateDataValue,
        //       row.districtDataValue,
        //       row.attributes,
        //       row.dateRange1TotalValue,
        //       row.dateRange1AvgValue,
        //       row.dateRange2TotalValue,
        //       row.dateRange2AvgValue
        //     ]);
          
            
        
        //    }