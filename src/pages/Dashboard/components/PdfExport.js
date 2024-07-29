import React from 'react'
import jsPDF from 'jspdf';
import 'jspdf-autotable';

const PdfExport = (title,selectedFilters,attributeOptions,tableInfo,tableHeadings,category,dateRange1Start,dateRange1End,dateRange2Start,dateRange2End,attributeHeading,cardKey) => {
  const doc = new jsPDF();
  const selectedFiltersWithNames = Object.fromEntries(
    Object.entries(selectedFilters).map(([key, value]) => {
      const options = attributeOptions[key];
      const selectedOption = options.find(option => (typeof option === 'object' ? option.id === value : option === value));
      return [key, typeof selectedOption === 'object' ? selectedOption.name : selectedOption];
    })
  );
 
 const stateValue = selectedFiltersWithNames['State'];

  
   // Set the title
  doc.setFontSize(14);
  doc.text(`${title.value}`, 10, 10);
  
  // Set the date ranges
  doc.setFontSize(10);
  let xPosition = 10;
  let yPosition = 20; 
  if (dateRange1Start && dateRange1End) {
    doc.text(`Date Range 1: ${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, xPosition, yPosition);
    xPosition += doc.getTextWidth(`Date Range 1: ${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`) + 10;
  }
  if (dateRange2Start && dateRange2End) {
    doc.text(`Date Range 2: ${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, xPosition, yPosition);
  }

  // Set the yPosition for the selected filters
  yPosition += 10;

  // Display selected filters
  doc.text('Selected Filters:', 10, yPosition);
  yPosition += 5;
  xPosition = 10;
    Object.entries(selectedFiltersWithNames).forEach(([key, value], index) => {
      if(value != undefined){
      const filterText = `${key}: ${value}`;
      doc.text(filterText, xPosition, yPosition);
      const textWidth = doc.getTextWidth(filterText);
      xPosition += textWidth + 10; 
      if (xPosition > 180) { 
        xPosition = 10;
        yPosition += 10;
      }
    }
    });
  
    yPosition += 200;
    
      let head = [];
      let body = []
      if(cardKey == 4){
        if(category == "teacher" || category == "parent"){
          head = [
            [
              { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
              { content: `${stateValue}`, colSpan: 3, styles: { halign: 'center' } },
              { content: 'Pan India', colSpan: 3, styles: { halign: 'center' } }
            ],
            tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
          ];
           // Define the table body
         body = tableInfo.map(row => [
          { content: row.attributes, styles: { halign: 'center' } },
          { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange1StudentValue, styles: { halign: 'center' } },
          { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
          { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange2StudentValue, styles: { halign: 'center' } },
          { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
        ]);
        }
        else{
          head = [
            [
              { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
              { content: `${stateValue}`, colSpan: 2, styles: { halign: 'center' } },
              { content: 'Pan India', colSpan: 2, styles: { halign: 'center' } }
            ],
            tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
          ];
           // Define the table body
         body = tableInfo.map(row => [
          { content: row.attributes, styles: { halign: 'center' } },
          { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
          { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
        ]);
    
        }

      }else{
        if(category == "teacher" || category == "parent"){
          head = [
            [
              { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
              { content: `${dateRange1Start.format("DD/MM/YYYY")} -${dateRange1End.format("DD/MM/YYYY")}`, colSpan: 3, styles: { halign: 'center' } },
              { content: `${dateRange2Start.format("DD/MM/YYYY")} -${dateRange2End.format("DD/MM/YYYY")}`, colSpan: 3, styles: { halign: 'center' } }
            ],
            tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
          ];
           // Define the table body
         body = tableInfo.map(row => [
          { content: row.attributes, styles: { halign: 'center' } },
          { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange1StudentValue, styles: { halign: 'center' } },
          { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
          { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange2StudentValue, styles: { halign: 'center' } },
          { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
        ]);
        }
        else{
        
          head = [
            [
              { content: 'State', rowSpan: 2, styles: { halign: 'center' } },
              { content: 'District', rowSpan: 2, styles: { halign: 'center' } },
              { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
              { content: `${dateRange1Start.format("DD/MM/YYYY")} -${dateRange1End.format("DD/MM/YYYY")}`, colSpan: 2, styles: { halign: 'center' } },
              { content: `${dateRange2Start.format("DD/MM/YYYY")} -${dateRange2End.format("DD/MM/YYYY")}`, colSpan: 2, styles: { halign: 'center' } }
            ],
            tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
          ];
           // Define the table body
         body = tableInfo.map(row => [
          { content: row.stateDataValue, styles: { halign: 'center' } },
          { content: row.districtDataValue, styles: { halign: 'center' } },
          { content: row.attributes, styles: { halign: 'center' } },
          { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
          { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
          { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
        ]);
    
        }

      }
     
      
      doc.autoTable({
        head: head,
        body: body,
        startY: 40,
        theme: 'grid',
        headStyles: {
          halign: 'center', 
          fillColor: [255, 255, 255],
          textColor: [0, 0, 0], 
          lineWidth: 0.2, 
          lineColor: [0, 0, 0], 
        },
        bodyStyles: {
          halign: 'center', 
          lineWidth: 0.2, 
          lineColor: [0, 0, 0], 
        },
        tableLineWidth: 0.2, 
        tableLineColor: [0, 0, 0], 
      });
    
      // Save the PDF
      doc.save(`${title.value}.pdf`);
}

export default PdfExport

  //   // Set the title
  // const initialXPosition = 10; // Adjust this value to move everything to the right
  // doc.setFontSize(14);
  // doc.text(`${title.value}`, initialXPosition, 10);

  // // Set the date ranges
  // doc.setFontSize(10);
  // let xPosition = initialXPosition;
  // let yPosition = 20;
  // if (dateRange1Start && dateRange1End) {
  //   doc.text(`Date Range 1: ${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`, xPosition, yPosition);
  //   xPosition += doc.getTextWidth(`Date Range 1: ${dateRange1Start.format("DD/MM/YYYY")} - ${dateRange1End.format("DD/MM/YYYY")}`) + 10;
  // }
  // if (dateRange2Start && dateRange2End) {
  //   doc.text(`Date Range 2: ${dateRange2Start.format("DD/MM/YYYY")} - ${dateRange2End.format("DD/MM/YYYY")}`, xPosition, yPosition);
  // }

  // // Set the yPosition for the selected filters
  // yPosition += 10;

  // // Display selected filters
  // xPosition = initialXPosition;
  // doc.text('Selected Filters:', xPosition, yPosition);
  // yPosition += 5;
  // xPosition = initialXPosition;
  // Object.entries(selectedFiltersWithNames).forEach(([key, value]) => {
  //   if (value) {
  //     const filterText = `${key}: ${value}`;
  //     doc.text(filterText, xPosition, yPosition);
  //     const textWidth = doc.getTextWidth(filterText);
  //     xPosition += textWidth + 10;
  //     if (xPosition > 180) {
  //       xPosition = initialXPosition;
  //       yPosition += 10;
  //     }
  //   }
  // });

  // yPosition += 10;