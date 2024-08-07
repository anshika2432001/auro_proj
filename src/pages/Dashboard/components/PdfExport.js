import React from 'react';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import AuroLogo from '../../../images/AuroLogo.png';
import AuroLogo1 from '../../../images/AuroLogo1.png';

const PdfExport = (
  title,
  selectedFilters,
  attributeOptions,
  tableInfo,
  tableHeadings,
  category,
  subtype,
  dateRange1Start,
  dateRange1End,
  dateRange2Start,
  dateRange2End,
  attributeHeading,
  cardKey
) => {
  const currentDate = new Date().toLocaleDateString();
  const doc = new jsPDF();
  const selectedFiltersWithNames = Object.fromEntries(
    Object.entries(selectedFilters).map(([key, value]) => {
      const options = attributeOptions[key];
      const selectedOption = options.find(option => (typeof option === 'object' ? option.id === value : option === value));
      return [key, typeof selectedOption === 'object' ? selectedOption.name : selectedOption];
    })
  );

  const stateValue = selectedFiltersWithNames['State'];

  const logoWidth = 30;
  const logoHeight = 15;
  const marginTop = 2; // Adjust this value as needed
  const titleMarginTop = marginTop + logoHeight + 10; // Add some space between logos and title

  doc.addImage(AuroLogo, 'PNG', 10, marginTop, logoWidth, logoHeight);
  doc.addImage(AuroLogo1, 'PNG', doc.internal.pageSize.getWidth() - 10 - logoWidth, marginTop, logoWidth, logoHeight);

  // Set the title
  doc.setFontSize(14);
  doc.text(`${title.value}`, 10, titleMarginTop);

  // Set the date ranges
  doc.setFontSize(10);
  let xPosition = 10;
  let yPosition = titleMarginTop + 10;
  if (dateRange1Start && dateRange1End) {
    doc.text(`Date Range 1: ${dateRange1Start.format('DD/MM/YYYY')} - ${dateRange1End.format('DD/MM/YYYY')}`, xPosition, yPosition);
    xPosition += doc.getTextWidth(`Date Range 1: ${dateRange1Start.format('DD/MM/YYYY')} - ${dateRange1End.format('DD/MM/YYYY')}`) + 10;
  }
  if (dateRange2Start && dateRange2End) {
    doc.text(`Date Range 2: ${dateRange2Start.format('DD/MM/YYYY')} - ${dateRange2End.format('DD/MM/YYYY')}`, xPosition, yPosition);
  }

  // Set the yPosition for the selected filters
  yPosition += 10;

  // Display selected filters
  doc.text('Selected Filters:', 10, yPosition);
  yPosition += 5;
  xPosition = 10;
  Object.entries(selectedFiltersWithNames).forEach(([key, value], index) => {
    if (value != undefined) {
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

  let head = [];
  let body = [];
  if (cardKey == 4) {
    if (category == 'Teachers' || category == 'Parents' || (category=="Students" && subtype =="r1" && (title.id == 12 || title.id == 13 || title.id == 10))) {
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
    } else {
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
  } else {
    if (category == 'Teachers' || category == 'Parents' || (category=="Students" && subtype =="r1" && (title.id == 12 || title.id == 13 || title.id == 10))) {
      head = [
        [
          { content: 'State', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'District', rowSpan: 2, styles: { halign: 'center' } },
          { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
          { content: `${dateRange1Start.format('DD/MM/YYYY')} - ${dateRange1End.format('DD/MM/YYYY')}`, colSpan: 3, styles: { halign: 'center' } },
          { content: `${dateRange2Start.format('DD/MM/YYYY')} - ${dateRange2End.format('DD/MM/YYYY')}`, colSpan: 3, styles: { halign: 'center' } }
        ],
        tableHeadings.map(heading => ({ content: heading, styles: { halign: 'center' } }))
      ];
      // Define the table body
      body = tableInfo.map(row => [
        { content: row.stateDataValue, styles: { halign: 'center' } },
        { content: row.districtDataValue, styles: { halign: 'center' } },
        { content: row.attributes, styles: { halign: 'center' } },
        { content: row.dateRange1TotalValue, styles: { halign: 'center' } },
        { content: row.dateRange1StudentValue, styles: { halign: 'center' } },
        { content: row.dateRange1AvgValue, styles: { halign: 'center' } },
        { content: row.dateRange2TotalValue, styles: { halign: 'center' } },
        { content: row.dateRange2StudentValue, styles: { halign: 'center' } },
        { content: row.dateRange2AvgValue, styles: { halign: 'center' } }
      ]);
    } else {
      head = [
        [
          { content: 'State', rowSpan: 2, styles: { halign: 'center' } },
          { content: 'District', rowSpan: 2, styles: { halign: 'center' } },
          { content: `${attributeHeading}`, rowSpan: 2, styles: { halign: 'center' } },
          { content: `${dateRange1Start.format('DD/MM/YYYY')} - ${dateRange1End.format('DD/MM/YYYY')}`, colSpan: 2, styles: { halign: 'center' } },
          { content: `${dateRange2Start.format('DD/MM/YYYY')} - ${dateRange2End.format('DD/MM/YYYY')}`, colSpan: 2, styles: { halign: 'center' } }
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

  let endY = 0;

  // Add the table
  doc.autoTable({
    head: head,
    body: body,
    startY: titleMarginTop + 30,
    theme: 'grid',
    headStyles: {
      halign: 'center',
      fillColor: [255, 255, 255],
      textColor: [0, 0, 0],
      lineWidth: 0.2,
      lineColor: [0, 0, 0]
    },
    bodyStyles: {
      halign: 'center',
      lineWidth: 0.2,
      lineColor: [0, 0, 0]
    },
    tableLineWidth: 0.2,
    tableLineColor: [0, 0, 0],
    didDrawPage: (data) => {
      endY = data.cursor.y; // Update the endY position after drawing the table

      // Add footer with download date, username, and page number
      const pageHeight = doc.internal.pageSize.getHeight();
      const pageWidth = doc.internal.pageSize.getWidth();
      const footerY = pageHeight - 10;

      doc.setFontSize(10);
      doc.text(`Downloaded by: Anshika`, 10, footerY);
      doc.text(`Date:  ${currentDate}`, pageWidth / 2, footerY, null, null, 'center');
      doc.text(`Page ${doc.internal.getNumberOfPages()}`, pageWidth - 10, footerY, null, null, 'right');
    }
  });

  // Check if we need to add a new page for the disclaimer
  if (endY + 20 > doc.internal.pageSize.getHeight()) {
    doc.addPage();
    endY = 10; // Reset the endY position
  }

  // Add the disclaimer text at the end
  const disclaimerText =
    'The data provided is intended solely for educational purposes and not for commercial use. Any modifications or repurposing require prior permission, and proper citation of the study and website is mandatory';
  const splitDisclaimerText = doc.splitTextToSize(disclaimerText, 180);
  doc.text(splitDisclaimerText, 10, endY + 10);

  // Save the PDF
  doc.save(`${title.value}.pdf`);
};

export default PdfExport;




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