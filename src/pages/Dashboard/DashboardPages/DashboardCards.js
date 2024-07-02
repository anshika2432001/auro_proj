import React, { useEffect, useState } from 'react';
import { Typography, Card, CardContent, Grid } from '@mui/material';
import { makeStyles } from '@mui/styles';
import axios from "../../../utils/axios";
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
    color: '#fff',
  },
  content: {
    paddingTop: '30px',
    height: '300px',
    overflowY: 'scroll',
  },
});

const colors = [
  'rgba(255,99,132,1)', 'rgba(255,159,64,1)', 'rgba(255,205,86,1)', 'rgba(75,192,192,1)', 'rgba(54,162,235,1)', 'rgba(153,102,255,1)', 'rgba(201,203,207,1)',
];

const capitalizeWords = (s) => s.replace(/\b\w/g, char => char.toUpperCase());

const ChartCard = ({ title, dataKey, data }) => {
  const classes = useStyles();

  return (
    <Card className="dashboard-card">
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
              domain={[0, 'dataMax']}
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
              width={160} // Increase the width to accommodate longer state names
              tick={{ fontSize: 12, textAnchor: 'end' }}
              interval={0}
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
  const [dashboardData, setDashboardData] = useState({
    studentCount: [],
    parentCount: [],
    teacherCount: [],
    schoolCount: []
  });

  useEffect(() => {
    getDashboardInfo();
  }, []);

  const getDashboardInfo = async () => {
    try {
      const res = await axios.get(`/dashboard-stats-data`);
      const result = res.data.result;

      const studentCount = result.studentCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        students: item.num_students,
        fill: colors[index % colors.length]
      }));
      const parentCount = result.parentCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        parents: item.num_parents,
        fill: colors[index % colors.length]
      }));
      const teacherCount = result.teacherCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        teachers: item.num_teachers,
        fill: colors[index % colors.length]
      }));
      const schoolCount = result.schoolCount.map((item, index) => ({
        state: capitalizeWords(item.state_name.toLowerCase()),
        schools: item.num_schools,
        fill: colors[index % colors.length]
      }));

      setDashboardData({ studentCount, parentCount, teacherCount, schoolCount });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Students" dataKey="students" data={dashboardData.studentCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Schools" dataKey="schools" data={dashboardData.schoolCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Teachers" dataKey="teachers" data={dashboardData.teacherCount} />
      </Grid>
      <Grid item xs={12} sm={12} md={6} lg={6}>
        <ChartCard title="Parents" dataKey="parents" data={dashboardData.parentCount} />
      </Grid>
    </Grid>
  );
};

export default DashboardCards;
