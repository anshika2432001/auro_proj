import React from 'react';
import { Card, CardContent,Button,Typography,Grid, Box } from '@mui/material';
import OfferingsBackgrund from '../../images/OfferingsBackground.png'
import OfferingsIllustration from '../../images/OfferingsIllustration.png'
import BarChartIcon from '@mui/icons-material/BarChart';
import DashboardOutlinedIcon from '@mui/icons-material/DashboardOutlined';
import LockClockOutlinedIcon from '@mui/icons-material/LockClockOutlined';
import LocationOnOutlinedIcon from '@mui/icons-material/LocationOnOutlined';

const OfferingsPage = () =>{
  return (
    <div>
       <Card >
      <CardContent>
        <Grid container alignItems="center">
          <Grid item xs={12} sm={6} md={6} lg={6}>
          <Box
           sx={{position: 'relative',width:"500px",height:"400px"}}
           >
            <Box
            component = "img"
            src={OfferingsBackgrund}
            alt= "Image1"
            sx={{position:'absolute',
              top:'10px',left:'10px',width:"500px",
              height:"400px",
            }}

            />
              <Box
            component = "img"
            src={OfferingsIllustration}
            alt= "Image1"
            sx={{position:'absolute',
              top:'50px',left:'20px',width:"350px",
              height:"350px",
            }}

            />
             

           </Box>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="h2"  >
              Our Offerings
            </Typography>
            <Typography variant="body2"  >
            Encompasses data on education indicators with interactive data visualization through user-friendly dashboards to aid comparative analysis and benchmarking of educational practices and policies.
            </Typography>
            <Grid container spacing={2} sx={{mt:2}}>
              <Grid item xs={6}>
                <Typography variant="h6" display="flex" flexDirection="column">
                  <BarChartIcon sx={{color:"#4772D9"}}/>
                  Comprehensive data analysis
                </Typography>
                <Typography variant="body1" >
                •	<b>Extensive datasets</b> on education indicators<br></br>
•	<b>Analysis</b> of trends, contextual factors, practices and learning environment.

                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LocationOnOutlinedIcon sx={{color:"#4772D9"}}/>
                  Data Reporting 
                </Typography>
                <Typography variant="body1">
                • <b>Tailored reports</b> with detailed insights on state of learning and educational contexts .
                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <DashboardOutlinedIcon sx={{color:"#4772D9"}}/>
                  Data Visualization and Accessibility
                </Typography>
                <Typography variant="body1">
                • <b>Interactive</b> visualizations and <b>user-friendly</b> dashboards<br></br>
•	Regular <b>updates</b> and data <b>refreshes</b><br></br>
•	<b>Collaboration</b> to enhance data collection, analysis, and interpretation.

                </Typography>
              </Grid>
              <Grid item xs={6} >
                <Typography variant="h6" display="flex" flexDirection="column">
                  <LockClockOutlinedIcon sx={{color:"#4772D9"}}/>
                  Comparative Analysis and Benchmarking
                </Typography>
                <Typography variant="body1">
                •	<b>Benchmarking</b> and <b>comparison</b> of educational practices and policies<br></br>
•	Evaluation of <b>effectiveness</b> of education systems

                </Typography>
              </Grid>
             
            </Grid>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
      
    </div>
  )
}

export default OfferingsPage
