import React from 'react';
import { Typography, Card, CardContent,Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';

import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const useStyles = makeStyles({
  card: {
    width: '100%',
    margin: '5px',
    position: 'relative',
  },
  header: {
    backgroundColor: '#0948a6',
    padding: '10px',
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    boxSizing: 'border-box',
    zIndex: 2,
    color:'#fff'
  },
  content: {
    paddingTop: '30px',
    height: '300px',
    overflowY: 'scroll',
  },
});

const studentData = [
  { state: "Andhra Pradesh", students: 30 },
  { state: "Arunachal Pradesh", students: 50 },
  { state: "Assam", students: 70 },
  { state: "Bihar", students: 40 },
  { state: "Chhattisgarh", students: 90 },
  { state: "Goa", students: 60 },
  { state: "Gujarat", students: 30 },
  { state: "Haryana", students: 45 },
  { state: "Himachal Pradesh", students: 55 },
  { state: "Jharkhand", students: 70 },
  { state: "Karnataka", students: 40 },
  { state: "Kerala", students: 60 },
  { state: "Madhya Pradesh", students: 75 },
  { state: "Maharashtra", students: 50 },
  { state: "Manipur", students: 30 },
  { state: "Meghalaya", students: 40 },
  { state: "Mizoram", students: 50 },
  { state: "Nagaland", students: 60 },
  { state: "Odisha", students: 70 },
  { state: "Punjab", students: 50 },
  { state: "Rajasthan", students: 40 },
  { state: "Sikkim", students: 30 },
  { state: "Tamil Nadu", students: 60 },
  { state: "Telangana", students: 80 },
  { state: "Tripura", students: 50 },
  { state: "Uttar Pradesh", students: 90 },
  { state: "Uttarakhand", students: 60 },
  { state: "West Bengal", students: 70 },
  { state: "Andaman", students: 40 },
  { state: "Chandigarh", students: 50 },
  { state: "Daman and Diu", students: 30 },
  { state: "Delhi", students: 60 },
  { state: "Jammu and Kashmir", students: 40 },
  { state: "Ladakh", students: 20 },
  { state: "Lakshadweep", students: 10 },
  { state: "Puducherry", students: 30 },
];

const colors = [
  'rgba(255,99,132,1)','rgba(255,159,64,1)','rgba(255,205,86,1)','rgba(75,192,192,1)','rgba(54,162,235,1)','rgba(153,102,255,1)','rgba(201,203,207,1)',
];

const coloredStudentData = studentData.map((item, index) => ({
  ...item,
  fill: colors[index % colors.length]
}));

const ChartCard = ({ title, dataKey, data }) => {
  const classes = useStyles();
  return (
    <Card className={classes.card}>
      <div className={classes.header}>
        <Typography variant="h6">Number of {title}</Typography>
      </div>
      <CardContent className={classes.content}>
        <ResponsiveContainer width="100%" height={data.length * 20}>
          <BarChart
            data={data}
            layout="vertical"
            margin={{ top: 15, right: 20, bottom: 7 }}
          >
            <CartesianGrid strokeDasharray="0" />
            <XAxis 
              type="number"
              tick={{ fontSize: 12 }}
              interval={0}
              domain={[0, 100]}
              ticks={[0, 20, 40, 60, 80, 100]}
              label={{
                value: `Number of ${title}`,
                position: 'insideBottom',
                offset: -5,
                style: { textAnchor: 'middle' },
                x: '50%',
              }}
            />
            <YAxis
              type="category"
              dataKey="state"
              width={123}
              tick={{ fontSize: 12 , textAnchor: 'end'}}
              interval={0}
              //tickLine={false} 
              //tickFormatter={(value) => value.length > 18 ? value.slice(0, 8) + '...' : value} 
              label={{
                value: 'States',
                angle: -90,
                position: 'insideLeft',
                style: { textAnchor: 'middle' },
              }}
            />
            <Tooltip formatter={(value) => `${value}`} />
            <Bar dataKey={dataKey} isAnimationActive={false}>
              {data.map((entry, index) => (
                <Bar key={`bar-${index}`} fill={entry.fill} />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

const DashboardCards = () => {
  return (
    <>
      
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6} md={6} lg={6}>
          <ChartCard title="Students" dataKey="students" data={coloredStudentData} />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={6}>
          <ChartCard title="Schools" dataKey="students" data={coloredStudentData} />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={6}>
          <ChartCard title="Teachers" dataKey="students" data={coloredStudentData} />
        </Grid>
        <Grid item xs={12} sm={6} md={6} lg={6}>
          <ChartCard title="Parents" dataKey="students" data={coloredStudentData} />
        </Grid>
      </Grid>
    </>
  );
};

export default DashboardCards;

